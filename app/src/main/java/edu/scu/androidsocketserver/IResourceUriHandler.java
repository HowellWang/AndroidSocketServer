package edu.scu.androidsocketserver;

import java.io.IOException;

/**
 * Created by yuhaowang on 5/18/17.
 */

public interface IResourceUriHandler {
    boolean accept(String uri);
    void postHandle(String uri, HttpContext httpContext) throws IOException;

}
