package org.codebase.locationcheater.hook.hookers;

import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.annotations.AfterInvocation;
import io.github.libxposed.api.annotations.BeforeInvocation;
import io.github.libxposed.api.annotations.XposedHooker;

public class LocationHookers {

    // 111.76,40.84

    @XposedHooker
    public static final class GetLatitudeHooker implements XposedInterface.Hooker {

        @BeforeInvocation
        public static void before(XposedInterface.BeforeHookCallback beforeHookCallback) {

        }

        @AfterInvocation
        public static void after(XposedInterface.AfterHookCallback afterHookCallback) {
            afterHookCallback.setResult(40.84D);
        }

    }

    public static final class GetLongitudeHooker implements XposedInterface.Hooker {

        @BeforeInvocation
        public static void before(XposedInterface.BeforeHookCallback beforeHookCallback) {

        }

        @AfterInvocation
        public static void after(XposedInterface.AfterHookCallback afterHookCallback) {
            afterHookCallback.setResult(111.76D);
        }

    }
}
