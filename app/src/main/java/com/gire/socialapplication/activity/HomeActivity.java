package com.gire.socialapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gire.socialapplication.R;
import com.gire.socialapplication.fragments.HomeFragment;
import com.gire.socialapplication.fragments.MenuListFragment;
import com.gire.socialapplication.interfaces.HomePageInterface;
import com.gire.socialapplication.utils.IntentAndFragmentService;

/**
 * Created by girish on 6/14/2017.
 */

public class HomeActivity extends AppCompatActivity implements HomePageInterface,View.OnClickListener {

    TextView toolbarText;
    ImageView menuIcon;
    Toolbar toolbar;

    Integer backOption = 0;
    Integer menuClick = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        toolbarText = (TextView) findViewById(R.id.toolbar_text);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        menuIcon = (ImageView) findViewById(R.id.menu_option);
        menuIcon.setOnClickListener(this);

        FragmentAction();
    }

    @Override
    public void homePageNotifier(Integer value) {
        backOption = 1;
        switch (value){
            case 1:
                toolbarText.setText("Near By Friends");
                break;
            case 2:
                toolbarText.setText("Friend Request");
                break;
            case 3:
                toolbarText.setText("Home");
                break;
            case 4:
                toolbarText.setText("My Profile");
                break;
            case 5:
                toolbarText.setText("Add New Post");
                break;
            case 6:
                toolbar.setVisibility(View.GONE);
                break;
            case 7:
                toolbar.setVisibility(View.VISIBLE);
                toolbarText.setText("Add New Post");
                break;
            case 8:
                toolbarText.setText("My Friends");
                break;
            case 9:
                toolbarText.setText("Create New Group");
                break;
            case 10:
                toolbarText.setText("Manage User Group");
                break;
            case 11:
                toolbarText.setText("Mutual Friends");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_option:
                toolbarText.setText("Menu");
                menuClick = 1;
                MenuListFragment menuListFragment = new MenuListFragment(HomeActivity.this);
                IntentAndFragmentService.fragmentdisplay(HomeActivity.this,R.id.home_frame,menuListFragment,null,false,false);
                overridePendingTransition(R.animator.entry_animation, R.animator.exit_animation);
                break;
        }
    }

    public void FragmentAction(){
        HomeFragment homeFragment = new HomeFragment(HomeActivity.this);
        IntentAndFragmentService.fragmentdisplay(HomeActivity.this,R.id.home_frame,homeFragment,null,false,false);
    }

}
