package hanu.a2_2001040208.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Helper {
    public static final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public static Bitmap downloadImage(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            Log.e("error", e.getLocalizedMessage());
        }
        return null;
    }

    public static void toastMessage(Context context, String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
        handler.postDelayed(toast::cancel, 1000);
    }
}
