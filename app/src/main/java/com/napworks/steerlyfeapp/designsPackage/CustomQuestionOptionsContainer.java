package com.napworks.steerlyfeapp.designsPackage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.interfacePackage.OnOptionSelectedInterface;
import com.napworks.steerlyfeapp.modelPackage.QuizQuestionsModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;

import java.util.List;


public class CustomQuestionOptionsContainer {

    private final Activity activity;

    private final LinearLayout mainContainer;
    private final OnOptionSelectedInterface onOptionSelectedInterface;
    private List<QuizQuestionsModel> optionsList;
    private String TAG = getClass().getSimpleName();
    List<String> selectedIndex;


    public CustomQuestionOptionsContainer(Activity activity, List<QuizQuestionsModel> optionsList, LinearLayout mainContainer,
                                          List<String> selectedIndex, OnOptionSelectedInterface onOptionSelectedInterface) {
        this.activity = activity;
        this.optionsList = optionsList;
        this.mainContainer = mainContainer;
        this.selectedIndex = selectedIndex;
        this.onOptionSelectedInterface = onOptionSelectedInterface;
        setContainerDetail();
    }


    private void setContainerDetail() {
        for (int i = 0; i < optionsList.size(); i++) {
            addDataInContainer(optionsList.get(i).getQuestion_public_id(), i, optionsList.get(i).getQuestion());
        }
    }


    private void addDataInContainer(final String optionText, final int position, String selected) {
        if (!optionText.isEmpty()) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View mainView = inflater.inflate(R.layout.container_quiz_options, mainContainer, false);
            TextView options = mainView.findViewById(R.id.options);
            CardView mainCardView = mainView.findViewById(R.id.mainCardView);
            options.setText(optionText);
            CommonMethods.showLog(TAG, "Selected : " + selected + " at : " + position);
            if (selected.equalsIgnoreCase("1")) {
                CommonMethods.showLog(TAG, "S position : " + position);
                mainCardView.setBackground(ContextCompat.getDrawable(activity, R.drawable.transparent_black_button_round_design));
                options.setTextColor(ContextCompat.getColor(activity, R.color.white));
            } else {
                mainCardView.setBackground(ContextCompat.getDrawable(activity, R.drawable.white_button_round_design));
                options.setTextColor(ContextCompat.getColor(activity, R.color.pure_black));

            }
            mainCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonMethods.showLog(TAG, "position : " + position);
                    CommonMethods.showLog(TAG, "Selected  : " + selected);
                    if (selected.equalsIgnoreCase("0")) {
                        optionsList.get(position).setQuestion("1");
                    } else {
                        optionsList.get(position).setQuestion("0");
                    }

//                    mainContainer.removeAllViews();
//                    setContainerDetail();

                    CommonMethods.showLog(TAG, "A Selected  : " + optionsList.get(position).getQuestion());
                    onOptionSelectedInterface.OnOptionSelected(optionsList, position);
                }
            });

            mainContainer.addView(mainView);
        }
    }
}
