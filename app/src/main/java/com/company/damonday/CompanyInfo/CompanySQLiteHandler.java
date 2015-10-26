package com.company.damonday.CompanyInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.company.damonday.Login.SQLiteHandler;
import com.company.damonday.MyFavourites.MyFavouritesObject;

import java.util.HashMap;

/**
 * Created by lamtaklung on 22/10/2015.
 */
public class CompanySQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "abc";

    // Login table name
    private static final String TABLE_MY_FAVOURITE = "my_favourite";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_ENT_ID = "ent_id";
    private static final String KEY_PIC_URL = "pic_url";
    private static final String KEY_TITLE = "title";
    private static final String KEY_PRICE = "price";
    private static final String KEY_LIKE = "like";
    private static final String KEY_FAIR = "fair";
    private static final String KEY_DISLIKE = "dislike";
    private static final String KEY_AVERAGE_SCORE = "average_score";
    private static final String KEY_CATEGORIES = "categories";
    private static final String KEY_CREATE_DATE = "create_date";

    public CompanySQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_MY_FAVOURITE_TABLE = "CREATE TABLE " + TABLE_MY_FAVOURITE + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_ENT_ID + " INTEGER NOT NULL, " +
                KEY_PIC_URL + " TEXT NOT NULL, " +
                KEY_TITLE + " TEXT NOT NULL, " +
                KEY_PRICE + " TEXT NOT NULL, " +
                KEY_LIKE + " TEXT NOT NULL, " +
                KEY_FAIR + " TEXT NOT NULL, " +
                KEY_DISLIKE + " TEXT NOT NULL, " +
                KEY_AVERAGE_SCORE + " TEXT NOT NULL, " +
                KEY_CATEGORIES + " TEXT NOT NULL, " +
                KEY_CREATE_DATE + " DATETIME)";
        db.execSQL(CREATE_MY_FAVOURITE_TABLE);

        Log.d("Create table", "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_FAVOURITE);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing my favourite details in database
     * */
    public void addMyFavourite(MyFavouritesObject myFavouritesObject){
        SQLiteDatabase db = this.getWritableDatabase();
        String cat = "";
        for (int i = 0; i < myFavouritesObject.getCat().size(); i++){
            if(cat != "")
                cat += ", ";
            cat += myFavouritesObject.getCat().get(i);
        }

        ContentValues values = new ContentValues();
        values.put(KEY_ENT_ID, myFavouritesObject.getEntId()); // entID
        values.put(KEY_PIC_URL, myFavouritesObject.getPicUrl()); // picUrl
        values.put(KEY_TITLE, myFavouritesObject.getTitle()); // title
        values.put(KEY_PRICE, myFavouritesObject.getPrice()); // price
        values.put(KEY_LIKE, myFavouritesObject.getLike()); // like
        values.put(KEY_FAIR, myFavouritesObject.getFair()); // fair
        values.put(KEY_DISLIKE, myFavouritesObject.getDislike()); // dislike
        values.put(KEY_AVERAGE_SCORE, myFavouritesObject.getAverageScore()); // averageScore
        values.put(KEY_CATEGORIES, cat); // cat
        values.put(KEY_CREATE_DATE, "datetime()"); // create date

        // Inserting Row
        System.out.println(values);
        long id = db.insert(TABLE_MY_FAVOURITE, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Getting all my favourite data from database
     * */
    public HashMap<String, String> getAllMyFavouriteDetails() {
        HashMap<String, String> myFavourite = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_MY_FAVOURITE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            myFavourite.put("id", cursor.getString(1));
            myFavourite.put("ent_id", cursor.getString(2));
            myFavourite.put("pic_url", cursor.getString(3));
            myFavourite.put("title", cursor.getString(4));
            myFavourite.put("price", cursor.getString(5));
            myFavourite.put("like", cursor.getString(6));
            myFavourite.put("fair", cursor.getString(7));
            myFavourite.put("dislike", cursor.getString(8));
            myFavourite.put("average_score", cursor.getString(9));
            myFavourite.put("categories", cursor.getString(10));
            myFavourite.put("create_date", cursor.getString(11));
        }
        cursor.close();
        db.close();
        // return my favourite
        Log.d(TAG, "Fetching user from Sqlite: " + myFavourite.toString());


        return myFavourite;
    }


    /**
     * Getting my favourite status return true if rows are there in table
     * */
    public int getRowCount(int entId) {
        String countQuery = "SELECT * FROM " + TABLE_MY_FAVOURITE + " WHERE " + KEY_ENT_ID + " = ?";
        String[] value = new String[]{Integer.toString(entId)};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, value);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteMyFavourite(int entId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        String where = KEY_ENT_ID + " = " + entId;
        db.delete(TABLE_MY_FAVOURITE, where, null);
        db.close();

        Log.d(TAG, "Deleted my favourite " + entId + " info from sqlite");
    }
}
