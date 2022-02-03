package com.qdegrees.activity.ui.dashboard;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qdegrees.activity.ui.panelprofile.PanelProfileFragment;
import com.qdegrees.activity.ui.panelprofile.ProfileSurveyListItem;
import com.qdegrees.activity.ui.survey.ProfileSurvey_AttemptActivity;
import com.qdegrees.activity.ui.survey.SurveyStart_Activity;
import com.qdegrees.doyoursurvey.R;
import com.qdegrees.local_storage.SharedPreferencesRepository;
import com.qdegrees.network.ApiService;
import com.qdegrees.network.RetrofitAPIClient;
import com.qdegrees.network.request.DashboardPostData;
import com.qdegrees.network.request.GenerateSurveyReferCodeRequest;
import com.qdegrees.network.request.ReferAFriend_Request;
import com.qdegrees.network.response.DashboardProfileResponse;
import com.qdegrees.network.response.DashboardResponse;
import com.qdegrees.network.response.GenerateSurveyReferCodeResponse;
import com.qdegrees.network.response.GeneratedSurveyReferCode_Response;
import com.qdegrees.network.response.ReedemPointResponse;
import com.qdegrees.network.response.ShareableSurveyResponse;
import com.qdegrees.utils.Utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bolts.AppLink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {
    Activity mActivity;
    RecyclerView recyclerView,shareableSurveyRecyclerView;
    protected ApiService apiService;
    String sUserEmail="";
    List<Survey_Listitem_Object> surveyList= new ArrayList<>();
    List<Shareable_Survey_ListItem_Object>shareAble_SurveyList= new ArrayList<>();
    ShareableListAdapter shareableListAdapter;
    MyAssignedListAdapter myAssignedListAdapter;

    String PointEarned="0",PointReedem="0",PointAvailable="0",CompletedSurvey="0";
    TextView tvPointEarned,tvPointRedeem,tvPointAvailable,tvCompletedSurvey;
    LinearLayout NoDataLayout,ShareableLayout;
    CardView RefreshCardView;

    /**************************Profile*********************/

    CardView cvProfile;
    int ProfilePercentage=0;
    ProgressBar progressBar;
    TextView tvPercentage,tvComplete,tvClickHere;
    List<ProfileSurveyListItem> ProfileSurveyList= new ArrayList<>();
    Dialog profileDialog;


    public static final int REQUEST_CODE=12;
    public static final int RESULT_CODE=13;
    public static final String Extra_Test_KEY="textKey";

    /****************Refer Friend*******************************/

    CardView referFriend;
    String ReferCode="";


    String sUserName="";


    /*****************Demo Screens***************************/

    RelativeLayout DemoLayout;
    ImageView demoImageView;
    Button demoButton;
    int demoPosition=1;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mActivity=getActivity();
        sUserEmail= SharedPreferencesRepository.getDataManagerInstance().getSessionEmail();
        ReferCode=SharedPreferencesRepository.getDataManagerInstance().getSessionReferCode();
        sUserName=SharedPreferencesRepository.getDataManagerInstance().getSessionname()+" "+SharedPreferencesRepository.getDataManagerInstance().getSessionLastname();


        apiService = RetrofitAPIClient.getRetrofitClient();
        recyclerView=root.findViewById(R.id.rvFragmentHome);
        shareableSurveyRecyclerView=root.findViewById(R.id.rv_Shareable_FragmentHome);


        LinearLayoutManager llm= new LinearLayoutManager(mActivity);
        LinearLayoutManager sllm= new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(llm);
        shareableSurveyRecyclerView.setLayoutManager(sllm);

        tvPointEarned=root.findViewById(R.id.tvFragmentHomePointEarned);
        tvPointRedeem=root.findViewById(R.id.tvFragmentHomePointRedeemed);
        tvPointAvailable=root.findViewById(R.id.tvFragmentHomePointAvailable);
        tvCompletedSurvey=root.findViewById(R.id.tvFragmentHomeCompletedSurvey);

        progressBar=root.findViewById(R.id.pbFragmentHome_Profile);
        tvPercentage=root.findViewById(R.id.tvFragmentHome_Percentage);
        tvComplete=root.findViewById(R.id.tv_fragmentHome_ProfileTitle);
        tvClickHere=root.findViewById(R.id.tv_fragmenthome_ClickHere);
        cvProfile=root.findViewById(R.id.cv_fragmentHome_Profile);
        referFriend=root.findViewById(R.id.cv_dashboard_referbtn);

        NoDataLayout=root.findViewById(R.id.llFragmentHomeNoData);
        RefreshCardView=root.findViewById(R.id.cv_dashboard_Refresh);


        DemoLayout=root.findViewById(R.id.Fragment_Home_Demo_Layout);
        demoImageView=root.findViewById(R.id.Fragment_Home_DemoImg);
        demoButton=root.findViewById(R.id.Fragment_Home_NextBtn);

        ShareableLayout=root.findViewById(R.id.ll_fragment_home_Shareable);


        demoButton.setOnClickListener(v->{
            demoPosition=demoPosition+1;

            if(demoPosition==4){
                DemoLayout.setVisibility(View.GONE);
                SharedPreferencesRepository.getDataManagerInstance().setDashboardDemoDone();
            }else{
                showDemo();
            }


        });



        RefreshCardView.setOnClickListener(v->{
            getProfileDashboard();
        });

        cvProfile.setOnClickListener(v->{
            Log.e("profileSurvey",ProfileSurveyList.size()+"");
            if(ProfileSurveyList.size()>0){
                createProfileDialog();
            }
        });

        referFriend.setOnClickListener(v->{
            ReferAFriendDialog();
        });

        getProfileDashboard();


        return root;
    }

    private void getDashboardData(){
        if(Utility.isNetworkAvailable(mActivity)){
            Utility.showDialog(mActivity);
            apiService.getDashboardData(new DashboardPostData(sUserEmail)).enqueue(new Callback<DashboardResponse>()
               {
                   @Override
                   public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                       try {
                           DashboardResponse dashboardResponse = response.body();
                           Log.e("Response", dashboardResponse.toString());
                           int code = dashboardResponse.status;
                           if (code == 200) {
                               surveyList.clear();
                               List<DashboardResponse.Data> dataList = dashboardResponse.data;
                               for (int i = 0; i < dataList.size(); i++) {
                                   String _id = dataList.get(i).id;
                                   String surveyId = dataList.get(i).survey_id;
                                   String surveyName = dataList.get(i).survey_name;
                                   String surveyPoints = dataList.get(i).points;
                                   String surveyStatus = dataList.get(i).status;
                                   String surveyType = dataList.get(i).survey_type;
                                   String surveyDate = dataList.get(i).date;
                                   List<SurveyQuestions_ListItem> QuestionList = new ArrayList<>();
                                   List<DashboardResponse.Questions> questions = dataList.get(i).Questions;
                                   for (int j = 0; j < questions.size(); j++) {
                                       String Idstr = questions.get(j).id;
                                       String Ques = questions.get(j).question;
                                       String queMsg = questions.get(j).que_message;
                                       String selection = questions.get(j).selection;
                                       String type = questions.get(j).type;
                                       String status = questions.get(j).status;
                                       String conditions = questions.get(j).condition;
                                       String profiling=questions.get(j).profiling;
                                       String dynamicSelection=questions.get(j).dynamicSelection;
                                       String minSelec=questions.get(j).min_selection;
                                       String maxSelec=questions.get(j).max_selection;
                                       List<String> requiredOnePf=new ArrayList<>();
                                       requiredOnePf=questions.get(j).requiredAnyOneFrom;

                                       boolean isResponseApplica=questions.get(j).is_response_count_applicable;
                                       boolean showHideAppli=questions.get(j).show_hide_applicable;
                                       String showHideTargetId=questions.get(j).show_hide_applicable_target_id;

                                       List<SurveyOptions_ListItem> OptionsList = new ArrayList<>();
                                       List<DashboardResponse.Options> options = questions.get(j).Options;

                                       for (int k = 0; k < options.size(); k++) {
                                           String option = options.get(k).option;
                                           String actionId = options.get(k).action_id;
                                           String textBox = options.get(k).text_box;
                                           int responseLimit=options.get(k).response_limit;

                                           List<String> coloums=new ArrayList<>();
                                           coloums=options.get(k).coloumns;


                                           List<SurveyOptions_HideShow_LIst> ShowHideList= new ArrayList<>();
                                           List<DashboardResponse.ShowHideOption> showOp=options.get(k).showHideOptions;
                                           if(showOp!=null) {
                                               for (int l = 0; l < showOp.size(); l++) {
                                                   String ProductName = showOp.get(l).product_name;
                                                   boolean ProductValue = showOp.get(l).product_value;
                                                   ShowHideList.add(new SurveyOptions_HideShow_LIst(ProductName, ProductValue));
                                               }
                                           }


                                           OptionsList.add(new SurveyOptions_ListItem(option, actionId, textBox,responseLimit,ShowHideList,true,coloums));
                                       }
                                       QuestionList.add(new SurveyQuestions_ListItem(Idstr, Ques, queMsg, selection, type, status, conditions, OptionsList,dynamicSelection,minSelec,maxSelec,requiredOnePf,profiling,isResponseApplica,showHideAppli,showHideTargetId));
                                   }
                                   surveyList.add(new Survey_Listitem_Object(_id, surveyName, surveyId, surveyPoints, surveyStatus, surveyType, surveyDate, QuestionList));
                               }
                               Utility.hideDialog(mActivity);
                               setDataOnView();

                           } else {
                               Utility.showAlertDialog(mActivity, "Error!!", "Something went wrong!!");
                               Utility.hideDialog(mActivity);
                           }
                       }catch(Exception e){
                               e.printStackTrace();
                               Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                               Utility.hideDialog(mActivity);
                           }

                   }

                   @Override
                   public void onFailure(Call<DashboardResponse> call, Throwable t) {
                       Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                       Utility.hideDialog(mActivity);
                   }
               }
            );

        }else{
            Utility.showAlertDialog(mActivity, mActivity.getString(R.string.alert), mActivity.getString(R.string.no_internet_connection));
        }
    }
    private void setDataOnView(){



        myAssignedListAdapter= new MyAssignedListAdapter(mActivity,surveyList);
        recyclerView.setAdapter(myAssignedListAdapter);
        myAssignedListAdapter.notifyDataSetChanged();

        if(surveyList.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            NoDataLayout.setVisibility(View.GONE);
        }else{
            recyclerView.setVisibility(View.GONE);
            NoDataLayout.setVisibility(View.VISIBLE);
        }

        getShareableData();

        if(SharedPreferencesRepository.getDataManagerInstance().IsDashboardDemoRequired()){
            DemoLayout.setVisibility(View.VISIBLE);
            showDemo();
        }else{
            DemoLayout.setVisibility(View.GONE);
        }



    }
    class AssignedListHolder extends RecyclerView.ViewHolder{

        CardView mainView;
        TextView SurveyPointText,SurveyNameText;
        public AssignedListHolder(View view){
            super(view);
            mainView=view.findViewById(R.id.cv_fragment_home_listitem);
            SurveyPointText=view.findViewById(R.id.tv_fragmentHomeListItem_Points);
            SurveyNameText=view.findViewById(R.id.tv_fragmentHomeListItem_SurveyName);
        }

    }
    class MyAssignedListAdapter extends RecyclerView.Adapter<AssignedListHolder>{

        List<Survey_Listitem_Object>itemList;
        Context mContext;
        public MyAssignedListAdapter(Context c, List<Survey_Listitem_Object> list){
            this.itemList=list;
            this.mContext=c;
        }
        @NonNull
        @Override
        public AssignedListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_listitem_layout,parent,false);
            AssignedListHolder auditListHolder= new AssignedListHolder(itemView);
            return auditListHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull AssignedListHolder holder, int position) {

            String surveyId=itemList.get(position).getId();
            String surveyName=itemList.get(position).getSurveyName();
            String surveyPoints=itemList.get(position).getPoints();
            List<SurveyQuestions_ListItem> QuestionList= itemList.get(position).getQuestionsList();;


            Log.e("survey Points",surveyPoints);
            holder.SurveyNameText.setText(surveyName);
            holder.SurveyPointText.setText(surveyPoints);


            holder.mainView.setOnClickListener(v->{
                Intent intent=new Intent(mActivity, SurveyStart_Activity.class);
                intent.putExtra("survey_id",surveyId);
                intent.putExtra("survey_name",surveyName);
                intent.putExtra("survey_points",surveyPoints);
                intent.putExtra("questionList",(ArrayList<SurveyQuestions_ListItem>)QuestionList);
                startActivityForResult(intent,REQUEST_CODE);
                //startActivity(intent);


            });



        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

    }
    /*********Point Data*****************/
    private void getProfileDashboard(){
        if(Utility.isNetworkAvailable(mActivity)){
            Utility.showDialog(mActivity);
            apiService.getDashboardProfile(new DashboardPostData(sUserEmail)).enqueue(new Callback<DashboardProfileResponse>() {
                @Override
                public void onResponse(Call<DashboardProfileResponse> call, Response<DashboardProfileResponse> response) {
                   try {
                       DashboardProfileResponse dashboardProfileResponse = response.body();
                       int code = dashboardProfileResponse.status;
                       if (code == 200) {
                           ProfileSurveyList.clear();
                           List<DashboardProfileResponse.profileTaskStatus> proflei = dashboardProfileResponse.data.ProfileTask;
                           List<DashboardProfileResponse.dataUser> userData = dashboardProfileResponse.data.dataUsers;
                           for (int i = 0; i < userData.size(); i++) {
                               PointEarned = userData.get(i).pointEarned;
                               PointReedem = userData.get(i).pointRedeemed;
                               PointAvailable = userData.get(i).pointAvailable;
                               CompletedSurvey = userData.get(i).completedSurvey;
                               ProfilePercentage = userData.get(i).profilePercent;
                           }
                           for (int i = 0; i < proflei.size(); i++) {
                               boolean pr = proflei.get(i).status;
                               String id = proflei.get(i).id;
                               String taskName = proflei.get(i).taskName;
                               if (!pr) {
                                   ProfileSurveyList.add(new ProfileSurveyListItem(id, taskName, pr));
                               }
                           }

                           tvPointEarned.setText(PointEarned);
                           tvPointRedeem.setText(PointReedem);
                           tvPointAvailable.setText(PointAvailable);
                           tvCompletedSurvey.setText(CompletedSurvey);

                           progressBar.setProgress(ProfilePercentage);
                           tvPercentage.setText(ProfilePercentage + "%");

                           if (ProfilePercentage == 100) {
                               tvComplete.setText("Thank you !! Your Profile Completed");
                               tvClickHere.setVisibility(View.GONE);
                           } else {
                               tvComplete.setText("Please complete your profile & Earn points");
                               tvClickHere.setVisibility(View.VISIBLE);
                           }


                       } else {
                           Utility.showAlertDialog(mActivity, "Error!!", "Something went wrong!!");
                       }
                       Utility.hideDialog(mActivity);
                       getDashboardData();
                   }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                    Utility.hideDialog(mActivity);
                }
                }

                @Override
                public void onFailure(Call<DashboardProfileResponse> call, Throwable t) {
                    Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                    Utility.hideDialog(mActivity);
                    getDashboardData();
                }
            });

        }else{
            Utility.showAlertDialog(mActivity, mActivity.getString(R.string.alert), mActivity.getString(R.string.no_internet_connection));
        }
    }
    private void createProfileDialog(){
        profileDialog= new Dialog(mActivity);
        profileDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        profileDialog.setContentView(R.layout.dialog_profilesurvey);
        ImageView closeImg=profileDialog.findViewById(R.id.ivCloseProfileDialog);
        RecyclerView profileRecyclerView=profileDialog.findViewById(R.id.rvDialogProfile);
        LinearLayoutManager llm= new LinearLayoutManager(mActivity);
        profileRecyclerView.setLayoutManager(llm);

        ProfileListAdapter profileListAdapter= new ProfileListAdapter(mActivity,ProfileSurveyList);
        profileRecyclerView.setAdapter(profileListAdapter);
        profileListAdapter.notifyDataSetChanged();

        closeImg.setOnClickListener(v->{
            profileDialog.dismiss();
        });
        profileDialog.show();
        ColorDrawable drawable = new ColorDrawable(Color.TRANSPARENT);
        profileDialog.getWindow().setBackgroundDrawable(drawable);
        profileDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);




    }
    class ProfileListHolder extends RecyclerView.ViewHolder{

        CardView mainView;
        TextView SurveyNameText,StartSurvey;
        public ProfileListHolder(View view){
            super(view);
            mainView=view.findViewById(R.id.cv_fragment_profile_listitem);
            SurveyNameText=view.findViewById(R.id.tv_fragmentProfileListItem_SurveyName);
            StartSurvey=view.findViewById(R.id.tv_fragment_profile_StartSurvey);

        }

    }
    class ProfileListAdapter extends RecyclerView.Adapter<ProfileListHolder>{

        List<ProfileSurveyListItem>itemList;
        Context mContext;
        public ProfileListAdapter(Context c, List<ProfileSurveyListItem> list){
            this.itemList=list;
            this.mContext=c;
        }
        @NonNull
        @Override
        public ProfileListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_profile_survey_listitem,parent,false);
            ProfileListHolder auditListHolder= new ProfileListHolder(itemView);
            return auditListHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ProfileListHolder holder, int position) {

            String surveyId=itemList.get(position).getSurveyId();
            String surveyName=itemList.get(position).getTaskName();
            holder.SurveyNameText.setText(surveyName.toUpperCase());

            holder.StartSurvey.setText("Start Survey to Complete Profile "+surveyName.toUpperCase()+" Details");

            holder.mainView.setOnClickListener(v->{
                if(profileDialog!=null){
                    profileDialog.dismiss();
                }
                Log.e("Percentage",""+ProfilePercentage);
                Intent intent= new Intent(mActivity, ProfileSurvey_AttemptActivity.class);
                intent.putExtra("survey_type",surveyName);
                intent.putExtra("percentage",ProfilePercentage);
                startActivityForResult(intent,REQUEST_CODE);
                //startActivity(intent);
            });



        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode == RESULT_CODE){
            getProfileDashboard();
        }
    }

    /**************************************Refer Friend Functionality***********************************************/
    private void ReferAFriendDialog(){
        Dialog referDialog= new Dialog(mActivity);
        referDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        referDialog.setContentView(R.layout.dialog_referfriend);

        String ReferalString=mActivity.getResources().getString(R.string.Referal_Text)+ReferCode+""+mActivity.getResources().getString(R.string.Referal_text_2);
        String Applink="https://doyoursurvey.com/#/register";
        Log.e("ReferalString",ReferalString);


        CardView whatsAppCard=referDialog.findViewById(R.id.cv_refer_whatsapp);
        EditText NameEditText=referDialog.findViewById(R.id.et_refer_name);
        EditText EmaildEditText=referDialog.findViewById(R.id.et_refer_Email);
        CardView submitCard=referDialog.findViewById(R.id.cv_refer_submit);
        ImageView facebookImage=referDialog.findViewById(R.id.iv_refer_facebook);
        ImageView twitterImage=referDialog.findViewById(R.id.iv_refer_twitter);
        ImageView linkdinImage=referDialog.findViewById(R.id.iv_refer_linkedin);
        ImageView mailImage=referDialog.findViewById(R.id.iv_refer_mail);




        whatsAppCard.setOnClickListener(v->{
            Utility.shareWhatsapp(mActivity,ReferalString,"");
            referDialog.dismiss();
        });
        facebookImage.setOnClickListener(v->{

          Utility.shareFacebook(mActivity,ReferalString,Applink);
            referDialog.dismiss();
        });
        twitterImage.setOnClickListener(v->{
            Utility.shareTwitter(mActivity,ReferalString,ReferalString,"","");
            referDialog.dismiss();
        });
        linkdinImage.setOnClickListener(v->{
            Utility.shareLinkedin(mActivity,ReferalString);
            referDialog.dismiss();
        });
        mailImage.setOnClickListener(v->{
            Utility.shareOnMail(mActivity,"Do Survey and Earn Points",ReferalString);
            referDialog.dismiss();
        });

        submitCard.setOnClickListener(v->{
            String nameStr=NameEditText.getText().toString();
            String emailStr=EmaildEditText.getText().toString();
            if(!TextUtils.isEmpty(emailStr)&& Utility.isValidEmail(emailStr)){
                doReferFriend(nameStr,emailStr);
                referDialog.dismiss();
            }else{
                EmaildEditText.setError("Please Enter a valid Email ID");
                Toast.makeText(mActivity, "Please Enter a valid Email ID", Toast.LENGTH_SHORT).show();
            }
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
    private void doReferFriend(String name,String email){
        if(Utility.isNetworkAvailable(mActivity)){
            Utility.showDialog(mActivity);
            apiService.DoReferFriend(new ReferAFriend_Request(sUserName,name,ReferCode,sUserEmail,email)).enqueue(new Callback<ReedemPointResponse>() {
                @Override
                public void onResponse(Call<ReedemPointResponse> call, Response<ReedemPointResponse> response) {
                    ReedemPointResponse reedemPointResponse= response.body();
                    int code=reedemPointResponse.status;
                    String msg=reedemPointResponse.message;
                    Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                    Utility.hideDialog(mActivity);

                }

                @Override
                public void onFailure(Call<ReedemPointResponse> call, Throwable t) {
                    Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                    Utility.hideDialog(mActivity);
                }
            });

        }else {
            Utility.showAlertDialog(mActivity, mActivity.getString(R.string.alert), mActivity.getString(R.string.no_internet_connection));
        }
    }
    /********************Show Demo Functionality*******************************/
    private void showDemo(){
        if(demoPosition==1){
            demoImageView.setImageResource(R.drawable.demo_profile);
            demoButton.setText("Next");
        }else if(demoPosition==2){
            demoImageView.setImageResource(R.drawable.demo_survey);
            demoButton.setText("Next");
        }else if(demoPosition==3){
            demoImageView.setImageResource(R.drawable.demo_refer);
            demoButton.setText("Got It");
        }
    }

    /************************Sharable Survey Functionality****************************/
    private void getShareableData(){
        if(Utility.isNetworkAvailable(mActivity)){
            Utility.showDialog(mActivity);
            apiService.getShareableSurveyList().enqueue(new Callback<ShareableSurveyResponse>() {
                @Override
                public void onResponse(Call<ShareableSurveyResponse> call, Response<ShareableSurveyResponse> response) {
                    try {
                        ShareableSurveyResponse dashboardResponse = response.body();
                        Log.e("Response", dashboardResponse.toString());
                        int code = dashboardResponse.status;
                        if (code == 200) {
                            shareAble_SurveyList.clear();
                            List<ShareableSurveyResponse.Data> dataList = dashboardResponse.data;
                            for (int i = 0; i < dataList.size(); i++) {
                                String _id = dataList.get(i).id;
                                String surveyId = dataList.get(i).survey_id;
                                String surveyName = dataList.get(i).survey_name;
                                String surveyPoints = dataList.get(i).points;
                                String surveyStatus = dataList.get(i).status;
                                String surveyType = dataList.get(i).survey_type;
                                String surveyDate = dataList.get(i).date;
                                String ReferPoints=dataList.get(i).refer_point;
                                boolean ReferApplicable=dataList.get(i).refer_applicable;
                                shareAble_SurveyList.add(new Shareable_Survey_ListItem_Object(
                                        _id,surveyName,surveyId,surveyPoints,surveyStatus,surveyType,ReferApplicable,ReferPoints,surveyDate
                                ));


                            }
                            Utility.hideDialog(mActivity);
                            setSharableData();

                        } else {
                            Utility.showAlertDialog(mActivity, "Error!!", "Something went wrong!!");
                            Utility.hideDialog(mActivity);
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                        Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                        Utility.hideDialog(mActivity);
                        ShareableLayout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ShareableSurveyResponse> call, Throwable t) {
                    Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                    Utility.hideDialog(mActivity);
                    ShareableLayout.setVisibility(View.GONE);
                }
            });


        }else{
            Utility.showAlertDialog(mActivity, mActivity.getString(R.string.alert), mActivity.getString(R.string.no_internet_connection));
        }
    }
    private void setSharableData(){
        shareableListAdapter= new ShareableListAdapter(mActivity,shareAble_SurveyList);
        shareableSurveyRecyclerView.setAdapter(shareableListAdapter);
        shareableListAdapter.notifyDataSetChanged();
        if(shareAble_SurveyList.size()>0){
            ShareableLayout.setVisibility(View.VISIBLE);
        }else{
            ShareableLayout.setVisibility(View.GONE);
        }

    }
    class shareableListHolder extends RecyclerView.ViewHolder{

        CardView mainView;
        TextView SurveyPointText,SurveyNameText,ShareText;
        public shareableListHolder(View view){
            super(view);
            mainView=view.findViewById(R.id.cv_fragment_home_listitem);
            SurveyPointText=view.findViewById(R.id.tv_fragmentHomeListItem_Points);
            SurveyNameText=view.findViewById(R.id.tv_fragmentHomeListItem_SurveyName);
            ShareText=view.findViewById(R.id.tv_fragmenthomeListItem_AnswerSurvey);
        }

    }
    class ShareableListAdapter extends RecyclerView.Adapter<shareableListHolder>{

        List<Shareable_Survey_ListItem_Object>itemList;
        Context mContext;
        public ShareableListAdapter(Context c, List<Shareable_Survey_ListItem_Object> list){
            this.itemList=list;
            this.mContext=c;
        }
        @NonNull
        @Override
        public shareableListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_listitem_layout,parent,false);
            shareableListHolder auditListHolder= new shareableListHolder(itemView);
            return auditListHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull shareableListHolder holder, int position) {

            String surveyId=itemList.get(position).get_ID();
            String surveyName=itemList.get(position).getSurveyName();
            String surveyPoints=itemList.get(position).getPoints();
            String referalPoint=itemList.get(position).getReferPoint();
            Log.e("survey Points",surveyPoints);
            holder.SurveyNameText.setText(surveyName);
            holder.SurveyPointText.setText(referalPoint);

            holder.ShareText.setText("Refer");


            holder.mainView.setOnClickListener(v->{
                generateReferalCode(surveyId,referalPoint,surveyPoints);
            });



        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

    }
    private void generateReferalCode(String surveyId,String referPoint,String surveyPoint){
        if(Utility.isNetworkAvailable(mActivity)){
            Utility.showDialog(mActivity);

            apiService.generateSurveyReferCode(new GenerateSurveyReferCodeRequest(sUserEmail,surveyId)).enqueue(new Callback<GenerateSurveyReferCodeResponse>() {
                @Override
                public void onResponse(Call<GenerateSurveyReferCodeResponse> call, Response<GenerateSurveyReferCodeResponse> response) {
                    try{
                        GenerateSurveyReferCodeResponse generateSurveyReferCodeResponse= response.body();
                        assert generateSurveyReferCodeResponse != null;
                        int code = generateSurveyReferCodeResponse.status;
                        String message=generateSurveyReferCodeResponse.message;
                        if(code==200){
                            Utility.hideDialog(mActivity);
                            getGeneratedReferalCode(surveyId,referPoint,surveyPoint);

                        }else{
                            Utility.showAlertDialog(mActivity, "Error!!", message);
                            Utility.hideDialog(mActivity);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                        Utility.hideDialog(mActivity);
                    }
                }

                @Override
                public void onFailure(Call<GenerateSurveyReferCodeResponse> call, Throwable t) {
                    Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                    Utility.hideDialog(mActivity);
                }
            });
        }else{
            Utility.showAlertDialog(mActivity, mActivity.getString(R.string.alert), mActivity.getString(R.string.no_internet_connection));
        }
    }
    private void getGeneratedReferalCode(String surveyId,String referPoint,String surveyPoint){
        if(Utility.isNetworkAvailable(mActivity)) {
            Utility.showDialog(mActivity);
            apiService.getGeneratedReferalCode(new GenerateSurveyReferCodeRequest(sUserEmail,surveyId)).enqueue(new Callback<GeneratedSurveyReferCode_Response>() {
                @Override
                public void onResponse(Call<GeneratedSurveyReferCode_Response> call, Response<GeneratedSurveyReferCode_Response> response) {
                    try{
                        GeneratedSurveyReferCode_Response generatedSurveyReferCode_response=response.body();

                        assert generatedSurveyReferCode_response != null;
                        int status=generatedSurveyReferCode_response.status;
                        String message=generatedSurveyReferCode_response.message;
                        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                        if(status==200){
                            List<GeneratedSurveyReferCode_Response.Data> data=generatedSurveyReferCode_response.DataList;

                            String ReferalId="";
                            String ReferalSurveyId="";
                            String ReferalCode="";

                            for(int i=0;i<data.size();i++){
                                List<GeneratedSurveyReferCode_Response.ReferalData> referalData=data.get(i).referalData;
                                for(int j=0;j<referalData.size();j++){
                                        ReferalId=referalData.get(j).ID;
                                        ReferalSurveyId=referalData.get(j).survey_id;
                                        ReferalCode=referalData.get(j).referedSurveyGeneratedCode;
                                }

                            }

                            Utility.hideDialog(mActivity);
                            ReferSurveyAFriendDialog(surveyId,ReferalCode,referPoint,surveyPoint);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                        Utility.hideDialog(mActivity);

                    }
                }

                @Override
                public void onFailure(Call<GeneratedSurveyReferCode_Response> call, Throwable t) {
                    Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                    Utility.hideDialog(mActivity);
                }
            });

        }else{
            Utility.showAlertDialog(mActivity, mActivity.getString(R.string.alert), mActivity.getString(R.string.no_internet_connection));
        }
    }
    private void ReferSurveyAFriendDialog(String surveyId,String referCode,String referPoint,String surveyPoints){
        Dialog referDialog= new Dialog(mActivity);
        referDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        referDialog.setContentView(R.layout.dialog_refersurvey);
        String referStr="You get "+referPoint+" Points,Friend gets "+surveyPoints+" Points";



        String Applink="https://doyoursurvey.com/#/register/attemp-survey-dyn/"+surveyId+"?surveyReferCode="+referCode;
        Log.e("ReferalString",Applink);

        CardView copyCardView=referDialog.findViewById(R.id.cv_refersurvey_copy);
        TextView referText=referDialog.findViewById(R.id.tv_Dialog_ReferSurvey_PointsText);
        referText.setText(referStr);
        ImageView facebookImage=referDialog.findViewById(R.id.iv_refersurvey_facebook);
        ImageView twitterImage=referDialog.findViewById(R.id.iv_refersurvey_twitter);
        ImageView whatsappImage=referDialog.findViewById(R.id.iv_refersurvey_whatsapp);
        ImageView mailImage=referDialog.findViewById(R.id.iv_refersurvey_mail);

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
    private void setClipboard(Context context, String text) {

            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);

        Toast.makeText(mActivity, "Refer Code Copied", Toast.LENGTH_SHORT).show();

    }



}
