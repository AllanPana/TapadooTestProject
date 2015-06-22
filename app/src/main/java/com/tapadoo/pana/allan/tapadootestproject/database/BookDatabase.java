package com.tapadoo.pana.allan.tapadootestproject.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

import com.tapadoo.pana.allan.tapadootestproject.extras.Util;
import com.tapadoo.pana.allan.tapadootestproject.pojos.Books;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by allan on 21/06/15.
 */
public class BookDatabase {

    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public BookDatabase(Context context) {
        dataBaseHelper = new DataBaseHelper(context);
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
    }


    /**
     *
     * @param booksList
     * @param clearPrevious
     */
    public void insertBooks(ArrayList<Books> booksList, boolean clearPrevious) {
        if(clearPrevious){
            deleteAllBooks();
        }
        String sqlQuery = "INSERT INTO " + DataBaseHelper.TABLE_NAME + " VALUES (?,?,?,?,?,?);";
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sqlQuery);
        sqLiteDatabase.beginTransaction();

        for (int i = 0; i < booksList.size(); i++) {
            Books book = booksList.get(i);
            statement.clearBindings();

            statement.bindLong(1, book.getId());
            statement.bindString(2, book.getTitle());
            statement.bindString(3, book.getIsbn());
            //statement.bindString(4, book.getDescription()!= null ? book.getIsbn() : "na");
            statement.bindLong(4, book.getPrice());
            statement.bindString(5, book.getCurrencyCode());
            statement.bindString(6, book.getAuthor());

            statement.execute();
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }


    /**
     *
     * @return booklist
     */
    public ArrayList<Books> getAllBooks() {
        ArrayList<Books> booksList = new ArrayList<>();
        String[] columns = {
                DataBaseHelper.COLUMN_ID,
                DataBaseHelper.COLUMN_TITLE,
                DataBaseHelper.COLUMN_ISBN,
                //DataBaseHelper.COLUMN_DESC,
                DataBaseHelper.COLUMN_PRICE,
                DataBaseHelper.COLUMN_CURRENCY_CODE,
                DataBaseHelper.COLUMN_AUTHOR};

        Cursor cursor = sqLiteDatabase.query(DataBaseHelper.TABLE_NAME,columns,null,null,null,null,null);

        if(cursor != null && cursor.moveToFirst()){
            do{

                Books book = new Books();

                book.setId(cursor.getInt(cursor.getColumnIndex(DataBaseHelper.COLUMN_ID)));
                book.setTitle(cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_TITLE)));
                book.setIsbn(cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_ISBN)));
                //book.setDescription(cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_DESC)));
                book.setPrice(cursor.getInt(cursor.getColumnIndex(DataBaseHelper.COLUMN_PRICE)));
                book.setCurrencyCode(cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_CURRENCY_CODE)));
                book.setAuthor(cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_AUTHOR)));

                booksList.add(book);

            }while (cursor.moveToNext());
        }

        return booksList;
    }

    /**
     * Delete all books
     */
    public void deleteAllBooks(){
        sqLiteDatabase.delete(DataBaseHelper.TABLE_NAME,null,null);
    }


    /**
     * ****************************************************
     * DatabaseHelper class
     */
    private class DataBaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "bookdb";
        private static final String TABLE_NAME = "booktable";
        private static final int DATABASE_VERSION = 1;

        private static final String COLUMN_ID = "id";
        private static final String COLUMN_TITLE = "title";
        private static final String COLUMN_ISBN = "isbn";
        //private static final String COLUMN_DESC = "description";
        private static final String COLUMN_PRICE = "price";
        private static final String COLUMN_CURRENCY_CODE = "currencyCode";
        private static final String COLUMN_AUTHOR = "author";

        private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + " (" + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_TITLE + " VARCHAR(255), "
                + COLUMN_ISBN + " VARCHAR(255), "
                + COLUMN_PRICE + " INTEGER, "
                + COLUMN_CURRENCY_CODE + " VARCHAR(255), "
                + COLUMN_AUTHOR + " VARCHAR(255));";
               // + COLUMN_ISBN + " VARCHAR(255));";

        private static final String DROP_TABLE = "DROP TABLE IF EXIST " + TABLE_NAME;

        private Context context;


        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
                Util.setLog("create table");

            } catch (SQLException sqle) {
                Util.setLog(sqle + "");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (SQLException sqle) {
                Util.setLog(sqle + "");
            }
        }
    }
}
