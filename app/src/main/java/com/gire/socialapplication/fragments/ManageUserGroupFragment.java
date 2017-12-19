package com.gire.socialapplication.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gire.socialapplication.R;
import com.gire.socialapplication.adapter.GroupListAdapter;
import com.gire.socialapplication.controller.WebInterface;
import com.gire.socialapplication.controller.WebServiceController;
import com.gire.socialapplication.model.groups.GroupUserList;
import com.gire.socialapplication.model.groups.GroupsMain;
import com.gire.socialapplication.userPreference.SharedPreferencesUtil;
import com.gire.socialapplication.utils.IntentAndFragmentService;
import com.gire.socialapplication.utils.ProgressIndicator;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.gire.socialapplication.constants.APIConstants.DELETE_USER_GROUP;
import static com.gire.socialapplication.constants.APIConstants.MANAGE_ALL_GROUPS;

/**
 * Created by girish on 7/13/2017.
 */

public class ManageUserGroupFragment extends Fragment implements WebInterface{

    View view;
    RecyclerView groupList;
    GroupListAdapter groupListAdapter;
    List<GroupsMain>groupsMains = new ArrayList<GroupsMain>();

    WebServiceController webServiceController;
    ProgressIndicator progressIndicator;

    String responseData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webServiceController = new WebServiceController(getActivity(),ManageUserGroupFragment.this);
        progressIndicator = new ProgressIndicator();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.manage_user_group,container,false);
        groupList = (RecyclerView) view.findViewById(R.id.user_group);
        groupList.setLayoutManager(new LinearLayoutManager(getActivity()));

        groupListAdapter = new GroupListAdapter(getActivity(),ManageUserGroupFragment.this,groupsMains);
        groupList.setAdapter(groupListAdapter);

        progressIndicator.showProgress(getActivity());
        getUserGroupData();

        return view;
    }

    private void getUserGroupData() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
        webServiceController.postRequest(MANAGE_ALL_GROUPS,requestParams,1);
    }

    @Override
    public void getResponse(String response, int flag) {

        progressIndicator.DismissProgress();

        try{
            switch (flag){
                case 1:
                    responseData = response;
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("status")){
                        JSONArray responseArray = jsonObject.getJSONArray("response");
                        for(int i=0;i<responseArray.length();i++){
                            JSONObject responseObject = responseArray.getJSONObject(i);

                            JSONArray groupUsers = responseObject.getJSONArray("group_users");
                            List<GroupUserList> groupUserList = new ArrayList<GroupUserList>();
                            for(int j=0;j<groupUsers.length();j++){
                                JSONObject groupObject = groupUsers.getJSONObject(j);
                                GroupUserList groupUser = new GroupUserList(groupObject.getString("user_id"),
                                        groupObject.getString("name"),groupObject.getString("profile_image"));
                                groupUserList.add(groupUser);
                            }

                            groupsMains.add(new GroupsMain(responseObject.getString("group_id"),
                                    responseObject.getString("admin"),responseObject.getString("group_name"),
                                    groupUserList));
                        }

                        groupListAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    JSONObject jsonObject1 = new JSONObject(response);
                    if(jsonObject1.getBoolean("status")){
                        Toast.makeText(getActivity(), "Group deleted successfully", Toast.LENGTH_SHORT).show();
                        getUserGroupData();
                    }else {
                        Toast.makeText(getActivity(), jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void deleteUserGroup(String groupId) {
        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("group_id",groupId);
        webServiceController.postRequest(DELETE_USER_GROUP,requestParams,2);
    }

    public void viewGroupDetails(String groupId) {
        Bundle bundle = new Bundle();
        bundle.putString("user_data",responseData);
        bundle.putString("group_id",groupId);
        IntentAndFragmentService.fragmentdisplay(getActivity(),R.id.home_frame,new GroupUserListFragment(),bundle,true,false);
    }

}
