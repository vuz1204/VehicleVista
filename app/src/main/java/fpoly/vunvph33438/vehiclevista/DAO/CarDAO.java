package fpoly.vunvph33438.vehiclevista.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.Database.DbHelper;
import fpoly.vunvph33438.vehiclevista.Model.Car;

public class CarDAO {

    private static final String TABLE_NAME = "Car";
    private static final String COLUMN_ID_CAR = "id_car";
    private static final String COLUMN_ID_BRAND = "id_brand";
    private static final String COLUMN_MODEL = "model";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_AVAILABLE = "available";
    private static final String COLUMN_IMAGE = "image";
    DbHelper dbHelper;
    private Context context;

    public CarDAO(Context context) {
        this.context = context;
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
        long insertedId = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
        if (insertedId != -1) {
            car.setIdCar((int) insertedId);
            return true;
        } else {
            return false;
        }
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
                byte[] imageUri = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
                list.add(new Car(idCar, idBrand, model, price, description, available, imageUri));
            }
        }
        cursor.close();
        return list;
    }

    public boolean updateImageUri(int carId, byte[] image) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE, image);

        int rowsAffected = db.update(TABLE_NAME, values, COLUMN_ID_CAR + " = ?", new String[]{String.valueOf(carId)});

        db.close();

        return rowsAffected > 0;
    }

    public Car getCarById(int carId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID_CAR + " = ?";
        ArrayList<Car> carList = getAll(sql, String.valueOf(carId));
        return carList.isEmpty() ? null : carList.get(0);
    }

    public String getCarNameById(int carId) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String[] columns = {COLUMN_MODEL}; // Assuming the name of the car is stored in the 'model' column
        String selection = COLUMN_ID_CAR + "=?";
        String[] selectionArgs = {String.valueOf(carId)};

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        String carName = null;
        if (cursor.moveToFirst()) {
            carName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MODEL));
        }

        cursor.close();
        return carName;
    }
}
