package com.gire.socialapplication.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gire.socialapplication.R;
import com.gire.socialapplication.adapter.UsersForGroupAdapter;
import com.gire.socialapplication.controller.WebInterface;
import com.gire.socialapplication.controller.WebServiceController;
import com.gire.socialapplication.model.UserForGroupModel;
import com.gire.socialapplication.userPreference.SharedPreferencesUtil;
import com.gire.socialapplication.utils.ProgressIndicator;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.gire.socialapplication.constants.APIConstants.CREATE_NEW_GROUP;
import static com.gire.socialapplication.constants.APIConstants.FRIENDS_LIST;

/**
 * Created by girish on 7/10/2017.
 */

public class CreateGroupsFragment extends Fragment implements WebInterface{

    View view;

    RecyclerView friendsList;
    EditText groupName;
    TextView createGroup;

    WebServiceController webServiceController;
    ProgressIndicator progressIndicator;

    List<UserForGroupModel> userForGroupModels = new ArrayList<UserForGroupModel>();
    UsersForGroupAdapter usersForGroupAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webServiceController = new WebServiceController(getActivity(),CreateGroupsFragment.this);
        progressIndicator = new ProgressIndicator();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_group_fragment,container,false);

        friendsList = (RecyclerView) view.findViewById(R.id.friends_list);
        groupName = (EditText) view.findViewById(R.id.group_name);
        createGroup = (TextView) view.findViewById(R.id.create_group);
        friendsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(groupName.getText().toString() != null && !groupName.getText().toString().isEmpty()){

                    progressIndicator.showProgress(getActivity());

                    StringBuilder result = new StringBuilder();
                    for(int i=0;i<userForGroupModels.size();i++){
                        if(userForGroupModels.get(i).getIsSelected() == 1){
                            if( i == (userForGroupModels.size()-1)){
                                result.append(userForGroupModels.get(i).getFriendId());
                            }else {
                                result.append(userForGroupModels.get(i).getFriendId());
                                result.append(",");
                            }
                        }
                    }

                    String resultData = result.toString();
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("user_id",SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
                    requestParams.put("group_users",resultData);
                    requestParams.put("group_name",groupName.getText().toString());

                    webServiceController.postRequest(CREATE_NEW_GROUP,requestParams,2);
                }else {
                    Toast.makeText(getActivity(), "Please enter the group name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        usersForGroupAdapter = new UsersForGroupAdapter(getActivity(),CreateGroupsFragment.this,userForGroupModels);
        friendsList.setAdapter(usersForGroupAdapter);

        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
        webServiceController.postRequest(FRIENDS_LIST,requestParams,1);

        return view;
    }

    @Override
    public void getResponse(String response, int flag) {
        progressIndicator.DismissProgress();

        try{
            switch (flag){
                case 1:
                    userForGroupModels.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("status")){
                        JSONArray userArray = jsonObject.getJSONArray("response");
                        for(int i=0;i<userArray.length();i++){
                            JSONObject userObject = userArray.getJSONObject(i);
                            userForGroupModels.add(new UserForGroupModel(
                                    userObject.getString("friend_id"),
                                    userObject.getString("friend_name"),
                                    userObject.getString("profile_image"),
                                    0));
                        }

                        usersForGroupAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:

                    JSONObject jsonObject1 = new JSONObject(response);
                    if(jsonObject1.getBoolean("status")){
                        for(int i=0;i<userForGroupModels.size();i++){
                            userForGroupModels.get(i).setIsSelected(0);
                        }

                        usersForGroupAdapter.notifyDataSetChanged();
                        groupName.setText("");

                        Toast.makeText(getActivity(), jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void notifyCheck(int position, Integer isSelected) {
        if(isSelected == 1){
            userForGroupModels.get(position).setIsSelected(0);
        }else {
            userForGroupModels.get(position).setIsSelected(1);
        }
        usersForGroupAdapter.notifyDataSetChanged();
    }

}
