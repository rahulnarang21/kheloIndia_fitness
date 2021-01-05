package kheloindia.com.assessment.webservice.KotlinRequests

import android.app.Activity
import kheloindia.com.assessment.model.CoachModel
import kheloindia.com.assessment.util.ProgressDialogUtility
import kheloindia.com.assessment.util.ResponseListener
import kheloindia.com.assessment.webservice.ApiClient
import kheloindia.com.assessment.webservice.ApiRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//@file:JvmName("CoachLoginRequest")
public class CoachLoginRequest(val context:Activity,val responseListener: ResponseListener,
                        var progressDialogUtility: ProgressDialogUtility,val token:String,
                               val username:String, val password:String) : Callback<CoachModel>{

    init {
        ApiClient.getClient2().create(ApiRequest::class.java).
        getCoachDetails("Bearer $token",username,password).
        enqueue(this)
    }

    override fun onFailure(call: Call<CoachModel>, t: Throwable) {
        progressDialogUtility.dismissProgressDialog()
        responseListener.onResponse(null)

    }

    override fun onResponse(call: Call<CoachModel>, response: Response<CoachModel>) {
        progressDialogUtility.dismissProgressDialog()
        responseListener.onResponse(response.body())
    }

}