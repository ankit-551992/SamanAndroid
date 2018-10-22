package com.algorepublic.saman.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MySQLiteHelper  extends SQLiteOpenHelper {

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
    public static final String CART_COLUMN_ID = "CART_ID";
    public static final String CART_PRODUCT_ID = "CART_PRODUCT_ID";
    public static final String CART_PRODUCT_COLOR = "CART_PRODUCT_COLOR";
    public static final String CART_PRODUCT_SIZE = "CART_PRODUCT_SIZE";
    public static final String CART_PRODUCT_NAME = "CART_PRODUCT_NAME";
    public static final String CART_PRODUCT_PRICE = "CART_PRODUCT_PRICE";
    public static final String CART_PRODUCT_IMAGE = "CART_PRODUCT_IMAGE";
    public static final String CART_PRODUCT_QUANTITY = "CART_PRODUCT_QUANTITY";


    //Table CART CREATE STATEMENT
    private static final String CREATE_CART_TABLE = "CREATE TABLE "
            + TABLE_CART+ "(" + CART_COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +CART_PRODUCT_ID+" INTEGER,"
            +CART_PRODUCT_COLOR+" TEXT,"
            +CART_PRODUCT_SIZE+" TEXT,"
            +CART_PRODUCT_NAME+" TEXT,"
            +CART_PRODUCT_PRICE+" INTEGER,"
            +CART_PRODUCT_IMAGE+" TEXT,"
            +CART_PRODUCT_QUANTITY+" INTEGER"+ ")";


//
//    //Adding ORDERS
//
//    public boolean addToCart(Cart cart) {
//
//        ContentValues values = new ContentValues();
//        // Check Product already in cart
//        if(!CheckProductAlreadyExit(cart.getCART_PRODUCT_ID(),cart.getCART_PRODUCT_SIZE(),cart.getCART_PRODUCT_COLOR())) {
//            values.put(CART_PRODUCT_ID, cart.getCART_PRODUCT_ID());
//            values.put(CART_PRODUCT_COLOR, cart.getCART_PRODUCT_COLOR());
//            values.put(CART_PRODUCT_SIZE, cart.getCART_PRODUCT_SIZE());
//            values.put(CART_PRODUCT_NAME, cart.getCART_PRODUCT_NAME());
//            values.put(CART_PRODUCT_PRICE, cart.getCART_PRODUCT_PRICE());
//            values.put(CART_PRODUCT_IMAGE, cart.getCART_PRODUCT_IMAGE());
//            values.put(CART_PRODUCT_QUANTITY,1);
//            SQLiteDatabase db = this.getWritableDatabase();
//            long rowInserted = db.insert(TABLE_CART, null, values);
//            db.close();
//            if (rowInserted != -1) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//        // IF Product already in cart
//        else
//        {
//            values.put(CART_PRODUCT_QUANTITY,
//                    (getCartProductCount(cart.getCART_PRODUCT_ID(),cart.getCART_PRODUCT_SIZE(),cart.getCART_PRODUCT_COLOR())+1));
//            SQLiteDatabase db = this.getWritableDatabase();
//            long rowUpdate = db.update(TABLE_CART,values, CART_PRODUCT_ID +" = ? AND " +CART_PRODUCT_SIZE +" = ? AND " +CART_PRODUCT_COLOR+" = ?",
//                    new String[]{""+cart.getCART_PRODUCT_ID(),cart.getCART_PRODUCT_SIZE(),cart.getCART_PRODUCT_COLOR()});
//            db.close();
//            if (rowUpdate != -1) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//    }
//
//
//    public ArrayList<Cart> getCartProducts(){
//
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_CART;
//        ArrayList<Cart> cartArrayList = new ArrayList<Cart>();
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Cart cart=new Cart();
//                cart.setCART_ID(cursor.getInt(0));
//                cart.setCART_PRODUCT_ID(cursor.getInt(1));
//                cart.setCART_PRODUCT_COLOR(cursor.getString(2));
//                cart.setCART_PRODUCT_SIZE(cursor.getString(3));
//                cart.setCART_PRODUCT_NAME(cursor.getString(4));
//                cart.setCART_PRODUCT_PRICE(cursor.getInt(5));
//                cart.setCART_PRODUCT_IMAGE(cursor.getString(6));
//                cart.setCART_PRODUCT_QUANTITY(cursor.getInt(7));
//
//                // Adding to list
//                cartArrayList.add(cart);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        // return list
//        return cartArrayList;
//    }
//
//    public boolean editCartItem(Cart cart,String newSize,int newQuantity)throws SQLException {
//
//        // Check if size is same and Just Product Quantity changed
//        if(cart.getCART_PRODUCT_SIZE().equals(newSize) && cart.getCART_PRODUCT_QUANTITY()!=newQuantity){
//            ContentValues values = new ContentValues();
//            values.put(CART_PRODUCT_QUANTITY, newQuantity);
//            SQLiteDatabase db = this.getWritableDatabase();
//            long rowUpdate = db.update(TABLE_CART,values, CART_PRODUCT_ID +" = ? AND " +CART_PRODUCT_SIZE +" = ? AND " +CART_PRODUCT_COLOR+" = ?",
//                    new String[]{""+cart.getCART_PRODUCT_ID(),cart.getCART_PRODUCT_SIZE(),cart.getCART_PRODUCT_COLOR()});
//            db.close();
//            if (rowUpdate != -1) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//        // Check if size not same
//        else if(!cart.getCART_PRODUCT_SIZE().equals(newSize)){
//            deleteItemFromCart(cart.getCART_PRODUCT_ID(),cart.getCART_PRODUCT_SIZE(),cart.getCART_PRODUCT_COLOR());
//            ContentValues values = new ContentValues();
//            // Check if product already exit with new size
//
//            //if product already not exit with new size
//            if(!CheckProductAlreadyExit(cart.getCART_PRODUCT_ID(),newSize,cart.getCART_PRODUCT_COLOR())) {
//                values.put(CART_PRODUCT_ID, cart.getCART_PRODUCT_ID());
//                values.put(CART_PRODUCT_COLOR, cart.getCART_PRODUCT_COLOR());
//                values.put(CART_PRODUCT_SIZE, newSize);
//                values.put(CART_PRODUCT_NAME, cart.getCART_PRODUCT_NAME());
//                values.put(CART_PRODUCT_PRICE, cart.getCART_PRODUCT_PRICE());
//                values.put(CART_PRODUCT_IMAGE, cart.getCART_PRODUCT_IMAGE());
//                values.put(CART_PRODUCT_QUANTITY,newQuantity);
//                SQLiteDatabase db = this.getWritableDatabase();
//                long rowInserted = db.insert(TABLE_CART, null, values);
//                db.close();
//                if (rowInserted != -1) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//            // if product already exit with new size
//            else
//            {
//                values.put(CART_PRODUCT_QUANTITY,
//                        (getCartProductCount(cart.getCART_PRODUCT_ID(), newSize, cart.getCART_PRODUCT_COLOR()) + newQuantity));
//                SQLiteDatabase db = this.getWritableDatabase();
//                long rowUpdate = db.update(TABLE_CART, values, CART_PRODUCT_ID + " = ? AND " + CART_PRODUCT_SIZE + " = ? AND " + CART_PRODUCT_COLOR + " = ?",
//                        new String[]{"" + cart.getCART_PRODUCT_ID(), newSize, cart.getCART_PRODUCT_COLOR()});
//                db.close();
//                if (rowUpdate != -1) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        }
//        return false;
//    }
//
//    public boolean deleteItemFromCart(int productID,String size,String color)throws SQLException {
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_CART,  CART_PRODUCT_ID +" = ? AND " +CART_PRODUCT_SIZE +" = ? AND " +CART_PRODUCT_COLOR+" = ?",
//                new String[]{""+productID,""+size,""+color});
//        db.close();
//        return false;
//    }
//
//    public int getCartProductCount(int productID,String size,String color){
//        int cnt=0;
//        String countQuery = "Select * from " + TABLE_CART+ " WHERE "+ CART_PRODUCT_ID+ " = "+ productID+
//                " AND " + CART_PRODUCT_COLOR + " =  \"" + color + "\""+
//                " AND " + CART_PRODUCT_SIZE + " =  \"" + size + "\"";
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        if(cursor!=null) {
//            if (cursor.moveToFirst()) {
//                do{
//                    cnt = cursor.getInt(7);
//                } while (cursor.moveToNext());
//            }
//        }
//        cursor.close();
//        return cnt;
//    }
//
//    public int getCartAllProductsCount(){
//        int cnt=0;
//        String countQuery = "Select * from " + TABLE_CART;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        if(cursor!=null) {
//            if (cursor.moveToFirst()) {
//                do{
//                    cnt = cnt + cursor.getInt(7);
//                } while (cursor.moveToNext());
//            }
//        }
//        cursor.close();
//        return cnt;
//    }
//
//
//    public boolean CheckProductAlreadyExit(int productID,String size,String color) {
//        String Query = "Select * from " + TABLE_CART+ " WHERE "+ CART_PRODUCT_ID+ " = "+ productID+
//                " AND " + CART_PRODUCT_COLOR + " =  \"" + color + "\""+
//                " AND " + CART_PRODUCT_SIZE + " =  \"" + size + "\"";
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(Query, null);
//        if(cursor.getCount() <= 0){
//            cursor.close();
//            return false;
//        }
//        cursor.close();
//        return true;
//    }

    //********************************************************************************
    //                                                                               *
    //                            Table CART End                                    *
    //                                                                               *
    //********************************************************************************




}
