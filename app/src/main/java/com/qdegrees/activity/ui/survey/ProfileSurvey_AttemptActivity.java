package com.qdegrees.activity.ui.survey;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.qdegrees.activity.ui.dashboard.DashboardFragment;
import com.qdegrees.activity.ui.dashboard.SurveyOptions_HideShow_LIst;
import com.qdegrees.activity.ui.dashboard.SurveyOptions_ListItem;
import com.qdegrees.activity.ui.dashboard.SurveyQuestions_ListItem;
import com.qdegrees.activity.ui.dashboard.Survey_Listitem_Object;
import com.qdegrees.doyoursurvey.R;
import com.qdegrees.local_storage.SharedPreferencesRepository;
import com.qdegrees.network.ApiService;
import com.qdegrees.network.RetrofitAPIClient;
import com.qdegrees.network.request.DashboardPostData;
import com.qdegrees.network.request.ProfileQuestion_Request;
import com.qdegrees.network.request.ProfileSubmit_Request;
import com.qdegrees.network.request.SubmitSurvey_Request;
import com.qdegrees.network.response.DashboardResponse;
import com.qdegrees.network.response.ProfileQuestion_Response;
import com.qdegrees.network.response.ProfileQuestion_Response;
import com.qdegrees.network.response.ReedemPointResponse;
import com.qdegrees.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileSurvey_AttemptActivity extends AppCompatActivity {
    Activity mActivity;
    ImageView backImageView;
    TextView surveyNameText, questionText;
    LinearLayout editTypeLayout, radioLayout, checkLayout, otherLayout;
    EditText editText, otherEditText;
    RadioGroup radioGroup;
    CardView nextBtn, submitBtn;

    String Surveyid="", SurveyName="", SurveyPoints="";
    List<SurveyQuestions_ListItem> QuestionList = new ArrayList<>();
    ArrayList<SurveyQuestions_ListItem> list = new ArrayList<>();
    int currentPosition = 0;
    String ActionId = "";
    String TextBox = "";
    List<SurveyAnswer_LIstItem_Object> AnswerList = new ArrayList<>();
    String sEmail="",SurveyType="";
    int SurveyPercentage=0;
    protected ApiService apiService;
    /**********************Check Box******************/
    String checkOtherText="";
    /*********************Check Box*********************/

    String DynamicSelection="";
    String DynamicCondition="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surveystart_activity);
        mActivity = this;
        SurveyType = getIntent().getStringExtra("survey_type");
        SurveyPercentage = getIntent().getIntExtra("percentage",0);

        Log.e("SurveyPercentage",""+SurveyPercentage);

        sEmail= SharedPreferencesRepository.getDataManagerInstance().getSessionEmail();
        apiService = RetrofitAPIClient.getRetrofitClient();

        Log.e("QuestionListSize", "" + QuestionList.size());
        getProfileQuestions();

    }
    private void findIds() {
        backImageView = findViewById(R.id.SurveyStart_Activity_BackImageView);
        surveyNameText = findViewById(R.id.SurveyStart_Activity_SurveyNameText);
        questionText = findViewById(R.id.SurveySrart_Activity_Questiontext);
        editTypeLayout = findViewById(R.id.SurveyStart_Activity_EditText_Layout);
        editText = findViewById(R.id.SurveyStart_Activity_EditText_Answer);
        radioLayout = findViewById(R.id.SurveyStart_Activity_RadioLayout);
        radioGroup = findViewById(R.id.SurveyStart_Activity_RadioGroup);
        checkLayout = findViewById(R.id.SurveyStart_Activity_CheckBoxLayout);
        otherLayout = findViewById(R.id.SurveyStart_Activity_OtherEditText_Layout);
        otherEditText = findViewById(R.id.SurveyStart_Activity_OtherEditText_Answer);


        nextBtn = findViewById(R.id.SurveyStart_Activity_NextbtnCv);
        submitBtn = findViewById(R.id.SurveyStart_Activity_SubmitbtnCv);
        backImageView.setOnClickListener(v -> {
            showAlertDialog();
        });
        surveyNameText.setText(SurveyName.toUpperCase());
        if (QuestionList.size() > 0) {
            showQuestions(currentPosition);
        }
        nextBtn.setOnClickListener(v -> {

            if(nextBtnValidation(currentPosition)) {
                currentPosition = currentPosition + 1;
                if (!TextUtils.isEmpty(ActionId) && !ActionId.matches("")) {
                    if (!ActionId.matches("exit")) {
                        for (int i = 0; i < QuestionList.size(); i++) {
                            String id = QuestionList.get(i).getIdStr();
                            if (ActionId.matches(id)) {
                                currentPosition = i;
                                break;
                            }
                        }
                        showQuestions(currentPosition);
                    } else {
                        // Submit the Survey here
                        submitSurvey();
                    }
                } else {
                    showQuestions(currentPosition);
                }
            }

        });
        submitBtn.setOnClickListener(v->{
            if(nextBtnValidation(currentPosition)) {
                submitSurvey();
            }
        });


    }

    @Override
    public void onBackPressed() {
        showAlertDialog();

    }

    private void showAlertDialog() {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
            alertDialogBuilder.setTitle("Exit!!");
            alertDialogBuilder.setMessage("Are you sure you want to exit this survey?");
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    finishActivity();
                    dialog.dismiss();
                }
            });
            alertDialogBuilder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void finishActivity() {
        Intent intent = new Intent();
        intent.putExtra(DashboardFragment.Extra_Test_KEY, "Testing passing data back to ActivityOne");
        setResult(DashboardFragment.RESULT_CODE, intent); // You can also send result without any data using setResult(int resultCode)
        mActivity.finish();
    }
    @SuppressLint("SetTextI18n")
    private void showQuestions(int position) {
        Log.e("position", "" + position);
        int qNo = position + 1;

        String QuestionStr=QuestionList.get(position).getQuestionStr();
        if(DynamicSelection!=null&&!TextUtils.isEmpty(DynamicSelection)){
            QuestionStr=QuestionStr.replace("(Auto-Brand)",DynamicSelection);
        }

        questionText.setText("Q. " + qNo + " " + QuestionStr);
        String QUestionType = QuestionList.get(position).getTypeStr();

        DynamicCondition=QuestionList.get(position).getDynamicSelection();

        otherEditText.setText("");
        editText.setText("");
        otherLayout.setVisibility(View.GONE);


        if (QUestionType.matches(mActivity.getResources().getString(R.string.question_type_text))) {
            editTypeLayout.setVisibility(View.VISIBLE);
            radioLayout.setVisibility(View.GONE);
            checkLayout.setVisibility(View.GONE);
            editText.requestFocus();

            ActionId = "";
            TextBox = "";
        }
        else if (QUestionType.matches(mActivity.getResources().getString(R.string.question_type_radio))) {
            editTypeLayout.setVisibility(View.GONE);
            radioLayout.setVisibility(View.VISIBLE);
            checkLayout.setVisibility(View.GONE);

            radioGroup.removeAllViews();
            radioGroup.setOnCheckedChangeListener(null);
            radioGroup.clearCheck();


            List<SurveyOptions_ListItem> options = QuestionList.get(position).getOptionsList();
            for (int i = 0; i < options.size(); i++) {
                RadioButton rd = new RadioButton(mActivity);
                rd.setId(i);
                rd.setText(options.get(i).getOptionStr());
                if(rd.isChecked()){
                    Log.e("rd","rdCheck");
                }
                radioGroup.addView(rd);
            }
            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton rb = group.findViewById(group.getCheckedRadioButtonId());
                String str = rb.getText().toString();
                QuestionList.get(position).setAnswerRadio(str);
                for (int i = 0; i < options.size(); i++) {
                    String optio = options.get(i).getOptionStr();
                    if (optio.matches(str)) {
                        ActionId = options.get(i).getActionId();
                        TextBox = options.get(i).getTextBox();
                        break;
                    }
                }
                if(DynamicCondition!=null&&!TextUtils.isEmpty(DynamicCondition)&&DynamicCondition.matches("yes")){
                    DynamicSelection=str;
                }

                if (TextBox!=null&&!TextUtils.isEmpty(TextBox)&&TextBox.matches("yes")) {
                    otherLayout.setVisibility(View.VISIBLE);
                } else {
                    otherLayout.setVisibility(View.GONE);
                    otherEditText.setText("");
                }

            });
        }
        else if (QUestionType.matches(mActivity.getResources().getString(R.string.question_type_checkbox))) {
            checkLayout.removeAllViews();
            editTypeLayout.setVisibility(View.GONE);
            radioLayout.setVisibility(View.GONE);
            checkLayout.setVisibility(View.VISIBLE);
            ActionId = "";
            List<SurveyOptions_ListItem> options = QuestionList.get(position).getOptionsList();
            List<String> AnswerArray = QuestionList.get(position).getCheckBoxAnswer();
            for(int i=0;i<options.size();i++){
                String acton = options.get(i).getActionId();
                String textb = options.get(i).getTextBox();
                String opt = options.get(i).getOptionStr();
                if (textb!=null&&!TextUtils.isEmpty(textb)&&textb.matches("yes")) {
                   checkOtherText=opt;
                   break;
                }
            }





            for (int i = 0; i < options.size(); i++) {
                CheckBox ch = new CheckBox(mActivity);
                ch.setText(options.get(i).getOptionStr());
                ch.setId(i);
                ch.setChecked(false);
                checkLayout.addView(ch);
                String acton = options.get(i).getActionId();
                String textb = options.get(i).getTextBox();
                String opt = options.get(i).getOptionStr();



                ch.setOnCheckedChangeListener((buttonView, isChecked) -> {

                    if (isChecked) {
                        AnswerArray.add(opt);
                        if(checkOtherText.matches(opt)) {

                            TextBox = textb;
                        }

                    } else {
                        AnswerArray.remove(opt);
                        if(checkOtherText.matches(opt)) {
                            TextBox = "";
                        }
                    }


                    if(AnswerArray.contains(checkOtherText)){
                        otherLayout.setVisibility(View.VISIBLE);
                    }else{
                        otherLayout.setVisibility(View.GONE);
                        otherEditText.setText("");
                    }
                    QuestionList.get(position).setCheckBoxAnswer(AnswerArray);


                });

            }

        }
        if (qNo == QuestionList.size()) {
            Log.e("submit", "submit");
            submitBtn.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.GONE);
        } else {
            submitBtn.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
        }
    }
    private boolean nextBtnValidation(int position) {
        String isRequired = QuestionList.get(position).getConditionStr();
        String QUestionType = QuestionList.get(position).getTypeStr();
        String QuestionStr = QuestionList.get(position).getQuestionStr();
        String QuestionIds = QuestionList.get(position).getIdStr();

        List<SurveyOptions_ListItem> optionLis = QuestionList.get(position).getOptionsList();
        List<String> AnswerArray = new ArrayList<>();

        String[] QuestionIdArray = new String[1];
        QuestionIdArray[0] = QuestionIds;


        String TextValue = "";
        if (QUestionType.matches(mActivity.getResources().getString(R.string.question_type_text))) {
            TextValue = editText.getText().toString();
            if (isRequired.matches("required")) {
                if (!TextUtils.isEmpty(TextValue.trim())) {
                    AnswerList.add(new SurveyAnswer_LIstItem_Object(QuestionStr, TextValue, optionLis, AnswerArray, QuestionIdArray));
                    return true;
                } else {
                    editText.setError("Mandatory!!! Survey Question");
                    Toast.makeText(mActivity, "Mandatory!!! Survey Question", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                AnswerList.add(new SurveyAnswer_LIstItem_Object(QuestionStr, TextValue, optionLis, AnswerArray, QuestionIdArray));
                return true;
            }


        }
        else if (QUestionType.matches(mActivity.getResources().getString(R.string.question_type_radio))) {

            String opt = QuestionList.get(position).getAnswerRadio();
            String Other = otherEditText.getText().toString();
            AnswerArray.add(opt);

            if (isRequired.matches("required")) {
                if (!TextUtils.isEmpty(opt.trim())) {
                    if (TextBox!=null&&!TextUtils.isEmpty(TextBox)&&TextBox.matches("yes")) {
                        if (!TextUtils.isEmpty(Other.trim())) {
                            AnswerList.add(new SurveyAnswer_LIstItem_Object(QuestionStr, Other, optionLis, AnswerArray, QuestionIdArray));
                            return true;
                        } else {
                            otherEditText.setError("Mandatory!!! Survey Other Field ");
                            Toast.makeText(mActivity, "Mandatory!!! Survey Other Field ", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                    } else {
                        AnswerList.add(new SurveyAnswer_LIstItem_Object(QuestionStr, Other, optionLis, AnswerArray, QuestionIdArray));
                        return true;
                    }
                } else {
                    Toast.makeText(mActivity, "Mandatory!!! Survey Question", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                AnswerList.add(new SurveyAnswer_LIstItem_Object(QuestionStr, Other, optionLis, AnswerArray, QuestionIdArray));
                return true;
            }


        }
        else if (QUestionType.matches(mActivity.getResources().getString(R.string.question_type_checkbox))) {

            List<String> AnswerA = QuestionList.get(position).getCheckBoxAnswer();
            AnswerArray = AnswerA;
            String Other = otherEditText.getText().toString();
            if (isRequired.matches("required")) {
                if (AnswerArray.size() > 0) {
                    if(AnswerArray.contains(checkOtherText)){
                        if(!TextUtils.isEmpty(Other.trim())){
                            AnswerList.add(new SurveyAnswer_LIstItem_Object(QuestionStr, Other, optionLis, AnswerArray, QuestionIdArray));
                            return true;
                        }else{
                            otherEditText.setError("Mandatory!!! Survey Other Field ");
                            Toast.makeText(mActivity, "Mandatory!!! Survey Other Field ", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }else{
                        AnswerList.add(new SurveyAnswer_LIstItem_Object(QuestionStr, Other, optionLis, AnswerArray, QuestionIdArray));
                        return true;
                    }

                } else {
                    Toast.makeText(mActivity, "Mandatory!!! Survey Question", Toast.LENGTH_SHORT).show();
                    return false;
                }

            } else {
                AnswerList.add(new SurveyAnswer_LIstItem_Object(QuestionStr, Other, optionLis, AnswerArray, QuestionIdArray));
                return true;
            }


        }
        else{
            return false;
        }

    }
    private void submitSurvey(){
        try {
            Log.e("Percentage",""+SurveyPercentage);

            int percentage=SurveyPercentage+10;
            Log.e("per",percentage+"");


            Log.e("compltedSurveyNumber", "1");
            Log.e("pointAdd", SurveyPoints);
            Log.e("surveyName", SurveyName);
            Log.e("surveyId", Surveyid);
            Log.e("surveyType", "panelSurvey");
            Log.e("surveyStatus", "complete");
            Log.e("email", sEmail);
            JSONArray question = new JSONArray();
            for (int i = 0; i < AnswerList.size(); i++) {
                String Ques = AnswerList.get(i).getQuestionStr();
                String text = AnswerList.get(i).getTextValue();
                JSONArray option = new JSONArray();
                List<SurveyOptions_ListItem> opt = AnswerList.get(i).getOptionsList();
                for (int j = 0; j < opt.size(); j++) {
                    String ot = opt.get(j).getOptionStr();
                    String act = opt.get(j).getActionId();
                    String tct = opt.get(j).getTextBox();
                    JSONObject jb = new JSONObject();
                    jb.put("option", ot);
                    jb.put("action_id", act);
                    jb.put("text_box", tct);
                    option.put(jb);
                }
                JSONArray answerA=new JSONArray();
                List<String> ansAr=AnswerList.get(i).getAnswerArray();
                for(int j=0;j<ansAr.size();j++){
                    answerA.put(ansAr.get(j));
                }
                JSONArray idAr=new JSONArray();
                idAr.put(AnswerList.get(i).getAnswerQuestionIdArray()[0]);

                JSONObject qobj= new JSONObject();
                qobj.put("question",Ques);
                qobj.put("options",option);
                qobj.put("ans",answerA);
                qobj.put("id",idAr);
                qobj.put("text_box",AnswerList.get(i).getTextValue());
                question.put(qobj);
            }
            Log.e("questions", question.toString());

            if(Utility.isNetworkAvailable(mActivity)) {
                Utility.showDialog(mActivity);

                apiService.DoProfileSubmit(new ProfileSubmit_Request(sEmail,SurveyType,question.toString(),percentage+"",SurveyPoints)).enqueue(new Callback<ReedemPointResponse>() {
                    @Override
                    public void onResponse(Call<ReedemPointResponse> call, Response<ReedemPointResponse> response) {
                        ReedemPointResponse reedemPointResponse=response.body();
                        int code=reedemPointResponse.status;
                        String msg=reedemPointResponse.message;

                        Utility.hideDialog(mActivity);
                        if(code==200){
                            finishActivity();
                        }else{
                            Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
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





        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    private void getProfileQuestions(){
        if(Utility.isNetworkAvailable(mActivity)){
            Utility.showDialog(mActivity);
            apiService.getProfileQuestion(new ProfileQuestion_Request(sEmail,SurveyType)).enqueue(new Callback<ProfileQuestion_Response>() {
                @Override
                public void onResponse(Call<ProfileQuestion_Response> call, Response<ProfileQuestion_Response> response) {
                    try {
                        ProfileQuestion_Response profileQuestion_response = response.body();
                        Log.e("Response", profileQuestion_response.toString());
                        int code = profileQuestion_response.status;
                        if (code == 200) {
                            QuestionList.clear();
                            ProfileQuestion_Response.Data dataList = profileQuestion_response.data;

                            Surveyid = dataList.id;
                            SurveyName = dataList.survey_name;
                            SurveyPoints = dataList.points;

                            List<ProfileQuestion_Response.Questions> questions = dataList.Questions;
                            for (int j = 0; j < questions.size(); j++) {
                                String Idstr = questions.get(j).id;
                                String Ques = questions.get(j).question;

                                String selection = questions.get(j).selection;
                                String type = questions.get(j).type;
                                String status = questions.get(j).status;
                                String conditions = questions.get(j).condition;
                                String dynamicSelection = questions.get(j).dynamicSelection;
                                List<String> requiredList=new ArrayList<>();


                                List<SurveyOptions_ListItem> OptionsList = new ArrayList<>();
                                List<ProfileQuestion_Response.Options> options = questions.get(j).Options;
                                for (int k = 0; k < options.size(); k++) {
                                    String option = options.get(k).option;
                                    String actionId = options.get(k).action_id;
                                    String textBox = options.get(k).text_box;
                                    List<SurveyOptions_HideShow_LIst> show=new ArrayList<>();
                                    List<String> coloumns=new ArrayList<>();
                                    OptionsList.add(new SurveyOptions_ListItem(option, actionId, textBox,0,show,true,coloumns));
                                }
                                QuestionList.add(new SurveyQuestions_ListItem(Idstr, Ques, "", selection, type, status, conditions, OptionsList,dynamicSelection,"","",requiredList,"",false,false,""));
                            }
                            Utility.hideDialog(mActivity);
                            findIds();


                        } else {
                            Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                            Utility.hideDialog(mActivity);
                            finishActivity();
                        }
                    }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                    Utility.hideDialog(mActivity);
                }


                }

                @Override
                public void onFailure(Call<ProfileQuestion_Response> call, Throwable t) {
                    Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                    Utility.hideDialog(mActivity);
                    finishActivity();
                }
            });




            

        }else{
            Utility.showAlertDialog(mActivity, mActivity.getString(R.string.alert), mActivity.getString(R.string.no_internet_connection));
        }
    }
}
