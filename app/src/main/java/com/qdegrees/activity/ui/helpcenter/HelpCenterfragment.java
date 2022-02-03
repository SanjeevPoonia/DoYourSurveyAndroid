package com.qdegrees.activity.ui.helpcenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qdegrees.activity.ui.panelprofile.PanelProfileFragment;
import com.qdegrees.activity.ui.panelprofile.ProfileSurveyListItem;
import com.qdegrees.doyoursurvey.R;

import java.util.ArrayList;
import java.util.List;

public class HelpCenterfragment extends Fragment {

    List<HelpCenterListItem> helpList= new ArrayList<>();
    Activity mActivity;
    RecyclerView helpRecyclerView;
    MyAssignedListAdapter myAssignedListAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_helpcenter, container, false);
        mActivity=getActivity();
        helpRecyclerView=root.findViewById(R.id.rvFragmentHelpCenter);
        LinearLayoutManager llm= new LinearLayoutManager(mActivity);
        helpRecyclerView.setLayoutManager(llm);
        helpList.add(new HelpCenterListItem("About DoYourSurvey","DoYourSurvey Services works in transforming the customer experience. We provide process consulting, sentiment analysis, quality assurance audits, service excellence, market research, mystery shopping, and much more to enhance customer experience enabled with in-house developed solutions. Working with both large and small organizations from diverse industries pertaining to automobile, telecom, banking, fashion, retail, consumer electronics, fitness, e-commerce, etc., this service sector organization is continuously expanding its footprints"));
        helpList.add(new HelpCenterListItem("How do I become a member of DoYourSurvey","It's very easy. Simply fill in your details on our sign-up page. We'll send you a mail after Successfully registration so you are able to confirm your membership."));
        helpList.add(new HelpCenterListItem("Can I join DoYourSurvey if I live outside India","No, unfortunately membership of this online panel is open only to residents of India"));
        helpList.add(new HelpCenterListItem("Will it cost me anything to join DoYourSurvey","There are no costs associated with joining the panel and you can leave at any time. However, you will need an e-mail address to receive invitations to participate in surveys and you need to be able to access the internet to complete our online surveys"));
        helpList.add(new HelpCenterListItem("What happens after I join DoYourSurvey","We will send you an e-mail welcoming you to DoYourSurvey and you will begin receiving e-mail invitations to take surveys. The e-mails will contain all the information you need to take part. Once you complete the survey, you'll have the opportunity to earn points"));
        helpList.add(new HelpCenterListItem("How often will I be asked to take surveys","We send out survey invitations on a regular basis, but cannot say who or when any member will receive invitations. This is because each survey is customized and based around the research goals for each individual client. Depending on your profile, we may ask you to complete surveys more or less often. For this reason, we ask you to keep your profile as up to date as possible"));
        helpList.add(new HelpCenterListItem("Why are surveys conducted","DoYourSurvey conducts surveys because businesses, governments, public bodies, and similar organizations are interested in the views and attitudes of the people who use their products and services. The more they know about what customers and citizens think, the easier it is for them to improve and adapt what they supply"));
        helpList.add(new HelpCenterListItem("How long does an average survey take to complete","The number of questions varies from one survey to another. We try to keep surveys as short as possible, although we have to make sure the results are meaningful. In general a survey should take around 10 to 15 minutes to complete"));
        helpList.add(new HelpCenterListItem("Can you mail the surveys to me or conduct them over the phone","Unfortunately there is no other way to take part in these online surveys, you need to complete them over the Internet"));
        helpList.add(new HelpCenterListItem("If I become a member of DoYourSurvey, will I receive e-mails advertising other products and services","No. We do not sell the personal information of our members and we will never try to sell you a product or service"));
        helpList.add(new HelpCenterListItem("Contact Us","Mail us your concern on contact@doyoursurvey.com"));
        myAssignedListAdapter= new MyAssignedListAdapter(mActivity,helpList);
        helpRecyclerView.setAdapter(myAssignedListAdapter);
        myAssignedListAdapter.notifyDataSetChanged();
        return root;
    }
    class AssignedListHolder extends RecyclerView.ViewHolder{

        CardView mainView;
        TextView TitleText,SubItem;
        public AssignedListHolder(View view){
            super(view);
            mainView=view.findViewById(R.id.cv_fragment_helpCenter_listitem);
            TitleText=view.findViewById(R.id.tv_fragmentHelpcenterListItem_Title);
            SubItem=view.findViewById(R.id.tv_fragmentHelpCenterListItem_SubItem);

        }

    }
    class MyAssignedListAdapter extends RecyclerView.Adapter<AssignedListHolder>{

        List<HelpCenterListItem>itemList;
        Context mContext;
        public MyAssignedListAdapter(Context c, List<HelpCenterListItem> list){
            this.itemList=list;
            this.mContext=c;
        }
        @NonNull
        @Override
        public AssignedListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_helpcenter_listitem,parent,false);
            AssignedListHolder auditListHolder= new AssignedListHolder(itemView);
            return auditListHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull AssignedListHolder holder, int position) {

            String Title=itemList.get(position).getMainItem();
            String subItem=itemList.get(position).getSubItem();
            holder.SubItem.setText(subItem);
            holder.TitleText.setText(Title);
            holder.TitleText.setOnClickListener(v->{
                if(holder.SubItem.getVisibility()==View.VISIBLE){
                    holder.SubItem.setVisibility(View.GONE);
                }else{
                    holder.SubItem.setVisibility(View.VISIBLE);
                }
            });


        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

    }
}