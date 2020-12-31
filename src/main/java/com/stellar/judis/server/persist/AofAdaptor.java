package com.stellar.judis.server.persist;

import com.stellar.judis.meta.Cache;
import com.stellar.judis.util.FileUtil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

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
        return null;
    }

    @Override
    public void snapshot(Cache cache) {
        File temp = FileUtil.createFile(tempFilePath);
        if (temp.exists()) {
            FileUtil.write(tempFilePath, JudisOperationBean.parseCache(cache));
            if (temp.length() > 0) {
                File file = new File(filePath);
                FileUtil.deleteFile(file);
                FileUtil.rename(tempFilePath, filePath);
            }
        } else {
            throw new RuntimeException("Snapshot failed");
        }
    }

    @Override
    public void parse(JudisOperationBean operationBean, String... args) {
        bufferList.add(operationBean.parse(args));
    }

    @Override
    public void update() {
        final List<String> cur = bufferList;
        bufferList = new LinkedList<>();
        FileUtil.append(filePath, cur);
    }
}
