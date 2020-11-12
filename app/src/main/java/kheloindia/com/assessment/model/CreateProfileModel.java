package kheloindia.com.assessment.model;

import com.google.gson.annotations.SerializedName;

public class CreateProfileModel {


    /**
     * IsSuccess : true
     * Message : Data Saved Successfully
     * Result : 1
     */

    @SerializedName("IsSuccess")
    private String IsSuccess;
    @SerializedName("Message")
    private String Message;
    @SerializedName("MessageH")
    private String MessageH;
    @SerializedName("Result")
    private String Result;

    public String getIsSuccess() {
        return IsSuccess;
    }

    public void setIsSuccess(String IsSuccess) {
        this.IsSuccess = IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String Result) {
        this.Result = Result;
    }

    public String getMessageH() {
        return MessageH;
    }

    public void setMessageH(String messageH) {
        MessageH = messageH;
    }
}
