package com.qdegrees.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.qdegrees.doyoursurvey.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    private static int TYPE_WIFI = 1;
    private static int TYPE_MOBILE = 2;
    private static int TYPE_NOT_CONNECTED = 0;
    private static CustomProgressDialog mCustomProgressDialog;

    public static void showAlertDialog(Context context, String title, String message) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private static int getConnectivityStatus(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (null != activeNetwork) {

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }
    public static String getNetworkClass(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isConnected())
            return "-"; //not connected
        if (info.getType() == ConnectivityManager.TYPE_WIFI)
            return "WIFI";
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkType = info.getSubtype();
            switch (networkType) {

                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                    return "4G";
                default:
                    return "?";
            }
        }
        return "?";
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public static boolean showEditTextError(EditText editText, String message) {
        editText.requestFocus();
        editText.setError(message);
        return false;
    }
    public static void showDialog(Activity activity) {
        if (mCustomProgressDialog == null && activity != null)
            mCustomProgressDialog = new CustomProgressDialog(activity);

        try {
            if (mCustomProgressDialog != null && !mCustomProgressDialog.isShowing()
                    && !activity.isFinishing())
                mCustomProgressDialog.show();
        } catch (Exception e) {
        }
    }
    /**
     * Hide Progress Dialog
     */
    public static void hideDialog(Activity activity) {
        try {
            if (mCustomProgressDialog != null && mCustomProgressDialog.isShowing() &&
                    !activity.isFinishing())
                mCustomProgressDialog.dismiss();
            mCustomProgressDialog = null;
        } catch (Exception e) {
        }
    }
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }


    /************Sharing*****************/
    public static void shareFacebook(Activity activity, String text, String url) {
        boolean facebookAppFound = false;
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));

        PackageManager pm = activity.getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.packageName).contains("com.facebook.katana")) {
                final ActivityInfo activityInfo = app.activityInfo;
                final ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setComponent(name);
                facebookAppFound = true;
                break;
            }
        }
        if (!facebookAppFound) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + url;
            shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }
        activity.startActivity(shareIntent);
    }

    /**
     * Share on Twitter. Using Twitter app if installed or web link otherwise.
     *
     * @param activity activity which launches the intent
     * @param text     text to share
     * @param url      url to share
     * @param via      twitter username without '@' who shares
     * @param hashtags hashtags for tweet without '#' and separated by ','
     */
    public static void shareTwitter(Activity activity, String text, String url, String via, String hashtags) {
        StringBuilder tweetUrl = new StringBuilder("https://twitter.com/intent/tweet?text=");
        tweetUrl.append(TextUtils.isEmpty(text) ? urlEncode(" ") : urlEncode(text));
        if (!TextUtils.isEmpty(url)) {
            tweetUrl.append("&url=");
            tweetUrl.append(urlEncode(url));
        }
        if (!TextUtils.isEmpty(via)) {
            tweetUrl.append("&via=");
            tweetUrl.append(urlEncode(via));
        }
        if (!TextUtils.isEmpty(hashtags)) {
            tweetUrl.append("&hastags=");
            tweetUrl.append(urlEncode(hashtags));
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl.toString()));
        List<ResolveInfo> matches = activity.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                intent.setPackage(info.activityInfo.packageName);
            }
        }
        activity.startActivity(intent);
    }

    /**
     * Share on Whatsapp (if installed)
     *
     * @param activity activity which launches the intent
     * @param text     text to share
     * @param url      url to share
     */
    public static void shareWhatsapp(Activity activity, String text, String url) {
        PackageManager pm = activity.getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text + " " + url);
            activity.startActivity(Intent
                    .createChooser(waIntent, activity.getString(R.string.refer_title)));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(activity, activity.getString(R.string.refer_whatsapp_not_found),
                    Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Convert to UTF-8 text to put it on url format
     *
     * @param s text to be converted
     * @return text on UTF-8 format
     */
    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("wtf", "UTF-8 should always be supported", e);
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }



    public static void shareLinkedin(Activity activity,String text1){
        Intent linkedinIntent = new Intent(Intent.ACTION_SEND);
        linkedinIntent.setType("text/plain");
        linkedinIntent.putExtra(Intent.EXTRA_TEXT, text1);

        boolean linkedinAppFound = false;
        List<ResolveInfo> matches2 = activity.getPackageManager()
                .queryIntentActivities(linkedinIntent, 0);

        for (ResolveInfo info : matches2) {
            if (info.activityInfo.packageName.toLowerCase().startsWith(
                    "com.linkedin")) {
                linkedinIntent.setPackage(info.activityInfo.packageName);
                linkedinAppFound = true;
                break;
            }
        }

        if (linkedinAppFound) {
            activity.startActivity(linkedinIntent);
        }
        else
        {
            Toast.makeText(activity,"LinkedIn app not Insatlled in your mobile", Toast.LENGTH_SHORT).show();
        }
    }

    public static void shareOnMail(Activity activity,String subject, String message){
        Intent i = new Intent(Intent.ACTION_SENDTO);
        //i.setType("message/rfc822");
        i.setData(Uri.parse("mailto:"));
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{""});
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT   , message);
        try {
            if (i.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(Intent.createChooser(i, "Send mail..."));
            }

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }
}
