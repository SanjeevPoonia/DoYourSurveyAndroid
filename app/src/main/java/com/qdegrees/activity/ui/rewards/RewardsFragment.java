package com.qdegrees.activity.ui.rewards;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.qdegrees.doyoursurvey.R;
import com.qdegrees.local_storage.SharedPreferencesRepository;
import com.qdegrees.network.ApiService;
import com.qdegrees.network.RetrofitAPIClient;
import com.qdegrees.network.request.DashboardPostData;
import com.qdegrees.network.request.ReedemPointsRequest;
import com.qdegrees.network.request.RewardPostData;
import com.qdegrees.network.response.DashboardProfileResponse;
import com.qdegrees.network.response.PointRedeemedHistory_Response;
import com.qdegrees.network.response.ReedemPointResponse;
import com.qdegrees.utils.Utility;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RewardsFragment extends Fragment {
    Activity mActivity;
    protected ApiService apiService;
    String sUserEmail="",sMobileNumber="";
    String PointEarned="0",PointReedem="0",PointAvailable="0",CompletedSurvey="0";
    TextView tvPointEarned,tvPointRedeem,tvPointAvailable,tvCompletedSurvey,NoDataText,tvAvailablePointWorth,tvRedeemPointWorth;
    RecyclerView rewardRecyclerView;
    List<RewardHistoryListItem> RewardHistoryList= new ArrayList<>();
    MyAssignedListAdapter myAssignedListAdapter;
    CardView ReedemPoints;

    TextView tvLastUpdatedOn;



    /******************************************************/
    int totalPage=0;
    int currentPage=1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rewards, container, false);
        mActivity=getActivity();
        sUserEmail= SharedPreferencesRepository.getDataManagerInstance().getSessionEmail();
        sMobileNumber=SharedPreferencesRepository.getDataManagerInstance().getSessionMobile();
        apiService = RetrofitAPIClient.getRetrofitClient();
        tvPointEarned=root.findViewById(R.id.tvFragmentRewardPointEarned);
        tvPointRedeem=root.findViewById(R.id.tvFragmentRewardPointRedeemed);
        tvPointAvailable=root.findViewById(R.id.tvFragmentRewardPointAvailable);
        tvCompletedSurvey=root.findViewById(R.id.tvFragmentRewardCompletedSurvey);
        tvAvailablePointWorth=root.findViewById(R.id.tvFragmentRewardPointAvailableWorth);
        tvRedeemPointWorth=root.findViewById(R.id.tvRewardFragRedeemPointWorth);
        tvLastUpdatedOn=root.findViewById(R.id.tv_rewardFrag_lastUpdated);

        NoDataText=root.findViewById(R.id.tvFragmentRewardNoDataText);
        ReedemPoints=root.findViewById(R.id.cv_Fragment_Reward_ReedemPoints);
        rewardRecyclerView=root.findViewById(R.id.rvFragmentReward);




        LinearLayoutManager llm= new LinearLayoutManager(mActivity);
        rewardRecyclerView.setLayoutManager(llm);
        ReedemPoints.setVisibility(View.GONE);
        ReedemPoints.setOnClickListener(v->{
            int points=Integer.parseInt(PointAvailable);

            if(points>=1000){
                    ReedemPointDialog();
            }else{
                Utility.showAlertDialog(mActivity,"Reedem Points","You need Atleast 1000 points to Reedem");
            }

        });
        getProfileDashboard();
        return root;
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
                            List<DashboardProfileResponse.dataUser> userData = dashboardProfileResponse.data.dataUsers;
                            for (int i = 0; i < userData.size(); i++) {
                                PointEarned = userData.get(i).pointEarned;
                                PointReedem = userData.get(i).pointRedeemed;
                                PointAvailable = userData.get(i).pointAvailable;
                                CompletedSurvey = userData.get(i).completedSurvey;

                            }
                            tvPointEarned.setText(PointEarned);
                            //tvPointRedeem.setText(PointReedem);
                           // tvPointAvailable.setText(PointAvailable);
                            tvCompletedSurvey.setText(CompletedSurvey);

                            Log.e("Points Available",PointAvailable);
                            Log.e("points Earned", PointReedem);
                            if(TextUtils.isEmpty(PointAvailable)||PointAvailable=="0"){
                                tvPointAvailable.setText("0");
                                tvAvailablePointWorth.setText("₹0");
                            }else{
                                tvPointAvailable.setText(PointAvailable);
                                double pointsAvailable = Integer.parseInt(PointAvailable);
                                double pointworth=pointsAvailable/250;
                                double po= (double)Math.round(pointworth*100) ;


                                tvAvailablePointWorth.setText("₹ "+po);
                            }
                            if(TextUtils.isEmpty(PointReedem) || PointReedem == "0"){
                                tvPointRedeem.setText("0");
                                tvRedeemPointWorth.setText("₹0");
                            }else{
                                tvPointRedeem.setText(PointReedem);
                                double pointsAvailable = Integer.parseInt(PointReedem);
                                double pointworth=pointsAvailable/250;
                                double po=(double)Math.round(pointworth*100);

                                tvRedeemPointWorth.setText("₹ "+pointworth);
                            }
                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy, EEE, hh:mm a", Locale.getDefault());
                            String currentDateandTime = sdf.format(new Date());


                            tvLastUpdatedOn.setText(currentDateandTime);

                            Utility.hideDialog(mActivity);
                            getReedemHisotry();

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
                public void onFailure(Call<DashboardProfileResponse> call, Throwable t) {
                    Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                    Utility.hideDialog(mActivity);

                }
            });

        }else{
            Utility.showAlertDialog(mActivity, mActivity.getString(R.string.alert), mActivity.getString(R.string.no_internet_connection));
        }
    }
    /***********ReedemHistory**************/
    private void getReedemHisotry(){
        if(Utility.isNetworkAvailable(mActivity)){
            Utility.showDialog(mActivity);
            apiService.getRedeemedHistory(new RewardPostData(sUserEmail,currentPage)).enqueue(new Callback<PointRedeemedHistory_Response>() {
                @Override
                public void onResponse(Call<PointRedeemedHistory_Response> call, Response<PointRedeemedHistory_Response> response) {
                    try{
                    PointRedeemedHistory_Response pointRedeemedHistory_response=response.body();
                    int code=pointRedeemedHistory_response.status;
                    if(code==200){
                        RewardHistoryList.clear();
                        totalPage=pointRedeemedHistory_response.data.totalPage;



                        List<PointRedeemedHistory_Response.RedeemPoints> list=pointRedeemedHistory_response.data.doc.redeemPointsData;
                        for(int i=0;i<list.size();i++){
                            String id=list.get(i)._id;
                            String redeemPoint=list.get(i).RedeemPoint;
                            String voucherFor=list.get(i).voucherFor;
                            String voucherCode=list.get(i).voucherCode;
                            String Status=list.get(i).status;
                            String date=list.get(i).date;
                            RewardHistoryList.add(new RewardHistoryListItem(id,redeemPoint,voucherFor,voucherCode,Status,date));
                        }
                        if(RewardHistoryList.size()>0){
                            NoDataText.setVisibility(View.GONE);
                            rewardRecyclerView.setVisibility(View.VISIBLE);
                        }else{
                            NoDataText.setVisibility(View.VISIBLE);
                            rewardRecyclerView.setVisibility(View.GONE);
                        }
                        myAssignedListAdapter= new MyAssignedListAdapter(mActivity,RewardHistoryList);
                        rewardRecyclerView.setAdapter(myAssignedListAdapter);
                        myAssignedListAdapter.notifyDataSetChanged();




                    }else{
                        Utility.showAlertDialog(mActivity,"Error!!","Something went wrong!!");

                    }
                        Utility.hideDialog(mActivity);
                    }catch(Exception e){
                        e.printStackTrace();
                        Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                        Utility.hideDialog(mActivity);
                    }

                }

                @Override
                public void onFailure(Call<PointRedeemedHistory_Response> call, Throwable t) {
                    Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                    Utility.hideDialog(mActivity);
                }
            });

        }else{
            Utility.showAlertDialog(mActivity, mActivity.getString(R.string.alert), mActivity.getString(R.string.no_internet_connection));
        }
    }
    class AssignedListHolder extends RecyclerView.ViewHolder{

        CardView mainView;
        TextView RedeemPoint,Vouchercode,Status,Date;
        ImageView VoucherImageView;
        public AssignedListHolder(View view){
            super(view);
            mainView=view.findViewById(R.id.cv_fragment_Reward_listitem);
            RedeemPoint=view.findViewById(R.id.tv_fragmentReward_ListItem_Points);
            Vouchercode=view.findViewById(R.id.tv_fragmentRewardListItem_VoucherCode);
            Status=view.findViewById(R.id.tv_fragmentReward_ListItem_Status);
            Date=view.findViewById(R.id.tv_fragmentRewardListItem_Date);
            VoucherImageView=view.findViewById(R.id.iv_fragmentreward_voucherImage);
        }

    }
    class MyAssignedListAdapter extends RecyclerView.Adapter<AssignedListHolder>{

        List<RewardHistoryListItem>itemList;
        Context mContext;
        public MyAssignedListAdapter(Context c, List<RewardHistoryListItem> list){
            this.itemList=list;
            this.mContext=c;
        }
        @NonNull
        @Override
        public AssignedListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_reward_listitem,parent,false);
            AssignedListHolder auditListHolder= new AssignedListHolder(itemView);
            return auditListHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull AssignedListHolder holder, int position) {

            String id=itemList.get(position).getReedemId();
            String DateStr=itemList.get(position).getDateStr();
            String Point=itemList.get(position).getReedemPoint();
            String Voucher=itemList.get(position).getVoucherFor();
            String VoucherCode=itemList.get(position).getVoucharCode();
            String Status=itemList.get(position).getVoucherStatus();

            String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
            String outputPattern = "dd-MMM-yyyy HH:mm:ss a";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern,Locale.getDefault());
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,Locale.getDefault());
            inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = null;
            String str = null;
            try {
                date = inputFormat.parse(DateStr);

                str = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

           holder.Date.setText(str);
           holder.RedeemPoint.setText(Point);


           if(Voucher.matches("paytm")){
               holder.VoucherImageView.setImageResource(R.drawable.paytm_logo);
           } else if(Voucher.matches("Amazon")){
               holder.VoucherImageView.setImageResource(R.drawable.amazon_logo);
           }else if(Voucher.matches("Flipkart")){
               holder.VoucherImageView.setImageResource(R.drawable.flipkart_logo);
           }else{
               holder.VoucherImageView.setVisibility(View.GONE);
           }
           if(VoucherCode.matches("Pending")){
               holder.Vouchercode.setText("Coupons will generate within 7 days");
           }else{
               holder.Vouchercode.setText(VoucherCode);
           }
           if(Status.toLowerCase().matches("pending")){
               holder.Status.setTextColor(mActivity.getResources().getColor(R.color.yellow));
           }else if(Status.toLowerCase().matches("decline")){
               holder.Status.setTextColor(mActivity.getResources().getColor(R.color.Google_Color));
           }else{
               holder.Status.setTextColor(mActivity.getResources().getColor(R.color.green));
           }
            holder.Status.setText(Status);
           if(position==itemList.size()-1){
               if(currentPage<totalPage){
                   currentPage=currentPage+1;
                   getReedemHisotryPage();
               }
           }


        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

    }

    /*******************************************************/
    private void ReedemPointDialog(){
        Dialog dialog= new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_redeempoints);

        TextView availablePointsText=dialog.findViewById(R.id.Activity_ReedemPoints_AvailablePointsText);
        TextView emailtext=dialog.findViewById(R.id.Activity_ReedemPoints_EmailText);
        ImageView backImage=dialog.findViewById(R.id.Activity_ReedemPoints_BackImage);
        EditText pointEdit=dialog.findViewById(R.id.Activity_ReedemPoints_ReedemPointEditText);
        Spinner voucherSpinner=dialog.findViewById(R.id.Activity_ReedemPoints_Voucher_Spinner);
        EditText PaytmMobileEdit=dialog.findViewById(R.id.Activity_ReedemPoints_PaytmMobileNumberEditText);
        LinearLayout paytmLayout=dialog.findViewById(R.id.Activity_ReedemPoints_PaytmNumber_Layout);
        CardView SubmitCV=dialog.findViewById(R.id.cv_Actvity_Submit);

        availablePointsText.setText("Available Points:"+PointAvailable);
        emailtext.setText("Email: "+sUserEmail);
        backImage.setOnClickListener(v->{
            dialog.dismiss();
        });
        List<String> spinnerList= new ArrayList<>();
        spinnerList.add("Select Voucher Type");
        spinnerList.add("Amazon");
        spinnerList.add("Flipkart");
        spinnerList.add("Paytm");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (mActivity, android.R.layout.simple_spinner_item,
                        spinnerList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        voucherSpinner.setAdapter(spinnerArrayAdapter);

        final String[] vc = new String[1];
        voucherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item=spinnerList.get(position);
                if(!item.matches("Select Voucher Type")){
                    if(item.matches("Paytm")){
                        vc[0] ="paytm";
                        paytmLayout.setVisibility(View.VISIBLE);
                        PaytmMobileEdit.setText(sMobileNumber);
                    }else{
                        vc[0] =spinnerList.get(position);
                        paytmLayout.setVisibility(View.GONE);
                        PaytmMobileEdit.setText("");
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        SubmitCV.setOnClickListener(v->{
            String VoucherType = "";
            String VoucherCode="Pending";
            String point=pointEdit.getText().toString();

            if(vc.length>0){
                VoucherType=vc[0];
            }
            if(!TextUtils.isEmpty(point)){
                double i=Double.parseDouble(point);
                double av=Double.parseDouble(PointAvailable);
                if(i<=av){
                    if(i>=1000){
                        if(!TextUtils.isEmpty(VoucherType)){
                            if(VoucherType.matches("paytm")){
                                String PaytmNumber=PaytmMobileEdit.getText().toString();
                                if(!TextUtils.isEmpty(PaytmNumber)&&PaytmNumber.length()==10){
                                    VoucherCode="paytm";
                                    dialog.dismiss();
                                    doReedemPoints(point,VoucherType,PaytmNumber,VoucherCode);
                                }else{
                                    PaytmMobileEdit.setError("Please Enter a Valid Paytm Mobile Number!!");
                                    PaytmMobileEdit.requestFocus();
                                }

                            }else{
                                dialog.dismiss();
                                doReedemPoints(point,VoucherType,"",VoucherCode);
                            }

                        }else{
                            Toast.makeText(mActivity, "Please Select Voucher Type", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        pointEdit.setError("Minimum 1000 Points Required For Redeem");
                        pointEdit.requestFocus();
                    }
                }else{
                    pointEdit.setError("Points cannot be greater than Available Points");
                    pointEdit.requestFocus();
                }
            }else{
                pointEdit.setError("Please enter valid points");
                pointEdit.requestFocus();
            }
        });
        dialog.show();
        try {
            Window window = dialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    private void doReedemPoints(String pointReedem,String voucherType,String PaytmNumber,String voucherCode){
        if(Utility.isNetworkAvailable(mActivity)){
            Utility.showDialog(mActivity);
            apiService.DoReedemPoints(new ReedemPointsRequest(sUserEmail,pointReedem,PaytmNumber,voucherType,voucherCode,"Pending")).enqueue(new Callback<ReedemPointResponse>() {
                @Override
                public void onResponse(Call<ReedemPointResponse> call, Response<ReedemPointResponse> response) {
                    try {
                        ReedemPointResponse pointRedeemedHistory_response = response.body();
                        int code = pointRedeemedHistory_response.status;
                        if (code == 200) {

                            Toast.makeText(mActivity, "Points Reedemed Successfully.", Toast.LENGTH_SHORT).show();
                            Utility.hideDialog(mActivity);
                            getProfileDashboard();


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
                public void onFailure(Call<ReedemPointResponse> call, Throwable t) {
                    Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                    Utility.hideDialog(mActivity);
                }
            });

        }else{
            Utility.showAlertDialog(mActivity, mActivity.getString(R.string.alert), mActivity.getString(R.string.no_internet_connection));
        }
    }


    /*******************************************************/
    private void getReedemHisotryPage(){
        if(Utility.isNetworkAvailable(mActivity)){
            Utility.showDialog(mActivity);
            apiService.getRedeemedHistory(new RewardPostData(sUserEmail,currentPage)).enqueue(new Callback<PointRedeemedHistory_Response>() {
                @Override
                public void onResponse(Call<PointRedeemedHistory_Response> call, Response<PointRedeemedHistory_Response> response) {
                    try{
                        PointRedeemedHistory_Response pointRedeemedHistory_response=response.body();
                        int code=pointRedeemedHistory_response.status;
                        if(code==200){

                            totalPage=pointRedeemedHistory_response.data.totalPage;



                            List<PointRedeemedHistory_Response.RedeemPoints> list=pointRedeemedHistory_response.data.doc.redeemPointsData;
                            for(int i=0;i<list.size();i++){
                                String id=list.get(i)._id;
                                String redeemPoint=list.get(i).RedeemPoint;
                                String voucherFor=list.get(i).voucherFor;
                                String voucherCode=list.get(i).voucherCode;
                                String Status=list.get(i).status;
                                String date=list.get(i).date;
                                RewardHistoryList.add(new RewardHistoryListItem(id,redeemPoint,voucherFor,voucherCode,Status,date));
                            }
                            if(RewardHistoryList.size()>0){
                                NoDataText.setVisibility(View.GONE);
                                rewardRecyclerView.setVisibility(View.VISIBLE);
                            }else{
                                NoDataText.setVisibility(View.VISIBLE);
                                rewardRecyclerView.setVisibility(View.GONE);
                            }
                            if(myAssignedListAdapter==null){
                                myAssignedListAdapter= new MyAssignedListAdapter(mActivity,RewardHistoryList);
                                rewardRecyclerView.setAdapter(myAssignedListAdapter);
                            }
                            myAssignedListAdapter.notifyDataSetChanged();
                        }else{
                            Utility.showAlertDialog(mActivity,"Error!!","Something went wrong!!");

                        }
                        Utility.hideDialog(mActivity);
                    }catch(Exception e){
                        e.printStackTrace();
                        Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                        Utility.hideDialog(mActivity);
                    }

                }

                @Override
                public void onFailure(Call<PointRedeemedHistory_Response> call, Throwable t) {
                    Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                    Utility.hideDialog(mActivity);
                }
            });

        }else{
            Utility.showAlertDialog(mActivity, mActivity.getString(R.string.alert), mActivity.getString(R.string.no_internet_connection));
        }
    }
}