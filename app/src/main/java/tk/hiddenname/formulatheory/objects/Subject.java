package tk.hiddenname.formulatheory.objects;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public class Subject {
   private long id;
   private String name;
   private int numOfFormulas, drawableId, color;
   //TEMP
   private ArrayList<Section> sections;

   public Subject() {
   }

   //TEMP CONSTRUCTOR
   public Subject(int nameId, int drawableId, int color, ArrayList<Section> sections, @NonNull Context context) {
	  this.color = color;
	  this.drawableId = drawableId;
	  this.sections = sections;
	  name = context.getString(nameId);
	  if(sections != null) {
		 for (Section section : sections) numOfFormulas += section.getNumOfFormulas();
	  } else numOfFormulas = 0;
   }
   //*********

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

   public void setColor(int color) {
	  this.color = color;
   }

   public int getColor() {
	  return color;
   }

   public void setDrawableId(int drawableId) {
	  this.drawableId = drawableId;
   }

   public int getDrawableId() {
	  return drawableId;
   }

   public void setNumOfFormulas(int numOfFormulas) {
	  this.numOfFormulas = numOfFormulas;
   }

   public int getNumOfFormulas() {
	  return numOfFormulas;
   }

   //TEMP METHODS
   public ArrayList<Section> getSections() {
	  return sections;
   }

   @NonNull
   @Override
   public String toString() {
	  return "Subject{" +
					 "name = '" + name + '\'' +
					 ", numOfFormulas = " + numOfFormulas +
					 ", drawableId = " + drawableId +
					 ", color = " + color +
					 '}';
   }
}