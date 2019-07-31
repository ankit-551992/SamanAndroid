package com.qtech.saman.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.qtech.saman.data.model.Product;
import com.qtech.saman.utils.GlobalValues;

import java.util.ArrayList;

public class MySQLiteHelper extends SQLiteOpenHelper {

    //DataBase Name
    private static final String DATABASE_NAME = "Saman.db";

    //Version Name
    private static final int DATABASE_VERSION = 1;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    //********************************************************************************
    //                                                                               *
    //                            Table CART  Start                                  *
    //                                                                               *
    //********************************************************************************

    // Tables Names
    public static final String TABLE_CART = "CART";

    //CART Table Columns
    public static final String CART_COLUMN_ID = "CART_ID";                                          //0
    public static final String CART_PRODUCT_ID = "CART_PRODUCT_ID";                                 //1
    public static final String CART_PRODUCT_NAME = "CART_PRODUCT_NAME";                             //2
    public static final String CART_PRODUCT_NAME_AR = "CART_PRODUCT_NAME_AR";                       //3
    public static final String CART_PRODUCT_DESCRIPTION = "CART_PRODUCT_DESCRIPTION";               //4
    public static final String CART_PRODUCT_DESCRIPTION_AR = "CART_PRODUCT_DESCRIPTION_AR";         //5
    public static final String CART_PRODUCT_PRICE = "CART_PRODUCT_PRICE";                           //6
    public static final String CART_PRODUCT_QUANTITY = "CART_PRODUCT_QUANTITY";                     //7
    public static final String CART_PRODUCT_SIZE_LENGTH = "CART_PRODUCT_SIZE_LENGTH";               //8
    public static final String CART_PRODUCT_SIZE_WIDTH = "CART_PRODUCT_SIZE_WIDTH";                 //9
    public static final String CART_PRODUCT_SIZE_HEIGHT = "CART_PRODUCT_SIZE_HEIGHT";               //10
    public static final String CART_PRODUCT_PICTURES = "CART_PRODUCT_PICTURES";                     //11
    public static final String CART_PRODUCT_IMAGES = "CART_PRODUCT_IMAGES";                         //12
    public static final String CART_PRODUCT_COLOR = "CART_PRODUCT_COLOR";                           //13
    public static final String CART_PRODUCT_IS_ACTIVE = "CART_PRODUCT_IS_ACTIVE";                   //14
    public static final String CART_PRODUCT_IS_DELETED = "CART_PRODUCT_IS_DELETED";                 //15
    public static final String CART_PRODUCT_CATEGORY_ID = "CART_PRODUCT_CATEGORY_ID";               //16
    public static final String CART_PRODUCT_ATTRIBUTE_ID = "CART_PRODUCT_ATTRIBUTE_ID";             //17
    public static final String CART_PRODUCT_ATTRIBUTE_GROUP_ID = "CART_PRODUCT_ATTRIBUTE_GROUP_ID"; //18
    public static final String CART_PRODUCT_CREATED_AT = "CART_PRODUCT_CREATED_AT";                 //19
    public static final String CART_PRODUCT_CREATED_BY = "CART_PRODUCT_CREATED_BY";                 //20
    public static final String CART_PRODUCT_UPDATED_AT = "CART_PRODUCT_UPDATED_AT";                 //21
    public static final String CART_PRODUCT_UPDATED_BY = "CART_PRODUCT_UPDATED_BY";                 //22
    public static final String CART_PRODUCT_OPTIONS = "CART_PRODUCT_OPTIONS";                       //23
    public static final String CART_PRODUCT_OPTIONS_NAMES = "CART_PRODUCT_OPTIONS_NAMES";           //24
    public static final String CART_PRODUCT_OPTIONS_NAMES_AR = "CART_PRODUCT_OPTIONS_NAMES_AR";     //25
    public static final String CART_PRODUCT_STORE_NAME = "CART_PRODUCT_STORE_NAME";                 //26
    public static final String CART_PRODUCT_STORE_NAME_AR = "CART_PRODUCT_STORE_NAME_AR";           //27


    //Table CART CREATE STATEMENT
    private static final String CREATE_CART_TABLE = "CREATE TABLE "
            + TABLE_CART + "(" + CART_COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CART_PRODUCT_ID + " INTEGER,"
            + CART_PRODUCT_NAME + " TEXT,"
            + CART_PRODUCT_NAME_AR + " TEXT,"
            + CART_PRODUCT_DESCRIPTION + " TEXT,"
            + CART_PRODUCT_DESCRIPTION_AR + " TEXT,"
            + CART_PRODUCT_PRICE + " REAL,"
            + CART_PRODUCT_QUANTITY + " INTEGER,"
            + CART_PRODUCT_SIZE_LENGTH + " INTEGER,"
            + CART_PRODUCT_SIZE_WIDTH + " INTEGER,"
            + CART_PRODUCT_SIZE_HEIGHT + " INTEGER,"
            + CART_PRODUCT_PICTURES + " TEXT,"
            + CART_PRODUCT_IMAGES + " TEXT,"
            + CART_PRODUCT_COLOR + " TEXT,"
            + CART_PRODUCT_IS_ACTIVE + " INTEGER,"
            + CART_PRODUCT_IS_DELETED + " INTEGER,"
            + CART_PRODUCT_CATEGORY_ID + " INTEGER,"
            + CART_PRODUCT_ATTRIBUTE_ID + " INTEGER,"
            + CART_PRODUCT_ATTRIBUTE_GROUP_ID + " INTEGER,"
            + CART_PRODUCT_CREATED_AT + " TEXT,"
            + CART_PRODUCT_CREATED_BY + " TEXT,"
            + CART_PRODUCT_UPDATED_AT + " TEXT,"
            + CART_PRODUCT_UPDATED_BY + " TEXT,"
            + CART_PRODUCT_OPTIONS + " TEXT,"
            + CART_PRODUCT_OPTIONS_NAMES + " TEXT,"
            + CART_PRODUCT_OPTIONS_NAMES_AR + " TEXT,"
            + CART_PRODUCT_STORE_NAME + " TEXT,"
            + CART_PRODUCT_STORE_NAME_AR + " TEXT" + ")";

    public boolean addToCart(Product product, String optionValues, String options, String optionsAr, int quantity) {

        optionsAr = optionsAr.replaceAll(",", "،");

        ContentValues values = new ContentValues();
        // Check Product already in cart
        if (!CheckProductAlreadyExit(product, optionValues)) {
            values.put(CART_PRODUCT_ID, product.getID());
            values.put(CART_PRODUCT_NAME, product.getProductName());
            values.put(CART_PRODUCT_NAME_AR, product.getProductNameAR());
            values.put(CART_PRODUCT_DESCRIPTION, product.getDescription());
            values.put(CART_PRODUCT_DESCRIPTION_AR, product.getDescriptionAR());
            values.put(CART_PRODUCT_PRICE, product.getPrice());
            values.put(CART_PRODUCT_QUANTITY, quantity);
            values.put(CART_PRODUCT_SIZE_LENGTH, product.getSizeLength());
            values.put(CART_PRODUCT_SIZE_WIDTH, product.getSizeWidth());
            values.put(CART_PRODUCT_SIZE_HEIGHT, product.getSizeHeight());
            values.put(CART_PRODUCT_PICTURES, product.getLogoURL());
            values.put(CART_PRODUCT_IMAGES, GlobalValues.convertListToString(product.getProductImagesURLs()));
            values.put(CART_PRODUCT_OPTIONS_NAMES, options);
            values.put(CART_PRODUCT_OPTIONS_NAMES_AR, optionsAr);
            values.put(CART_PRODUCT_STORE_NAME, product.getStoreName());
            values.put(CART_PRODUCT_STORE_NAME_AR, product.getStoreNameAR());

            int isActive = 0;
            if (product.getIsActive()) {
                isActive = 1;
            }
            values.put(CART_PRODUCT_IS_ACTIVE, isActive);


            int isDeleted = 0;
            if (product.getIsDeleted()) {
                isDeleted = 1;
            }
            values.put(CART_PRODUCT_IS_DELETED, isDeleted);

//            values.put(CART_PRODUCT_CATEGORY_ID, categoryID);
//            values.put(CART_PRODUCT_ATTRIBUTE_ID, attributeID);
//            values.put(CART_PRODUCT_ATTRIBUTE_GROUP_ID, attributeGroupID);
            values.put(CART_PRODUCT_CREATED_AT, product.getCreatedAt());
//            values.put(CART_PRODUCT_CREATED_BY, product.getCreateBy());
            values.put(CART_PRODUCT_UPDATED_AT, product.getUpdatedAt());
//            values.put(CART_PRODUCT_UPDATED_BY, product.getUpdateBy());
            values.put(CART_PRODUCT_OPTIONS, optionValues);

            SQLiteDatabase db = this.getWritableDatabase();
            long rowInserted = db.insert(TABLE_CART, null, values);
            db.close();
            if (rowInserted != -1) {
                return true;
            } else {
                return false;
            }
        }
        // If Product already in cart
        else {
            values.put(CART_PRODUCT_QUANTITY,
                    (getCartProductCount(product, optionValues) + quantity));
            SQLiteDatabase db = this.getWritableDatabase();
            long rowUpdate = db.update(TABLE_CART, values, CART_PRODUCT_ID + " = ? AND " + CART_PRODUCT_OPTIONS + " = ?",
                    new String[]{"" + product.getID(), optionValues});
            db.close();
            if (rowUpdate != -1) {
                return true;
            } else {
                return false;
            }
        }
    }

    public ArrayList<Product> getCartProducts() {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CART;
        ArrayList<Product> cartArrayList = new ArrayList<Product>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setCartID(cursor.getInt(0));
                product.setID(cursor.getInt(1));
                product.setProductName(cursor.getString(2));
                product.setProductNameAR(cursor.getString(3));
                product.setDescription(cursor.getString(4));
                product.setDescriptionAR(cursor.getString(5));
                product.setPrice(cursor.getFloat(6));
                product.setQuantity(cursor.getInt(7));
                product.setSizeLength(cursor.getInt(8));
                product.setSizeWidth(cursor.getInt(9));
                product.setSizeHeight(cursor.getInt(10));
                product.setLogoURL(cursor.getString(11));
//                product.setProductImagesURLs(cursor.getInt(12));

                boolean isActive = false;
                if (cursor.getInt(14) == 1) {
                    isActive = true;
                }

                boolean isDeleted = false;
                if (cursor.getInt(15) == 1) {
                    isDeleted = true;
                }
                product.setIsActive(isDeleted);

//                product.setCartCategory(cursor.getInt(16));
//                product.setCartAttributeID(cursor.getInt(17));
//                product.setCartAttributeGroupID(cursor.getInt(18));


                product.setCreatedAt(cursor.getString(19));
//                product.setCreateBy(cursor.getString(20));
                product.setUpdatedAt(cursor.getString(21));
//                product.setUpdateBy(cursor.getString(22));
                product.setOptionValues(cursor.getString(23));

                product.setOptions(cursor.getString(24));
                product.setOptionsAR(cursor.getString(25));
                product.setStoreName(cursor.getString(26));
                product.setStoreNameAR(cursor.getString(27));

                // Adding to list
                cartArrayList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return list
        return cartArrayList;
    }


    public boolean editCartItem(Product product, String optionValues, int newQuantity) throws SQLException {

        // Check if OptionValues is same and Just Product Quantity changed
        if (product.getOptionValues() == optionValues && product.getQuantity() != newQuantity) {
            ContentValues values = new ContentValues();
            values.put(CART_PRODUCT_QUANTITY, newQuantity);
            SQLiteDatabase db = this.getWritableDatabase();
            long rowUpdate = db.update(TABLE_CART, values, CART_PRODUCT_ID + " = ? AND " + CART_PRODUCT_OPTIONS + " = ?",
                    new String[]{"" + product.getID(), product.getOptionValues()});
            db.close();
            if (rowUpdate != -1) {
                return true;
            } else {
                return false;
            }
        }
        // Check if OptionValues not same
        else if (product.getOptionValues() != optionValues) {
//            deleteItemFromCart(product);
            ContentValues values = new ContentValues();
            // Check if product already exit with AttributeID
            //if product already not exit with AttributeID
            if (!CheckProductAlreadyExit(product, optionValues)) {

                values.put(CART_PRODUCT_ID, product.getID());
                values.put(CART_PRODUCT_NAME, product.getProductName());
                values.put(CART_PRODUCT_NAME_AR, product.getProductNameAR());
                values.put(CART_PRODUCT_DESCRIPTION, product.getDescription());
                values.put(CART_PRODUCT_DESCRIPTION_AR, product.getDescriptionAR());
                values.put(CART_PRODUCT_PRICE, product.getPrice());
                values.put(CART_PRODUCT_QUANTITY, newQuantity);
                values.put(CART_PRODUCT_SIZE_LENGTH, product.getSizeLength());
                values.put(CART_PRODUCT_SIZE_WIDTH, product.getSizeWidth());
                values.put(CART_PRODUCT_SIZE_HEIGHT, product.getSizeHeight());
                values.put(CART_PRODUCT_PICTURES, product.getPictures().get(0));
                values.put(CART_PRODUCT_IMAGES, product.getProductImagesURLs().get(0));
//                values.put(CART_PRODUCT_COLOR, "Random");


                int isActive = 0;
                if (product.getIsActive()) {
                    isActive = 1;
                }
                values.put(CART_PRODUCT_IS_ACTIVE, isActive);


                int isDeleted = 0;
                if (product.getIsDeleted()) {
                    isDeleted = 1;
                }
                values.put(CART_PRODUCT_IS_DELETED, isDeleted);

//                values.put(CART_PRODUCT_CATEGORY_ID, product.getProductCategories().get(0).getID());
//                values.put(CART_PRODUCT_ATTRIBUTE_ID, product.getProductAttributes().get(0).getID());
//                values.put(CART_PRODUCT_ATTRIBUTE_GROUP_ID, product.getProductAttributes().get(0).getAttributeGroupID());
                values.put(CART_PRODUCT_CREATED_AT, product.getCreatedAt());
//            values.put(CART_PRODUCT_CREATED_BY, product.getCreateBy());
                values.put(CART_PRODUCT_UPDATED_AT, product.getUpdatedAt());
//            values.put(CART_PRODUCT_UPDATED_BY, product.getUpdateBy());
                values.put(CART_PRODUCT_OPTIONS, optionValues);

                SQLiteDatabase db = this.getWritableDatabase();
                long rowInserted = db.insert(TABLE_CART, null, values);
                db.close();
                if (rowInserted != -1) {
                    return true;
                } else {
                    return false;
                }
            }
            // if product already exit
            else {
                values.put(CART_PRODUCT_QUANTITY,
                        (getCartProductCount(product, optionValues) + newQuantity));
                SQLiteDatabase db = this.getWritableDatabase();
                long rowUpdate = db.update(TABLE_CART, values, CART_PRODUCT_ID + " = ? AND " + CART_PRODUCT_OPTIONS + " = ?",
                        new String[]{"" + product.getID(), "" + product.getOptionValues()});
                db.close();
                if (rowUpdate != -1) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean deleteItemFromCart(Product product) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowDelete = db.delete(TABLE_CART, CART_PRODUCT_ID + " = ? AND " + CART_PRODUCT_OPTIONS + " = ?",
                new String[]{"" + product.getID(), product.getOptionValues()});
        db.close();
        if (rowDelete != -1)
            return true;

        return false;
    }

    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CART);
        db.close();
    }

    public int getCartProductCount(Product product, String optionValues) {
        int cnt = 0;
        String countQuery = "Select * from " + TABLE_CART + " WHERE " + CART_PRODUCT_ID + " = " + product.getID() +
                " AND " + CART_PRODUCT_OPTIONS + " =  \"" + optionValues + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    cnt = cursor.getInt(7);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return cnt;
    }

    public int getCartAllProductsCount() {
        int cnt = 0;
        String countQuery = "Select * from " + TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
//                    cnt = cnt + cursor.getInt(7);
                    cnt = cnt + 1;
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return cnt;
    }

    public int getCartAllProductsCounting() {
        int cnt = 0;
        String countQuery = "Select * from " + TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    cnt = cnt + cursor.getInt(7);
//                    cnt = cnt + 1;
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return cnt;
    }

    private boolean CheckProductAlreadyExit(Product product, String options) {
        String Query = "Select * from " + TABLE_CART + " WHERE " + CART_PRODUCT_ID + " = " + product.getID() +
                " AND " + CART_PRODUCT_OPTIONS + " =  \"" + options + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    //********************************************************************************
    //                                                                               *
    //                            Table CART End                                     *
    //                                                                               *
    //********************************************************************************

}
