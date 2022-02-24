package com.kcang.template;

import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

import java.util.Map;

public class MyHttpRequest extends DefaultHttpRequest {
    public MyHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri) {
        super(httpVersion, method, uri);
    }

    public MyHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri, boolean validateHeaders) {
        super(httpVersion, method, uri, validateHeaders);
    }

    public MyHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri, HttpHeaders headers) {
        super(httpVersion, method, uri, headers);
    }

    @Override
    public String toString(){
        StringBuilder buf = new StringBuilder();
        String separator = "\r\n";

        buf.append(this.method());
        buf.append(' ');
        buf.append(this.uri());
        buf.append(' ');
        buf.append(this.protocolVersion());
        buf.append(separator);

        for (Map.Entry<String, String> e: this.headers()) {
            buf.append(e.getKey());
            buf.append(": ");
            buf.append(e.getValue());
            buf.append(separator);
        }

        buf.append(separator);

        return buf.toString();
    }
}
