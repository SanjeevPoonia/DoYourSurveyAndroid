package com.qdegrees.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
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

    /*********************************************/

    public static void referNewDialog(Activity mActivity,String ReferCode){
        Dialog referDialog= new Dialog(mActivity);
        referDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        referDialog.setContentView(R.layout.dialog_refernearn);
        String ReferalString=mActivity.getResources().getString(R.string.Referal_Text)+ReferCode+""+mActivity.getResources().getString(R.string.Referal_text_2);
        String Applink="https://doyoursurvey.com/#/register";
        Log.e("ReferalString",ReferalString);

        LottieAnimationView animView= referDialog.findViewById(R.id.animLottie);
        animView.playAnimation();

        CardView whatsAppCard=referDialog.findViewById(R.id.cv_referNearn_whatsapp);
        ImageView facebookImage=referDialog.findViewById(R.id.iv_referNearn_facebook);
        ImageView twitterImage=referDialog.findViewById(R.id.iv_referNearn_twitter);
        ImageView linkdinImage=referDialog.findViewById(R.id.iv_referNearn_linkedin);
        ImageView mailImage=referDialog.findViewById(R.id.iv_referNearn_mail);
        ImageView ivCloseDialog=referDialog.findViewById(R.id.ivCloseReferDialog);

        ivCloseDialog.setOnClickListener(v->{
            referDialog.dismiss();
        });

        whatsAppCard.setOnClickListener(v->{
           shareWhatsapp(mActivity,ReferalString,"");
            referDialog.dismiss();
        });
        facebookImage.setOnClickListener(v->{

           shareFacebook(mActivity,ReferalString,Applink);
            referDialog.dismiss();
        });
        twitterImage.setOnClickListener(v->{
           shareTwitter(mActivity,ReferalString,ReferalString,"","");
            referDialog.dismiss();
        });
        linkdinImage.setOnClickListener(v->{
           shareLinkedin(mActivity,ReferalString);
            referDialog.dismiss();
        });
        mailImage.setOnClickListener(v->{
            shareOnMail(mActivity,"Do Survey and Earn Points",ReferalString);
            referDialog.dismiss();
        });


        referDialog.show();
        ColorDrawable drawable = new ColorDrawable(Color.TRANSPARENT);
        referDialog.getWindow().setBackgroundDrawable(drawable);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(referDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogAnimation;
        referDialog.getWindow().setAttributes(lp);
    }

    /*********************************************/

    public static void ReferSurveyAFriendDialog(Activity mActivity,String surveyId,String referCode,String referPoint,String surveyPoints){
        Dialog referDialog= new Dialog(mActivity);
        referDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        referDialog.setContentView(R.layout.dialog_refersurvey_new);
        String referStr="You get "+referPoint+" Points,Friend gets "+surveyPoints+" Points";
        LottieAnimationView animView= referDialog.findViewById(R.id.animLottie_refer);
        animView.playAnimation();


        String Applink="https://doyoursurvey.com/#/register/attemp-survey-dyn/"+surveyId+"?surveyReferCode="+referCode;
        Log.e("ReferalString",Applink);

        CardView copyCardView=referDialog.findViewById(R.id.cv_refersurveyNew_copy);
        TextView referText=referDialog.findViewById(R.id.tv_Dialog_ReferSurveyNew_PointsText);
        referText.setText(referStr);
        ImageView facebookImage=referDialog.findViewById(R.id.iv_refersurveynew_facebook);
        ImageView twitterImage=referDialog.findViewById(R.id.iv_refersurveynew_twitter);
        ImageView whatsappImage=referDialog.findViewById(R.id.iv_refersurveynew_whatsapp);
        ImageView mailImage=referDialog.findViewById(R.id.iv_refersurveynew_mail);

        ImageView close = referDialog.findViewById(R.id.ivCloseReferSurveyDialog);

        close.setOnClickListener(v->{
            referDialog.dismiss();
        });

        copyCardView.setOnClickListener(v->{
            setClipboard(mActivity,Applink);
        });

        whatsappImage.setOnClickListener(v->{
            Utility.shareWhatsapp(mActivity,"Refer Survey To Your Friends",Applink+"&trk=wp");
            referDialog.dismiss();
        });
        facebookImage.setOnClickListener(v->{
            Utility.shareFacebook(mActivity,"Refer Survey To Your Friends",Applink+"&trk=fk");
            referDialog.dismiss();
        });
        twitterImage.setOnClickListener(v->{
            Utility.shareTwitter(mActivity, "Refer Survey To Your Friends",Applink+"&trk=tr","","");
            referDialog.dismiss();
        });
        mailImage.setOnClickListener(v->{
            Utility.shareOnMail(mActivity,"Refer Survey To Your Friends",Applink+"&tk=el");
            referDialog.dismiss();
        });




        referDialog.show();
        ColorDrawable drawable = new ColorDrawable(Color.TRANSPARENT);
        referDialog.getWindow().setBackgroundDrawable(drawable);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(referDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogAnimation;
        referDialog.getWindow().setAttributes(lp);
    }

    private static void setClipboard(Context context, String text) {

        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(context, "Refer code Copied", Toast.LENGTH_SHORT).show();

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
