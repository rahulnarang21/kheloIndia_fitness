package kheloindia.com.assessment.webservice;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

import kheloindia.com.assessment.R;
import kheloindia.com.assessment.model.ForgotPasswordModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CT13 on 2017-06-15.
 */

public class ForgotPasswordRequest implements Callback<ForgotPasswordModel> {

    private ProgressDialogUtility progressDialogUtility;
    private String requestType, email, otp, newpassword;
    private ResponseListener responseListener;
    private Activity activity;
    private boolean isOtpResent;


    public ForgotPasswordRequest(Activity activity, String requestType, String email, String otp,
                                 String newpassword, ResponseListener responseListener, boolean isOtpResent) {
        this.requestType = requestType;
        this.email = email;
        this.otp = otp;
        this.newpassword = newpassword;
        this.responseListener = responseListener;
        this.activity = activity;
        this.isOtpResent = isOtpResent;
    }


    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<ForgotPasswordModel> call = apiService.submitForgotPassword(getRequestBody());

        progressDialogUtility = new ProgressDialogUtility(activity);
        progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }


    private HashMap<String, Object> getRequestBody() {
        HashMap hashMap = new HashMap<String, Object>();
        hashMap.put("requestType", requestType);
        hashMap.put("email", email);
        hashMap.put("otp", otp);
        hashMap.put("newpassword", newpassword);

        Log.e("ForgotpasswordRequest", "hashmap==> " + hashMap);

        return hashMap;
    }


    @Override
    public void onResponse(Call<ForgotPasswordModel> call, Response<ForgotPasswordModel> response) {
        progressDialogUtility.dismissProgressDialog();
        if (isOtpResent)
            Toast.makeText(activity, activity.getString(R.string.otp_sent), Toast.LENGTH_LONG).show();
        try {
            responseListener.onResponse(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<ForgotPasswordModel> call, Throwable t) {
        progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }
}
