package com.piperwhite.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by BLANCA on 24/01/2016.
 */
public class ItemsDatabaseHelper extends SQLiteOpenHelper {

    private static ItemsDatabaseHelper sInstance;

    // Database Info
    private static final String DATABASE_NAME = "ToDoDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_ITEMS = "items";

    // Item Table Columns
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_TEXT = "text";

    public static synchronized ItemsDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new ItemsDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private ItemsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // These is where we need to write create table statements.
    // This is called when database is created.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                KEY_ITEM_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_ITEM_TEXT + " TEXT" +
                ")";
        db.execSQL(query);
    }

    // This method is called when database is upgraded like
    // modifying the table structure,
    // adding constraints to database, etc
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            onCreate(db);
        }
    }


    // Update an item of the database
    public void updateItem(Item item) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The item might already exist in the database
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_TEXT, item.getText());

            // This assumes ids are unique
            int rows = db.update(TABLE_ITEMS, values, KEY_ITEM_ID + "= ?", new String[]{Long.toString(item.getId())});

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("updateItem", "Error while trying to update item to database");
        } finally {
            db.endTransaction();
        }
    }

    // Insert an item into the database
    public void insertItem(Item item) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The item might already exist in the database
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_TEXT, item.getText());

            //SQLite auto increments the primary key column.
            long id = db.insertOrThrow(TABLE_ITEMS, null, values);
            item.setId(id);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("insertItem", "Error while trying to insert item to database");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Item> getItems(){
        ArrayList<Item> items = new ArrayList<>();

        String ITEMS_SELECT_QUERY =
                String.format("SELECT * FROM %s ",
                        TABLE_ITEMS);


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ITEMS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String itemText= cursor.getString(cursor.getColumnIndex(KEY_ITEM_TEXT));
                    int itemId= cursor.getInt(cursor.getColumnIndex(KEY_ITEM_ID));
                    Item newItem= new Item(itemText, itemId);
                    items.add(newItem);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("getItems", "Error while trying to get items from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return items;
    }

    public void deleteItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        db.setTransactionSuccessful();
        try {
            db.delete(TABLE_ITEMS, KEY_ITEM_ID + "=" + item.getId(), null);
        }catch (Exception e){
            Log.d("deleteItem", "Error while trying to delete an item from database");
        }finally {
            db.endTransaction();
        }
    }


}
