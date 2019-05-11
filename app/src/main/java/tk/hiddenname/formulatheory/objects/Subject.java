package tk.hiddenname.formulatheory.objects;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public class Subject {
   private long id;
   private String name;
   private int color, code;
   //TEMP
   private ArrayList<Section> sections;

   public Subject() {
   }

   //TEMP CONSTRUCTOR
   public Subject(int nameId, int color, ArrayList<Section> sections, int code, @NonNull Context context) {
	  this.color = color;
	  this.sections = sections;
	  name = context.getString(nameId);
   }
   //*********

   public int getCode() {
	  return code;
   }

   public void setCode(int code) {
	  this.code = code;
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

   public void setColor(int color) {
	  this.color = color;
   }

   public int getColor() {
	  return color;
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
					 ", color = " + color +
					 ", id = " + id +
					 '}';
   }
}