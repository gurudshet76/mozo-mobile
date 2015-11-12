package com.carmozo.driverapp.UiUtilities;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shreyasgs on 15-10-2015.
 */
public class PostJsonArrayRequest extends JsonRequest<JSONArray> {

    Map<String, String> headers;
    Map<String, String> params;
    private Response.Listener listener;

    /**
     * Creates a new request.
     * @param url URL to fetch the JSON from
     * @param listener Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public PostJsonArrayRequest(int requestMethod, String url,
                                    Map<String, String> headers,
                                    Map<String, String> params,
                                    Response.Listener<JSONArray> listener,
                                    Response.ErrorListener errorListener) {
        super(requestMethod, url, null, listener, errorListener);
        this.headers = headers;
        this.params = params;
        this.listener = listener;
    }

    @Override
    protected void deliverResponse(JSONArray response) {
        listener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders ()  throws AuthFailureError {
        return headers != null? headers: super.getHeaders();
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params != null? params: super.getParams();
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONArray(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}

