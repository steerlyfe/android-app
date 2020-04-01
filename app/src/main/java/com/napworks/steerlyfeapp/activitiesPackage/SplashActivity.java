package com.napworks.steerlyfeapp.activitiesPackage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.LoadingDialog;
import com.napworks.steerlyfeapp.firebasePackage.MyFirebaseInstanceIDService;
import com.napworks.steerlyfeapp.interfacePackage.StaticDataResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.CountryModel;
import com.napworks.steerlyfeapp.modelPackage.CategoryModel;
import com.napworks.steerlyfeapp.modelPackage.QuizQuestionsModel;
import com.napworks.steerlyfeapp.modelPackage.StaticDataMainModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SplashActivity extends AppCompatActivity implements View.OnClickListener, StaticDataResponseInterface {
    @BindView(R.id.signUp)
    TextView signUp;
    @BindView(R.id.login)
    TextView login;

    private String TAG = getClass().getSimpleName();
    SharedPreferences sharedPreferences;
    LoadingDialog loadingDialog;
    CommonMessageDialog commonMessageDialog;
    private String selected = MyConstants.LOGIN;

    DatabaseMethods databaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        loadingDialog = new LoadingDialog(this);
        databaseMethods = new DatabaseMethods(this);
        commonMessageDialog = new CommonMessageDialog(this);
        MyFirebaseInstanceIDService.getToken(this);
        CommonMethods.showLog(TAG, "Notification Token : " + sharedPreferences.getString(MyConstants.NOTIFICATION_TOKEN, ""));
        if (!TextUtils.isEmpty(sharedPreferences.getString(MyConstants.USER_ID, ""))) {
            CommonMethods.showLog(TAG, "Questions Status : " + sharedPreferences.getInt(MyConstants.QUESTION_SUBMITTED, 0));
            if (sharedPreferences.getInt(MyConstants.QUESTION_SUBMITTED, 0) == 1
                    || sharedPreferences.getInt(MyConstants.QUESTION_SUBMITTED, 0) == 2) {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtra(MyConstants.CALLED_FROM, "");
                startActivity(i);
                finishAffinity();
                overridePendingTransition(0, 0);
            } else {
                Intent i = new Intent(SplashActivity.this, QuizQuestionsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                finishAffinity();
                overridePendingTransition(0, 0);
            }
        } else {
            if (databaseMethods.getAllCategoryCount() > 0) {
                CommonMethods.showLog(TAG, "Data Exists");

            } else {
                loadingDialog.showDialog();
                CommonMethods.showLog(TAG, "Data Not Exists");
                CommonWebServices.getStaticData(this);
            }
        }

        login.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUp:
                selected = MyConstants.SIGN_UP;
                Intent intent = new Intent(this, CommonLoginSignUpActivity.class);
                intent.putExtra(MyConstants.TYPE, selected);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;

            case R.id.login:
                selected = MyConstants.LOGIN;
                intent = new Intent(this, CommonLoginSignUpActivity.class);
                intent.putExtra(MyConstants.TYPE, selected);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
        }
    }

    /*
     * Static Api Response
     */
    @Override
    public void getStaticDataResponse(String status, StaticDataMainModel staticDataMainModel) {
        loadingDialog.hideDialog();
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (staticDataMainModel.getStatus() != null) {
                    if (staticDataMainModel.getStatus().equals("1")) {
                        CommonMethods.showLog(TAG, "Static Data Api Success");
                        DatabaseMethods databaseMethods = new DatabaseMethods(this);
                        databaseMethods.deleteAllCategories();
                        databaseMethods.deleteAllCountries();
                        databaseMethods.deleteAllQuestions();
//                        databaseMethods.deleteAllQuestionOptions();

                        for (CategoryModel categoryModel : staticDataMainModel.getCategoriesList()) {
                            databaseMethods.insertNewCategory(categoryModel);
                        }
                        for (CountryModel countryModel : staticDataMainModel.getCountriesList()) {
                            databaseMethods.insertNewCountry(countryModel);
                        }
                        for (QuizQuestionsModel quizQuestionsModel : staticDataMainModel.getQuizQuestions()) {
                            databaseMethods.insertQuestions(quizQuestionsModel);
                            databaseMethods.insertQuestionOptions(quizQuestionsModel.getQuestion_id(), quizQuestionsModel.getOptions_list(), "0");
                        }
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
}
