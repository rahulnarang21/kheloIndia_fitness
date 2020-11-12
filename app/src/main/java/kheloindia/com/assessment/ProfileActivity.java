package kheloindia.com.assessment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.Base64;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import kheloindia.com.assessment.adapter.ListViewDialogAdapter;
import kheloindia.com.assessment.adapter.SchoolNewAdapter;
import kheloindia.com.assessment.model.ListViewDialogModel;
import kheloindia.com.assessment.model.ProfileImageModel;
import kheloindia.com.assessment.model.ProfileModel;
import kheloindia.com.assessment.model.VerifyEmailOTPModel;
import kheloindia.com.assessment.util.AppConfig;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.util.Utility;

import kheloindia.com.assessment.functions.CircleTransformWhite;
import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.webservice.ApiClient;
import kheloindia.com.assessment.webservice.ApiRequest;
import kheloindia.com.assessment.webservice.ForgotPasswordRequest;
import kheloindia.com.assessment.webservice.UpdateProfileRequest;

import kheloindia.com.assessment.webservice.VerifyEmailOTPRequest;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by PC10 on 06/27/2017.
 */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener {

    Toolbar toolbar;
    ImageView back_img, profile_img;
    TextView logout_tv;
    FloatingActionButton select_image_fab;
    Context ctx;
    InputStream targetStream;
    // Camera activity request codes

    String TAG = "ProfileActivity";

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final String IMAGE_DIRECTORY_NAME = "Fitness365_Images";
    public static final int REQUEST_SELECT_PICTURE = 2;
    public static Uri mUri;
    public static Bitmap mPhoto;
    File imageFile;
    String Base64Image;
    EditText phone_edt, address_et,city_et,state_et,district_et,block_et,username_et,
            organisation_et,position_et,sportsPrefs1_et,sportsPrefs2_et;
    TextView email_tv, username_tv, gender_tv, name_tv, sportsPrefs2Label;
    ImageView edit_phone_img, save_phone_img, edit_address_img, save_address_img,edit_city_img,
            edit_state_img,edit_district_img,edit_block_img,edit_gender_img,edit_email_img,
            edit_username_img,edit_organisation_img,edit_position_img,edit_sports_prefs1_img,
            edit_sports_prefs2_img;
    //  LinearLayout school_linearlayout;
    SharedPreferences sp;

    private AsyncHttpClient client;
    private ProgressDialog dialog;
    Boolean isConnected;

    int phoneOrAddress = 0;

    private ConnectionDetector connectionDetector;
    private ProgressDialogUtility progressDialogUtility;

    private SchoolNewAdapter schoolNewAdapter;

    private ArrayList<HashMap<String, String>> schoolList;

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    String phone = "", address = "", email = "", selectedGender = "", image = "", username = "",
            selectedStateName = "";

    String Qualification;
    String Whatsup_No;
    String Tshirt_Size ;
    String Trouser_Size;
    String Aadhar_No ;
    String Alternative_Email ;

    EditText qualification_et, t_shirt_size_et, trouser_size_et,personal_email_et,whatsapp_no_et,adhar_no_et;
    ImageView edit_qualification_img, edit_t_shirt_size_img,edit_trouser_size_img,edit_personal_email_img,
            edit_whatsapp_no_img, edit_adhar_no_img;
    Button save_details_btn;
    private Picasso picasso;
    boolean areStatesLoaded = false;
    boolean isGenderLoaded = false;
    ListView statesListView,genderListView,sportsPrefs1ListView,sportsPrefs2ListView,organisationListView,positionListView;
    ListViewDialogAdapter statesListAdapter,genderListAdapter,sportsPrefs1ListAdapter,sportsPrefs2ListAdapter,
            organisationListAdapter,positionListAdapter;
    ArrayList<ListViewDialogModel> statesArrayList = new ArrayList<>();
    ArrayList<ListViewDialogModel> genderArrayList = new ArrayList<>();
    AlertDialog statesAlertDialog,genderAlertDialog,organisationDialog,positionDialog,sportsPrefs1Dialog,sportsPrefs2Dialog;
    int selectedStateId=0;
    ListViewDialogModel selectedSportsPrefs1,selectedSportsPrefs2,selectedOrganisation,selectedPosition;
    int selectedGenderId = -1;
    private Dialog otpDialog;
    private EditText otp_edt;
    Typeface font_light, font_regular, font_medium;
    private AlertDialog verifyEmailDialog;
    private String newEmail="";
    private boolean resendOtp;
    private boolean onlyForEmail = false;
    ArrayList<ListViewDialogModel> sportsPrefs1ArrayList = new ArrayList<>();
    ArrayList<ListViewDialogModel> sportsPrefs2ArrayList = new ArrayList<>();
    ArrayList<ListViewDialogModel> organisationArrayList = new ArrayList<>();
    ArrayList<ListViewDialogModel> positionArrayList = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();

        String imagePath = sp.getString("profile_pic_url","");
        if (imagePath.length()>1){
            String path =getString(R.string.image_base_url).substring(0,getString(R.string.image_base_url).length()-1)+imagePath;
            Log.e(TAG,"path==> " + path);
            //Utility.rotateFabForward(this,select_image_fab);
            picasso .load(path).networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransformWhite()).into(profile_img, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    // Utility.rotateFabBackward(select_image_fab);
                }

                @Override
                public void onError(Exception e) {
                    // Utility.rotateFabBackward(select_image_fab);

                }

            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ctx = ProfileActivity.this;

        init();
        picasso =new Picasso.Builder(this).build();
    }

    private void init() {
        client = new AsyncHttpClient();
        client.setTimeout(1000 * 20);
        dialog = new ProgressDialog(ProfileActivity.this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        font_regular = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-Regular.ttf");
        font_medium = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-Medium.ttf");

        isConnected = checkInternetConnectionn(getApplicationContext());


        progressDialogUtility = new ProgressDialogUtility(this);
        schoolList = new ArrayList<HashMap<String, String>>();

        address = sp.getString(AppConfig.ADDRESS, "");
        phone = sp.getString("phone", "");
        email = sp.getString("email", "");
        selectedGender = sp.getString(AppConfig.GENDER, "");
        image = sp.getString("image", "");
        username = sp.getString(AppConfig.USER_NAME, "");
        Qualification = sp.getString("Qualification","");
        Whatsup_No = sp.getString("Whatsup_No","");
        Tshirt_Size = sp.getString("Tshirt_Size","");
        Trouser_Size = sp.getString("Trouser_Size","");
        Aadhar_No = sp.getString("Aadhar_No","");
        Alternative_Email = sp.getString("Alternative_Email","");

        setGenderId(selectedGender);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        back_img = (ImageView) toolbar.findViewById(R.id.back_img);
        logout_tv = (TextView) toolbar.findViewById(R.id.logout_tv);
        select_image_fab = (FloatingActionButton) findViewById(R.id.select_image_fab);
        profile_img = (ImageView) findViewById(R.id.profile_img);
        phone_edt = (EditText) findViewById(R.id.phone_edt);
        edit_phone_img = (ImageView) findViewById(R.id.edit_phone_img);
        save_phone_img = (ImageView) findViewById(R.id.save_phone_img);
        edit_address_img = (ImageView) findViewById(R.id.edit_address_img);
        edit_gender_img = findViewById(R.id.edit_gender_img);
        edit_email_img = findViewById(R.id.edit_email_img);
        edit_username_img = findViewById(R.id.edit_username_img);
        save_address_img = (ImageView) findViewById(R.id.save_address_img);
        address_et = (EditText) findViewById(R.id.address_et);
        gender_tv = (TextView) findViewById(R.id.gender_tv);
        email_tv = (TextView) findViewById(R.id.email_tv);
        username_et = findViewById(R.id.username_edt);
        name_tv = (TextView) findViewById(R.id.name_tv);
        qualification_et = (EditText) findViewById(R.id.qualification_et);
        t_shirt_size_et = (EditText) findViewById(R.id.t_shirt_size_et);
        personal_email_et = (EditText) findViewById(R.id.personal_email_et);
        trouser_size_et = (EditText) findViewById(R.id.trouser_size_et);
        whatsapp_no_et = (EditText) findViewById(R.id.whatsapp_no_et);
        adhar_no_et = (EditText) findViewById(R.id.adhar_no_et);
        edit_qualification_img = (ImageView) findViewById(R.id.edit_qualification_img);
        edit_whatsapp_no_img = (ImageView) findViewById(R.id.edit_whatsapp_no_img);
        edit_t_shirt_size_img = (ImageView) findViewById(R.id.edit_t_shirt_size_img);
        edit_trouser_size_img = (ImageView) findViewById(R.id.edit_trouser_size_img);
        edit_adhar_no_img = (ImageView) findViewById(R.id.edit_adhar_no_img);
        edit_personal_email_img = (ImageView) findViewById(R.id.edit_personal_email_img);
        save_details_btn = (Button) findViewById(R.id.save_details_btn);
        edit_city_img = (ImageView) findViewById(R.id.edit_city_img);
        edit_state_img = (ImageView) findViewById(R.id.edit_state_img);
        edit_district_img = (ImageView) findViewById(R.id.edit_district_img);
        edit_block_img = (ImageView) findViewById(R.id.edit_block_img);
        city_et = (EditText) findViewById(R.id.city_et);
        state_et = (EditText) findViewById(R.id.state_et);
        district_et = (EditText) findViewById(R.id.district_et);
        block_et = (EditText) findViewById(R.id.block_et);
        organisation_et = findViewById(R.id.organisation_et);
        position_et = findViewById(R.id.position_et);
        sportsPrefs1_et = findViewById(R.id.sports_prefs1_et);
        sportsPrefs2_et = findViewById(R.id.sports_prefs2_et);
        edit_organisation_img = findViewById(R.id.edit_organisation_img);
        edit_position_img = findViewById(R.id.edit_position_img);
        edit_sports_prefs1_img = findViewById(R.id.edit_sports_prefs1_img);
        edit_sports_prefs2_img = findViewById(R.id.edit_sports_prefs2_img);
        sportsPrefs2Label = findViewById(R.id.sports_prefs2_label);

        //    school_linearlayout = (LinearLayout) findViewById(R.id.school_linearlayout);
        Typeface font_semi_bold = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-SemiBold.ttf");
        save_details_btn.setTypeface(font_semi_bold);

//        if (selectedGender.equalsIgnoreCase("0")) {
//            gender_tv.setText("Male");
//        } else if (selectedGender.equalsIgnoreCase("1")) {
//            gender_tv.setText("Female");
//        }

        gender_tv.setText(selectedGender);

        email_tv.setText(email);
        username_et.setText(username);
        address_et.setText(address);
        phone_edt.setText(phone);
        name_tv.setText(username);
        qualification_et.setText(Qualification);
        t_shirt_size_et.setText(Tshirt_Size);
        personal_email_et.setText(Alternative_Email);
        trouser_size_et.setText(Trouser_Size);
        whatsapp_no_et.setText(Whatsup_No);
        adhar_no_et.setText(Aadhar_No);
        city_et.setText(sp.getString(AppConfig.CITY_NAME,""));

        district_et.setText(sp.getString(AppConfig.DISTRICT_NAME,""));
        block_et.setText(sp.getString(AppConfig.BLOCK_NAME,""));

        selectedStateId = sp.getInt(AppConfig.STATE_ID,0);
        selectedStateName = sp.getString(AppConfig.STATE_NAME,"");

        state_et.setText(selectedStateName);

        loadOrganisations();
        loadPositions();
        loadSportsPrefs1();
        loadSportsPrefs2();

        setEditTextEnableOrDisable(phone_edt, false);
        setEditTextEnableOrDisable(address_et, false);
        setEditTextEnableOrDisable(city_et,false);
        setEditTextEnableOrDisable(state_et,false);
        setEditTextEnableOrDisable(district_et,false);
        setEditTextEnableOrDisable(block_et,false);
        setEditTextEnableOrDisable(username_et,false);
        setEditTextEnableOrDisable(organisation_et,false);
        setEditTextEnableOrDisable(position_et,false);
        setEditTextEnableOrDisable(sportsPrefs1_et,false);
        setEditTextEnableOrDisable(sportsPrefs2_et,false);

        logout_tv.setOnClickListener(this);
        back_img.setOnClickListener(this);
        select_image_fab.setOnClickListener(this);
        edit_phone_img.setOnClickListener(this);
        save_phone_img.setOnClickListener(this);
        edit_address_img.setOnClickListener(this);
        save_address_img.setOnClickListener(this);
        edit_qualification_img.setOnClickListener(this);
        edit_whatsapp_no_img.setOnClickListener(this);
        edit_t_shirt_size_img.setOnClickListener(this);
        edit_trouser_size_img .setOnClickListener(this);
        edit_adhar_no_img.setOnClickListener(this);
        edit_personal_email_img.setOnClickListener(this);
        save_details_btn.setOnClickListener(this);
        edit_city_img.setOnClickListener(this);
        edit_state_img.setOnClickListener(this);
        edit_district_img.setOnClickListener(this);
        edit_block_img.setOnClickListener(this);
        edit_gender_img.setOnClickListener(this);
        edit_email_img.setOnClickListener(this);
        edit_username_img.setOnClickListener(this);
        edit_organisation_img.setOnClickListener(this);
        edit_position_img.setOnClickListener(this);
        edit_sports_prefs2_img.setOnClickListener(this);
        edit_sports_prefs1_img.setOnClickListener(this);

        try {
//            ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_SCHOOLS_MASTER, "", "", "", "");
//            Log.e("SchoolActivity", "school size==> " + objectArrayList.size());
            schoolList.clear();
        } catch (Exception e){
            e.printStackTrace();
        }

     /*   for (Object o : objectArrayList) {

            HashMap<String, String> map = new HashMap<String, String>();
            SchoolsMasterModel schoolModel = (SchoolsMasterModel) o;
            map.put("school_name", schoolModel.getSchool_name());
            map.put("school_id", "" + schoolModel.getSchool_id());

            schoolList.add(map);

            View child = getLayoutInflater().inflate(R.layout.activity_school_listitem, null);
            TextView school_name_tv;
            ImageView tick_img;
            school_name_tv = (TextView) child.findViewById(R.id.school_name_tv);
            tick_img = (ImageView) child.findViewById(R.id.tick_img);

            if (schoolModel.getSchool_id() == Integer.parseInt(Constant.SCHOOL_ID)) {
                tick_img.setVisibility(View.VISIBLE);
            } else {
                tick_img.setVisibility(View.GONE);
            }
            school_name_tv.setText(schoolModel.getSchool_name());
            school_linearlayout.addView(child);
        }*/
        personal_email_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text_entered = personal_email_et.getText().toString();
                if (!text_entered.isEmpty() && text_entered.contains(" ")) {
                    personal_email_et.setText(personal_email_et.getText().toString().replace(" ", ""));
                    personal_email_et.setSelection(personal_email_et.getText().length());
                }
            }
        });
    }

    public static boolean checkInternetConnectionn(Context context) {

        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // If permission not given in manifest,it will give error..so first add
        // internet permission to manifest file
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {

            return true;
        } else {
            return false;
        }

    }

    private void setEditTextEnableOrDisable(EditText et, boolean b) {
        et.setFocusable(b);
        et.setFocusableInTouchMode(b);
        et.setEnabled(b);
        et.setClickable(b);

        if (b) {
            et.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
            Drawable originalDrawable = et.getBackground();
            et.setBackgroundDrawable(originalDrawable);
            et.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            et.setBackgroundDrawable(null);
            et.setTextColor(Color.parseColor("#000000"));
           /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
           imm.showSoftInput(et, InputMethodManager.HIDE_IMPLICIT_ONLY);*/
        }
    }

    @Override
    public void onClick(View v) {
        if (v == back_img) {
            finish();
        }

        else if (v == logout_tv) {
            Utility.showLogoutDialog(this);
        }

        else if (v == select_image_fab) {
            selectImage();
        }

        else if (v == edit_phone_img) {
            setEditTextEnableOrDisable(phone_edt, true);
            edit_phone_img.setVisibility(View.GONE);
            save_details_btn.setVisibility(View.VISIBLE);
        }

        else if (v == save_phone_img) {
            phoneOrAddress = 1;
            //String phone = phone_edt.getText().toString().trim();
            String address = "";
            String Qualification = "";
            String Whatsup_No = "";
            String Tshirt_Size = "";
            String Trouser_Size = "";
            String Aadhar_No = "";
            String Alternative_Email = "";
            //CallSaveAPI(phone, address,Qualification,Whatsup_No,Tshirt_Size,Trouser_Size,Aadhar_No,Alternative_Email);

        }

        else if (v == edit_address_img) {
            setEditTextEnableOrDisable(address_et, true);
            save_details_btn.setVisibility(View.VISIBLE);
            edit_address_img.setVisibility(View.GONE);
        }

        else if (v == save_address_img) {
            phoneOrAddress = 2;
            String address = address_et.getText().toString().trim();
            String phone = "";
            String Qualification = "";
            String Whatsup_No = "";
            String Tshirt_Size = "";
            String Trouser_Size = "";
            String Aadhar_No = "";
            String Alternative_Email = "";
            //CallSaveAPI(phone, address,Qualification,Whatsup_No,Tshirt_Size,Trouser_Size,Aadhar_No,Alternative_Email);
        }

        else if (v == edit_qualification_img) {
            setEditTextEnableOrDisable(qualification_et, true);
            edit_qualification_img.setVisibility(View.GONE);
            save_details_btn.setVisibility(View.VISIBLE);
        }

        else if (v == edit_t_shirt_size_img) {
            setEditTextEnableOrDisable(t_shirt_size_et, true);
            edit_t_shirt_size_img.setVisibility(View.GONE);
            save_details_btn.setVisibility(View.VISIBLE);
        }

        else if (v == edit_trouser_size_img) {
            setEditTextEnableOrDisable(trouser_size_et, true);
            edit_trouser_size_img.setVisibility(View.GONE);
            save_details_btn.setVisibility(View.VISIBLE);
        }

        else if (v == edit_personal_email_img) {
            setEditTextEnableOrDisable(personal_email_et, true);
            edit_personal_email_img.setVisibility(View.GONE);
            save_details_btn.setVisibility(View.VISIBLE);
        }

        else if (v == edit_whatsapp_no_img) {
            setEditTextEnableOrDisable(whatsapp_no_et, true);
            edit_whatsapp_no_img.setVisibility(View.GONE);
            save_details_btn.setVisibility(View.VISIBLE);
        }

        else if (v == edit_adhar_no_img) {
            setEditTextEnableOrDisable(adhar_no_et, true);
            edit_adhar_no_img.setVisibility(View.GONE);
            save_details_btn.setVisibility(View.VISIBLE);
        }

        else if (v==edit_city_img){
            setEditTextEnableOrDisable(city_et, true);
            save_details_btn.setVisibility(View.VISIBLE);
            edit_city_img.setVisibility(View.GONE);
        }

        else if (v==edit_district_img){
            setEditTextEnableOrDisable(district_et, true);
            save_details_btn.setVisibility(View.VISIBLE);
            edit_district_img.setVisibility(View.GONE);
        }
        else if (v==edit_block_img){
            setEditTextEnableOrDisable(block_et, true);
            save_details_btn.setVisibility(View.VISIBLE);
            edit_block_img.setVisibility(View.GONE);
        }


        else if (v==edit_state_img){
//            setEditTextEnableOrDisable(address_et, true);
            save_details_btn.setVisibility(View.VISIBLE);
            if (areStatesLoaded)
                showStatesDialog();
            else
                getStates();

//            edit_address_img.setVisibility(View.GONE);

        }

        else if (v==edit_gender_img){
//            setEditTextEnableOrDisable(address_et, true);
            save_details_btn.setVisibility(View.VISIBLE);
            showGenderDialog();

//            edit_address_img.setVisibility(View.GONE);

        }

        else if (v == edit_username_img) {
            setEditTextEnableOrDisable(username_et, true);
            edit_username_img.setVisibility(View.GONE);
            save_details_btn.setVisibility(View.VISIBLE);
        }

        else if (v == edit_email_img) {
            resendOtp = false;
            showChangeEmailDialog();
//            setEditTextEnableOrDisable(username_et, true);
//            edit_username_img.setVisibility(View.GONE);
//            save_details_btn.setVisibility(View.VISIBLE);

        }

        else if (v == edit_organisation_img) {
            showOrganisationDialog();
            save_details_btn.setVisibility(View.VISIBLE);
        }
        else if (v == edit_position_img){
            showPositionDialog();
            save_details_btn.setVisibility(View.VISIBLE);
        }
        else if (v == edit_sports_prefs1_img){
            showSportsPrefs1Dialog();
            save_details_btn.setVisibility(View.VISIBLE);
        }
        else if (v == edit_sports_prefs2_img){
//            if (selectedPosition==null){
//                Toast.makeText(ctx, getString(R.string.are_you_a_pet_athlete_or_coach), Toast.LENGTH_SHORT).show();
//            }
//            else if (selectedPosition.getListItemId() == 4){
//                showSportsPrefs2Dialog();
//                save_details_btn.setVisibility(View.VISIBLE);
//            }
            if (selectedSportsPrefs1!=null) {
                showSportsPrefs2Dialog();
                save_details_btn.setVisibility(View.VISIBLE);
            }
            else
                Toast.makeText(ctx, "Please select Sports Preferences 1", Toast.LENGTH_SHORT).show();
        }

//        else if (v == organisation_)
////            showOrganisationDialog();
////        case R.id.position_et:
////        showPositionDialog();
////        break;
////        case R.id.sports_prefs1_et:
////        showSportsPrefs1Dialog();
////        break;
////        case R.id.sports_prefs2_et:
////        showSportsPrefs2Dialog();
////        break;
////


        else if(v==save_details_btn){

            String address = address_et.getText().toString().trim();
            String phone = phone_edt.getText().toString().trim();
            String Qualification = qualification_et.getText().toString().trim();
            String Whatsup_No = whatsapp_no_et.getText().toString().trim();
            String Tshirt_Size = t_shirt_size_et.getText().toString().trim();
            String Trouser_Size = trouser_size_et.getText().toString().trim();
            String Aadhar_No = adhar_no_et.getText().toString().trim();
            String Alternative_Email = personal_email_et.getText().toString().trim();
            String city = city_et.getText().toString().trim();
            String district = district_et.getText().toString().trim();
            String block = block_et.getText().toString().trim();
            String username = username_et.getText().toString().trim();

//            if(address.length()<1){
//                address = "";
//            }  if(phone.length()<1){
//                phone = "";
//            } if(Qualification.length()<1){
//                Qualification = "";
//            } if(Whatsup_No.length()<1){
//                Whatsup_No = "";
//            } if(Tshirt_Size.length()<1){
//                Tshirt_Size = "";
//            } if(Trouser_Size.length()<1){
//                Trouser_Size = "";
//            } if(Aadhar_No.length()<1){
//                Aadhar_No = "";
//            } if(Alternative_Email.length()<1){
//                Alternative_Email = "";
//            }
            if(username.length()<2){
                username_et.requestFocus();
                username_et.setError(getString(R.string.validate_username_not_empty));

            }
            else if (selectedGenderId==-1){
                Toast.makeText(ctx, getString(R.string.validate_gender_no_empty), Toast.LENGTH_LONG).show();
            }
            else if(phone.length()!=10){
                phone_edt.requestFocus();
                phone_edt.setError(getString(R.string.validate_phone_number_not_empty));
            }
            else if (selectedStateId==0){
//                state_et.requestFocus();
//                state_et.setError(getString(R.string.validate_state_not_empty));
                Toast.makeText(ctx, getString(R.string.validate_state_not_empty), Toast.LENGTH_LONG).show();
            }
            else if (selectedOrganisation==null){
                Toast.makeText(ctx, getString(R.string.validate_select_not_empty,"organisation"), Toast.LENGTH_LONG).show();
            }
            else if (selectedPosition==null){
                Toast.makeText(ctx, getString(R.string.are_you_a_pet_athlete_or_coach), Toast.LENGTH_LONG).show();
            }
            else if (district.length()<3 || !district.matches("[a-zA-Z ]+")){
                district_et.requestFocus();
                district_et.setError(getString(R.string.validate_string_not_empty,getString(R.string.district)));
            }
            else if (city.length()<3 || !city.matches("[a-zA-Z ]+")){
                city_et.requestFocus();
                city_et.setError(getString(R.string.validate_string_not_empty,getString(R.string.city)));
            }
            else if (block.length()<1 ){
                block_et.requestFocus();
                block_et.setError(getString(R.string.validate_string_not_empty,getString(R.string.block)));
            }
            else if(Qualification.length()<1){
                qualification_et.requestFocus();
                qualification_et.setError(getString(R.string.validate_qualification_not_empty));
            }
            else if (selectedSportsPrefs1==null){
                Toast.makeText(ctx, getString(R.string.validate_select_not_empty,"Sports Preferences 1"), Toast.LENGTH_LONG).show();
            }
            else if (selectedPosition.getListItemId()==4 && selectedSportsPrefs2==null){
                Toast.makeText(ctx,getString(R.string.validate_select_not_empty,"Sports Preferences 2"),Toast.LENGTH_LONG).show();
            }
//            else if(whatsapp_no_et.getText().toString().length()!=10){
//                whatsapp_no_et.requestFocus();
//                whatsapp_no_et.setError(getString(R.string.validate_string_not_empty,getString(R.string.whatsapp_no_label)));
//
//            }
//
//            else if(adhar_no_et.getText().toString().length()!=12){
//                adhar_no_et.requestFocus();
//                adhar_no_et.setError(getString(R.string.validate_string_not_empty,getString(R.string.aadhar_no_label)));
//
//            }
//            else if(Alternative_Email.length()<1){
//                personal_email_et.requestFocus();
//                personal_email_et.setError(getString(R.string.validate_email_id_not_empty));
//            }
//            else if(!Constant.verify(Alternative_Email,this)) {
//                personal_email_et.requestFocus();
//                personal_email_et.setError(getString(R.string.validate_email_id));
//
//            }else if( Whatsup_No.length()!=10){
//                whatsapp_no_et.requestFocus();
//                whatsapp_no_et.setError(getString(R.string.validate_whatsapp_not_empty));
//            }else if(Aadhar_No.length()!=12){
//                adhar_no_et.requestFocus();
//                adhar_no_et.setError(getString(R.string.validate_adhar_not_empty));
//            }
            else{
//                Toast.makeText(ctx, "Save data!", Toast.LENGTH_SHORT).show();
                CallSaveAPI(username,selectedGenderId,phone, address, Qualification, Whatsup_No, Tshirt_Size, Trouser_Size, Aadhar_No, Alternative_Email,city,district,block);
            }

        }
    }

    private void getStates(){
        ArrayList objectArrayList = DBManager.getInstance().getAllTableData(this,DBManager.TBL_STATE_MASTER,"","","","");
        if (objectArrayList.size()>0){
            boolean isStateSelected;
            for (Object object:objectArrayList){
                isStateSelected = false;
                ListViewDialogModel listViewDialogModel = (ListViewDialogModel) object;
                int stateId = listViewDialogModel.getListItemId();
                if (selectedStateId!=0 && selectedStateId == stateId)
                    isStateSelected = true;
                statesArrayList.add(new ListViewDialogModel(listViewDialogModel.getListItemId(),
                        listViewDialogModel.getListItemTitle(),isStateSelected));
            }
        }
        areStatesLoaded = true;
        showStatesDialog();
    }


    private void showStatesDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setCancelable(false);

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.list_view_pop_up_layout,null);
        builder.setView(view);
        statesListView = view.findViewById(R.id.list_view);
        statesListAdapter = new ListViewDialogAdapter(this,statesArrayList);
        statesListView.setAdapter(statesListAdapter);

        if (selectedStateId!=0){
            statesListView.setSelection(selectedStateId-1);
        }

        selectState();

        statesAlertDialog = builder.create();
        statesAlertDialog.show();
    }

    private void selectState(){
        statesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int j =0;j<statesArrayList.size();j++){
                    if (i == j){
                        if (!statesArrayList.get(i).isItemSelected()){
                            statesArrayList.get(i).setItemSelected(true);
                        }
                    }
                    else {
                        statesArrayList.get(j).setItemSelected(false);
                    }
                }

                statesListAdapter.notifyDataSetChanged();
                selectedStateName = statesArrayList.get(i).getListItemTitle();
                state_et.setText(selectedStateName);
                selectedStateId = statesArrayList.get(i).getListItemId();
                statesAlertDialog.dismiss();
            }
        });

    }



    private void showGenderDialog(){
        if (!isGenderLoaded){
            genderArrayList.add(new ListViewDialogModel(0,"Male",false));
            genderArrayList.add(new ListViewDialogModel(1,"Female",false));
            genderArrayList.add(new ListViewDialogModel(2,"Transgender",false));
            isGenderLoaded = true;
        }

        for (int i=0;i<genderArrayList.size();i++){
            ListViewDialogModel genderModel = genderArrayList.get(i);
            if (selectedGenderId!=-1 && selectedGenderId == genderModel.getListItemId()){
                genderModel.setItemSelected(true);
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setCancelable(false);

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.list_view_pop_up_layout,null);
        builder.setView(view);
        genderListView = view.findViewById(R.id.list_view);
        genderListAdapter = new ListViewDialogAdapter(this,genderArrayList);
        genderListView.setAdapter(genderListAdapter);

//        if (selectedStateId!=0){
//            statesListView.setSelection(selectedStateId-1);
//        }

        selectGender();

        genderAlertDialog = builder.create();
        genderAlertDialog.show();
    }

    private void selectGender(){
        genderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int j =0;j<genderArrayList.size();j++){
                    if (i == j){
                        if (!genderArrayList.get(i).isItemSelected()){
                            genderArrayList.get(i).setItemSelected(true);
                        }
                    }
                    else {
                        genderArrayList.get(j).setItemSelected(false);
                    }
                }

                genderListAdapter.notifyDataSetChanged();
                selectedGender = genderArrayList.get(i).getListItemTitle();
                gender_tv.setText(selectedGender);
                selectedGenderId = genderArrayList.get(i).getListItemId();
                genderAlertDialog.dismiss();
            }
        });

    }

    private void loadOrganisations(){
        if (organisationArrayList.size()==0) {
            organisationArrayList.add(new ListViewDialogModel(1, "Indian School", false));
            organisationArrayList.add(new ListViewDialogModel(2, "Foreign School", false));
            organisationArrayList.add(new ListViewDialogModel(3, "Academy", false));
            organisationArrayList.add(new ListViewDialogModel(4, "Community Coaching", false));
        }

        for (ListViewDialogModel organisation:organisationArrayList){
            if (organisation.getListItemId() == sp.getInt(AppConfig.ORGANIZATION,0)){
                organisation.setItemSelected(true);
                selectedOrganisation = organisation;
                organisation_et.setText(organisation.getListItemTitle());
            }
        }
    }

    private void showOrganisationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(false);
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.list_view_pop_up_layout, null);
        builder.setView(view);
        organisationListView = view.findViewById(R.id.list_view);
        organisationListAdapter = new ListViewDialogAdapter(this, organisationArrayList);
        organisationListView.setAdapter(organisationListAdapter);

        if (selectedOrganisation != null) {
            organisationListView.setSelection(selectedOrganisation.getListItemId() - 1);
        }

        selectOrganisation();

        organisationDialog = builder.create();
        organisationDialog.show();
    }

    private void selectOrganisation() {
        organisationListView.setOnItemClickListener((adapterView, view, i, l) -> {
            for (int j = 0; j < organisationArrayList.size(); j++) {
                if (i == j) {
                    if (!organisationArrayList.get(i).isItemSelected()) {
                        organisationArrayList.get(i).setItemSelected(true);
                    }
                } else {
                    organisationArrayList.get(j).setItemSelected(false);
                }
            }

            organisationListAdapter.notifyDataSetChanged();
            organisation_et.setText(organisationArrayList.get(i).getListItemTitle());
            selectedOrganisation = organisationArrayList.get(i);
            organisationDialog.dismiss();
        });

    }

    private void loadPositions(){
        if (positionArrayList.size()==0) {
            positionArrayList.add(new ListViewDialogModel(4, "PET", false));
            positionArrayList.add(new ListViewDialogModel(5, "Athlete", false));
            positionArrayList.add(new ListViewDialogModel(6, "Coach", false));
        }

        for (ListViewDialogModel position:positionArrayList){
            if (position.getListItemId() == sp.getInt(AppConfig.POSITION,0)){
                position.setItemSelected(true);
                selectedPosition = position;
                position_et.setText(position.getListItemTitle());
                if (selectedPosition.getListItemId()==4){
                    sportsPrefs2Label.setText(getString(R.string.sports_preferences_2));
                }
                else {
                    sportsPrefs2Label.setText(getString(R.string.sports_preferences_2_optional));
                }
            }
        }
    }

    private void showPositionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(false);
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.list_view_pop_up_layout, null);
        builder.setView(view);
        positionListView = view.findViewById(R.id.list_view);
        positionListAdapter = new ListViewDialogAdapter(this, positionArrayList);
        positionListView.setAdapter(positionListAdapter);

        if (selectedPosition != null) {
            positionListView.setSelection(selectedPosition.getListItemId() - 1);
        }

        selectPosition();

        positionDialog = builder.create();
        positionDialog.show();
    }

    private void selectPosition() {
        positionListView.setOnItemClickListener((adapterView, view, i, l) -> {
            for (int j = 0; j < positionArrayList.size(); j++) {
                if (i == j) {
                    if (!positionArrayList.get(i).isItemSelected()) {
                        positionArrayList.get(i).setItemSelected(true);
                    }
                } else {
                    positionArrayList.get(j).setItemSelected(false);
                }
            }

            positionListAdapter.notifyDataSetChanged();
            position_et.setText(positionArrayList.get(i).getListItemTitle());
            selectedPosition = positionArrayList.get(i);

            if (selectedPosition.getListItemId()==4){
                sportsPrefs2Label.setText(getString(R.string.sports_preferences_2));
            }
            else {
                sportsPrefs2Label.setText(getString(R.string.sports_preferences_2_optional));
            }

            positionDialog.dismiss();
        });

    }


    private void loadSportsPrefs1(){
        if (sportsPrefs1ArrayList.size()==0) {
            List<String> tempList = Arrays.asList(getResources().getStringArray(R.array.sports_array));
            for (int i = 0; i < tempList.size(); i++) {
                String sports = tempList.get(i);
                ListViewDialogModel sportsPrefs1Model = new ListViewDialogModel(i, tempList.get(i), false);
                if (sports.equalsIgnoreCase(sp.getString(AppConfig.SPORTS_PREFS1,""))){
                    sportsPrefs1Model.setItemSelected(true);
                    selectedSportsPrefs1 = sportsPrefs1Model;
                    sportsPrefs1_et.setText(selectedSportsPrefs1.getListItemTitle());
                }
                sportsPrefs1ArrayList.add(sportsPrefs1Model);
            }
        }
    }


    private void showSportsPrefs1Dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(false);
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.list_view_pop_up_layout, null);
        builder.setView(view);
        sportsPrefs1ListView = view.findViewById(R.id.list_view);
        sportsPrefs1ListAdapter = new ListViewDialogAdapter(this, sportsPrefs1ArrayList);
        sportsPrefs1ListView.setAdapter(sportsPrefs1ListAdapter);

        if (selectedSportsPrefs1 !=null) {
            sportsPrefs1ListView.setSelection(selectedSportsPrefs1.getListItemId() - 1);
        }

        selectSportsPrefs1();

        sportsPrefs1Dialog = builder.create();
        sportsPrefs1Dialog.show();
    }

    private void selectSportsPrefs1() {
        sportsPrefs1ListView.setOnItemClickListener((adapterView, view, i, l) -> {
            for (int j = 0; j < sportsPrefs1ArrayList.size(); j++) {
                if (i == j) {
                    if (!sportsPrefs1ArrayList.get(i).isItemSelected()) {
                        sportsPrefs1ArrayList.get(i).setItemSelected(true);
                    }
                } else {
                    sportsPrefs1ArrayList.get(j).setItemSelected(false);
                }
            }


            selectedSportsPrefs2 = null;
            sportsPrefs1ListAdapter.notifyDataSetChanged();
            sportsPrefs1_et.setText(sportsPrefs1ArrayList.get(i).getListItemTitle());
            selectedSportsPrefs1 = sportsPrefs1ArrayList.get(i);
            sportsPrefs2ArrayList.clear();
            loadSportsPrefs2();
            sportsPrefs2_et.setText("");
            sportsPrefs1Dialog.dismiss();
        });

    }

    private void loadSportsPrefs2(){
        if (sportsPrefs2ArrayList.size()==0) {
            List<String> tempList = Arrays.asList(getResources().getStringArray(R.array.sports_array));
            for (int i = 0; i < tempList.size(); i++) {
//                if (selectedSportsPrefs1.getListItemId()!=i)
//                    sportsPrefs2ArrayList.add(new ListViewDialogModel(i, tempList.get(i), false));
                String sports = tempList.get(i);
                ListViewDialogModel sportsPrefs2Model = new ListViewDialogModel(i, tempList.get(i), false);
                if (selectedSportsPrefs1!=null && sp.getString(AppConfig.SPORTS_PREFS1,"").equalsIgnoreCase(selectedSportsPrefs1.getListItemTitle())
                        && sports.equalsIgnoreCase(sp.getString(AppConfig.SPORTS_PREFS2,""))){
                    sportsPrefs2Model.setItemSelected(true);
                    selectedSportsPrefs2 = sportsPrefs2Model;
                    sportsPrefs2_et.setText(selectedSportsPrefs2.getListItemTitle());
                }
                if (selectedSportsPrefs1!=null && !selectedSportsPrefs1.getListItemTitle().equalsIgnoreCase(sports))
                    sportsPrefs2ArrayList.add(sportsPrefs2Model);
            }
        }

    }

    private void showSportsPrefs2Dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(false);
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.list_view_pop_up_layout, null);
        builder.setView(view);
        sportsPrefs2ListView = view.findViewById(R.id.list_view);
        sportsPrefs2ListAdapter = new ListViewDialogAdapter(this, sportsPrefs2ArrayList);
        sportsPrefs2ListView.setAdapter(sportsPrefs2ListAdapter);

        if (selectedSportsPrefs2 != null) {
            sportsPrefs2ListView.setSelection(selectedSportsPrefs2.getListItemId() - 1);
        }

        selectSportsPrefs2();

        sportsPrefs2Dialog = builder.create();
        sportsPrefs2Dialog.show();
    }

    private void selectSportsPrefs2() {
        sportsPrefs2ListView.setOnItemClickListener((adapterView, view, i, l) -> {
            for (int j = 0; j < sportsPrefs2ArrayList.size(); j++) {
                if (i == j) {
                    if (!sportsPrefs2ArrayList.get(i).isItemSelected()) {
                        sportsPrefs2ArrayList.get(i).setItemSelected(true);
                    }
                } else {
                    sportsPrefs2ArrayList.get(j).setItemSelected(false);
                }
            }

            sportsPrefs2ListAdapter.notifyDataSetChanged();
            sportsPrefs2_et.setText(sportsPrefs2ArrayList.get(i).getListItemTitle());
            selectedSportsPrefs2 = sportsPrefs2ArrayList.get(i);
            sportsPrefs2Dialog.dismiss();
        });

    }


    private void showChangeEmailDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setCancelable(false);

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.new_email_dialog,null);
        builder.setView(view);
        EditText email_edt = view.findViewById(R.id.email_edt);
        Button verify_btn = view.findViewById(R.id.verify_email_btn);

        verify_btn.setOnClickListener((v)-> {
            verifyEmailDialog.dismiss();
            newEmail = email_edt.getText().toString().trim();
            progressDialogUtility.showProgressDialog();
            VerifyEmailOTPRequest verifyEmailOTPRequest = new VerifyEmailOTPRequest(this,newEmail,resendOtp,this);
            verifyEmailOTPRequest.hitUserRequest();
           // Toast.makeText(ctx, email, Toast.LENGTH_SHORT).show();
            //showOTPDialog("");
        });

        verifyEmailDialog = builder.create();
        verifyEmailDialog.show();
    }

    private void showOTPDialog(final String otpFromServer) {

        otpDialog = new Dialog(this);
        otpDialog.setCancelable(true);
        otpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        otpDialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);

        Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(0);
        otpDialog.getWindow().setBackgroundDrawable(d);

        otpDialog.setContentView(R.layout.dialog_otp);


        TextView resend_otp_tv, text2, email_tv;
        Button submit_btn;

        resend_otp_tv = (TextView) otpDialog.findViewById(R.id.resend_otp_tv);
        otp_edt = (EditText) otpDialog.findViewById(R.id.otp_edt);
        text2 = (TextView) otpDialog.findViewById(R.id.text2);
        email_tv = (TextView) otpDialog.findViewById(R.id.email_tv);
        submit_btn = (Button) otpDialog.findViewById(R.id.submit_btn);

        resend_otp_tv.setTypeface(font_regular);
        otp_edt.setTypeface(font_regular);
        // otp_edt.setText(otp);
        text2.setTypeface(font_regular);
        email_tv.setTypeface(font_regular);
        submit_btn.setTypeface(font_regular);
        email_tv.setText(newEmail);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otp = otp_edt.getText().toString();
                if (otp.length() >0 && otp.equals(otpFromServer)) {
                    otpDialog.dismiss();
                    progressDialogUtility.showProgressDialog();
                    onlyForEmail = true;
                    UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest(ProfileActivity.this,newEmail,
                            ProfileActivity.this);
                    updateProfileRequest.hitUserRequest();
                    /*ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest(ForgotPasswordActivity.this, requestType, email, otp, new_password, ForgotPasswordActivity.this);
                    forgotPasswordRequest.hitUserRequest();*/
                } else {
                    otp_edt.setError(getString(R.string.enter_valid_otp));

                }
            }
        });


        resend_otp_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialogUtility.dismissProgressDialog();
                resendOtp = true;
                otp_edt.setText("");
                otpDialog.dismiss();
                VerifyEmailOTPRequest verifyEmailOTPRequest = new VerifyEmailOTPRequest(
                        ProfileActivity.this,newEmail,resendOtp,ProfileActivity.this);
                verifyEmailOTPRequest.hitUserRequest();
            }
        });
        otpDialog.show();
    }


    private void CallSaveAPI(String username,int gender,String phone, String address, String Qualification,String Whatsup_No,
                             String Tshirt_Size,String Trouser_Size,String Aadhar_No,String Alternative_Email,
                             String city, String district, String block) {
        connectionDetector = new ConnectionDetector(this);
        if (connectionDetector.isConnectingToInternet()) {

            //    db.dropNamedTable(this, db.TBL_LP_STUDENT_MASTER);
            //   db.createNamedTable(this, db.TBL_LP_STUDENT_MASTER);

            //progressDialogUtility.showProgressDialog();

            String sportsPrefs2 = "";
            if (selectedSportsPrefs2!=null){
                sportsPrefs2 = selectedSportsPrefs2.getListItemTitle();
            }
            onlyForEmail = false;
            UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest(this, username,gender,phone, address, Qualification,
                    Whatsup_No, Tshirt_Size, Trouser_Size, Aadhar_No,
                    Alternative_Email,city,selectedStateId,district,block,
                    selectedOrganisation.getListItemId(),selectedPosition.getListItemId(),
                    selectedSportsPrefs1.getListItemTitle(),sportsPrefs2,this);
            updateProfileRequest.hitUserRequest();
        }
    }


    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        //final CharSequence[] options = {"Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Select or take a new Picture!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                /*    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    mUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);

                    startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);*/
                    if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                    }
                    else {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                    }

                } else if (options[item].equals("Choose from Gallery")) {

                    pickFromGallery();

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int RequestCode, int ResultCode, Intent Data) {
        super.onActivityResult(RequestCode, ResultCode, Data);

        // ******* Camera ********
        if (RequestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE
                && ResultCode == RESULT_OK) {

            try {
                // LoadCaptureImage(Data);

                Bitmap bitmap = (Bitmap) Data.getExtras().get("data");
                File file =  Utility.ConvertBitmapToFile(bitmap, ctx);
                // profile_img.setImageBitmap(bitmap);

                //Picasso.with(ProfileActivity.this).load(file).transform(new CircleTransformWhite()).into(profile_img);
                //       uploadImageToServer(file);

                uploadImage(file);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // ******* Gallery ********
        else if (RequestCode == REQUEST_SELECT_PICTURE && ResultCode == RESULT_OK) {

            //  mUri = Data.getData();

            //  LoadCaptureImage(Data);

            try {
                Uri uri = Data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // profile_img.setImageBitmap(bitmap);
                File file = Utility.ConvertBitmapToFile(bitmap, ctx);
//                rotateImage(
//                        bitmap,
//                        getOrientation(file.getAbsolutePath()),
//                        bitmap.getWidth(), bitmap.getHeight());
                //  Picasso.with(ProfileActivity.this).load(file).transform(new CircleTransformWhite()).into(profile_img);
                uploadImage(file);
            } catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    private void LoadCaptureImage(Intent data) {

        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(data.getData());

            // uploadImage(getBytes(is));


        } catch (Exception e) {
            e.printStackTrace();
        }



        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Log.e("ProfileActivity", "path=> " + mUri.getPath());

        try {
            mPhoto = getBitmapFromUri(mUri);

            Log.e("ProfileActivity", "mPhoto=> " + mPhoto);
            Log.e("ProfileActivity", "mUri=> " + mUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int height = mPhoto.getHeight();
        int width = mPhoto.getWidth();


        Log.e("ProfileActivity", "orientation==> " + getOrientation(mUri.getPath()));

        mPhoto = rotateImage(
                mPhoto,
                getOrientation(mUri.getPath()),
                width, height);

        getContentResolver().notifyChange(mUri, null);

        imageFile = new File(compressImage(bitmapToUriConverter(mPhoto).getPath()));


        int file_size = Integer.parseInt(String.valueOf(imageFile.length()/1024));

        Log.e("file_size==>", "" + file_size);

        Base64Image = encodeImage(mPhoto);

        String fileName = imageFile.getName();
        Log.e("Base64Image==>", "" + Base64Image);
        Picasso.get().load(imageFile).transform(new CircleTransformWhite()).into(profile_img);
        Log.e("imageFile==>", "" + imageFile);

        // File initialFile = new File("src/main/resources/sample.txt");
        try {
            targetStream = new FileInputStream(imageFile);
            Log.e(TAG,"targetStream==> "+targetStream.toString());
        } catch (Exception e){
            e.printStackTrace();
        }

      /*  File newFile = new File(sp.getString("test_coordinator_id","")+".jpg");
        imageFile.renameTo(newFile);

        Log.e(TAG,"rename imageFile==> "+imageFile);*/

        //  uploadImageToServer(imageFile);

        uploadImage(imageFile);

        // CallUploadImageAPI(imageFile, Base64Image, targetStream);


        //  uploadImage();

    /*    ProfileRequest profileRequest = new ProfileRequest(this,Constant.TEST_COORDINATOR_ID, sp.getString("user_type", ""),Base64Image, this);
        profileRequest.hitUserRequest();*/
    }

    private void uploadImage(File file) {
        Utility.rotateFabForward(this,select_image_fab);
        RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", Constant.TEST_COORDINATOR_ID+".jpg", fbody);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), sp.getString("test_coordinator_id",""));


        ApiRequest apiService =
                ApiClient.getClient(ProfileActivity.this).create(ApiRequest.class);
        Call<ProfileImageModel> call = apiService.postImage1(body, name);
        call.enqueue(new Callback<ProfileImageModel>() {
            @Override
            public void onResponse(Call<ProfileImageModel> call, Response<ProfileImageModel> response) {

                Log.e("responseee===> ","success "+response.toString());


                if (response.isSuccessful()) {

                    ProfileImageModel responseBody = response.body();
                    Log.e("on responseee===> ","success "+responseBody.getIsSuccess());
                    Log.e("on responseee===> ","message "+responseBody.getMessage());
                    String url=getString(R.string.image_base_url).substring(0,getString(R.string.image_base_url).length()-1)+responseBody.getMessage();
                    picasso.invalidate(url);
                    picasso.load(url).networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransformWhite()).into(profile_img, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Utility.rotateFabBackward(select_image_fab);
                        }

                        @Override
                        public void onError(Exception e) {
                            Utility.rotateFabBackward(select_image_fab);

                        }


                    });
                    SharedPreferences.Editor e = sp.edit();
                    e.putString("profile_pic_url",responseBody.getMessage());
                    e.commit();

                } else {
                    ProfileImageModel responseBody = response.body();
                    Log.e("on responseee===> ","success "+responseBody.getIsSuccess());
                    Log.e("on responseee===> ","message "+responseBody.getMessage());
                    Utility.rotateFabBackward(select_image_fab);
                }

            }

            @Override
            public void onFailure(Call<ProfileImageModel> call, Throwable t) {
                Log.e("on failure===> ","failure "+t.toString());
                Utility.rotateFabBackward(select_image_fab);
            }
        });
    }

    private String encodeImage(Bitmap mPhoto) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mPhoto.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        // String encImage = Base64.encodeToString(b, Base64.NO_WRAP);

        encImage = encImage.replaceAll("(?:\\r\\n|\\n\\r|\\n|\\r)", "");

        return encImage;
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            // mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constant.TEST_COORDINATOR_ID+ ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    void pickFromGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser
                (intent, "Select Picture"), REQUEST_SELECT_PICTURE);

    }

    protected Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    public Uri bitmapToUriConverter(Bitmap mBitmap) {
        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // Calculate inSampleSize

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
           /* Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, 450, 350,
                    true);*/
            File file = new File(this.getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");


            FileOutputStream out = this.openFileOutput(file.getName(),
                    Context.MODE_WORLD_READABLE);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = file.getAbsolutePath();
            File f = new File(realPath);
            uri = Uri.fromFile(f);

        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
        return uri;
    }

    public static int getOrientation(String imagePath) {
        int rotate = 0;
        try {
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Log.e("ProfileActivity", "***orientation*** " + orientation);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    Log.e("ProfileActivity", "***degree*** " + 270);
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    Log.e("ProfileActivity", "***degree*** " + 180);
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    Log.e("ProfileActivity", "***degree*** " + 90);
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    Log.e("ProfileActivity", "***degree***  undeifned");
                    rotate = 360;
                    break;
            }

            Log.v("", "Exif orientation: " + orientation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public Bitmap rotateImage(Bitmap bitmap, int angle, int width, int height) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);


        if (scaledBitmap != null) {
            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2,
                    new Paint(Paint.FILTER_BITMAP_FLAG));
        }


//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            if (scaledBitmap != null) {
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                        scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                        true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            if (scaledBitmap != null) {
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Fitness365/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }

    protected String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
            } else
                Utility.showPermissionDialog(this,getString(R.string.accept_permission,getString(R.string.camera)));
        }
    }
    @Override
    public void onResponse(Object obj) {
        Log.e("ProfileActivity", "obj===> " + obj);
        if (obj instanceof ProfileModel) {
            progressDialogUtility.dismissProgressDialog();
            ProfileModel model;
            model = (ProfileModel) obj;

            if (model.getIsSuccess().equalsIgnoreCase("true")) {

                save_details_btn.setVisibility(View.GONE);

                setEditTextEnableOrDisable(phone_edt, false);
                setEditTextEnableOrDisable(address_et, false);
                setEditTextEnableOrDisable(qualification_et, false);
                setEditTextEnableOrDisable(adhar_no_et, false);
                setEditTextEnableOrDisable(whatsapp_no_et, false);
                setEditTextEnableOrDisable(personal_email_et, false);
                setEditTextEnableOrDisable(t_shirt_size_et, false);
                setEditTextEnableOrDisable(trouser_size_et, false);
                setEditTextEnableOrDisable(city_et, false);
                setEditTextEnableOrDisable(state_et, false);
                setEditTextEnableOrDisable(district_et, false);
                setEditTextEnableOrDisable(block_et, false);
                setEditTextEnableOrDisable(username_et, false);

                edit_phone_img.setVisibility(View.VISIBLE);
                edit_address_img.setVisibility(View.VISIBLE);
                edit_qualification_img.setVisibility(View.VISIBLE);
                edit_adhar_no_img.setVisibility(View.VISIBLE);
                edit_whatsapp_no_img.setVisibility(View.VISIBLE);
                edit_personal_email_img.setVisibility(View.VISIBLE);
                edit_t_shirt_size_img.setVisibility(View.VISIBLE);
                edit_trouser_size_img.setVisibility(View.VISIBLE);
                edit_state_img.setVisibility(View.VISIBLE);
                edit_district_img.setVisibility(View.VISIBLE);
                edit_city_img.setVisibility(View.VISIBLE);
                edit_block_img.setVisibility(View.VISIBLE);
                edit_username_img.setVisibility(View.VISIBLE);


                SharedPreferences.Editor e = sp.edit();

                String address = address_et.getText().toString().trim();
                String phone = phone_edt.getText().toString().trim();
                String Qualification = qualification_et.getText().toString().trim();
                String Whatsup_No = whatsapp_no_et.getText().toString().trim();
                String Tshirt_Size = t_shirt_size_et.getText().toString().trim();
                String Trouser_Size = trouser_size_et.getText().toString().trim();
                String Aadhar_No = adhar_no_et.getText().toString().trim();
                String Alternative_Email = personal_email_et.getText().toString().trim();
                String username = username_et.getText().toString().trim();
                //String cityName = city_et

                if (onlyForEmail && newEmail.length()>0){
                    email_tv.setText(newEmail);
                    e.putString(AppConfig.EMAIL,newEmail);
                }

                e.putString(AppConfig.ADDRESS, address);
                if(phone.length()>0){
                    e.putString("phone", phone);
                }
                if(Qualification.length()>0){
                    e.putString("Qualification", Qualification);
                }
                e.putString("Whatsup_No", Whatsup_No);
                e.putString("Tshirt_Size", Tshirt_Size);
                e.putString("Trouser_Size", Trouser_Size);
                e.putString("Aadhar_No", Aadhar_No);
                e.putString("Alternative_Email", Alternative_Email);

                e.putString(AppConfig.CITY_NAME,city_et.getText().toString().trim());
                if(selectedStateId!=0) {
                    e.putInt(AppConfig.STATE_ID, selectedStateId);
                    e.putString(AppConfig.STATE_NAME, selectedStateName);
                }
                if (selectedGenderId!=-1) {
                    //e.putInt(AppConfig.GENDER, selectedGenderId);
                    e.putString(AppConfig.GENDER, selectedGender);
                }
                e.putString(AppConfig.DISTRICT_NAME,district_et.getText().toString().trim());
                e.putString(AppConfig.BLOCK_NAME,block_et.getText().toString().trim());

                e.putInt(AppConfig.ORGANIZATION,selectedOrganisation.getListItemId());
                e.putInt(AppConfig.POSITION,selectedPosition.getListItemId());
                e.putString(AppConfig.SPORTS_PREFS1,selectedSportsPrefs1.getListItemTitle());

                if (selectedSportsPrefs2!=null)
                    e.putString(AppConfig.SPORTS_PREFS2,selectedSportsPrefs2.getListItemTitle());

                if (username.length()>0){
                    e.putString(AppConfig.USER_NAME,username);
                }

                e.commit();

              /*  if (phoneOrAddress == 1) {
                    setEditTextEnableOrDisable(phone_edt, false);
                    save_phone_img.setVisibility(View.GONE);
                    edit_phone_img.setVisibility(View.VISIBLE);

                    SharedPreferences.Editor e = sp.edit();
                    e.putString("phone", phone_edt.getText().toString());
                    e.commit();
                } else if (phoneOrAddress == 2) {
                    setEditTextEnableOrDisable(address_et, false);
                    save_address_img.setVisibility(View.GONE);
                    edit_address_img.setVisibility(View.VISIBLE);

                    SharedPreferences.Editor e = sp.edit();
                    e.putString("address", address_et.getText().toString());
                    e.commit();
                }*/


              String msg = model.getMessage();
              if (sp.getString(AppConfig.LANGUAGE,"en").equals("hi"))
                  msg = model.getMessageH();
              Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();


            } else {
//                Toast.makeText(getApplicationContext(), "Something went wrong while updating profile. Please try again.", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), getString(R.string.unable_connect_server), Toast.LENGTH_SHORT).show();

            }

        } else if (obj instanceof ProfileImageModel) {
            progressDialogUtility.dismissProgressDialog();
            ProfileImageModel model;
            model = (ProfileImageModel) obj;

            if (model.getIsSuccess().equalsIgnoreCase("true")) {
                Toast.makeText(getApplicationContext(), model.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), model.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (obj instanceof VerifyEmailOTPModel){
            progressDialogUtility.dismissProgressDialog();
            VerifyEmailOTPModel verifyEmailOTPModel = (VerifyEmailOTPModel) obj;
            if (verifyEmailOTPModel.getIsSuccess().equalsIgnoreCase("true"))
                showOTPDialog(verifyEmailOTPModel.getOTP());
            else{
                String msg = verifyEmailOTPModel.getMessage();
                if (PreferenceManager.getDefaultSharedPreferences(this).getString(AppConfig.LANGUAGE,"en").equals("hi"))
                    msg = verifyEmailOTPModel.getMessageH();
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            }

        }
    }

   public void setGenderId(String gender){
       switch (gender) {
           case "Male":
               selectedGenderId = 0;
               break;
           case "Female":
               selectedGenderId = 1;
               break;
           case "Transgender":
               selectedGenderId = 2;
               break;
       }
   }


}