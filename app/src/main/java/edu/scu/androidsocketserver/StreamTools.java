package edu.scu.androidsocketserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yuhaowang on 5/18/17.
 */

public class StreamTools {
    public static String readLine(InputStream nis) throws IOException {
        StringBuffer sb = new StringBuffer();
        int a = 0, b = 0;
        while (b != -1 && !(a == '\r' && b == '\n')) {
            a = b;
            b = nis.read();
            sb.append((char) b);
        }
        if (sb.length() == 0) {
            return null;
        }
        return sb.toString();
    }

    public static String readInput(InputStream inputStream, long length) throws IOException {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        int b;
        while (count < length) {
            count++;
            b = inputStream.read();
            sb.append((char) b);
        }
        return sb.toString();
    }

    public static byte[] readRawFromStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[10240];
        int read;
        while ((read = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, read);
        }
        return outputStream.toByteArray();
    }
}
