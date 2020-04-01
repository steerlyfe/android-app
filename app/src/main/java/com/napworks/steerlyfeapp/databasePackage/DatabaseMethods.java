package com.napworks.steerlyfeapp.databasePackage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.napworks.steerlyfeapp.modelPackage.CountryModel;
import com.napworks.steerlyfeapp.modelPackage.CategoryModel;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.modelPackage.QuizQuestionsModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.ArrayList;
import java.util.List;

public class DatabaseMethods {

    private SQLiteDatabase database;
    private String TAG = getClass().getSimpleName();

    public DatabaseMethods(Context context) {
        try {
            SqliteDatabaseClass sqliteDatabaseClass = new SqliteDatabaseClass(context);
            database = sqliteDatabaseClass.getWritableDatabase();
        } catch (Exception e) {
            CommonMethods.showLog(TAG, "DatabaseMethods Exception " + e.getMessage());
        }
    }

    /*
     * Inser New Category
     */
    public long insertNewCategory(CategoryModel categoryModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyConstants.CATEGORY_ID, categoryModel.getCategoryId());
        contentValues.put(MyConstants.CATEGORY_NAME, categoryModel.getCategoryName());
        contentValues.put(MyConstants.CATEGORY_URL, categoryModel.getCategoryUrl());
        long result = database.insert(MyConstants.CATEGORY_TABLE, null, contentValues);
        CommonMethods.showLog(TAG, "insertNewCategory " + result);
        return result;
    }
    /*
     * Get  Category List
     */
    public List<CategoryModel> getAllCategoriesList() {
        List<CategoryModel> data = new ArrayList<>();
        Cursor cursor = database.query(MyConstants.CATEGORY_TABLE, null, null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                data.add(getCategoryDetail(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    /*
     * Get Category List Count
     */
    public int getAllCategoryCount() {
        Cursor cursor = database.query(MyConstants.CATEGORY_TABLE, null, null,
                null, null, null, null);
        int count = 0;
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    public ArrayList<String> getAllCategoriesName() {
        ArrayList<String> data = new ArrayList<>();
        Cursor cursor = database.query(MyConstants.CATEGORY_TABLE, new String[]{MyConstants.CATEGORY_NAME},
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                data.add(cursor.getString(cursor.getColumnIndex(MyConstants.CATEGORY_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    public String getCategoryId(String categoryName) {
        String id = "";
        Cursor cursor = database.query(MyConstants.CATEGORY_TABLE, new String[]{MyConstants.CATEGORY_ID},
                MyConstants.CATEGORY_NAME + "=?", new String[]{categoryName}, null, null, null);
        if (cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndex(MyConstants.CATEGORY_ID));
        }
        cursor.close();
        CommonMethods.showLog(TAG, "getCategoryId " + id);
        return id;
    }
    /*
     * Get Category Name
     */
    public String getCategoryName(String categoryId) {
        String name = "";
        Cursor cursor = database.query(MyConstants.CATEGORY_TABLE, new String[]{MyConstants.CATEGORY_NAME},
                MyConstants.CATEGORY_ID + "=?", new String[]{categoryId}, null, null, null);
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(MyConstants.CATEGORY_NAME));
        }
        cursor.close();
        CommonMethods.showLog(TAG, "getCategoryName " + name);
        return name;
    }
    /*
     * Get Category Detail
     */
    private CategoryModel getCategoryDetail(Cursor cursor) {
        String categoryId = cursor.getString(cursor.getColumnIndex(MyConstants.CATEGORY_ID));
        String categoryName = cursor.getString(cursor.getColumnIndex(MyConstants.CATEGORY_NAME));
        String categoryUrl = cursor.getString(cursor.getColumnIndex(MyConstants.CATEGORY_URL));
        return new CategoryModel(categoryId, categoryName, categoryUrl);
    }

    /*
     * Delete all Category
     */
    public long deleteAllCategories() {
        long result = database.delete(MyConstants.CATEGORY_TABLE, null, null);
        CommonMethods.showLog(TAG, "deleteAllCategories " + result);
        return result;
    }

    /*
     * Insert Country to database
     */
    public long insertNewCountry(CountryModel countryModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyConstants.COUNTRY_ID, countryModel.getCountryId());
        contentValues.put(MyConstants.COUNTRY_NAME, countryModel.getCountryName());
        contentValues.put(MyConstants.COUNTRY_SHORT_CODE, countryModel.getShortCode());
        contentValues.put(MyConstants.COUNTRY_LONG_CODE, countryModel.getLongCode());
        contentValues.put(MyConstants.COUNTRY_CALLING_CODE, countryModel.getCallingCode());
        contentValues.put(MyConstants.COUNTRY_TIME_ZONE, countryModel.getTimeZone());
        long result = database.insert(MyConstants.COUNTRY_TABLE, null, contentValues);
        CommonMethods.showLog(TAG, "insertNewCountry " + result);
        return result;
    }
    /*
     * Delete Country List From Database
     */
    public long deleteAllCountries() {
        long result = database.delete(MyConstants.COUNTRY_TABLE, null, null);
        CommonMethods.showLog(TAG, "deleteAllCountries " + result);
        return result;
    }
    /*
     * Get All Countries List
     */
    public List<CountryModel> getAllCountriesList() {
        List<CountryModel> data = new ArrayList<>();
        Cursor cursor = database.query(MyConstants.COUNTRY_TABLE, null, null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                data.add(getCountryDetail(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }
    /*
     * Get Country Calling Code
     */
    public String getCountryCallingCode(String countryId) {
        String countryCallingCode = "";
        Cursor cursor = database.query(MyConstants.COUNTRY_TABLE, new String[]{MyConstants.COUNTRY_CALLING_CODE},
                MyConstants.COUNTRY_SHORT_CODE + "=?", new String[]{countryId}, null, null, null);
        if (cursor.moveToFirst()) {
            countryCallingCode = cursor.getString(cursor.getColumnIndex(MyConstants.COUNTRY_CALLING_CODE));
        }
        cursor.close();
        CommonMethods.showLog(TAG, "getCountryCallingCode " + countryCallingCode);
        return countryCallingCode;
    }
    /*
     * Get Country Name
     */
    public String getCountryName(String countryId) {
        String countryName = "";
        Cursor cursor = database.query(MyConstants.COUNTRY_TABLE, new String[]{MyConstants.COUNTRY_NAME},
                MyConstants.COUNTRY_SHORT_CODE + "=?", new String[]{countryId}, null, null, null);
        if (cursor.moveToFirst()) {
            countryName = cursor.getString(cursor.getColumnIndex(MyConstants.COUNTRY_NAME));
        }
        cursor.close();
        CommonMethods.showLog(TAG, "getCountryName " + countryName);
        return countryName;
    }
    /*
     *  Search Countries List
     */
    public List<CountryModel> getSearchCountriesList(String countryName) {
        List<CountryModel> data = new ArrayList<>();
        String query = "SELECT * FROM " + MyConstants.COUNTRY_TABLE + " WHERE "
                + MyConstants.COUNTRY_NAME +
                " LIKE '" + countryName + "%'";
        CommonMethods.showLog(TAG, "Query : " + query);
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                data.add(getCountryDetail(cursor));
            } while (cursor.moveToNext());
        }
        return data;
    }

    /*
     * Get Country Detail
     */
    private CountryModel getCountryDetail(Cursor cursor) {
        String countryId = cursor.getString(cursor.getColumnIndex(MyConstants.COUNTRY_ID));
        String countryName = cursor.getString(cursor.getColumnIndex(MyConstants.COUNTRY_NAME));
        String shortCode = cursor.getString(cursor.getColumnIndex(MyConstants.COUNTRY_SHORT_CODE));
        String longCode = cursor.getString(cursor.getColumnIndex(MyConstants.COUNTRY_LONG_CODE));
        String callingCode = cursor.getString(cursor.getColumnIndex(MyConstants.COUNTRY_CALLING_CODE));
        String timeZone = cursor.getString(cursor.getColumnIndex(MyConstants.COUNTRY_TIME_ZONE));
        return new CountryModel(countryId, countryName, shortCode, longCode, callingCode, timeZone);
    }


    /*
     * Insert Product to database
     */


    public long insertNewProduct(ProductDetailModel productDetailModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyConstants.PRODUCT_ID, productDetailModel.getProduct_id());
        contentValues.put(MyConstants.PRODUCT_NAME, productDetailModel.getProduct_name());
        contentValues.put(MyConstants.PRODUCT_STORE_ID, productDetailModel.getStore_id());
        contentValues.put(MyConstants.PRODUCT_CATEGORY_ID, productDetailModel.getCategory_id());
        contentValues.put(MyConstants.PRODUCT_DESCRIPTION, productDetailModel.getDescription());
        contentValues.put(MyConstants.PRODUCT_IMAGE, productDetailModel.getImage_url());
        contentValues.put(MyConstants.PRODUCT_SALE_PRICE, productDetailModel.getSale_price());
        contentValues.put(MyConstants.PRODUCT_ACTUAL_PRICE, productDetailModel.getActual_price());

        contentValues.put(MyConstants.PRODUCT_ADDITIONAL_FEATURES, productDetailModel.getAdditionalFeaturesType());
        contentValues.put(MyConstants.PRODUCT_ADDITIONAL_FEATURES_PRICE, productDetailModel.getAdditionalFeaturesPrice());
        contentValues.put(MyConstants.PRODUCT_AVAILABILITY_TYPE, productDetailModel.getProductAvailabilityType());
        contentValues.put(MyConstants.PRODUCT_AVAILABILITY_PRICE, productDetailModel.getProductAvailabilityPrice());
        contentValues.put(MyConstants.SUB_STORE_ID, productDetailModel.getSub_store_id());
        contentValues.put(MyConstants.SELLER_NAME, productDetailModel.getSeller_name());
        contentValues.put(MyConstants.SELLER_ADDRESS, productDetailModel.getSeller_address());
        contentValues.put(MyConstants.PRODUCT_QUANTITY, productDetailModel.getProduct_quantity());
        long result = database.insert(MyConstants.PRODUCT_TABLE, null, contentValues);
        CommonMethods.showLog(TAG, "insertNewProduct " + result);
        return result;
    }
    /*
     * Get All Products Quantity
     */
    public int getAllProductQuantity(String productId) {
        int quantity = 0, quantityValue = 0;
        Cursor cursor = database.query(MyConstants.PRODUCT_TABLE, null,
                MyConstants.PRODUCT_ID + "= ?", new String[]{productId}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                quantity = cursor.getInt(cursor.getColumnIndex(MyConstants.PRODUCT_QUANTITY));
                quantityValue = quantityValue + quantity;

            } while (cursor.moveToNext());
//            quantity = cursor.getInt(cursor.getColumnIndex(MyConstants.PRODUCT_QUANTITY));
        }
        cursor.close();
        return quantityValue;
    }
    /*
     * Get Product Quantity
     */
    public int getProductQuantity(String productId) {
        int quantity = 0;
        Cursor cursor = database.query(MyConstants.PRODUCT_TABLE, null,
                MyConstants.PRODUCT_ID + "= ?", new String[]{productId}, null, null, null);

        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(cursor.getColumnIndex(MyConstants.PRODUCT_QUANTITY));
        }
        cursor.close();
        return quantity;
    }

    public int getAllProductCount(String productId) {
        Cursor cursor = database.query(MyConstants.PRODUCT_TABLE, null,
                MyConstants.PRODUCT_ID + "=?", new String[]{productId}, null, null, null);
        int count = 0;
        count = cursor.getCount();
        cursor.close();
        return count;
    }
    /*
     * Get All Product List
     */
    public List<ProductDetailModel> getAllProductList() {
        List<ProductDetailModel> data = new ArrayList<>();
        Cursor cursor = database.query(MyConstants.PRODUCT_TABLE, null, null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                data.add(getProductDetail(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    /*
     * Get Product Detail
     */
    private ProductDetailModel getProductDetail(Cursor cursor) {
        String productId = cursor.getString(cursor.getColumnIndex(MyConstants.PRODUCT_ID));
        String productName = cursor.getString(cursor.getColumnIndex(MyConstants.PRODUCT_NAME));
        String productStoreId = cursor.getString(cursor.getColumnIndex(MyConstants.PRODUCT_STORE_ID));
        String productCategoryId = cursor.getString(cursor.getColumnIndex(MyConstants.PRODUCT_CATEGORY_ID));
        String productDescription = cursor.getString(cursor.getColumnIndex(MyConstants.PRODUCT_DESCRIPTION));
        double productActualPrice = cursor.getDouble(cursor.getColumnIndex(MyConstants.PRODUCT_ACTUAL_PRICE));
        String productImage = cursor.getString(cursor.getColumnIndex(MyConstants.PRODUCT_IMAGE));
        double productSalePrice = cursor.getDouble(cursor.getColumnIndex(MyConstants.PRODUCT_SALE_PRICE));
        String productAdditionalFeatures = cursor.getString(cursor.getColumnIndex(MyConstants.PRODUCT_ADDITIONAL_FEATURES));
        double productAdditionalFeaturesPrice = cursor.getDouble(cursor.getColumnIndex(MyConstants.PRODUCT_ADDITIONAL_FEATURES_PRICE));
        String productAvailabilityType = cursor.getString(cursor.getColumnIndex(MyConstants.PRODUCT_AVAILABILITY_TYPE));
        double productAvailabilityPrice = cursor.getDouble(cursor.getColumnIndex(MyConstants.PRODUCT_AVAILABILITY_PRICE));
        String productSubStoreId = cursor.getString(cursor.getColumnIndex(MyConstants.SUB_STORE_ID));
        String sellerName = cursor.getString(cursor.getColumnIndex(MyConstants.SELLER_NAME));
        String sellerAddress = cursor.getString(cursor.getColumnIndex(MyConstants.SELLER_ADDRESS));
        int productQuantity = cursor.getInt(cursor.getColumnIndex(MyConstants.PRODUCT_QUANTITY));
        return new ProductDetailModel(productId, productDescription, productCategoryId, productName,
                productStoreId, productActualPrice, productSalePrice, productQuantity, productImage, productAdditionalFeatures,
                productAdditionalFeaturesPrice, productAvailabilityType, productAvailabilityPrice, productSubStoreId, sellerName, sellerAddress, false);
    }

//    public boolean updateProductQuantity(String productId, int productQuantity) {
//        ProductDetailModel productDetailModel;
//        CommonMethods.showLog(TAG, "Q : " + productQuantity);
//        CommonMethods.showLog(TAG, "I : " + productId);
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(MyConstants.PRODUCT_QUANTITY, productQuantity);
//        String query = "SELECT * FROM " + MyConstants.PRODUCT_TABLE + " WHERE "
//                + MyConstants.PRODUCT_ID + "= ?" + " ORDER BY " + MyConstants.PRODUCT_ADDED_TIME + " DESC";
//        Cursor cursor = database.rawQuery(query, new String[]{productId});
//        if (cursor.moveToFirst()) {
//            productDetailModel = getProductDetail(cursor);
//            updateProductQuantityUpdated(productDetailModel.getProduct_id(), productDetailModel.getAdditionalFeaturesType(),
//                    productDetailModel.getSub_store_id(), productDetailModel.getProductAvailabilityType(), productQuantity);
//            CommonMethods.showLog(TAG, "Timestamp " + cursor.getString(cursor.getColumnIndex(MyConstants.PRODUCT_AVAILABILITY_TYPE)));
//        }
//        cursor.close();
//
//        return true;
//    }

    /*
 * Method for add or update product quantity in database
 */
    public boolean addOrUpdate(ProductDetailModel productDetailModel, boolean isAdd) {
        CommonMethods.showLog(TAG, "Product Id : " + productDetailModel.getProduct_id());
        boolean value;
        int count = getProductQuantity(productDetailModel.getProduct_id());
        CommonMethods.showLog(TAG, "Count  : " + count);
        if (count > 0) {
            CommonMethods.showLog(TAG, "Count Not 0 ");
            if (isAdd) {
                count = count + 1;
            } else {
                count = count - 1;
            }

            value = getLastInsertedCartItem(productDetailModel.getProduct_id(), count);

            CommonMethods.showLog(TAG, " updated : " + value);

        } else {
            CommonMethods.showLog(TAG, "Count 0 ");
            productDetailModel.setProduct_quantity(1);
            long result = insertNewProduct(productDetailModel);
            if (result > 0) {
                value = true;
            } else {
                value = false;
            }
        }
        return value;
    }

    public boolean getLastInsertedCartItem(String productId, int productQuantity) {
        ProductDetailModel productDetailModel;
        Cursor cursor = database.query(MyConstants.PRODUCT_TABLE, null,
                MyConstants.PRODUCT_ID + "= ? ",
                new String[]{productId}, null, null, null);
        if (cursor.moveToFirst()) {
            productDetailModel = getProductDetail(cursor);
            productDetailModel.setProduct_quantity(productQuantity);
            updateProductQuantityUpdated(productDetailModel.getProduct_id(), productDetailModel.getAdditionalFeaturesType(),
                    productDetailModel.getSub_store_id(), productDetailModel.getProductAvailabilityType(),
                    productDetailModel.getProduct_quantity());
            CommonMethods.showLog(TAG, "Av Type " + cursor.getString(cursor.getColumnIndex(MyConstants.PRODUCT_AVAILABILITY_TYPE)));
        }
        cursor.close();
        return true;
    }


    public boolean updateProductQuantity(String productId, int productQuantity) {
        CommonMethods.showLog(TAG, "Q : " + productQuantity);
        CommonMethods.showLog(TAG, "I : " + productId);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyConstants.PRODUCT_QUANTITY, productQuantity);
        database.update(MyConstants.PRODUCT_TABLE, contentValues, MyConstants.PRODUCT_ID + "=?", new String[]{productId});
        if (productQuantity == 0) {
            deleteParticularProduct(productId);
        }
        return true;
    }

    public boolean productExistsInCart(String productId) {
        boolean exists = false;
        String query = "SELECT * FROM " + MyConstants.PRODUCT_TABLE + " WHERE "
                + MyConstants.PRODUCT_ID + "= ?";
        CommonMethods.showLog(TAG, "Query : " + query);
        Cursor cursor = database.rawQuery(query, new String[]{productId});
        if (cursor.moveToFirst()) {
            exists = true;
        }
        cursor.close();
        CommonMethods.showLog(TAG, "Exists : " + exists);
        return exists;
    }
    /*
     * Check product Exist in database or not
     */
    public boolean productExistsInCartUpdated(String productId, String
            productAdditionalFeatures, String subStoreId, String productAvailabilityType) {
        boolean exists = false;
        String query = "SELECT * FROM " + MyConstants.PRODUCT_TABLE + " WHERE "
                + MyConstants.PRODUCT_ID + "= ? AND " + MyConstants.PRODUCT_ADDITIONAL_FEATURES + "= ? AND "
                + MyConstants.SUB_STORE_ID + "= ? AND " + MyConstants.PRODUCT_AVAILABILITY_TYPE + "= ?";
        CommonMethods.showLog(TAG, "Query : " + query);
        Cursor cursor = database.rawQuery(query, new String[]{productId, productAdditionalFeatures, subStoreId, productAvailabilityType});
        if (cursor.moveToFirst()) {
            exists = true;
        }
        cursor.close();
        CommonMethods.showLog(TAG, "Exists : " + exists);
        return exists;
    }

    public int getProductQuantityUpdated(String productId, String
            productAdditionalFeatures, String subStoreId, String productAvailabilityType) {
        int quantity = 0;
        Cursor cursor = database.query(MyConstants.PRODUCT_TABLE, null,
                MyConstants.PRODUCT_ID + "= ? AND " + MyConstants.PRODUCT_ADDITIONAL_FEATURES + "= ? AND "
                        + MyConstants.SUB_STORE_ID + "= ? AND " + MyConstants.PRODUCT_AVAILABILITY_TYPE + "= ?",
                new String[]{productId, productAdditionalFeatures, subStoreId, productAvailabilityType}, null, null, null);
        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(cursor.getColumnIndex(MyConstants.PRODUCT_QUANTITY));
        }
        cursor.close();
        return quantity;
    }
    /*
     * Update Product Quantity
     */
    public boolean updateProductQuantityUpdated(String productId, String
            productAdditionalFeatures, String subStoreId,
                                                String productAvailabilityType, int productQuantity) {
        CommonMethods.showLog(TAG, "Q : " + productQuantity);
        CommonMethods.showLog(TAG, "I : " + productId);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyConstants.PRODUCT_QUANTITY, productQuantity);
        database.update(MyConstants.PRODUCT_TABLE, contentValues, MyConstants.PRODUCT_ID + "= ? AND " + MyConstants.PRODUCT_ADDITIONAL_FEATURES + "= ? AND "
                        + MyConstants.SUB_STORE_ID + "= ? AND " + MyConstants.PRODUCT_AVAILABILITY_TYPE + "= ?"
                , new String[]{productId, productAdditionalFeatures, subStoreId, productAvailabilityType});
        if (productQuantity == 0) {
            deleteParticularProductUpdated(productId, productAdditionalFeatures, subStoreId, productAvailabilityType);
        }
        return true;
    }

    /*
     *Delete Product from databsse
     */
    public long deleteParticularProductUpdated(String productId, String
            productAdditionalFeatures, String subStoreId,
                                               String productAvailabilityType) {
        long result = database.delete(MyConstants.PRODUCT_TABLE, MyConstants.PRODUCT_ID + "= ? AND " +
                        MyConstants.PRODUCT_ADDITIONAL_FEATURES + "= ? AND "
                        + MyConstants.SUB_STORE_ID + "= ? AND " + MyConstants.PRODUCT_AVAILABILITY_TYPE + "= ?",
                new String[]{productId, productAdditionalFeatures, subStoreId, productAvailabilityType});
        CommonMethods.showLog(TAG, "deleteAllCategories " + result);
        return result;
    }

    public boolean addOrUpdateProductsUpdated(ProductDetailModel productDetailModel,
                                              boolean isAdd) {
        CommonMethods.showLog(TAG, "Product Id : " + productDetailModel.getProduct_id());
        boolean value;
        int count = getProductQuantityUpdated(productDetailModel.getProduct_id(), productDetailModel.getAdditionalFeaturesType(),
                productDetailModel.getSub_store_id(), productDetailModel.getProductAvailabilityType());
        CommonMethods.showLog(TAG, "Count  : " + count);
        if (count > 0) {
            CommonMethods.showLog(TAG, "Count Not 0 ");
            if (isAdd) {
                count = count + 1;
            } else {
                count = count - 1;
            }

            value = updateProductQuantityUpdated(productDetailModel.getProduct_id(), productDetailModel.getAdditionalFeaturesType(),
                    productDetailModel.getSub_store_id(), productDetailModel.getProductAvailabilityType(), count);
            CommonMethods.showLog(TAG, " updated : " + value);
        } else {
            CommonMethods.showLog(TAG, "Count 0 ");
            productDetailModel.setProduct_quantity(1);
            long result = insertNewProduct(productDetailModel);
            if (result > 0) {
                value = true;
            } else {
                value = false;
            }
        }
        return value;
    }
    /*
     * Delete all Products From Database
     */
    public long deleteAllProducts() {
        long result = database.delete(MyConstants.PRODUCT_TABLE, null, null);
        CommonMethods.showLog(TAG, "deleteAllProducts " + result);
        return result;
    }
    /*
     * Delete All Favorite Products from database
     */
    public long deleteAllFavoriteProducts() {
        long result = database.delete(MyConstants.FAVOURITE_TABLE, null, null);
        CommonMethods.showLog(TAG, "deleteAllFavoriteProducts " + result);
        return result;
    }
    /*
     * Delete Particular Product From Database
     */
    public long deleteParticularProduct(String productId) {
        long result = database.delete(MyConstants.PRODUCT_TABLE, MyConstants.PRODUCT_ID + "=?", new String[]{productId});
        CommonMethods.showLog(TAG, "deleteAllCategories " + result);
        return result;
    }


    // Questions Table

    /*
     * Insert Questions to database
     */
    public long insertQuestions(QuizQuestionsModel quizQuestionsModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyConstants.QUESTION_ID, quizQuestionsModel.getQuestion_id());
        contentValues.put(MyConstants.QUESTION, quizQuestionsModel.getQuestion());
        contentValues.put(MyConstants.QUESTION_PUBLIC_ID, quizQuestionsModel.getQuestion_public_id());

        long result = database.insert(MyConstants.QUESTION_TABLE, null, contentValues);
        CommonMethods.showLog(TAG, "insertQuestions " + result);
        return result;
    }
    /*
     * Get All  Questions from database
     */
    public List<QuizQuestionsModel> getAllQuestions() {
        List<QuizQuestionsModel> data = new ArrayList<>();
        Cursor cursor = database.query(MyConstants.QUESTION_TABLE, null, null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                data.add(getQuestionDetail(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    /*
     * Get  Questions detail  from database
     */
    private QuizQuestionsModel getQuestionDetail(Cursor cursor) {
        String questionId = cursor.getString(cursor.getColumnIndex(MyConstants.QUESTION_ID));
        String questionPublicId = cursor.getString(cursor.getColumnIndex(MyConstants.QUESTION_PUBLIC_ID));
        String question = cursor.getString(cursor.getColumnIndex(MyConstants.QUESTION));
        return new QuizQuestionsModel(questionId, questionPublicId, question);
    }

//    private QuizQuestionsModel getQuestionDetail(Cursor cursor) {
//        List<QuizQuestionsModel> questionOptionsList = getQuestionOptions();
//        List<String> optionsList = new ArrayList<>();
//        List<String> optionStatusList = new ArrayList<>();
//        for (int i = 0; i < questionOptionsList.size(); i++) {
//            if (cursor.getString(cursor.getColumnIndex(MyConstants.QUESTION_ID)).equalsIgnoreCase(questionOptionsList.get(i).getQuestion_id())) {
//                optionsList.add(questionOptionsList.get(i).getQuestion_public_id());
//                optionStatusList.add(questionOptionsList.get(i).getOptionStatus());
//            }
//        }
//        String questionId = cursor.getString(cursor.getColumnIndex(MyConstants.QUESTION_ID));
//        String questionPublicId = cursor.getString(cursor.getColumnIndex(MyConstants.QUESTION_PUBLIC_ID));
//        String question = cursor.getString(cursor.getColumnIndex(MyConstants.QUESTION));
//        return new QuizQuestionsModel(questionId, questionPublicId, question, optionsList, optionStatusList);
//    }

    /*
     * Get  Question Id from database
     */
    public String getQuestionId(String id) {
        String questionId = "";
        Cursor cursor = database.query(MyConstants.QUESTION_TABLE, null,
                MyConstants.QUESTION_ID + "=?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            questionId = cursor.getString(cursor.getColumnIndex(MyConstants.QUESTION_ID));
        }
        cursor.close();
        return questionId;
    }

    /*
     * Delete All  Questions from database
     */
    public long deleteAllQuestions() {
        long result = database.delete(MyConstants.QUESTION_TABLE, null, null);
        CommonMethods.showLog(TAG, "deleteAllQuestions " + result);
        return result;
    }

    // Questions Options Table

    /*
     *Insert  Questions Options to  database
     */

    public long insertQuestionOptions(String id, List<String> answerList, String
            optionStatus) {
        long result = 0;
        String questionId = getQuestionId(id);
        CommonMethods.showLog(TAG, "AnswerList Size : " + answerList.size());
        ContentValues contentValues = new ContentValues();
        for (String innerData : answerList) {
            contentValues.put(MyConstants.QUESTION_ID, questionId);
            contentValues.put(MyConstants.OPTION_STATUS, optionStatus);
            contentValues.put(MyConstants.QUESTION_OPTIONS, innerData);
            result = database.insert(MyConstants.QUESTION_OPTIONS_TABLE, null, contentValues);
            CommonMethods.showLog(TAG, "insertQuestionOptions " + result);
        }
        return result;
    }


    /*
     *Get   Questions Options from  database
     */

    public List<QuizQuestionsModel> getQuestionOptions() {
        List<QuizQuestionsModel> data = new ArrayList<>();
        Cursor cursor = database.query(MyConstants.QUESTION_OPTIONS_TABLE, null, null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                data.add(getQuestionOptionsDetail(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }
    /*
     *Get   Question Option Detail  from  database
     */

    private QuizQuestionsModel getQuestionOptionsDetail(Cursor cursor) {
        String questionId = cursor.getString(cursor.getColumnIndex(MyConstants.QUESTION_ID));
        String questionOptions = cursor.getString(cursor.getColumnIndex(MyConstants.QUESTION_OPTIONS));
        String optionStatus = cursor.getString(cursor.getColumnIndex(MyConstants.OPTION_STATUS));
        return new QuizQuestionsModel(questionId, questionOptions, optionStatus);
    }
    /*
     *Delete  Questions Options from  database
     */

    public long deleteAllQuestionOptions() {
        long result = database.delete(MyConstants.QUESTION_OPTIONS_TABLE, null, null);
        CommonMethods.showLog(TAG, "deleteAllQuestionOptions " + result);
        return result;
    }
    /*
     * Update  Questions Options from  database
     */


    public boolean updateQuestionOptions(String questionId, String questionOption) {
        boolean value = false;
        List<QuizQuestionsModel> databaseOptionsList = new ArrayList<>();

        databaseOptionsList = getQuestionOptions();
        CommonMethods.showLog(TAG, "DATA Size  : " + databaseOptionsList.size());
//        for (int i = 0; i < databaseOptionsList.size(); i++) {
//            if (databaseOptionsList.get(i).getQuestion_id().equalsIgnoreCase(questionId)) {
//                CommonMethods.showLog(TAG, " I : " + i);
//                if (databaseOptionsList.get(i).getQuestion().equalsIgnoreCase("1")) {
//                    CommonMethods.showLog(TAG, "Match at : " + i);
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put(MyConstants.OPTION_STATUS, "0");
//                    long result = database.update(MyConstants.QUESTION_OPTIONS_TABLE, contentValues, MyConstants.QUESTION_ID + "=?", new String[]{questionId});
//                    CommonMethods.showLog(TAG, "updateQuestionOptions " + result);
//                    value = true;
//                    break;
//                }
//            }
//        }

        CommonMethods.showLog(TAG, "Question Option : " + questionOption);
        String optionStatus = getOptionStatus(questionOption);
        CommonMethods.showLog(TAG, "option Status  : " + optionStatus);
        if (optionStatus.equalsIgnoreCase("0")) {
            CommonMethods.showLog(TAG, "Option Status 0 ");
            ContentValues contentValues = new ContentValues();
            contentValues.put(MyConstants.OPTION_STATUS, "1");
            database.update(MyConstants.QUESTION_OPTIONS_TABLE, contentValues, MyConstants.QUESTION_OPTIONS + "=?", new String[]{questionOption});
            value = true;
        } else {
            CommonMethods.showLog(TAG, "Option Status 1 ");
            ContentValues contentValues = new ContentValues();
            contentValues.put(MyConstants.OPTION_STATUS, "0");
            database.update(MyConstants.QUESTION_OPTIONS_TABLE, contentValues, MyConstants.QUESTION_OPTIONS + "=?", new String[]{questionOption});
            value = true;
        }
        return value;
    }
    /*
     *Get Options status  from  database
     */

    public String getOptionStatus(String questionOption) {
        String optionStatus = " ";
        Cursor cursor = database.query(MyConstants.QUESTION_OPTIONS_TABLE, null,
                MyConstants.QUESTION_OPTIONS + "=?", new String[]{questionOption}, null, null, null);
        if (cursor.moveToFirst()) {
            optionStatus = cursor.getString(cursor.getColumnIndex(MyConstants.OPTION_STATUS));
        }
        cursor.close();
        return optionStatus;
    }

    //  Favourite Table
    /*
     *Insert Favorite Product to database
     */

    public long insertFavouriteProduct(ProductDetailModel productDetailModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyConstants.PRODUCT_ID, productDetailModel.getProduct_id());
        contentValues.put(MyConstants.PRODUCT_NAME, productDetailModel.getProduct_name());
        contentValues.put(MyConstants.PRODUCT_STORE_ID, productDetailModel.getStore_id());
        contentValues.put(MyConstants.PRODUCT_CATEGORY_ID, productDetailModel.getCategory_id());
        contentValues.put(MyConstants.PRODUCT_DESCRIPTION, productDetailModel.getDescription());
        contentValues.put(MyConstants.PRODUCT_IMAGE, productDetailModel.getImage_url());
        contentValues.put(MyConstants.PRODUCT_SALE_PRICE, productDetailModel.getSale_price());
        contentValues.put(MyConstants.PRODUCT_ACTUAL_PRICE, productDetailModel.getActual_price());
        contentValues.put(MyConstants.PRODUCT_QUANTITY, productDetailModel.getProduct_quantity());
        long result = database.insert(MyConstants.FAVOURITE_TABLE, null, contentValues);
        CommonMethods.showLog(TAG, "insertFavouriteProduct " + result);
        return result;
    }
    /*
     *Get All  Favorite Products from database
     */

    public int getAllFavouriteProductCount() {
        Cursor cursor = database.query(MyConstants.FAVOURITE_TABLE, null, null,
                null, null, null, null);
        int count = 0;
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    /*
     *Check  Favorite Products exists in database or not
     */
    public boolean productExistsInFavourite(String productId) {
        boolean exists = false;
        String query = "SELECT * FROM " + MyConstants.FAVOURITE_TABLE + " WHERE "
                + MyConstants.PRODUCT_ID + "= ?";
        CommonMethods.showLog(TAG, "Query : " + query);
        Cursor cursor = database.rawQuery(query, new String[]{productId});
        if (cursor.moveToFirst()) {
            exists = true;
        }
        cursor.close();
        CommonMethods.showLog(TAG, "Exists : " + exists);
        return exists;
    }
    /*
     *Delete Particular Favorite Product from database
     */
    public long deleteParticularFavoriteProduct(String productId) {
        long result = database.delete(MyConstants.FAVOURITE_TABLE, MyConstants.PRODUCT_ID + "=?", new String[]{productId});
        CommonMethods.showLog(TAG, "deleteParticularFavoriteProduct " + result);
        return result;
    }
    /*
     *Get All  Favorite Products List  from database
     */
    public List<ProductDetailModel> getAllFavoriteProductList() {
        List<ProductDetailModel> data = new ArrayList<>();
        Cursor cursor = database.query(MyConstants.FAVOURITE_TABLE, null, null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                data.add(getFavoriteProductDetail(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }
    /*
     *Get Favorite Product Detail from database
     */
    private ProductDetailModel getFavoriteProductDetail(Cursor cursor) {
        String productId = cursor.getString(cursor.getColumnIndex(MyConstants.PRODUCT_ID));
        String productName = cursor.getString(cursor.getColumnIndex(MyConstants.PRODUCT_NAME));
        String productStoreId = cursor.getString(cursor.getColumnIndex(MyConstants.PRODUCT_STORE_ID));
        String productCategoryId = cursor.getString(cursor.getColumnIndex(MyConstants.PRODUCT_CATEGORY_ID));
        String productDescription = cursor.getString(cursor.getColumnIndex(MyConstants.PRODUCT_DESCRIPTION));
        double productActualPrice = cursor.getDouble(cursor.getColumnIndex(MyConstants.PRODUCT_ACTUAL_PRICE));
        String productImage = cursor.getString(cursor.getColumnIndex(MyConstants.PRODUCT_IMAGE));
        double productSalePrice = cursor.getDouble(cursor.getColumnIndex(MyConstants.PRODUCT_SALE_PRICE));
        int productQuantity = cursor.getInt(cursor.getColumnIndex(MyConstants.PRODUCT_QUANTITY));
        return new ProductDetailModel(productId, productDescription, productCategoryId, productName,
                productStoreId, productActualPrice, productSalePrice, productQuantity, productImage, false);
    }


    public int getFavoriteProductQuantityUpdated(String productId, String
            productAdditionalFeatures, String subStoreId, String productAvailabilityType) {
        int quantity = 0;
        Cursor cursor = database.query(MyConstants.FAVOURITE_TABLE, null,
                MyConstants.PRODUCT_ID + "= ? AND " + MyConstants.PRODUCT_ADDITIONAL_FEATURES + "= ? AND "
                        + MyConstants.SUB_STORE_ID + "= ? AND " + MyConstants.PRODUCT_AVAILABILITY_TYPE + "= ?",
                new String[]{productId, productAdditionalFeatures, subStoreId, productAvailabilityType}, null, null, null);
        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(cursor.getColumnIndex(MyConstants.PRODUCT_QUANTITY));
        }
        cursor.close();
        return quantity;
    }


}