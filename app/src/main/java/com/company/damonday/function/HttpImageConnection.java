package com.company.damonday.function;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by lamtaklung on 17/5/15.
 */
//decode the URL using HTTPConnection
public class HttpImageConnection  {

    private String mURL;
    private int width;

    public HttpImageConnection(String url, int width){
        mURL = url;
        this.width = width;
    }

    public Bitmap Connection(){
        URL url = null;

        try {
            int inWidth = 0;
            int inHeight = 0;
            url = new URL(mURL);
            URLConnection conn = url.openConnection();


            HttpURLConnection httpConn = (HttpURLConnection)conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();



            // Decode image size
            InputStream inputStream = httpConn.getInputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();
            inputStream = null;

            // save width and height
            inWidth = options.outWidth;
            inHeight = options.outHeight;

            // decode full image pre-resized
            inputStream = httpConn.getInputStream();
            options = new BitmapFactory.Options();
            // calc rought re-size (this is no exact resize)
            options.inSampleSize = Math.max(inWidth/width, inHeight/174);
            // decode full image

            Bitmap roughBitmap = BitmapFactory.decodeStream(inputStream, null, options);

            // calc exact destination size
            Matrix m = new Matrix();
            RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            RectF outRect = new RectF(0, 0, width, 174);
            m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
            float[] values = new float[9];
            m.getValues(values);

            // resize bitmap
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);

            // save image
            try
            {
                FileOutputStream out = new FileOutputStream(mURL);
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                return resizedBitmap;
            }
            catch (Exception e)
            {
                Log.e("Image", e.getMessage(), e);
                return null;
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.i("loadingImg", e.toString());
            return null;
        }
    }
}
