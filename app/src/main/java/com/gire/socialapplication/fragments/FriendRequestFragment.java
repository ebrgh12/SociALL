package com.gire.socialapplication.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.gire.socialapplication.R;
import com.gire.socialapplication.adapter.ReceivedRequestAdapter;
import com.gire.socialapplication.adapter.SentRequestAdapter;
import com.gire.socialapplication.constants.APIConstants;
import com.gire.socialapplication.controller.WebInterface;
import com.gire.socialapplication.controller.WebServiceController;
import com.gire.socialapplication.model.ReceivedRequestModel;
import com.gire.socialapplication.model.SentRequestModel;
import com.gire.socialapplication.userPreference.SharedPreferencesUtil;
import com.gire.socialapplication.utils.ProgressIndicator;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.gire.socialapplication.constants.APIConstants.ASSIGN_TO_MODE;
import static com.gire.socialapplication.constants.APIConstants.UPDATE_FRIEND_REQUEST;

/**
 * Created by girish on 6/20/2017.
 */

public class FriendRequestFragment extends Fragment implements View.OnClickListener,WebInterface{

    View view;
    TextView receivedRequest,sentRequest;
    RecyclerView requestViewList;
    String friendId;

    WebServiceController webServiceController;
    ProgressIndicator progressIndicator;

    List<ReceivedRequestModel> receivedRequestModelList = new ArrayList<>();
    List<SentRequestModel> sentRequestModelList = new ArrayList<>();

    ReceivedRequestAdapter receivedRequestAdapter;
    SentRequestAdapter sentRequestAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webServiceController = new WebServiceController(getActivity(),FriendRequestFragment.this);
        progressIndicator = new ProgressIndicator();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.friend_request_fragment,container,false);
        receivedRequest = (TextView) view.findViewById(R.id.received_request);
        sentRequest = (TextView) view.findViewById(R.id.sent_request);
        requestViewList = (RecyclerView) view.findViewById(R.id.list_data);
        requestViewList.setLayoutManager(new LinearLayoutManager(getActivity()));

        receivedRequest.setOnClickListener(this);
        sentRequest.setOnClickListener(this);

        //request received request at initial creation
        receivedRequestApi();

        return view;
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.received_request:
                receivedRequest.setBackground(getActivity().getResources().getDrawable(R.drawable.blue_curved_rectangle));
                sentRequest.setBackground(getActivity().getResources().getDrawable(R.drawable.white_curved_rectangle));

                receivedRequest.setTextColor(Color.WHITE);
                sentRequest.setTextColor(Color.BLACK);

                receivedRequestApi();
                break;
            case R.id.sent_request:
                receivedRequest.setBackground(getActivity().getResources().getDrawable(R.drawable.white_curved_rectangle));
                sentRequest.setBackground(getActivity().getResources().getDrawable(R.drawable.blue_curved_rectangle));

                receivedRequest.setTextColor(Color.BLACK);
                sentRequest.setTextColor(Color.WHITE);

                sentRequestApi();
                break;
        }
    }

    private void receivedRequestApi() {
        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
        webServiceController.postRequest(APIConstants.MY_FRIEND_REQUEST,requestParams,1);
    }

    private void sentRequestApi() {
        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id",SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
        webServiceController.postRequest(APIConstants.SENT_REQUEST_LIST,requestParams,2);
    }


    @Override
    public void getResponse(String response, int flag) {
        progressIndicator.DismissProgress();
        try{
            switch (flag){
                case 1:
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.optBoolean("status")){
                        JSONArray responseArray = jsonObject.getJSONArray("response");
                        receivedRequestModelList.clear();
                        for(int i=0;i<responseArray.length();i++){
                            JSONObject responseObject = responseArray.getJSONObject(i);
                            TextDrawable drawable;
                            if(i%2 == 0){
                                drawable = TextDrawable.builder()
                                        .buildRound(responseObject.getString("friend_name").substring(0,1).toUpperCase(), Color.RED);
                            }else {
                                drawable = TextDrawable.builder()
                                        .buildRound(responseObject.getString("friend_name").substring(0,1).toUpperCase(), Color.GREEN);
                            }

                            receivedRequestModelList.add(new ReceivedRequestModel(responseObject.getString("friend_name"),
                                    responseObject.getString("friend_id"),responseObject.getString("profile_image"),
                                    drawable));
                        }
                    }else {
                        Toast.makeText(getActivity(), jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    }

                    receivedRequestAdapter = new ReceivedRequestAdapter(getActivity(),FriendRequestFragment.this,
                            receivedRequestModelList);
                    requestViewList.setAdapter(receivedRequestAdapter);

                    break;
                case 2:
                    JSONObject jsonObject1 = new JSONObject(response);
                    if(jsonObject1.optBoolean("status")){
                        JSONArray responseArray = jsonObject1.optJSONArray("response");
                        sentRequestModelList.clear();
                        for(int j=0;j<responseArray.length();j++){
                            JSONObject responseObj = responseArray.getJSONObject(j);
                            TextDrawable drawable;
                            if(j%2 == 0){
                                drawable = TextDrawable.builder()
                                        .buildRound(responseObj.getString("friend_name").substring(0,1).toUpperCase(), Color.RED);
                            }else {
                                drawable = TextDrawable.builder()
                                        .buildRound(responseObj.getString("friend_name").substring(0,1).toUpperCase(), Color.GREEN);
                            }

                            sentRequestModelList.add(new SentRequestModel(responseObj.getString("profile_image"),
                                    responseObj.getString("friend_name"),responseObj.getString("friend_id"),drawable));
                        }
                    }else {
                        Toast.makeText(getActivity(), jsonObject1.optString("message"), Toast.LENGTH_SHORT).show();
                    }

                    sentRequestAdapter = new SentRequestAdapter(getActivity(),FriendRequestFragment.this,sentRequestModelList);
                    requestViewList.setAdapter(sentRequestAdapter);
                    break;
                case 10:
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object.optBoolean("status")){
                                /**
                                 * display pop for selection mode
                                 * */
                                //modeSelection();
                                receivedRequestApi();
                            }else {
                                Toast.makeText(getActivity(), object.optString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){

                        }
                    break;
                case 20:

                    try{
                        JSONObject object = new JSONObject(response);
                        if(object.optBoolean("status")){
                            // reload friend rquest list
                            receivedRequestApi();
                        }else {
                            Toast.makeText(getActivity(), object.optString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){

                    }
                    break;
            }
        }catch (Exception E){
            E.printStackTrace();
        }
    }

    private void modeSelection() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.mode_selection_layout);

        TextView family = (TextView) dialog.findViewById(R.id.add_to_family);
        TextView party = (TextView) dialog.findViewById(R.id.add_to_party);
        TextView conference = (TextView) dialog.findViewById(R.id.add_to_conference);
        TextView stranger = (TextView) dialog.findViewById(R.id.add_as_stranger);

        family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUserMode("1");
            }
        });

        party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUserMode("2");
            }
        });

        conference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUserMode("3");
            }
        });

        stranger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUserMode("0");
            }
        });

        dialog.show();
    }

    private void changeUserMode(String mode) {
        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id",SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
        requestParams.put("friend_id",friendId);
        requestParams.put("mode_type",mode);
        webServiceController.postRequest(ASSIGN_TO_MODE,requestParams,20);
    }

    /**
     * If user accepts then the status value is
     * 1
     * */
    public void sendAcceptRequest(String friendId) {
        this.friendId = friendId;
        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id",SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
        requestParams.put("friend_id",friendId);
        requestParams.put("status","1");
        webServiceController.postRequest(UPDATE_FRIEND_REQUEST,requestParams,10);
    }

    /**
     * If user declined then the status value is
     * 2
     * */
    public void declainRequest(String userId) {

    }

}