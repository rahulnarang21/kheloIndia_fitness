package kheloindia.com.assessment.webservice.KotlinRequests

import kheloindia.com.assessment.model.CoachTokenModel

import android.app.Activity
import kheloindia.com.assessment.model.CoachModel
import kheloindia.com.assessment.util.AppConfig
import kheloindia.com.assessment.util.ProgressDialogUtility
import kheloindia.com.assessment.util.ResponseListener
import kheloindia.com.assessment.webservice.ApiClient
import kheloindia.com.assessment.webservice.ApiRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//@file:JvmName("CoachLoginRequest")
public class CoachTokenLoginRequest(val context:Activity,val responseListener: ResponseListener,
                               var progressDialogUtility: ProgressDialogUtility) : Callback<CoachTokenModel>{

    init {
        ApiClient.getClient2().create(ApiRequest::class.java).
        getCoachLoginToken(AppConfig.COACH_ADMIN_USERNAME,AppConfig.COACH_ADMIN_PASSWORD).
        enqueue(this)
    }

    override fun onFailure(call: Call<CoachTokenModel>, t: Throwable) {
        progressDialogUtility.dismissProgressDialog()
        responseListener.onResponse(null)

    }

    override fun onResponse(call: Call<CoachTokenModel>, response: Response<CoachTokenModel>) {
        //progressDialogUtility.dismissProgressDialog()
        responseListener.onResponse(response.body())
    }

}