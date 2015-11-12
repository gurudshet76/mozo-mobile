package com.carmozo.driverapp.Activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.carmozo.driverapp.Application.DriverAppApplicationController;
import com.carmozo.driverapp.R;
import com.carmozo.driverapp.Traccar.TrackingService;
import com.carmozo.driverapp.UiUtilities.C;
import com.carmozo.driverapp.UiUtilities.DirectionsJSONParser;
import com.carmozo.driverapp.UiUtilities.DriverJobLists;
import com.carmozo.driverapp.UiUtilities.PostJsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by shreyasgs on 17-09-2015.
 */
public class DriverNavBoard extends AppCompatActivity implements OnClickListener, LocationListener {
    private GoogleMap map;
    private MapView mapView;
    private Button captureCarPhotoBtn;
    private Button pickupActivityBtn;
    //private Button callOwner;
    //private TextView mobileNo;
    private TextView orderRequest;
    private TextView customerName;
    private TextView mapDistanceTime;
    private TextView pickupPoint;
    //private TextView dropPoint;
    DriverJobLists mJobsItem;

    private static String loginResp ;
    private static String authKey;
    private static String provKey;
    private static String prov_sec_key;
    private static String driverMobileNo;

    private ArrayList<LatLng> markerPoints;
    private LatLng origin;
    private LatLng dest;
    private double mLatitude=0;
    private double mLongitude=0;

    public static final int LOCATION_MIN_TIME_STATUSCHANGE = 20000;
    public static final int LOCATION_MIN_DISTANCE = 0 ;

    private static String TAG = DriverNavBoard.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drivenav_layout);

        mJobsItem = (DriverJobLists)getIntent().getSerializableExtra(C.AppKey.DRIVER_APP_INCOMING_JOB_SELECTED_KEY);

        loginResp = getIntent().getExtras().getString(C.AppKey.DRIVER_APP_LOGIN_RESP_KEY);
        authKey = getIntent().getExtras().getString(C.AppKey.DRIVER_APP_AUTH_TOKEN_KEY);
        provKey = getIntent().getExtras().getString(C.AppKey.DRIVER_APP_PROVIDER_API_KEY);
        prov_sec_key = getIntent().getExtras().getString(C.AppKey.DRIVER_APP_PROVIDER_API_SECRET_KEY);

        driverMobileNo = getIntent().getExtras().getString(C.jsonKey.DRIVER_LOGIN_MOBILENO_KEY);
        C.TraccarConnParam.TRACCAR_CONN_DEVICEID = driverMobileNo;
        C.TraccarConnParam.TRACCAR_CONN_DEVICEID = C.TraccarConnParam.TRACCAR_CONN_DEVICEID.replace("+", "");

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        if(map != null) {
            //map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        }

        markerPoints = new ArrayList<LatLng>();
        // Enable MyLocation Button in the Map
        map.setMyLocationEnabled(true);


        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location From GPS
        Location location = locationManager.getLastKnownLocation(provider);

        if(location!=null){
            onLocationChanged(location);
        }

        locationManager.requestLocationUpdates(provider, LOCATION_MIN_TIME_STATUSCHANGE, LOCATION_MIN_DISTANCE, this);

        drawMarkerStart(mJobsItem.getStartLatLong(), mJobsItem.getEndLatLong());

        int imageCnt = RetrieveImageCountFromPath ();
        captureCarPhotoBtn = (Button)findViewById(R.id.captureImageBtn);
        captureCarPhotoBtn.setOnClickListener(this);
        if(imageCnt > 4)
            captureCarPhotoBtn.setText(R.string.view_photos);

        //--  Pickup/drop button
        pickupActivityBtn = (Button)findViewById(R.id.pickupActivityBtn);
        pickupActivityBtn.setOnClickListener(this);

        orderRequest = (TextView)findViewById(R.id.orderRequest);
        orderRequest.setText(mJobsItem.getJobId());

        customerName = (TextView)findViewById(R.id.customerName);
        customerName.setText(mJobsItem.getCustomerName());

        //mobileNo = (TextView)findViewById(R.id.mobileNo);
        //mobileNo.setText(mJobsItem.getCustomerMobileNo());
       // mobileNo.setText("(9449833452)");

        pickupPoint = (TextView)findViewById(R.id.pickupNavPoint);
        pickupPoint.setText(mJobsItem.getPickupPoint());
        //pickupPoint.setText("Test pickup point, pin:123456");

       // dropPoint = (TextView)findViewById(R.id.dropNavPoint);
        //dropPoint.setText(mJobsItem.getDestAddress());
       // dropPoint.setText("Test drop point, pin: 123456");

        //mapDistanceTime = (TextView)findViewById(R.id.mapDistanceTime);
        //mapDistanceTime.setText("Distance: 3.3 km, Duration : 10 mins");
    }

    @Override
    public void onClick(View arg0){
        if(arg0.getId() == R.id.pickupActivityBtn){

            String pickupDropStr = pickupActivityBtn.getText().toString();

            if(pickupDropStr.equals(getString(R.string.start_pickup)) ) {
                Log.v(TAG, "[Guru] - Start Trip");
                //-- Start trip to update status
                startPickupDropTrip();

                //-- Initiate traccer app
                Log.v(TAG, "[Guru]: " + C.TraccarConnParam.TRACCAR_CONN_DEVICEID);
                startTraccarService ();

                //-- change button text to Stop
                pickupActivityBtn.setText(R.string.end_pickup);
            }
            else if(pickupDropStr.equals(getString(R.string.end_pickup))) {
                Log.v(TAG, "[Guru] - End Trip");
                //-- Stop trip to update status
                stopPickupDropTrip();

                //-- Stop traccer app
                stopTraccarService();

                //-- Change button text to Start
                pickupActivityBtn.setText(getString(R.string.start_pickup));
            }

        }
        else if(arg0.getId() == R.id.captureImageBtn) {
            try {
                //Toast.makeText(this, "Launching Car photo Capture...", Toast.LENGTH_SHORT).show();

                Intent captureCarPhotoIntent = new Intent(DriverNavBoard.this, DriverCarPhotos.class);
                captureCarPhotoIntent.putExtra(C.jsonKey.DRIVER_ORDER_RESP_ORDER_ID_KEY, mJobsItem.getJobId());
                startActivity(captureCarPhotoIntent);

            } catch (ActivityNotFoundException anfe) {
                String errorMessage = "Whoops - your device doesn't support capturing images !";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu options and adds items to action bar
        getMenuInflater().inflate(R.menu.menu_drivernavapp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_call_owner) {
            //-- Launch phone dialer
            String phone = "+" + mJobsItem.getCustomerMobileNo();
            Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phone, null));
            startActivity(phoneIntent);
        }
        else if (id == R.id.action_upload_invoice) {
            //-- Launch app for uploading invoice
        }
        else if(id == R.id.action_settings) {
            //-- Launch settings
        }

        return super.onOptionsItemSelected(item);
    }

    private void drawMarkerStart(LatLng startPoint, LatLng endPoint) {

        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = new PolylineOptions();

        MarkerOptions startOptions = new MarkerOptions();
        MarkerOptions endOptions = new MarkerOptions();

        startOptions.position(startPoint);
        endOptions.position(endPoint);

        lineOptions.add(startPoint);
        lineOptions.add(endPoint);
        lineOptions.width(2);
        lineOptions.color(Color.RED);

        // Add new marker to the Google Map Android API V2
        map.addMarker(startOptions);
        map.addMarker(endOptions);


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(endPoint, 12));
        map.animateCamera(CameraUpdateFactory.zoomTo(12));

        map.addPolyline(lineOptions);
    }

    private void drawMarker(LatLng point) {
        markerPoints.add(point);

        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();

        // Setting the position of the marker
        options.position(point);

        /**
         * For the start location, the color of marker is GREEN and
         * for the end location, the color of marker is RED.
         */
        if (markerPoints.size() == 1) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            if (markerPoints.size() == 2) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }

            // Add new marker to the Google Map Android API V2
            map.addMarker(options);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = C.General.GOOGLE_MAP_CONNECTION_URL + output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.v(TAG, "Exception while downloading url:" + e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if(result.size()<1){
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.RED);
            }

            mapDistanceTime.setText(getString(R.string.map_distance)+":" + distance +
                                    ", " + getString(R.string.map_duration)+ ":"+ duration);

            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        // Draw the marker, if destination location is not set
        if(markerPoints.size() < 2){

            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            LatLng point = new LatLng(mLatitude, mLongitude);

            map.moveCamera(CameraUpdateFactory.newLatLng(point));
            map.animateCamera(CameraUpdateFactory.zoomTo(12));

            drawMarker(point);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onBackPressed() {
        Log.v(TAG, "[Guru] - End Trip");
        //-- Stop trip to update status
        stopPickupDropTrip();

        //-- Stop traccer app
        stopTraccarService();

        super.onBackPressed();
    }

    /*
       Start pickup/drop when driver picks up vehicle from owners or from service center
     */
    public void startPickupDropTrip () {
        String startTripUrl = C.ConnectionString.START_TRIP_URL + mJobsItem.getJobId() + "/" + mJobsItem.getSubOrderId() +
                                "/" + mJobsItem.getLogisticsId() + "?command=8";

        HashMap <String, String> headers = new HashMap<String, String>();

        headers.put("Content-Type", "application/json");
        headers.put(C.headerReqKey.DRIVER_APP_HTTP_REQ_AUTH_KEY, authKey);
        headers.put(C.headerReqKey.DRIVER_APP_HTTP_REQ_PROVIDER_API_KEY, authKey);
        headers.put(C.headerReqKey.DRIVER_APP_HTTP_REQ_PROVIDER_SEC_KEY, prov_sec_key);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("\""+ C.jsonKey.DRIVER_TRIP_TRACCER_ID_KEY + "\"",  "\"" + driverMobileNo + "\""); //Get ID for device stored for traccer

        HashMap<String, Object> body = new HashMap<String, Object>();
        body.put("\"" + C.jsonKey.DRIVER_REQ_DATA_KEY + "\"", params);

        String bodyJsonStr = body.toString().replaceAll("=", ":");

        Log.v(TAG, bodyJsonStr);

        PostJsonObjectRequest request = new PostJsonObjectRequest(Request.Method.POST,
                                        C.ConnectionString.START_TRIP_URL,
                                        headers, bodyJsonStr,
                    new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v(TAG, response.toString());
                        pickupActivityBtn.setText(getString(R.string.end_pickup));

                    } },
                    new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.v(TAG, "[Guru]:Error: " + volleyError.getMessage());
                                //Toast.makeText(getApplicationContext(),
                                //      volleyError.getMessage(), Toast.LENGTH_SHORT).show();

                                //Toast.makeText(getApplicationContext(),  getString(R.string.error_login_status), Toast.LENGTH_SHORT).show();
                            }
                        });

        // Adding request to request queue
        DriverAppApplicationController.getInstance().addToRequestQueue(request);
    }


    /*
       Stop pickup/drop when driver drops vehicle to service center or delivers to
       vehicle owner
     */
    public void stopPickupDropTrip () {
        String startTripUrl = C.ConnectionString.STOP_TRIP_URL + mJobsItem.getJobId() + "/" + mJobsItem.getSubOrderId() +
                "/" + mJobsItem.getLogisticsId() + "?command=9";

        HashMap <String, String> headers = new HashMap<String, String>();

        headers.put("Content-Type", "application/json");
        headers.put(C.headerReqKey.DRIVER_APP_HTTP_REQ_AUTH_KEY, authKey);
        headers.put(C.headerReqKey.DRIVER_APP_HTTP_REQ_PROVIDER_API_KEY, authKey);
        headers.put(C.headerReqKey.DRIVER_APP_HTTP_REQ_PROVIDER_SEC_KEY, prov_sec_key);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("\""+ C.jsonKey.DRIVER_REQ_STOP_TRIP_KM_KEY + "\"",  "\"" + "14" + "\""); //Get ID for device stored for traccer

        HashMap<String, Object> body = new HashMap<String, Object>();
        body.put("\"" + C.jsonKey.DRIVER_REQ_DATA_KEY + "\"", params);

        String bodyJsonStr = body.toString().replaceAll("=", ":");

        Log.v(TAG, bodyJsonStr);

        PostJsonObjectRequest request = new PostJsonObjectRequest(Request.Method.POST,
                C.ConnectionString.STOP_TRIP_URL,
                headers, bodyJsonStr,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v(TAG, response.toString());
                        //--Change Button to Stop trip
                        pickupActivityBtn.setText(getString(R.string.start_pickup));

                    } },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.v(TAG, "[Guru]:Error: " + volleyError.getMessage());
                        //Toast.makeText(getApplicationContext(),
                        //      volleyError.getMessage(), Toast.LENGTH_SHORT).show();

                        //Toast.makeText(getApplicationContext(),  getString(R.string.error_login_status), Toast.LENGTH_SHORT).show();
                    }
                });

        // Adding request to request queue
        DriverAppApplicationController.getInstance().addToRequestQueue(request);
    }

    private int RetrieveImageCountFromPath() {
        String GridViewDemo_ImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                                        "/gridviewtest/" + mJobsItem.getJobId();

        List<String> tFileList = new ArrayList<String>();
        File f = new File(GridViewDemo_ImagePath);
        if(f.exists()) {
            File[] files = f.listFiles();
            Arrays.sort(files);

            for(int i = 0; i < files.length; i++) {
                File file = files[i];
                if(file.isDirectory())
                    continue;
                tFileList.add(file.getPath());
            }
        }
        return tFileList.size();
    }

    public void startTraccarService(){
        startService(new Intent(this, TrackingService.class));
    }

    public void stopTraccarService(){
        stopService(new Intent(this, TrackingService.class));
    }
}
