package org.codebase.locationcheater.hook;

import android.util.Pair;

import org.codebase.locationcheater.hook.hookers.HookersHelper;
import org.codebase.locationcheater.hook.hookers.LocationManagerHookers;
import org.codebase.locationcheater.hook.hookers.TelephoneHookers;
import org.codebase.locationcheater.hook.hookers.WifiManagerHookers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.github.libxposed.api.XposedInterface;

public class Hookers extends HookersHelper {

    public Hookers(ClassLoader classLoader) {
        super(classLoader);
    }

    @Override
    public List<Pair<MethodDescriptor, Class<? extends XposedInterface.Hooker>>> getHookList() {
        return Stream.of(
                        new WifiManagerHookers(classLoader),
                        new TelephoneHookers(classLoader),
                        // new LocationHookers(classLoader)
                        new LocationManagerHookers(classLoader)
                )
                .flatMap(hookersHelper -> hookersHelper.getHookList().stream())
                .collect(Collectors.toList());
    }

}
