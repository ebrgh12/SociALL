package com.gire.socialapplication.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gire.socialapplication.R;
import com.gire.socialapplication.activity.HomeActivity;
import com.gire.socialapplication.adapter.HomePagePostAdapter;
import com.gire.socialapplication.adapter.LikeListAdapter;
import com.gire.socialapplication.adapter.ModeAdapter;
import com.gire.socialapplication.adapter.ViewUserCommentsAdapter;
import com.gire.socialapplication.controller.WebInterface;
import com.gire.socialapplication.controller.WebServiceController;
import com.gire.socialapplication.interfaces.HomePageInterface;
import com.gire.socialapplication.model.CommentListModel;
import com.gire.socialapplication.model.HomePageModeTypes;
import com.gire.socialapplication.model.HomePagePostModel;
import com.gire.socialapplication.model.LikeListModel;
import com.gire.socialapplication.userPreference.SharedPreferencesUtil;
import com.gire.socialapplication.utils.IntentAndFragmentService;
import com.gire.socialapplication.utils.ProgressIndicator;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.gire.socialapplication.constants.APIConstants.ADD_COMMENT;
import static com.gire.socialapplication.constants.APIConstants.GET_ALL_COMMENTS;
import static com.gire.socialapplication.constants.APIConstants.GET_ALL_POST;
import static com.gire.socialapplication.constants.APIConstants.LIKE_POST;
import static com.gire.socialapplication.constants.APIConstants.POST_TOTAL_LIST;

/**
 * Created by girish on 6/14/2017.
 */

public class HomeFragment extends Fragment implements View.OnClickListener,WebInterface{

    View view;
    LinearLayout findNearBy,wifiNearBy,blueToothNearBy,addNewPost;
    HomePageInterface homePageInterface;
    RelativeLayout relativeLayout;
    LinearLayout viewAction,closeViewAction;
    RecyclerView modeTypeList,homePagePostList;

    WebServiceController webServiceController;
    ProgressIndicator progressIndicator;
    List<HomePagePostModel> homePagePostModels = new ArrayList<HomePagePostModel>();
    HomePagePostAdapter homePagePostAdapter;
    List<HomePageModeTypes> homePageModeTypes = new ArrayList<HomePageModeTypes>();
    ModeAdapter modeAdapter;
    String requestPostId,clickPostId;
    LikeListAdapter likeListAdapter;
    List<LikeListModel> likeListModels = new ArrayList<LikeListModel>();
    ViewUserCommentsAdapter viewUserCommentsAdapter;
    List<CommentListModel> commentListModels = new ArrayList<CommentListModel>();
    Dialog dialogComments;

    @SuppressLint("ValidFragment")
    public HomeFragment(Object object) {
        homePageInterface = (HomePageInterface) object;
    }

    public HomeFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webServiceController = new WebServiceController(getActivity(),HomeFragment.this);
        progressIndicator = new ProgressIndicator();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment,container,false);

        modeTypeList = (RecyclerView) view.findViewById(R.id.mode_tpe_list);
        homePagePostList = (RecyclerView) view.findViewById(R.id.home_page_post);

        modeTypeList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        homePagePostList.setLayoutManager(new LinearLayoutManager(getActivity()));

        findNearBy = (LinearLayout) view.findViewById(R.id.near_by);
        findNearBy.setOnClickListener(this);

        wifiNearBy = (LinearLayout) view.findViewById(R.id.wifi);
        wifiNearBy.setOnClickListener(this);

        blueToothNearBy = (LinearLayout) view.findViewById(R.id.blue_tooth);
        blueToothNearBy.setOnClickListener(this);

        viewAction = (LinearLayout) view.findViewById(R.id.view_action);
        viewAction.setOnClickListener(this);

        closeViewAction = (LinearLayout) view.findViewById(R.id.close_view);
        closeViewAction.setOnClickListener(this);

        addNewPost = (LinearLayout) view.findViewById(R.id.add_new_post);
        addNewPost.setOnClickListener(this);

        relativeLayout = (RelativeLayout) view.findViewById(R.id.action_view);

        homePageModeTypes.add(new HomePageModeTypes("Family","1",getActivity().getResources().getDrawable(R.drawable.blue_curved_rectangle)));
        homePageModeTypes.add(new HomePageModeTypes("Party","2",getActivity().getResources().getDrawable(R.drawable.white_curved_rectangle)));
        homePageModeTypes.add(new HomePageModeTypes("Conference","3",getActivity().getResources().getDrawable(R.drawable.white_curved_rectangle)));
        homePageModeTypes.add(new HomePageModeTypes("Group","4",getActivity().getResources().getDrawable(R.drawable.white_curved_rectangle)));

        modeAdapter = new ModeAdapter(getActivity(),HomeFragment.this,homePageModeTypes);
        modeTypeList.setAdapter(modeAdapter);
        homePagePostAdapter = new HomePagePostAdapter(getActivity(),HomeFragment.this,homePagePostModels);
        homePagePostList.setAdapter(homePagePostAdapter);
        requestPost("1");

        return view;
    }

    private void requestPost(String postTypeId) {
        requestPostId = postTypeId;
        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
        requestParams.put("mode_type",postTypeId);
        webServiceController.postRequest(GET_ALL_POST,requestParams,1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.near_by:
                relativeLayout.setVisibility(View.GONE);
                homePageInterface.homePageNotifier(1);
                IntentAndFragmentService.fragmentdisplay(getActivity(),R.id.home_frame,new SearchFriendsFragments(),null,true,false);
                break;
            case R.id.wifi:
                @SuppressLint("WifiManagerLeak")
                WifiManager wifi = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);
                if (wifi.isWifiEnabled()){
                    relativeLayout.setVisibility(View.GONE);
                    homePageInterface.homePageNotifier(1);
                    IntentAndFragmentService.fragmentdisplay(getActivity(),R.id.home_frame,new WifiDetactFragment(),null,true,false);
                }else {
                    Toast.makeText(getActivity(), "Please enable wifi to use this option.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.blue_tooth:
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    relativeLayout.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Your device wont support bluetooth.", Toast.LENGTH_SHORT).show();
                }else {
                    if(!mBluetoothAdapter.isEnabled()){
                        Toast.makeText(getActivity(), "Please enable blue tooth to use this option.", Toast.LENGTH_SHORT).show();
                    }else {
                        relativeLayout.setVisibility(View.GONE);
                        homePageInterface.homePageNotifier(1);
                        IntentAndFragmentService.fragmentdisplay(getActivity(),R.id.home_frame,new BluetoothDetectFragment(),null,true,false);
                    }

                }
                break;
            case R.id.view_action:
                relativeLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.close_view:
                relativeLayout.setVisibility(View.GONE);
                break;
            case R.id.add_new_post:
                relativeLayout.setVisibility(View.GONE);
                homePageInterface.homePageNotifier(5);
                AddNewPostFragment addNewPostFragment = new AddNewPostFragment(homePageInterface);
                IntentAndFragmentService.fragmentdisplay(getActivity(),R.id.home_frame,addNewPostFragment,null,false,false);
                break;
        }
    }

    @Override
    public void getResponse(String response, int flag) {
        switch (flag){
            case 1:
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.optBoolean("status")){
                        JSONArray responseArray = jsonObject.getJSONArray("response");
                        homePagePostModels.clear();
                        for(int i=0;i<responseArray.length();i++){
                            JSONObject responseObject = responseArray.getJSONObject(i);
                            List<String> imageData = new ArrayList<String>();
                            JSONArray imageArray = responseObject.getJSONArray("file_deatails");
                            for(int j=0;j<imageArray.length();j++){
                                JSONObject imageObject = imageArray.getJSONObject(j);
                                if(imageObject.getString("file_name").length() > 1){
                                    imageData.add(imageObject.getString("file_path")+imageObject.getString("file_name"));
                                }
                            }
                            homePagePostModels.add(new HomePagePostModel(responseObject.getString("post_id"),responseObject.getString("title"),
                                    responseObject.getString("description"),responseObject.getString("total_likes"),responseObject.getString("total_comments"),
                                    Integer.parseInt(responseObject.getString("like_status")),
                                    Integer.parseInt(responseObject.getString("comment_status")),imageData));
                        }

                        homePagePostAdapter.notifyDataSetChanged();

                    }else {
                        homePagePostModels.clear();
                        homePagePostAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                progressIndicator.DismissProgress();
                progressIndicator.DismissProgress();

                break;
            case 2:

                progressIndicator.DismissProgress();

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("status")){
                        requestPost(requestPostId);
                    }else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case 3:
                progressIndicator.DismissProgress();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("status")){
                        JSONArray responseArray = jsonObject.getJSONArray("response");
                        for(int i=0;i<responseArray.length();i++){
                            JSONObject responseObject = responseArray.getJSONObject(i);
                            likeListModels.add(new LikeListModel(responseObject.getString("name"),responseObject.getString("profile_image")));
                        }
                        likeListAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case 4:
                progressIndicator.DismissProgress();

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("status")){
                        requestPost(requestPostId);
                    }else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case 5:
                progressIndicator.DismissProgress();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("status")){
                        JSONArray responseArray = jsonObject.getJSONArray("response");
                        for(int i=0;i<responseArray.length();i++){
                            JSONObject responseObject = responseArray.getJSONObject(i);
                            commentListModels.add(new CommentListModel(responseObject.getString("name"),
                                    responseObject.getString("comment"),responseObject.getString("profile_image")));
                        }

                        viewUserCommentsAdapter.notifyDataSetChanged();
                    }else {
                        dialogComments.dismiss();
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }

    }

    public void getNewPost(String modeId, int position) {
        for(int i=0;i<homePageModeTypes.size();i++){
            if(position == i){
                homePageModeTypes.get(i).setViewBg(getActivity().getResources().getDrawable(R.drawable.blue_curved_rectangle));
            }else {
                homePageModeTypes.get(i).setViewBg(getActivity().getResources().getDrawable(R.drawable.white_curved_rectangle));
            }
        }

        modeAdapter.notifyDataSetChanged();
        requestPost(modeId);

    }

    public void likeThePost(String postId) {
        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id",SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
        requestParams.put("post_id",postId);
        webServiceController.postRequest(LIKE_POST,requestParams,2);
    }

    public void viewLikeList(String postId) {
        likeListModels.clear();

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.post_like_list_fragment);

        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.like_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        likeListAdapter = new LikeListAdapter(getActivity(),likeListModels);
        recyclerView.setAdapter(likeListAdapter);

        dialog.show();

        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("post_id",postId);
        webServiceController.postRequest(POST_TOTAL_LIST,requestParams,3);
    }

    public void addComment(final String postId) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_comment_popup);

        final EditText commentText = (EditText) dialog.findViewById(R.id.user_comment);
        TextView submitComment = (TextView) dialog.findViewById(R.id.submit_comment);
        TextView cancelComment = (TextView) dialog.findViewById(R.id.cancel_view);

        submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                progressIndicator.showProgress(getActivity());
                RequestParams requestParams = new RequestParams();
                requestParams.put("user_id",SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
                requestParams.put("post_id",postId);
                requestParams.put("comment",commentText.getText().toString());
                webServiceController.postRequest(ADD_COMMENT,requestParams,4);
            }
        });

        cancelComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void viewUserComments(String postId) {
        commentListModels.clear();

        dialogComments = new Dialog(getActivity());
        dialogComments.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogComments.setContentView(R.layout.view_comments_list);

        RecyclerView recyclerView = (RecyclerView) dialogComments.findViewById(R.id.comment_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        viewUserCommentsAdapter = new ViewUserCommentsAdapter(getActivity(),commentListModels);
        recyclerView.setAdapter(viewUserCommentsAdapter);

        dialogComments.show();

        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("post_id",postId);
        webServiceController.postRequest(GET_ALL_COMMENTS,requestParams,5);

    }

}