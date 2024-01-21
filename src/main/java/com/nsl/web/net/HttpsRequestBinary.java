package com.nsl.web.net;

import java.io.BufferedInputStream;
import java.io.IOException;
import javax.net.ssl.HttpsURLConnection;

import com.nsl.web.data.Buffer;
import com.nsl.web.data.DataContainer;
import com.nsl.web.data.BinaryContainer;

/**
 * Given URL from the client, an object responsible to send request for binary data
 * to the URL.
 * 
 * @author PGD
 */
public class HttpsRequestBinary extends HttpsRequest<byte[]> {
    private static final int BUFFER_SIZE = 1024;
    private static final int CONNECT_TIMEOUT = 10000;

    public HttpsRequestBinary(String url) throws IOException {
        super(url);
    }

    @Override
    protected void setConnection(HttpsURLConnection conn) throws IOException {
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(CONNECT_TIMEOUT);
    }

    @Override
    protected DataContainer<byte[]> storeData(HttpsURLConnection conn, boolean isSuccess) throws IOException {
        BufferedInputStream is = null;
        try {
            is = isSuccess ? new BufferedInputStream(conn.getInputStream())
                           : new BufferedInputStream(conn.getErrorStream());
            BinaryContainer imageContainer = new BinaryContainer();
            while (true) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int len = is.read(buffer, 0, BUFFER_SIZE);
                if (len == -1) {
                    break;
                }
                imageContainer.addBuffer(new Buffer<>(buffer, len));
            }
            return imageContainer;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
