package com.stellar.judis.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/31 10:14
 */
public class FileUtil {
    public static Path joinPath(String first, String... more) {
        return Paths.get(first, more);
    }
    /**
     * 从文件读取
     * @param path
     * @return
     */
    public static byte[] readBytes(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> readLines(Path path) {
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void readLinesByChannel(Path path, Charset charset, Consumer<String> consumer) {
        try (FileChannel channel = FileChannel.open(path)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
            ByteBuffer stringBuffer = ByteBuffer.allocate(1024);
            int bytesRead = channel.read(buffer);
            while (bytesRead != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    if ((b == 10 || b == 13) && stringBuffer.position() > 0) {
                        stringBuffer.flip();
                        final String line = charset.decode(stringBuffer).toString();
                        consumer.accept(line);
                        stringBuffer.clear();
                    } else {
                        if (b == 10 || b == 13) {
                            continue;
                        }
                        if (stringBuffer.hasRemaining()) {
                            stringBuffer.put(b);
                        } else {
                            final int capacity = stringBuffer.capacity();
                            byte[] newBuffer = new byte[capacity * 2];
                            System.arraycopy(stringBuffer.array(), 0, newBuffer, 0, capacity);
                            stringBuffer = (ByteBuffer)ByteBuffer.wrap(newBuffer).position(capacity);
                            stringBuffer.put(b);
                        }
                    }
                }
                buffer.clear();
                bytesRead = channel.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean createFile(Path path) {
        try {
            if (Files.exists(path)) {
                return true;
            }
            Path dir = path.getParent();
            if (!Files.exists(dir)) {
                Files.createDirectory(dir);
            }
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 写入文件
     * @param path
     * @param lines
     * @param charset
     * @param openOptions
     */
    public static void write(Path path, Iterable<? extends CharSequence> lines, Charset charset, OpenOption... openOptions) {
        try {
            Files.write(path, lines, charset, openOptions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(Path path, String content, Charset charset) {
        write(path, Collections.singleton(content), charset);
    }

    public static void write(Path path, List<String> lines, Charset charset) {
        write(path, lines, charset);
    }

    public static void append(Path path, String content, Charset charset) {
        write(path, Collections.singleton(content), charset, StandardOpenOption.APPEND);
    }

    public static void appendLines(Path path, List<String> lines, Charset charset) {
        write(path, lines, charset, StandardOpenOption.APPEND);
    }

    /**
     * 拷贝文件
     * @param fromPath
     * @param toPath
     */
    public static void copyIfAbsent(Path fromPath, Path toPath) {
        try {
            Files.copy(fromPath, toPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copy(Path fromPath, Path toPath) {
        try {
            Files.copy(fromPath, toPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteIfExists(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
