package adventure.action.updatechecker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Alec Lee on 9/2/16.
 *
 * Sometimes you want to act on users updating their apps (upsell new feature, warn users a
 * preexisting feature is gone, do some work behind the scenes, etc), but it can be hard to
 * differentiate an app update from a fresh app install.
 *
 *
 * In your application's onCreate(), call: UpdateChecker.check(BuildConfig.VERSION_CODE, myContext)
 */
public class UpdateChecker {

	public static final String DEFAULT_PREFSTORE = "android_update_checker";
	private static final String VERSION_PREF_NAME = "version_code";

	public static class UpdateResponse {
		public UpdateState state;
		public int lastSavedVersionCode;

		/* package */ UpdateResponse(UpdateState state, int lastSavedVersionCode) {
			this.state = state;
			this.lastSavedVersionCode = lastSavedVersionCode;
		}
	}

	public enum UpdateState {
		UPDATED,		// User has updated from an older version to the current version
		FIRST_LAUNCH,	// User is using the app for the first time or has cleared app data.
		NO_CHANGE,		// User has used this version of the app before
	}

	public static UpdateResponse check(int versionCode, Context context) {
		return check(versionCode, context, DEFAULT_PREFSTORE);
	}

	public static UpdateResponse check(int versionCode, Context context, String preferenceName) {
		if (preferenceName == null || preferenceName.length() == 0) {
			Log.e("UpdateChecker", "Provided null or empty SharedPreference name");
			return null;
		}
		if (context == null) {
			Log.e("UpdateChecker", "Provided null context");
			return null;
		}

		return check(versionCode, context.getSharedPreferences(preferenceName, Activity.MODE_PRIVATE));
	}

	public static UpdateResponse check(int versionCode, SharedPreferences preferredPreferenceStore) {
		if (preferredPreferenceStore == null) {
			Log.e("UpdateChecker", "provided SharedPreferences null");
			return null;
		}
		if (versionCode <= 0) {
			Log.e("UpdateChecker", "provided versionCode less than 1");
			return null;
		}

		int savedVersionCode = preferredPreferenceStore.getInt("version_code", -1);
		preferredPreferenceStore.edit().putInt(VERSION_PREF_NAME, versionCode).apply();
		if (savedVersionCode == -1) {
			return new UpdateResponse(UpdateState.FIRST_LAUNCH, savedVersionCode);
		} else if (savedVersionCode != versionCode) {
			return new UpdateResponse(UpdateState.UPDATED, savedVersionCode);
		} else {
			return new UpdateResponse(UpdateState.NO_CHANGE, savedVersionCode);
		}
	}
}
