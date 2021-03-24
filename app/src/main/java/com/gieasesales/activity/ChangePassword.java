package com.gieasesales.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gieasesales.Http.CallApi;
import com.gieasesales.R;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.Util;
import com.gieasesales.interfaces.VolleyResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import static com.gieasesales.utils.Util.CHANGE_PASSWORD;
import static com.gieasesales.utils.Util.getData;

public class ChangePassword extends AppCompatActivity {

    Button BtnSave;
    CommonAlertDialog alert;
    EditText EdOldPassword, EdNewPassword, EdConfirmPassword;
    ImageView back_arrow;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Change Password");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        EdOldPassword = findViewById(R.id.old_pass);
        EdNewPassword = findViewById(R.id.new_pass);
        back_arrow = findViewById(R.id.back_arrow);
        EdConfirmPassword = findViewById(R.id.confirm_pass);
        BtnSave = findViewById(R.id.btn_save);
        alert = new CommonAlertDialog(this);
        BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EdOldPassword.getEditableText().toString().isEmpty()) {
                    alert.build("Enter Old Password",false);
                } else if (EdNewPassword.getEditableText().toString().isEmpty()) {
                    alert.build("Enter New Password",false);
                } else if (EdConfirmPassword.getEditableText().toString().isEmpty()) {
                    alert.build("Enter Confirm Password",false);
                } else if ((!EdNewPassword.getEditableText().toString().equalsIgnoreCase(EdConfirmPassword.getEditableText().toString()))) {
                    alert.build("New and Confirm Password are not same",false);
                } else
                    Changepass();
            }
        });
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Changepass() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("LoginId", getData("LoginId", getApplicationContext()));
            obj.put("OldPassword", EdOldPassword.getEditableText().toString());
            obj.put("NewPassword", EdNewPassword.getEditableText().toString());
            Util.Logcat.e("ChangePassword:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponse(this, params.toString(), CHANGE_PASSWORD, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    if (message.contains("TimeoutError")) {
                        alert.build(getString(R.string.timeout_error),false);
                        //Util.ErrorLog(ChangePassword.this, getString(R.string.timeout_error));
                    } else {
                        alert.build(getString(R.string.server_error),false);
                        //Util.ErrorLog(ChangePassword.this, getString(R.string.server_error));
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

                            Util.saveData("loginpass", EdNewPassword.getEditableText().toString(), getApplicationContext());
                            EdOldPassword.setText("");
                            EdNewPassword.setText("");
                            EdConfirmPassword.setText("");

                            close(resobject.getString("StatusDesc"));
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

    private void close(final String s) {
        final AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(this, R.style.alertDialog);
        alertDialogBuilder.setMessage(s);
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //  Intent login = new Intent(ChangePassword.this, Login.class);
                        // login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                    }
                });


        AlertDialog alert = alertDialogBuilder.create();
        alert.setCancelable(true);
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
