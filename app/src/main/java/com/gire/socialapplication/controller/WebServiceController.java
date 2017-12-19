package com.gire.socialapplication.controller;

import android.content.Context;
import android.util.Log;

import com.gire.socialapplication.constants.APIConstants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import cz.msebera.android.httpclient.Header;
import java.io.UnsupportedEncodingException;

/**
 * Created by girish on 6/14/2017.
 */

public class WebServiceController {

    Context context;
    WebInterface myInterface;
    boolean progressFlag = false;

    public WebServiceController(Context context, Object obj) {
        this.context = context;
        this.myInterface = (WebInterface) obj;
    }

    public void getRequest(String url, final int flag, final boolean progressFlag) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                // TODO Auto-generated method stub
                String response = "";
                try {
                    response = new String(arg2, "UTF-8");

                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                myInterface.getResponse(response, flag);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                myInterface.getResponse("failed_to_get_data", flag);
            }

        });

    }

    public void postRequest(String url, RequestParams params, final int flag) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        Log.e("url", url + params.toString());
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                // TODO Auto-generated method stub
                String response = "";

                try {
                    response = new String(arg2, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                myInterface.getResponse(response, flag);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                // TODO Auto-generated method stub
                myInterface.getResponse("error_response", flag);
            }

        });

    }

    public void uploadImageData(String url, MultipartEntity multipartEntity, Integer flag){
        try{
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity = multipartEntity;
            post.setEntity(reqEntity);
            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();
            final String response_str = EntityUtils.toString(multipartEntity);
            if (resEntity != null) {
                myInterface.getResponse(response_str, flag);
            }else {
                myInterface.getResponse(response_str, flag);
            }
        }catch (Exception e){
            e.printStackTrace();
            myInterface.getResponse("error", flag);
        }
    }

}
