package example.android.astrofivepagerapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.LocationRequest;


import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Register extends AppCompatActivity{

    EditText et_name, et_email, et_mobile,et_countrycode;
    Button register;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    TextView cleardata;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    TextInputLayout t_name,t_mobile,t_countrycode,t_email;
    private static final int REQUEST_LOCATION_TURN_ON = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(Register.this);

        et_email = (EditText) findViewById(R.id.et_email);
        et_name = (EditText) findViewById(R.id.et_name);
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_countrycode = (EditText) findViewById(R.id.et_countrycode);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        SharedPreferences pref1 = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref1.edit();
        String email = pref1.getString("email", ""); // getting String
        String name = pref1.getString("name", ""); // getting String
        String mobile = pref1.getString("mobile", ""); // getting String
        String countrycode = pref1.getString("countrycode", ""); // getting String

        et_email.setText(email);
        et_mobile.setText(mobile);
        et_name.setText(name);
        if(countrycode.length()>1) {
            et_countrycode.setText(countrycode);
        }else
        {
            et_countrycode.setText("91");
        }


        register = (Button) findViewById(R.id.register);
        cleardata = (TextView) findViewById(R.id.cleardata);
        cleardata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_email.setText("");
                et_mobile.setText("");
                et_name.setText("");

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                    callregister(et_name.getText().toString(), et_email.getText().toString(), /*et_countrycode.getText().toString()+*/et_mobile.getText().toString(),"Paid");

                }
            }
        });

    }

    Boolean validate() {

        if (et_name.getText().toString().trim().length() == 0) {
            et_name.setError("Please Enter  name");
            et_name.requestFocus();
            return false;
        }
        if (!et_name.getText().toString().trim().matches("[a-zA-Z. ]+")) {
            et_name.setError("Please enter valid  name");
            et_name.requestFocus();
            return false;
        }
       if (!et_mobile.getText().toString().trim().matches("[0-9]{10}")) {
            et_mobile.setError("Please enter valid mobile number");
           et_mobile.requestFocus();
            return false;
        }
        if (et_mobile.getText().toString().trim().length() == 0) {
            et_mobile.setError("Please enter valid mobile number");

            return false;
        } else if (et_mobile.getText().toString().trim().length() != 10) {
            et_mobile.setError("Please enter valid mobile number");
            return false;
        } else if (!(et_mobile.getText().toString().trim().startsWith("9") || et_mobile.getText().toString().trim().startsWith("8") || et_mobile.getText().toString().trim().startsWith("7") || et_mobile.getText().toString().trim().startsWith("6"))) {
            et_mobile.setError("Please enter mobile number start with 9 or 8 or 7 or 6");
            return false;
        }
        if (!(et_countrycode.getText().toString().length() >=1)) {
            et_countrycode.setError("Please enter valid Country Code");
          et_countrycode.requestFocus();
            return false;
        }
        if (!Pattern.matches("[0-9]{1,3}", et_countrycode.getText().toString())) {
            et_countrycode.setError("Please enter valid Country Code");
            et_countrycode.requestFocus();
            return false;
        }
        if (et_email.getText().toString().trim().length() == 0) {
            et_email.setError("Please Enter Email");
         et_email.requestFocus();
            return false;

        }
        if (!et_email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            //   Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
            et_email.setError("Please Enter valid Email id");
         //   t_email.requestFocus();
            return false;

        }

        /*String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";*/


        return true;

    }

    private void callregister(String name, String emailid, String mobileno,String Type) {
        progressDialog.show();
        ApiInterface apiGetClippingList = ApiClient.getClient().create(ApiInterface.class);
        Call<Registerresponse> callGetClippingList;
        callGetClippingList = apiGetClippingList.register(name, emailid, mobileno,Type);
        Log.d("", "apiGetCategoryList url : " + callGetClippingList.request().url());
        callGetClippingList.enqueue(new Callback<Registerresponse>() {
            @Override
            public void onResponse(Call<Registerresponse> call, Response<Registerresponse> response) {

                try {
                    if (response.body() != null) {
                        progressDialog.dismiss();
                            Toast.makeText(Register.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                        editor.putString("email", et_email.getText().toString()); // Storing string
                        editor.putString("name", et_name.getText().toString()); // Storing string
                        editor.putString("mobile", et_mobile.getText().toString()); // Storing string
                        editor.putString("active", response.body().getMessage()); // Storing string
                        editor.putString("countrycode", et_countrycode.getText().toString()); // Storing string
                        editor.commit();

                            if (response.body().getMessage().equalsIgnoreCase("Exist") || response.body().getMessage().equalsIgnoreCase("Success")) {


                                Intent i = new Intent(Register.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        } else{
                            showResponseAlert("Server not responding", false);
                        progressDialog.dismiss();



                    }
                    } catch(Exception e){
                    progressDialog.dismiss();


                    e.printStackTrace();

                    }
                }

            @Override
            public void onFailure(Call<Registerresponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Register.this, "Something went wrong" + t.toString(), Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void showResponseAlert(String message, final Boolean Flag) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Register.this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (Flag) {
                            if(progressDialog.isShowing() || progressDialog!=null){
                                progressDialog.dismiss();
                            }
                            Intent i = new Intent(Register.this, MainActivity.class);
                            startActivity(i);
                            finish();

                        } else {
                            Toast.makeText(Register.this, "Please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }



    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;





}