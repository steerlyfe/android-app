package com.napworks.steerlyfeapp.activitiesPackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.designsPackage.CustomQuestionOptionsContainer;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.ConfirmationDialog;
import com.napworks.steerlyfeapp.dialogPackage.LoadingDialog;
import com.napworks.steerlyfeapp.interfacePackage.CommonStringResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.OnOptionSelectedInterface;
import com.napworks.steerlyfeapp.interfacePackage.ConfirmationDialogResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.QuizQuestionsResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.CommonStringModel;
import com.napworks.steerlyfeapp.modelPackage.QuizQuestionsModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizQuestionsActivity extends AppCompatActivity implements View.OnClickListener, OnOptionSelectedInterface, QuizQuestionsResponseInterface, ConfirmationDialogResponseInterface {
    private String TAG = getClass().getSimpleName();

    @BindView(R.id.optionsInflatedLayout)
    LinearLayout optionsInflatedLayout;
    @BindView(R.id.mainLinear)
    LinearLayout mainLinear;
    @BindView(R.id.toolbarTextView)
    TextView toolbarTextView;
    //    @BindView(R.id.currentQuestionNo)
//    TextView currentQuestionNo;
    @BindView(R.id.questionNo)
    TextView questionNo;
    @BindView(R.id.totalQuestions)
    TextView totalQuestions;
    @BindView(R.id.questionDesc)
    TextView questionDesc;
    @BindView(R.id.nextButton)
    TextView nextButton;
    @BindView(R.id.previousButton)
    TextView previousButton;
    @BindView(R.id.skipText)
    TextView skipText;


    private List<QuizQuestionsModel> questionsList;
    private List<QuizQuestionsModel> questionOptionsList;
    private List<QuizQuestionsModel> optionsList;
    List<String> selectedIndex;
    DatabaseMethods databaseMethods;
    private int position = 0;
    private int selectedPosition = 0;
    SharedPreferences sharedPreferences;
    LoadingDialog loadingDialog;
    CommonMessageDialog commonMessageDialog;
    ConfirmationDialog confirmationDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);
        ButterKnife.bind(this);
        questionsList = new ArrayList<>();
        questionOptionsList = new ArrayList<>();
        selectedIndex = new ArrayList<>();
        optionsList = new ArrayList<>();
        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        commonMessageDialog = new CommonMessageDialog(this);
        confirmationDialog = new ConfirmationDialog(this, this);
        loadingDialog = new LoadingDialog(this);
//        CommonMethods.toolbarInitialize(this, toolbar, getString(R.string.quizOptions), toolbarTextView);

        databaseMethods = new DatabaseMethods(this);
        questionsList.addAll(databaseMethods.getAllQuestions());
        CommonMethods.showLog(TAG, "Question List Size : " + questionsList.size());


        setData();
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        skipText.setOnClickListener(this);
    }


    /*
     * Set Data
     */

    private void setData() {

        questionOptionsList.clear();
        selectedIndex.clear();
        questionOptionsList.addAll(databaseMethods.getQuestionOptions());
        CommonMethods.showLog(TAG, "Question Options List Size : " + questionOptionsList.size());

        optionsList.clear();
        if (questionOptionsList.size() > 0) {
            CommonMethods.showLog(TAG, "Question Id : " + questionsList.get(position).getQuestion_id());
            CommonMethods.showLog(TAG, "Position : " + position);
            for (int i = 0; i < questionOptionsList.size(); i++) {
                if (questionsList.get(position).getQuestion_id().equalsIgnoreCase(questionOptionsList.get(i).getQuestion_id())) {
                    CommonMethods.showLog(TAG, "Match at : " + i);
                    CommonMethods.showLog(TAG, "Option Status :" + questionOptionsList.get(i).getQuestion() + " at : " + i);
                    optionsList.add(new QuizQuestionsModel(questionOptionsList.get(i).getQuestion_id(),
                            questionOptionsList.get(i).getQuestion_public_id(), questionOptionsList.get(i).getQuestion()));
                    if (questionOptionsList.get(i).getQuestion().equalsIgnoreCase("1")) {
                        selectedIndex.add(questionOptionsList.get(i).getQuestion());
                    }
                } else {
                    CommonMethods.showLog(TAG, "Not Match at : " + i);
                }
            }
            CommonMethods.showLog(TAG, "Selected Options List Size " + selectedIndex.size());

        }


        if (position == 0) {
            previousButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.VISIBLE);

        } else if (position == questionsList.size() - 1) {
            previousButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setText(getString(R.string.submitQuestions));
        } else {
            previousButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setText(getString(R.string.nextQuestion));
        }

//        currentQuestionNo.setText(String.valueOf(position + 1));
        questionNo.setText(String.valueOf(position + 1));
        totalQuestions.setText(String.valueOf(questionsList.size()));
        questionDesc.setText(questionsList.get(position).getQuestion());

//        optionsList.clear();
//        for (int i = 0; i < questionsList.get(position).getOptions_list().size(); i++) {
//                optionsList.add(new QuizQuestionsModel(questionsList.get(position).getOptions_list().get(i), questionsList.get(position).getOptionStatusList().get(i)));
//        }
        CommonMethods.showLog(TAG, "Option List Size : " + optionsList.size());


        if (optionsList.size() > 0) {
            optionsInflatedLayout.removeAllViews();
            new CustomQuestionOptionsContainer(this, optionsList, optionsInflatedLayout, selectedIndex, this);
        }
    }

    @Override
    public void OnOptionSelected(List<QuizQuestionsModel> selectedOptionsList, int position) {
        CommonMethods.showLog(TAG, "Position : " + position);
        CommonMethods.showLog(TAG, "Value At Position : " + selectedOptionsList.get(position).getQuestion_id());
        CommonMethods.showLog(TAG, "Text At Position : " + selectedOptionsList.get(position).getQuestion());
        CommonMethods.showLog(TAG, "Status At Position : " + selectedOptionsList.get(position).getQuestion_public_id());
        databaseMethods.updateQuestionOptions(selectedOptionsList.get(position).getQuestion_id(), selectedOptionsList.get(position).getQuestion_public_id());
//        databaseMethods.updateQuestionOptionsA(selectedOptionsList.get(position).getQuestion_public_id());
        setData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:
                if (questionsList.size() - 1 == position) {
                    CommonMethods.showLog(TAG, "Last Index");
                    confirmationDialog.showDialog(getString(R.string.submitQuestionsConfirmationText),
                            MyConstants.SUBMIT_QUESTIONS, getString(R.string.cancel), getString(R.string.submit));

//                    hitSubmitQuesApi("0", prepareJSON());
                } else {
                    if (selectedIndex.size() == 0) {
                        commonMessageDialog.showDialog(getString(R.string.pleaseChooseYourOption));
                    } else {
                        position = position + 1;
                        CommonMethods.showLog(TAG, "Position  : " + position);
                        setData();
                    }
                }
                break;
            case R.id.previousButton:
                if (position > 0) {
                    position = position - 1;
                    setData();
                } else {
                    CommonMethods.showLog(TAG, "Position less than 0");
                }
                break;
            case R.id.skipText:
                confirmationDialog.showDialog(getString(R.string.skipQuestionsConfirmationText),
                        MyConstants.SKIP_QUESTIONS, getString(R.string.cancel), getString(R.string.skip));
                break;
        }
    }


    /*
     * Submit Questions Api
     */

    private void hitSubmitQuesApi(String skipValue, String jsonValue) {
        loadingDialog.showDialog();
        CommonWebServices.submitQuizQuestions(sharedPreferences, skipValue, jsonValue, this);
    }



    /*
     * Method for prepare Json
     */
    public String prepareJSON() {
        String lastOption = "";
        JSONArray jsonArray = new JSONArray();
        JSONArray innerArray = new JSONArray();
        for (int i = 0; i < questionOptionsList.size(); i++) {
            if (questionOptionsList.get(i).getQuestion().equalsIgnoreCase("1")) {
                if (lastOption.equalsIgnoreCase(questionOptionsList.get(i).getQuestion_id())) {
                    innerArray.put(questionOptionsList.get(i).getQuestion_public_id());
                } else {
                    innerArray = new JSONArray();
                    innerArray.put(questionOptionsList.get(i).getQuestion_public_id());
                    JSONObject jsonObject1 = new JSONObject();
                    try {
                        jsonObject1.put("options", innerArray);
                        jsonObject1.put("question_id", questionOptionsList.get(i).getQuestion_id());
                        jsonArray.put(jsonObject1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    lastOption = questionOptionsList.get(i).getQuestion_id();
                }
            }
        }
        CommonMethods.showLog(TAG, "JSON ARRAY : " + jsonArray.toString());
        return jsonArray.toString();
    }


    /*
     * Quiz Questions Api Response
     */

    @Override
    public void getQuizQuestionsResponse(String status, CommonStringModel commonStringModel, String skipType) {
        loadingDialog.hideDialog();
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (commonStringModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "Submit Quiz Questions Api Status : " + commonStringModel.getStatus());
                    switch (commonStringModel.getStatus()) {
                        case "1":
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            if (skipType.equalsIgnoreCase("0")) {
                                editor.putInt(MyConstants.QUESTION_SUBMITTED, 1);
                            } else {
                                editor.putInt(MyConstants.QUESTION_SUBMITTED, 2);
                            }
                            editor.apply();
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra(MyConstants.CALLED_FROM, "");
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(intent);
                            break;
                        case "2":
                            CommonMethods.showLog(TAG, "Submit Quiz Questions Status 2 : " + commonStringModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "11":
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "0":
                            commonMessageDialog.showDialog(commonStringModel.getMessage());
                            break;
                        case "10":
                            CommonMethods.sessionOut(this);
                            break;
                    }
                } else {
                    commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                }
                break;
            case MyConstants.NULL_RESPONSE:
            case MyConstants.FAILURE_RESPONSE:
                commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                break;
        }
    }


    /*
     * Confirmation Dialog Response
     */


    @Override
    public void getConfirmationDialogResponse(String type) {
        if (type.equals(MyConstants.SUBMIT_QUESTIONS)) {
            hitSubmitQuesApi("0", prepareJSON());
        } else {
            hitSubmitQuesApi("1", "");
        }

    }
}
