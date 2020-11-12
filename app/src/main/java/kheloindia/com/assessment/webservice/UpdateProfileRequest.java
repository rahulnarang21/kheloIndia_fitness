package kheloindia.com.assessment.webservice;

import android.app.Activity;

import java.util.HashMap;

import kheloindia.com.assessment.model.ProfileModel;
import kheloindia.com.assessment.util.AppConfig;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.functions.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC10 on 18-Aug-17.
 */

public class UpdateProfileRequest implements Callback<ProfileModel> {

    private ProgressDialogUtility progressDialogUtility;
    private String username,email,phone,address,Qualification, Whatsup_No, Tshirt_Size, Trouser_Size, Aadhar_No,
            Alternative_Email, cityName, districtName, blockName,sportsPrefs1,sportsPrefs2;
    int stateId,gender,organisation,position;
    private ResponseListener responseListener;
    private Activity activity;
    private boolean onlyForEmail;


    public UpdateProfileRequest(Activity activity, String username, int gender,String phone, String address, String Qualification, String Whatsup_No,
                                String Tshirt_Size, String Trouser_Size, String Aadhar_No, String Alternative_Email,
                                String cityName, int stateId, String districtName, String blockName,
                                int organisation,int position, String sportsPrefs1,String sportsPrefs2,
                                ResponseListener responseListener){
        this.username = username;
        this.gender = gender;
        this.phone=phone;
        this.address=address;
        this.Qualification=Qualification;
        this.Whatsup_No=Whatsup_No;
        this.Tshirt_Size=Tshirt_Size;
        this.Trouser_Size=Trouser_Size;
        this.Aadhar_No=Aadhar_No;
        this.Alternative_Email=Alternative_Email;
        this.cityName = cityName;
        this.stateId = stateId;
        this.districtName = districtName;
        this.blockName = blockName;
        this.position = position;
        this.organisation = organisation;
        this.sportsPrefs1 = sportsPrefs1;
        this.sportsPrefs2 = sportsPrefs2;
        this.onlyForEmail = false;
        this.responseListener=responseListener;
        this.activity=activity;
    }


    public UpdateProfileRequest(Activity activity, String email,
                                ResponseListener responseListener){
        this.email=email;
        this.onlyForEmail = true;
        this.responseListener=responseListener;
        this.activity=activity;
    }

    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<ProfileModel> call = apiService.getProfileDetails(getRequestBody());

        progressDialogUtility=new ProgressDialogUtility(activity);
        progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }


    private HashMap<String, Object> getRequestBody(){
        HashMap hashMap=new HashMap<String, Object>();
        if (!onlyForEmail){
            hashMap.put("coordinatorname",username);
            hashMap.put(AppConfig.GENDER,gender);
            hashMap.put("Phone",phone);
            hashMap.put("Address",address);
            hashMap.put("Qualification",Qualification);
            hashMap.put("Whatsup_No",Whatsup_No);
            hashMap.put("Tshirt_Size",Tshirt_Size);
            hashMap.put("Trouser_Size",Trouser_Size);
            hashMap.put("Aadhar_No",Aadhar_No);
            hashMap.put("Alternative_Email",Alternative_Email);
            hashMap.put(AppConfig.STATE_ID, stateId);
            hashMap.put(AppConfig.CITY_NAME, cityName);
            hashMap.put(AppConfig.DISTRICT_NAME, districtName);
            hashMap.put(AppConfig.BLOCK_NAME, blockName);
            hashMap.put(AppConfig.ORGANIZATION,organisation);
            hashMap.put(AppConfig.POSITION,position);
            hashMap.put(AppConfig.SPORTS_PREFS1,sportsPrefs1);
            hashMap.put(AppConfig.SPORTS_PREFS2,sportsPrefs2);
        }
        else {
            hashMap.put(AppConfig.EMAIL,email);
        }
        hashMap.put("Test_Coordinator_ID", Constant.TEST_COORDINATOR_ID);


        return hashMap;
    }


    @Override
    public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
        progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<ProfileModel> call, Throwable t) {
        progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }
}