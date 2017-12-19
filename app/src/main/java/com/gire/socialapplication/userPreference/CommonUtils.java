package com.gire.socialapplication.userPreference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by girish on 26/5/16.
 */
public class CommonUtils {

    private static Pattern pattern;
    private static Matcher matcher;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean validateEmail(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static void startActivity(Context context,Class activity){
        Intent intent=new Intent(context,activity);
        context.startActivity(intent);
    }

    public static void logoutSesstion(Context context){
    }

    /*public static void customToast(Context context,String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        View view = toast.getView();
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setGravity(Gravity.CENTER);
        text.setTextColor(context.getResources().getColor(R.color.white));
        toast.show();
    }*/



    public static void hideKeyboard(Context context,View view) {
        InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length()>0 ? true: false;
    }

    // mobile validation(0-6)
    public static boolean validateFirstNumbersMobile(String mobile){
        String MOBILE_PATERN="^[0-9+ ]*$";
        boolean test = false;

            pattern = Pattern.compile(MOBILE_PATERN);
            matcher = pattern.matcher(mobile);
            test=matcher.matches();


        return test;
    }

    public static Uri getPlaystoreLink(Context context){
        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        try {
            return Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName);
        } catch (android.content.ActivityNotFoundException anfe) {
            return Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName);
        }
    }



    public static final boolean isConnectionAvailable(Context act) {
        try {
            ConnectivityManager cm = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo.State wifi = cm.getNetworkInfo(1).getState();

            NetworkInfo.State mobile = cm.getNetworkInfo(0).getState();

            if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
                return true;
            } else if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setPrefs(String key, String value, Context context) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(key, value);
            editor.commit();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static String getPrefs(String key, Context context) {
        SharedPreferences preferences=null;
        try {
          preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }catch (Exception e){
            e.printStackTrace();
        }
        return preferences.getString(key, null);
    }

    public static void deletePrefs(String key, Context context) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().remove(key).commit();

    }


    public static String setAMPM(String fromTime,String toTime){
        String timeString="";
        String from[]=fromTime.split(":");
        String to[]=toTime.split(":");
        if(Integer.parseInt(from[0])>=12){
            timeString = from[0] +" PM";
        }else {
            timeString = from[0] +" AM";
        }

        if(Integer.parseInt(to[0])>=12){
            timeString =timeString+"-"+to[0] +" PM";
        }else {
            timeString =timeString+"-"+to[0] +" AM";
        }

        return timeString;
    }

    /*public static void showDialog(Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }*/

    public static String getMonthName(int month){
        switch(month){
            case 1:
                return "JAN";

            case 2:
                return "FEB";

            case 3:
                return "MAR";

            case 4:
                return "APR";

            case 5:
                return "MAY";

            case 6:
                return "JUN";

            case 7:
                return "JUL";

            case 8:
                return "AUG";

            case 9:
                return "SEP";

            case 10:
                return "OCT";

            case 11:
                return "NOV";

            case 12:
                return "DEC";
        }

        return "";
    }

    public static void displayToastMsg(Activity activity,String msg){

        Toast.makeText(activity,msg,Toast.LENGTH_LONG).show();
    }


}
