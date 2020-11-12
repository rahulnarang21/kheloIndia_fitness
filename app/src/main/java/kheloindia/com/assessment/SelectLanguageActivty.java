package kheloindia.com.assessment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.util.AppConfig;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.Utility;

public class SelectLanguageActivty extends AppCompatActivity implements View.OnClickListener {

    private CheckBox eng_cb,hin_cb;
    private Button next_btn;
    private TextView preferred_lang_tv;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_language_screen);
        eng_cb= findViewById(R.id.eng_cb);
        hin_cb= findViewById(R.id.hin_cb);
        next_btn= findViewById(R.id.next_btn);
        preferred_lang_tv= findViewById(R.id.preferred_lang_tv);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        if (sharedPreferences.getString(AppConfig.LANGUAGE,"en").equals("en")) {
            hin_cb.setButtonDrawable(android.R.color.transparent);
            hin_cb.setTextColor(getResources().getColor(R.color.grey));
            eng_cb.setChecked(true);
        }
        else {
            eng_cb.setButtonDrawable(android.R.color.transparent);
            eng_cb.setTextColor(getResources().getColor(R.color.grey));
            hin_cb.setChecked(true);
        }

        Utility.changeLanguage(this,sharedPreferences.getString(AppConfig.LANGUAGE,"en"));

        hin_cb.setOnClickListener(this);
        eng_cb.setOnClickListener(this);
        next_btn.setOnClickListener(this);
       // preferred_lang_tv.setPaintFlags(preferred_lang_tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Typeface font_semi_bold = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-SemiBold.ttf");
        next_btn.setTypeface(font_semi_bold);
        //Utility.changeLanguage(this,"en");
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.next_btn:
//            if (eng_cb.isChecked()) {
//
//
//            } else {
//
//
//            }
                Intent intent;
                DBManager dbManager = new DBManager();
                if (sharedPreferences.getBoolean(AppConfig.IS_LOGIN,false)) {
                    if (sharedPreferences.getBoolean(AppConfig.GOT_ALL_TEST,false) &&
                            (dbManager.getTableCount(DBManager.TBL_LP_FITNESS_TEST_CATEGORY,
                                    AppConfig.TEST_NAME_HINDI, "null"))<=0) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(AppConfig.GOT_ALL_TEST, false);
                        editor.apply();
                    }
                    Constant.classList.clear();
                    Constant.testList.clear();
                    intent = new Intent(SelectLanguageActivty.this, TakeTestActivity.class);
                }
                else
                    intent = new Intent(SelectLanguageActivty.this, KheloDashBoardActivity.class);
                startActivity(intent);
                break;


            case R.id.eng_cb:
                hin_cb.setButtonDrawable(android.R.color.transparent);
                hin_cb.setTextColor(getResources().getColor(R.color.grey));
                eng_cb.setButtonDrawable(R.drawable.selected_i);
                eng_cb.setTextColor(getResources().getColor(R.color.white));
                Utility.changeLanguage(this,"en");
                preferred_lang_tv.setText(getString(R.string.pref_lan));
                next_btn.setText(getString(R.string.next));

                break;


            case R.id.hin_cb:
                eng_cb.setButtonDrawable(android.R.color.transparent);
                eng_cb.setTextColor(getResources().getColor(R.color.grey));
                hin_cb.setButtonDrawable(R.drawable.selected_i);
                hin_cb.setTextColor(getResources().getColor(R.color.white));
                Utility.changeLanguage(this,"hi");
                preferred_lang_tv.setText(getString(R.string.pref_lan));
                next_btn.setText(getString(R.string.next));

                break;

        }
    }
}
