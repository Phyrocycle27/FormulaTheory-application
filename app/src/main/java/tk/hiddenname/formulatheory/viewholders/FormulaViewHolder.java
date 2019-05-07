package tk.hiddenname.formulatheory.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import tk.hiddenname.formulatheory.R;

public class FormulaViewHolder extends RecyclerView.ViewHolder {
   private TextView formula, description;

   public FormulaViewHolder(View v) {
	  super(v);
	  formula = v.findViewById(R.id.formula);
	  description = v.findViewById(R.id.description);
   }

   public TextView getLabel1() {
	  return formula;
   }

   public TextView getLabel2() {
	  return description;
   }
}