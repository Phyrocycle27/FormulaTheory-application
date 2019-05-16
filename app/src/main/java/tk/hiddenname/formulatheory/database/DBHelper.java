package tk.hiddenname.formulatheory.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

   private static final int DATABASE_VERSION = 1;
   private static final String DATABASE_NAME = "sample_database";

   public DBHelper(Context context) {
	  super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  Log.d("LogDB", "Открываем БД");
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
	  Log.d("LogDB", "создаём таблицы");
	  db.execSQL(DBConstants.SubjectEntity.CREATE_TABLE);
	  db.execSQL(DBConstants.SectionEntity.CREATE_TABLE);
	  db.execSQL(DBConstants.FormulaObjectEntity.CREATE_TABLE);
	  db.execSQL(DBConstants.FormulaEntity.CREATE_TABLE);
	  db.execSQL(DBConstants.UnitObjectEntity.CREATE_TABLE);
	  db.execSQL(DBConstants.UnitEntity.CREATE_TABLE);
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

   }
}