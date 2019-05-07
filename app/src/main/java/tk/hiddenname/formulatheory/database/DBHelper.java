package tk.hiddenname.formulatheory.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import tk.hiddenname.formulatheory.R;
import tk.hiddenname.formulatheory.objects.Formula;
import tk.hiddenname.formulatheory.objects.Section;
import tk.hiddenname.formulatheory.objects.Subject;
import tk.hiddenname.formulatheory.objects.UnitsTest;

public class DBHelper extends SQLiteOpenHelper {

   private static final int DATABASE_VERSION = 1;
   private static final String DATABASE_NAME = "sample_database";
   private Context context;

   DBHelper(Context context) {
	  super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  Log.d("LogDB", "Открываем БД");
	  this.context = context;
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
	  firstAddDataToDB(db);
   }

   private void firstAddDataToDB(SQLiteDatabase db) {
	  List<Subject> subjects = createDataSubject();
	  ContentValues cv = new ContentValues();
	  int subjectId = 0, sectionId = 0, formulaSubSectionId = 0, formulaId = 0;
	  Log.d("LogDB", "*************************** создаём данные ******************************");
	  //***************** Добавление предметов в таблицу subject ***********************************
	  if (subjects.size() > 0) {
		 for (Subject subject : subjects) {
			cv.clear();
			cv.put(DBConstants.SubjectEntity.COLUMN_NAME, subject.getName());
			cv.put(DBConstants.SubjectEntity._ID, subjectId);
			cv.put(DBConstants.SubjectEntity.COLUMN_NUM_OF_FORMULAS, subject.getNumOfFormulas());
			cv.put(DBConstants.SubjectEntity.COLUMN_PHOTO_ID, subject.getDrawableId());
			cv.put(DBConstants.SubjectEntity.COLUMN_COLOR, subject.getColor());
			Log.d("LogDB", "Предмет " + cv.toString());
			db.insert(DBConstants.SubjectEntity.TABLE_NAME, null, cv);
			//******************** Добавление разделов предмета в таблицу section ******************
			if (subject.getSections() != null) {
			   for (Section section : subject.getSections()) {
				  cv.clear();
				  cv.put(DBConstants.SectionEntity.COLUMN_NAME, section.getName());
				  cv.put(DBConstants.SectionEntity._ID, sectionId);
				  cv.put(DBConstants.SectionEntity.COLUMN_NUM_OF_FORMULAS, section.getNumOfFormulas());
				  cv.put(DBConstants.SectionEntity.COLUMN_SUBJECT_ID, subjectId);
				  Log.d("LogDB", "Раздел " + cv.toString());
				  db.insert(DBConstants.SectionEntity.TABLE_NAME, null, cv);
				  // ******* Добравления подраздела с формулами в таблицу formula_subsection *******
				  if (section.getFormulas() != null) {
					 for (Formula formula : section.getFormulas()) {
						cv.clear();
						cv.put(DBConstants.FormulaObjectEntity.COLUMN_DESCRIPTION, formula.getName());
						cv.put(DBConstants.FormulaObjectEntity._ID, formulaSubSectionId);
						cv.put(DBConstants.FormulaObjectEntity.COLUMN_SECTION_ID, sectionId);
						Log.d("LogDB", "Формула " + cv.toString());
						db.insert(DBConstants.FormulaObjectEntity.TABLE_NAME, null, cv);
						//****************** Добавление формул в таблицу formula *******************
						if (formula.getFormulas().length > 0) {
						   for (String formula_str : formula.getFormulas()) {
							  cv.clear();
							  cv.put(DBConstants.FormulaEntity._ID, formulaId);
							  cv.put(DBConstants.FormulaEntity.COLUMN_FORMULA, formula_str);
							  cv.put(DBConstants.FormulaEntity.COLUMN_FORMULA_SUBSECTION_ID, formulaSubSectionId);
							  Log.d("LogDB", "формулы " + cv.toString());
							  db.insert(DBConstants.FormulaEntity.TABLE_NAME, null, cv);
							  formulaId++;
						   }
						}
						formulaSubSectionId++;
					 }
				  }
				  sectionId++;
			   }
			}
			subjectId++;
		 }
	  }
	  List<UnitsTest> unitsTests = createDataUnitsTest();
	  int unitObjectId = 0, unitId = 0;
	  if (unitsTests.size() > 0) {
		 for (UnitsTest unitsTest : unitsTests) {
			cv.clear();
			cv.put(DBConstants.UnitObjectEntity._ID, unitObjectId);
			cv.put(DBConstants.UnitObjectEntity.COLUMN_HINT, unitsTest.getHint());
			cv.put(DBConstants.UnitObjectEntity.COLUMN_LETTER, unitsTest.getLetter());
			Log.d("LogDB", "единицы измерения (объект) " + cv.toString());
			db.insert(DBConstants.UnitObjectEntity.TABLE_NAME, null, cv);
			for (Map.Entry<String, Double> entry : unitsTest.getMap().entrySet()) {
			   cv.clear();
			   cv.put(DBConstants.UnitEntity._ID, unitId);
			   cv.put(DBConstants.UnitEntity.COLUMN_UNIT_NAME, entry.getKey());
			   cv.put(DBConstants.UnitEntity.COLUMN_UNIT_COEFF, entry.getValue());
			   cv.put(DBConstants.UnitEntity.COLUMN_UNIT_OBJECT_ID, unitObjectId);
			   Log.d("LogDB", "единицы измерения " + cv.toString());
			   db.insert(DBConstants.UnitEntity.TABLE_NAME, null, cv);
			   unitId++;
			}
			unitObjectId++;
		 }
	  }
   }

   private List<Subject> createDataSubject() {
	  Log.d("LogDB", "создаём данные");
	  /* *********** СПИСОК ФОРМУЛ **************** */
	  ArrayList<Formula> formulas = new ArrayList<>();
	  formulas.add(new Formula(R.array.pressOfBody, context));
	  formulas.add(new Formula(R.array.pressOfWater, context));
	  /* ********** СПИСОК РАЗДЕЛОВ *************** */
	  ArrayList<Section> sections = new ArrayList<>();
	  sections.add(new Section(R.string.hydrostatics, formulas, context));
	  /* ********** СПИСОК ПРЕДМТОВ *************** */
	  ArrayList<Subject> subjects = new ArrayList<>();
	  subjects.add(new Subject(R.string.physics, R.drawable.physics, android.R.color.holo_blue_light, sections, context));
	  subjects.add(new Subject(R.string.chemistry, R.drawable.chemistry, android.R.color.holo_red_light, null, context));
	  subjects.add(new Subject(R.string.algebra, R.drawable.algebra, android.R.color.holo_orange_light, null, context));
	  subjects.add(new Subject(R.string.geometry, R.drawable.geometry, android.R.color.holo_green_light, null, context));
	  return subjects;
   }

   @NotNull
   @Contract(" -> new")
   private List<UnitsTest> createDataUnitsTest() {
	  return new ArrayList<UnitsTest>() {{
		 add(new UnitsTest("P", "давление", new TreeMap<String, Double>() {{
			put("Па", 1.0);
			put("гПа", 100.0);
			put("кПа", 1000.0);
			put("МПа", 1000000.0);
			put("мПа", 0.001);
			put("мкПа", 0.000001);
		 }}));
		 add(new UnitsTest("F", "сила", new TreeMap<String, Double>() {{
			put("Н", 1.0);
			put("кН", 1000.0);
			put("МН", 1000000.0);
			put("мН", 0.001);
			put("мкН", 0.000001);
		 }}));
		 add(new UnitsTest("S", "площадь", new TreeMap<String, Double>() {{
			put("км²", 1000000.0);
			put("га", 10000.0);
			put("а", 100.0);
			put("дм²", 0.01);
			put("см²", 0.0001);
			put("мм²", 0.000001);
			put("м²", 1.0);
		 }}));
		 add(new UnitsTest("h", "длина", new TreeMap<String, Double>() {{
			put("м", 1.0);
			put("см", 0.01);
			put("дм", 0.1);
			put("мм", 0.001);
			put("км", 1000.0);
		 }}));
		 add(new UnitsTest("ρ", "плотность", new TreeMap<String, Double>() {{
			put("кг/м³", 1.0);
			put("г/см³", 0.001);
		 }}));
		 add(new UnitsTest("g", "ускорение", new TreeMap<String, Double>() {{
			put("м/c²", 1.0);
		 }}));
	  }};
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

   }
}