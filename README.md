# AndroidUpdateChecker

Often times its useful to do some action on app update. Maybe that is upsell a new feature to users
or make some API requests behind the secenes. It can be hard though to differentiate a true app update and
a fresh install.


TODO:
better strategy? http://stackoverflow.com/questions/26352881/detect-if-new-install-or-updated-version-android-app

```
public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		
		UpdateChecker.UpdateState state = UpdateChecker.check(BuildConfig.VERSION_CODE, this);
		switch (state) {
		  case UPDATED:
		    // User has updated the app from an older version to the current version
		    break;
		  case FIRST_LAUNCH:
		    // User is using the app for the first time OR has cleared their app data.
		    // Note that first use of UpdateChecker will return FIRST_LAUNCH because UpdateChecker
		    // won't have a locally saved app version yet.
		    break;
		  case NO_CHANGE:
		    // User has used this version of the app before
		    break;
		}
	}
}
```
