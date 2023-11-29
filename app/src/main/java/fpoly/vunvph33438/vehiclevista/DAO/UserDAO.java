package fpoly.vunvph33438.vehiclevista.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.Database.DbHelper;
import fpoly.vunvph33438.vehiclevista.Model.User;

public class UserDAO {
    public static final String TABLE_NAME = "User";
    public static final String COLUMN_ID_USER = "id_user";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_FULLNAME = "fullname";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE_NUMBER = "phoneNumber";
    public static final String COLUMN_ROLE = "role";
    DbHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new DbHelper(context);
    }

    public boolean insertData(User obj) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, obj.getUsername());
        contentValues.put(COLUMN_PASSWORD, obj.getPassword());
        contentValues.put(COLUMN_FULLNAME, obj.getFullname());
        contentValues.put(COLUMN_EMAIL, obj.getEmail());
        contentValues.put(COLUMN_PHONE_NUMBER, obj.getPhoneNumber());
        contentValues.put(COLUMN_ROLE, obj.getRole());
        long check = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        obj.setId_user((int) check);
        return check != -1;
    }

    public boolean delete(User obj) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String dk[] = {String.valueOf(obj.getId_user())};
        long check = sqLiteDatabase.delete(TABLE_NAME, COLUMN_ID_USER + "= ?", dk);
        return check != -1;
    }

    public boolean update(User obj) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String dk[] = {String.valueOf(obj.getId_user())};
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, obj.getUsername());
        contentValues.put(COLUMN_PASSWORD, obj.getPassword());
        contentValues.put(COLUMN_FULLNAME, obj.getFullname());
        contentValues.put(COLUMN_EMAIL, obj.getEmail());
        contentValues.put(COLUMN_PHONE_NUMBER, obj.getPhoneNumber());
        contentValues.put(COLUMN_ROLE, obj.getRole());
        long check = sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_ID_USER + "= ?", dk);
        return check != -1;
    }

    public boolean updatePass(User obj) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String dk[] = {String.valueOf(obj.getId_user())};
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, obj.getUsername());
        contentValues.put(COLUMN_PASSWORD, obj.getPassword());
        contentValues.put(COLUMN_FULLNAME, obj.getFullname());
        contentValues.put(COLUMN_EMAIL, obj.getEmail());
        contentValues.put(COLUMN_PHONE_NUMBER, obj.getPhoneNumber());
        contentValues.put(COLUMN_ROLE, obj.getRole());
        long check = sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_ID_USER + "= ?", dk);
        return check != -1;
    }

    private ArrayList<User> getAll(String sql, String... selectionArgs) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ArrayList<User> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);

        try {
            if (cursor.getCount() > 0) {
                while ((cursor.moveToNext())) {
                    int idUser = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_USER));
                    String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
                    String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
                    String fullname = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULLNAME));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                    String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER));
                    int role = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ROLE));
                    list.add(new User(idUser, username, password, fullname, email, phoneNumber, role));
                }
            }
        } finally {
            cursor.close();
            sqLiteDatabase.close();
        }

        return list;
    }
    public boolean updatePassForgot(User obj) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String dk[] = {String.valueOf(obj.getId_user())};
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, obj.getUsername());
        contentValues.put(COLUMN_PASSWORD, obj.getPassword());
        contentValues.put(COLUMN_FULLNAME, obj.getFullname());
        contentValues.put(COLUMN_EMAIL, obj.getEmail());
        contentValues.put(COLUMN_PHONE_NUMBER, obj.getPhoneNumber());
        contentValues.put(COLUMN_ROLE, obj.getRole());
        long check = sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_USERNAME + "=?", new String[]{obj.getUsername()});
        return check != -1;
    }

    public ArrayList<User> selectAll() {
        String sql = "SELECT * FROM " + TABLE_NAME;
        return getAll(sql);
    }

    public User selectID(String id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + "=?";
        ArrayList<User> list = getAll(sql, id);

        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public boolean checkLogin(String username, String password, String role) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=? AND " + COLUMN_ROLE + "=?";
        String[] selectionArgs = new String[]{username, password, role};
        Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);

        try {
            return cursor.getCount() > 0;
        } finally {
            cursor.close();
            sqLiteDatabase.close();
        }
    }
    public boolean checkInfor(String username, String email) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_EMAIL + "=?";
        String[] selectionArgs = new String[]{username, email};
        Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);

        try {
            return cursor.getCount() > 0;
        } finally {
            cursor.close();
            sqLiteDatabase.close();
        }
    }
}
