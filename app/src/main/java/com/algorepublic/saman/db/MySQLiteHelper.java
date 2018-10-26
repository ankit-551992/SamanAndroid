package com.algorepublic.saman.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.algorepublic.saman.data.model.Product;

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
    public static final String TABLE_CART= "CART";

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


    //Table CART CREATE STATEMENT
    private static final String CREATE_CART_TABLE = "CREATE TABLE "
            + TABLE_CART+ "(" + CART_COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +CART_PRODUCT_ID+" INTEGER,"
            +CART_PRODUCT_NAME+" TEXT,"
            +CART_PRODUCT_NAME_AR+" TEXT,"
            +CART_PRODUCT_DESCRIPTION+" TEXT,"
            +CART_PRODUCT_DESCRIPTION_AR+" TEXT,"
            +CART_PRODUCT_PRICE+" INTEGER,"
            +CART_PRODUCT_QUANTITY+" INTEGER,"
            +CART_PRODUCT_SIZE_LENGTH+" INTEGER,"
            +CART_PRODUCT_SIZE_WIDTH+" INTEGER,"
            +CART_PRODUCT_SIZE_HEIGHT+" INTEGER,"
            +CART_PRODUCT_PICTURES+" TEXT,"
            +CART_PRODUCT_IMAGES+" TEXT,"
            +CART_PRODUCT_COLOR+" TEXT,"
            +CART_PRODUCT_IS_ACTIVE+" INTEGER,"
            +CART_PRODUCT_IS_DELETED+" INTEGER,"
            +CART_PRODUCT_CATEGORY_ID+" INTEGER,"
            +CART_PRODUCT_ATTRIBUTE_ID+" INTEGER,"
            +CART_PRODUCT_ATTRIBUTE_GROUP_ID+" INTEGER,"
            +CART_PRODUCT_CREATED_AT+" TEXT,"
            +CART_PRODUCT_CREATED_BY+" TEXT,"
            +CART_PRODUCT_UPDATED_AT+" TEXT,"
            +CART_PRODUCT_UPDATED_BY+" TEXT"+ ")";

    public boolean addToCart(Product product,int categoryID,int attributeID,int attributeGroupID,int colorID) {

        ContentValues values = new ContentValues();
        // Check Product already in cart
        if(!CheckProductAlreadyExit(product,attributeID,attributeGroupID)) {
            values.put(CART_PRODUCT_ID, product.getID());
            values.put(CART_PRODUCT_NAME, product.getProductName());
            values.put(CART_PRODUCT_NAME_AR, product.getProductNameAR());
            values.put(CART_PRODUCT_DESCRIPTION, product.getDescription());
            values.put(CART_PRODUCT_DESCRIPTION_AR, product.getDescriptionAR());
            values.put(CART_PRODUCT_PRICE, product.getPrice());
            values.put(CART_PRODUCT_QUANTITY,1);
            values.put(CART_PRODUCT_SIZE_LENGTH, product.getSizeLength());
            values.put(CART_PRODUCT_SIZE_WIDTH, product.getSizeWidth());
            values.put(CART_PRODUCT_SIZE_HEIGHT, product.getSizeHeight());
            values.put(CART_PRODUCT_PICTURES, product.getPictures().get(0));
            values.put(CART_PRODUCT_IMAGES, product.getProductImagesURLs().get(0));
            values.put(CART_PRODUCT_COLOR, ""+colorID);


            int isActive=0;
            if(product.getIsActive()){
                isActive=1;
            }
            values.put(CART_PRODUCT_IS_ACTIVE, isActive);


            int isDeleted=0;
            if(product.getIsDeleted()){
                isDeleted=1;
            }
            values.put(CART_PRODUCT_IS_DELETED, isDeleted);

            values.put(CART_PRODUCT_CATEGORY_ID, categoryID);
            values.put(CART_PRODUCT_ATTRIBUTE_ID, attributeID);
            values.put(CART_PRODUCT_ATTRIBUTE_GROUP_ID, attributeGroupID);
            values.put(CART_PRODUCT_CREATED_AT, product.getCreatedAt());
//            values.put(CART_PRODUCT_CREATED_BY, product.getCreateBy());
            values.put(CART_PRODUCT_UPDATED_AT, product.getUpdatedAt());
//            values.put(CART_PRODUCT_UPDATED_BY, product.getUpdateBy());

            SQLiteDatabase db = this.getWritableDatabase();
            long rowInserted = db.insert(TABLE_CART, null, values);
            db.close();
            if (rowInserted != -1) {
                return true;
            } else {
                return false;
            }
        }
        // IF Product already in cart
        else
        {
            values.put(CART_PRODUCT_QUANTITY,
                    (getCartProductCount(product,attributeID,attributeGroupID)+1));
            SQLiteDatabase db = this.getWritableDatabase();
            long rowUpdate = db.update(TABLE_CART,values, CART_PRODUCT_ID +" = ? AND " +CART_PRODUCT_IS_ACTIVE +" = ? AND " +CART_PRODUCT_SIZE_LENGTH+" = ?",
                    new String[]{""+product.getID(),""+product.getIsActive(),""+product.getSizeLength()});
            db.close();
            if (rowUpdate != -1) {
                return true;
            } else {
                return false;
            }
        }
    }


    public ArrayList<Product> getCartProducts(){

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CART;
        ArrayList<Product> cartArrayList = new ArrayList<Product>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product=new Product();
                product.setCartID(cursor.getInt(0));
                product.setID(cursor.getInt(1));
                product.setProductName(cursor.getString(2));
                product.setProductNameAR(cursor.getString(3));
                product.setDescription(cursor.getString(4));
                product.setDescriptionAR(cursor.getString(5));
                product.setPrice(cursor.getInt(6));
                product.setQuantity(cursor.getInt(7));
                product.setSizeLength(cursor.getInt(8));
                product.setSizeWidth(cursor.getInt(9));
                product.setSizeHeight(cursor.getInt(10));
//                product.setPictures(cursor.getInt(11));
//                product.setProductImagesURLs(cursor.getInt(12));
//                product.setColor(cursor.getString(13));
                boolean isActive=false;
                if(cursor.getInt(14)==1){
                    isActive=true;
                }

                boolean isDeleted=false;
                if(cursor.getInt(15)==1){
                    isDeleted=true;
                }
                product.setIsActive(isDeleted);

                product.setCartCategory(cursor.getInt(16));
                product.setCartAttributeID(cursor.getInt(17));
                product.setCartAttributeGroupID(cursor.getInt(18));



                product.setCreatedAt(cursor.getString(19));
//                product.setCreateBy(cursor.getString(20));
                product.setUpdatedAt(cursor.getString(21));
//                product.setUpdateBy(cursor.getString(22));

                // Adding to list
                cartArrayList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return list
        return cartArrayList;
    }

    public boolean editCartItem(Product product,int newAttributeID,int newQuantity)throws SQLException {

        // Check if AttributeID is same and Just Product Quantity changed
        if(product.getCartAttributeID()==newAttributeID && product.getQuantity()!=newQuantity){
            ContentValues values = new ContentValues();
            values.put(CART_PRODUCT_QUANTITY, newQuantity);
            SQLiteDatabase db = this.getWritableDatabase();
            long rowUpdate = db.update(TABLE_CART,values, CART_PRODUCT_ID +" = ? AND " +CART_PRODUCT_IS_ACTIVE +" = ? AND " +CART_PRODUCT_SIZE_LENGTH+" = ?",
                    new String[]{""+product.getID(),""+product.getIsActive(),""+product.getSizeLength()});
            db.close();
            if (rowUpdate != -1) {
                return true;
            } else {
                return false;
            }
        }
        // Check if AttributeID not same
        else if(product.getCartAttributeID()!=newAttributeID){
            deleteItemFromCart(product);
            ContentValues values = new ContentValues();
            // Check if product already exit with AttributeID
            //if product already not exit with AttributeID
            if(!CheckProductAlreadyExit(product,newAttributeID,product.getCartAttributeGroupID())) {

                values.put(CART_PRODUCT_ID, product.getID());
                values.put(CART_PRODUCT_NAME, product.getProductName());
                values.put(CART_PRODUCT_NAME_AR, product.getProductNameAR());
                values.put(CART_PRODUCT_DESCRIPTION, product.getDescription());
                values.put(CART_PRODUCT_DESCRIPTION_AR, product.getDescriptionAR());
                values.put(CART_PRODUCT_PRICE, product.getPrice());
                values.put(CART_PRODUCT_QUANTITY,1);
                values.put(CART_PRODUCT_SIZE_LENGTH, product.getSizeLength());
                values.put(CART_PRODUCT_SIZE_WIDTH, product.getSizeWidth());
                values.put(CART_PRODUCT_SIZE_HEIGHT, product.getSizeHeight());
                values.put(CART_PRODUCT_PICTURES, product.getPictures().get(0));
                values.put(CART_PRODUCT_IMAGES, product.getProductImagesURLs().get(0));
                values.put(CART_PRODUCT_COLOR, "Random");


                int isActive=0;
                if(product.getIsActive()){
                    isActive=1;
                }
                values.put(CART_PRODUCT_IS_ACTIVE, isActive);


                int isDeleted=0;
                if(product.getIsDeleted()){
                    isDeleted=1;
                }
                values.put(CART_PRODUCT_IS_DELETED, isDeleted);

                values.put(CART_PRODUCT_CATEGORY_ID, product.getProductCategories().get(0).getID());
                values.put(CART_PRODUCT_ATTRIBUTE_ID, product.getProductAttributes().get(0).getID());
                values.put(CART_PRODUCT_ATTRIBUTE_GROUP_ID, product.getProductAttributes().get(0).getAttributeGroupID());
                values.put(CART_PRODUCT_CREATED_AT, product.getCreatedAt());
//            values.put(CART_PRODUCT_CREATED_BY, product.getCreateBy());
                values.put(CART_PRODUCT_UPDATED_AT, product.getUpdatedAt());
//            values.put(CART_PRODUCT_UPDATED_BY, product.getUpdateBy());

                SQLiteDatabase db = this.getWritableDatabase();
                long rowInserted = db.insert(TABLE_CART, null, values);
                db.close();
                if (rowInserted != -1) {
                    return true;
                } else {
                    return false;
                }
            }
            // if product already exit with AttributeID
            else
            {
                values.put(CART_PRODUCT_QUANTITY,
                        (getCartProductCount(product,newAttributeID,product.getCartAttributeGroupID())+newQuantity));
                SQLiteDatabase db = this.getWritableDatabase();
                long rowUpdate = db.update(TABLE_CART,values, CART_PRODUCT_ID +" = ? AND " +CART_PRODUCT_ATTRIBUTE_ID +" = ? AND " +CART_PRODUCT_ATTRIBUTE_GROUP_ID+" = ?",
                        new String[]{""+product.getID(),""+product.getCartAttributeID(),""+product.getCartAttributeGroupID()});
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

    public boolean deleteItemFromCart(Product product)throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART,  CART_PRODUCT_ID +" = ? AND " +CART_PRODUCT_ATTRIBUTE_ID +" = ? AND " +CART_PRODUCT_ATTRIBUTE_GROUP_ID+" = ?",
                new String[]{""+product.getID(),""+product.getCartAttributeID(),""+product.getCartAttributeGroupID()});
        db.close();
        return false;
    }


    public int getCartProductCount(Product product,int attributeID,int attributeGroupID){
        int cnt=0;
        String countQuery = "Select * from " + TABLE_CART+ " WHERE "+ CART_PRODUCT_ID+ " = "+ product.getID()+
                " AND " + CART_PRODUCT_ATTRIBUTE_ID + " =  \"" + attributeID + "\""+
                " AND " + CART_PRODUCT_ATTRIBUTE_GROUP_ID + " =  \"" + attributeGroupID + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do{
                    cnt = cursor.getInt(7);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return cnt;
    }

    public int getCartAllProductsCount(){
        int cnt=0;
        String countQuery = "Select * from " + TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do{
                    cnt = cnt + cursor.getInt(7);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return cnt;
    }


    public boolean CheckProductAlreadyExit(Product product,int attributeID,int attributeGroupID) {
        String Query = "Select * from " + TABLE_CART+ " WHERE "+ CART_PRODUCT_ID+ " = "+ product.getID()+
                " AND " + CART_PRODUCT_ATTRIBUTE_ID + " =  \"" + attributeID + "\""+
                " AND " + CART_PRODUCT_ATTRIBUTE_GROUP_ID + " =  \"" + attributeGroupID + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
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
