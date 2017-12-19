package com.gire.socialapplication.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gire.socialapplication.R;
import com.gire.socialapplication.adapter.SearchNearUserAdapter;
import com.gire.socialapplication.constants.APIConstants;
import com.gire.socialapplication.constants.CommonConstants;
import com.gire.socialapplication.controller.WebInterface;
import com.gire.socialapplication.controller.WebServiceController;
import com.gire.socialapplication.model.NearByFriendsModel;
import com.gire.socialapplication.userPreference.SharedPreferencesUtil;
import com.gire.socialapplication.utils.ProgressIndicator;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.gire.socialapplication.constants.APIConstants.SEND_FRIEND_REQUEST;

/**
 * Created by girish on 6/15/2017.
 */

public class SearchFriendsFragments extends Fragment implements WebInterface{
    View view;
    WebServiceController webServiceController;
    SearchNearUserAdapter searchNearUserAdapter;
    List<NearByFriendsModel> nearByFriendsModels = new ArrayList<NearByFriendsModel>();

    RecyclerView socialList;
    RelativeLayout progressLayout;
    ProgressIndicator progressIndicator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webServiceController = new WebServiceController(getActivity(),SearchFriendsFragments.this);
        progressIndicator = new ProgressIndicator();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.near_by_friends,container,false);

        progressLayout = (RelativeLayout) view.findViewById(R.id.progress_layout);
        socialList = (RecyclerView) view.findViewById(R.id.friend_list);
        socialList.setLayoutManager(new LinearLayoutManager(getActivity()));

        requestNearFriends();

        return view;
    }

    private void requestNearFriends() {
        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("lattitude", CommonConstants.latitude);
        requestParams.put("longitude",CommonConstants.longitude);
        requestParams.put("user_id", SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
        webServiceController.postRequest(APIConstants.SEARCH_FRIENDS,requestParams,1);
    }


    @Override
    public void getResponse(String response, int flag) {
        progressIndicator.DismissProgress();
        switch (flag){
            case 1:
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.optBoolean("status")){
                        JSONArray jsonArray = jsonObject.optJSONArray("response");

                        if(jsonArray.length() == 0){
                            Toast.makeText(getActivity(), "No Friends Found Near By Your Location.", Toast.LENGTH_SHORT).show();
                        }

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject dataObject = jsonArray.getJSONObject(i);
                            Double value = Double.parseDouble(dataObject.getString("distance"));
                            String value1 = "0";
                            if(value != null){
                                if(value == (double) Math.round(value)){
                                    if(value/1000000000 > 1.0){
                                        value1 = String.format("%.1f G", value/1000000000);
                                    }
                                    else if(value/1000000 > 1.0){
                                        value1 = String.format("%.1f M", value/1000000);
                                    }
                                    else if(value/1000 > 1.0){
                                        value1 = String.format("%.1f K", value/1000);
                                    }
                                    else{
                                        value1 = String.format("%.1f", value);
                                    }
                                }
                                else{
                                    value1 = String.format("%.2f", value);
                                }
                            }

                            nearByFriendsModels.add(new NearByFriendsModel(dataObject.getString("user_id"),dataObject.getString("name"),
                                    dataObject.getString("profile_image"),value1));
                        }

                        progressLayout.setVisibility(View.GONE);
                        socialList.setVisibility(View.VISIBLE);

                        searchNearUserAdapter = new SearchNearUserAdapter(getActivity(),SearchFriendsFragments.this,nearByFriendsModels);
                        socialList.setAdapter(searchNearUserAdapter);

                    }else {
                        Toast.makeText(getActivity(),jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case 2:
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.optBoolean("status")){
                        Toast.makeText(getActivity(), jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){

                }
                break;
        }
    }

    public void SendRequest(String userId) {
        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id",SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
        requestParams.put("friend_id",userId);
        webServiceController.postRequest(SEND_FRIEND_REQUEST,requestParams,2);
    }

}
