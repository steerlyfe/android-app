package com.napworks.steerlyfeapp.designsPackage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.modelPackage.ReviewsModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;

import java.util.List;


public class CustomReviewsContainer {

    private final Activity activity;

    private final LinearLayout mainContainer;
    private List<ReviewsModel> reviewsList;
    private String TAG = getClass().getSimpleName();


    public CustomReviewsContainer(Activity activity, List<ReviewsModel> reviewsList, LinearLayout mainContainer) {
        this.activity = activity;
        this.reviewsList = reviewsList;
        this.mainContainer = mainContainer;

        setContainerDetail();
    }


    private void setContainerDetail() {

        for (int i = 0; i < reviewsList.size(); i++) {
            addDataInContainer(reviewsList.get(i).getImage_url(), i,
                    reviewsList.get(i).getFull_name(), reviewsList.get(i).getDescription(),
                    reviewsList.get(i).getRating(), reviewsList.get(i).getRating_time());

        }


    }


    private void addDataInContainer(final String image, final int position, final String name,
                                    final String description, final int rating, long timestamp) {
        if (!name.isEmpty()) {
            String finalName = "";
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View mainView = inflater.inflate(R.layout.container_reviews, mainContainer, false);
            ImageView imageView = mainView.findViewById(R.id.imageView);
            TextView nameText = mainView.findViewById(R.id.name);
            TextView imageNameText = mainView.findViewById(R.id.imageNameText);
            FrameLayout nameFrameLayout = mainView.findViewById(R.id.nameFrameLayout);
            TextView descriptionText = mainView.findViewById(R.id.description);
            RatingBar ratingBar = mainView.findViewById(R.id.ratingBar);
            nameText.setSelected(true);


            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.placeholder(R.drawable.logo);
//            requestOptions = requestOptions.error(R.drawable.logo_with_text_colored);

            if (image.equalsIgnoreCase("")) {
                nameFrameLayout.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                String[] separated = name.split(" ");
                CommonMethods.showLog(TAG, "ARRAY LENGTH : " + separated.length);
                if (separated.length > 1) {
                    finalName = separated[0].substring(0, 1) + separated[1].substring(0, 1);
                } else {
                    finalName = separated[0].substring(0, 1);
                }
                CommonMethods.showLog(TAG, "AB : " + finalName);
                imageNameText.setText(finalName);
            } else {
                nameFrameLayout.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                Glide.with(activity)
                        .asBitmap()
                        .load(image)
                        .apply(requestOptions)
                        .into(new CircularTransformation(activity, imageView));
            }

            nameText.setText(activity.getString(R.string.by).
                    concat("  ").
                    concat(name).
                    concat(" ").
                    concat(activity.getString(R.string.on)).
                    concat(" ").
                    concat(CommonMethods.getDate(timestamp)));
            descriptionText.setText(description);
            ratingBar.setRating(rating);
            mainContainer.addView(mainView);
        }


    }


}
