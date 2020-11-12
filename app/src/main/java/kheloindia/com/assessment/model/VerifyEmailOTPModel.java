package kheloindia.com.assessment.model;

public class VerifyEmailOTPModel {

    /**
     * IsSuccess : True
     * Message : Success
     * MessageH : सफलता
     * OTP : 192414
     * Result : 1
     */

    private String IsSuccess;
    private String Message;
    private String MessageH;
    private String OTP;
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

    public String getMessageH() {
        return MessageH;
    }

    public void setMessageH(String MessageH) {
        this.MessageH = MessageH;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String Result) {
        this.Result = Result;
    }
}
