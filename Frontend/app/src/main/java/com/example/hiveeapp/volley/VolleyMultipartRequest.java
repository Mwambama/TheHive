package com.example.hiveeapp.volley;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VolleyMultipartRequest extends Request<NetworkResponse> {

    private final String BOUNDARY = "apiclient-" + System.currentTimeMillis();
    private final String MULTIPART_FORM_DATA = "multipart/form-data;boundary=" + BOUNDARY;
    private final Response.Listener<NetworkResponse> mListener;
    private final Response.ErrorListener mErrorListener;
    private Map<String, String> headers;
    private Map<String, DataPart> byteData;

    public VolleyMultipartRequest(int method, String url, Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();

        // Set content type for multipart form data
        headers.put("Content-Type", MULTIPART_FORM_DATA);

        // Add authorization header
        String username = "employer@example.com";
        String password = "Test@1234";
        String credentials = username + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        // Add any other headers if needed
        if (this.headers != null) {
            headers.putAll(this.headers);
        }

        return headers;
    }


    @Override
    public String getBodyContentType() { return MULTIPART_FORM_DATA; }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (byteData != null && byteData.size() > 0) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                for (Map.Entry<String, DataPart> entry : byteData.entrySet()) {
                    buildPart(bos, entry.getKey(), entry.getValue());
                }
                bos.write(("--" + BOUNDARY + "--").getBytes());
            } catch (IOException e) {
                VolleyLog.e("IOException writing to ByteArrayOutputStream bos, building multipart request.");
            }
            return bos.toByteArray();
        }
        return null;
    }

    private void buildPart(ByteArrayOutputStream bos, String paramName, DataPart data) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(BOUNDARY).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"").append(paramName).append("\"; filename=\"")
                .append(data.getFileName()).append("\"\r\n");
        sb.append("Content-Type: ").append(data.getType()).append("\r\n\r\n");

        bos.write(sb.toString().getBytes());
        bos.write(data.getContent());
        bos.write("\r\n".getBytes());
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setByteData(Map<String, DataPart> byteData) {
        this.byteData = byteData;
    }

    public static class DataPart {
        private String fileName;
        private byte[] content;
        private String type;

        public DataPart(String fileName, byte[] content) {
            this.fileName = fileName;
            this.content = content;
            this.type = "application/pdf"; // default MIME type
        }

        public DataPart(String fileName, byte[] content, String type) {
            this.fileName = fileName;
            this.content = content;
            this.type = type;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getContent() {
            return content;
        }

        public String getType() {
            return type;
        }
    }
}
