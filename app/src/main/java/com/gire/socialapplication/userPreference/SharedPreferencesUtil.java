package com.gire.socialapplication.userPreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.gire.socialapplication.constants.CommonConstants;

import static com.gire.socialapplication.constants.CommonConstants.userAge;
import static com.gire.socialapplication.constants.CommonConstants.userEducation;
import static com.gire.socialapplication.constants.CommonConstants.userHobbies;
import static com.gire.socialapplication.constants.CommonConstants.userInterests;
import static com.gire.socialapplication.constants.CommonConstants.userSexId;

/**
 * Created by girish on 6/14/2017.
 */

public class SharedPreferencesUtil {

    private static SharedPreferencesUtil sInstance;

    private SharedPreferences mPrefs;

    public static SharedPreferencesUtil getInstance(Context context) {
        if (sInstance == null) {
            sInstance =  new SharedPreferencesUtil(context);
        }
        return sInstance;
    }

    private SharedPreferencesUtil(Context context) {
        mPrefs = context.getSharedPreferences(CommonConstants.sharedPreference, 0);
    }

    /* set user profile to priff */
    public void setUserPriff(String customerId,String userName,String userEmail,String userPhone,String userImage){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(CommonConstants.userId, customerId);
        editor.putString(CommonConstants.userName,userName);
        editor.putString(CommonConstants.userEmail,userEmail);
        editor.putString(CommonConstants.userPhone,userPhone);
        editor.putString(CommonConstants.userImage,userImage);
        editor.commit();
    }

    public void setInterests(String interests){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(userInterests,interests);
        editor.commit();
    }

    public void setHobby(String hobbies){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(userHobbies,hobbies);
        editor.commit();
    }

    public void setAge(String age){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(userAge,age);
        editor.commit();
    }

    public void setEducation(String education){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(userEducation,education);
        editor.commit();
    }

    public void setUserSex(Integer id){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(userSexId,id);
        editor.commit();
    }

    /*all retrieve methods*/
    public String getCustomerID(){
        return mPrefs.getString(CommonConstants.userId,null);
    }

    public String getUserName(){
        return mPrefs.getString(CommonConstants.userName,null);
    }

    public String getUserEmail(){
        return mPrefs.getString(CommonConstants.userEmail,null);
    }

    public String getUserPhone(){
        return mPrefs.getString(CommonConstants.userPhone,null);
    }

    public String getUserImage(){
        return mPrefs.getString(CommonConstants.userImage,null);
    }

    public String getUserInterests(){
        return mPrefs.getString(CommonConstants.userInterests,null);
    }

    public String getUserHobbies(){
        return mPrefs.getString(CommonConstants.userHobbies,null);
    }

    public String getUserAge(){
        return mPrefs.getString(CommonConstants.userAge,null);
    }

    public String getUserEducation(){
        return mPrefs.getString(CommonConstants.userEducation,null);
    }

    public Integer getUserSexId(){
        return mPrefs.getInt(CommonConstants.userSexId,0);
    }

    public void logOut()
    {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.clear();
        editor.commit();
    }

}
