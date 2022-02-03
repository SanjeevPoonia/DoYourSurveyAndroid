package com.qdegrees.network;


import com.qdegrees.network.request.ChangePassword_Request;
import com.qdegrees.network.request.DashboardPostData;
import com.qdegrees.network.request.GenerateSurveyReferCodeRequest;
import com.qdegrees.network.request.LoginPostData;
import com.qdegrees.network.request.ProfileQuestion_Request;
import com.qdegrees.network.request.ProfileSubmit_Request;
import com.qdegrees.network.request.ReedemPointsRequest;
import com.qdegrees.network.request.ReferAFriend_Request;
import com.qdegrees.network.request.RegisterUserRequest;
import com.qdegrees.network.request.RewardPostData;
import com.qdegrees.network.request.SignupVerifyEmailRequest;
import com.qdegrees.network.request.SocialLogin_Request;
import com.qdegrees.network.request.SubmitSurvey_Request;
import com.qdegrees.network.request.VerifyOtpRequest;
import com.qdegrees.network.response.DashboardProfileResponse;
import com.qdegrees.network.response.DashboardResponse;
import com.qdegrees.network.response.GenerateSurveyReferCodeResponse;
import com.qdegrees.network.response.GeneratedSurveyReferCode_Response;
import com.qdegrees.network.response.LoginResponse;
import com.qdegrees.network.response.PointRedeemedHistory_Response;
import com.qdegrees.network.response.ProfileQuestion_Response;
import com.qdegrees.network.response.ReedemPointResponse;
import com.qdegrees.network.response.ShareableSurveyResponse;
import com.qdegrees.network.response.SignupVerifyEmailReqResponse;
import com.qdegrees.network.response.VerifyOtpResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
public interface ApiService {
    @POST("client/login")
    Call<LoginResponse> doLogin(@Body LoginPostData loginPostData);

    @POST("survey/getDynSurvey")
    Call<DashboardResponse>getDashboardData(@Body DashboardPostData dashboardPostData);

    @POST("client/sendOtpForVerifyEmail")
    Call<SignupVerifyEmailReqResponse> getVerifyOtp(@Body SignupVerifyEmailRequest signupVerifyEmailRequest);

    @POST("client/verifyOtpForEmail")
    Call<VerifyOtpResponse> doVerifyOtp(@Body VerifyOtpRequest verifyOtpRequest);

    @POST("client/signup")
    Call<LoginResponse>doSignup(@Body RegisterUserRequest registerUserRequest);

    @POST("user-panel-profile/dashboard")
    Call<DashboardProfileResponse>getDashboardProfile(@Body DashboardPostData dashboardPostData);

    @POST("survey/getRedeemedPointHistory")
    Call<PointRedeemedHistory_Response>getRedeemedHistory(@Body RewardPostData rewardPostData);

    @POST("survey/redeemPoint")
    Call<ReedemPointResponse>DoReedemPoints(@Body ReedemPointsRequest reedemPointsRequest);

    @POST("survey/SubmitSurvey")
    Call<ReedemPointResponse>DoSubmitSurvey(@Body SubmitSurvey_Request submitSurvey_request);

    @POST("user-panel-profile/userProfileQuestion")
    Call<ProfileQuestion_Response>getProfileQuestion(@Body ProfileQuestion_Request profileQuestion_request);

    @POST("user-panel-profile/userProfileTaskSubmit")
    Call<ReedemPointResponse>DoProfileSubmit(@Body ProfileSubmit_Request profileSubmit_request);

    @POST("client/changePassword")
    Call<ReedemPointResponse>DoChangePassword(@Body ChangePassword_Request changePassword_request);

    @POST("dashboard/referFriend")
    Call<ReedemPointResponse>DoReferFriend(@Body ReferAFriend_Request referAFriend_request);

    @POST("client/isRegistred")
    Call<LoginResponse>doSocialLogin(@Body SocialLogin_Request socialLogin_request);

    @GET("survey/getReferedEligibleSurvey")
    Call<ShareableSurveyResponse>getShareableSurveyList();
    @POST("survey/generateSurveyReferCode")
    Call<GenerateSurveyReferCodeResponse>generateSurveyReferCode(@Body GenerateSurveyReferCodeRequest generateSurveyReferCodeRequest);
    @POST("survey/getGenerateSurveyReferCode")
    Call<GeneratedSurveyReferCode_Response>getGeneratedReferalCode(@Body GenerateSurveyReferCodeRequest generateSurveyReferCodeRequest);


}
