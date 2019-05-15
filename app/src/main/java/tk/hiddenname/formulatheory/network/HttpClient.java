package tk.hiddenname.formulatheory.network;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tk.hiddenname.formulatheory.objects.Formula;
import tk.hiddenname.formulatheory.objects.Section;
import tk.hiddenname.formulatheory.objects.Subject;

class HttpClient {

   private ServerAPI serverAPI;

   HttpClient() {
	  serverAPI = new Retrofit.Builder()
						  .baseUrl("https://formula-theory-server.herokuapp.com")
						  .addConverterFactory(GsonConverterFactory.create())
						  .build()
						  .create(ServerAPI.class);
   }

   private void log(String message) {
	  Log.d("HttpClient", message);
   }

   List<Subject> getSubjects() {
	  try {
		 Response<List<Subject>> response = serverAPI.getSubjects().execute();
		 if (response.isSuccessful()) return response.body();
		 else log("response code " + response.code());
	  } catch (IOException e) {
		 e.printStackTrace();
	  }
	  return null;
   }

   Subject getSubject(long subjectId) {
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

   List<Section> getSections(long subjectId) {
	  try {
		 Response<List<Section>> response = serverAPI.getSections(subjectId).execute();
		 if (response.isSuccessful()) return response.body();
		 else log("response code " + response.code());
	  } catch (IOException e) {
		 e.printStackTrace();
	  }
	  return null;
   }

   Section getSection(long sectionId) {
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

   Formula getFormula(long formulaId) {
	  try {
		 Response<Formula> response = serverAPI.getFormula(formulaId).execute();
		 if (response.isSuccessful()) return response.body();
		 else log("response code " + response.code());
	  } catch (IOException e) {
		 e.printStackTrace();
	  }
	  return null;
   }

   List<Formula> getFormulas(long sectionId) {
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