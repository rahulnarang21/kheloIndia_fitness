package kheloindia.com.assessment.model;

public class UpdateTokenModel {

    /**
     * IsSuccess : true
     * Message : Token updated
     * MessageH : null
     * OTP :
     * Result : fsaqiV6RFEjamubVo39T1a:APA91bGJmUG9NB7IBVjrk4nvuad_V-a3DXv9BjyyoDUY021ucmDZntNbfgsbVLQxhsCIzn2hvmemjyx_7HOzf1ZunFwU5eBrpLrTVeP3l_IfigZ7OpExJx7B8E2Aa05pRQsES0E3BhXE
     */

    private String IsSuccess;
    private String Message;
    private Object MessageH;
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

    public Object getMessageH() {
        return MessageH;
    }

    public void setMessageH(Object MessageH) {
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
