package tk.hiddenname.formulatheory.objects;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class Section {
   private String name;
   private long id;
   private int numOfFormulas;
   //TEMP
   private ArrayList<Formula> formulas;

   public Section() {
   }

   public void setId(long id) {
	  this.id = id;
   }

   public long getId() {
	  return id;
   }

   public void setName(String name) {
	  this.name = name;
   }

   public String getName() {
	  return name;
   }

   public void setNumOfFormulas(int numOfFormulas) {
	  this.numOfFormulas = numOfFormulas;
   }

   public int getNumOfFormulas() {
	  return numOfFormulas;
   }

   public ArrayList<Formula> getFormulas() {
	  return formulas;
   }

   @NonNull
   @Override
   public String toString() {
	  return "Section{" +
					 "name='" + name + '\'' +
					 ", id=" + id +
					 ", numOfFormulas=" + numOfFormulas +
					 '}';
   }
}