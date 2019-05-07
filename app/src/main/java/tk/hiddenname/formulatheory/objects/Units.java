package tk.hiddenname.formulatheory.objects;

import java.util.HashMap;
import java.util.Map;

public class Units {

   private Map<String, Unit> units = new HashMap<>();

   public void addUnit(String letter, String hint, Units.Unit unit) {
	  unit.hint = hint;
	  units.put(letter, unit);
   }

   public String[] getUnits(String letter) {
      try {
		 return units.get(letter).units.keySet().toArray(new String[0]);
	  } catch (NullPointerException e) {
         e.printStackTrace();
	  }
	  return null;
   }

   public Double getCoef(String letter, String unit) {
      return units.get(letter).getUnits().get(unit);
   }

   public String getHint(String letter) {
	  return units.get(letter).hint;
   }

   public class Unit {

	  private Map<String, Double> units;
	  private String hint;

	  public Unit(HashMap<String, Double> units) {
		 this.units = units;
	  }

	  Map<String, Double> getUnits() {
		 return units;
	  }
   }
}