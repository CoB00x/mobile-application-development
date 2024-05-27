package com.example.pr10_zubarev;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "contacts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    public DatabaseHelper(Context context) {
        super(context, "contacts.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PHONE + " TEXT)";
        db.execSQL(createTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int
            newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, contact.getName());
        cv.put(COLUMN_PHONE, contact.getPhone());
        long result = db.insert(TABLE_NAME, null, cv);
        db.close();
        return result != -1;
    }
    public boolean deleteContact(String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_PHONE + " = ?",
                new String[]{phone});
        db.close();
        return result > 0;
    }
    public Contact findByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new
                        String[]{COLUMN_ID, COLUMN_NAME, COLUMN_PHONE},
                COLUMN_NAME + " = ?", new String[]{name}, null,
                null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Contact contact = new Contact(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2));
            cursor.close();
            db.close();
            return contact;
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }
    public Contact findByPhone(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new
                        String[]{COLUMN_ID, COLUMN_NAME, COLUMN_PHONE},
                COLUMN_PHONE + " = ?", new String[]{phone}, null,
                null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Contact contact = new Contact(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2));
            cursor.close();
            db.close();
            return contact;
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact(cursor.getInt(0),
                        cursor.getString(1), cursor.getString(2));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactList;
    }
    public boolean updateContact(String oldPhone, String newName, String newPhone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, newName);
        cv.put(COLUMN_PHONE, newPhone);
        int result = db.update(TABLE_NAME, cv, COLUMN_PHONE + " = ?", new String[]{oldPhone});
        db.close();
        return result > 0;
    }
}