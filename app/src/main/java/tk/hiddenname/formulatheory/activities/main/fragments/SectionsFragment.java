package tk.hiddenname.formulatheory.activities.main.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Objects;

import tk.hiddenname.formulatheory.R;
import tk.hiddenname.formulatheory.adapters.ComplexRecyclerViewAdapter;
import tk.hiddenname.formulatheory.adapters.RecyclerItemClickListener;
import tk.hiddenname.formulatheory.objects.Section;

public class SectionsFragment extends Fragment {

   private ParentFragment parent;
   private RecyclerView rv;
   private ArrayList<Object> sections;

   void setParent(ParentFragment parent) {
	  this.parent = parent;
   }

   private void setSections() {
	  sections = parent.getListActivity().getSections(parent.getSubjectId());
   }

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	  View view;
	  setSections();
	  if (sections != null) {
		 view = inflater.inflate(R.layout.fragment_content, container, false);
		 createRecyclerView(view);
	  } else {
		 view = inflater.inflate(R.layout.fragment_empty, container, false);
	  }
	  return view;
   }

   private void createRecyclerView(@NonNull View view) {
	  rv = view.findViewById(R.id.recycler);
	  rv.setHasFixedSize(false);
	  rv.setLayoutManager(new LinearLayoutManager(getActivity()));
	  rv.setAdapter(new ComplexRecyclerViewAdapter(sections, rv.getContext()));
   }

   @Override
   public void onResume() {
	  super.onResume();
	  if (rv != null)
		 rv.addOnItemTouchListener(new RecyclerItemClickListener(
		 		Objects.requireNonNull(getActivity()).getApplicationContext(), rv,
				 new RecyclerItemClickListener.OnItemClickListener() {
					@Override
					public void onItemClick(View view, int position) {
					   FormulasFragment formulasFragment = new FormulasFragment();
					   Section section = (Section) sections.get(position);
					   formulasFragment.setSectionId(section.getId());
					   formulasFragment.setParent(parent);
					   parent.replace(formulasFragment);
					}

					@Override
					public void onLongItemClick(View view, int position) {

					}
				 }));
   }
}