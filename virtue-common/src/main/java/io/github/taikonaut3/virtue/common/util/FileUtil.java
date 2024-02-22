package io.github.taikonaut3.virtue.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public interface FileUtil {

    Logger logger = LoggerFactory.getLogger(FileUtil.class);

    static void writeLineFile(String content, File targetFile) {
            if (content.isEmpty()) {
                return;
            }
            if (!targetFile.exists()) {
                createFileWithParentDirectory(targetFile.getAbsolutePath());
            }
            try (RandomAccessFile file = new RandomAccessFile(targetFile, "rw");
                 FileChannel channel = file.getChannel(); FileLock lock = channel.lock()) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = file.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String fileContents = sb.toString();

                if (!fileContents.contains(content)) {
                    fileContents += content + "\n";
                }
                file.setLength(0);
                file.write(fileContents.getBytes());
            } catch (Exception e) {
                logger.error("Write File fail file:{}", targetFile);
            }
    }

    static void createFileWithParentDirectory(String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        createParentDirectories(parentDir);
        try {
            boolean success = file.createNewFile();
            if (!success) {
                logger.warn("Create File Fail,path: {}", filePath);
            }
        } catch (Exception e) {
            logger.error("Create File Fail,path: {}", filePath);
        }
    }

    private static void createParentDirectories(File directory) {
        if (!directory.exists()) {
            boolean success = directory.mkdirs();
            if (!success) {
                logger.warn("Create Directory Fail,directory: {}", directory);
            }
        }
    }
}
