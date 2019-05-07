package tk.hiddenname.formulatheory.objects;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Formula implements Parcelable {
   private String description;
   private int numOfFormulas;
   private long id;
   private String[] formulas;
   //TEMP
   private String formula;
   private String[] components;
   private static final String delimeters = "[=+\\-*(/)\\s]";

   public Formula() {
   }

   public Formula(int formulasArray, @NonNull Context context) {
	  String[] tmpFormulasArr;
	  tmpFormulasArr = context.getResources().getStringArray(formulasArray);
	  numOfFormulas = tmpFormulasArr.length - 1;
	  description = tmpFormulasArr[0];
	  formulas = new String[numOfFormulas];
	  System.arraycopy(tmpFormulasArr, 1, formulas, 0, numOfFormulas);
	  formula = formulas[0];
	  components = createComponets(formula);
   }

   public void setId(long id) {
	  this.id = id;
   }

   public long getId() {
	  return id;
   }

   public void setName(String description) {
	  this.description = description;
   }

   public String getName() {
	  return description;
   }

   private void setFormula() {
	  try {
		 formula = formulas[0];
		 components = createComponets(formula);
	  } catch (NullPointerException e) {
		 formula = "";
	  }
   }

   public String getFormula() {
	  return formula;
   }

   public void setFormulas(String[] formulas) {
	  this.formulas = formulas;
	  setFormula();
   }

   public String[] getFormulas() {
	  return formulas;
   }

   private String[] createComponets(@NotNull String str) {
	  ArrayList<String> strings = new ArrayList<>();
	  for (String component : str.split(delimeters))
		 if (!component.equals("")) strings.add(component);
	  return strings.toArray(new String[0]);
   }

   public String[] getComponents() {
	  return components;
   }

   public String getComponentByIndex(int index) {
	  return components[index];
   }

   int getNumOfFormulas() {
	  return numOfFormulas;
   }

   public float solve(Map<String, Double> values, Map<String, Double> units) {
	  // Неизвестный компонент и целевая формула
	  String unknownComponent = "", targetFormula = "";
	  // Определяем неизвестный компонент
	  for (String key : values.keySet()) {
		 // Проверяем значение компонента в таблице по его ключу
		 if (values.get(key) == null) {
			Log.d("Calculate", key);
			unknownComponent = key;
		 }
	  }

	  // Находим формулу, подходящую для нахождения значения неизвестного компонента
	  for (String formula : formulas)
		 if (formula.startsWith(unknownComponent)) targetFormula = formula;
	  // Заменяем буквы в формуле на известные нам значения и составляем выражение
	  Log.d("Calculate", "Current target formula is: " + targetFormula);
	  String[] str = targetFormula.split("[=]");
	  targetFormula = str[1].substring(1);
	  for (String component : components) {
		 if (!component.equals(unknownComponent)) {
			double val = values.get(component);
			val *= units.get(component);
			targetFormula = targetFormula.replaceFirst(component, String.valueOf(val));
		 }
	  }
	  // Вывод выражения в Log
	  Log.d("Calculate", "Current target formula after replace is: " + targetFormula);
	  // *****************Передаём полученное выражение на вычисление в новый поток***********************
	  // Создаём внутренний локальный поток для вычисления
	  final String finalExpression = targetFormula;
	  class SolveThread extends Thread {
		 @Override
		 public void run() {
			super.run();
		 }

		 private double calculate() {
			Expression expression = new Expression(finalExpression);
			return expression.calculate();
		 }
	  }
	  // Запускаем поток вычисления
	  SolveThread st = new SolveThread();
	  st.start();
	  // Возвращаем ответ на выражение
	  Log.d("Calculate", "Unknown component's unit is: " + units.get(unknownComponent));
	  return (float) (st.calculate() * units.get(unknownComponent));
   }

   @NotNull
   @Override
   public String toString() {
	  return "Formula{" +
					 "description = '" + description + '\'' +
					 ", numOfFormulas = " + numOfFormulas +
					 ", id = " + id +
					 ", formulas = " + Arrays.toString(formulas) +
					 ", formula = '" + formula + '\'' +
					 ", components = " + Arrays.toString(components) +
					 '}';
   }

   //PARCELABLE

   private Formula(@NonNull Parcel in) {
	  description = in.readString();
	  formula = in.readString();
	  numOfFormulas = in.readInt();
	  formulas = in.createStringArray();
	  components = in.createStringArray();
   }

   public static final Creator<Formula> CREATOR = new Creator<Formula>() {
	  @NotNull
	  @Contract("_ -> new")
	  @Override
	  public Formula createFromParcel(Parcel in) {
		 return new Formula(in);
	  }

	  @NotNull
	  @Contract(value = "_ -> new", pure = true)
	  @Override
	  public Formula[] newArray(int size) {
		 return new Formula[size];
	  }
   };

   @Override
   public int describeContents() {
	  return 0;
   }

   @Override
   public void writeToParcel(Parcel dest, int flags) {
	  dest.writeString(description);
	  dest.writeString(formula);
	  dest.writeInt(numOfFormulas);
	  dest.writeStringArray(formulas);
	  dest.writeStringArray(components);
   }
}