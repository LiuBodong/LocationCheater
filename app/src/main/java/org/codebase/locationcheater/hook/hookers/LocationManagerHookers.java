package org.codebase.locationcheater.hook.hookers;

import static org.codebase.locationcheater.hook.MainHook.module;

import android.location.Location;
import android.util.Pair;

import org.codebase.locationcheater.hook.ProfileDtoHolder;
import org.codebase.locationcheater.ui.dao.LocationDto;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.annotations.AfterInvocation;
import io.github.libxposed.api.annotations.BeforeInvocation;
import io.github.libxposed.api.annotations.XposedHooker;

public class LocationManagerHookers extends HookersHelper {


    public LocationManagerHookers(ClassLoader classLoader) {
        super(classLoader);
    }

    private static void setRandomLocation(Location location) {
        ProfileDtoHolder.doIfNonNull(profileDto -> {

            LocationDto locationDto = profileDto.getLocation();
            double originalLat = locationDto.getLatitude();
            double originalLng = locationDto.getLongitude();

            int maxDistance = 100; // 最大移动距离：100米

            Random random = new SecureRandom();
            // 随机生成 0 - 100 米的移动距离
            double distance = random.nextDouble() * maxDistance;
            // 随机生成 0 - 2π 弧度的方位角（0 弧度为正北，顺时针递增）
            double bearing = random.nextDouble() * 2 * Math.PI;

            double earthRadius = 6371000; // 地球半径，单位：米

            // 计算在正北（deltaNorth）和正东（deltaEast）方向上的移动量（单位：米）
            double deltaNorth = distance * Math.cos(bearing);
            double deltaEast = distance * Math.sin(bearing);

            // 将移动距离转换为经纬度的变化量
            double deltaLat = (deltaNorth / earthRadius) * (180 / Math.PI);
            double deltaLng = (deltaEast / (earthRadius * Math.cos(originalLat * Math.PI / 180))) * (180 / Math.PI);

            double newLat = originalLat + deltaLat;
            double newLng = originalLng + deltaLng;

            location.setLatitude(newLat);
            location.setLongitude(newLng);

            module.log("原始位置: " + originalLat + ", " + originalLng);
            module.log("移动距离: " + distance + " 米, 方位角: " + Math.toDegrees(bearing) + "°");
            module.log("新位置: " + newLat + ", " + newLng);
        });
    }

    @Override
    public List<Pair<MethodDescriptor, Class<? extends XposedInterface.Hooker>>> getHookList() {
        return List.of(
                Pair.create(MethodDescriptor.of("android.location.LocationManager", "getLastLocation"), GetLoationHooker.class),
                Pair.create(MethodDescriptor.of("android.location.LocationManager", "getLastKnownLocation", new Class<?>[]{String.class}), GetLoationHooker.class)
        );
    }

    @XposedHooker
    public static final class GetLoationHooker implements XposedInterface.Hooker {

        @BeforeInvocation
        public static void before(XposedInterface.BeforeHookCallback beforeHookCallback) {

        }

        @AfterInvocation
        public static void after(XposedInterface.AfterHookCallback afterHookCallback) {
            module.log("Hook Location");
            Object result = afterHookCallback.getResult();
            if (result instanceof Location) {
                setRandomLocation((Location) result);
            }
            afterHookCallback.setResult(result);
        }

    }

}
