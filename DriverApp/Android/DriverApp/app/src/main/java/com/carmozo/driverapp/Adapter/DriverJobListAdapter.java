package com.carmozo.driverapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carmozo.driverapp.R;
import com.carmozo.driverapp.UiUtilities.DriverJobLists;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by shreyasgs on 27-09-2015.
 */
public class DriverJobListAdapter extends BaseAdapter {

    private static String TAG = DriverJobListAdapter.class.getSimpleName();
    private Activity activity;
    private LayoutInflater inflater;
    private List<DriverJobLists> jobItemList ;

    public DriverJobListAdapter(Activity activity, List<DriverJobLists> jobItemList) {
        this.activity = activity ;
        this.jobItemList = jobItemList;
    }

    @Override
    public int getCount () {
        return jobItemList.size();
    }

    @Override
    public Object getItem (int location) {
        return jobItemList.get(location);
    }

    @Override
    public long getItemId (int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.driver_job_list_row, null);


        //ImageView imageView = (ImageView) convertView.findViewById(R.id.thumbnail);


        TextView jobId = (TextView) convertView.findViewById(R.id.jobId);
        TextView customerName = (TextView) convertView.findViewById(R.id.customerName);
        //TextView customerMobileNo = (TextView) convertView.findViewById(R.id.customerMobileNo);
        TextView pickupPoint = (TextView) convertView.findViewById(R.id.pickupPoint);
        TextView dropPoint = (TextView) convertView.findViewById(R.id.dropPoint);
        TextView scheduleTime = (TextView) convertView.findViewById(R.id.scheduledTime);
        //TextView pickupPointTxt = (TextView)convertView.findViewById(R.id.pickupPointTxt);
        //TextView dropPointTxt = (TextView)convertView.findViewById(R.id.dropPointTxt);

        // getting movie data for the row
        DriverJobLists jobList = jobItemList.get(position);

        // thumbnail image
       // thumbNail.setImageUrl(jobList.getThumbnailUrl(), imageLoader);
        //imageView.setImageResource(R.drawable.ic_launcher);

        // Job Title
        jobId.setText(String.valueOf(jobList.getJobId()));

        // Customer name
        customerName.setText(String.valueOf(jobList.getCustomerName()));

        //Customer Contact No:
        //customerMobileNo.setText(String.valueOf(jobList.getCustomerMobileNo()));

        // Pickup Point
        Resources res = convertView.getResources();
        pickupPoint.setText(String.valueOf(jobList.getPickupPoint()));

        dropPoint.setText(String.valueOf(jobList.getDestAddress()));

        // release year
        // java.util.Date time= new java.util.Date((long)jobList.getScheduleTime()*1000);


        long lBookingDate  = jobList.getScheduleTime();
        //Log.v(TAG, "[Guru]:" + String.valueOf(lBookingDate));

        Date resultDate = new Date(lBookingDate);
        //resultDate.setTime(jobList.getScheduleTime());
        String formattedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(resultDate);
        //System.out.println(formattedDate);
        //Log.v(TAG, "[Guru]:" + formattedDate);
        scheduleTime.setText(formattedDate);

        return convertView;
    }
}
