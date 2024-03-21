package org.codebase.locationcheater.hook.hookers;

import android.location.Location;
import android.util.Pair;

import org.codebase.locationcheater.hook.ProfileDtoHolder;
import org.codebase.locationcheater.ui.dao.LocationDto;

import java.lang.reflect.Field;
import java.util.List;

import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.annotations.AfterInvocation;
import io.github.libxposed.api.annotations.BeforeInvocation;
import io.github.libxposed.api.annotations.XposedHooker;

public class LocationHookers extends HookersHelper {

    public LocationHookers(ClassLoader classLoader) {
        super(classLoader);
    }

    @Override
    public List<Pair<MethodDescriptor, Class<? extends XposedInterface.Hooker>>> getHookList() {
        String className = Location.class.getName();
        return List.of(
                Pair.create(MethodDescriptor.of(className, "getLongitude"), GetLongitudeHooker.class),
                Pair.create(MethodDescriptor.of(className, "getLatitude"), GetLatitudeHooker.class)
        );

    }

    @XposedHooker
    public static final class GetLatitudeHooker implements XposedInterface.Hooker {

        @BeforeInvocation
        public static void before(XposedInterface.BeforeHookCallback beforeHookCallback) {

        }

        @AfterInvocation
        public static void after(XposedInterface.AfterHookCallback afterHookCallback) {
            ProfileDtoHolder.doIfNonNull(profileDto -> {
                LocationDto location = profileDto.getLocation();
                Object result = afterHookCallback.getResult();
                if (result instanceof Location) {
                    Class<Location> locationClass = (Class<Location>) result.getClass();
                    try {
                        Field mLatitudeDegreesField = locationClass.getDeclaredField("mLatitudeDegrees");
                        mLatitudeDegreesField.setAccessible(true);
                        mLatitudeDegreesField.set(result, location.getLatitude());
                    } catch (Exception ignored) {

                    }
                }
                afterHookCallback.setResult(result);
            });
        }

    }

    @XposedHooker
    public static final class GetLongitudeHooker implements XposedInterface.Hooker {

        @BeforeInvocation
        public static void before(XposedInterface.BeforeHookCallback beforeHookCallback) {

        }

        @AfterInvocation
        public static void after(XposedInterface.AfterHookCallback afterHookCallback) {
            ProfileDtoHolder.doIfNonNull(profileDto -> {
                LocationDto location = profileDto.getLocation();
                Object result = afterHookCallback.getResult();
                if (result instanceof Location) {
                    Class<?> locationClass = result.getClass();
                    try {
                        Field mLatitudeDegreesField = locationClass.getDeclaredField("mLongitudeDegrees");
                        mLatitudeDegreesField.setAccessible(true);
                        mLatitudeDegreesField.set(result, location.getLongitude());
                    } catch (Exception ignored) {

                    }
                }
                afterHookCallback.setResult(result);
            });
        }

    }
}
