package hanu.a2_2001040208.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "a2_2001040208.db";
    private static final int DB_VERSION = 1;

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE cart_items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,  " +
                "product_id INTEGER NOT NULL," +
                "thumbnail TEXT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "unit_price INTEGER NOT NULL, " +
                "quantity INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cart_items");
        onCreate(db);
    }
}

