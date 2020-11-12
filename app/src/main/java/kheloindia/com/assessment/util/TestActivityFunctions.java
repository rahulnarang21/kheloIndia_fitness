package kheloindia.com.assessment.util;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import kheloindia.com.assessment.R;

public class TestActivityFunctions {
    public static void setGender(Context context,TextView age_gender_tv, ImageView boy_or_girl_img, String gender, String age){
        if (gender.equalsIgnoreCase("1")) {

            age_gender_tv.setText(age+"/"+context.getString(R.string.female_short));

        } else if (gender.equalsIgnoreCase("0")) {

            age_gender_tv.setText(age+"/"+context.getString(R.string.male_short));
        }

        else if (gender.equalsIgnoreCase("2")){
            age_gender_tv.setText(age+"/"+context.getString(R.string.transgender_short));
        }

        if (gender.equalsIgnoreCase("1")) {
            boy_or_girl_img.setImageResource(R.drawable.girl_blue_i);
        } else if (gender.equalsIgnoreCase("0")) {
            boy_or_girl_img.setImageResource(R.drawable.boy_i);
        }
    }
}
