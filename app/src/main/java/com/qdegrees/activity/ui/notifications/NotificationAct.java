package com.qdegrees.activity.ui.notifications;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.qdegrees.doyoursurvey.R;
import com.qdegrees.local_storage.SharedPreferencesRepository;
import com.qdegrees.network.ApiService;
import com.qdegrees.network.RetrofitAPIClient;
import com.qdegrees.network.request.NotificationRequest;
import com.qdegrees.network.response.NotificationResponse;
import com.qdegrees.utils.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAct extends AppCompatActivity {
    ImageView backImage;
    RecyclerView rvNotifications;
    List<NotificationsListItem> notificationList= new ArrayList<>();
    String tokenStr="", userIdStr="", emailStr="";
    Activity mActivity;
    protected ApiService apiService;
    myNotificationAdapter myNotificationAdapter;

    String CurrentDate="";

    AppCompatTextView noText;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_notification);
        mActivity= this;
        apiService = RetrofitAPIClient.getRetrofitClient();
        emailStr= SharedPreferencesRepository.getDataManagerInstance().getSessionEmail();
        userIdStr= SharedPreferencesRepository.getDataManagerInstance().getSessionUserId();
        tokenStr= SharedPreferencesRepository.getDataManagerInstance().getSessionRememberToken();

        backImage=findViewById(R.id.iv_Back_Notifications);
        rvNotifications=findViewById(R.id.rv_notifications);
        noText=findViewById(R.id.tv_notification_notext);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
       CurrentDate = df.format(c);

        LinearLayoutManager ll= new LinearLayoutManager(this);
        rvNotifications.setLayoutManager(ll);

        getNotificationList();

    }


    private void getNotificationList(){
        if(Utility.isNetworkAvailable(mActivity)){
            Utility.showDialog(mActivity);
            apiService.getNotificationList(tokenStr,new NotificationRequest(userIdStr,20,1)).enqueue(new Callback<NotificationResponse>() {
                @Override
                public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                    try{
                        NotificationResponse notificationResponse= response.body();
                        int code=notificationResponse.status;
                        String message=notificationResponse.message;
                        if(code==200){
                            notificationList.clear();
                            List<NotificationResponse.dataNotifications> list = notificationResponse.data.dataUsers;
                            for(int i=0;i<list.size();i++){
                                String uId=list.get(i).user_id;
                                String notiId= list.get(i)._id;
                                String title=list.get(i).title;
                                String msg=list.get(i).message;
                                String ty=list.get(i).type;
                                String created=list.get(i).created_at;
                                String updated=list.get(i).updated_at;
                                int readStatus=list.get(i).read_status;
                                int ss=list.get(i).status;
                                notificationList.add(new NotificationsListItem(uId,notiId,title,msg,ty,created,updated,readStatus,ss));
                            }
                            Utility.hideDialog(mActivity);
                            setView();

                        }else{
                            Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                            Utility.hideDialog(mActivity);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                        Utility.hideDialog(mActivity);
                    }
                }

                @Override
                public void onFailure(Call<NotificationResponse> call, Throwable t) {
                    Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                    Utility.hideDialog(mActivity);
                }
            });

        }else{
            Utility.showAlertDialog(mActivity, mActivity.getString(R.string.alert), mActivity.getString(R.string.no_internet_connection));
        }
     }


     private void setView(){
        myNotificationAdapter= new myNotificationAdapter(mActivity,notificationList);
        rvNotifications.setAdapter(myNotificationAdapter);
        myNotificationAdapter.notifyDataSetChanged();

        if(notificationList.size()>0){
            noText.setVisibility(View.GONE);
            rvNotifications.setVisibility(View.VISIBLE);
        }else{
            noText.setVisibility(View.VISIBLE);
            rvNotifications.setVisibility(View.GONE);
        }


     }

    class NotificaitonListHolder extends RecyclerView.ViewHolder{

        CardView MainView;
        AppCompatTextView tvName,tvTime,tvMessage;
        public NotificaitonListHolder(View view){
            super(view);
            MainView=view.findViewById(R.id.cvNotification);
            tvName=view.findViewById(R.id.tvName);
            tvTime=view.findViewById(R.id.tvTime);
            tvMessage=view.findViewById(R.id.tvMessage);
        }

    }
    class myNotificationAdapter extends RecyclerView.Adapter<NotificaitonListHolder>{
         List<NotificationsListItem>itemList;
         Context mContext;

         public myNotificationAdapter(Context c,List<NotificationsListItem> list){
             mContext=c;
             itemList=list;
         }
         @NonNull
         @Override
         public NotificaitonListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
             View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_single_lay,parent,false);
             NotificaitonListHolder notiHolder= new NotificaitonListHolder(itemView);
             return notiHolder;
         }

         @Override
         public void onBindViewHolder(@NonNull NotificaitonListHolder holder, int position) {
             String tit= itemList.get(position).getTitle();
             String msg = itemList.get(position).getMessage();
             String time = itemList.get(position).getCreatedAt();
             int readStatus = itemList.get(position).getReadStatus();
             holder.tvName.setText(tit);
             holder.tvMessage.setText(msg);
             if(!TextUtils.isEmpty(time)){
                 String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
                 String outputPattern = "dd MMM, yyyy";
                 String outputPatterntime = "hh:mm a";
                 SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                 inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                 SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
                 outputFormat.setTimeZone(TimeZone.getDefault());
                 SimpleDateFormat outputFormattime = new SimpleDateFormat(outputPatterntime);
                 outputFormattime.setTimeZone(TimeZone.getDefault());
                 Date date = null;
                 String strDate = null;
                 String strtime = null;
                 try {
                     date = inputFormat.parse(time);
                     strDate = outputFormat.format(date);
                     strtime = outputFormattime.format(date);

                     if(strDate.matches(CurrentDate)){
                         holder.tvTime.setText(strtime);
                     }else{
                         holder.tvTime.setText(strDate+" | "+strtime);
                     }


                 } catch (ParseException e) {
                     e.printStackTrace();
                 }
             }

             if(readStatus==0){
                 holder.MainView.setCardBackgroundColor(Color.parseColor("#FFF4ED"));
             }else{
                 holder.MainView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
             }

         }

         @Override
         public int getItemCount() {
             return itemList.size();
         }
     }


}
