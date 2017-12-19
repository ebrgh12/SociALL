package com.gire.socialapplication.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gire.socialapplication.R;
import com.gire.socialapplication.constants.APIConstants;
import com.gire.socialapplication.constants.CommonConstants;
import com.gire.socialapplication.controller.WebInterface;
import com.gire.socialapplication.controller.WebServiceController;
import com.gire.socialapplication.interfaces.LocationUpdate;
import com.gire.socialapplication.service.UpdateUserLocation;
import com.gire.socialapplication.userPreference.SharedPreferencesUtil;
import com.gire.socialapplication.utils.ApplicationConstants;
import com.gire.socialapplication.utils.ProgressIndicator;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import static com.gire.socialapplication.constants.APIConstants.UPDATE_LOCATION;

public class LoginActivity extends AppCompatActivity implements WebInterface,LocationUpdate {

    EditText ph_num,password;
    TextView new_user;
    Button login;
    private String android_id;
    WebServiceController webServiceController;
    ProgressIndicator progressIndicator;
    GoogleCloudMessaging gcmObj;
    String regId = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        webServiceController = new WebServiceController(LoginActivity.this,LoginActivity.this);
        progressIndicator = new ProgressIndicator();

        init();

    }

    private void init() {

        ph_num = (EditText)findViewById(R.id.ph_num);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        new_user = (TextView)findViewById(R.id.new_user);

        android_id = Settings.Secure.getString(LoginActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressIndicator.showProgress(LoginActivity.this);
                getDeviceId();
            }
        });

        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUp.class);
                intent.putExtra("device_id",android_id);
                startActivity(intent);
                finish();
            }
        });

    }

    private void getDeviceId() {
        /**
         *
         * call the API and once Success then move to next fragment
         *
         * */
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regId = gcmObj.register(ApplicationConstants.GOOGLE_PROJ_ID);
                    msg = regId;
                } catch (Exception e){
                    msg = "Error :" + e.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {

                    RequestParams params = new RequestParams();
                    params.put("phone_no",ph_num.getText().toString());
                    params.put("password",password.getText().toString());
                    params.put("device_id",regId);
                    webServiceController.postRequest(APIConstants.BASE_URL+"api/login",params,1);

                } else {
                    progressIndicator.DismissProgress();

                    Toast.makeText(LoginActivity.this,"failure gcm",Toast.LENGTH_SHORT);
                }
            }
        }.execute(null, null, null);
    }

    @Override
    public void getResponse(String response, int flag) {
        switch (flag){
            case 1:
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("status"))
                    {
                        SharedPreferencesUtil.getInstance(this).setUserPriff(
                                jsonObject.getJSONObject("response").getString("user_id"),
                                jsonObject.getJSONObject("response").getString("name"),
                                jsonObject.getJSONObject("response").getString("email_id"),
                                jsonObject.getJSONObject("response").getString("phone_no"),
                                jsonObject.getJSONObject("response").getString("profile_image"));

                        RequestParams requestParams = new RequestParams();
                        requestParams.put("lattitude",String.valueOf(CommonConstants.latitude));
                        requestParams.put("longitude",String.valueOf(CommonConstants.longitude));
                        requestParams.put("user_id", SharedPreferencesUtil.getInstance(LoginActivity.this).getCustomerID());

                        webServiceController.postRequest(UPDATE_LOCATION,requestParams,2);

                    }else {
                        progressIndicator.DismissProgress();
                        Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressIndicator.DismissProgress();
                    e.printStackTrace();
                }
                break;
            case 2:
                progressIndicator.DismissProgress();
                locationUpdate();
                break;
        }

    }

    @Override
    public void locationUpdate() {
        Toast.makeText(this, "User Login Successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

}
