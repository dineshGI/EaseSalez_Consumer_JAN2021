package com.gieasesales.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.PhotoProvider;
import com.gieasesales.utils.Util;
import com.gieasesales.interfaces.VolleyResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.gieasesales.utils.Util.GET_PROFILE;
import static com.gieasesales.utils.Util.PROFILE_PIC;
import static com.gieasesales.utils.Util.PROFILE_UPDATE;
import static com.gieasesales.utils.Util.getData;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    Button BtnSave, BtnUpload, BtnView;
    CommonAlertDialog alert;
    EditText EdName, EdEmail, EdMobile, EdAddress1, EdCity, EdPincode;
    //EdDocNo,EdAddress2,EdDob;
    // Spinner SpinDocType;
    // Spinner SpinGender;
    List<String> Doclist;
    String IsFirstLogin;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    String imageStoragePath, Base64Img = "", Profile64Img = "", DOCPath = "";
    ImageView BtnCalender, BtnProfilePic;
    TextView KYCStatus;
    boolean profile = false;
    ImageView back_arrow;
    String finish;

    //₹
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        alert = new CommonAlertDialog(this);
        EdName = findViewById(R.id.p_name);
        EdEmail = findViewById(R.id.p_email);
        EdMobile = findViewById(R.id.p_mobile);
        EdAddress1 = findViewById(R.id.p_address_one);
        // EdAddress2 = findViewById(R.id.p_address_two);
        EdCity = findViewById(R.id.p_city);
        EdPincode = findViewById(R.id.p_pincode);
        //EdDob = findViewById(R.id.p_dob);
        //SpinGender = findViewById(R.id.p_gender);
        //EdDocNo = findViewById(R.id.p_docno);
        BtnSave = findViewById(R.id.p_save);
        back_arrow = findViewById(R.id.back_arrow);
        BtnSave.setOnClickListener(this);
        BtnUpload = findViewById(R.id.upload);
        BtnUpload.setOnClickListener(this);
        BtnView = findViewById(R.id.view);
        BtnView.setOnClickListener(this);
        BtnCalender = findViewById(R.id.cal);
        BtnCalender.setOnClickListener(this);
        BtnProfilePic = findViewById(R.id.prof_upload);
        BtnProfilePic.setOnClickListener(this);
        KYCStatus = findViewById(R.id.doc_text);

        IsFirstLogin = Util.getData("IsFirstLogin", getApplicationContext());
        finish = getIntent().getStringExtra("finish");

        if (finish.equalsIgnoreCase("true")) {
            back_arrow.setVisibility(View.INVISIBLE);
        }
        List<String> list = new ArrayList<>();
        list.add("Male");
        list.add("Female");

        ArrayAdapter<String> GenderArrayAdapter = new ArrayAdapter<>
                (Profile.this, R.layout.spinner_textview,
                        list); //selected item will look like a spinner set from XML
        GenderArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (Util.getData("SaveProfilePic", getApplicationContext()) != null)
            BtnProfilePic.setImageBitmap(Util.convertBase64ToBitmap(Util.getData("SaveProfilePic", getApplicationContext())));
        //GetDoctype();
        GetProfile();
    }

    private void showImage(String docPath) {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(this);
        //  imageView.setImageURI(imageUri);
        Glide.with(Profile.this).load(docPath).into(imageView);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }

    private void Opencamera() {
        final CharSequence[] items = {"Take Photo", "Cancel"};
        //final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload Photo");
        builder.setCancelable(false);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    //  PROFILE_PIC_COUNT = 1;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//com.cheerz
                    File file = PhotoProvider.getOutputMediaFile(1);
                    if (file != null) {
                        imageStoragePath = file.getAbsolutePath();
                    }

                    Uri fileUri = PhotoProvider.getOutputMediaFileUri(Profile.this, file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[item].equals("Choose from Library")) {
                    //  PROFILE_PIC_COUNT = 1;
                   /* Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);*/
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                    // startActivityForResult(intent, SELECT_FILE);

                } else if (items[item].equals("Cancel")) {
                    //  PROFILE_PIC_COUNT = 0;
                    dialog.dismiss();
                    finish();
                }
            }
        });
        builder.show();
    }

    private void GetProfile() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));
            Util.Logcat.e("GET PROFILE::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), GET_PROFILE, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(Profile.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(Profile.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {

                    //Util.Logcat.e("onResponse : " + response);

                    try {
                        Util.Logcat.e("PROFILE:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            if (resobject.getString("Status").equalsIgnoreCase("0")) {
                                JSONArray jsonArray = resobject.optJSONArray("_GetConsumerDetailsOutput");
                                if (jsonArray == null || jsonArray.length() == 0) {
                                    Util.Logcat.e("EMPTY | Null:::" + String.valueOf(jsonArray.length()));
                                    Util.ShowToast(Profile.this, getString(R.string.NoIteminCart));
                                } else {

                                    JSONObject imageobject = jsonArray.getJSONObject(0);
                                    if (!imageobject.getString("KYCStatusId").equalsIgnoreCase("1")) {
                                        BtnUpload.setVisibility(View.VISIBLE);
                                    } else {
                                        BtnUpload.setVisibility(View.GONE);
                                    }

                                    KYCStatus.setText(imageobject.getString("KYCStatus"));

                                    //  Glide.with(Profile.this).load(imageobject.getString("ProfileImagePath")).into(BtnProfilePic);

                                    EdName.setText(imageobject.getString("Name"));
                                    EdEmail.setText(imageobject.getString("EmailId"));
                                    EdAddress1.setText(imageobject.getString("Address1"));
                                    //EdAddress2.setText(imageobject.getString("Address2"));
                                    EdCity.setText(imageobject.getString("City"));
                                    EdPincode.setText(imageobject.getString("Pincode"));
                                    //EdDob.setText(imageobject.getString("DOB"));
                                    EdMobile.setText(imageobject.getString("MobileNo"));
                                    //EdDocNo.setText(imageobject.getString("DocumentNo"));
                                    DOCPath = imageobject.getString("DocumentPath");
                                    Util.Logcat.e("DOCPath" + DOCPath);

                                }
                            }
                        } else if (resobject.getString("Status").equalsIgnoreCase("1")) {
                            alert.build(resobject.getString("StatusDesc"), false);
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

    private void SaveDetails() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("UserId", getData("ConsumerId", getApplicationContext()));
            obj.put("Name", EdName.getEditableText().toString());
            obj.put("EmailId", EdEmail.getEditableText().toString());
            obj.put("Address1", EdAddress1.getEditableText().toString());
            obj.put("Address2", "");
            obj.put("City", EdCity.getEditableText().toString());
            obj.put("Pincode", EdPincode.getEditableText().toString());
            obj.put("DOB", "");
            obj.put("Gender", "");
            obj.put("MobileNo", EdMobile.getEditableText().toString());
            obj.put("InputType", "2");
            //imageupdate = 1;
            //other details=0;
            obj.put("IDProofType", "0");
            obj.put("IDProofNo", "");
            if (Base64Img.isEmpty()) {
                obj.put("KYCImageStatus", "0");
            } else {
                obj.put("KYCImageStatus", "1");
            }
            obj.put("IDProofImage", Base64Img);
            Util.Logcat.e("PROFILE UPDATE:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), PROFILE_UPDATE, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(Profile.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(Profile.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);

                    try {
                        Util.Logcat.e("OUTPUT:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {

                            Util.ShowToast(Profile.this, resobject.getString("StatusDesc"));
                            if (finish.equalsIgnoreCase("true")) {

                                if (Profile.this.getPackageName().equals("com.gieasesales")) {
                                    Intent home = new Intent(Profile.this, MainActivity.class);
                                    startActivity(home);
                                    finish();
                                }

                            } else if (finish.equalsIgnoreCase("false")) {
                                closeNew(resobject.getString("StatusDesc"));
                            } else {
                                alert.build(resobject.getString("StatusDesc"), false);
                            }

                        } else if (resobject.getString("Status").equalsIgnoreCase("1")) {
                            alert.build(resobject.getString("StatusDesc"), false);
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

    private void closeNew(final String s) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.cutom_alert);
        // set the custom dialog components - text, image and button
        TextView text = dialog.findViewById(R.id.texts);
        LinearLayout success = dialog.findViewById(R.id.success);
        //LinearLayout failure = dialog.findViewById(R.id.failure);
        text.setText(s);
        //Util.Logcat.e("STATUS>" + value);

        success.setVisibility(View.VISIBLE);
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // image.setImageResource(R.mipmap.ic_launcher);
        Button dialogButton = dialog.findViewById(R.id.okay);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void close(final String s) {
        final AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(this, R.style.alertDialog);
        alertDialogBuilder.setMessage(s);
        alertDialogBuilder.setPositiveButton("Ok",

                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        finish();
                    }
                });


        AlertDialog alert = alertDialogBuilder.create();
        alert.setCancelable(true);
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(item);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Util.Logcat.e("requestCode" + String.valueOf(requestCode));
        switch (requestCode) {
            case REQUEST_CAMERA:

                Bitmap bitmap = PhotoProvider.optimizeBitmap(8, imageStoragePath);
                if (bitmap != null) {
                    try {
                        // PhotoProvider.refreshGallery(getActivity(), imageStoragePath);
                        bitmap = PhotoProvider.optimizeBitmap(8, imageStoragePath);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        //callapi(encoded);

                        if (profile) {
                            Profile64Img = Util.BitMapToString(bitmap);
                            BtnProfilePic.setImageBitmap(bitmap);
                            Util.saveData("SaveProfilePic", Util.BitMapToString(bitmap), getApplicationContext());
                            UpdateProfilePic(Profile64Img);
                        } else {
                            Base64Img = Util.BitMapToString(bitmap);
                        }

                    } catch (Exception e) {

                    }
                } else {
                    finish();
                }
                break;
            case SELECT_FILE:

                if (resultCode == RESULT_OK && data != null) {
                    Toast.makeText(this,
                            "Image Selected ", Toast.LENGTH_SHORT)
                            .show();
                    Uri selectedImageUri = data.getData();
                    String selectedImagePath = getRealPathFromURI(selectedImageUri);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    options.inSampleSize = 8;
                    Bitmap bmap = BitmapFactory.decodeFile(selectedImagePath,
                            options);
                    bmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                    byte[] byteArray = stream.toByteArray();
                    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    //Log.e("IMG", encoded);

                    // callapi(encoded);
                } else {
                    finish();
                }

                break;
            default:
                break;
        }
    }

    private void UpdateProfilePic(String Image) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("ConsumerId", getData("ConsumerId", getApplicationContext()));
            obj.put("InputType", "2");
            obj.put("ImagePath", Image);
            Util.Logcat.e("UpdateProfilePic:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), PROFILE_PIC, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error), false);
                        //Util.ErrorLog(Profile.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error), false);
                        //Util.ErrorLog(Profile.this, getString(R.string.server_error));
                    }
                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("OUTPUT:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));

                        if (resobject.getString("Status").equalsIgnoreCase("0")) {
                            GetProfile();
                            Util.ShowToast(Profile.this, resobject.getString("StatusDesc"));
                        } else if (resobject.getString("Status").equalsIgnoreCase("1")) {
                            alert.build(resobject.getString("StatusDesc"), false);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prof_upload:
                profile = true;
                Opencamera();
                break;
            case R.id.upload:
                Opencamera();
                break;
            case R.id.view:
                showImage(DOCPath);
                break;
            case R.id.cal:
                /*final Calendar c = Calendar.getInstance();
                Integer mYear = c.get(Calendar.YEAR);
                Integer mMonth = c.get(Calendar.MONTH);
                Integer mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Profile.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        if (monthOfYear <= 8 && dayOfMonth > 9) {
                            String _data = dayOfMonth + "/" + "0" + (monthOfYear + 1) + "/" + year;
                            EdDob.setText(_data);
                        } else if (monthOfYear <= 8 && dayOfMonth <= 9) {
                            String _data = "0" + dayOfMonth + "/" + "0" + (monthOfYear + 1) + "/" + year;
                            EdDob.setText(_data);
                        } else {
                            if (dayOfMonth <= 9) {
                                String _data = "0" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                EdDob.setText(_data);
                            } else {
                                String _data = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                EdDob.setText(_data);
                            }

                        }
                        Calendar userAge = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                        Calendar minAdultAge = new GregorianCalendar();
                        minAdultAge.add(Calendar.YEAR, -18);
                        if (minAdultAge.before(userAge)) {
                            alert.build("Age Should not be less than 18");
                            EdDob.setText("");
                        }

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
                break;*/
            case R.id.p_save:
                if (EdName.getEditableText().toString().isEmpty()) {
                    alert.build(getString(R.string.enter_name), false);
                } else if (EdEmail.getEditableText().toString().isEmpty()) {
                    alert.build(getString(R.string.enter_email), false);
                } else if (Util.isEmailValid(EdEmail.getEditableText().toString()) == false) {
                    alert.build(getString(R.string.enter_valid_email), false);
                } else if (EdAddress1.getEditableText().toString().isEmpty()) {
                    alert.build(getString(R.string.enter_address), false);
                } else if (EdCity.getEditableText().toString().isEmpty()) {
                    alert.build(getString(R.string.enter_city), false);
                }/* else if (EdDob.getEditableText().toString().isEmpty()) {
                    alert.build(getString(R.string.enter_dob));
                } else if (EdGender.getEditableText().toString().isEmpty()) {
                    alert.build(getString(R.string.enter_gender));
                } else if (EdDocNo.getEditableText().toString().isEmpty()) {
                    alert.build(getString(R.string.enter_docno));
                }*/ else {
                    SaveDetails();
                }
                break;
            default:
                break;
        }
    }
}
