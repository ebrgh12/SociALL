package com.gire.socialapplication.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gire.socialapplication.R;
import com.gire.socialapplication.adapter.FriendsListAdapter;
import com.gire.socialapplication.adapter.FriendsModeAdapter;
import com.gire.socialapplication.controller.WebInterface;
import com.gire.socialapplication.controller.WebServiceController;
import com.gire.socialapplication.model.FriendsListModel;
import com.gire.socialapplication.model.HomePageModeTypes;
import com.gire.socialapplication.userPreference.SharedPreferencesUtil;
import com.gire.socialapplication.utils.IntentAndFragmentService;
import com.gire.socialapplication.utils.ProgressIndicator;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.gire.socialapplication.constants.APIConstants.ASSIGN_TO_MODE;
import static com.gire.socialapplication.constants.APIConstants.FRIENDS_LIST;
import static com.gire.socialapplication.constants.APIConstants.SEND_FRIEND_FEEDBACK;

/**
 * Created by girish on 7/8/2017.
 */

public class MyFriendsListFragment extends Fragment implements WebInterface {

    View view;
    RecyclerView modeList,friendsList;
    List<HomePageModeTypes> homePageModeTypes = new ArrayList<HomePageModeTypes>();
    FriendsModeAdapter friendsModeAdapter;
    List<FriendsListModel> friendsListModels = new ArrayList<FriendsListModel>();
    List<FriendsListModel> friendsListModelsTemp = new ArrayList<FriendsListModel>();
    FriendsListAdapter friendsListAdapter;

    WebServiceController webServiceController;
    ProgressIndicator progressIndicator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webServiceController = new WebServiceController(getActivity(),MyFriendsListFragment.this);
        progressIndicator = new ProgressIndicator();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_friends_fragment,container,false);
        modeList = (RecyclerView) view.findViewById(R.id.friends_mode_type);
        friendsList = (RecyclerView) view.findViewById(R.id.friends_list);

        progressIndicator.showProgress(getActivity());

        modeList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        friendsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        homePageModeTypes.add(new HomePageModeTypes("Family","1",getActivity().getResources().getDrawable(R.drawable.blue_curved_rectangle)));
        homePageModeTypes.add(new HomePageModeTypes("Party","2",getActivity().getResources().getDrawable(R.drawable.white_curved_rectangle)));
        homePageModeTypes.add(new HomePageModeTypes("Conference","3",getActivity().getResources().getDrawable(R.drawable.white_curved_rectangle)));
        homePageModeTypes.add(new HomePageModeTypes("Stranger","0",getActivity().getResources().getDrawable(R.drawable.white_curved_rectangle)));

        friendsModeAdapter = new FriendsModeAdapter(getActivity(),MyFriendsListFragment.this,homePageModeTypes);
        modeList.setAdapter(friendsModeAdapter);

        friendsListAdapter = new FriendsListAdapter(getActivity(),MyFriendsListFragment.this,friendsListModels);
        friendsList.setAdapter(friendsListAdapter);

        getFriendsList();

        return view;
    }

    private void getFriendsList() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
        webServiceController.postRequest(FRIENDS_LIST,requestParams,1);
    }

    public void friendsModeType(String modeId, int position) {
        for(int i=0;i<homePageModeTypes.size();i++){
            if(position == i){
                homePageModeTypes.get(i).setViewBg(getActivity().getResources().getDrawable(R.drawable.blue_curved_rectangle));
            }else {
                homePageModeTypes.get(i).setViewBg(getActivity().getResources().getDrawable(R.drawable.white_curved_rectangle));
            }
        }
        friendsModeAdapter.notifyDataSetChanged();
        friendsListModels.clear();

        switch (modeId){
            case "0":
                for(int i=0;i<friendsListModelsTemp.size();i++){
                    if(friendsListModelsTemp.get(i).getModeId().equals("0")){
                        friendsListModels.add(friendsListModelsTemp.get(i));
                    }
                }
                break;
            case "1":
                for(int i=0;i<friendsListModelsTemp.size();i++){
                    if(friendsListModelsTemp.get(i).getModeId().equals("1")){
                        friendsListModels.add(friendsListModelsTemp.get(i));
                    }
                }
                break;
            case "2":
                for(int i=0;i<friendsListModelsTemp.size();i++){
                    if(friendsListModelsTemp.get(i).getModeId().equals("2")){
                        friendsListModels.add(friendsListModelsTemp.get(i));
                    }
                }
                break;
            case "3":
                for(int i=0;i<friendsListModelsTemp.size();i++){
                    if(friendsListModelsTemp.get(i).getModeId().equals("3")){
                        friendsListModels.add(friendsListModelsTemp.get(i));
                    }
                }
                break;
        }

        if(friendsListModels.size() == 0){
            Toast.makeText(getActivity(), "You don't have friends in the selected mode type.", Toast.LENGTH_SHORT).show();
        }

        progressIndicator.DismissProgress();
        friendsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void getResponse(String response, int flag) {

        switch (flag){
            case 1:

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("status")){
                        JSONArray friendList = jsonObject.getJSONArray("response");
                        friendsListModelsTemp.clear();
                        for(int i=0;i<friendList.length();i++){
                            JSONObject friendListObject = friendList.getJSONObject(i);
                            friendsListModelsTemp.add(new FriendsListModel(friendListObject.getString("friend_id"),
                                    friendListObject.getString("friend_name"),friendListObject.getString("profile_image"),
                                    friendListObject.getString("mode"),Integer.parseInt(friendListObject.getString("mode_assign"))));
                        }

                        friendsModeType("1", 0);
                    }else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
            case 10:

                progressIndicator.DismissProgress();

                try{
                    JSONObject object = new JSONObject(response);
                    if(object.optBoolean("status")){
                        Toast.makeText(getActivity(), "Friend Mode Updated Successfully.", Toast.LENGTH_SHORT).show();
                        getFriendsList();
                    }else {
                        Toast.makeText(getActivity(), object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;

            case 11:

                progressIndicator.DismissProgress();

                try{
                    JSONObject feedbackObject = new JSONObject(response);
                    if(feedbackObject.getBoolean("status")){
                        Toast.makeText(getActivity(), feedbackObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), feedbackObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
        }

    }

    public void modeSelection(final String friendId) {
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
                dialog.dismiss();
                changeUserMode("1",friendId);
            }
        });

        party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                changeUserMode("2",friendId);
            }
        });

        conference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                changeUserMode("3",friendId);
            }
        });

        stranger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                changeUserMode("0",friendId);
            }
        });

        dialog.show();
    }

    private void changeUserMode(String modeId,String friendId) {
        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id",SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
        requestParams.put("friend_id",friendId);
        requestParams.put("mode_type",modeId);
        webServiceController.postRequest(ASSIGN_TO_MODE,requestParams,10);
    }

    public void sendMesage(String friendId) {
        Bundle bundle = new Bundle();
        bundle.putString("friend_id",friendId);
        IntentAndFragmentService.fragmentdisplay(getActivity(),R.id.home_frame,new ChatFragment(),bundle,true,false);
    }

    public void sendFeedbackUser(final String friendId) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.friend_feedback_popup);

        final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.rating_bar);
        final CheckBox partyGuy = (CheckBox) dialog.findViewById(R.id.party_guy);
        final CheckBox funnyGuy = (CheckBox) dialog.findViewById(R.id.funny_guy);
        final CheckBox adventurousGuy = (CheckBox) dialog.findViewById(R.id.adventurous_guy);
        final CheckBox nerdGuy = (CheckBox) dialog.findViewById(R.id.nerd_guy);

        TextView submitFeedback = (TextView) dialog.findViewById(R.id.send_feedback);
        TextView cancelFeedback = (TextView) dialog.findViewById(R.id.cancel_feedback);

        submitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(partyGuy.isChecked() || funnyGuy.isChecked() || adventurousGuy.isChecked() || nerdGuy.isChecked()){
                    if(ratingBar.getRating() != 0){
                        dialog.dismiss();

                        progressIndicator.showProgress(getActivity());
                        String idValue = null;

                        if(partyGuy.isChecked()){
                            if(idValue != null){
                                idValue = idValue+","+"1";
                            }else {
                                idValue = "1";
                            }
                        }

                        if(funnyGuy.isChecked()){
                            if(idValue != null){
                                idValue = idValue+","+"2";
                            }else {
                                idValue = "2";
                            }
                        }

                        if(adventurousGuy.isChecked()){
                            if(idValue != null){
                                idValue = idValue+","+"3";
                            }else {
                                idValue = "3";
                            }
                        }

                        if(nerdGuy.isChecked()){
                            if(idValue != null){
                                idValue = idValue+","+"4";
                            }else {
                                idValue = "4";
                            }
                        }

                        RequestParams requestParams = new RequestParams();
                        requestParams.put("user_id",SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
                        requestParams.put("friend_id",friendId);
                        requestParams.put("question_ids",idValue);
                        requestParams.put("rating",String.valueOf(ratingBar.getRating()));

                        webServiceController.postRequest(SEND_FRIEND_FEEDBACK,requestParams,11);
                    }else {
                        Toast.makeText(getActivity(), "Please select rating to proceed.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please select any one option to proceed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
