package kheloindia.com.assessment.webservice;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;

import kheloindia.com.assessment.model.UpdateTokenModel;
import kheloindia.com.assessment.model.UserModel;
import kheloindia.com.assessment.util.AppConfig;
import kheloindia.com.assessment.util.Crypto;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateTokenRequest implements Callback<UpdateTokenModel> {

    private String userid,token;
    private ResponseListener responseListener;
    private Activity activity;

    public UpdateTokenRequest(String userid,String token,Activity activity,ResponseListener responseListener){
        this.userid = userid;
        this.token = token;
        this.responseListener = responseListener;
        this.activity = activity;
    }

    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<UpdateTokenModel> call = apiService.updateToken(getRequestBody());
        call.enqueue(this);
    }

    private HashMap<String, Object> getRequestBody() {
        HashMap hashMap=new HashMap<String, Object>();
//        Crypto crypto = new Crypto(AppConfig.security_key);
//        hashMap.put("userid", crypto.encryptAsBase64(userid));
        hashMap.put("userid", userid);
        hashMap.put(AppConfig.TOKEN,token);
        hashMap.put(AppConfig.DEVICE_TYPE,2);
        hashMap.put(AppConfig.USER_TYPE,4);
        Log.e("LoginRequest","hashmap==> "+hashMap);
        return hashMap;
    }

    @Override
    public void onResponse(Call<UpdateTokenModel> call, Response<UpdateTokenModel> response) {
//        try {
//            responseListener.onResponse(response.body());
//        }catch(Exception e){
//            e.printStackTrace();
//        }
    }

    @Override
    public void onFailure(Call<UpdateTokenModel> call, Throwable t) {
//        responseListener.onResponse(null);
    }


}
