package com.nsl.web.net;

import java.io.BufferedInputStream;
import java.io.IOException;
import javax.net.ssl.HttpsURLConnection;

import com.nsl.web.data.DataContainer;
import com.nsl.web.data.ImageContainer;
import com.nsl.web.data.DataContainer.DataType;

/**
 * Given URL from the client, an object responsible to send request for an image
 * to the URL.
 * 
 * @author PGD
 */
class HttpsRequestImage extends HttpsRequest {
    private static final int BUFFER_SIZE = 1024;
    private static final int CONNECT_TIMEOUT = 10000;

    public HttpsRequestImage(String url) throws IOException {
        super(url);
    }

    @Override
    protected void setConnection(HttpsURLConnection conn) throws IOException {
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(CONNECT_TIMEOUT);
    }

    @Override
    protected void storeData(DataContainer container, HttpsURLConnection conn, boolean isSuccess) throws IOException {
        BufferedInputStream is = null;
        try {
            is = isSuccess ? new BufferedInputStream(conn.getInputStream())
                           : new BufferedInputStream(conn.getErrorStream());
            ImageContainer imageContainer = new ImageContainer();
            while (true) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int len = is.read(buffer, 0, BUFFER_SIZE);
                if (len == -1) {
                    break;
                }
                imageContainer.addBuffer(new ImageContainer.BufferElement(buffer, len));
            }
            container.setData(imageContainer, DataType.IMAGE);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
