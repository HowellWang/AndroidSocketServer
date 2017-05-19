package edu.scu.androidsocketserver;

/**
 * Created by yuhaowang on 5/18/17.
 */

public class WebConfiguration {
    private int port;
    private int maxParallels;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxParallels() {
        return maxParallels;
    }

    public void setMaxParallels(int maxParallels) {
        this.maxParallels = maxParallels;
    }
}
