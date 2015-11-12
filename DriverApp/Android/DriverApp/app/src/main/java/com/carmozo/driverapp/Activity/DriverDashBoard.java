package com.carmozo.driverapp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.carmozo.driverapp.Adapter.DriverDrawMenuListAdapter;
import com.carmozo.driverapp.Adapter.DriverJobListAdapter;
import com.carmozo.driverapp.Application.DriverAppApplicationController;
import com.carmozo.driverapp.R;
import com.carmozo.driverapp.UiUtilities.C;
import com.carmozo.driverapp.UiUtilities.DriverDrawMenuListItem;
import com.carmozo.driverapp.UiUtilities.DriverJobLists;
import com.carmozo.driverapp.UiUtilities.PostJsonArrayRequest;
import com.carmozo.driverapp.UiUtilities.PostJsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by shreyasgs on 17-09-2015.
 */
public class DriverDashBoard extends AppCompatActivity {

    private List<DriverJobLists> incomingJobList = new ArrayList<DriverJobLists>();
    private ListView jobsListView;
    private DriverJobListAdapter jobsListAdapter;
    private static String TAG = DriverDashBoard.class.getSimpleName();
    private static String loginResp ;
    private static String authKey;
    private static String provKey;
    private static String prov_sec_key;
    private static String driverMobileNo;
    private static int countDriverUpdate;

    /* Drawer menu layout options */

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    //private String[] drawerMenuTitles;
    private TypedArray drawerMenuIcons;

    private ArrayList<DriverDrawMenuListItem> driverDrawerItems;
    private DriverDrawMenuListAdapter driverDrawerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.CustomActionBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_dashboard);
        countDriverUpdate = 0;

        Bundle bundle = getIntent().getExtras();
        loginResp = bundle.getString(C.AppKey.DRIVER_APP_LOGIN_RESP_KEY);
        authKey = bundle.getString(C.AppKey.DRIVER_APP_AUTH_TOKEN_KEY);
        provKey = bundle.getString(C.AppKey.DRIVER_APP_PROVIDER_API_KEY);
        prov_sec_key = bundle.getString(C.AppKey.DRIVER_APP_PROVIDER_API_SECRET_KEY);
        driverMobileNo = bundle.getString(C.jsonKey.DRIVER_LOGIN_MOBILENO_KEY);

        Log.v(TAG, "[Guru]: Inside DashBoard File");
        jobsListView = (ListView) findViewById(R.id.listView);
        jobsListAdapter = new DriverJobListAdapter(this, incomingJobList);
        jobsListView.setAdapter(jobsListAdapter);

        makeJsonArrayRequest();

        //--
        mTitle = mDrawerTitle = getTitle();
         /*
        settings of Drawer menu content
         */
        setDriverDrawerlayoutItem(3);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }

        jobsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (-1 < position) {
                    DriverJobLists item = (DriverJobLists) incomingJobList.get(position);
                    Intent orderListIntent = new Intent(DriverDashBoard.this, DriverNavBoard.class);

                    orderListIntent.putExtra(C.AppKey.DRIVER_APP_AUTH_TOKEN_KEY, authKey);
                    orderListIntent.putExtra(C.AppKey.DRIVER_APP_PROVIDER_API_KEY, provKey);
                    orderListIntent.putExtra(C.AppKey.DRIVER_APP_PROVIDER_API_SECRET_KEY, prov_sec_key);
                    orderListIntent.putExtra(C.AppKey.DRIVER_APP_LOGIN_RESP_KEY, loginResp);
                    orderListIntent.putExtra(C.AppKey.DRIVER_APP_INCOMING_JOB_SELECTED_KEY, (Serializable) item);
                    orderListIntent.putExtra(C.jsonKey.DRIVER_LOGIN_MOBILENO_KEY, driverMobileNo);
                    startActivity(orderListIntent);
                } else {
                    //--Invalid selection
                    Toast.makeText(getApplicationContext(), getString(R.string.error_job_selection), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void setDriverDrawerlayoutItem(int position){

        String[] drawerMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        drawerMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        driverDrawerItems = new ArrayList<DriverDrawMenuListItem>();

        // adding nav drawer items to array

        driverDrawerItems.add(new DriverDrawMenuListItem(drawerMenuTitles[0], drawerMenuIcons.getResourceId(0, -1)));
        driverDrawerItems.add(new DriverDrawMenuListItem(drawerMenuTitles[1], drawerMenuIcons.getResourceId(1, -1)));
        driverDrawerItems.add(new DriverDrawMenuListItem(drawerMenuTitles[2], drawerMenuIcons.getResourceId(2, -1), true,
                                                                                String.valueOf(countDriverUpdate)));
        driverDrawerItems.add(new DriverDrawMenuListItem(drawerMenuTitles[3], drawerMenuIcons.getResourceId(3, -1)));
        driverDrawerItems.add(new DriverDrawMenuListItem(drawerMenuTitles[4], drawerMenuIcons.getResourceId(4, -1)));
        driverDrawerItems.add(new DriverDrawMenuListItem(drawerMenuTitles[0], drawerMenuIcons.getResourceId(5, -1)));


        // Recycle the typed array
        drawerMenuIcons.recycle();

        // setting the nav drawer list adapter
        driverDrawerAdapter = new DriverDrawMenuListAdapter(getApplicationContext(),
                driverDrawerItems);
        mDrawerList.setAdapter(driverDrawerAdapter);

        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name)
        {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:

                }
                return true;
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
       getSupportActionBar().setTitle(mTitle);
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {

    }
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    /**
     * Method to make json object request where json response starts wtih {
     * */
    private void makeJsonObjectRequest() {
    }

    /**
     * Method to make json array request where response starts with [
     * */
    private void makeJsonArrayRequest() {
        HashMap <String, String> headers = new HashMap<String, String>();

        headers.put("Content-Type", "application/json");
        headers.put(C.headerReqKey.DRIVER_APP_HTTP_REQ_AUTH_KEY, authKey);
        headers.put(C.headerReqKey.DRIVER_APP_HTTP_REQ_PROVIDER_API_KEY, provKey);
        headers.put(C.headerReqKey.DRIVER_APP_HTTP_REQ_PROVIDER_SEC_KEY, prov_sec_key);

        //HashMap<String, String> params = new HashMap<String, String>();
        Log.v(TAG, "[Guru]:" + headers.toString());

        PostJsonObjectRequest request = new PostJsonObjectRequest(Request.Method.POST,
                        C.ConnectionString.RETR_ORDER_LIST_URL,
                        headers, null,
                        new Response.Listener<JSONObject>(){
                        @Override
                        public void onResponse(JSONObject response){
                            Log.v(TAG, response.toString());

                            try {

                                JSONArray jArray = response.getJSONArray(C.jsonKey.DRIVER_LOGIN_NODE_KEY);

                                countDriverUpdate = jArray.length() ;
                                for (int i = 0; i < jArray.length(); i++) {

                                    //-- JSON element - order_doc_id
                                    JSONObject jObject = (JSONObject) jArray.getJSONObject(i);
                                    JSONObject jOrderDocId = jObject.getJSONObject(C.jsonKey.DRIVER_ORDER_RESP_ORDER_DOC_ID_KEY);
                                    //-- JSON element - order_id, user_id, booking_date
                                    String order_id = jOrderDocId.getString(C.jsonKey.DRIVER_ORDER_RESP_ORDER_ID_KEY);
                                    String user_id = jOrderDocId.getString(C.jsonKey.DRIVER_ORDER_RES_USER_ID_KEY);
                                    String booking_date = jOrderDocId.getString(C.jsonKey.DRIVER_ORDER_RESP_BOOKING_DATE_KEY);

                                    long lBookingDate = 0;
                                    java.text.DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                    try {

                                        Date convertedDate = (Date) formatter.parse(booking_date);
                                        lBookingDate = convertedDate.getTime();
                                        Log.v(TAG, "[Guru]:" + String.valueOf(lBookingDate));

                                    }catch(ParseException e) {
                                        e.printStackTrace();
                                    }

                                    //-- JSON element - user_data ->[name, mobile]
                                    JSONObject jUserData = jOrderDocId.getJSONObject(C.jsonKey.DRIVER_ORDER_RESP_USER_DATA_KEY);
                                    String user_name = jUserData.getString(C.jsonKey.DRIVER_ORDER_RESP_USER_NAME_KEY);
                                    String mobile_no = jUserData.getString(C.jsonKey.DRIVER_ORDER_RESP_MOBILE_NO_KEY);

                                    //-- JSON element - customer_address -> [text -> Street, loc]
                                    JSONObject jUserAddr = jOrderDocId.getJSONObject(C.jsonKey.DRIVER_ORDER_RESP_CUST_ADDR_KEY);
                                    JSONArray cust_loc_arr = jUserAddr.getJSONArray(C.jsonKey.DRIVER_ORDER_RESP_CUST_LOC_KEY);
                                    LatLng custLatLang = new LatLng(cust_loc_arr.getInt(0), cust_loc_arr.getInt(1));

                                    JSONObject jAddrDet = jUserAddr.getJSONObject(C.jsonKey.DRIVER_ORDER_RESP_CUST_ADDR_TEXT_KEY);
                                    String cust_street_addr = jAddrDet.getString(C.jsonKey.DRIVER_ORDER_RESP_STREET_KEY);

                                    //--JSON element - sub_order_doc_id ->destination_address[loc, street]
                                    JSONObject sub_order_doc = jObject.getJSONObject(C.jsonKey.DRIVER_ORDER_RESP_PARTNER_SUBORDER_DOC_KEY);
                                    String sub_order_id = sub_order_doc.getString(C.jsonKey.DRIVER_ORDER_RESP_PARTNER_SUBORDER_ID_KEY);

                                    //-- JSON element - destination address and loc
                                    JSONObject dest_node = jObject.getJSONObject(C.jsonKey.DRIVER_ORDER_RESP_PARTNER_ADDR_KEY);
                                    String dest_addr = dest_node.getString(C.jsonKey.DRIVER_ORDER_RESP_PARTNER_ADDR_STREET_KEY);
                                    //String dest_loc = dest_node.getString(C.jsonKey.DRIVER_ORDER_RESP_PARTNER_ADDR_LOC_KEY);

                                    //-- JSON element - logistics Id
                                    String logistics_id = jObject.getString(C.jsonKey.DRIVER_ORDER_RESP_LOGISTICS_ID_KEY);

                                    //-- JSON element -- Status id
                                    JSONObject status_obj = jObject.getJSONObject(C.jsonKey.DRIVER_ORDER_RESP_STATUS_NODE_KEY);
                                    int statusId = status_obj.getInt(C.jsonKey.DRIVER_ORDER_RESP_STATUS_ID_KEY);


                                    DriverJobLists item = new DriverJobLists();

                                    item.setCustomerId(user_id);
                                    item.setCustomerName(user_name);
                                    item.setCustomerMobileNo(mobile_no);
                                    item.setJobId(order_id);
                                    item.setJobTitle("General Servicing");
                                    item.setPickupPoint(cust_street_addr);
                                    item.setScheduleTime(lBookingDate); //-- pickup time
                                    item.setDestAddress(dest_addr);
                                    item.setStartLatLong(custLatLang);
                                    item.setSubOrderId(sub_order_id);
                                    item.setLogisticsId(logistics_id);
                                    item.setStatusId(statusId);

                                    incomingJobList.add(item);
                                }

                            } catch(JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Error:"+e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }

                            jobsListAdapter.notifyDataSetChanged();
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                            Toast.makeText(getApplicationContext(),
                                    error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        // Adding request to request queue
        DriverAppApplicationController.getInstance().addToRequestQueue(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

 }
