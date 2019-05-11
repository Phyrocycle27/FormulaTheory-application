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

   public HttpClient () {
	  serverAPI = new Retrofit.Builder()
						  .baseUrl("https://formula-theory-server.herokuapp.com")
						  .addConverterFactory(GsonConverterFactory.create())
						  .build()
						  .create(ServerAPI.class);
   }

   private void log(String message) {
	  Log.d("HttpClient", message);
   }

   private void addToDB(){

   }

   // ************** ПРЕДМЕТЫ ******************

   public List<Subject> getSubjects() {
	  Call<List<Subject>> subjects = serverAPI.getSubjects();
	  try {
		 Response<List<Subject>> response = subjects.execute();
		 if (response.isSuccessful()) return response.body();
		 else log("response code " + response.code());
	  } catch (IOException e) {
		 e.printStackTrace();
	  }
	  return null;
   }

   public Subject getSubject(long subjectId) {
	  try {
		 Response<Subject> response = serverAPI.getSubject(subjectId).execute();
		 if (response.isSuccessful()) return response.body();
		 else log("response code " + response.code());
	  } catch (IOException e) {
		 e.printStackTrace();
	  }
	  return null;
   }

   //**************** РАЗДЕЛЫ *********************

   public List<Section> getSections(long subjectId) {
	  try {
		 Response<List<Section>> response = serverAPI.getSections(subjectId).execute();
		 if (response.isSuccessful()) return response.body();
		 else log("response code " + response.code());
	  } catch (IOException e) {
		 e.printStackTrace();
	  }
	  return null;
   }

   public Section getSection(long sectionId) {
	  try {
		 Response<Section> response = serverAPI.getSection(sectionId).execute();
		 if (response.isSuccessful()) return response.body();
		 else log("response code " + response.code());
	  } catch (IOException e) {
		 e.printStackTrace();
	  }
	  return null;
   }

   // ********************** ФОРМУЛЫ ***********************

   public Formula getFormula(long formulaId) {
	  try {
		 Response<Formula> response = serverAPI.getFormula(formulaId).execute();
		 if (response.isSuccessful()) return response.body();
		 else log("response code " + response.code());
	  } catch (IOException e) {
		 e.printStackTrace();
	  }
	  return null;
   }

   public List<Formula> getFormulas(long sectionId) {
	  try {
		 Response<List<Formula>> response = serverAPI.getFormulas(sectionId).execute();
		 if (response.isSuccessful()) return response.body();
		 else log("response code " + response.code());
	  } catch (IOException e) {
		 e.printStackTrace();
	  }
	  return null;
   }
}
