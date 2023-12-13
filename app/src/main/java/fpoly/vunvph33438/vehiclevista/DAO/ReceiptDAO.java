// ReceiptDAO.java
package fpoly.vunvph33438.vehiclevista.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import fpoly.vunvph33438.vehiclevista.Database.DbHelper;
import fpoly.vunvph33438.vehiclevista.Model.Receipt;
import fpoly.vunvph33438.vehiclevista.Model.User;

public class ReceiptDAO {

    private static final String TABLE_NAME = "Receipt";
    private static final String COLUMN_ID_RECEIPT = "id_receipt";
    private static final String COLUMN_ID_CAR = "id_car";
    private static final String COLUMN_ID_USER = "id_user";
    private static final String COLUMN_RENTAL_START_DATE = "rentalStartDate";
    private static final String COLUMN_RENTAL_END_DATE = "rentalEndDate";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_PAYMENT_METHOD = "paymentMethod";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_IMAGE_PAYMENT = "imagePayment";
    DbHelper dbHelper;
    private Context context;
    private UserDAO userDAO;

    public ReceiptDAO(Context context) {
        this.context = context;
        dbHelper = new DbHelper(context);
        userDAO = new UserDAO(context);
    }

    public boolean insert(Receipt receipt) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID_CAR, receipt.getId_Car());
        contentValues.put(COLUMN_ID_USER, receipt.getId_User());
        contentValues.put(COLUMN_RENTAL_START_DATE, receipt.getRentalStartDate());
        contentValues.put(COLUMN_RENTAL_END_DATE, receipt.getRentalEndDate());
        contentValues.put(COLUMN_PRICE, receipt.getPrice());
        contentValues.put(COLUMN_PAYMENT_METHOD, receipt.getPaymentMethod());
        contentValues.put(COLUMN_STATUS, receipt.getStatus());
        contentValues.put(COLUMN_DATE, receipt.getDate());
        contentValues.put(COLUMN_IMAGE_PAYMENT, receipt.getImagePayment());
        try {
            long insertedId = sqLiteDatabase.insertOrThrow(TABLE_NAME, null, contentValues);
            receipt.setId_Receipt((int) insertedId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            sqLiteDatabase.close();
        }
    }


    public boolean delete(Receipt receipt) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String[] whereArgs = {String.valueOf(receipt.getId_Receipt())};
        long check = sqLiteDatabase.delete(TABLE_NAME, COLUMN_ID_RECEIPT + "=?", whereArgs);
        return check != -1;
    }

    public boolean update(Receipt receipt, boolean isStatus) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String[] whereArgs = {String.valueOf(receipt.getId_Receipt())};
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID_RECEIPT, receipt.getId_Receipt());
        contentValues.put(COLUMN_ID_CAR, receipt.getId_Car());
        contentValues.put(COLUMN_ID_USER, receipt.getId_User());
        contentValues.put(COLUMN_RENTAL_START_DATE, receipt.getRentalStartDate());
        contentValues.put(COLUMN_RENTAL_END_DATE, receipt.getRentalEndDate());
        contentValues.put(COLUMN_PRICE, receipt.getPrice());
        contentValues.put(COLUMN_PAYMENT_METHOD, receipt.getPaymentMethod());
        contentValues.put(COLUMN_STATUS, receipt.getStatus());
        contentValues.put(COLUMN_DATE, receipt.getDate());
        contentValues.put(COLUMN_IMAGE_PAYMENT, receipt.getImagePayment());
        // Update paymentMethod based on the boolean value
        int paymentMethodValue = isStatus ? 1 : 0;
        contentValues.put(COLUMN_STATUS, paymentMethodValue);

        long check = sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_ID_RECEIPT + "=?", whereArgs);
        return check != -1;
    }


    public ArrayList<Receipt> selectID() {
        String sql = "SELECT * FROM " + TABLE_NAME;
        return getAll(sql);
    }

    public Receipt selectIdByCar(String id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID_CAR + "=?";
        ArrayList<Receipt> list = getAll(sql, id);

        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    private ArrayList<Receipt> getAll(String sql, String... selectionArgs) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ArrayList<Receipt> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int id_Receipt = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_RECEIPT));
                int id_Car = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_CAR));
                int id_User = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_USER));
                String rentalStartDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RENTAL_START_DATE));
                String rentalEndDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RENTAL_END_DATE));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                int paymentMethod = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_METHOD));
                int status = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PAYMENT));
                list.add(new Receipt(id_Receipt, id_Car, id_User, rentalStartDate, rentalEndDate,price, paymentMethod, status, date, image));
            }
        }
        cursor.close();
        return list;
    }

    public int revenue(String startDate, String endDate) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String sql = "SELECT SUM(price) as revenue FROM Receipt WHERE date BETWEEN ? AND ?";
        String dk[] = {startDate, endDate};
        int revenue = 0;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, dk);
        if (cursor.moveToFirst()) {
            try {
                revenue = cursor.getInt(cursor.getColumnIndexOrThrow("revenue"));
            } catch (Exception e) {
                revenue = 0;
            }
        }
        cursor.close();
        return revenue;
    }
    public boolean updateImage(int receiptId, byte[] image) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_PAYMENT, image);
        String[] whereArgs = {String.valueOf(receiptId)};
        int rowsAffected = db.update(TABLE_NAME, values, COLUMN_ID_RECEIPT + " = ?", whereArgs);
        db.close();
        return rowsAffected > 0;
    }

}
