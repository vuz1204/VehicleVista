package fpoly.vunvph33438.vehiclevista.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.Database.DbHelper;
import fpoly.vunvph33438.vehiclevista.Model.Brand;

public class BrandDAO {
    DbHelper dbHelper;
    private static final String TABLE_NAME = "Brand";
    private static final String COLUMN_ID_BRAND = "id_brand";
    private static final String COLUMN_BRAND = "brand";
    private Context context;
    public BrandDAO(Context context) {
        dbHelper = new DbHelper(context);
    }
    public boolean insert(Brand obj) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BRAND, obj.getBrand());
        long id = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        obj.setIdBrand((int) id);
        return id != -1;
    }

    public boolean delete(Brand obj) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String dk[] = {String.valueOf(obj.getIdBrand())};
        long check = sqLiteDatabase.delete(TABLE_NAME, COLUMN_ID_BRAND + "=?", dk);
        return check != -1;
    }
    public boolean update(Brand obj) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String dk[] = {String.valueOf(obj.getIdBrand())};
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BRAND, obj.getBrand());
        long check = sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_ID_BRAND + "=?", dk);
        return check != -1;
    }
    private ArrayList<Brand> getAll(String sql, String... selectionArgs){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ArrayList<Brand> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            do {
                int idBrand = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_BRAND));
                String brand = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND));
                list.add(new Brand(idBrand, brand));
            } while (cursor.moveToNext());
        }
        return list;
    }
    public ArrayList<Brand> selectAll() {
        String sql = "SELECT * FROM " + TABLE_NAME;
        return getAll(sql);
    }
    public Brand selectID(String id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID_BRAND + " = ?";
        ArrayList<Brand> list = getAll(sql, id);
        return list.isEmpty() ? null : list.get(0);
    }
    public String getBrandNameById(int idBrand) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String[] columns = {COLUMN_BRAND}; // Assuming the name of the car is stored in the 'model' column
        String selection = COLUMN_ID_BRAND + "=?";
        String[] selectionArgs = {String.valueOf(idBrand)};

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        String brandName = null;
        if (cursor.moveToFirst()) {
            brandName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND));
        }

        cursor.close();
        return brandName;
    }

}
