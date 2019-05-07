package tk.hiddenname.formulatheory.objects;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class UnitsTest {
   private String hint;
   private String letter;
   private Map<String, Double> map = new TreeMap<>();

   private static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
	  Comparator<K> valueComparator = new Comparator<K>() {
		 public int compare(K k1, K k2) {
			return Objects.requireNonNull(map.get(k1)).compareTo(map.get(k2));
		 }
	  };
	  Map<K, V> sortedByValues = new TreeMap<>(valueComparator);
	  sortedByValues.putAll(map);
	  return sortedByValues;
   }

   public UnitsTest() {
   }

   public UnitsTest(String letter, String hint, Map<String, Double> map) {
      this.letter = letter;
      this.hint = hint;
	  this.map = sortByValues(map);
   }

   public String getHint() {
	  return hint;
   }

   public void setHint(String hint) {
	  this.hint = hint;
   }

   public String getLetter() {
	  return letter;
   }

   public void setLetter(String letter) {
	  this.letter = letter;
   }

   public Map<String, Double> getMap() {
	  return map;
   }

   public void setMap(Map<String, Double> map) {
	  this.map = sortByValues(map);
   }
}
