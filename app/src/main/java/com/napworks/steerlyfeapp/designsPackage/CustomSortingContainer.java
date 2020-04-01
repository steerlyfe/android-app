package com.napworks.steerlyfeapp.designsPackage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.interfacePackage.OnFilterDialogInterface;
import com.napworks.steerlyfeapp.interfacePackage.OnOptionSelectedInterface;
import com.napworks.steerlyfeapp.modelPackage.CategoryModel;
import com.napworks.steerlyfeapp.modelPackage.QuizQuestionsModel;
import com.napworks.steerlyfeapp.modelPackage.SortingModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;

import java.util.List;


public class CustomSortingContainer {

    private final Activity activity;

    private final LinearLayout mainContainer;
    private final OnFilterDialogInterface onFilterDialogInterface;
    private List<SortingModel> sortingList;
    private String TAG = getClass().getSimpleName();
    private int previousPosition = 0;

    public CustomSortingContainer(Activity activity, List<SortingModel> sortingList, LinearLayout mainContainer, OnFilterDialogInterface onFilterDialogInterface) {
        this.activity = activity;
        this.sortingList = sortingList;
        this.mainContainer = mainContainer;
        this.onFilterDialogInterface = onFilterDialogInterface;
        setContainerDetail();
    }


    private void setContainerDetail() {
        for (int i = 0; i < sortingList.size(); i++) {
            addDataInContainer(sortingList.get(i).getTitle(), i, sortingList.get(i).getSortingValue(), sortingList.get(i).isSelected);
        }
    }


    private void addDataInContainer(final String titleText, final int position, String sortValue, boolean isSelected) {
        if (!titleText.isEmpty()) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View mainView = inflater.inflate(R.layout.container_sorting, mainContainer, false);
            TextView title = mainView.findViewById(R.id.title);
            ImageView tickImageView = mainView.findViewById(R.id.tickImageView);
            LinearLayout mainLayout = mainView.findViewById(R.id.mainLayout);
            title.setText(titleText);
            if (isSelected) {
                tickImageView.setVisibility(View.VISIBLE);
                previousPosition = position;
            } else {
                tickImageView.setVisibility(View.GONE);
            }
            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonMethods.showLog(TAG, "position : " + position);
                    CommonMethods.showLog(TAG, "previous Position : " + previousPosition);
                    CommonMethods.showLog(TAG, "Selected  : " + isSelected);

                    sortingList.get(previousPosition).setSelected(false);
                    sortingList.get(position).setSelected(true);
                    previousPosition = position;
                    mainContainer.removeAllViews();
                    setContainerDetail();
                    onFilterDialogInterface.OnFilterDialogSelected(sortingList, position);
                }
            });

            mainContainer.addView(mainView);
        }
    }
}
