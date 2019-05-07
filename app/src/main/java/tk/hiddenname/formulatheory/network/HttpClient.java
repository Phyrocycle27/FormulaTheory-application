package tk.hiddenname.formulatheory.network;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tk.hiddenname.formulatheory.objects.Formula;
import tk.hiddenname.formulatheory.objects.Section;
import tk.hiddenname.formulatheory.objects.Subject;

public class HttpClient {

   private ServerAPI serverAPI;

   public HttpClient() {
	  serverAPI = new Retrofit.Builder()
						  .baseUrl("https://formula-theory-server.herokuapp.com")
						  .addConverterFactory(GsonConverterFactory.create())
						  .build()
						  .create(ServerAPI.class);
   }

   private void log(String message) {
	  Log.d("HttpClient", message);
   }

   // ************** ПРЕДМЕТЫ ******************

   public void getSubjects() {
	  Call<List<Subject>> subjects = serverAPI.getSubjects();

	  try {
		 Response<List<Subject>> response = subjects.execute();
		 if (response.isSuccessful()) {
			log("response body size is " + response.body().size());
			log("response body is " + response.body().toString());
		 } else {
			log("response code " + response.code());
		 }
	  } catch (IOException e) {
		 e.printStackTrace();
	  }
   }

   public void getSubject(int subjectId) {
	  Call<Subject> subject = serverAPI.getSubject(subjectId);

	  try {
		 Response<Subject> response = subject.execute();
		 if (response.isSuccessful()) {
			log("response subject's name is " + response.body().getName());
			log("response body is " + response.body().toString());
		 } else {
			log("response code " + response.code());
		 }
	  } catch (IOException e) {
		 e.printStackTrace();
	  }
   }

   //**************** РАЗДЕЛЫ *********************

   public void getSections() {
	  Call<List<Section>> sections = serverAPI.getSections();

	  try {
		 Response<List<Section>> response = sections.execute();
		 if (response.isSuccessful()) {
			log("response body size is " + response.body().size());
			log("response body is " + response.body().toString());
		 } else {
			log("response code " + response.code());
		 }
	  } catch (IOException e) {
		 e.printStackTrace();
	  }
   }

   public void getSection(int sectionId) {
	  Call<Section> section = serverAPI.getSection(sectionId);

	  try {
		 Response<Section> response = section.execute();
		 if (response.isSuccessful()) {
			log("response subject's name is " + response.body().getName());
			log("response body is " + response.body().toString());
		 } else {
			log("response code " + response.code());
		 }
	  } catch (IOException e) {
		 e.printStackTrace();
	  }
   }

   public void getSectionsBySubjectId(int subjectId) {
	  Call<List<Section>> sections = serverAPI.getSections(subjectId);

	  try {
		 Response<List<Section>> response = sections.execute();
		 if (response.isSuccessful()) {
			log("response subject's name is " + response.body().toString());
			log("response body is " + response.body().toString());
		 } else {
			log("response code " + response.code());
		 }
	  } catch (IOException e) {
		 e.printStackTrace();
	  }
   }

   // ********************** ФОРМУЛЫ ***********************

   public void getFormulas() {
	  Call<List<Formula>> formulas = serverAPI.getFormulas();

	  try {
		 Response<List<Formula>> response = formulas.execute();
		 if (response.isSuccessful()) {
			log("response body size is " + response.body().size());
			log("response body is " + response.body().toString());
		 } else {
			log("response code " + response.code());
		 }
	  } catch (IOException e) {
		 e.printStackTrace();
	  }
   }

   public void getFormula(int formulaId) {
	  Call<Formula> formula = serverAPI.getFormula(formulaId);

	  try {
		 Response<Formula> response = formula.execute();
		 if (response.isSuccessful()) {
			log("response formula name is " + response.body().getName());
			log("response body is " + response.body().toString());
		 } else {
			log("response code " + response.code());
		 }
	  } catch (IOException e) {
		 e.printStackTrace();
	  }
   }

   public void getFormulasBySectionId(int sectionId) {
	  Call<List<Formula>> formulas = serverAPI.getFormulas(sectionId);

	  try {
		 Response<List<Formula>> response = formulas.execute();
		 if (response.isSuccessful()) {
			log("response body size is " + response.body().size());
			log("response body is " + response.body().toString());
		 } else {
			log("response code " + response.code());
		 }
	  } catch (IOException e) {
		 e.printStackTrace();
	  }
   }
}
