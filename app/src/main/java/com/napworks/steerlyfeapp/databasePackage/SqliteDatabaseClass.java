package com.napworks.steerlyfeapp.databasePackage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;


public class SqliteDatabaseClass extends SQLiteOpenHelper {

    private static int DATABASE_VERSION = 15;
    private String TAG = getClass().getSimpleName();

    public SqliteDatabaseClass(Context context) {
        super(context, context.getPackageName(), null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            String countryTable = "CREATE TABLE IF NOT EXISTS " + MyConstants.COUNTRY_TABLE
                    + " ( " + MyConstants.COUNTRY_ID + " TEXT, "
                    + MyConstants.COUNTRY_NAME + " TEXT, "
                    + MyConstants.COUNTRY_SHORT_CODE + " TEXT, "
                    + MyConstants.COUNTRY_LONG_CODE + " TEXT, "
                    + MyConstants.COUNTRY_CALLING_CODE + " TEXT, "
                    + MyConstants.COUNTRY_TIME_ZONE + " TEXT )";
            db.execSQL(countryTable);

            String productTable = "CREATE TABLE IF NOT EXISTS " + MyConstants.PRODUCT_TABLE
                    + " ( " + MyConstants.PRODUCT_ID + " TEXT, "
                    + MyConstants.PRODUCT_NAME + " TEXT, "
                    + MyConstants.CART_VALUE + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                    + MyConstants.PRODUCT_STORE_ID + " TEXT, "
                    + MyConstants.PRODUCT_DESCRIPTION + " TEXT, "
                    + MyConstants.PRODUCT_CATEGORY_ID + " TEXT, "
                    + MyConstants.PRODUCT_ACTUAL_PRICE + " REAL, "
                    + MyConstants.PRODUCT_SALE_PRICE + " REAL, "
                    + MyConstants.PRODUCT_QUANTITY + " REAL, "
                    + MyConstants.PRODUCT_ADDITIONAL_FEATURES + " TEXT, "
                    + MyConstants.PRODUCT_ADDITIONAL_FEATURES_PRICE + " REAL, "
                    + MyConstants.PRODUCT_AVAILABILITY_TYPE + " TEXT, "
                    + MyConstants.PRODUCT_AVAILABILITY_PRICE + " REAL, "
                    + MyConstants.SUB_STORE_ID + " TEXT, "
                    + MyConstants.SELLER_NAME + " TEXT, "
                    + MyConstants.SELLER_ADDRESS + " TEXT, "
                    + MyConstants.PRODUCT_IMAGE + " TEXT )";
            db.execSQL(productTable);

            String favouriteTable = "CREATE TABLE IF NOT EXISTS " + MyConstants.FAVOURITE_TABLE
                    + " ( " + MyConstants.PRODUCT_ID + " TEXT, "
                    + MyConstants.PRODUCT_NAME + " TEXT, "
                    + MyConstants.CART_VALUE + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                    + MyConstants.PRODUCT_STORE_ID + " TEXT, "
                    + MyConstants.PRODUCT_DESCRIPTION + " TEXT, "
                    + MyConstants.PRODUCT_CATEGORY_ID + " TEXT, "
                    + MyConstants.PRODUCT_ACTUAL_PRICE + " REAL, "
                    + MyConstants.PRODUCT_SALE_PRICE + " REAL, "
                    + MyConstants.PRODUCT_QUANTITY + " REAL, "
                    + MyConstants.PRODUCT_IMAGE + " TEXT )";
            db.execSQL(favouriteTable);

            String categoryTable = "CREATE TABLE IF NOT EXISTS " + MyConstants.CATEGORY_TABLE
                    + " ( " + MyConstants.CATEGORY_ID + " TEXT, "
                    + MyConstants.CATEGORY_NAME + " TEXT, "
                    + MyConstants.CATEGORY_URL + " TEXT )";

            db.execSQL(categoryTable);

            String questionsTable = "CREATE TABLE IF NOT EXISTS " + MyConstants.QUESTION_TABLE
                    + " ( " + MyConstants.QUESTION_ID + " TEXT, "
                    + MyConstants.QUESTION_PUBLIC_ID + " TEXT, "
                    + MyConstants.QUESTION + " TEXT) ";
            db.execSQL(questionsTable);


            String questionOptionsTable = "CREATE TABLE IF NOT EXISTS " + MyConstants.QUESTION_OPTIONS_TABLE
                    + " ( " + MyConstants.QUESTION_ID + " TEXT, "
                    + MyConstants.OPTION_STATUS + " TEXT, "
                    + MyConstants.QUESTION_OPTIONS + " TEXT) ";
            db.execSQL(questionOptionsTable);


        } catch (Exception e) {
            CommonMethods.showLog(TAG, "onCreate Exception " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
//            db.execSQL("DROP TABLE IF EXISTS " + MyConstants.COUNTRY_TABLE);
//            db.execSQL("DROP TABLE IF EXISTS " + MyConstants.CATEGORY_TABLE);
            onCreate(db);
        } catch (Exception e) {
            CommonMethods.showLog(TAG, "onUpgrade Exception " + e.getMessage());
        }
    }
}
