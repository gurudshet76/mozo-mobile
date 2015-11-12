package com.carmozo.driverapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carmozo.driverapp.R;
import com.carmozo.driverapp.UiUtilities.DriverDrawMenuListItem;

import java.util.ArrayList;

/**
 * Created by shreyasgs on 26-10-2015.
 */
public class DriverDrawMenuListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DriverDrawMenuListItem> driverDrawMenuListItem;

    public DriverDrawMenuListAdapter(Context context, ArrayList<DriverDrawMenuListItem> driverDrawMenuListItem){
        this.context = context;
        this.driverDrawMenuListItem = driverDrawMenuListItem;
    }

    @Override
    public int getCount() {
        return driverDrawMenuListItem.size();
    }

    @Override
    public Object getItem(int position) {
        return driverDrawMenuListItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.driver_drawer_list_layout, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

        imgIcon.setImageResource(driverDrawMenuListItem.get(position).getIcon());
        txtTitle.setText(driverDrawMenuListItem.get(position).getTitle());

        // displaying count
        // check whether it set visible or not
        if(driverDrawMenuListItem.get(position).getCounterVisibility()){
            txtCount.setText(driverDrawMenuListItem.get(position).getCount());
        }else{
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }

        return convertView;
    }
}
