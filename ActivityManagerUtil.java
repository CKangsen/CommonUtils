

import android.support.v4.app.Fragment;

import com.afmobi.palmgroup.ui.activity.BaseActivity;

import java.util.Stack;

public class ActivityManagerUtil {

	private static final String TAG = ActivityManagerUtil.class.getSimpleName();

	private Stack<BaseActivity> activityStack;
	private Stack<Fragment> fragmentStack;

	private static ActivityManagerUtil instance;

	/**
	 * 是否清空所有的导航，设置此变量主要是清空导航栈时，在baseActivity在从数据源删除数据，导航遍历关闭acitivity时出错
	 */
	public boolean isAllClear;
	private ActivityManagerUtil() {
		activityStack = new Stack<BaseActivity>();
	}

	public static ActivityManagerUtil getActivityManager() {
		if (instance == null) {
			instance = new ActivityManagerUtil();
		}
		return instance;
	}

	public static Stack<BaseActivity> getActivityStack() {
		return getActivityManager().activityStack;
	}

	public void popActivity(BaseActivity activity) {
		if (activity != null) {
			activity.finish();
			activityStack.remove(activity);
			LogUtils.i(TAG, "popActivity==" + activity);
			activity = null;
		}
	}

	public void popActivity(Class<?> cls) {
		for (int i = activityStack.size() - 1; i >= 0; i--) {
			BaseActivity activity = activityStack.get(i);
			if (activity != null) {
				if (activity.getClass().equals(cls)) {
					popActivity(activity);
					break;
				}
			} else {
				break;
			}
		}
	}

	public void popActivity() {
		if (activityStack.size() > 0) {
			BaseActivity activity = activityStack.pop();
			if (activity != null) {
				activity.finish();
				LogUtils.i(TAG, "popActivity==" + activity);
			}
		}
	}

	public BaseActivity currentActivity() {
		BaseActivity activity = null;
		if (!activityStack.empty()) {
			activity = activityStack.lastElement();
		}
		return activity;
	}

	public Fragment currentFragment() {
		Fragment fragment = null;
		if (!fragmentStack.empty()) {
			fragment = fragmentStack.lastElement();
			System.out.println("MyActivityManager  fragment  " + fragment);
		}
		return fragment;
	}

	public void pushActivity(BaseActivity activity) {
		LogUtils.i(TAG, "pushActivity==" + activity);
		activityStack.add(activity);
	}

	public boolean isExistsActivity(Class<?> cls) {
		boolean isExists = false;
		for (int i = 0; i < activityStack.size(); i++) {
			BaseActivity activity = activityStack.get(i);
			LogUtils.i(TAG, "   [ExistsActivity i=="+i+"=" + activity);
			if (cls.equals(activity.getClass())) {
				isExists = true;
				LogUtils.e(TAG, "   isExists=="+i+"=" + activity);
				break;
			}
		}
		return isExists;
	}

	/**
	 * 获取指定的activity
	 *
	 * @param cls
	 * @return
	 */
	public BaseActivity getActivity(Class<?> cls) {
		for (int i = 0; i < activityStack.size(); i++) {
			BaseActivity activity = activityStack.get(i);
			if (cls.equals(activity.getClass())) {
				return activity;
			}
		}
		return null;
	}

	public boolean IsEmpty() {
		return (null != activityStack && (0 == activityStack.size()));
	}

	public void popAllActivityExceptOne(Class<?> cls) {
		BaseActivity topActivity = null;
		while (true) {
			BaseActivity activity = currentActivity();
			if (activity == null) {
				break;
			}
			if (activity.getClass().equals(cls)) {
				activityStack.remove(activity);
				topActivity = activity;
				continue;
			}
			popActivity(activity);
		}
		if (topActivity != null) {
			activityStack.add(0, topActivity);
		}
	}

	/**
	 * get current activity
	 * 获取当前的 最前面的Acticity
	 * @return
	 */
	public BaseActivity getCurrentActivity() {
		if (activityStack != null && activityStack.size() > 0) {
			return activityStack.get(activityStack.size() - 1);
		}
		return null;
	}

	private void popAllActivity() {
		if (null != activityStack) {
			for (int i = 0; i < activityStack.size(); i++) {
				activityStack.get(i).finish();
			}
		}
	}
}
