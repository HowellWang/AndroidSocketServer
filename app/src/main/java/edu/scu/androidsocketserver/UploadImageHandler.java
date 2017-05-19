package edu.scu.androidsocketserver;


import android.app.Activity;
import android.os.Environment;
import android.util.Log;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;


/**
 * Created by yuhaowang on 5/18/17.
 */

public class UploadImageHandler implements IResourceUriHandler {

    private String acceptPrefix = "/upload_image/";
    Activity activity;

    public UploadImageHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean accept(String uri) {
        return uri.startsWith(acceptPrefix);
    }


    @Override
    public void postHandle(String uri, HttpContext httpContext) {
        String tmpPath = Environment.getExternalStorageDirectory().getPath() + "/test_upload.jpg";
        long totalLength = Long.parseLong(httpContext.getRequestHeaderValue("Content-Length").trim());
        try {
            FileOutputStream fos = new FileOutputStream(tmpPath);
            InputStream nis = httpContext.getUnderlySocket().getInputStream();
            byte[] buffer = new byte[10240];
            int nReaded = 0;
            long nLeftLength = totalLength;
            while (nLeftLength > 0 && (nReaded = nis.read(buffer)) > 0) {
                fos.write(buffer, 0, nReaded);
                nLeftLength -= nReaded;
            }
            fos.close();
            OutputStream nos = httpContext.getUnderlySocket().getOutputStream();
            PrintStream printer = new PrintStream(nos);
            printer.println("HTTP/1.1 200 OK");
            printer.println();
            onImageLoaded(tmpPath);
        } catch (FileNotFoundException e) {
            Log.d("spy", "FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("spy", "IOException");
            e.printStackTrace();
        }
    }


    String tag = "";

    public void onImageLoaded(String path) {
        Log.i(tag, "path=" + path);
    }
}