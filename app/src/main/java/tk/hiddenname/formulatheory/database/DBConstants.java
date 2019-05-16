package tk.hiddenname.formulatheory.database;

public class DBConstants {

   public static class SubjectEntity {
	  public static final String TABLE_NAME = "subject";
	  public static final String COLUMN_NAME = "name";
	  public static final String COLUMN_COLOR = "color";
	  public static final String COLUMN_CODE = "code";
	  public static final String _ID = "_id";
	  static final String[] SELECTED_COLUMNS = new String[]{
			  _ID, COLUMN_NAME, COLUMN_COLOR, COLUMN_CODE
	  };
	  static final String CREATE_TABLE =
			  "CREATE TABLE IF NOT EXISTS " +
					  TABLE_NAME + "( " +
					  _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					  COLUMN_NAME + " TEXT NOT NULL, " +
					  COLUMN_CODE + " INTEGER NOT NULL, " +
					  COLUMN_COLOR + " INTEGER NOT NULL " + " )";
   }

   public static class SectionEntity {
	  public static final String TABLE_NAME = "section";
	  public static final String COLUMN_NAME = "name";
	  public static final String _ID = "_id";
	  public static final String COLUMN_SUBJECT_ID = "subject_id";
	  public static final String COLUMN_NUM_OF_FORMULAS = "num_of_formulas";
	  static final String[] SELECTED_COLUMNS = new String[]{
			  COLUMN_NAME, COLUMN_NUM_OF_FORMULAS, COLUMN_SUBJECT_ID, _ID
	  };
	  static final String CREATE_TABLE =
			  "CREATE TABLE IF NOT EXISTS " +
					  TABLE_NAME + "( " +
					  _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					  COLUMN_NAME + " TEXT NOT NULL, " +
					  COLUMN_SUBJECT_ID + " INTEGER NOT NULL, " +
					  COLUMN_NUM_OF_FORMULAS + " INTEGER NOT NULL, " +
					  "FOREIGN KEY (" + COLUMN_SUBJECT_ID + ") REFERENCES " +
					  SubjectEntity.TABLE_NAME + "(" + SubjectEntity._ID + ") " + ")";

   }

   public static class FormulaObjectEntity {
	  public static final String TABLE_NAME = "formula_object";
	  public static final String _ID = "_id";
	  public static final String COLUMN_DESCRIPTION = "description";
	  public static final String COLUMN_SECTION_ID = "section_id";
	  static final String[] SELECTED_COLUMNS = new String[]{
			  _ID, COLUMN_DESCRIPTION, COLUMN_SECTION_ID
	  };
	  static final String CREATE_TABLE =
			  "CREATE TABLE IF NOT EXISTS " +
					  TABLE_NAME + "( " +
					  _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					  COLUMN_SECTION_ID + " INTEGER NOT NULL, " +
					  COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
					  "FOREIGN KEY (" + COLUMN_SECTION_ID + ") REFERENCES " +
					  SectionEntity.TABLE_NAME + "(" + SectionEntity._ID + ") " + ")";
   }

   public static class FormulaEntity {
	  public static final String TABLE_NAME = "formula";
	  public static final String _ID = "_id";
	  public static final String COLUMN_FORMULA = "formula";
	  public static final String COLUMN_FORMULA_SUBSECTION_ID = "formula_subsection_id";
	  static final String[] SELECTED_COLUMNS = new String[]{
			  COLUMN_FORMULA, COLUMN_FORMULA_SUBSECTION_ID
	  };
	  static final String CREATE_TABLE =
			  "CREATE TABLE IF NOT EXISTS " +
					  TABLE_NAME + "( " +
					  _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					  COLUMN_FORMULA_SUBSECTION_ID + " INTEGER NOT NULL, " +
					  COLUMN_FORMULA + " TEXT NOT NULL, " +
					  "FOREIGN KEY (" + COLUMN_FORMULA_SUBSECTION_ID + ") REFERENCES " +
					  FormulaObjectEntity.TABLE_NAME + "(" + FormulaObjectEntity._ID + ") " + ")";
   }

   public static class UnitObjectEntity {
	  public static final String TABLE_NAME = "unit_object";
	  public static final String _ID = "_id";
	  public static final String COLUMN_LETTER = "letter";
	  public static final String COLUMN_HINT = "hint";
	  static final String[] SELECTED_COLUMNS = new String[]{
			  _ID, COLUMN_HINT, COLUMN_LETTER
	  };
	  static final String CREATE_TABLE =
			  "CREATE TABLE IF NOT EXISTS " +
					  TABLE_NAME + "( " +
					  _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					  COLUMN_LETTER + " TEXT NOT NULL, " +
					  COLUMN_HINT + " TEXT NOT NULL" + " )";
   }

   public static class UnitEntity {
	  public static final String TABLE_NAME = "unit";
	  public static final String _ID = "_id";
	  public static final String COLUMN_UNIT_NAME = "name";
	  public static final String COLUMN_UNIT_COEFF = "coeff";
	  public static final String COLUMN_UNIT_OBJECT_ID = "unit_object_id";
	  static final String[] SELECTED_COLUMNS = new String[]{
	  		COLUMN_UNIT_COEFF, COLUMN_UNIT_NAME, COLUMN_UNIT_OBJECT_ID
	  };
	  static final String CREATE_TABLE =
			  "CREATE TABLE IF NOT EXISTS " +
					  TABLE_NAME + "( " +
					  _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					  COLUMN_UNIT_NAME + " TEXT NOT NULL, " +
					  COLUMN_UNIT_COEFF + " REAL NOT NULL, " +
					  COLUMN_UNIT_OBJECT_ID + " INTEGER NOT NULL, " +
					  "FOREIGN KEY (" + COLUMN_UNIT_OBJECT_ID + ") REFERENCES " +
					  UnitObjectEntity.TABLE_NAME + "(" + UnitObjectEntity._ID + ") " + ")";
   }
}