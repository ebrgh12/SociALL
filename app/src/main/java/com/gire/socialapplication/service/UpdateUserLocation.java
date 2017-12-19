package com.gire.socialapplication.service;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.gire.socialapplication.activity.SplashActivity;
import com.gire.socialapplication.constants.CommonConstants;
import com.gire.socialapplication.controller.WebInterface;
import com.gire.socialapplication.controller.WebServiceController;
import com.gire.socialapplication.interfaces.LocationUpdate;
import com.gire.socialapplication.userPreference.SharedPreferencesUtil;
import com.loopj.android.http.RequestParams;

import static com.gire.socialapplication.constants.APIConstants.UPDATE_LOCATION;

/**
 * Created by girish on 6/15/2017.
 */

public class UpdateUserLocation implements WebInterface {

    WebServiceController webServiceController;
    Context activity;
    LocationUpdate locationUpdate;

    public UpdateUserLocation(Context activity, Object object) {
        this.activity = activity;
        locationUpdate = (LocationUpdate) object;
        webServiceController = new WebServiceController(activity,activity);

        RequestParams requestParams = new RequestParams();
        requestParams.put("lattitude",String.valueOf(CommonConstants.latitude));
        requestParams.put("longitude",String.valueOf(CommonConstants.longitude));
        requestParams.put("user_id", SharedPreferencesUtil.getInstance(activity).getCustomerID());

        webServiceController.postRequest(UPDATE_LOCATION,requestParams,1);

    }

    @Override
    public void getResponse(String response, int flag) {
        locationUpdate.locationUpdate();
    }

}
