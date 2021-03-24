package com.gieasesales.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.Util;
import com.gieasesales.interfaces.VolleyResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static com.gieasesales.utils.Util.ORDER_SCAN;
import static com.gieasesales.utils.Util.getData;

public class ScanActivity extends AppCompatActivity {
    CommonAlertDialog alert;
    TextView datatext;
    ImageView btn_camera, back_arrow;
    EditText orderdata;
    String BaseString = "", PosId = "";
    Button btn_submit;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int GALLERY = 2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Scan Your Order");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        datatext = findViewById(R.id.datatext);
        datatext.setText("");
        btn_camera = findViewById(R.id.btn_camera);
        btn_submit = findViewById(R.id.btn_submit);
        orderdata = findViewById(R.id.orderdata);
        back_arrow = findViewById(R.id.back_arrow);
        alert = new CommonAlertDialog(this);
        PosId = getIntent().getStringExtra("PosId");

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //final CharSequence[] items = {"Take Photo", "Cancel"};
                final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ScanActivity.this);
                builder.setTitle("Add Photo!");
                builder.setCancelable(false);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            //  PROFILE_PIC_COUNT = 1;
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                            }

                        } else if (items[item].equals("Choose from Library")) {
                            // startActivityForResult(intent, GALLERY);
                            pickFromGallery();
                        } else if (items[item].equals("Cancel")) {
                            //  PROFILE_PIC_COUNT = 0;
                            dialog.dismiss();
                            // finish();
                        }
                    }
                });
                builder.show();

                /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }*/
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderdata.getEditableText().toString().trim();
                if (orderdata.getEditableText().toString().isEmpty() && BaseString.isEmpty()) {
                    alert.build("Scan Order Copy or Enter order",false);
                } else if (orderdata.getEditableText().toString().equalsIgnoreCase(" ")) {
                    alert.build("Enter order",false);
                } else {
                    updateorder();
                }
            }
        });
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        orderdata.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String str = s.toString();
                if (str.equalsIgnoreCase(" ")||str.equalsIgnoreCase("  ")) {
                    // orderdata.setError("Space is not allowed");
                    orderdata.setText("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void updateorder() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerOrderImageId", "0");
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));

            obj.put("OrderData", orderdata.getEditableText().toString());
            obj.put("PosId", PosId);
            obj.put("ImageData", BaseString);
            obj.put("InputType", "2");

            Util.Logcat.e("REVIEW:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), ORDER_SCAN, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error),false);
                        //Util.ErrorLog(ScanActivity.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error),false);
                        //Util.ErrorLog(ScanActivity.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("OUTPUT:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                        //alert.build(resobject.getString("StatusDesc"));

                        datatext.setText("");
                        orderdata.setText("");
                        BaseString = "";

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            alert.build(resobject.getString("StatusDesc"),true);
                            //finish();
                        } else if (resobject.getString("Status").equalsIgnoreCase("1")) {
                            alert.build(resobject.getString("StatusDesc"),false);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY);
        // startActivityForResult(intent, GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode", "" + requestCode);
        Log.e("resultCode", "" + resultCode);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //imageView.setImageBitmap(imageBitmap);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 8;
            //  Bitmap bitmap = BitmapFactory.decodeFile(tempImageFile.getAbsolutePath(), bmOptions);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            BaseString = Base64.encodeToString(byteArray, Base64.DEFAULT);
            // BaseString = Util.BitMapToString(imageBitmap);
            datatext.setText("Scan Success");
            Log.e("BaseString", BaseString);

        } else if (requestCode == GALLERY && resultCode == RESULT_OK) {

            Toast.makeText(this,
                    "Image Selected ", Toast.LENGTH_SHORT)
                    .show();

            if (data != null && data.getData() != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    if (inputStream != null) {

                        // No special persmission needed to store the file like that
                        FileOutputStream fos = openFileOutput("temp", Context.MODE_PRIVATE);

                        final int BUFFER_SIZE = 1 << 10 << 3; // 8 KiB buffer
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int bytesRead = -1;
                        while ((bytesRead = inputStream.read(buffer)) > -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                        inputStream.close();
                        fos.close();

                        File tempImageFile = new File(getFilesDir() + "/" + "temp");
                        // File image = new File(sd+filePath, imageName);
                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        bmOptions.inSampleSize = 8;
                        Bitmap bitmap = BitmapFactory.decodeFile(tempImageFile.getAbsolutePath(), bmOptions);
                        // bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 25, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        BaseString = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        datatext.setText("Image Upload Success");
                        // BaseString=encoded;
                        Log.e("BaseString", BaseString);
                        //  btn_camera.setImageBitmap(bitmap);
                        // Do whatever you want with the File

                        // Delete when not needed anymore
                        deleteFile("temp");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
           /* Uri selectedImageUri = data.getData();
            String selectedImagePath = getRealPathFromURI(selectedImageUri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            options.inSampleSize = 8;
            Bitmap bmap = BitmapFactory.decodeFile(selectedImagePath,
                    options);
            bmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            byte[] byteArray = stream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);*/

        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private String getRealPathFromURI(Uri uri) {

        if (uri == null) {
            return null;
        }

        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return uri.getPath();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);

    }

}
