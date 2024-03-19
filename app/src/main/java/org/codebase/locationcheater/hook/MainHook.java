package org.codebase.locationcheater.hook;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.codebase.locationcheater.hook.hookers.LocationHookers;
import org.codebase.locationcheater.hook.hookers.WifiManagerHookers;

import java.lang.reflect.Method;

import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.XposedModule;

public class MainHook extends XposedModule {

    /**
     * Instantiates a new Xposed module.<br/>
     * When the module is loaded into the target process, the constructor will be called.
     *
     * @param base  The implementation interface provided by the framework, should not be used by the module
     * @param param Information about the process in which the module is loaded
     */
    public MainHook(@NonNull XposedInterface base, @NonNull ModuleLoadedParam param) {
        super(base, param);
    }

    @Override
    public void onPackageLoaded(@NonNull PackageLoadedParam param) {
        super.onPackageLoaded(param);
        if (!param.isFirstPackage()) {
            return;
        }

        SharedPreferences settings = getRemotePreferences("settings");
        if (settings.getBoolean("enabled", false)) {
            ClassLoader classLoader = param.getClassLoader();
            try {
                this.hookLocation(classLoader);
                this.hookWifiManager(classLoader);
            } catch (Exception ignored) {

            }
        }
    }

    private void hookLocation(ClassLoader classLoader) throws Exception {
        Class<?> locationClass = Class.forName("android.location.Location", false, classLoader);
        Method getLatitudeMethod = locationClass.getDeclaredMethod("getLatitude");
        Method getLongitudeMethod = locationClass.getDeclaredMethod("getLongitude");
        hook(getLatitudeMethod, LocationHookers.GetLatitudeHooker.class);
        hook(getLongitudeMethod, LocationHookers.GetLongitudeHooker.class);
    }

    private void hookWifiManager(ClassLoader classLoader) throws Exception {
        Class<?> wifiManagerClass = Class.forName("android.net.wifi.WifiManager", false, classLoader);
        Method getScanResultsMethod = wifiManagerClass.getDeclaredMethod("getScanResults");
        hook(getScanResultsMethod, WifiManagerHookers.GetScanResultsHooker.class);
    }

}
