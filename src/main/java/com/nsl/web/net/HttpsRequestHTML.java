package com.nsl.web.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;

import com.nsl.web.data.DataContainer;
import com.nsl.web.data.DataContainer.DataType;
import com.nsl.web.data.HTMLContainer;

/**
 * Given URL from the client, an object responsible to send request for HTML page
 * to the URL.
 * 
 * @author PGD
 */
class HttpsRequestHTML extends HttpsRequest {
    private static final int CONNECT_TIMEOUT = 1500;

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
    protected void storeData(DataContainer container, HttpsURLConnection conn, boolean isSuccess) throws IOException {
        BufferedReader br = null;
        try {
            if (isSuccess) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            
            HTMLContainer result = new HTMLContainer(sb.toString());
            container.setData(result, DataType.HTML);
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