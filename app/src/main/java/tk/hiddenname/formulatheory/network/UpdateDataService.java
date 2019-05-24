package tk.hiddenname.formulatheory.network;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

import android.database.sqlite.SQLiteDatabase;

import android.util.Log;

import java.util.List;
import java.util.Map;

import tk.hiddenname.formulatheory.R;
import tk.hiddenname.formulatheory.database.DBConstants;
import tk.hiddenname.formulatheory.database.DBHelper;
import tk.hiddenname.formulatheory.objects.Formula;
import tk.hiddenname.formulatheory.objects.Section;
import tk.hiddenname.formulatheory.objects.Subject;
import tk.hiddenname.formulatheory.objects.Unit;

public class UpdateDataService extends IntentService {

   public final static String ACTION = "tk.hiddenname.formulatheory.network.UpdateDataService.RESPONSE";
   public final static String ACTION_UPDATE = "tk.hiddenname.formulatheory.network.UpdateDataService.UPDATE";
   public final static String DATABASE_STATUS_KEY = "RESULT_OF_OPEN_DATABASE";
   public final static String CLIENT_STATUS_KEY = "RESULT_OF_CONNECT_TO_SERVER";
   public final static String MESSAGE_KEY = "MESSAGE";
   private final String TAG = "UpdateDataService";

   public UpdateDataService() {
	  super("UpdateDataService");
   }

   @Override
   public void onDestroy() {
	  super.onDestroy();
	  Log.d(TAG, "onDestroy");
   }

   private boolean addDataToDB() {
	  Log.d(TAG, "addDataToDB");
	  HttpClient httpClient = new HttpClient();
	  ContentValues cv = new ContentValues();
	  DBHelper dbHelper = new DBHelper(this);
	  SQLiteDatabase db = dbHelper.getWritableDatabase();
	  boolean dbStatus = true;
	  // Проверяем доступ к БД
	  if (db.isOpen()) {
		 // Загружаем предметы с сервера, проверяем на пустоту ответ и добавляем в БД
		 List<Subject> subjects = httpClient.getSubjects();
		 if (subjects != null) {
			int formulaId = 0;
			for (Subject subject : subjects) {
			   cv.clear();
			   cv.put(DBConstants.SubjectEntity.COLUMN_NAME, subject.getName());
			   cv.put(DBConstants.SubjectEntity.COLUMN_COLOR, subject.getColor());
			   cv.put(DBConstants.SubjectEntity._ID, subject.getId());
			   cv.put(DBConstants.SubjectEntity.COLUMN_CODE, subject.getCode());
			   db.insert(DBConstants.SubjectEntity.TABLE_NAME, null, cv);
			   // Загружаем раззделы с сервера, проверяем на пустоту ответ и добавляем в БД
			   List<Section> sections = httpClient.getSections(subject.getId());
			   if (sections != null)
				  for (Section section : sections) {
					 cv.clear();
					 cv.put(DBConstants.SectionEntity.COLUMN_NAME, section.getName());
					 cv.put(DBConstants.SectionEntity.COLUMN_SUBJECT_ID, subject.getId());
					 cv.put(DBConstants.SectionEntity._ID, section.getId());
					 // Загружаем объекты формул с сервера, проверяем на пустоту ответ и добавляем в БД
					 int numOfFormulas = 0;
					 List<Formula> formulas = httpClient.getFormulas(section.getId());
					 if (formulas != null) {
						ContentValues formulaCv = new ContentValues();
						for (Formula formula : formulas) {
						   formulaCv.clear();
						   formulaCv.put(DBConstants.FormulaObjectEntity._ID, formula.getId());
						   formulaCv.put(DBConstants.FormulaObjectEntity.COLUMN_DESCRIPTION, formula.getDescription());
						   formulaCv.put(DBConstants.FormulaObjectEntity.COLUMN_SECTION_ID, section.getId());
						   db.insert(DBConstants.FormulaObjectEntity.TABLE_NAME, null, formulaCv);
						   // Получаем массив формул у объекта и загружаем его в БД
						   String[] formulasArr = formula.getFormulas();
						   if (formulasArr.length > 0)
							  for (String formulaStr : formulasArr) {
								 formulaCv.clear();
								 formulaCv.put(DBConstants.FormulaEntity._ID, formulaId);
								 formulaCv.put(DBConstants.FormulaEntity.COLUMN_FORMULA, formulaStr);
								 formulaCv.put(DBConstants.FormulaEntity.COLUMN_FORMULA_SUBSECTION_ID, formula.getId());
								 db.insert(DBConstants.FormulaEntity.TABLE_NAME, null, formulaCv);
								 numOfFormulas++;
								 formulaId++;
							  }
						}
					 }
					 cv.put(DBConstants.SectionEntity.COLUMN_NUM_OF_FORMULAS, numOfFormulas);
					 db.insert(DBConstants.SectionEntity.TABLE_NAME, null, cv);
				  }
			}
		 }
		 // Единицы измерения
		 List<Unit> units = new JsonParser().getUnits(httpClient.getUnitsJSON());
		 if (units != null) {
			int unitId = 0;
			for (Unit unit : units) {
			   cv.clear();
			   cv.put(DBConstants.UnitObjectEntity._ID, unit.getId());
			   cv.put(DBConstants.UnitObjectEntity.COLUMN_LETTER, unit.getLetter());
			   cv.put(DBConstants.UnitObjectEntity.COLUMN_HINT, unit.getHint());
			   db.insert(DBConstants.UnitObjectEntity.TABLE_NAME, null, cv);
			   for (Map.Entry entry : unit.getMap().entrySet()) {
				  cv.clear();
				  cv.put(DBConstants.UnitEntity._ID, unitId);
				  cv.put(DBConstants.UnitEntity.COLUMN_UNIT_NAME, entry.getKey().toString());
				  cv.put(DBConstants.UnitEntity.COLUMN_UNIT_COEFF, (Double) entry.getValue());
				  cv.put(DBConstants.UnitEntity.COLUMN_UNIT_OBJECT_ID, unit.getId());
				  db.insert(DBConstants.UnitEntity.TABLE_NAME, null, cv);
				  unitId++;
			   }
			}
		 }
	  } else dbStatus = false;
	  db.close();
	  dbHelper.close();
	  return dbStatus;
   }

   @Override
   protected void onHandleIntent(Intent intent) {
	  Log.d(TAG, "onHandleIntent");
	  if(NetworkUtil.isInternetConnected()) {
		 sendMessageToMain(getString(R.string.loading));
		 boolean dbStatus = addDataToDB();
		 sendMessageToMain(getString(R.string.success_loading));
		 finish(dbStatus);
	  } else {
		 try {
			sendMessageToMain(getString(R.string.failed_connect_to_server));
			Thread.sleep(100);
		 } catch (InterruptedException e) {
			e.printStackTrace();
		 }
	  }
   }

   private void sendMessageToMain(String message) {
	  Log.d(TAG, "sendMessageToMain");
	  Intent responseIntent = new Intent();
	  responseIntent.setAction(ACTION_UPDATE);
	  responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
	  responseIntent.putExtra(MESSAGE_KEY, message);
	  sendBroadcast(responseIntent);
   }

   private void finish(boolean dbStatus) {
	  Log.d(TAG, "finish");
	  Intent responseIntent = new Intent();
	  responseIntent.setAction(ACTION);
	  responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
	  responseIntent.putExtra(DATABASE_STATUS_KEY, dbStatus);
	  sendBroadcast(responseIntent);
   }
}