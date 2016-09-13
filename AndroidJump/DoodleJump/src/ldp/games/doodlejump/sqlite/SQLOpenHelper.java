package ldp.games.doodlejump.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "androidjump.db";
	private static final int DATABASE_VERSION = 1;
	
	public SQLOpenHelper(Context context){
		this(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public SQLOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		arg0.execSQL("create table androidjump (highid integer primary key autoincrement,"
				+"date text,"+"height text,"+"difficulty text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
