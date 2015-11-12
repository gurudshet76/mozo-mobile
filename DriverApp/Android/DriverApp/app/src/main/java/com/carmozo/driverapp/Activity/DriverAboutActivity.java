package com.carmozo.driverapp.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.carmozo.driverapp.R;

/**
 * Created by shreyasgs on 04-10-2015.
 */
public class DriverAboutActivity extends AppCompatActivity {

    TextView abtAppname;
    TextView abtBuildno;
    TextView abtLastupdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_driver_app);

        abtAppname = (TextView)findViewById(R.id.abt_appname);
        abtAppname.setText("Carmozo Driver 1.0");

        abtBuildno = (TextView)findViewById(R.id.abt_buildno);
        abtBuildno.setText("Build #1936");

        abtLastupdate = (TextView)findViewById(R.id.abt_lastupdate);
        abtLastupdate.setText("Last updated: 22nd Sept 2015");
    }
}
