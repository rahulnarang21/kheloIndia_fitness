package kheloindia.com.assessment.webservice;


import android.content.Context;
import android.widget.Toast;

import java.util.HashMap;

import kheloindia.com.assessment.R;
import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.model.VerifyEmailOTPModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyEmailOTPRequest implements Callback<VerifyEmailOTPModel> {

    private ProgressDialogUtility progressDialogUtility;
    private String email;
    private ResponseListener responseListener;
    private Context activity;
    boolean isOTPResent;


    public VerifyEmailOTPRequest(Context activity, String email,boolean isOTPResent,ResponseListener responseListener) {
        this.email=email;
        this.responseListener=responseListener;
        this.activity=activity;
        this.isOTPResent = isOTPResent;
    }


    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<VerifyEmailOTPModel> call = apiService.verifyEmail(getRequestBody());

//             progressDialogUtility=new ProgressDialogUtility(activity);
//             progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }

    private HashMap<String, Object> getRequestBody() {
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put("AccessBy","0");
        hashMap.put("Screen","REG");
        hashMap.put("Email",email);
        hashMap.put("Otp",0);
        hashMap.put("otpSendFor",0);
        return hashMap;
    }

    @Override
    public void onResponse(Call<VerifyEmailOTPModel> call, Response<VerifyEmailOTPModel> response) {
        // progressDialogUtility.dismissProgressDialog();
        if (isOTPResent)
            Toast.makeText(activity, activity.getString(R.string.otp_sent), Toast.LENGTH_LONG).show();
        try {
            if(response.body()!=null)
                responseListener.onResponse(response.body());
            else{
                responseListener.onResponse(null);
            }
        } catch(Exception e){
            Constant.test_running=false;
            responseListener.onResponse(null);
        }
    }

    @Override
    public void onFailure(Call<VerifyEmailOTPModel> call, Throwable t) {
        responseListener.onResponse(null);
    }
}
