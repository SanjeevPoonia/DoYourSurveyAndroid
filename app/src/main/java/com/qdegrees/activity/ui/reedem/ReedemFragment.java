package com.qdegrees.activity.ui.reedem;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.qdegrees.doyoursurvey.R;
import com.qdegrees.local_storage.SharedPreferencesRepository;
import com.qdegrees.network.ApiService;
import com.qdegrees.network.RetrofitAPIClient;
import com.qdegrees.network.request.DashboardPostData;
import com.qdegrees.network.request.ReedemPointsRequest;
import com.qdegrees.network.response.DashboardProfileResponse;
import com.qdegrees.network.response.ReedemPointResponse;
import com.qdegrees.utils.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReedemFragment extends Fragment {
    Activity mActivity;
    protected ApiService apiService;
    String sUserEmail="",sMobileNumber="";
    String PointEarned="0",PointReedem="0",PointAvailable="0",CompletedSurvey="0";
    TextView tvPointsAvailable,tvAvailablePointsWorth,tvSelectedPointWorth;
    TextInputEditText etPaytmNumber,etRedeemPoints;
    CardView cvRedeem;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_reedem, container, false);
        mActivity=getActivity();
        apiService = RetrofitAPIClient.getRetrofitClient();
        sUserEmail= SharedPreferencesRepository.getDataManagerInstance().getSessionEmail();
        sMobileNumber=SharedPreferencesRepository.getDataManagerInstance().getSessionMobile();

        tvPointsAvailable=root.findViewById(R.id.tvFragmentReedemPointAvailable);
        tvAvailablePointsWorth= root.findViewById(R.id.tvFragmentReedemPointWorth);
        tvSelectedPointWorth = root.findViewById(R.id.tv_Redeem_pointsWorthselected);
        cvRedeem=root.findViewById(R.id.cv_Redeem_RedeemPoint);

        etPaytmNumber = root . findViewById(R.id.et_Redeem_PaytmNumber);
        etRedeemPoints = root . findViewById(R.id.et_Redeem_Points);

        etPaytmNumber .setText(sMobileNumber);
        getProfileDashboard();


        etRedeemPoints.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String po=s.toString();
                if(!TextUtils.isEmpty(po)){
                    double i=Double.parseDouble(po);

                    double pointworth=i/250;
                    double pos= (double) Math.round(pointworth*100);
                    tvSelectedPointWorth.setText("₹ "+pos);
                }else{
                    tvSelectedPointWorth.setText("₹ "+0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cvRedeem.setOnClickListener(v->{
            int points=Integer.parseInt(PointAvailable);
            if(points>=1000){
                String point=etRedeemPoints.getText().toString();
                String VoucherType="paytm";
                if(!TextUtils.isEmpty(point)){

                    double i=Double.parseDouble(point);
                    double av=Double.parseDouble(PointAvailable);
                    double pointworth=i/250;
                    double po= (double) Math.round(pointworth*100);
                    tvSelectedPointWorth.setText("₹ "+po);



                    if(i<=av){
                        if(i>=1000){
                            String PaytmNumber=etPaytmNumber.getText().toString();
                            if(!TextUtils.isEmpty(PaytmNumber)&&PaytmNumber.length()==10){
                                doReedemPoints(point,VoucherType,PaytmNumber,"Pending");
                            }else{
                                Toast.makeText(mActivity, "Please Enter a Valid Paytm Mobile Number!!", Toast.LENGTH_SHORT).show();
                                Utility.showEditTextError(etPaytmNumber,"Please Enter a Valid Paytm Mobile Number!!");
                            }

                        }else{
                            Toast.makeText(mActivity, "  Minimum 1000 Points Required For Redeem", Toast.LENGTH_SHORT).show();
                            Utility.showEditTextError(etRedeemPoints,"  Minimum 1000 Points Required For Redeem");
                        }

                    }else{
                        Toast.makeText(mActivity, "Points cannot be greater than Available Points", Toast.LENGTH_SHORT).show();
                        Utility.showEditTextError(etRedeemPoints,"Points cannot be greater than Available Points");
                    }


                }else{
                    Toast.makeText(mActivity, "Please enter valid points", Toast.LENGTH_SHORT).show();
                    Utility.showEditTextError(etRedeemPoints,"Please enter valid points");
                }

            }else{
                Utility.showAlertDialog(mActivity,"Reedem Points","You need Atleast 1000 points to Reedem");
            }
        });
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



                            if(TextUtils.isEmpty(PointAvailable)||PointAvailable=="0"){

                                tvPointsAvailable.setText("0");
                                tvAvailablePointsWorth.setText("₹0");
                            }else{
                                tvPointsAvailable.setText(PointAvailable);

                                double pointsAvailable = Integer.parseInt(PointAvailable);
                                double pointworth=pointsAvailable/250;
                                double po= (double) Math.round(pointworth*100);


                                tvAvailablePointsWorth.setText("₹ "+po);
                            }

                            Utility.hideDialog(mActivity);



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
}
