package com.carmozo.driverapp.Activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.carmozo.driverapp.R;
import com.carmozo.driverapp.UiUtilities.C;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by shreyasgs on 06-11-2015.
 */
public class DriverInvoiceMgmt  extends AppCompatActivity implements View.OnClickListener {

    String orderId;
    Button captureBtn = null;
    final int CAMERA_CAPTURE = 1;
    final int CAPTURED_PHOTOS_CNT = 3;
    private Uri picUri;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private GridView grid;
    private List<String> listOfImagesPath;
    private boolean[] thumbnailsSelection;
    private Uri imageToUploadUri;

    private String InvoicePhoto_ImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/carmozo/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_car_photo_main);

        //-- Job request ID
        orderId = getIntent().getExtras().getString(C.jsonKey.DRIVER_ORDER_RESP_ORDER_ID_KEY);

        InvoicePhoto_ImagePath += "/" + orderId + "/";

        listOfImagesPath = null;
        listOfImagesPath = RetrieveCapturedImagePath() ;

        captureBtn = (Button)findViewById(R.id.capture_btn1);
        captureBtn.setOnClickListener(this);

        grid = (GridView)findViewById(R.id.gridview);
        grid.setAdapter(new ImageListAdapter(this, listOfImagesPath));

        thumbnailsSelection = new boolean[listOfImagesPath.size()];
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(DriverInvoiceMgmt.this, "You clicked at " + (+position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View arg0)
    {
        if(arg0.getId() == R.id.capture_btn1) {
            try {

                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File imageDirectory = new File(InvoicePhoto_ImagePath);
                if(!imageDirectory.exists()) {
                    //Toast.makeText(this, "Image Directory Created", Toast.LENGTH_SHORT).show();
                    imageDirectory.mkdirs();
                }

                String imgcurTime = dateFormat.format(new Date());
                String _path = InvoicePhoto_ImagePath + imgcurTime + ".jpg" ;

                imageToUploadUri = Uri.fromFile(new File(_path));

                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageToUploadUri);

                startActivityForResult(captureIntent, CAMERA_CAPTURE);

            } catch(ActivityNotFoundException anfe){
                String errorMessage = "Whoops - your device doesn't support capturing images !";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_CAPTURE){

                listOfImagesPath = null;
                listOfImagesPath = RetrieveCapturedImagePath();
                thumbnailsSelection = new boolean[listOfImagesPath.size()];

                if(listOfImagesPath != null) {
                    grid.setAdapter(new ImageListAdapter(this, listOfImagesPath));
                }
                if(listOfImagesPath.size() > CAPTURED_PHOTOS_CNT)
                    captureBtn.setEnabled(false);
                else
                    captureBtn.setEnabled(true);
            }
        }
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

    public class ImageListAdapter extends BaseAdapter {

        private Context context;
        private List<String> imgPic;
        private LayoutInflater inflater;

        public ImageListAdapter (Context c, List<String> thePic) {
            inflater = LayoutInflater.from(c);
            context = c;
            imgPic = thePic;
        }

        public int getCount() {
            if(imgPic != null)
                return imgPic.size();
            else
                return 0;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position){
            return position;
        }

        public Bitmap decodeFile(String path) {
            Bitmap b = null;
            File f = new File(path);
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(f);
                BitmapFactory.decodeStream(fis, null, o);
                fis.close();

                int IMAGE_MAX_SIZE = 1024; // maximum dimension limit
                int scale = 1;
                if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                    scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
                }

                // Decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;

                fis = new FileInputStream(f);
                b = BitmapFactory.decodeStream(fis, null, o2);
                fis.close();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return b;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            ImageView picture;
            //TextView name;
            CheckBox itemCheckbox;

        /*
        BitmapFactory.Options bfOptions = new BitmapFactory.Options();
        bfOptions.inDither = false;
        bfOptions.inPurgeable = true;
        bfOptions.inInputShareable = true;
        bfOptions.inTempStorage = new byte[32 * 1024];*/

            if (v == null) {
                v = inflater.inflate(R.layout.driver_car_photo, parent, false);
                v.setTag(R.id.picture, v.findViewById(R.id.picture));
                //v.setTag(R.id.text, v.findViewById(R.id.text));
                v.setTag(R.id.itemCheckBox, v.findViewById(R.id.itemCheckBox));
            }

            picture = (ImageView) v.getTag(R.id.picture);
            //name = (TextView) v.getTag(R.id.text);
            itemCheckbox = (CheckBox) v.getTag(R.id.itemCheckBox);

        /*
        FileInputStream fs = null;
        Bitmap bm;
        try {
            fs = new FileInputStream(new File(imgPic.get(position).toString()));
            if (fs != null) {
                bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
                picture.setImageBitmap(bm);
                picture.setId(position);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        */
            Bitmap bm = decodeFile(imgPic.get(position).toString());
            picture.setImageBitmap(bm);
            picture.setId(position);


            itemCheckbox.setId(position);
            itemCheckbox.setChecked(thumbnailsSelection[position]);
            //name.setId(position);
            //name.setText(imgPic.get(position).toString());

            itemCheckbox.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();

                    // TODO Delete selected images
                    if (thumbnailsSelection[id]) {
                        cb.setChecked(false);
                        thumbnailsSelection[id] = false;
                    } else {
                        cb.setChecked(true);
                        thumbnailsSelection[id] = true;

                        final String imgPath = imgPic.get(id).toString();
                        File file = new File(imgPath);
                        file.delete();
                        listOfImagesPath.remove(id);
                        //listOfImagesPath.notify();
                        grid.invalidateViews();
                    }
                }
            });

            if(imgPic.size() > CAPTURED_PHOTOS_CNT)
                //captureBtn.setEnabled(false);
                captureBtn.setText(R.string.upload_photos);
            else
                captureBtn.setEnabled(true);

            return v;
        }

    }

    private List<String> RetrieveCapturedImagePath() {
        List<String> tFileList = new ArrayList<String>();
        File f = new File(InvoicePhoto_ImagePath);
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
        return tFileList;
    }
}

