package tk.hiddenname.formulatheory.database;

class DBConstants {

   static class SubjectEntity {
	  static final String TABLE_NAME = "subject";
	  static final String COLUMN_NAME = "name";
	  static final String COLUMN_COLOR = "color";
	  static final String COLUMN_PHOTO_ID = "photo_id";
	  static final String _ID = "_id";
	  static final String COLUMN_NUM_OF_FORMULAS = "num_of_formulas";
	  static final String[] SELECTED_COLUMNS = new String[]{
			  _ID, COLUMN_NAME, COLUMN_NUM_OF_FORMULAS, COLUMN_COLOR, COLUMN_PHOTO_ID
	  };
	  static final String CREATE_TABLE =
			  "CREATE TABLE IF NOT EXISTS " +
					  TABLE_NAME + "( " +
					  _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					  COLUMN_NAME + " TEXT NOT NULL, " +
					  COLUMN_COLOR + " INTEGER NOT NULL, " +
					  COLUMN_PHOTO_ID + " INTEGER NOT NULL, " +
					  COLUMN_NUM_OF_FORMULAS + " INTEGER NOT NULL" + " )";
   }

   static class SectionEntity {
	  static final String TABLE_NAME = "section";
	  static final String COLUMN_NAME = "name";
	  static final String _ID = "_id";
	  static final String COLUMN_SUBJECT_ID = "subject_id";
	  static final String COLUMN_NUM_OF_FORMULAS = "num_of_formulas";
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

   static class FormulaObjectEntity {
	  static final String TABLE_NAME = "formula_object";
	  static final String _ID = "_id";
	  static final String COLUMN_DESCRIPTION = "description";
	  static final String COLUMN_SECTION_ID = "section_id";
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

   static class FormulaEntity {
	  static final String TABLE_NAME = "formula";
	  static final String _ID = "_id";
	  static final String COLUMN_FORMULA = "formula";
	  static final String COLUMN_FORMULA_SUBSECTION_ID = "formula_subsection_id";
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

   static class UnitObjectEntity {
	  static final String TABLE_NAME = "unit_object";
	  static final String _ID = "_id";
	  static final String COLUMN_LETTER = "letter";
	  static final String COLUMN_HINT = "hint";
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

   static class UnitEntity {
	  static final String TABLE_NAME = "unit";
	  static final String _ID = "_id";
	  static final String COLUMN_UNIT_NAME = "unit_name";
	  static final String COLUMN_UNIT_COEFF = "unit_coeff";
	  static final String COLUMN_UNIT_OBJECT_ID = "unit_object_id";
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