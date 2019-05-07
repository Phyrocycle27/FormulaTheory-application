package tk.hiddenname.formulatheory.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tk.hiddenname.formulatheory.objects.Formula;
import tk.hiddenname.formulatheory.objects.Section;
import tk.hiddenname.formulatheory.objects.Subject;
import tk.hiddenname.formulatheory.objects.UnitsTest;

public interface ServerAPI {

   //************ ПРЕДМЕТЫ **************

   @GET("subject/getSubjects")
   Call<List<Subject>> getSubjects();

   @GET("subject/getSubject")
   Call<Subject> getSubject(@Query("id") int subjectId);

   //************ РАЗДЕЛЫ ***************

   @GET("section/getSections")
   Call<List<Section>> getSections(@Query("subjectId") int subjectId);

   @GET("section/getSection")
   Call<Section> getSection(@Query("id") int sectionId);

   //************ ФОРМУЛЫ *************

   @GET("formulaObject/getFormulasObjects")
   Call<List<Formula>> getFormulasObjects(@Query("sectionId") int sectionId);

   @GET("formulaObject/getFormulaObject")
   Call<Formula> getFormulaObject(@Query("id") int formulaObjectId);

   @GET("formula/getFormulas")
   Call<List<Formula>> getFormulas(@Query("formulaObjectId") int formulaObjectId);

   @GET("formula/getFormula")
   Call<Formula> getFormula(@Query("id") int formulaId);

   //************* ЕДИНИЦЫ ИЗМЕРЕНИЯ ************

   @GET("unit/getUnits")
   Call<List<UnitsTest>> getUnitObjecs();

   @GET("unit/getUnit")
   Call<UnitsTest> getUnits(@Query("id") int unitId);
}