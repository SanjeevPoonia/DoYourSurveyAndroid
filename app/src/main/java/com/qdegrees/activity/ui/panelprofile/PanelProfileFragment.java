package com.qdegrees.activity.ui.panelprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qdegrees.activity.ui.survey.ProfileSurvey_AttemptActivity;
import com.qdegrees.doyoursurvey.R;
import com.qdegrees.local_storage.SharedPreferencesRepository;
import com.qdegrees.network.ApiService;
import com.qdegrees.network.RetrofitAPIClient;
import com.qdegrees.network.request.DashboardPostData;
import com.qdegrees.network.response.DashboardProfileResponse;
import com.qdegrees.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PanelProfileFragment extends Fragment {
    Activity mActivity;
    protected ApiService apiService;
    String sUserEmail="";
    String PointEarned="0",PointReedem="0",PointAvailable="0",CompletedSurvey="0";
    TextView tvPointEarned,tvPointRedeem,tvPointAvailable,tvCompletedSurvey;
    int ProfilePercentage=0;
    ProgressBar progressBar;
    TextView tvPercentage,tvComplete;
    RecyclerView profileRecyclerView;
    List<ProfileSurveyListItem> ProfileSurveyList= new ArrayList<>();
    MyAssignedListAdapter myAssignedListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_panelprofile, container, false);
        mActivity=getActivity();
        sUserEmail= SharedPreferencesRepository.getDataManagerInstance().getSessionEmail();
        apiService = RetrofitAPIClient.getRetrofitClient();
        tvPointEarned=root.findViewById(R.id.tvFragmentProfilePointEarned);
        tvPointRedeem=root.findViewById(R.id.tvFragmentProfilePointRedeemed);
        tvPointAvailable=root.findViewById(R.id.tvFragmentProfilePointAvailable);
        tvCompletedSurvey=root.findViewById(R.id.tvFragmentProfileCompletedSurvey);
        progressBar=root.findViewById(R.id.pbFragmentProfileProfile);
        tvPercentage=root.findViewById(R.id.tvFragmentProfilePercentage);
        tvComplete=root.findViewById(R.id.tvFragmentProfileComplete);
        profileRecyclerView=root.findViewById(R.id.rvFragmentProfile);
        LinearLayoutManager llm= new LinearLayoutManager(mActivity);
        profileRecyclerView.setLayoutManager(llm);
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
                   try{
                    DashboardProfileResponse dashboardProfileResponse=response.body();
                    int code=dashboardProfileResponse.status;
                    if(code==200){
                        ProfileSurveyList.clear();
                        List<DashboardProfileResponse.profileTaskStatus> proflei=dashboardProfileResponse.data.ProfileTask;
                        List<DashboardProfileResponse.dataUser> userData=dashboardProfileResponse.data.dataUsers;
                        for(int i=0;i<userData.size();i++){
                            PointEarned=userData.get(i).pointEarned;
                            PointReedem=userData.get(i).pointRedeemed;
                            PointAvailable=userData.get(i).pointAvailable;
                            CompletedSurvey=userData.get(i).completedSurvey;
                            ProfilePercentage=userData.get(i).profilePercent;
                        }

                        for(int i=0;i<proflei.size();i++){
                            boolean pr=proflei.get(i).status;
                            String id=proflei.get(i).id;
                            String taskName=proflei.get(i).taskName;
                            if(!pr){
                                ProfileSurveyList.add(new ProfileSurveyListItem(id,taskName,pr));
                            }
                        }


                        tvPointEarned.setText(PointEarned);
                        tvPointRedeem.setText(PointReedem);
                        tvPointAvailable.setText(PointAvailable);
                        tvCompletedSurvey.setText(CompletedSurvey);

                        progressBar.setProgress(ProfilePercentage);
                        tvPercentage.setText(ProfilePercentage+"%");
                        if(ProfilePercentage==100){
                            tvComplete.setText("Thank you !! Your Profile Completed");
                        }else{
                            tvComplete.setText("Please complete your profile");
                        }
                        if(ProfileSurveyList.size()>0){
                            profileRecyclerView.setVisibility(View.VISIBLE);
                        }else{
                            profileRecyclerView.setVisibility(View.GONE);
                        }
                        myAssignedListAdapter= new MyAssignedListAdapter(mActivity,ProfileSurveyList);
                        profileRecyclerView.setAdapter(myAssignedListAdapter);
                        myAssignedListAdapter.notifyDataSetChanged();

                        Utility.hideDialog(mActivity);


                    }else{
                        Utility.showAlertDialog(mActivity,"Error!!","Something went wrong!!");
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

    class AssignedListHolder extends RecyclerView.ViewHolder{

        CardView mainView;
        TextView SurveyNameText;
        public AssignedListHolder(View view){
            super(view);
            mainView=view.findViewById(R.id.cv_fragment_profile_listitem);
            SurveyNameText=view.findViewById(R.id.tv_fragmentProfileListItem_SurveyName);
        }

    }
    class MyAssignedListAdapter extends RecyclerView.Adapter<AssignedListHolder>{

        List<ProfileSurveyListItem>itemList;
        Context mContext;
        public MyAssignedListAdapter(Context c, List<ProfileSurveyListItem> list){
            this.itemList=list;
            this.mContext=c;
        }
        @NonNull
        @Override
        public AssignedListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_profile_survey_listitem,parent,false);
            AssignedListHolder auditListHolder= new AssignedListHolder(itemView);
            return auditListHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull AssignedListHolder holder, int position) {

            String surveyId=itemList.get(position).getSurveyId();
            String surveyName=itemList.get(position).getTaskName();
            holder.SurveyNameText.setText(surveyName.toUpperCase());

            holder.mainView.setOnClickListener(v->{
                Intent intent= new Intent(mActivity, ProfileSurvey_AttemptActivity.class);
                intent.putExtra("survey_type",surveyName);
                intent.putExtra("percentage",ProfilePercentage);
                startActivity(intent);
            });



        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

    }
}