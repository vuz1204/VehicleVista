package fpoly.vunvph33438.vehiclevista.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;

import java.io.InputStream;
import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.Database.DbHelper;
import fpoly.vunvph33438.vehiclevista.Model.Car;

public class CarDAO {
    DbHelper dbHelper;
    private static final String TABLE_NAME = "Car";
    private static final String COLUMN_ID_CAR = "id_car";
    private static final String COLUMN_ID_BRAND = "id_brand";
    private static final String COLUMN_MODEL = "model";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_DESCRIPTION = "description";

    private static final String COLUMN_AVAILABLE = "available";
    private static final String COLUMN_IMAGE = "image";
    private Context context;
    public CarDAO(Context context) {
        dbHelper = new DbHelper(context);
    }
    public boolean insert(Car car) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID_BRAND, car.getIdBrand());
        contentValues.put(COLUMN_MODEL, car.getModel());
        contentValues.put(COLUMN_PRICE, car.getPrice());
        contentValues.put(COLUMN_DESCRIPTION, car.getDescription());
        contentValues.put(COLUMN_AVAILABLE, car.isAvailable());
        contentValues.put(COLUMN_IMAGE, car.getImage());
        long check = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        car.setIdCar((int) check);
        return check != -1;
    }
    public boolean delete(Car car) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String[] whereArgs = {String.valueOf(car.getIdCar())};
        long check = sqLiteDatabase.delete(TABLE_NAME, COLUMN_ID_CAR + "=?", whereArgs);
        return check != -1;
    }
    public boolean update(Car car) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String[] whereArgs = {String.valueOf(car.getIdCar())};
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID_BRAND, car.getIdBrand());
        contentValues.put(COLUMN_MODEL, car.getModel());
        contentValues.put(COLUMN_PRICE, car.getPrice());
        contentValues.put(COLUMN_DESCRIPTION, car.getDescription());
        contentValues.put(COLUMN_AVAILABLE, car.isAvailable());
        contentValues.put(COLUMN_IMAGE, car.getImage());
        long check = sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_ID_CAR + "=?", whereArgs);
        return check != -1;
    }
    public ArrayList<Car> getAllCars() {
        String sql = "SELECT * FROM " + TABLE_NAME;
        return getAll(sql);
    }
    private ArrayList<Car> getAll(String sql, String... selectionArgs) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ArrayList<Car> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int idCar = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_CAR));
                int idBrand = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_BRAND));
                String model = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MODEL));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                int available = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AVAILABLE));
                String imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
                list.add(new Car(idCar, idBrand, model, price,description, available, imageUri));
            }
        }
        cursor.close();
        return list;
    }
    public void insertImg(ActivityResultLauncher<Intent> activityResultLauncher) {
        Intent myfileintent = new Intent(Intent.ACTION_GET_CONTENT);
        myfileintent.setType("image/*");
        activityResultLauncher.launch(myfileintent);
    }
    public void onActivityResult(Uri imageUri, ImageView imageView) {
        try {
            InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            imageView.setImageBitmap(selectedImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Add a new method in CarDAO to save the image URI
    public boolean updateImageUri(int carId, Uri imageUri) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_IMAGE, imageUri.toString());
        String[] whereArgs = {String.valueOf(carId)};
        long check = sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_ID_CAR + "=?", whereArgs);
        return check != -1;
    }

}
