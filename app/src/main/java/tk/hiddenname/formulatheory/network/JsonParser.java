package tk.hiddenname.formulatheory.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tk.hiddenname.formulatheory.objects.Unit;

class JsonParser {

   List<Unit> getUnits(String response) {
	  try {
		 JSONArray array = new JSONArray(response);
		 List<Unit> units = new ArrayList<>();
		 for (int i = 0; i < array.length(); i++) {
			JSONObject unitObj = array.getJSONObject(i);
			Unit unit = new Unit();
			unit.setId(unitObj.getInt("id"));
			unit.setLetter(unitObj.getString("letter"));
			unit.setHint(unitObj.getString("hint"));
			JSONArray unitsArr = unitObj.getJSONArray("units");
			Map<String, Double> unitsMap = new HashMap<>();
			for (int a = 0; a < unitsArr.length(); a++) {
			   JSONObject obj = unitsArr.getJSONObject(a);
			   unitsMap.put(obj.getString("name"), obj.getDouble("coeff"));
			}
			unit.setMap(unitsMap);
			units.add(unit);
		 }
		 return units;
	  } catch (JSONException e) {
	     e.printStackTrace();
	  }
	  return null;
   }
}
