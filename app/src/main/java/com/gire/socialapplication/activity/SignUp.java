package com.gire.socialapplication.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gire.socialapplication.R;
import com.gire.socialapplication.constants.APIConstants;
import com.gire.socialapplication.constants.CommonConstants;
import com.gire.socialapplication.controller.WebInterface;
import com.gire.socialapplication.controller.WebServiceController;
import com.gire.socialapplication.interfaces.LocationUpdate;
import com.gire.socialapplication.service.UpdateUserLocation;
import com.gire.socialapplication.userPreference.CommonUtils;
import com.gire.socialapplication.userPreference.SharedPreferencesUtil;
import com.gire.socialapplication.utils.ProgressIndicator;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.gire.socialapplication.constants.APIConstants.UPDATE_LOCATION;

public class SignUp extends AppCompatActivity implements WebInterface,LocationUpdate {

    EditText usr_name,email,ph_num,pass,cnfrm;
    Button signup;
    Bundle bundle;
    ImageView userProfile;
    WebServiceController webServiceController;
    ProgressIndicator progressIndicator;
    String userChoosenTask;
    File userImage = null;
    HttpEntity resEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        webServiceController = new WebServiceController(SignUp.this,SignUp.this);
        progressIndicator = new ProgressIndicator();

        init();

    }

    private void init() {

        bundle = getIntent().getExtras();
        usr_name = (EditText)findViewById(R.id.usr_name);
        email = (EditText)findViewById(R.id.email);
        ph_num = (EditText)findViewById(R.id.ph_num);
        pass = (EditText)findViewById(R.id.pass);
        cnfrm = (EditText)findViewById(R.id.cnfrm);

        userProfile = (ImageView) findViewById(R.id.image_view);

        signup = (Button)findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!CommonUtils.isNotNull(usr_name.getText().toString()) ||
                        !CommonUtils.isNotNull(email.getText().toString()) ||
                        !CommonUtils.isNotNull(ph_num.getText().toString()) ||
                        !CommonUtils.isNotNull(pass.getText().toString()) ||
                        !CommonUtils.isNotNull(cnfrm.getText().toString()))
                {
                    Toast.makeText(SignUp.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }else
                {
                    if(pass.getText().toString().equals(cnfrm.getText().toString()))
                    {
                        if(userImage != null){
                            callAPI();
                        }else {
                            Toast.makeText(SignUp.this, "Please upload a profile pic.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),101);
    }

    public void callAPI()
    {
        progressIndicator.showProgress(SignUp.this);

        Thread thread=new Thread(new Runnable(){
            @Override
            public void run() {
                registerThreadAction(usr_name.getText().toString(),email.getText().toString(),
                        ph_num.getText().toString(),cnfrm.getText().toString(),bundle.getString("device_id"));
            }
        });
        thread.start();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100){
                onCaptureImageResult(data);
            }else if (requestCode == 101){
                onSelectFromGalleryResult(data);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                addFile(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userProfile.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();

            addFile(thumbnail);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        userProfile.setImageBitmap(thumbnail);
    }

    private void addFile(Bitmap thumbnail) {
        try{
            int width = thumbnail.getWidth();
            int height = thumbnail.getHeight();
            float scaleWidth = ((float) 1024) / width;
            float scaleHeight = ((float) 768) / height;
            // create a matrix for the manipulation
            Matrix matrix = new Matrix();
            // resize the bit map
            matrix.postScale(scaleWidth, scaleHeight);
            // recreate the new Bitmap
            Bitmap resizedBitmap = Bitmap.createBitmap(thumbnail, 0, 0, width, height, matrix, false);

            //create a file to write bitmap data
            File filesDir = getFilesDir();
            File imageFile = new File(filesDir, System.currentTimeMillis() + ".jpg");

            OutputStream os;
            try {
                os = new FileOutputStream(imageFile);
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
            }

            userImage = imageFile;
        }catch (Exception e){

        }
    }

    @Override
    public void getResponse(String response, int flag) {
        switch (flag){
            case 1:
                if(response.equals("error")){
                    progressIndicator.DismissProgress();
                    Toast.makeText(this, "error doing registration", Toast.LENGTH_SHORT).show();
                }else {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getBoolean("status"))
                        {

                            SharedPreferencesUtil.getInstance(SignUp.this).setUserPriff(
                                    jsonObject.getJSONObject("response").getString("user_id"),
                                    jsonObject.getJSONObject("response").getString("name"),
                                    jsonObject.getJSONObject("response").getString("email_id"),
                                    jsonObject.getJSONObject("response").getString("phone_no"),
                                    jsonObject.getJSONObject("response").getString("profile_image"));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RequestParams requestParams = new RequestParams();
                                    requestParams.put("lattitude",String.valueOf(CommonConstants.latitude));
                                    requestParams.put("longitude",String.valueOf(CommonConstants.longitude));
                                    requestParams.put("user_id", SharedPreferencesUtil.getInstance(SignUp.this).getCustomerID());

                                    webServiceController.postRequest(UPDATE_LOCATION,requestParams,2);
                                }
                            });

                        }else {
                            progressIndicator.DismissProgress();
                            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }catch (JSONException e) {
                        progressIndicator.DismissProgress();
                        e.printStackTrace();
                    }
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
        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignUp.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignUp.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void registerThreadAction(String s, String s1, String s2, String s3, String device_id){

        try{
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(APIConstants.BASE_URL+"api/registration");
            FileBody bin1 = null;
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            bin1 = new FileBody(userImage);
            reqEntity.addPart("image", bin1);
            reqEntity.addPart("name", new StringBody(s));
            reqEntity.addPart("email_id", new StringBody(s1));
            reqEntity.addPart("phone_no", new StringBody(s2));
            reqEntity.addPart("password", new StringBody(s3));
            reqEntity.addPart("device_id", new StringBody(device_id));

            post.setEntity(reqEntity);
            HttpResponse response = client.execute(post);
            resEntity = response.getEntity();
            final String response_str = EntityUtils.toString(resEntity);
            if (resEntity != null) {
                getResponse(response_str,1);
            }else {
                getResponse("error", 1);
            }
        }catch (Exception e){
            getResponse("error", 1);
            e.toString();
        }

    }
}