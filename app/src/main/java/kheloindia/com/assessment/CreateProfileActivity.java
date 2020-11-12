package kheloindia.com.assessment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kheloindia.com.assessment.adapter.ListViewDialogAdapter;
import kheloindia.com.assessment.model.CreateProfileModel;
import kheloindia.com.assessment.model.ListViewDialogModel;
import kheloindia.com.assessment.model.VerifyEmailOTPModel;
import kheloindia.com.assessment.util.AppConfig;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.util.Utility;
import kheloindia.com.assessment.webservice.CreateProfileRequest;

import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.webservice.VerifyEmailOTPRequest;

public class CreateProfileActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, ResponseListener, CompoundButton.OnCheckedChangeListener {
    private EditText name_et, email_et, phone_et, address_et, state_et, city_et, district_et, block_et,
            qualification_et, tot_code_et,organisation_et,position_et,sportsPrefs1_et,sportsPrefs2_et;
    private RadioGroup gender_rg;
    private RadioButton male_rb, female_rb, trans_gender_rb;
    private CheckBox terms_condition_cbx, above_eighteen_years_cbx, attended_tot_cbx;
    private Button save_details_btn;
    private Toolbar toolbar;
    private String name_text, email_text, phone_text, qualification_text, address_text, city_text, gender_text, district_txt, block_txt, tot_code_text = "";
    private String message;
    private ConnectionDetector connectionDetector;
    private ImageView info_iv;
    private LinearLayout tot_code_layout;
    ListView statesListView,sportsPrefs1ListView,sportsPrefs2ListView,organisationListView,positionListView;
    ListViewDialogAdapter statesListAdapter,sportsPrefs1ListAdapter,sportsPrefs2ListAdapter,
            organisationListAdapter,positionListAdapter;
    ArrayList<ListViewDialogModel> statesArrayList = new ArrayList<>();
    AlertDialog statesAlertDialog,organisationDialog,positionDialog,sportsPrefs1Dialog,sportsPrefs2Dialog;
    int selectedStateId = 0;
    ListViewDialogModel selectedSportsPrefs1,selectedSportsPrefs2,selectedOrganisation,selectedPosition;
    String selectedStateName = "";
    private ProgressDialogUtility progressDialogUtility;
    private Dialog otpDialog;
    private EditText otp_edt;
    private boolean resendOtp;
    ArrayList<ListViewDialogModel> sportsPrefs1ArrayList = new ArrayList<>();
    ArrayList<ListViewDialogModel> sportsPrefs2ArrayList = new ArrayList<>();
    ArrayList<ListViewDialogModel> organisationArrayList = new ArrayList<>();
    ArrayList<ListViewDialogModel> positionArrayList = new ArrayList<>();
    Spinner sportsPrefs1Spinner;
    TextView sportsPrefs2Label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        init();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void init() {
        progressDialogUtility = new ProgressDialogUtility(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("Create Profile");
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Utility.showActionBar(this, toolbar, "");
        TextView title = toolbar.findViewById(R.id.toolbar_title);
        title.setText(getString(R.string.create_profile));

        name_et = (EditText) findViewById(R.id.name_et);
        email_et = (EditText) findViewById(R.id.email_et);
        phone_et = (EditText) findViewById(R.id.phone_et);
        address_et = (EditText) findViewById(R.id.address_et);
        state_et = (EditText) findViewById(R.id.state_et);
        city_et = (EditText) findViewById(R.id.city_et);
        district_et = (EditText) findViewById(R.id.district_et);
        block_et = (EditText) findViewById(R.id.block_et);
        gender_rg = (RadioGroup) findViewById(R.id.gender_rg);
        male_rb = (RadioButton) findViewById(R.id.male_rb);
        female_rb = (RadioButton) findViewById(R.id.female_rb);
        trans_gender_rb = (RadioButton) findViewById(R.id.trans_gender_rb);
        qualification_et = (EditText) findViewById(R.id.qualification_et);
        save_details_btn = (Button) findViewById(R.id.save_details_btn);
        terms_condition_cbx = (CheckBox) findViewById(R.id.terms_condition_cbx);
        above_eighteen_years_cbx = (CheckBox) findViewById(R.id.above_eighteen_years_cbx);
        info_iv = (ImageView) findViewById(R.id.info_iv);
        attended_tot_cbx = (CheckBox) findViewById(R.id.attended_tot);
        tot_code_layout = (LinearLayout) findViewById(R.id.tot_code_layout);
        tot_code_et = (EditText) findViewById(R.id.tot_code);
        organisation_et = findViewById(R.id.organisation_et);
        position_et = findViewById(R.id.position_et);
        sportsPrefs1_et = findViewById(R.id.sports_prefs1_et);
        sportsPrefs2_et = findViewById(R.id.sports_prefs2_et);
        sportsPrefs2Label = findViewById(R.id.sports_prefs2_label);
        //stateLayout = (LinearLayout) findViewById(R.id.state_layout);
        //stateParentLayout = (LinearLayout) findViewById(R.id.state_parent_layout);
//        sportsPrefs1Spinner = (Spinner) findViewById(R.id.sports_prefs1_spinner);

        save_details_btn.setOnClickListener(this);
        info_iv.setOnClickListener(this);
        gender_rg.setOnCheckedChangeListener(this);
        state_et.setOnClickListener(this);
        attended_tot_cbx.setOnCheckedChangeListener(this);
        organisation_et.setOnClickListener(this);
        position_et.setOnClickListener(this);
        sportsPrefs1_et.setOnClickListener(this);
        sportsPrefs2_et.setOnClickListener(this);

        //stateParentLayout.setOnClickListener(this);
        //stateLayout.setOnClickListener(this);

        male_rb.setChecked(true);
        connectionDetector = new ConnectionDetector(this);
        getStates();


    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        int id = radioGroup.getCheckedRadioButtonId();

        switch (id) {

            case R.id.male_rb:
                gender_text = "0";
                break;

            case R.id.female_rb:
                gender_text = "1";
                break;

            case R.id.trans_gender_rb:
                gender_text = "2";
                break;

        }
    }

    private void getStates() {
        ArrayList objectArrayList = DBManager.getInstance().getAllTableData(this, DBManager.TBL_STATE_MASTER, "", "", "", "");
        if (objectArrayList.size() > 0) {
            for (Object object : objectArrayList) {
                ListViewDialogModel listViewDialogModel = (ListViewDialogModel) object;
                statesArrayList.add(new ListViewDialogModel(listViewDialogModel.getListItemId(), listViewDialogModel.getListItemTitle(), false));
            }
        }
    }


    private void showStatesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(false);
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.list_view_pop_up_layout, null);
        builder.setView(view);
        statesListView = view.findViewById(R.id.list_view);
        statesListAdapter = new ListViewDialogAdapter(this, statesArrayList);
        statesListView.setAdapter(statesListAdapter);

        if (selectedStateId != 0) {
            statesListView.setSelection(selectedStateId - 1);
        }

        selectState();

        statesAlertDialog = builder.create();
        statesAlertDialog.show();
    }

    private void selectState() {
        statesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int j = 0; j < statesArrayList.size(); j++) {
                    if (i == j) {
                        if (!statesArrayList.get(i).isItemSelected()) {
                            statesArrayList.get(i).setItemSelected(true);
                        }
                    } else {
                        statesArrayList.get(j).setItemSelected(false);
                    }
                }

                statesListAdapter.notifyDataSetChanged();
                state_et.setText(statesArrayList.get(i).getListItemTitle());
                selectedStateId = statesArrayList.get(i).getListItemId();
                statesAlertDialog.dismiss();
            }
        });

    }

    private void showOrganisationDialog(){
        if (organisationArrayList.size()==0) {
            organisationArrayList.add(new ListViewDialogModel(1, "Indian School", false));
            organisationArrayList.add(new ListViewDialogModel(2, "Foreign School", false));
            organisationArrayList.add(new ListViewDialogModel(3, "Academy", false));
            organisationArrayList.add(new ListViewDialogModel(4, "Community Coaching", false));
        }

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

    private void showPositionDialog(){
        if (positionArrayList.size()==0) {
            positionArrayList.add(new ListViewDialogModel(4, "PET", false));
            positionArrayList.add(new ListViewDialogModel(5, "Athlete", false));
            positionArrayList.add(new ListViewDialogModel(6, "Coach", false));
        }

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


    private void showSportsPrefs1Dialog(){
        if (sportsPrefs1ArrayList.size()==0) {
            List<String> tempList = Arrays.asList(getResources().getStringArray(R.array.sports_array));
            for (int i = 0; i < tempList.size(); i++) {
                sportsPrefs1ArrayList.add(new ListViewDialogModel(i, tempList.get(i), false));
            }
        }

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

            sportsPrefs2ArrayList.clear();
            sportsPrefs2_et.setText("");
            selectedSportsPrefs2 = null;
            sportsPrefs1ListAdapter.notifyDataSetChanged();
            sportsPrefs1_et.setText(sportsPrefs1ArrayList.get(i).getListItemTitle());
            selectedSportsPrefs1 = sportsPrefs1ArrayList.get(i);
            sportsPrefs1Dialog.dismiss();
        });

    }


    private void showSportsPrefs2Dialog(){
        if (sportsPrefs2ArrayList.size()==0) {
            List<String> tempList = Arrays.asList(getResources().getStringArray(R.array.sports_array));
            for (int i = 0; i < tempList.size(); i++) {
                if (selectedSportsPrefs1.getListItemId()!=i)
                    sportsPrefs2ArrayList.add(new ListViewDialogModel(i, tempList.get(i), false));
            }
        }

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


    private boolean validate() {

        name_text = name_et.getText().toString().trim();
        email_text = email_et.getText().toString().trim();
        phone_text = phone_et.getText().toString().trim();
        qualification_text = qualification_et.getText().toString().trim();
        //address_text=address_et.getText().toString().trim();
        city_text = city_et.getText().toString().trim();
        district_txt = district_et.getText().toString().trim();
        block_txt = block_et.getText().toString().trim();
        if (attended_tot_cbx.isChecked())
            tot_code_text = tot_code_et.getText().toString().trim();

        if (selectedOrganisation==null){
            Toast.makeText(this, getString(R.string.validate_select_not_empty,"organisation"), Toast.LENGTH_LONG).show();
            return false;
        }
        else if (selectedPosition==null){
            Toast.makeText(this, getString(R.string.are_you_a_pet_athlete_or_coach), Toast.LENGTH_LONG).show();
            return false;
        }
        else if (name_text.length() < 1 || !name_text.matches("[a-zA-Z ]+")) {
            name_et.requestFocus();
            name_et.setError(getString(R.string.validate_your_name));
            return false;
        } else if (email_text.length() < 1) {
            email_et.requestFocus();
            email_et.setError(getString(R.string.validate_email_id));
            return false;
        } else if (!Constant.verify(email_text, CreateProfileActivity.this)) {
            email_et.requestFocus();
            email_et.setError(getString(R.string.validate_email_id));
            return false;
        } else if (phone_text.length() != 10) {
            phone_et.requestFocus();
            phone_et.setError(getString(R.string.validate_phone_number_not_empty));
            return false;

        }
//        else if(address_text.length()<1){
//            address_et.requestFocus();
//            address_et.setError(getString(R.string.validate_address_not_empty));
//            return false;
//
//        }
        else if (selectedStateId == 0) {
//            state_et.requestFocus();
//            state_et.setError(getString(R.string.validate_state_not_empty));
            Toast.makeText(this, getString(R.string.validate_state_not_empty), Toast.LENGTH_LONG).show();
            return false;
        } else if (district_txt.length() < 3 || !district_txt.matches("[a-zA-Z ]+")) {
            district_et.requestFocus();
            district_et.setError(getString(R.string.validate_string_not_empty, getString(R.string.district)));
            return false;
        } else if (city_text.length() < 3 || !city_text.matches("[a-zA-Z ]+")) {
            city_et.requestFocus();
            city_et.setError(getString(R.string.validate_string_not_empty, getString(R.string.city)));
            return false;
        } else if (block_txt.length() < 1) {
            block_et.requestFocus();
            block_et.setError(getString(R.string.validate_string_not_empty, getString(R.string.block)));
            return false;
        } else if (qualification_text.length() < 1) {
            qualification_et.requestFocus();
            qualification_et.setError(getString(R.string.validate_qualification_not_empty));
            return false;

        } else if (selectedSportsPrefs1==null){
            Toast.makeText(this, getString(R.string.validate_select_not_empty,getString(R.string.sports_preferences_1)), Toast.LENGTH_LONG).show();
            return false;
        }
        else if (selectedPosition.getListItemId()==4 && selectedSportsPrefs2==null){
            Toast.makeText(this,getString(R.string.validate_select_not_empty,getString(R.string.sports_preferences_2)),Toast.LENGTH_LONG).show();
            return false;
        }
        else if (attended_tot_cbx.isChecked() && tot_code_text.length() < 1) {
            tot_code_et.requestFocus();
            tot_code_et.setError(getString(R.string.validate_string_not_empty, getString(R.string.tot_code)));
            return false;
        } else if (!terms_condition_cbx.isChecked() || !above_eighteen_years_cbx.isChecked()) {
            Toast.makeText(this, R.string.validate_terms_and_conditions, Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_details_btn:
                if (connectionDetector.isConnectingToInternet()) {
                    if (validate()) {
                        progressDialogUtility.dismissProgressDialog();
                        resendOtp = false;
                        VerifyEmailOTPRequest verifyEmailOTPRequest = new VerifyEmailOTPRequest(this,email_text,resendOtp,this);
                        verifyEmailOTPRequest.hitUserRequest();
                    }
                } else
                    Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show();

                break;

            case R.id.info_iv:
                startActivity(new Intent(this, WebViewPdf.class));
                break;

            case R.id.state_et:
                showStatesDialog();
                break;
            case R.id.organisation_et:
                showOrganisationDialog();
                break;
            case R.id.position_et:
                showPositionDialog();
                break;
            case R.id.sports_prefs1_et:
                showSportsPrefs1Dialog();
                break;
            case R.id.sports_prefs2_et:
                if (selectedSportsPrefs1!=null)
                    showSportsPrefs2Dialog();
                else
                    Toast.makeText(this, "Please select Sports Preferences 1", Toast.LENGTH_SHORT).show();
                break;

        }

    }



    @Override
    public void onResponse(Object obj) {
        if (obj instanceof CreateProfileModel) {
            CreateProfileModel createProfileModel = (CreateProfileModel) obj;
            if (createProfileModel.getIsSuccess().equalsIgnoreCase("true")) {
                message = getString(R.string.registration_success,createProfileModel.getResult());
                showDataSaveDialog(message, true);

            } else {
                // message="Registration failed as "+createProfileModel.getMessage();
                message = createProfileModel.getMessage();
                if (PreferenceManager.getDefaultSharedPreferences(this).getString(
                        AppConfig.LANGUAGE, "en").equals("hi"))
                    message = createProfileModel.getMessageH();
                showDataSaveDialog(message, false);
            }

            name_et.setText("");
            email_et.setText("");
            male_rb.setChecked(true);
            phone_et.setText("");
            address_et.setText("");
            city_et.setText("");
            district_et.setText("");
            block_et.setText("");
            state_et.setText("");
            tot_code_et.setText("");
            tot_code_layout.setVisibility(View.GONE);
            name_et.requestFocus();
            attended_tot_cbx.setChecked(false);
            terms_condition_cbx.setChecked(false);
            above_eighteen_years_cbx.setChecked(false);
            if (selectedStateId != 0) {
                statesArrayList.get(selectedStateId - 1).setItemSelected(false);
                statesListAdapter.notifyDataSetChanged();
                selectedStateId = 0;
            }
            qualification_et.setText("");
        }  else if (obj instanceof VerifyEmailOTPModel){
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
        else
            Toast.makeText(this, R.string.unable_connect_server, Toast.LENGTH_SHORT).show();
    }

    private void showOTPDialog(final String otpFromServer) {

        otpDialog = new Dialog(this);
        otpDialog.setCancelable(false);
        otpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        otpDialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);

        Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(0);
        otpDialog.getWindow().setBackgroundDrawable(d);

        otpDialog.setContentView(R.layout.dialog_otp);


        TextView resend_otp_tv, text2, email_tv;
        Button submit_btn;

        Typeface font_regular = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-Regular.ttf");
        Typeface font_medium = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-Medium.ttf");

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
        email_tv.setText(email_text);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otp = otp_edt.getText().toString();
                if (otp.length() >0 && otp.equals(otpFromServer)) {
                    otpDialog.dismiss();
                    progressDialogUtility.showProgressDialog();
                    String sportsPrefs2 = "";
                    if (selectedSportsPrefs2!=null){
                        sportsPrefs2 = selectedSportsPrefs2.getListItemTitle();
                    }
                    CreateProfileRequest createProfileRequest = new CreateProfileRequest
                            (CreateProfileActivity.this, name_text, email_text, gender_text, phone_text,
                                    address_et.getText().toString().trim(),
                                    qualification_text, city_text, selectedStateId, district_txt,
                                    block_txt, String.valueOf(attended_tot_cbx.isChecked()), tot_code_text,
                                    selectedOrganisation.getListItemId(),selectedPosition.getListItemId(),
                                    selectedSportsPrefs1.getListItemTitle(),sportsPrefs2
                                    ,CreateProfileActivity.this);
                    createProfileRequest.hitUserRequest();

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
                        CreateProfileActivity.this,email_text,resendOtp,CreateProfileActivity.this);
                verifyEmailOTPRequest.hitUserRequest();
//                resendOtp = true;
//                requestType = "0";
//                otp_edt.setText("");
//                otpDialog.dismiss();
//                ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest(ForgotPasswordActivity.this,
//                        requestType, email, "", new_password, ForgotPasswordActivity.this,resendOtp);
//                forgotPasswordRequest.hitUserRequest();
            }
        });
        otpDialog.show();
    }

    private void resetFields() {

    }


    private void showDataSaveDialog(String message, final boolean isRegistered) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(getString(R.string.registration_status));
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isRegistered)
                    CreateProfileActivity.super.onBackPressed();
            }
        });

        alertDialog.show();


    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == R.id.attended_tot) {
            if (b)
                tot_code_layout.setVisibility(View.VISIBLE);
            else {
                tot_code_layout.setVisibility(View.GONE);
                tot_code_et.setText("");
                tot_code_et.clearFocus();
                tot_code_et.setError(null);
                Utility.hideKeyboard(this);
            }
        }
    }
}
