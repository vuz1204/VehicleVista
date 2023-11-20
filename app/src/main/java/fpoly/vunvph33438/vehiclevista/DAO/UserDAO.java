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
    public static final String COLUMN_PHONENUMBER = "phoneNumber";
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
        contentValues.put(COLUMN_PHONENUMBER, obj.getPhoneNumber());
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

    public boolean updatePass(User obj) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String dk[] = {String.valueOf(obj.getId_user())};
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, obj.getUsername());
        contentValues.put(COLUMN_PASSWORD, obj.getPassword());
        contentValues.put(COLUMN_FULLNAME, obj.getFullname());
        contentValues.put(COLUMN_PHONENUMBER, obj.getPhoneNumber());
        long check = sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_ID_USER + "= ?", dk);
        return check != -1;
    }

    private ArrayList<User> getAll(String sql, String... selectionArgs) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ArrayList<User> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
        if (cursor.getCount() > 0) {
            while ((cursor.moveToNext())) {
                int idUser = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_USER));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
                String fullname = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULLNAME));
                int phoneNumber = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PHONENUMBER));
                list.add(new User(idUser, username, password, fullname, phoneNumber));
            }
        }
        return list;
    }

    public ArrayList<User> selectAll() {
        String sql = "SELECT * FROM " + TABLE_NAME;
        return getAll(sql);
    }

    public User selectID(String id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID_USER + "=?";
        ArrayList<User> list = getAll(sql, id);

        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        String[] selectionArgs = new String[]{username, password};
        Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);

        try {
            return cursor.getCount() > 0;
        } finally {
            cursor.close();
            sqLiteDatabase.close();
        }
    }
}
