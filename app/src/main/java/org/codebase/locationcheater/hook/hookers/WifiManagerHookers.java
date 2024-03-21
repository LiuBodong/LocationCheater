package org.codebase.locationcheater.hook.hookers;

import static org.codebase.locationcheater.hook.MainHook.module;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiSsid;
import android.util.Log;
import android.util.Pair;

import org.codebase.locationcheater.hook.ProfileDtoHolder;
import org.codebase.locationcheater.ui.dao.WifiDto;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.annotations.AfterInvocation;
import io.github.libxposed.api.annotations.BeforeInvocation;
import io.github.libxposed.api.annotations.XposedHooker;

@XposedHooker
public class WifiManagerHookers extends HookersHelper {

    public WifiManagerHookers(ClassLoader classLoader) {
        super(classLoader);
    }

    @Override
    public List<Pair<MethodDescriptor, Class<? extends XposedInterface.Hooker>>> getHookList() {
        final String className = WifiManager.class.getName();
        return List.of(
                Pair.create(MethodDescriptor.of(className, "getWifiState"), GetWifiStateHooker.class),
                Pair.create(MethodDescriptor.of(className, "getConnectionInfo"), GetConnectionInfoHooker.class),
                Pair.create(MethodDescriptor.of(className, "getScanResults"), GetScanResultsHooker.class)
        );
    }

    @XposedHooker
    public static final class GetWifiStateHooker implements XposedInterface.Hooker {
        @BeforeInvocation
        public static void before(XposedInterface.BeforeHookCallback beforeHookCallback) {

        }

        @AfterInvocation
        public static void after(XposedInterface.AfterHookCallback afterHookCallback) {
            ProfileDtoHolder.doIfNonNull(profileDto -> {
                if (profileDto.isWifiEnabled()) {
                    module.log("Wifi state is enabled");
                    afterHookCallback.setResult(WifiManager.WIFI_STATE_ENABLED);
                } else {
                    module.log("Wifi state is disabled");
                    afterHookCallback.setResult(WifiManager.WIFI_STATE_DISABLED);
                }
            });
        }
    }

    @XposedHooker
    public static final class GetConnectionInfoHooker implements XposedInterface.Hooker {
        @BeforeInvocation
        public static void before(XposedInterface.BeforeHookCallback beforeHookCallback) {

        }

        @AfterInvocation
        public static void after(XposedInterface.AfterHookCallback afterHookCallback) {
            ProfileDtoHolder.doIfNonNull(profileDto -> {
                if (profileDto != null) {
                    WifiDto connectedWifi = profileDto.getConnectedWifi();
                    WifiInfo wifiInfo = new WifiInfo.Builder()
                            .setBssid(connectedWifi.getMac())
                            .setSsid(connectedWifi.getSsid().getBytes(StandardCharsets.UTF_8))
                            .build();
                    Class<? extends WifiInfo> wifiInfoClass = wifiInfo.getClass();
                    try {
                        Field mMacAddress = wifiInfoClass.getDeclaredField("mMacAddress");
                        mMacAddress.setAccessible(true);
                        mMacAddress.set(wifiInfo, connectedWifi.getMac());
                    } catch (Exception ignored) {

                    }
                    afterHookCallback.setResult(wifiInfo);
                }
            });
        }
    }

    @XposedHooker
    public static final class GetScanResultsHooker implements XposedInterface.Hooker {

        @BeforeInvocation
        public static void before(XposedInterface.BeforeHookCallback beforeHookCallback) {

        }

        @AfterInvocation
        public static void after(XposedInterface.AfterHookCallback afterHookCallback) {
            ProfileDtoHolder.doIfNonNull(profileDto -> {
                List<ScanResult> scanResults = profileDto.getScanResults().stream().map(wifiDto -> {
                    ScanResult scanResult = new ScanResult();
                    scanResult.SSID = wifiDto.getSsid();
                    scanResult.BSSID = wifiDto.getMac();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                        Class<WifiSsid> wifiSsidClass = WifiSsid.class;
                        try {
                            Constructor<? extends WifiSsid> wifiSsidConstructor = wifiSsidClass.getDeclaredConstructor(byte[].class);
                            WifiSsid wifiSsid = wifiSsidConstructor.newInstance(wifiDto.getSsid().getBytes(StandardCharsets.UTF_8));
                            Class<? extends ScanResult> scanResultClass = scanResult.getClass();
                            Method setWifiSsidMethod = scanResultClass.getDeclaredMethod("setWifiSsid", WifiSsid.class);
                            setWifiSsidMethod.setAccessible(true);
                            setWifiSsidMethod.invoke(scanResult, wifiSsid);
                        } catch (Exception e) {
                            module.log("Error create scan result", e);
                        }
                    }
                    return scanResult;
                }).collect(Collectors.toList());
                afterHookCallback.setResult(scanResults);
            });
        }

    }

}
