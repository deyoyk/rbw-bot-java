package com.deyo.rbw.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class AtomicFileUtil {
    /**
     * Atomically writes data to a file by writing to a temp file and renaming.
     * @param target The target file to write to.
     * @param data The byte array to write.
     * @throws IOException If an I/O error occurs.
     */
    public static void atomicWrite(File target, byte[] data) throws IOException {
        File temp = new File(target.getAbsolutePath() + ".tmp");
        try (FileOutputStream fos = new FileOutputStream(temp)) {
            fos.write(data);
            fos.flush();
            fos.getFD().sync();
        }
        Files.move(temp.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }
}
