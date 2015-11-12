package com.carmozo.driverapp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.carmozo.driverapp.Application.DriverAppApplicationController;
import com.carmozo.driverapp.R;
import com.carmozo.driverapp.UiUtilities.C;
import com.carmozo.driverapp.UiUtilities.PostJsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * Created by shreyasgs on 25-09-2015.
 */
public class DriverLogin  extends AppCompatActivity {
    Button b1;
    EditText mobileNo, passwd;
    private static String TAG = DriverLogin.class.getSimpleName();
    private static boolean loginStatus = false;
    //private static String loginResponse;
    int counter = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        b1=(Button)findViewById(R.id.button);
        mobileNo=(EditText)findViewById(R.id.editText);
        passwd=(EditText)findViewById(R.id.editText2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mobileNo.getText().toString().isEmpty() &&
                        !passwd.getText().toString().isEmpty()) {
                    //Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
                    loginStatus = false;

                    makeJsonObjectRequest(mobileNo.getText().toString(), passwd.getText().toString());

                    Log.v(TAG, "[Guru]:" + loginStatus);


                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_login_status), Toast.LENGTH_SHORT).show();

                    counter--;

                    if (counter == 0) {
                        b1.setEnabled(false);
                    }
                }
            }
        });

    }

    /**
     * Method to make json object request where json response starts wtih {
     * */
    private void makeJsonObjectRequest(final String mobileNo, String passwd) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put ("Content-Type", "application/json");

        HashMap<String, Object> body = new HashMap<String, Object> ();
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("\""+ C.jsonKey.DRIVER_LOGIN_MOBILENO_KEY + "\"", "\""+ mobileNo + "\"");
        params.put("\"" + C.jsonKey.DRIVER_LOGIN_PASSWD_KEY +"\"", "\""+convertPassMd5(passwd)+"\"");
        params.put("\"" + C.jsonKey.DRIVER_LOGIN_PROVIDER_KEY + "\"", "1");

        body.put("\"" + C.jsonKey.DRIVER_LOGIN_NODE_KEY + "\"", params);

        String bodyParam =  body.toString().replaceAll("=", ":");
        //Log.v(TAG, body.toString().replaceAll("=", ":"));
        Log.v(TAG, bodyParam);
        //Toast.makeText(getApplicationContext(), body.toString().replaceAll("=", ":"), Toast.LENGTH_LONG).show();
        PostJsonObjectRequest  request = new PostJsonObjectRequest(Request.Method.POST,
                C.ConnectionString.LOGIN_URL,
                headers, bodyParam,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v(TAG, response.toString());
                        loginStatus = true;

                        //loginResponse = response.toString();
                        try {
                            JSONObject jObj = response.getJSONObject(C.jsonKey.DRIVER_LOGIN_NODE_KEY);
                            String auth_key = jObj.getString(C.jsonKey.DRIVER_LOGIN_RESP_AUTH_KEY);
                            String prov_key = jObj.getString(C.jsonKey.DRIVER_LOGIN_RESP_PROV_KEY);
                            String prov_sec_key = jObj.getString(C.jsonKey.DRIVER_LOGIN_RESP_PROV_SEC_KEY);

                            if (loginStatus == true) {
                                Intent loginIntent = new Intent(DriverLogin.this, DriverDashBoard.class);
                                loginIntent.putExtra(C.AppKey.DRIVER_APP_AUTH_TOKEN_KEY, auth_key);
                                loginIntent.putExtra(C.AppKey.DRIVER_APP_PROVIDER_API_KEY, prov_key);
                                loginIntent.putExtra(C.AppKey.DRIVER_APP_PROVIDER_API_SECRET_KEY, prov_sec_key);
                                loginIntent.putExtra(C.AppKey.DRIVER_APP_LOGIN_RESP_KEY, response.toString());
                                loginIntent.putExtra(C.jsonKey.DRIVER_LOGIN_MOBILENO_KEY, mobileNo);
                                startActivity(loginIntent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loginStatus = false;
                            Toast.makeText(getApplicationContext(),  getString(R.string.error_login_status), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.v(TAG, "[Guru]:Error: " + volleyError.getMessage());
                        //Toast.makeText(getApplicationContext(),
                          //      volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        loginStatus = false;
                        Toast.makeText(getApplicationContext(),  getString(R.string.error_login_status), Toast.LENGTH_SHORT).show();
                    }
                });

        DriverAppApplicationController.getInstance().addToRequestQueue(request);
    }

    /**
     * Method to make json array request where response starts with [
     * */
    private void makeJsonArrayRequest() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static String convertPassMd5(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }
}
