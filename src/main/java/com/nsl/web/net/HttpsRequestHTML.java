package com.nsl.web.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;

import com.nsl.web.data.Buffer;
import com.nsl.web.data.DataContainer;
import com.nsl.web.data.HTMLContainer;

/**
 * Given URL from the client, an object responsible to send request for HTML page
 * to the URL.
 * 
 * @author PGD
 */
public class HttpsRequestHTML extends HttpsRequest<String> {
    private static final int CONNECT_TIMEOUT = 10000;

    public HttpsRequestHTML(String url) throws IOException {
        super(url);
    }

    @Override
    protected void setConnection(HttpsURLConnection conn) throws IOException {
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "text/html");
        conn.setConnectTimeout(CONNECT_TIMEOUT);
    }

    @Override
    protected DataContainer<String> storeData(HttpsURLConnection conn, boolean isSuccess) throws IOException {
        BufferedReader br = null;
        try {
            if (isSuccess) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            
            HTMLContainer htmlContainer = new HTMLContainer();
            String line;
            while ((line = br.readLine()) != null) {
                htmlContainer.addBuffer(new Buffer<>(line, line.length()));
            }
            return htmlContainer;
        } finally {
            close(br);
        }
    }
    
    private void close(BufferedReader br) throws IOException {
        if (br != null) {
            br.close();
        }
    }
}