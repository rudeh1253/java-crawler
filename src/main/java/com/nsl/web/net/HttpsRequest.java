package com.nsl.web.net;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import com.nsl.web.data.DataContainer;

/**
 * Given URL from the client, an object responsible to send a request
 * to the URL.
 * It would be better if you call HttsRequest.request() out of main thread.
 * 
 * @author PGD
 */
public abstract class HttpsRequest {
    /**
     * URL object of target.
     */
    private final URL url;
    
    /**
     * Cookie data that is going to be included in the HTTP request.
     */
    private String cookies;
    
    /**
     * Request properties.
     */
    private Map<String, String> requestProperties;
    
    /**
     * @param url where to send request
     * @throws IOException
     */
    public HttpsRequest(String url) throws IOException {
        this.url = new URL(url);
        this.cookies = null;
        this.requestProperties = null;
    }
    
    /**
     * Set cookies into the HTTP request.
     * 
     * @param nameValuePairs (Name, Value) pairs of cookies
     */
    public void setCookies(Map<String, String> nameValuePairs) {
        StringBuilder sb = new StringBuilder();
        for (String name : nameValuePairs.keySet()) {
            sb.append(name)
              .append('=')
              .append(nameValuePairs.get(name))
              .append(';');
        }
        this.cookies = sb.substring(0, sb.length() - 1);
    }
    
    public void setCookies(String nameValuePairsString) {
        this.cookies = nameValuePairsString;
    }
    
    public void setProperties(Map<String, String> properties) {
        this.requestProperties = properties;
    }

    /**
     * The main procedure to send request to the URL.
     * Just call this method to send request and get response.
     * 
     * @return response data.
     * @throws IOException
     */
    public final DataContainer request() throws IOException {
        return execute();
    }
    
    /**
     * Helper method for request()
     */
    private DataContainer execute() throws IOException {
        HttpsURLConnection conn = null;
        DataContainer container = new DataContainer();
        try {
            conn = buildConnection();
            int responseCode = conn.getResponseCode();
            boolean isSuccess = responseCode >= 200 && responseCode <= 300;
            storeData(container, conn, isSuccess);            
        } finally {
            close(conn);
        }
        return container;
    }

    private HttpsURLConnection buildConnection() throws IOException {
        HttpsURLConnection conn = (HttpsURLConnection)this.url.openConnection();
        setConnection(conn);
        if (this.cookies != null) {
            conn.setRequestProperty("Cookie", this.cookies);
        }
        setRequestProperties(conn);
        return conn;
    }
    
    private void setRequestProperties(HttpsURLConnection conn) {
        if (this.requestProperties != null) {
            for (String key : requestProperties.keySet()) {
                conn.setRequestProperty(key, requestProperties.get(key));
            }
        }
    }
    
    /**
     * Set connection property.
     * For instance, set content type, or request method.
     * 
     * @param conn connection object responsible to connect to the server.
     * @throws IOException
     */
    protected abstract void setConnection(HttpsURLConnection conn) throws IOException;
    
    /**
     * Defining how to store data to DataContainer object.
     * Given a connection to the URL, take InputStream from the connection,
     * and get data through the stream.
     * 
     * @param container instance where data is stored.
     * @param conn instance which connects 
     * @param isSuccess true if building connection was successful,
     *                  false if building connection failed,
     *                  according to the response code.
     * @throws IOException
     */
    protected abstract void storeData(DataContainer container,
                                      HttpsURLConnection conn,
                                      boolean isSuccess) throws IOException;
    
    private void close(HttpsURLConnection conn) throws IOException {
        if (conn != null) {
            conn.disconnect();
        }
    }
    
    /**
     * Return an instance for request HTML.
     * 
     * @param url to request.
     * @return the instance.
     * @throws IOException
     */
    public static HttpsRequest getHTMLRequester(String url) throws IOException {
        return new HttpsRequestHTML(url);
    }
    
    /**
     * Return an instance for request image.
     * 
     * @param url to request.
     * @return the instance.
     * @throws IOException
     */
    public static HttpsRequest getImageRequester(String url) throws IOException {
        return new HttpsRequestImage(url);
    }
    
    @Override
    public String toString() {
        
        return "URL: " + url.toString() + '\n'
               + "Cookies: " + getCookiesAsToStringFormat();
    }
    
    private String getCookiesAsToStringFormat() {
        if (this.cookies == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String cookie : this.cookies.split(";")) {
            sb.append("       ").append(cookie).append('\n');
        }
        return sb.substring(0, sb.length() - 1);
    }
}