package kheloindia.com.assessment.webservice;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.HashMap;

import kheloindia.com.assessment.R;
import kheloindia.com.assessment.model.CreateProfileModel;
import kheloindia.com.assessment.util.AppConfig;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProfileRequest implements Callback<CreateProfileModel> {

    private ProgressDialogUtility progressDialogUtility;
    private String username, emailid, phoneno, address, qualification, gender, districtName, cityName, blockName,
            isAttended, totCode,sportsPrefs1,sportsPrefs2;
    private int stateId,organisation,position;
    private ResponseListener responseListener;
    private Activity activity;
    private SharedPreferences sharedPreferences;


    public CreateProfileRequest(Activity activity, String username, String emailid, String gender,String phoneno,
                                String address, String qualification, String cityName, int stateId, String districtName,
                                String blockName, String isAttended, String totCode, int organisation,int position,
                                String sportsPrefs1,String sportsPrefs2,ResponseListener responseListener) {
        this.username =username;
        this.emailid = emailid;
        this.gender =gender;
        this.phoneno =phoneno;
        this.address = address;
        this.stateId = stateId;
        this.districtName = districtName;
        this.cityName = cityName;
        this.blockName = blockName;
        this.qualification = qualification;
        this.isAttended = isAttended;
        this.totCode = totCode;
        this.organisation = organisation;
        this.position = position;
        this.sportsPrefs1 = sportsPrefs1;
        this.sportsPrefs2 = sportsPrefs2;
        this.responseListener = responseListener;
        this.activity = activity;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<CreateProfileModel> call = apiService.createProfile(getRequestBody());
        progressDialogUtility = new ProgressDialogUtility(activity);
        progressDialogUtility.setMessage(activity.getString(R.string.creating_profile_msg));
        progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }


    private HashMap<String, Object> getRequestBody() {
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put("Name",username);
        hashMap.put("Email",emailid);
        hashMap.put("Gender",gender);
        hashMap.put("PhoneNo",phoneno);
        hashMap.put(AppConfig.ADDRESS,address);
        hashMap.put(AppConfig.STATE_ID,stateId);
        hashMap.put(AppConfig.DISTRICT_NAME,districtName);
        hashMap.put(AppConfig.CITY_NAME,cityName);
        hashMap.put(AppConfig.BLOCK_NAME,blockName);
        hashMap.put("Qualification",qualification);
        hashMap.put(AppConfig.IS_TOT_ATTENDED,isAttended);
        hashMap.put(AppConfig.TOT_CODE,totCode);
        hashMap.put(AppConfig.ORGANIZATION,organisation);
        hashMap.put(AppConfig.POSITION,position);
        hashMap.put(AppConfig.SPORTS_PREFS1,sportsPrefs1);
        hashMap.put(AppConfig.SPORTS_PREFS2,sportsPrefs2);
        if (sharedPreferences.getString(AppConfig.LANGUAGE,"en").equals("hi"))
            hashMap.put(AppConfig.SELECTED_LANGUAGE,"H");
        Log.e("LoginRequest","hashmap==> "+hashMap);

        return hashMap;
    }


    @Override
    public void onResponse(Call<CreateProfileModel> call, Response<CreateProfileModel> response) {
        progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<CreateProfileModel> call, Throwable t) {
        progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }

}
