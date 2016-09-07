package com.worldchip.childpark.database;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper {

	private static final String DATABASE_NAME = "ShareDB.db";
    private static final int DATABASE_VERSION = 1;
    
    public static final String APP_TABLE = "bbp_app_table";
    
    public static final String BuilderAppTable =  
		 "create table " + APP_TABLE
				+ " (_id integer primary key autoincrement, "
				+ "packageName text, "  + "appName text, "+ "icon text);";
    
    private final Context mContext;
    private DatabaseHelper mDatabaseHelper;
    
    private SQLiteDatabase db;
    
    public DBHelper(Context context)
    {
         this.mContext = context;
         mDatabaseHelper = new DatabaseHelper(context);
    }
    
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
	   DatabaseHelper(Context context)
	   {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
       }
    
       @Override
       public void onCreate(SQLiteDatabase db)
       {
           db.execSQL(BuilderAppTable);
           
       }
       
       
       @Override
       public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
       {
           db.execSQL("DROP TABLE IF EXISTS "+APP_TABLE);
           onCreate(db);
       }
    }
    
    public DBHelper open() throws SQLException
    {
    	close();
        db = mDatabaseHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
    	mDatabaseHelper.close();
    }
    
    public boolean deleteRow(String tableName, long rowId)
    {
    	 open();
         return db.delete(tableName, "_id =" + rowId, null) > 0;
    }
    
    public boolean deleteRow(String tableName, String whereSql)
    {
   	    open();
        return db.delete(tableName, whereSql, null) > 0;
   }
    
    public Cursor getCursor(String tableName, String whereStr)
    {
    	open();
    	return db.rawQuery("select * from "+tableName+" "+ whereStr, null);
    	
    }

    public Cursor getCursorBySql(String sqlStr)
    {
    	open();
    	return db.rawQuery(sqlStr, null);
    	
    }
    
    public Cursor getCursorGroupBy(String tableName, String columnName, String groupBy) throws SQLException
    {
    	open();
    	return db.rawQuery("select "+columnName+" from "+tableName+" " + groupBy, null);
    }  
    
    public Cursor getCursor(String tableName, String columeName, String value) throws SQLException
    {
    	open();
    	return db.rawQuery("select * from "+tableName+" where "+columeName+" ='"+value+"'", null);
    }     
      
    public Cursor getTopN(String tableName,String columeName, int topN) throws SQLException
    {
    	open();
    	return db.rawQuery("select * from "+tableName+" order by "+columeName+" desc limit "+ topN, null);
    }

    public boolean insertValues(String tableName, ContentValues values)
    {
    	open();
        return db.insert(tableName, null, values) > 0;
    }
    
    public boolean updateValues(String tableName, ContentValues values, String whereStr)
    {
    	open();
        return db.update(tableName, values, whereStr, null) >0;
    }
    
    public void InsertValuesWithTrans(String tableName, ContentValues values)
    {
    	open();
    	db.beginTransaction();
        db.insert(tableName, null, values);
        db.endTransaction();
    }
    
    public boolean updateColumeValue(String tableName, String columeName, String value, String whereColume, String whereValue)
    {
    	ContentValues args = new ContentValues();
        args.put(columeName, value);
        open();
        return db.update(tableName, args,whereColume + "='" + whereValue +"'", null) > 0;
    }
}
