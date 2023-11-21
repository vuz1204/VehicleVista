package fpoly.vunvph33438.vehiclevista.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "VehicleVista.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create the User table
        String createUserTable = "CREATE TABLE User (" +
                "id_user INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE, " +
                "password TEXT NOT NULL, " +
                "fullname TEXT NOT NULL, " +
                "phoneNumber INTEGER NOT NULL)";
        sqLiteDatabase.execSQL(createUserTable);

        // Insert a default user
        String insertDefaultUser = "INSERT INTO User (username, password, fullname, phoneNumber) VALUES ('admin', 'admin', 'Nguyen Van Vu', 345353424)";
        sqLiteDatabase.execSQL(insertDefaultUser);

        // Create the Brand table
        String createBrandTable = "CREATE TABLE Brand (" +
                "id_brand INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "brand TEXT NOT NULL)";
        sqLiteDatabase.execSQL(createBrandTable);

        // Create the Car table
        String createCarTable = "CREATE TABLE Car (" +
                "id_car INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_brand INTEGER NOT NULL, " +
                "model TEXT NOT NULL, " +
                "price INTEGER NOT NULL, " +
                "available BOOLEAN NOT NULL DEFAULT 1, " +
                "image BLOB, " +
                "FOREIGN KEY (id_brand) REFERENCES Brand (id_brand))"; 
        sqLiteDatabase.execSQL(createCarTable);

        // Create the Receipt table
        String createReceiptTable = "CREATE TABLE Receipt (" +
                "id_receipt INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_car INTEGER NOT NULL, " +
                "id_user INTEGER NOT NULL, " +
                "rentalStartDate DATE NOT NULL, " +
                "rentalEndDate DATE NOT NULL, " +
                "price INTEGER NOT NULL, " +
                "paymentMethod INTEGER NOT NULL, " +
                "status BOOLEAN NOT NULL, " +
                "FOREIGN KEY (id_car) REFERENCES Car (id_car), " +
                "FOREIGN KEY (id_user) REFERENCES User (id_user))";
        sqLiteDatabase.execSQL(createReceiptTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i < i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS User");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Brand");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Car");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Receipt");
            onCreate(sqLiteDatabase);
        }
    }
}
