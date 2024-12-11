package com.example.hiveeapp.volley;

import android.util.Log;

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

    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private final String mimeType = "multipart/form-data; boundary=" + boundary;
    private final Response.Listener<NetworkResponse> mListener;
    private final Response.ErrorListener mErrorListener;
    private Map<String, DataPart> byteData;
    private Map<String, String> headers;

    public VolleyMultipartRequest(int method, String url, Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }

    @Override
    public String getBodyContentType() {
        return mimeType;
    }

    @Override
    public byte[] getBody() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            if (byteData != null && !byteData.isEmpty()) {
                for (Map.Entry<String, DataPart> entry : byteData.entrySet()) {
                    buildPart(bos, entry.getKey(), entry.getValue());
                }
                bos.write(("--" + boundary + "--\r\n").getBytes());
            }
        } catch (IOException e) {
            Log.e("VolleyMultipartRequest", "Error writing multipart body", e);
        }
        return bos.toByteArray();
    }

    private void buildPart(ByteArrayOutputStream bos, String name, DataPart data) throws IOException {
        StringBuilder partHeader = new StringBuilder();
        partHeader.append("--").append(boundary).append("\r\n")
                .append("Content-Disposition: form-data; name=\"").append(name).append("\"; filename=\"")
                .append(data.getFileName()).append("\"\r\n")
                .append("Content-Type: ").append(data.getType()).append("\r\n\r\n");
        bos.write(partHeader.toString().getBytes());
        bos.write(data.getContent());
        bos.write("\r\n".getBytes());
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            Log.e("VolleyMultipartRequest", "Error parsing network response", e);
            return Response.error(new VolleyError("Error parsing network response"));
        }
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        if (mErrorListener != null) {
            mErrorListener.onErrorResponse(error);
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setByteData(Map<String, DataPart> byteData) {
        this.byteData = byteData;
    }

    public static class DataPart {
        private final String fileName;
        private final byte[] content;
        private final String type;

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
