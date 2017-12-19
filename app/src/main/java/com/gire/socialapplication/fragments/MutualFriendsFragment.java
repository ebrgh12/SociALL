package com.gire.socialapplication.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gire.socialapplication.R;
import com.gire.socialapplication.adapter.MutualFriendsAdapter;
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

import static com.gire.socialapplication.constants.APIConstants.GET_MUTUAL_FRIENDS;
import static com.gire.socialapplication.constants.APIConstants.SEND_FRIEND_REQUEST;

/**
 * Created by girish on 7/22/2017.
 */

public class MutualFriendsFragment extends Fragment implements WebInterface{

    View view;
    RecyclerView recyclerView;
    WebServiceController webServiceController;
    ProgressIndicator progressIndicator;
    List<NearByFriendsModel> nearByFriendsModels = new ArrayList<NearByFriendsModel>();

    MutualFriendsAdapter mutualFriendsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webServiceController = new WebServiceController(getActivity(),MutualFriendsFragment.this);
        progressIndicator = new ProgressIndicator();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mutual_friends,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.mutual_friends);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
        webServiceController.postRequest(GET_MUTUAL_FRIENDS,requestParams,1);

        return view;
    }

    @Override
    public void getResponse(String response, int flag) {
        progressIndicator.DismissProgress();
        try{
            switch (flag){
                case 1:
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("status")){
                        JSONArray responseArray = jsonObject.getJSONArray("response");
                        for(int i=0;i<responseArray.length();i++){
                            JSONObject responseObject = responseArray.getJSONObject(i);
                            nearByFriendsModels.add(new NearByFriendsModel(responseObject.getString("user_id"),
                                    responseObject.getString("name"),responseObject.getString("profile_image"),
                                    "0",responseObject.getString("rating")));
                        }
                        mutualFriendsAdapter = new MutualFriendsAdapter(getActivity(),
                                MutualFriendsFragment.this,nearByFriendsModels);
                        recyclerView.setAdapter(mutualFriendsAdapter);

                    }else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    JSONObject requestObj = new JSONObject(response);
                    if(requestObj.getBoolean("status")){
                        Toast.makeText(getActivity(), "Friend Request Sent Successfully.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), requestObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void sendFriendRequest(String userId) {
        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id",SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
        requestParams.put("friend_id",userId);
        webServiceController.postRequest(SEND_FRIEND_REQUEST,requestParams,2);
    }

}
