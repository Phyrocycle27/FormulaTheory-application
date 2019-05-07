package tk.hiddenname.formulatheory.activities.main;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tk.hiddenname.formulatheory.R;
import tk.hiddenname.formulatheory.activities.main.fragments.ParentFragment;
import tk.hiddenname.formulatheory.objects.Subject;
import tk.hiddenname.formulatheory.database.DatabaseThread;

public class ListActivity extends AppCompatActivity {

   private static DatabaseThread dataThread;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_main);
	  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	  dataThread = new DatabaseThread(getApplicationContext());
	  dataThread.start();

	  final Toolbar toolbar = findViewById(R.id.toolbar);
	  setSupportActionBar(toolbar);

	  if (getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.app_name);
	  Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

	  final ViewPager viewPager = findViewById(R.id.viewpager);
	  setupViewPager(viewPager);

	  TabLayout tabLayout = findViewById(R.id.tabs);
	  tabLayout.setupWithViewPager(viewPager);

	  //final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
   }

   private void setupViewPager(ViewPager viewPager) {
	  Adapter adapter = new Adapter(getSupportFragmentManager());
	  for (Subject subject : getSubjects()) {
		 ParentFragment fragment = new ParentFragment();
		 fragment.setSubjectId(subject.getId());
		 fragment.setListActivity(this);
		 adapter.addFragment(fragment, subject.getName());
	  }
	  viewPager.setAdapter(adapter);
   }

   private class Adapter extends FragmentPagerAdapter {

	  private final List<Fragment> mFragments = new ArrayList<>();
	  private final List<String> mFragmentTitles = new ArrayList<>();

	  Adapter(FragmentManager fm) {
		 super(fm);
	  }

	  void addFragment(Fragment fragment, String title) {
		 mFragments.add(fragment);
		 mFragmentTitles.add(title);
	  }

	  @Override
	  public Fragment getItem(int position) {
		 return mFragments.get(position);
	  }

	  @Override
	  public int getCount() {
		 return mFragments.size();
	  }

	  @Override
	  public CharSequence getPageTitle(int position) {
		 return mFragmentTitles.get(position);
	  }
   }

   public ArrayList<Subject> getSubjects() {
      return dataThread.getSubjects();
   }

   public ArrayList<Object> getSections(long subjectId) {
      try {
		 return new ArrayList<Object>(dataThread.getSections(subjectId));
	  } catch (NullPointerException e) {
		 Log.e("LogDB", "У предмета с id = " + subjectId + " не найдено разделов");
         return null;
	  }
   }

   public ArrayList<Object> getFormulas(long sectionId) {
      try {
		 return new ArrayList<Object>(dataThread.getFormulaObjects(sectionId));
	  } catch (NullPointerException e) {
		 Log.e("LogDB", "У раздела с id = " + sectionId + " не найдено формул");
         return null;
	  }
   }

   @Contract(pure = true)
   public static DatabaseThread getDataThread(){
      return dataThread;
   }
}