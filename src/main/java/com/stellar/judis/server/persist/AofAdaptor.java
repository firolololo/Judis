package com.stellar.judis.server.persist;

import com.stellar.judis.meta.Cache;
import com.stellar.judis.meta.JudisElement;
import com.stellar.judis.util.FileUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.shaded.org.jctools.queues.MpscArrayQueue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/30 16:54
 */
public class AofAdaptor implements PersistAdaptor {
    private static final InternalLogger LOG = InternalLoggerFactory.getInstance(AofAdaptor.class);
    private static final int BUFFER_MAX_CAPACITY = 2048;
    private MpscArrayQueue<String> bufferList = new MpscArrayQueue<>(BUFFER_MAX_CAPACITY);

    private final String dataPath = "." + File.separator + "data";
    private String fileName = "default.aof";
    private String tempFileName = "default.aof.temp";
    private Path filePath;
    private Path tempFilePath;
    private AtomicBoolean isSnapshot = new AtomicBoolean(false);
    private AtomicBoolean isUpdate = new AtomicBoolean(false);

    public AofAdaptor() {
        init();
    }

    public AofAdaptor(String fileName) {
        this.fileName = fileName;
        init();
    }

    private void init() {
        filePath = FileUtil.joinPath(dataPath, fileName);
        tempFilePath = FileUtil.joinPath(dataPath, tempFileName);
        if (!FileUtil.createFile(filePath))
            throw new RuntimeException("AofAdaptor init failed");
    }

    @Override
    public Cache load() {
        if (Files.exists(filePath)) {
            long startTime = System.currentTimeMillis();
            Cache cache = new Cache();
            FileUtil.readLinesByChannel(filePath, StandardCharsets.UTF_8, (record) -> JudisOperationBean.execute(cache, record));
            long endTime = System.currentTimeMillis();
            float excTime= (float)(endTime - startTime) / 1000;
            LOG.info("load cost time:{}s", excTime);
            return cache;
        }
        return new Cache();
    }

    @Override
    public void snapshot(Cache cache) {
        if (!Files.exists(tempFilePath)) {
            FileUtil.createFile(tempFilePath);
        }
        if (Files.exists(tempFilePath) && isSnapshot.compareAndSet(false, true)) {
            try {
                FileUtil.appendLines(tempFilePath, JudisOperationBean.parseCache(cache), StandardCharsets.UTF_8);
                FileUtil.copy(tempFilePath, filePath);
                FileUtil.deleteIfExists(tempFilePath);
            } finally {
                isSnapshot.compareAndSet(true, false);
            }
        } else {
            throw new RuntimeException("Snapshot failed");
        }
    }

    @Override
    public void parse(JudisOperationBean operationBean, String className, JudisElement element, String... args) {
        String hist = operationBean.parse(className, element, args);
        // 插入记录失败
        if (!bufferList.offer(hist)) {
            FileUtil.append(filePath, hist, StandardCharsets.UTF_8);
            update();
        }
    }

    @Override
    public int update() {
        if (!isSnapshot.get() && isUpdate.compareAndSet(false, true)) {
            try {
                return bufferList.drain(line -> FileUtil.append(filePath, line, StandardCharsets.UTF_8));
            } finally {
                isUpdate.compareAndSet(true, false);
            }
        }
        return -1;
    }
}
