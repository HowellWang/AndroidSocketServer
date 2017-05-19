package edu.scu.androidsocketserver;

import android.util.Log;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Created by yuhaowang on 5/18/17.
 */

public class GetPostHandler implements IResourceUriHandler {
    private String acceptPrefix = "/get/";
    String tag = "GetPostHandler";

    private String boundary = null;

    private int contentLength = 0;

    @Override
    public boolean accept(String uri) {
        return uri.startsWith(acceptPrefix);
    }

    @Override
    public void postHandle(String uri, HttpContext httpContext) throws IOException {

        Log.i("GetPostHandler", "http 4 body  type=" + httpContext.getType() + "  uri =" + uri);
        long totalLength = Long.parseLong(httpContext.getRequestHeaderValue("Content-Length").trim());
        if ("GET".equals(httpContext.getType())) {//index.jsp?id=100&op=bind
        } else if ("POST".equals(httpContext.getType())) {
            String data = StreamTools.readInput(httpContext.getUnderlySocket().getInputStream(), totalLength);
            Log.i(tag, "data  =" + data);
        } else if ("HEAD".equals(httpContext.getType())) {
        } else {
            String data = StreamTools.readInput(httpContext.getUnderlySocket().getInputStream(), totalLength);
            Log.i(tag, "data  =" + data);
        }
        OutputStream outputStream = httpContext.getUnderlySocket().getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.println("HTTP/1.1 200 OK");
        printWriter.println();
        printWriter.println("from result handle");
        printWriter.flush();
    }

    @SuppressWarnings("deprecation")
    private void doPost(InputStream inputStream, OutputStream out) throws Exception {
        DataInputStream reader = new DataInputStream(inputStream);
        String line = reader.readLine();
        while (line != null) {
            System.out.println(line);
            line = reader.readLine();
            if ("".equals(line)) {
                break;
            } else if (line.indexOf("Content-Length") != -1) {
                this.contentLength = Integer.parseInt(line.substring(line.indexOf("Content-Length") + 16));
            }

            else if (line.indexOf("multipart/form-data") != -1) {

                this.boundary = line.substring(line.indexOf("boundary") + 9);
                this.doMultiPart(reader, out);
                return;
            }
        }

        System.out.println("begin reading posted data......");
        String dataLine = null;

        byte[] buf = {};
        int size = 0;
        if (this.contentLength != 0) {
            buf = new byte[this.contentLength];
            while (size < this.contentLength) {
                int c = reader.read();
                buf[size++] = (byte) c;

            }
            System.out.println("The data user posted: " + new String(buf, 0, size));
        }

        String response = "";
        response += "HTTP/1.1 200 OK\n";
        response += "Server: Sunpache 1.0\n";
        response += "Content-Type: text/html\n";
        response += "Last-Modified: Mon, 11 Jan 1998 13:23:42 GMT\n";
        response += "Accept-ranges: bytes";
        response += "\n";
        String body = "<html><head><title>test server</title></head><body><p>post ok:</p>" + new String(buf, 0, size) + "</body></html>";
        System.out.println(body);
        out.write(response.getBytes());
        out.write(body.getBytes());
        out.flush();
        reader.close();
        out.close();
        System.out.println("request complete.");
    }

    @SuppressWarnings("deprecation")
    private void doMultiPart(DataInputStream reader, OutputStream out) throws Exception {
        System.out.println("doMultiPart ......");
        String line = reader.readLine();
        while (line != null) {
            System.out.println(line);
            line = reader.readLine();
            if ("".equals(line)) {
                break;
            } else if (line.indexOf("Content-Length") != -1) {
                this.contentLength = Integer.parseInt(line.substring(line.indexOf("Content-Length") + 16));
                System.out.println("contentLength: " + this.contentLength);
            } else if (line.indexOf("boundary") != -1) {

                this.boundary = line.substring(line.indexOf("boundary") + 9);
            }
        }
        System.out.println("begin get data......");

        if (this.contentLength != 0) {

            byte[] buf = new byte[this.contentLength];
            int totalRead = 0;
            int size = 0;
            while (totalRead < this.contentLength) {
                size = reader.read(buf, totalRead, this.contentLength - totalRead);
                totalRead += size;
            }

            String dataString = new String(buf, 0, totalRead);
            System.out.println("the data user posted:\n" + dataString);
            int pos = dataString.indexOf(boundary);

            pos = dataString.indexOf("\n", pos) + 1;
            pos = dataString.indexOf("\n", pos) + 1;
            pos = dataString.indexOf("\n", pos) + 1;
            pos = dataString.indexOf("\n", pos) + 1;

            int start = dataString.substring(0, pos).getBytes().length;
            pos = dataString.indexOf(boundary, pos) - 4;

            int end = dataString.substring(0, pos).getBytes().length;

            int fileNameBegin = dataString.indexOf("filename") + 10;
            int fileNameEnd = dataString.indexOf("\n", fileNameBegin);
            String fileName = dataString.substring(fileNameBegin, fileNameEnd);

            if (fileName.lastIndexOf("\\") != -1) {
                fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
            }
            fileName = fileName.substring(0, fileName.length() - 2);
            OutputStream fileOut = new FileOutputStream("c:\\" + fileName);
            fileOut.write(buf, start, end - start);
            fileOut.close();
            fileOut.close();
        }
        String response = "";
        response += "HTTP/1.1 200 OK\n";
        response += "Server: Sunpache 1.0\n";
        response += "Content-Type: text/html\n";
        response += "Last-Modified: Mon, 11 Jan 1998 13:23:42 GMT\n";
        response += "Accept-ranges: bytes";
        response += "\n";
        out.write("<html><head><title>test server</title></head><body><p>Post is ok</p></body></html>".getBytes());
        out.flush();
        reader.close();
        System.out.println("request complete.");
    }

}

