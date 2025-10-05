package org.acra.util;

import org.acra.ACRA;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Responsible for sending Toasts under all circumstances.
 * <p/>
 * @author William Ferguson
 * @since 4.3.0
 */
public final class ToastSender {

    /**
     * Sends a Toast and ensures that any Exception thrown during sending is handled.
     *
     * @param context           Application context.
     * @param text   Id of the resource to send as the Toast message.
     * @param toastLength       Length of the Toast.
     */
    public static void sendToast(Context context, String text, int toastLength) {
        try {
            Toast.makeText(context, text, toastLength).show();
        } catch (RuntimeException e) {
            Log.e(ACRA.LOG_TAG, "Could not send crash Toast", e);
        }
    }

    /**
     * Sends a Toast and ensures that any Exception thrown during sending is handled.
     *
     * @param context           Application context.
     * @param resourceId   Id of the resource to send as the Toast message.
     * @param toastLength       Length of the Toast.
     */
    public static void sendToast(Context context, int resourceId, int toastLength) {
        try {
            Toast.makeText(context, resourceId, toastLength).show();
        } catch (RuntimeException e) {
            Log.e(ACRA.LOG_TAG, "Could not send crash Toast", e);
        }
    }
}
