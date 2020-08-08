package com.bullhead.nafees.android.util;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * Provide ID for uniquely identifying app installations.
 * Read more about this
 * <p>
 * {@link <a href="https://android-developers.googleblog.com/2011/03/identifying-app-installations.html">here</a>}
 */
public final class Installation {
    private static final String INSTALLATION = "INSTALLATION";
    private static       String sID          = null;

    private Installation() {
        throw new IllegalStateException("Nah! 0xcafebabe");
    }

    /**
     * Get the unique installation id
     *
     * @param context context
     * @return unique id
     */
    public synchronized static String id(@NonNull Context context) {
        if (sID == null) {
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists())
                    writeInstallationFile(installation);
                sID = readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sID;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f     = new RandomAccessFile(installation, "r");
        byte[]           bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String           id  = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }
}
