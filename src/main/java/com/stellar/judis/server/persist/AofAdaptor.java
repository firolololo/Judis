package com.stellar.judis.server.persist;

import com.stellar.judis.meta.Cache;
import com.stellar.judis.meta.JudisElement;
import com.stellar.judis.util.FileUtil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/30 16:54
 */
public class AofAdaptor implements PersistAdaptor {
    private List<String> bufferList = new LinkedList<>();
    private static final int BUFFER_MAX_CAPACITY = 2048;

    private final String dataPath = "." + File.separator + "data";
    private String fileName = "default.aof";
    private String filePath;
    private String tempFilePath;
    private AtomicBoolean isSnapshot = new AtomicBoolean(false);

    public AofAdaptor() {
        init();
    }

    public AofAdaptor(String fileName) {
        this.fileName = fileName;
        init();
    }

    private void init() {
        filePath = dataPath + File.separator + fileName;
        tempFilePath = filePath + ".temp";
        File file = FileUtil.createFile(filePath);
        if (!file.exists())
            throw new RuntimeException("AofAdaptor init failed");
    }

    @Override
    public Cache load() {
        if (!FileUtil.isEmpty(filePath)) {
            File file = new File(filePath);
            List<String> records = FileUtil.getFileContentEachLine(file);
            Cache cache = new Cache();
            for (String record: records) {
                JudisOperationBean.execute(cache, record);
            }
            return cache;
        }
        return new Cache();
    }

    @Override
    public void snapshot(Cache cache) {
        File temp = FileUtil.createFile(tempFilePath);
        if (temp.exists() && isSnapshot.compareAndSet(false, true)) {
            try {
                FileUtil.write(tempFilePath, JudisOperationBean.parseCache(cache));
                if (temp.length() > 0) {
                    File file = new File(filePath);
                    FileUtil.deleteFile(file);
                    FileUtil.rename(tempFilePath, filePath);
                }
            } finally {
                isSnapshot.compareAndSet(true, false);
            }
        } else {
            throw new RuntimeException("Snapshot failed");
        }
    }

    @Override
    public void parse(JudisOperationBean operationBean, String className, JudisElement element, String... args) {
        bufferList.add(operationBean.parse(className, element, args));
        if (bufferList.size() >= BUFFER_MAX_CAPACITY)
            update();
    }

    @Override
    public int update() {
        if (!isSnapshot.get()) {
            final List<String> cur = new LinkedList<>(bufferList);
            if (cur.size() > 0) {
                bufferList = new LinkedList<>();
                FileUtil.append(filePath, cur);
                return cur.size();
            }
            return 0;
        }
        return -1;
    }
}
