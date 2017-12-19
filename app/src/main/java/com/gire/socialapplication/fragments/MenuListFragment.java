package com.gire.socialapplication.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gire.socialapplication.R;
import com.gire.socialapplication.activity.LoginActivity;
import com.gire.socialapplication.activity.SplashActivity;
import com.gire.socialapplication.interfaces.HomePageInterface;
import com.gire.socialapplication.userPreference.SharedPreferencesUtil;
import com.gire.socialapplication.utils.IntentAndFragmentService;

/**
 * Created by girish on 6/19/2017.
 */

public class MenuListFragment extends Fragment implements View.OnClickListener{
    View view;
    TextView homeFragment,myProfile,updateNewProfile,discoverFriend,viewFriendRequest,
            logOut,friendList,createNewGroup,manageUserGroup,
            mutualFriends;
    HomePageInterface homePageInterface;

    @SuppressLint("ValidFragment")
    public MenuListFragment(Object object) {
        homePageInterface = (HomePageInterface) object;
    }

    public MenuListFragment(){
        /*
        * empty constructor
        * */
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.menu_fragment,container,false);
        homeFragment = (TextView) view.findViewById(R.id.home);
        myProfile = (TextView) view.findViewById(R.id.my_profile);
        updateNewProfile = (TextView) view.findViewById(R.id.upload_post);
        discoverFriend = (TextView) view.findViewById(R.id.discover_friend);
        viewFriendRequest = (TextView) view.findViewById(R.id.view_friend_request);
        logOut = (TextView) view.findViewById(R.id.log_out);
        friendList = (TextView) view.findViewById(R.id.my_friends_list);
        createNewGroup = (TextView) view.findViewById(R.id.create_group);
        manageUserGroup = (TextView) view.findViewById(R.id.manage_groups);
        mutualFriends = (TextView) view.findViewById(R.id.mutual_friends);

        homeFragment.setOnClickListener(this);
        myProfile.setOnClickListener(this);
        updateNewProfile.setOnClickListener(this);
        discoverFriend.setOnClickListener(this);
        viewFriendRequest.setOnClickListener(this);
        logOut.setOnClickListener(this);
        friendList.setOnClickListener(this);
        createNewGroup.setOnClickListener(this);
        manageUserGroup.setOnClickListener(this);
        mutualFriends.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home:
                homePageInterface.homePageNotifier(3);
                IntentAndFragmentService.fragmentdisplay(getActivity(),R.id.home_frame,new HomeFragment(homePageInterface),null,false,false);
                break;
            case R.id.my_profile:
                homePageInterface.homePageNotifier(4);
                IntentAndFragmentService.fragmentdisplay(getActivity(),R.id.home_frame,new MyProfileFragment(),null,false,false);
                break;
            case R.id.upload_post:
                homePageInterface.homePageNotifier(5);
                AddNewPostFragment addNewPostFragment = new AddNewPostFragment(homePageInterface);
                IntentAndFragmentService.fragmentdisplay(getActivity(),R.id.home_frame,addNewPostFragment,null,false,false);
                break;
            case R.id.discover_friend:
                homePageInterface.homePageNotifier(1);
                IntentAndFragmentService.fragmentdisplay(getActivity(),R.id.home_frame,new SearchFriendsFragments(),null,true,false);
                break;
            case R.id.view_friend_request:
                homePageInterface.homePageNotifier(2);
                IntentAndFragmentService.fragmentdisplay(getActivity(),R.id.home_frame,new FriendRequestFragment(),null,false,false);
                break;
            case R.id.mutual_friends:
                homePageInterface.homePageNotifier(11);
                IntentAndFragmentService.fragmentdisplay(getActivity(),R.id.home_frame,new MutualFriendsFragment(),null,false,false);
                break;
            case R.id.log_out:
                SharedPreferencesUtil.getInstance(getActivity()).logOut();
                IntentAndFragmentService.intentDisplay(getActivity(),LoginActivity.class,null);
                getActivity().finish();
                break;
            case R.id.my_friends_list:
                homePageInterface.homePageNotifier(8);
                IntentAndFragmentService.fragmentdisplay(getActivity(),R.id.home_frame,new MyFriendsListFragment(),null,false,false);
                break;
            case R.id.create_group:
                homePageInterface.homePageNotifier(9);
                IntentAndFragmentService.fragmentdisplay(getActivity(),R.id.home_frame,new CreateGroupsFragment(),null,false,false);
                break;
            case R.id.manage_groups:
                homePageInterface.homePageNotifier(10);
                IntentAndFragmentService.fragmentdisplay(getActivity(),R.id.home_frame,new ManageUserGroupFragment(),null,false,false);
                break;
        }
    }
    
}
