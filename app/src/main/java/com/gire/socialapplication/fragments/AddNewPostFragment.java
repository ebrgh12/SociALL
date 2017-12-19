package com.gire.socialapplication.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gire.socialapplication.R;
import com.gire.socialapplication.adapter.AddImageGridAdapter;
import com.gire.socialapplication.adapter.SelectGroupPostAdapter;
import com.gire.socialapplication.constants.APIConstants;
import com.gire.socialapplication.controller.WebInterface;
import com.gire.socialapplication.controller.WebServiceController;
import com.gire.socialapplication.interfaces.HomePageInterface;
import com.gire.socialapplication.interfaces.ImageNotify;
import com.gire.socialapplication.userPreference.SharedPreferencesUtil;
import com.gire.socialapplication.utils.IntentAndFragmentService;
import com.gire.socialapplication.utils.ProgressIndicator;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.gire.socialapplication.constants.APIConstants.MANAGE_ALL_GROUPS;

/**
 * Created by girish on 6/22/2017.
 */

public class AddNewPostFragment extends Fragment implements View.OnClickListener,ImageNotify,WebInterface {

    View view;
    TextView takeNewPic,uploadPost;
    RecyclerView imagesList;
    EditText postTopic,postDescription;
    HomePageInterface homePageInterface;
    List<Bitmap> imageData = new ArrayList<Bitmap>();
    AddImageGridAdapter addImageGridAdapter;
    CustomCameraFragment customCameraFragment;
    Integer countValue = 0;
    List<File> fileList = new ArrayList<File>();
    HttpEntity resEntity;
    ProgressIndicator progressIndicator;
    WebServiceController webServiceController;
    String modeValue = null,groupValue = null;
    Dialog dialogGroup;

    @SuppressLint("ValidFragment")
    public AddNewPostFragment(Object object) {
        homePageInterface = (HomePageInterface) object;
    }

    public AddNewPostFragment(){
        /**
         * empty constructor
         * */
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webServiceController = new WebServiceController(getActivity(),AddNewPostFragment.this);
        progressIndicator = new ProgressIndicator();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_new_post,container,false);
        takeNewPic = (TextView) view.findViewById(R.id.add_new_image);
        uploadPost = (TextView) view.findViewById(R.id.upload_new_post);
        imagesList = (RecyclerView) view.findViewById(R.id.image_added);
        postTopic = (EditText) view.findViewById(R.id.post_title);
        postDescription = (EditText) view.findViewById(R.id.post_description);

        imagesList.setLayoutManager(new GridLayoutManager(getActivity(),2));
        addImageGridAdapter = new AddImageGridAdapter(getActivity(),imageData);
        imagesList.setAdapter(addImageGridAdapter);

        takeNewPic.setOnClickListener(this);
        uploadPost.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_new_image:
                if(countValue < 2){
                    homePageInterface.homePageNotifier(6);
                    customCameraFragment = new CustomCameraFragment(AddNewPostFragment.this);
                    IntentAndFragmentService.fragmentdisplay(getActivity(),R.id.home_frame,customCameraFragment,null,true,false);
                }else {
                    Toast.makeText(getActivity(), "You can add only 2 pic per post", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.upload_new_post:
                if(postTopic.getText().toString() != null && !postTopic.getText().toString().isEmpty()){
                    if(countValue == 0){
                        if(postDescription.getText().toString() != null && !postDescription.getText().toString().isEmpty()){
                            selectModeType();
                        }else {
                            Toast.makeText(getActivity(), "please enter post description", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        selectModeType();
                    }
                }else {
                    Toast.makeText(getActivity(), "please enter a post topic", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void selectModeType() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.mode_selection_layout);

        TextView family = (TextView) dialog.findViewById(R.id.add_to_family);
        TextView party = (TextView) dialog.findViewById(R.id.add_to_party);
        TextView conference = (TextView) dialog.findViewById(R.id.add_to_conference);
        TextView stranger = (TextView) dialog.findViewById(R.id.add_as_stranger);
        stranger.setText("GROUP");

        family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                selectedMode("1");
            }
        });

        party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                selectedMode("2");
            }
        });

        conference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                selectedMode("3");
            }
        });

        stranger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                selectGroup("4");
            }
        });

        dialog.show();
    }

    private void selectGroup(String modeId) {
        modeValue = modeId;
        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id",SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
        webServiceController.postRequest(MANAGE_ALL_GROUPS,requestParams,1);
    }

    private void selectedMode(final String modeId) {
        progressIndicator.showProgress(getActivity());
        Thread thread=new Thread(new Runnable(){
            @Override
            public void run() {
                uploadPost(modeId);
            }
        });
        thread.start();
    }

    private void uploadPost(String modeId) {
        try{
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(APIConstants.BASE_URL+"upload_post");
            FileBody bin1 = null;
            FileBody bin2 = null;

            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            if(fileList.size() != 0){
                bin1 = new FileBody(fileList.get(0));
                reqEntity.addPart("userFiles1", bin1);
                if(fileList.size() == 2){
                    bin2 = new FileBody(fileList.get(1));
                    reqEntity.addPart("userFiles2", bin2);
                }
            }
            reqEntity.addPart("user_id", new StringBody(SharedPreferencesUtil.getInstance(getActivity()).getCustomerID()));
            reqEntity.addPart("mode_type", new StringBody(modeId));
            reqEntity.addPart("title", new StringBody(postTopic.getText().toString()));
            reqEntity.addPart("description", new StringBody(postDescription.getText().toString()));
            reqEntity.addPart("post_type", new StringBody("1"));
            if(groupValue != null){
                reqEntity.addPart("group_id", new StringBody(groupValue));
            }
            post.setEntity(reqEntity);
            HttpResponse response = client.execute(post);
            resEntity = response.getEntity();
            final String response_str = EntityUtils.toString(resEntity);
            if (resEntity != null) {
                progressIndicator.DismissProgress();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageData.clear();
                        addImageGridAdapter.notifyDataSetChanged();
                        postTopic.setText("");
                        postDescription.setText("");
                        groupValue = null;
                        Toast.makeText(getActivity(), "Post added successfully", Toast.LENGTH_SHORT).show();
                    }
                });

            }else {
                progressIndicator.DismissProgress();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Failed to add post try again later", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }catch (Exception e){
            progressIndicator.DismissProgress();
            e.toString();
        }

    }

    @Override
    public void imageNotify(final Bitmap bitmap) {

        progressIndicator.showProgress(getActivity());

        try{
            countValue =countValue + 1;
            getActivity().onBackPressed();
            homePageInterface.homePageNotifier(7);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try{

                        imageData.add(bitmap);
                        addImageGridAdapter.notifyDataSetChanged();

                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        float scaleWidth = ((float) 1024) / width;
                        float scaleHeight = ((float) 768) / height;
                        // create a matrix for the manipulation
                        Matrix matrix = new Matrix();
                        // resize the bit map
                        matrix.postScale(scaleWidth, scaleHeight);
                        // recreate the new Bitmap
                        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);

                        //create a file to write bitmap data
                        File filesDir = getActivity().getFilesDir();
                        File imageFile = new File(filesDir, System.currentTimeMillis() + ".jpg");

                        OutputStream os;
                        try {
                            os = new FileOutputStream(imageFile);
                            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                            os.flush();
                            os.close();
                        } catch (Exception e) {
                            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                        }

                    fileList.add(imageFile);

                        progressIndicator.DismissProgress();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            },1000);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void getResponse(String response, int flag) {
        progressIndicator.DismissProgress();
        try{
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getBoolean("status")){
                // toad data to list
                final List<String> groupName = new ArrayList<String>();
                final List<String> groupId = new ArrayList<String>();
                JSONArray groupArray = jsonObject.getJSONArray("response");
                for(int i=0;i<groupArray.length();i++){
                    JSONObject groupObj = groupArray.getJSONObject(i);
                    groupId.add(groupObj.getString("group_id"));
                    groupName.add(groupObj.getString("group_name"));
                }

                callPopupGroup(groupName,groupId);
            }else {
                Toast.makeText(getActivity(), "You don't have any group to post.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void callPopupGroup(List<String> groupName, List<String> groupId) {
        dialogGroup = new Dialog(getActivity());
        dialogGroup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogGroup.setCancelable(true);
        dialogGroup.setContentView(R.layout.select_group_for_post);

        RecyclerView recyclerView = (RecyclerView) dialogGroup.findViewById(R.id.group_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SelectGroupPostAdapter selectGroupPostAdapter = new SelectGroupPostAdapter
                (getActivity(),AddNewPostFragment.this,groupName,groupId);
        recyclerView.setAdapter(selectGroupPostAdapter);
        dialogGroup.show();
    }

    public void selectUserPostGroup(String groupId) {
        groupValue = groupId;
        dialogGroup.dismiss();
        selectedMode(modeValue);
    }

}
