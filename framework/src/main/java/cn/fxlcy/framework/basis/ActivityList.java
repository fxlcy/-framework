package cn.fxlcy.framework.basis;

import android.app.Activity;

public interface ActivityList {
	void addActivity(Activity activity);

	Activity getCurrentActivity();
	
	boolean removeActivity(Activity object);
	
	Activity removeActivity(int position);
	
	boolean removeActivity(Class<? extends Activity> clazz);
	
	Activity getActivity(int position);

	<A extends Activity> A getActivity(int position, Class<A> clazz);

	void clearActivity();
	
	void finishActivity(Activity activity);
	
	void finishActivity(int position);
	
	void finishActivity(Class<? extends Activity> clazz);
	
	void finishAllActivity();
	
	boolean containsActivity(Activity activity);
	
	int activitySize();
}
