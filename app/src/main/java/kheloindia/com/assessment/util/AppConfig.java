package kheloindia.com.assessment.util;

import java.security.PublicKey;

import kheloindia.com.assessment.functions.Constant;

public class AppConfig {

    // COMMON STRINGS
    public static final String YOUTUBE_API_KEY = "AIzaSyABk_T7991L2avBm_RgYOkpHTFUEhPMZMg";
    public static final String TAG = "check";
    public static final String IS_APP_UPDATED = "isAppUpdated";
    public static boolean IS_STATE_UPDATION_DIALOG_SHOWING = false;
    public static boolean IS_SCHOOL_DEACTIVATED_DIALOG_SHOWING = false;
    public static final String LANGUAGE = "lan";
    public static final String SELECTED_LANGUAGE = "SelectedLanguage";
    public static final String GOT_ALL_TEST = "got_all_test";
//    public static final String BASE_URL = "https://fitnessdata.kheloindia.gov.in/Service1.svc/";
    public static final String BASE_URL = "http://103.65.20.140:8082/Service1.svc/";
    public static final String IMAGE_BASE_URL = "https://fitnessdata.kheloindia.gov.in/";
//    public static final String IMAGE_BASE_URL = "http://103.65.20.140:8082/";
    public static final String NSRS_BASE_URL = "http://34.199.234.160:64/api/";
//    public static final String NSRS_BASE_URL = "http://www.nsrsapi.kheloindia.gov.in/api/";

    // API URL
    public static final String GET_COACH_LOGIN_TOKEN = NSRS_BASE_URL+"login/";
    public static final String LOGIN_COACH_URL =  "NSRS/CoachDetail";



    // FONT PATH STRINGS
    public static final String FONT_BASE_PATH = "fonts/";
    public static final String BARLOW_LIGHT_PATH = FONT_BASE_PATH + "Barlow-Light.ttf";
    public static final String BARLOW_MEDIUM_PATH = FONT_BASE_PATH + "Barlow-Medium.ttf";
    public static final String BARLOW_SEMIBOLD_PATH = FONT_BASE_PATH + "Barlow-SemiBold.ttf";
    public static final String BARLOW_CONDENSED_PATH = FONT_BASE_PATH + "BarlowCondensed-ExtraBoldItalic.otf";


    // FIREBASE STRINGS
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";

    // LOGIN STRINGS
    public static final String TEST_COORDINATOR_NAME = "test_coordinator_name";
    public static final String TEST_COORDINATOR_PASSWORD = "test_coordinator_password";
    public static final String PASSWORD = "password";
    public static final String IS_STUDENT_DETAILS_GET = "isStudentDetailGet";
    public static final String USER_ID = "userid";
    public static final String TEST_COORDINATOR_ID = "test_coordinator_id";
    public static final String USER_NAME = "username";
    public static final String EMAIL = "email";
    public static final String ORGANIZATION = "IsIndianSchool"; //1: Indian School,2: Foreign School,3: Academy,4: Community Coaching
    public static final String POSITION = "IsAthlete"; // 4: PET,5: Athlete,6 : Coach
    public static final String SPORTS_PREFS1 = "SportsPreference1";
    public static final String SPORTS_PREFS2 = "SportsPreference2";
    public static final String USER_TYPE = "usertype";
    public static final String USER_TYPE_SHARED_PREFS = "user_type";
    public static final String USER_TYPE_PET = "4";
    public static final String USER_TYPE_COACH = "13";
    public static final String COACH_ADMIN_USERNAME = "Coach_Admin";
    public static final String COACH_ADMIN_PASSWORD = "Coach@2020";



    // PROFILE STRINGS
    public static final String GENDER_ID = "gender";
    public static final String GENDER = "gender";
    public static final String GENDER_H = "genderH";

    // SCHOOL STRings
    public static final String SCHOOL_ID = "school_id";
    public static final String SCHOOL_NAME = "school_name";
    public static final String IS_ATTACHED = "IsActive";
    public static final String CURRENT_SCHOOL_ID = "current_school_id";

    // token strings
    public static final String TOKEN = "devicetoken";
    public static final String DEVICE_TYPE = "devicetype";

    // COORDINATOR STRINGS
    public static final String ADDRESS = "Address";
    public static final String IS_TOT_ATTENDED = "isattended";
    public static final String TOT_CODE = "totcode";


    // STUDENTS STRINGS
    public static final String STUDENT_ID = "student_id";
    public static final String TRAINER_ID = "trainer_id";
    public static final String CAMP_ID = "camp_id";
    public static final String CAMP_NAME = "CampName";
    //public static final String security_key = "IP84UTvzJKds1Jomx8gIbTXcEEJSUilGqpxCcmnx";
public static final String security_key = "IP84UTvzJKds1Jom";

    public static final String NAME_STRING = "name";
    public static final String CLASS_ID = "class_id";
    public static final String IS_RETEST_ALLOWED = "IsRetestAllowed";
    public static final String FOR_RETEST = "forRetest";

    // STATE STRINGS
    public static final String STATE_ID = "StateId";
    public static final String STATE_NAME = "StateName";
    public static final String CITY_NAME = "CityName";
    public static final String DISTRICT_NAME = "District";
    public static final String BLOCK_NAME = "Block";

    // TEST STRINGS
    public static final String EXPORTED = "exported";
    public static final String TEST_TYPE_ID = "test_type_id";
    public static final String SYNCED = "synced";
    public static final int HEIGHT_MIN_LIMIT=40;
    public static final int HEIGHT_MAX_LIMIT=250;
    public static final int WEIGHT_MIN_LIMIT=10;
    public static final int WEIGHT_MAX_LIMIT=500;
    public static final String IS_LOGIN ="isLogin";
    public static final String SCORE = "Score";
    public static final String HEIGHT_AND_WEIGHT = "Height & Weight";
    public static final String TEST_NAME_HINDI = "Test_NameH";
    public static final String TEST_DESC_HINDI = "Test_DescriptionH";
    public static final String PURPOSE_HINDI = "PurposeH";
    public static final String EQUIPMENT_REQ_HINDI = "Equipment_RequiredH";
    public static final String ADMINSTRATIVE_SUGGESTIONS_HINDI = "Administrative_SuggestionsH";
    public static final String SCORING_HINDI = "scoringH";
    public static final String VIDEO_LINK_HINDI = "VideoLinkH";
//     temp.setTest_descriptionH(cr.getString(cr.getColumnIndex(AppConfig.TEST_DESC_HINDI)));


    //URLS
    public static final String REGISTER_TOT_URL = "https://docs.google.com/forms/d/1apHfphffPv8UQcw_U2h9XOOuP6MkBNzKlsls04El6xM/viewform?ts=5c3c2875&edit_requested=true";
    public static final String SOP_URL = "https://schoolfitness.kheloindia.gov.in/UploadedFiles/SampleData/SOP";
    public static final String ECERTIFICATE_URL = "http://schoolfitness.kheloindia.gov.in:8080/";
    public static final String ADMIN_MANUAL_URL = "https://schoolfitness.kheloindia.gov.in/UploadedFiles/SampleData/AdminManual";

    public static final String GET_SCHOOLS_URL = "getSchools/";

    public static final String VERIFY_EMAIL_URL = "EmailOTPVerify/";

    public static final String UPDATE_TOKEN_URL = "UpdateUserDeviceToken/";


}
