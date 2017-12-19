package com.gire.socialapplication.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.gire.socialapplication.R;
import com.gire.socialapplication.controller.WebInterface;
import com.gire.socialapplication.controller.WebServiceController;
import com.gire.socialapplication.userPreference.SharedPreferencesUtil;
import com.gire.socialapplication.utils.IntentAndFragmentService;
import com.gire.socialapplication.utils.ProgressIndicator;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import static com.gire.socialapplication.constants.APIConstants.UPDATE_USER_PROFILE;

/**
 * Created by girish on 6/22/2017.
 */

public class MyProfileFragment extends Fragment implements WebInterface{

    View view;
    ImageView userProfileImage,userImage;
    EditText userName,userEmail,userPhone,userInterests,userHobby,userAge,userEducation;
    TextView myPost,updateProfile;
    Spinner userGender;

    WebServiceController webServiceController;
    ProgressIndicator progressIndicator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webServiceController = new WebServiceController(getActivity(),MyProfileFragment.this);
        progressIndicator = new ProgressIndicator();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_profile_fragment,container,false);

        userProfileImage = (ImageView) view.findViewById(R.id.profile_pic);
        userImage = (ImageView) view.findViewById(R.id.user_image);
        userName = (EditText) view.findViewById(R.id.user_name);
        userEmail = (EditText) view.findViewById(R.id.user_email);
        userPhone = (EditText) view.findViewById(R.id.user_phone);
        userInterests = (EditText) view.findViewById(R.id.interests);
        userHobby = (EditText) view.findViewById(R.id.hobbies);
        userAge = (EditText) view.findViewById(R.id.user_age);
        userEducation = (EditText) view.findViewById(R.id.education);

        userGender = (Spinner) view.findViewById(R.id.user_gender);

        myPost = (TextView) view.findViewById(R.id.view_my_post);
        updateProfile = (TextView) view.findViewById(R.id.update_profile);

        Picasso.with(getActivity())
                .load(SharedPreferencesUtil.getInstance(getActivity()).getUserImage())
                .into(userImage);

        userName.setText(SharedPreferencesUtil.getInstance(getActivity()).getUserName());
        userEmail.setText(SharedPreferencesUtil.getInstance(getActivity()).getUserEmail());
        userPhone.setText(SharedPreferencesUtil.getInstance(getActivity()).getUserPhone());

        if(SharedPreferencesUtil.getInstance(getActivity()).getUserInterests() != null){
            userInterests.setText(SharedPreferencesUtil.getInstance(getActivity()).getUserInterests());
        }

        if(SharedPreferencesUtil.getInstance(getActivity()).getUserHobbies() != null){
            userHobby.setText(SharedPreferencesUtil.getInstance(getActivity()).getUserHobbies());
        }
        if(SharedPreferencesUtil.getInstance(getActivity()).getUserAge() != null){
            userAge.setText(SharedPreferencesUtil.getInstance(getActivity()).getUserAge());
        }

        if(SharedPreferencesUtil.getInstance(getActivity()).getUserEducation() != null){
            userEducation.setText(SharedPreferencesUtil.getInstance(getActivity()).getUserEducation());
        }

        if(SharedPreferencesUtil.getInstance(getActivity()).getUserSexId() != 0){
            userGender.setSelection(SharedPreferencesUtil.getInstance(getActivity()).getUserSexId());
        }

        myPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentAndFragmentService.fragmentdisplay(getActivity(),R.id.home_frame,new MyPostFragment(),null,true,false);
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText().toString() != null && !userName.getText().toString().isEmpty()
                        && userEmail.getText().toString() != null && !userEmail.getText().toString().isEmpty()
                        && userPhone.getText().toString() != null && !userPhone.getText().toString().isEmpty()){

                    progressIndicator.showProgress(getActivity());

                    RequestParams requestParams = new RequestParams();
                    requestParams.put("user_id",SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
                    requestParams.put("email_id",userEmail.getText().toString());
                    if(userInterests.getText().toString()!= null && !userInterests.getText().toString().isEmpty()){
                        SharedPreferencesUtil.getInstance(getActivity()).setInterests(userInterests.getText().toString());
                        requestParams.put("interests",userInterests.getText().toString());
                    }

                    if(userHobby.getText().toString() != null && !userHobby.getText().toString().isEmpty()){
                        SharedPreferencesUtil.getInstance(getActivity()).setHobby(userHobby.getText().toString());
                        requestParams.put("hobbies",userHobby.getText().toString());
                    }

                    if(userAge.getText().toString() != null && !userAge.getText().toString().isEmpty()){
                        SharedPreferencesUtil.getInstance(getActivity()).setAge(userAge.getText().toString());
                        requestParams.put("age",userAge.getText().toString());
                    }

                    if(userEducation.getText().toString() != null && !userEducation.getText().toString().isEmpty()){
                        SharedPreferencesUtil.getInstance(getActivity()).setEducation(userEducation.getText().toString());
                        requestParams.put("education",userEducation.getText().toString());
                    }

                    if(!userGender.getSelectedItem().toString().equals("Select Gender")){
                        if(userGender.getSelectedItem().toString().equals("Male")){
                            SharedPreferencesUtil.getInstance(getActivity()).setUserSex(1);
                        }else {
                            SharedPreferencesUtil.getInstance(getActivity()).setUserSex(2);
                        }
                        requestParams.put("gender",userGender.getSelectedItem().toString());
                    }

                    requestParams.put("name",userName.getText().toString());

                    webServiceController.postRequest(UPDATE_USER_PROFILE,requestParams,1);

                }else {
                    Toast.makeText(getActivity(), "user name, user email and user phone are mandatory fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void getResponse(String response, int flag) {
        progressIndicator.DismissProgress();
        try{
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getBoolean("true")){
                Toast.makeText(getActivity(), "Profile update successful.", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), "Failed to update profile.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
