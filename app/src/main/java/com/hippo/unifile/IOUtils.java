package com.hippo.unifile;

import java.io.Closeable;
import java.io.IOException;

class IOUtils {

    /**
     * Close the closeable stuff. Don't worry about anything.
     *
     * @param is the closeable stuff
     */
    public static void closeQuietly(Closeable is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }
}
