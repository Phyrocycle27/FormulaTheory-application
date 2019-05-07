package tk.hiddenname.formulatheory.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import tk.hiddenname.formulatheory.R;

public class SectionViewHolder extends RecyclerView.ViewHolder {

   private TextView label1, label2;

   public SectionViewHolder(View v) {
	  super(v);
	  label1 = v.findViewById(R.id.name);
	  label2 = v.findViewById(R.id.numOfFormulas);
   }

   public TextView getLabel1() {
	  return label1;
   }

   public void setLabel1(TextView label1) {
	  this.label1 = label1;
   }

   public TextView getLabel2() {
	  return label2;
   }

   public void setLabel2(TextView label2) {
	  this.label2 = label2;
   }
}