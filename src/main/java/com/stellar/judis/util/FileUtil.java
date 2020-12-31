package com.stellar.judis.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/31 10:14
 */
public class FileUtil {

    private FileUtil() {}

    public static void write(final String filePath,
                             final Iterable<? extends CharSequence> lines,
                             final String charset,
                             OpenOption... openOptions) {
        try {
            CharsetEncoder encoder = Charset.forName(charset).newEncoder();
            final Path path = Paths.get(filePath);
            Path parentPath = path.getParent();
            if (parentPath != null) {
                File parent = parentPath.toFile();
                if (!parent.exists()) {
                    parent.mkdir();
                }
            }
            OutputStream out = path.getFileSystem().provider().newOutputStream(path, openOptions);
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, encoder))) {
                for (CharSequence line: lines) {
                    if (line != null) {
                        writer.append(line);
                        writer.newLine();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Write content to file：" + filePath + " failed");
        }
    }

    public static void write(final String filePath, final CharSequence line, OpenOption... openOptions) {
        write(filePath, Collections.singletonList(line), "UTF-8", openOptions);
    }

    public static void write(final String filePath, final Iterable<? extends CharSequence> lines, OpenOption... openOptions) {
        write(filePath, lines, "UTF-8", openOptions);
    }

    public static List<String> getFileContentEachLine(File file) {
        return getFileContentEachLine(file, 0);
    }

    public static List<String> getFileContentEachLine(File file, int initLine) {
        List<String> contentList = new LinkedList<>();
        if (!file.exists()) {
            return contentList;
        }
        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            int lineNo = 0;
            while (lineNo < initLine) {
                lineNo++;
                reader.readLine();
            }
            String dataEachLine;
            while ((dataEachLine = reader.readLine()) != null) {
                lineNo++;
                if (Objects.equals("", dataEachLine)) continue;
                contentList.add(dataEachLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Get file：" + file.getPath() + " content failed");
        }
        return contentList;
    }

    public static File createFile(final String filePath) {
        if (filePath == null || filePath.equals("")) throw new RuntimeException("path can't be empty");
        File file = new File(filePath);
        if (file.exists())
            return file;
        File dir = file.getParentFile();
        if (!dir.exists() && !dir.mkdirs())
            throw new RuntimeException("Parent file create failed");
        try {
            boolean createFile = file.createNewFile();
            if (!createFile) throw new RuntimeException("File：" + filePath + " create failed");
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("File：" + filePath + " create failed");
        }
    }

    public static void deleteFile(final File file) {
        if (file != null && file.exists()) {
            boolean res = file.delete();
            if (!res) throw new RuntimeException("File：" + file.getPath() + " delete failed");
        }
    }

    public static boolean rename(final String sourcePath, final String targetPath) {
        File sourceFile = new File(sourcePath);
        File targetFile = new File(targetPath);
        return sourceFile.renameTo(targetFile);
    }



    public static boolean isEmpty(final String filePath) {
        if (filePath == null || filePath.equals("")) return true;
        File file = new File(filePath);
        return file.length() <= 0;
    }

    public static void append(final String filePath, final String line) {
        write(filePath, line, StandardOpenOption.APPEND);
    }

    public static void append(final String filePath, final Iterable<? extends CharSequence> lines) {
        write(filePath, lines, StandardOpenOption.APPEND);
    }
}
