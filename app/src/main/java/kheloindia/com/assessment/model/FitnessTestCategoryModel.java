package kheloindia.com.assessment.model;

import java.util.Date;

import kheloindia.com.assessment.functions.Constant;

/**
 * Created by CT13 on 2017-06-02.
 */

public class FitnessTestCategoryModel {


    private int test_id;
    private String test_name;
    private String test_nameH;
    private String score_criteria;
    private String score_measurement;
    private String score_unit;
    private String name;
    private String test_description;
    private String test_descriptionH;
    private String purpose;
    private String purposeH;
    private String Equipment_Required;
    private String Administrative_Suggestions;
    private String scoring;
    private String Equipment_RequiredH;
    private String Administrative_SuggestionsH;
    private String scoringH;
    private int MultipleLane;
    private int TimerType;
    private int FinalPosition;
    private String VideoLink;
    private String VideoLinkH;

    public String getVideoLink() {
        return VideoLink;
    }

    public void setVideoLink(String videoLink) {
        VideoLink = videoLink;
    }

    public int getMultipleLane() {
        return MultipleLane;
    }

    public void setMultipleLane(int multipleLane) {
        MultipleLane = multipleLane;
    }

    public int getTimerType() {
        return TimerType;
    }

    public void setTimerType(int timerType) {
        TimerType = timerType;
    }

    public String getTest_applicable() {
        return tests_applicable;
    }

    public void setTest_applicable(String test_applicable) {
        this.tests_applicable = test_applicable;
    }

    private String tests_applicable;

    public Date getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        Date dateTime = Constant.ConvertStringTODate(created_on);
        this.created_on = dateTime;
    }

    public Date getLast_modified_on() {
        return last_modified_on;
    }

    public void setLast_modified_on(String last_modified_on) {
        Date dateTime = Constant.ConvertStringTODate(last_modified_on);
        this.last_modified_on = dateTime;
    }

    private Date created_on;
    private Date last_modified_on;

    public String getEquipment_Required() {
        return Equipment_Required;
    }

    public void setEquipment_Required(String equipment_Required) {
        Equipment_Required = equipment_Required;
    }

    public String getAdministrative_Suggestions() {
        return Administrative_Suggestions;
    }

    public void setAdministrative_Suggestions(String administrative_Suggestions) {
        Administrative_Suggestions = administrative_Suggestions;
    }

    public String getScoring() {
        return scoring;
    }

    public void setScoring(String scoring) {
        this.scoring = scoring;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }


    public String getTest_description() {
        return test_description;
    }

    public void setTest_description(String test_description) {
        this.test_description = test_description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int sub_test_id;

    public int getTest_id() {
        return test_id;
    }

    public void setTest_id(int test_id) {
        this.test_id = test_id;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public String getScore_criteria() {
        return score_criteria;
    }

    public void setScore_criteria(String score_criteria) {
        this.score_criteria = score_criteria;
    }

    public String getScore_measurement() {
        return score_measurement;
    }

    public void setScore_measurement(String score_measurement) {
        this.score_measurement = score_measurement;
    }

    public String getScore_unit() {
        return score_unit;
    }

    public void setScore_unit(String score_unit) {
        this.score_unit = score_unit;
    }

    public int getSub_test_id() {
        return sub_test_id;
    }

    public void setSub_test_id(int sub_test_id) {
        this.sub_test_id = sub_test_id;
    }

    public int getFinalPosition() {
        return FinalPosition;
    }

    public void setFinalPosition(int finalPosition) {
        FinalPosition = finalPosition;
    }

    public String getTest_nameH() {
        return test_nameH;
    }

    public void setTest_nameH(String test_nameH) {
        this.test_nameH = test_nameH;
    }

    public String getTest_descriptionH() {
        return test_descriptionH;
    }

    public void setTest_descriptionH(String test_descriptionH) {
        this.test_descriptionH = test_descriptionH;
    }

    public String getPurposeH() {
        return purposeH;
    }

    public void setPurposeH(String purposeH) {
        this.purposeH = purposeH;
    }

    public String getEquipment_RequiredH() {
        return Equipment_RequiredH;
    }

    public void setEquipment_RequiredH(String equipment_RequiredH) {
        Equipment_RequiredH = equipment_RequiredH;
    }

    public String getAdministrative_SuggestionsH() {
        return Administrative_SuggestionsH;
    }

    public void setAdministrative_SuggestionsH(String administrative_SuggestionsH) {
        Administrative_SuggestionsH = administrative_SuggestionsH;
    }

    public String getScoringH() {
        return scoringH;
    }

    public void setScoringH(String scoringH) {
        this.scoringH = scoringH;
    }

    public String getVideoLinkH() {
        return VideoLinkH;
    }

    public void setVideoLinkH(String videoLinkH) {
        VideoLinkH = videoLinkH;
    }
}
