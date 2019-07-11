package com.centura.e_commerce.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.centura.e_commerce.database.model.UserDetails;
import com.centura.e_commerce.database.model.VirtualShoopingCart;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Product_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create Table
        db.execSQL(UserDetails.CREATE_TABLE);
        db.execSQL(VirtualShoopingCart.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + UserDetails.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VirtualShoopingCart.TABLE_NAME);
        //Create Table Again
        onCreate(db);
    }

    //insert new row
    public long insertUserDetail(String user_detail) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserDetails.DETAIL, user_detail);

        // insert row
        long id = db.insert(UserDetails.TABLE_NAME, null, contentValues);
        // return newly inserted row id
        return id;

    }

    public UserDetails getUserDetail(long user_id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(UserDetails.TABLE_NAME,
                new String[]{UserDetails.USER_ID, UserDetails.DETAIL},
                UserDetails.USER_ID + "=?",
                new String[]{String.valueOf(user_id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        UserDetails userDetailsModel = new UserDetails(
                cursor.getInt(cursor.getColumnIndex(UserDetails.USER_ID)),
                cursor.getString(cursor.getColumnIndex(UserDetails.DETAIL))
                // cursor.getInt(cursor.getColumnIndex(ProductDetailsModel.PRODUCT_QUANTITY))
        );

        // close the db connection
        cursor.close();

        return userDetailsModel;
    }

    public ArrayList<UserDetails> getAllUsersDetails() {
        ArrayList<UserDetails> userDetailsModels = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + UserDetails.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                if (cursor.getCount() > 0) {

                    UserDetails userDetails = new UserDetails();
                    userDetails.setId(cursor.getInt(cursor.getColumnIndex(UserDetails.USER_ID)));
                    userDetails.setUser_detail(cursor.getString(cursor.getColumnIndex(UserDetails.DETAIL)));
                    //productDetailsModel.setProduct_quantity(cursor.getInt(cursor.getColumnIndex(ProductDetailsModel.PRODUCT_QUANTITY)));
                    userDetailsModels.add(userDetails);
                } else {
                    System.out.println("In ELSE Condtion");
                }

            } while (cursor.moveToNext());
        }
        db.close();
        return userDetailsModels;

    }

    public int updateUserDetail(String user_detail, int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserDetails.DETAIL, user_detail);

        // updating row
        return db.update(UserDetails.TABLE_NAME, values, UserDetails.USER_ID + " = ?",
                new String[]{String.valueOf(user_id)});
    }

    //insert new row
    public boolean insertProduct(String product,String product_id){
        // get writable database as we want to write data
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(VirtualShoopingCart.PRODUCT,product);
        contentValues.put(VirtualShoopingCart.USER_ID,product_id);
        // insert row
        db.insert(VirtualShoopingCart.TABLE_NAME, null,contentValues);
        // return newly inserted row id
        return true;

    }

    public VirtualShoopingCart getProductDetail(String user_id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(VirtualShoopingCart.TABLE_NAME,
                new String[]{VirtualShoopingCart.USER_ID, VirtualShoopingCart.PRODUCT},
                VirtualShoopingCart.USER_ID + "=?",
                new String[]{String.valueOf(user_id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        VirtualShoopingCart virtualShoopingCart = new VirtualShoopingCart(
                cursor.getString(cursor.getColumnIndex(VirtualShoopingCart.USER_ID)),
                cursor.getString(cursor.getColumnIndex(VirtualShoopingCart.PRODUCT))
                // cursor.getInt(cursor.getColumnIndex(ProductDetailsModel.PRODUCT_QUANTITY))
        );

        // close the db connection
        cursor.close();

        return virtualShoopingCart;
    }

    public ArrayList<VirtualShoopingCart> getAllProductDetails(){
        ArrayList<VirtualShoopingCart> virtualShoopingCartArrayList=new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + VirtualShoopingCart.TABLE_NAME ;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);


        if(cursor.moveToFirst()){
            do {
                if(cursor.getCount()>0)
                {

                    VirtualShoopingCart virtualShoopingCart=new VirtualShoopingCart();
                    virtualShoopingCart.setId(cursor.getString(cursor.getColumnIndex(VirtualShoopingCart.USER_ID)));
                    virtualShoopingCart.setProduct(cursor.getString(cursor.getColumnIndex(VirtualShoopingCart.PRODUCT)));
                    //productDetailsModel.setProduct_quantity(cursor.getInt(cursor.getColumnIndex(ProductDetailsModel.PRODUCT_QUANTITY)));
                    virtualShoopingCartArrayList.add(virtualShoopingCart);
                }
                else
                {
                    System.out.println("In ELSE Condtion" );
                }

            }while (cursor.moveToNext());
        }
        db.close();
        return virtualShoopingCartArrayList;

    }
    public int getProductCount() {
        String countQuery = "SELECT  * FROM " +VirtualShoopingCart.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }


    public void deleteProduct(String UserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(VirtualShoopingCart.TABLE_NAME, VirtualShoopingCart.USER_ID + " = ?",
                new String[]{UserId});
        db.close();
    }
    /* public void deleteProduct(ProductDetailsModel productDetailsModel) {
         SQLiteDatabase db = this.getWritableDatabase();
         db.delete(ProductDetailsModel.TABLE_NAME, ProductDetailsModel.USER_ID + " = ?",
                 new String[]{productDetailsModel.getId()});
         db.close();
     }*/
    public int updateProduct(String product,String user_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VirtualShoopingCart.PRODUCT,product);

        // updating row
        return db.update(VirtualShoopingCart.TABLE_NAME, values, VirtualShoopingCart.USER_ID + " = ?",
                new String[]{String.valueOf(user_id)});
    }
}

