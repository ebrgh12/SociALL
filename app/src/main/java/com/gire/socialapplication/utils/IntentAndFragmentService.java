package com.gire.socialapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by girish on 11/7/2015.
 */
public class IntentAndFragmentService {

    private static FragmentManager fragmentManager = null;

    public static void fragmentdisplay(FragmentActivity context, int viewID, Fragment fragmentName, Bundle values, boolean forBackPress, boolean refresh) {
        try {
            if (fragmentName != null) {
                if (values != null && values.size() > 0) {
                    fragmentName.setArguments(values);
                }
                fragmentManager = context.getSupportFragmentManager();
                if (refresh) {
                    if (fragmentName.isVisible()) {
                        fragmentManager.beginTransaction()
                                .remove(fragmentName).commit();
                    }else{
                        fragmentManager.beginTransaction()
                                .remove(fragmentManager.findFragmentById(viewID)).commit();
                        fragmentManager.popBackStackImmediate();
                }}
                if (forBackPress) {
                    fragmentManager.beginTransaction()
                            .add(viewID, fragmentName).addToBackStack(null).commit();
                } else {
                        fragmentManager.beginTransaction()
                                .replace(viewID, fragmentName).commit();

                }
            } else {
            }
        } catch (NullPointerException e) {
        }
    }

    public static void intentDisplay(Context currentActivity, Class callingActivity, Bundle values) {
        try {
            Intent intent = new Intent(currentActivity, callingActivity);
            if (values != null && values.size() > 0 && !values.isEmpty()) {
                intent.putExtra("bundleWithValues", values);
            }
            currentActivity.startActivity(intent);
        } catch (NullPointerException e) {
        }
    }
}
