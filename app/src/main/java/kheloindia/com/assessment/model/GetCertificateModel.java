package kheloindia.com.assessment.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GetCertificateModel implements Parcelable {
    protected GetCertificateModel(Parcel in) {
        IsSuccess = in.readString();
        Message = in.readString();
    }

    public static final Creator<GetCertificateModel> CREATOR = new Creator<GetCertificateModel>() {
        @Override
        public GetCertificateModel createFromParcel(Parcel in) {
            return new GetCertificateModel(in);
        }

        @Override
        public GetCertificateModel[] newArray(int size) {
            return new GetCertificateModel[size];
        }
    };

    public String getIsSuccess() {
        return IsSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        IsSuccess = isSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    private String IsSuccess;
    private String Message;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(IsSuccess);
        dest.writeString(Message);
    }

}
