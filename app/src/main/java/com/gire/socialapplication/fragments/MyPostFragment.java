package com.gire.socialapplication.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gire.socialapplication.R;
import com.gire.socialapplication.adapter.MyPostAdapter;
import com.gire.socialapplication.controller.WebInterface;
import com.gire.socialapplication.controller.WebServiceController;
import com.gire.socialapplication.model.MyPostModel;
import com.gire.socialapplication.userPreference.SharedPreferencesUtil;
import com.gire.socialapplication.utils.ProgressIndicator;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.gire.socialapplication.constants.APIConstants.MY_POST_LIST;

/**
 * Created by girish on 6/29/2017.
 */

public class MyPostFragment extends Fragment implements WebInterface{

    View view;
    RecyclerView myPostList;
    WebServiceController webServiceController;
    ProgressIndicator progressIndicator;

    List<MyPostModel> myPostModels = new ArrayList<MyPostModel>();
    MyPostAdapter myPostAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_post_fragment,container,false);
        myPostList = (RecyclerView) view.findViewById(R.id.my_post);
        myPostList.setLayoutManager(new LinearLayoutManager(getActivity()));

        webServiceController = new WebServiceController(getActivity(),MyPostFragment.this);
        progressIndicator = new ProgressIndicator();

        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
        webServiceController.postRequest(MY_POST_LIST,requestParams,1);

        return view;
    }

    @Override
    public void getResponse(String response, int flag) {

        progressIndicator.DismissProgress();

        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray responseArray = jsonObject.getJSONArray("response");
            for(int i=0;i<responseArray.length();i++){
                JSONObject responseObject = responseArray.getJSONObject(i);

                List<String> imageFile = new ArrayList<String>();

                JSONArray fileList = responseObject.getJSONArray("file_deatails");
                for(int j=0;j<fileList.length();j++){
                    JSONObject imageData = fileList.getJSONObject(j);
                    if(imageData.getString("file_name").length() > 0){
                        imageFile.add(imageData.getString("file_path")+imageData.getString("file_name"));
                    }
                }

                myPostModels.add(new MyPostModel(
                        responseObject.getString("title"),responseObject.getString("description"),imageFile));

                myPostAdapter = new MyPostAdapter(getActivity(),myPostModels);
                myPostList.setAdapter(myPostAdapter);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
