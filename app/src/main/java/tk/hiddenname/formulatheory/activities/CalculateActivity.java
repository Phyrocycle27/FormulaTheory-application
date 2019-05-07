package tk.hiddenname.formulatheory.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import es.dmoral.toasty.Toasty;
import tk.hiddenname.formulatheory.R;
import tk.hiddenname.formulatheory.activities.main.ListActivity;
import tk.hiddenname.formulatheory.objects.Formula;
import tk.hiddenname.formulatheory.objects.UnitsTest;

public class CalculateActivity extends AppCompatActivity {

   private Formula formula;
   private Map<String, Double> enteredValues = new HashMap<>(), selectedUnits = new HashMap<>();
   private List<View> addedViews = new ArrayList<>();
   private ArrayList<UnitsTest> list = new ArrayList<>();

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_calculate);
	  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	  ListView listView = findViewById(R.id.listview);

	  //Получаем формулу через Intent
	  formula = getIntent().getParcelableExtra("formula");
	  ArrayAdapter<String> adapter =
			  new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, formula.getFormulas());
	  listView.setAdapter(adapter);

	  final LinearLayout linear = findViewById(R.id.linear);
	  for (int i = 0; i < formula.getComponents().length; i++) {
		 String component = formula.getComponentByIndex(i);
		 linear.addView(addField(component, i));
	  }
	  Log.d("Calculate", "(onCreate) HashMap \"values\" is: " + enteredValues.toString());
   }

   @SuppressLint("SetTextI18n")
   private View addField(final String component, final int i) {
	  @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.calculate_field, null);
	  list.add(ListActivity.getDataThread().getUnitByLetter(component));
	  TextView letter = view.findViewById(R.id.component_name);
	  EditText valueField = view.findViewById(R.id.value_field);
	  Spinner spinner = view.findViewById(R.id.units_spinner);
	  spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		 @Override
		 public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		    String[] strings = list.get(i).getMap().keySet().toArray(new String[0]);
			selectedUnits.put(component, Objects.requireNonNull(list.get(i).getMap().get(strings[position])));
			Log.d("Calculate", "(addField) HashMap \"units\" is: " + selectedUnits.toString());
		 }

		 @Override
		 public void onNothingSelected(AdapterView<?> parent) {

		 }
	  });
	  try {
		 ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
				 android.R.layout.simple_spinner_item, list.get(i).getMap().keySet().toArray(new String[0]));
		 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 spinner.setAdapter(adapter);
		 valueField.setHint(list.get(i).getHint());
	  } catch (NullPointerException e) {
		 e.printStackTrace();
	  }
	  letter.setText(component + " = ");
	  // ********** исключения
	  if (component.equals("g")) valueField.setText("9.81");
	  addedViews.add(view);
	  return view;
   }

   @Override
   protected void onDestroy() {
	  super.onDestroy();
	  formula = null;
	  enteredValues = null;
	  selectedUnits = null;
	  addedViews = null;
	  list = null;
   }

   @Override
   protected void onResume() {
	  super.onResume();
	  // Определяем кнопку "Решить" и устанавливаем на неё слушатель
	  final Button btn = findViewById(R.id.calculate_btn);
	  btn.setOnClickListener(new View.OnClickListener() {
		 @Override
		 public void onClick(View v) {
			//*********************** Убираем клавиатуру **********************************
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(btn.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			//**************************************************************************************
			View unknown = new View(getApplicationContext());
			//Передаём введённые значения в
			byte count = 0;
			for (int i = 0; i < addedViews.size(); i++) {
			   String str = ((EditText) addedViews.get(i).findViewById(R.id.value_field)).getText().toString();
			   if (str.equals("")) {
				  count++;
				  unknown = addedViews.get(i);
			   }
			   try {
				  enteredValues.put(formula.getComponentByIndex(i), Double.valueOf(str));
			   } catch (NumberFormatException e) {
				  enteredValues.put(formula.getComponentByIndex(i), null);
			   }
			}
			// Логируем значения данных в HashMap
			Log.d("Calculate", "(onResume) HashMap \"values\" is: " + enteredValues.toString());
			Log.d("Calculate", "(onResume) HashMap \"units\" is: " + selectedUnits.toString());
			// Выводим ответ в нужном формате
			if (count == 1) {
			   String tmpAnswr = String.valueOf(formula.solve(enteredValues, selectedUnits));
			   String answer;
			   String[] tmpArr = tmpAnswr.split("[.]");
			   Log.d("Calculate", "Answer: " + tmpAnswr);
			   if (tmpArr[1].startsWith("000")) answer = tmpArr[0];
			   else answer = tmpAnswr;
			   ((EditText) unknown.findViewById(R.id.value_field)).setText(answer);
			}
			// Иначе выводим тост с подсказкой
			else if (count >= 2) {
			   Toasty.warning(CalculateActivity.this, R.string.not_enough_data_to_solve, Toast.LENGTH_LONG, true).show();
			} else {
			   Toasty.warning(CalculateActivity.this, R.string.nothing_to_solve).show();
			}
			// очищаем массив
			for (String key : formula.getComponents()) enteredValues.put(key, null);
		 }
	  });
   }
}