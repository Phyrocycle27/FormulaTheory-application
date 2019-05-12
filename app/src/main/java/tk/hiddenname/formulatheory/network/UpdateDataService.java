package tk.hiddenname.formulatheory.network;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import tk.hiddenname.formulatheory.database.DBConstants;
import tk.hiddenname.formulatheory.database.DBHelper;
import tk.hiddenname.formulatheory.objects.Formula;
import tk.hiddenname.formulatheory.objects.Section;
import tk.hiddenname.formulatheory.objects.Subject;

public class UpdateDataService extends IntentService {

   public final static String ACTION = "tk.hiddenname.formulatheory.network.UpdateDataService.RESPONSE";
   public final static String DATABASE_STATUS_KEY = "RESULT_OF_OPEN_DATABASE";
   public final static String CLIENT_STATUS_KEY = "RESULT_OF_CONNECT_TO_SERVER";

   public UpdateDataService() {
	  super("UpdateDataService");
   }

   @Override
   public void onDestroy() {
	  super.onDestroy();
	  Log.d("UpdateDataService", "onDestroy");
   }

   @Override
   protected void onHandleIntent(Intent intent) {
	  HttpClient httpClient = new HttpClient();
	  ContentValues cv = new ContentValues();
	  DBHelper dbHelper = new DBHelper(this);
	  SQLiteDatabase db = dbHelper.getWritableDatabase();
	  // открываем БД
	  boolean dbStatus = true;
	  boolean clientStatus = true;
	  // Проверяем доступ к БД
	  if (db.isOpen()) {
		 // Проверяем соединение с сервером
		 if (httpClient.ping() == 0) {
			// Загружаем предметы с сервера, проверяем на пустоту ответ и добавляем в БД
			List<Subject> subjects = httpClient.getSubjects();
			if (!subjects.isEmpty()) {
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
				  if (!sections.isEmpty())
					 for (Section section : sections) {
						cv.clear();
						cv.put(DBConstants.SectionEntity.COLUMN_NAME, section.getName());
						cv.put(DBConstants.SectionEntity.COLUMN_SUBJECT_ID, subject.getId());
						cv.put(DBConstants.SectionEntity._ID, section.getId());
						// Загружаем объекты формул с сервера, проверяем на пустоту ответ и добавляем в БД
						int numOfFormulas = 0;
						List<Formula> formulas = httpClient.getFormulas(section.getId());
						if (!sections.isEmpty()) {
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
		 } else clientStatus = false;
	  } else dbStatus = false;
	  db.close();
	  dbHelper.close();
	  //Отправляем результат работы в вызывающую активность широковещательной рассылкой
	  Intent responseIntent = new Intent();
	  responseIntent.setAction(ACTION);
	  responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
	  responseIntent.putExtra(DATABASE_STATUS_KEY, dbStatus);
	  responseIntent.putExtra(CLIENT_STATUS_KEY, clientStatus);
	  sendBroadcast(responseIntent);
   }
}