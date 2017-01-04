package nl.everlutions.buildscripting;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;

public class BaseApplication extends Application
        implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    public static final String TAG = BaseApplication.class.getSimpleName();
    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


        initCrashlyticsCrashReporter();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }


    private void initCrashlyticsCrashReporter() {
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(false).build())
                .build();
        final Fabric fabric = new Fabric.Builder(this)
                .kits(crashlyticsKit)
                .build();
        Fabric.with(fabric);
    }

    public static boolean wasInBackground = false;

    @Override
    public void onActivityCreated(Activity activity, Bundle arg1) {
        wasInBackground = false;
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle arg1) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        wasInBackground = false;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            wasInBackground = true;
        }
    }

    class ScreenOffReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            wasInBackground = true;
        }
    }

    public synchronized static BaseApplication getInstance() {
        return instance;
    }

}
