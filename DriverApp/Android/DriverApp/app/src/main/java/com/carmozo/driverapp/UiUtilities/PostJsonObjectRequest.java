package com.carmozo.driverapp.UiUtilities;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shreyasgs on 15-10-2015.
 */
public class PostJsonObjectRequest extends JsonRequest<JSONObject> {
    HashMap<String, String> headers;
    //String params;
   //JSONObject params;
    private Response.Listener listener;
    private static String TAG = PostJsonObjectRequest.class.getSimpleName();

    /**
     * Creates a new request.
     * @param method the HTTP method to use
     * @param url URL to fetch the JSON from
     * @param jsonRequest A {@link JSONObject} to post with the request. Null is allowed and
     *   indicates no parameters will be posted along with request.
     * @param listener Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public PostJsonObjectRequest(int method, String url,
                             HashMap<String, String> headers,
                             String params,
                             Response.Listener<JSONObject> listener,
                                Response.ErrorListener errorListener) {

        super(method, url, params,
                listener,
                errorListener);
        this.headers = headers;
        //this.params = params;
        this.listener = listener;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders ()  throws AuthFailureError {

        Log.v(TAG, "[Guru]:" + headers.toString());
        return headers != null? headers: super.getHeaders();
    }


    /*
    @Override

    protected Map<String, String> getParams() throws AuthFailureError {

        return this.params != null? params: super.getParams();
    }*/

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
