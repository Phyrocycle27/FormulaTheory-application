package tk.hiddenname.formulatheory.activities.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tk.hiddenname.formulatheory.R;
import tk.hiddenname.formulatheory.activities.main.fragments.ParentFragment;
import tk.hiddenname.formulatheory.network.UpdateDataService;
import tk.hiddenname.formulatheory.objects.Subject;
import tk.hiddenname.formulatheory.database.DatabaseThread;

public class ListActivity extends AppCompatActivity {

   private static DatabaseThread dataThread;
   private UpdateBroadcastReceiver receiver;
   private SharedPreferences mSettings;
   private int shortAnimationDuration;
   private Intent service;
   public static final String APP_PREFERENCES = "mySettings",
		   APP_PREFERENCES_LOADED_DATA = "LoadedData";

   private View contentView, loadingView;
   private Toolbar toolbar;
   private ViewPager viewPager;
   private TabLayout tabs;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_main);
	  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	  shortAnimationDuration = getResources().getInteger(android.R.integer.config_longAnimTime);

	  mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

	  contentView = findViewById(R.id.main_content);
	  loadingView = findViewById(R.id.loading_spinner);
	  toolbar = findViewById(R.id.toolbar);
	  viewPager = findViewById(R.id.viewpager);
	  tabs = findViewById(R.id.tabs);

	  if (mSettings.contains(APP_PREFERENCES_LOADED_DATA)) {
		 if (!mSettings.getBoolean(APP_PREFERENCES_LOADED_DATA, false)) startService();
		 else {
			loadingView.setVisibility(View.GONE);
			contentView.setAlpha(0f);
			contentView.setVisibility(View.VISIBLE);

			contentView.animate()
					.alpha(1f)
					.setDuration(shortAnimationDuration)
					.setListener(null);
			initScreen();
		 }
	  } else startService();

	  //final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
   }

   private void crossfade() {
	  contentView.setAlpha(0f);
	  contentView.setVisibility(View.VISIBLE);

	  contentView.animate()
			  .alpha(1f)
			  .setDuration(shortAnimationDuration)
			  .setListener(null);

	  loadingView.animate()
			  .alpha(0f)
			  .setDuration(shortAnimationDuration)
			  .setListener(new AnimatorListenerAdapter() {
				 @Override
				 public void onAnimationEnd(Animator animation) {
					loadingView.setVisibility(View.GONE);
				 }
			  });
   }

   private void initScreen() {
	  //Запускаем поток для работы с БД
	  dataThread = new DatabaseThread(this);
	  dataThread.start();

	  setSupportActionBar(toolbar);
	  setupViewPager(viewPager);
	  tabs.setupWithViewPager(viewPager);

	  if (getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.app_name);
	  Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
   }

   private void startService() {
	  contentView.setVisibility(View.GONE);
	  //Запускаем сервис для получения данных из интернета
	  service = new Intent(ListActivity.this, UpdateDataService.class);
	  startService(service);

	  // регистрируем слушатель
	  receiver = new UpdateBroadcastReceiver();
	  IntentFilter intentFilter = new IntentFilter(UpdateDataService.ACTION);
	  intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
	  registerReceiver(receiver, intentFilter);
   }

   @Override
   protected void onDestroy() {
	  super.onDestroy();
	  if (receiver != null) unregisterReceiver(receiver);
   }

   public class UpdateBroadcastReceiver extends BroadcastReceiver {

	  @Override
	  public void onReceive(Context context, Intent intent) {
		 boolean dbStatus = intent.getBooleanExtra(UpdateDataService.DATABASE_STATUS_KEY, false);
		 boolean clientStatus = intent.getBooleanExtra(UpdateDataService.CLIENT_STATUS_KEY, false);
		 SharedPreferences.Editor editor = mSettings.edit();
		 if (dbStatus && clientStatus) {
			//Toast.makeText(ListActivity.this, "Данные успешно загружены", Toast.LENGTH_SHORT).show();
			editor.putBoolean(APP_PREFERENCES_LOADED_DATA, true);
			crossfade();
			initScreen();
		 } else if (!dbStatus && clientStatus) {
			Toast.makeText(ListActivity.this, "Ошибка при открытии БД", Toast.LENGTH_SHORT).show();
			editor.putBoolean(APP_PREFERENCES_LOADED_DATA, false);
		 } else if (dbStatus && !clientStatus) {
			Toast.makeText(ListActivity.this, "Ошибка при подключении к серверу", Toast.LENGTH_SHORT).show();
			editor.putBoolean(APP_PREFERENCES_LOADED_DATA, false);
		 }
		 editor.apply();
		 stopService(service);
		 service = null;
	  }
   }

   private void setupViewPager(ViewPager viewPager) {
	  Adapter adapter = new Adapter(getSupportFragmentManager());
	  try {
		 for (Subject subject : getSubjects()) {
			ParentFragment fragment = new ParentFragment();
			fragment.setSubjectId(subject.getId());
			fragment.setListActivity(this);
			adapter.addFragment(fragment, subject.getName());
		 }
	  } catch (NullPointerException e) {
		 e.printStackTrace();
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
   public static DatabaseThread getDataThread() {
	  return dataThread;
   }
}