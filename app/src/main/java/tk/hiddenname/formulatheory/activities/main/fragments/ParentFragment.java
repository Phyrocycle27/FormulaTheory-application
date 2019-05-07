package tk.hiddenname.formulatheory.activities.main.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tk.hiddenname.formulatheory.R;
import tk.hiddenname.formulatheory.activities.main.ListActivity;

public class ParentFragment extends Fragment {

   private long subjectId;
   private ListActivity listActivity;

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	  return inflater.inflate(R.layout.fragment_container, container, false);
   }

   @Override
   public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
	  SectionsFragment sectionsFragment = new SectionsFragment();
	  sectionsFragment.setParent(this);
	  replace(sectionsFragment);
   }

   public void setListActivity(ListActivity listActivity) {
	  this.listActivity = listActivity;
   }

   public ListActivity getListActivity() {
	  return listActivity;
   }

   public long getSubjectId() {
	  return subjectId;
   }

   public void setSubjectId(long id) {
	  subjectId = id;
   }

   void replace(Fragment childFragment) {
	  FragmentTransaction ft = getChildFragmentManager().beginTransaction();
	  ft.replace(R.id.child_fragment_container, childFragment).addToBackStack(null).commit();
   }

}