package edu.scu.androidsocketserver;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuhaowang on 5/18/17.
 */

public class HttpContext {
    private Socket underlySocket;
    Map<String, String> heard = new HashMap<>();
    private String type;

    public void setUnderlySocket(Socket underlySocket) {
        this.underlySocket = underlySocket;
    }

    /**
     * @param key
     * @param value
     */

    public void addRequestHeader(String key, String value) {
        heard.put(key, value);
    }

    public Socket getUnderlySocket() {
        return underlySocket;
    }

    public String getRequestHeaderValue(String key) {
        return heard.get(key);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}