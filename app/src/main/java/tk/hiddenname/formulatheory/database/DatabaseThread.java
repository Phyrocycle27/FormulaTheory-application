package tk.hiddenname.formulatheory.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import tk.hiddenname.formulatheory.objects.Formula;
import tk.hiddenname.formulatheory.objects.Section;
import tk.hiddenname.formulatheory.objects.Subject;
import tk.hiddenname.formulatheory.objects.Unit;

public class DatabaseThread extends Thread {

   private Cursor cursor;
   private SQLiteDatabase db;

   public DatabaseThread(Context context) {
	  DBHelper dbHelper = new DBHelper(context);
	  db = dbHelper.getWritableDatabase();
   }

   @Override
   public void run() {
      super.run();
	  Log.d("LogDB", "открываем БД для письма");
   }


   //********************** Получаем предметы из БД *************************
   public ArrayList<Subject> getSubjects() {
	  cursor = db.query(DBConstants.SubjectEntity.TABLE_NAME, DBConstants.SubjectEntity.SELECTED_COLUMNS,
			  null, null, null, null, null);
	  if (cursor != null && cursor.moveToFirst()) {
		 ArrayList<Subject> subjects = new ArrayList<>();
		 do {
			Subject subject = new Subject();
			for (String column : cursor.getColumnNames()) {
			   switch (column) {
				  case DBConstants.SubjectEntity._ID:
					 subject.setId(cursor.getLong(cursor.getColumnIndex(column)));
				  case DBConstants.SubjectEntity.COLUMN_NAME:
					 subject.setName(cursor.getString(cursor.getColumnIndex(column)));
					 break;
				  case DBConstants.SubjectEntity.COLUMN_COLOR:
					 subject.setColor(cursor.getInt(cursor.getColumnIndex(column)));
					 break;
				  case DBConstants.SubjectEntity.COLUMN_CODE:
				     subject.setCode(cursor.getInt(cursor.getColumnIndex(column)));
			   }
			}
			subjects.add(subject);
		 } while (cursor.moveToNext());
		 cursor.close();
		 Log.d("LogDB", "success getSubjects");
		 return subjects;
	  } else {
		 Log.d("LogDB", "Cursor Subject is null");
		 return null;
	  }
   }

   //*********************** Получаем разделы из БД ********************************
   public ArrayList<Section> getSections(long subjectId) {
	  cursor = db.query(DBConstants.SectionEntity.TABLE_NAME, DBConstants.SectionEntity.SELECTED_COLUMNS,
			  DBConstants.SectionEntity.COLUMN_SUBJECT_ID + " = ?",
			  new String[]{Long.toString(subjectId)}, null, null, null);
	  if (cursor != null && cursor.moveToFirst()) {
		 ArrayList<Section> sections = new ArrayList<>();
		 do {
			Section section = new Section();
			for (String column : cursor.getColumnNames()) {
			   switch (column) {
				  case DBConstants.SectionEntity._ID:
					 section.setId(cursor.getLong(cursor.getColumnIndex(column)));
					 break;
				  case DBConstants.SectionEntity.COLUMN_NAME:
					 section.setName(cursor.getString(cursor.getColumnIndex(column)));
					 break;
				  case DBConstants.SectionEntity.COLUMN_NUM_OF_FORMULAS:
					 section.setNumOfFormulas(cursor.getInt(cursor.getColumnIndex(column)));
			   }
			}
			sections.add(section);
		 } while (cursor.moveToNext());
		 cursor.close();
		 Log.d("LogDB", "success getSections");
		 return sections;
	  } else {
		 Log.d("LogDB", "Cursor Sections is null");
		 return null;
	  }
   }

   //********************************* Получаем объекты формул из БД ********************
   public ArrayList<Formula> getFormulaObjects(long sectionId) {
	  cursor = db.query(DBConstants.FormulaObjectEntity.TABLE_NAME, DBConstants.FormulaObjectEntity.SELECTED_COLUMNS,
			  DBConstants.FormulaObjectEntity.COLUMN_SECTION_ID + " = ?",
			  new String[]{Long.toString(sectionId)}, null, null, null);
	  if (cursor != null && cursor.moveToFirst()) {
		 ArrayList<Formula> formulasObject = new ArrayList<>();
		 do {
			Formula formula = new Formula();
			for (String column : cursor.getColumnNames()) {
			   switch (column) {
				  case DBConstants.FormulaObjectEntity._ID:
					 formula.setId(cursor.getLong(cursor.getColumnIndex(column)));
					 break;
				  case DBConstants.FormulaObjectEntity.COLUMN_DESCRIPTION:
					 formula.setName(cursor.getString(cursor.getColumnIndex(column)));
			   }
			}
			formula.setFormulas(getFormulas(formula.getId()));
			formulasObject.add(formula);
		 } while (cursor.moveToNext());
		 cursor.close();
		 Log.d("LogDB", "success getFormulaObjects");
		 return formulasObject;
	  } else {
		 Log.d("LogDB", "Cursor Formulas is null");
		 return null;
	  }
   }

   //******************************** Получаем формулы из БД ****************************
   @Nullable
   private String[] getFormulas(long formulaObjectId) {
	  Cursor c = db.query(DBConstants.FormulaEntity.TABLE_NAME, DBConstants.FormulaEntity.SELECTED_COLUMNS,
			  DBConstants.FormulaEntity.COLUMN_FORMULA_SUBSECTION_ID + " = ?",
			  new String[]{Long.toString(formulaObjectId)}, null, null, null);
	  if (c != null && c.moveToFirst()) {
		 ArrayList<String> formulas = new ArrayList<>();
		 do {
			formulas.add(c.getString(c.getColumnIndex(DBConstants.FormulaEntity.COLUMN_FORMULA)));
		 } while (c.moveToNext());
		 c.close();
		 return formulas.toArray(new String[0]);
	  } else {
		 Log.d("LogDB", "Cursor formula is null");
		 return null;
	  }
   }

   //****************************** Получаем объекты единиц измерения из БД ***********************
   public Unit getUnitByLetter(String letter) {
	  cursor = db.query(DBConstants.UnitObjectEntity.TABLE_NAME, DBConstants.UnitObjectEntity.SELECTED_COLUMNS,
			  DBConstants.UnitObjectEntity.COLUMN_LETTER + " = ?", new String[]{letter},
			  null, null, null);
	  if (cursor != null && cursor.moveToFirst()) {
		 Unit unit = new Unit();
		 int unitObjectId = 0;
		 for (String column : cursor.getColumnNames()) {
			switch (column) {
			   case DBConstants.UnitObjectEntity._ID:
				  unitObjectId = cursor.getInt(cursor.getColumnIndex(column));
				  break;
			   case DBConstants.UnitObjectEntity.COLUMN_HINT:
				  unit.setHint(cursor.getString(cursor.getColumnIndex(column)));
				  break;
			   case DBConstants.UnitObjectEntity.COLUMN_LETTER:
				  unit.setLetter(cursor.getString(cursor.getColumnIndex(column)));
			}
		 }
		 cursor.close();
		 unit.setMap(getUnits(unitObjectId));
		 return unit;
	  } else {
		 Log.d("LogDB", "Cursor unit object is null");
		 return null;
	  }
   }

   //******************************* Получаем единицы измерения из БД ****************************
   @Nullable
   private Map<String, Double> getUnits(long unitObjectId) {
	  cursor = db.query(DBConstants.UnitEntity.TABLE_NAME, DBConstants.UnitEntity.SELECTED_COLUMNS,
			  DBConstants.UnitEntity.COLUMN_UNIT_OBJECT_ID + " = ?",
			  new String[]{Long.toString(unitObjectId)}, null, null, null);
	  if (cursor != null && cursor.moveToFirst()) {
		 Map<String, Double> map = new TreeMap<>();
		 do {
			String key = "";
			double value = 0.0;
			for (String column : cursor.getColumnNames()) {
			   switch (column) {
				  case DBConstants.UnitEntity.COLUMN_UNIT_COEFF:
					 value = cursor.getDouble(cursor.getColumnIndex(column));
					 break;
				  case DBConstants.UnitEntity.COLUMN_UNIT_NAME:
					 key = cursor.getString(cursor.getColumnIndex(column));
			   }
			}
			map.put(key, value);
		 } while (cursor.moveToNext());
		 cursor.close();
		 return map;
	  } else {
		 Log.d("LogDB", "Cursor units is null");
		 return null;
	  }
   }
}