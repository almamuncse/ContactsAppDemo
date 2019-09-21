package com.codingwithmamun.contactsappdemo.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.codingwithmamun.contactsappdemo.models.Contact;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "contacts.db";
    private static final String TABLE_NAME = "contacts_table";
    public static final String COL0 = "ID";
    public static final String COL1 = "NAME";
    public static final String COL2 = "PHONE_NUMBER";
    public static final String COL3 = "DEVICE";
    public static final String COL4 = "EMAIL";
    public static final String COL5 = "PROFILE_PHOTO";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " +
                TABLE_NAME + "(" +
                COL0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1 + " TEXT, " +
                COL2 + " TEXT, " +
                COL3 + " TEXT, " +
                COL4 + " TEXT, " +
                COL5 + " TEXT )";
        db.execSQL(sql);

//        String sql = "CREATE TABLE " +
//                TABLE_NAME + " ( " +
//                COL0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COL1 + " TEXT, " +
//                COL2 + " TEXT, " +
//                COL3 + " TEXT, " +
//                COL4 + " TEXT, " +
//                COL5 + " TEXT )";
//        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF  EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    /**
     * Insert a new contact into the database
     * @param contact
     * @return
     */
    public boolean addContacts(Contact contact){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COL1,contact.getName());
        values.put(COL2,contact.getPhonenumber());
        values.put(COL3,contact.getDevice());
        values.put(COL4,contact.getEmail());
        values.put(COL5,contact.getProfileImage());

        long result=db.insert(TABLE_NAME,null,values);


        if (result==-1){
            return false;
        }else{
            return true;
        }

    }

    /**
     * Retrieve all Contact from database
     * @return
     */
    public Cursor getAllContact(){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
    }
}
