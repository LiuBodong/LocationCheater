package org.codebase.locationcheater.hook.hookers;

import android.net.wifi.ScanResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.annotations.AfterInvocation;
import io.github.libxposed.api.annotations.BeforeInvocation;
import io.github.libxposed.api.annotations.XposedHooker;

@XposedHooker
public class WifiManagerHookers {

    @XposedHooker
    public static final class GetScanResultsHooker implements XposedInterface.Hooker {

        @BeforeInvocation
        public static void before(XposedInterface.BeforeHookCallback beforeHookCallback) {

        }

        @AfterInvocation
        public static void after(XposedInterface.AfterHookCallback afterHookCallback) {
            ScanResult scanResult = new ScanResult();
        }

    }

}
