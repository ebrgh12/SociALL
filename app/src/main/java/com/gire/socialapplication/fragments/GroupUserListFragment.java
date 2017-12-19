package com.gire.socialapplication.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.gire.socialapplication.R;
import com.gire.socialapplication.adapter.MyGroupUserAdapter;
import com.gire.socialapplication.adapter.UserListPopupAdapter;
import com.gire.socialapplication.constants.APIConstants;
import com.gire.socialapplication.controller.WebInterface;
import com.gire.socialapplication.controller.WebServiceController;
import com.gire.socialapplication.model.groups.GroupUserList;
import com.gire.socialapplication.model.groups.GroupsMain;
import com.gire.socialapplication.userPreference.SharedPreferencesUtil;
import com.gire.socialapplication.utils.ProgressIndicator;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.gire.socialapplication.constants.APIConstants.ADD_USERS_TO_GROUP;
import static com.gire.socialapplication.constants.APIConstants.DELETE_USER_FROM_GROUP;
import static com.gire.socialapplication.constants.APIConstants.GET_USER_GROUP_INFO;

/**
 * Created by girish on 7/19/2017.
 */

public class GroupUserListFragment extends Fragment implements WebInterface{

    View view;
    RecyclerView recyclerView;
    TextView addNewUser;
    String responseData,groupId;
    List<Integer> userId = new ArrayList<Integer>();
    List<GroupUserList> groupUserLists = new ArrayList<GroupUserList>();
    List<GroupUserList> userListData = new ArrayList<GroupUserList>();
    MyGroupUserAdapter myGroupUserAdapter;

    WebServiceController webServiceController;
    ProgressIndicator progressIndicator;

    UserListPopupAdapter userListPopupAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webServiceController = new WebServiceController(getActivity(),GroupUserListFragment.this);
        progressIndicator = new ProgressIndicator();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.group_user_fragment,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.user_group);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        addNewUser = (TextView) view.findViewById(R.id.add_new_user);
        addNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewUser();
            }
        });

        Bundle extras = this.getArguments();
        if(extras != null) {
            responseData = extras.getString("user_data");
            groupId = extras.getString("group_id");
        }

        getNewUserList();

        return view;
    }

    private void loadUserData() {
        try{
            JSONObject jsonObject = new JSONObject(responseData);
            if(jsonObject.getBoolean("status")){
                JSONObject responseObject = jsonObject.getJSONObject("response");
                userId.clear();
                groupUserLists.clear();
                if(responseObject.getString("group_id").equals(groupId)){
                    JSONArray groupUsers = responseObject.getJSONArray("group_users");
                    for(int j=0;j<groupUsers.length();j++){
                        JSONObject groupObject = groupUsers.getJSONObject(j);
                        userId.add(Integer.parseInt(groupObject.getString("user_id")));
                        groupUserLists.add(new GroupUserList(groupObject.getString("user_id"),
                                groupObject.getString("name"),groupObject.getString("profile_image")));
                    }
                }
            }else {
                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
            }

            myGroupUserAdapter = new MyGroupUserAdapter(getActivity(),GroupUserListFragment.this,groupUserLists);
            recyclerView.setAdapter(myGroupUserAdapter);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addNewUser() {
        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
        webServiceController.postRequest(APIConstants.FRIENDS_LIST,requestParams,1);
    }

    @Override
    public void getResponse(String response, int flag) {
        progressIndicator.DismissProgress();
        try{
            switch (flag){
                case 1:
                    userListData.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("status")){
                        JSONArray responseArray = jsonObject.getJSONArray("response");
                        for(int i=0;i<responseArray.length();i++){
                            JSONObject userData = responseArray.getJSONObject(i);
                            Boolean isSuccess = true;
                            for(int j=0;j<userId.size();j++){
                                if(userId.get(j) ==Integer.parseInt(userData.getString("friend_id"))){
                                    isSuccess = false;
                                    break;
                                }
                            }

                            if(isSuccess){
                                userListData.add(new GroupUserList(userData.getString("friend_id"),
                                        userData.getString("friend_name"),userData.getString("profile_image"),false));
                            }
                        }
                    }

                    if(userListData.size() != 0){
                        displayPopup();
                    }else {
                        Toast.makeText(getActivity(),
                                "You have already added all your friends to your group.",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 2:
                        JSONObject userAddStatus = new JSONObject(response);
                        if(userAddStatus.getBoolean("status")){
                            Toast.makeText(getActivity(), "User Added Successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(), userAddStatus.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        getNewUserList();

                    break;

                case 3:
                    responseData = response;
                    loadUserData();
                    break;

                case 4:
                    JSONObject deleteJson = new JSONObject(response);
                    if(deleteJson.getBoolean("status")){
                        getNewUserList();
                    }else {
                        Toast.makeText(getActivity(), deleteJson.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getNewUserList() {
        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("group_id",groupId);
        webServiceController.postRequest(GET_USER_GROUP_INFO,requestParams,3);
    }

    private void displayPopup() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.user_list_popup);

        RecyclerView userList = (RecyclerView) dialog.findViewById(R.id.user_list);
        userList.setLayoutManager(new LinearLayoutManager(getActivity()));

        TextView submitAction = (TextView) dialog.findViewById(R.id.submit_button);
        TextView cancelAction = (TextView) dialog.findViewById(R.id.cancel_button);

        submitAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                StringBuilder result = new StringBuilder();
                for(int i=0;i<userListData.size();i++){
                    if(userListData.get(i).getFlag()){
                        if( i == (userListData.size()-1)){
                            result.append(userListData.get(i).getUserId());
                        }else {
                            result.append(userListData.get(i).getUserId());
                            result.append(",");
                        }
                    }
                }

                String resultData = result.toString();
                RequestParams requestParams = new RequestParams();
                requestParams.put("group_user_id",resultData);
                requestParams.put("group_id",groupId);

                progressIndicator.showProgress(getActivity());
                webServiceController.postRequest(ADD_USERS_TO_GROUP,requestParams,2);
            }
        });

        cancelAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        userListPopupAdapter = new UserListPopupAdapter(getActivity(),
                GroupUserListFragment.this,userListData);
        userList.setAdapter(userListPopupAdapter);

        dialog.show();
    }

    public void selectUnSelectUser(int position, Boolean flag) {
        if(flag){
            userListData.get(position).setFlag(false);
        }else {
            userListData.get(position).setFlag(true);
        }
        userListPopupAdapter.notifyDataSetChanged();
    }

    public void deleteUserFromGroup(String userId) {
        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id",userId);
        requestParams.put("group_id",groupId);
        webServiceController.postRequest(DELETE_USER_FROM_GROUP,requestParams,4);
    }

}
