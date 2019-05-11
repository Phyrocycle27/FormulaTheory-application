package tk.hiddenname.formulatheory.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tk.hiddenname.formulatheory.objects.Formula;
import tk.hiddenname.formulatheory.objects.Section;
import tk.hiddenname.formulatheory.objects.Subject;
import tk.hiddenname.formulatheory.objects.Unit;

public interface ServerAPI {

   //************ ПРЕДМЕТЫ **************

   @GET("subject/getSubjects")
   Call<List<Subject>> getSubjects();

   @GET("subject/getSubject")
   Call<Subject> getSubject(@Query("id") long subjectId);

   //************ РАЗДЕЛЫ ***************

   @GET("section/getSections")
   Call<List<Section>> getSections(@Query("subjectId") long subjectId);

   @GET("section/getSection")
   Call<Section> getSection(@Query("id") long sectionId);

   //************ ФОРМУЛЫ *************

   @GET("formula/getFormulas")
   Call<List<Formula>> getFormulas(@Query("sectionId") long sectionId);

   @GET("formula/getFormula")
   Call<Formula> getFormula(@Query("id") long formulaId);

   //************* ЕДИНИЦЫ ИЗМЕРЕНИЯ ************

   @GET("unit/getUnits")
   Call<List<Unit>> getUnitObjects();

   @GET("unit/getUnit")
   Call<Unit> getUnits(@Query("id") long unitId);
}