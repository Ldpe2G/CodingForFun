package ldp.games.doodlejump.sqlite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ldp.games.doodlejump.otherviews.OptionView;

import android.R.id;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class DataBaseOperation {
	
	private static final String TABLE_NAME = "androidjump";
	private  SQLOpenHelper dbsqlopenhelper;
	SharedPreferences sharedPreferences;
	String diffString;
	
	public DataBaseOperation(Context context){
		this.dbsqlopenhelper = new SQLOpenHelper(context);
		sharedPreferences = context.getSharedPreferences(OptionView.PREFS_DIFF, 0);
		diffString = sharedPreferences.getString(OptionView.PREFS_DIFF, "Rookie");
	}
	
	public void SaveHeight(long height, String date){
		
		SQLiteDatabase database_read = dbsqlopenhelper.getReadableDatabase();
		Cursor cursor = database_read.query(TABLE_NAME, null,  null,  null, null,  null, "highid desc");
		int height_col = cursor.getColumnIndex("height");
		int diff_col = cursor.getColumnIndex("difficulty");
		long temp_h = 0;
		for (cursor.moveToFirst() ; !(cursor.isAfterLast()) ; cursor.moveToNext()){
			if(cursor.getString(diff_col).equalsIgnoreCase(diffString)){
				temp_h =  Long.parseLong(cursor.getString(height_col));
			}
				
		}
		if(GetNum() > 0){
			cursor.moveToFirst();
		//	long temp = Long.parseLong(cursor.getString(height_col));
			if(height > temp_h)
				SaveToDataBase(height, date);
		}
		else {
			SaveToDataBase(height, date);
		}
		cursor.close();
		database_read.close();
	}

	private void SaveToDataBase(long height, String date) {
		SQLiteDatabase database_write = dbsqlopenhelper.getWritableDatabase();
		ContentValues values = new ContentValues(); 
		values.put("date", date);
		values.put("height", ""+height);
		values.put("difficulty", diffString);
		database_write.insert(TABLE_NAME, "highid", values);
		database_write.close();
	}
	
	public List<MyString> GetScoreList(){
		List<MyString> scoreList = new ArrayList<MyString>();

		SQLiteDatabase database_read = dbsqlopenhelper.getReadableDatabase();
		
		Cursor cursor = database_read.query(TABLE_NAME, null,  null,  null, null,  null, "highid desc");
		int height_col = cursor.getColumnIndex("height");
		int date_col = cursor.getColumnIndex("date");
		int diff_col = cursor.getColumnIndex("difficulty");
		int num = 0;
		if(GetNum() > 0){
			for (cursor.moveToFirst() ; !(cursor.isAfterLast()) ; cursor.moveToNext()){
				num++;
				scoreList.add(new MyString(""+num+"."+cursor.getString(date_col)+"     "+cursor.getString(height_col), cursor.getString(diff_col)));
			}
		}
		return scoreList;
	}
	
	public long GetNum(){
		
		SQLiteDatabase database_read = dbsqlopenhelper.getReadableDatabase();
		
		Cursor cursor = database_read.query(TABLE_NAME, null,  null,  null, null,  null, "highid desc");
		int height_col = cursor.getColumnIndex("height");
		int num = 0;
		for (cursor.moveToFirst() ; !(cursor.isAfterLast()) ; cursor.moveToNext()){
			num ++;
		}
		return num;
	}

	
}
