package com.qdegrees.activity.ui.survey;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.OrientationHelper;

import com.qdegrees.activity.ui.dashboard.DashboardFragment;
import com.qdegrees.activity.ui.dashboard.SurveyOptions_HideShow_LIst;
import com.qdegrees.activity.ui.dashboard.SurveyOptions_ListItem;
import com.qdegrees.activity.ui.dashboard.SurveyQuestions_ListItem;
import com.qdegrees.doyoursurvey.R;
import com.qdegrees.local_storage.SharedPreferencesRepository;
import com.qdegrees.network.ApiService;
import com.qdegrees.network.RetrofitAPIClient;
import com.qdegrees.network.request.SubmitSurvey_Request;
import com.qdegrees.network.response.ReedemPointResponse;
import com.qdegrees.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.LinearLayout.HORIZONTAL;

public class SurveyStart_Activity extends AppCompatActivity {
    Activity mActivity;
    ImageView backImageView;
    TextView surveyNameText, questionText;
    LinearLayout editTypeLayout, radioLayout, checkLayout, otherLayout;
    EditText editText, otherEditText;
    RadioGroup radioGroup;
    CardView nextBtn, submitBtn;

    String Surveyid, SurveyName, SurveyPoints;
    List<SurveyQuestions_ListItem> QuestionList = new ArrayList<>();
    ArrayList<SurveyQuestions_ListItem> list = new ArrayList<>();
    int currentPosition = 0;
    String ActionId = "";
    String TextBox = "";
    List<SurveyAnswer_LIstItem_Object> AnswerList = new ArrayList<>();
    String sEmail="";
    protected ApiService apiService;
    /**********************Check Box******************/
    String checkOtherText="";

    /*********************Check Box*********************/

    String DynamicSelection="";
    String DynamicCondition="";
    boolean RequiredSelectionExit=false;
    boolean ProfilingExitRequired=false;
    String ProfilingEnd="";

    int QuestionsNumbers=0;

    List<String> CheckBoxActionList= new ArrayList<>();


    /**********************Show Hide Option***************************/

    boolean ShowHideApplicable=false;
    String ShowHideTargetId="";
    String ShowHideApplicableQuestionId="";
    List<SurveyOptions_HideShow_LIst> ShowHideOptionList= new ArrayList<>();


    /***********Matrix Question Added *******************************/

    LinearLayout MatrixLayout;
    TableLayout MatrixTableLayout;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surveystart_activity);
        mActivity = this;
        Surveyid = getIntent().getStringExtra("survey_id");
        SurveyName = getIntent().getStringExtra("survey_name");
        SurveyPoints = getIntent().getStringExtra("survey_points");
        list = (ArrayList<SurveyQuestions_ListItem>) getIntent().getSerializableExtra("questionList");
        QuestionList = list;
        sEmail= SharedPreferencesRepository.getDataManagerInstance().getSessionEmail();
        apiService = RetrofitAPIClient.getRetrofitClient();



        findIds();

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

        /***********Matrix Question Layout*************************************/

        MatrixLayout=findViewById(R.id.SurveyStart_Activity_MatrixLayout);
        MatrixTableLayout=findViewById(R.id.SurveyStart_Activity_MatixTableLayout);


        /**************************************************************************/





        nextBtn = findViewById(R.id.SurveyStart_Activity_NextbtnCv);
        submitBtn = findViewById(R.id.SurveyStart_Activity_SubmitbtnCv);
        backImageView.setOnClickListener(v -> {
            showAlertDialog();
        });
        surveyNameText.setText(SurveyName);
        if (QuestionList.size() > 0) {
            showQuestions(currentPosition);
        }
        nextBtn.setOnClickListener(v -> {
            hideSoftKeyboard(mActivity);

            if(nextBtnValidation(currentPosition)) {
                currentPosition = currentPosition + 1;
                if(RequiredSelectionExit){
                    Toast.makeText(mActivity, "You are not Eligible for this Survey.", Toast.LENGTH_SHORT).show();
                    submitSurvey("0","uncomplete");
                }
                else if(ProfilingExitRequired&&ProfilingEnd!=null&&!TextUtils.isEmpty(ProfilingEnd)&&ProfilingEnd.matches("end")){

                    submitSurvey("0","uncomplete");
                }
                else{
                    if (!TextUtils.isEmpty(ActionId) && !ActionId.matches("")) {
                        if(ActionId.matches("exit")){
                            submitSurvey("0","uncomplete");
                        }
                        else if(ActionId.matches("profiling_exit")){
                            ProfilingExitRequired=true;
                            showQuestions(currentPosition);
                        }
                        else{
                            for (int i = 0; i < QuestionList.size(); i++) {
                                String id = QuestionList.get(i).getIdStr();
                                if (ActionId.matches(id)) {
                                    currentPosition = i;

                                    break;
                                }
                            }
                            showQuestions(currentPosition);
                        }

                    } else {

                        showQuestions(currentPosition);
                    }
                }

            }
        });
        submitBtn.setOnClickListener(v->{
            hideSoftKeyboard(mActivity);
            if(nextBtnValidation(currentPosition)) {
                submitSurvey(SurveyPoints,"complete");
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
            Button positiveBtn=alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeBtn=alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            positiveBtn.setTextColor(Color.parseColor("#BD0000"));
            negativeBtn.setTextColor(Color.parseColor("#3053F4"));

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

        int qNo = position + 1;
        CheckBoxActionList.clear();
        QuestionsNumbers=QuestionsNumbers+1;
        RequiredSelectionExit=false;

        String QuestionStr=QuestionList.get(position).getQuestionStr();
        String msgStr=QuestionList.get(position).getQuestionMessage();


        /******************/
        if(!ShowHideApplicable) {
            boolean sht = QuestionList.get(position).isShowHideApplicable();
            ShowHideApplicable = sht;



            if (ShowHideApplicable) {
                ShowHideTargetId = QuestionList.get(position).getShowHideAppliTargetId();
                ShowHideApplicableQuestionId=QuestionList.get(position).getIdStr();

            }
        }







        /********************************************/



        if(DynamicSelection!=null&&!TextUtils.isEmpty(DynamicSelection)){
            QuestionStr=QuestionStr.replace("(Auto-Brand)",DynamicSelection);
        }


        questionText.setText("Q. " + QuestionsNumbers + " " +QuestionStr +"  "+msgStr);
        String QUestionType = QuestionList.get(position).getTypeStr();


        DynamicCondition=QuestionList.get(position).getDynamicSelection();
        ProfilingEnd=QuestionList.get(position).getProfiling();





        otherEditText.setText("");
        editText.setText("");
        otherLayout.setVisibility(View.GONE);


        if (QUestionType.matches(mActivity.getResources().getString(R.string.question_type_text))) {
            editTypeLayout.setVisibility(View.VISIBLE);
            radioLayout.setVisibility(View.GONE);
            checkLayout.setVisibility(View.GONE);
            MatrixLayout.setVisibility(View.GONE);
            editText.requestFocus();
            ActionId = "";
            TextBox = "";
            List<SurveyOptions_ListItem> options = QuestionList.get(position).getOptionsList();
            for (int i = 0; i < options.size(); i++) {
                ActionId=options.get(i).getActionId();
            }
        }
        else if (QUestionType.matches(mActivity.getResources().getString(R.string.question_type_radio))) {
            editTypeLayout.setVisibility(View.GONE);
            radioLayout.setVisibility(View.VISIBLE);
            checkLayout.setVisibility(View.GONE);
            MatrixLayout.setVisibility(View.GONE);
            radioGroup.removeAllViews();
            radioGroup.setOnCheckedChangeListener(null);
            radioGroup.clearCheck();


            List<SurveyOptions_ListItem> options = QuestionList.get(position).getOptionsList();
            String qId=QuestionList.get(position).getIdStr();

            if(qId.matches(ShowHideTargetId)){



                for(int i=0;i<options.size();i++){
                    String optionName=options.get(i).getOptionStr();
                    for(int j=0;j<ShowHideOptionList.size();j++){
                        String value=ShowHideOptionList.get(j).getProductName();

                        if(optionName.matches(value)){

                            boolean mss=ShowHideOptionList.get(j).isProductValue();

                            options.get(i).setShowThisOption(mss);
                            break;
                        }
                    }
                }

            }

            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5,10,5,10);



            for (int i = 0; i < options.size(); i++) {
                boolean isShow=options.get(i).isShowThisOption();

                if(isShow) {
                    RadioButton rd = new RadioButton(mActivity);
                    rd.setBackgroundColor(Color.parseColor("#B9ECF3"));
                    rd.setId(i);
                    rd.setText(options.get(i).getOptionStr());
                    rd.setGravity(Gravity.CENTER_VERTICAL);
                    rd.setLayoutParams(layoutParams);
                    radioGroup.addView(rd);
                }
            }
            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton rb = group.findViewById(group.getCheckedRadioButtonId());
                String str = rb.getText().toString();
                QuestionList.get(position).setAnswerRadio(str);
                for (int i = 0; i < options.size(); i++) {
                    String optio = options.get(i).getOptionStr();
                    if (optio.equals(str)) {
                        ActionId = options.get(i).getActionId();
                        TextBox = options.get(i).getTextBox();
                        if(ShowHideApplicableQuestionId.matches(qId)) {
                            ShowHideOptionList = options.get(i).getShowHideList();

                        }
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
            MatrixLayout.setVisibility(View.GONE);
            List<SurveyOptions_ListItem> options = QuestionList.get(position).getOptionsList();
            List<String> AnswerArray = QuestionList.get(position).getCheckBoxAnswer();
            ActionId="";
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
                        CheckBoxActionList.add(acton);

                    } else {
                        AnswerArray.remove(opt);
                        if(checkOtherText.matches(opt)) {
                            TextBox = "";
                        }
                        CheckBoxActionList.remove(acton);

                    }
                    if(AnswerArray.contains(checkOtherText)){
                        otherLayout.setVisibility(View.VISIBLE);
                    }else{
                        otherLayout.setVisibility(View.GONE);
                        otherEditText.setText("");
                    }






                    QuestionList.get(position).setCheckBoxAnswer(AnswerArray);

                    if(CheckBoxActionList.size()>0){
                        if(allElementsTheSame(CheckBoxActionList)){
                            ActionId=CheckBoxActionList.get(0);
                        }
                    }


                });

            }
        }
        else if (QUestionType.matches(mActivity.getResources().getString(R.string.question_type_matrix_check))){
            editTypeLayout.setVisibility(View.GONE);
            radioLayout.setVisibility(View.GONE);
            checkLayout.setVisibility(View.GONE);
            MatrixLayout.setVisibility(View.VISIBLE);

            MatrixTableLayout.removeAllViews();
            List<SurveyOptions_ListItem> options = QuestionList.get(position).getOptionsList();
            List<SurveyOptions_ListItem>optionsNew= new ArrayList<>();
            List<SurveyOptions_HideShow_LIst> ShowHideList= new ArrayList<>();
            List<String>blankColoumn= new ArrayList<>();

           // optionsNew.add(new SurveyOptions_ListItem("","","",0,ShowHideList,true,blankColoumn));
            optionsNew.addAll(options);

            List<String> coloumnList= new ArrayList<>();
            coloumnList.add("");
            if(options.size()>0){
                coloumnList.addAll(options.get(0).getColoumns());
            }

            TableRow FirstRow= new TableRow(this);
            FirstRow.setPadding(15,15,15,15);
            FirstRow.setWeightSum(1);

            for (int i=0;i<coloumnList.size();i++){
                TextView newText=new TextView(this);
                newText.setText(coloumnList.get(i));
                newText.setGravity(Gravity.CENTER);
                newText.setPadding(15,15,15,15);
                newText.setTextColor(Color.BLACK);
                newText.setTypeface(newText.getTypeface(), Typeface.BOLD);
                FirstRow.addView(newText);
            }
            MatrixTableLayout.addView(FirstRow);

            for(int i=0;i<optionsNew.size();i++){
                Log.e("mainPosition",""+i);
                TableRow dynaRow=new TableRow(this);
                dynaRow.setPadding(15,15,15,15);
                dynaRow.setWeightSum(1);

                for(int j=0;j<coloumnList.size();j++){
                    if(j==0){
                        Log.e("Position",""+i);
                        TextView Col= new TextView(this);
                        Col.setText(optionsNew.get(i).getOptionStr());
                        Col.setGravity(Gravity.CENTER);
                        Col.setPadding(15,15,15,15);
                        Col.setTextColor(Color.BLACK);
                        Col.setTypeface(Col.getTypeface(), Typeface.BOLD);
                        dynaRow.addView(Col);
                    }else{
                        LinearLayout ll= new LinearLayout(this);
                        ll.setGravity(Gravity.CENTER);
                        ll.setOrientation(HORIZONTAL);

                        CheckBox ch= new CheckBox(this);
                        ch.setPadding(15,15,15,15);
                        ll.addView(ch);
                        dynaRow.addView(ll);
                    }
                }
                MatrixTableLayout.addView(dynaRow);






            }











            String qId=QuestionList.get(position).getIdStr();
            for (int i = 0; i < options.size(); i++) {

/*
                boolean isShow=options.get(i).isShowThisOption();

                if(isShow) {
                    RadioButton rd = new RadioButton(mActivity);
                    rd.setId(i);
                    rd.setText(options.get(i).getOptionStr());
                    radioGroup.addView(rd);
                }*/
            }


        }
        if (qNo == QuestionList.size()) {

            submitBtn.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.GONE);
        }
        else {
            if(ActionId!=null&&!TextUtils.isEmpty(ActionId)&&ActionId.matches("final")){
                submitBtn.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.GONE);
            }else{
                submitBtn.setVisibility(View.GONE);
                nextBtn.setVisibility(View.VISIBLE);
            }


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
                    if(maxMinValidation(position,AnswerArray)) {
                        if (AnswerArray.contains(checkOtherText)) {
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
                    }else{
                        return false;
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
    private void submitSurvey(String surveyPoints,String surveyStatus){
        try {

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
                qobj.put("questions",Ques);
                qobj.put("options",option);
                qobj.put("answer",answerA);
                qobj.put("id",idAr);
                qobj.put("textVal",AnswerList.get(i).getTextValue());
                question.put(qobj);
            }


            if(Utility.isNetworkAvailable(mActivity)) {
                Utility.showDialog(mActivity);
                apiService.DoSubmitSurvey(new SubmitSurvey_Request("1",surveyPoints,SurveyName,Surveyid,"panelSurvey",surveyStatus,sEmail,question.toString())).enqueue(new Callback<ReedemPointResponse>() {
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
    private boolean maxMinValidation(int position,List<String> AnswerArray){
        String minSelection=QuestionList.get(position).getMinimumSelection();


        if(minSelection!=null&&!TextUtils.isEmpty(minSelection)&&!minSelection.matches("")){
            int mnsel=Integer.parseInt(minSelection);
            if(AnswerArray.size()>=mnsel){
                if(maxValidation(position,AnswerArray)){
                    List<String> RequiredOneOfThem=QuestionList.get(position).getRequiredOneOfThem();
                    if(RequiredOneOfThem!=null&&RequiredOneOfThem.size()>0){
                            boolean ansValidat=false;
                            for(int i=0;i<AnswerArray.size();i++){
                                String Ans=AnswerArray.get(i);
                                if(RequiredOneOfThem.contains(Ans)){
                                    ansValidat=true;
                                    break;
                                }
                            }
                            if(ansValidat){
                                RequiredSelectionExit=false;
                            }else{
                                RequiredSelectionExit=true;
                            }

                            return true;

                    }else{
                        return true;
                    }
                }else{
                    return false;
                }
            }else{
                Toast.makeText(mActivity, "Minimum "+mnsel+" Selection is Required!!!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            return true;
        }
    }
    public boolean maxValidation(int position,List<String>AnswerArray){
        String maxSelection=QuestionList.get(position).getMaximumSelection();
        if(maxSelection!=null&&!TextUtils.isEmpty(maxSelection)&&!maxSelection.matches("all")){
            int maxSel=Integer.parseInt(maxSelection);
            if(AnswerArray.size()<=maxSel){
                return true;

            }else{
                Toast.makeText(mActivity, "Maximum "+maxSel+" Can be select!!!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            return true;
        }
    }
    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isAcceptingText()) {
                inputMethodManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(),
                        0
                );
            }
        }

    }
    public static boolean allElementsTheSame(List<String> list) {
        if (list.size() != 0) {
            String first = list.get(0);
            for (String element : list) {
                if (!element.equals(first)) {
                    return false;
                }
            }
        }
        return true;
    }

}
