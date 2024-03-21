package org.codebase.locationcheater.hook.hookers;

import android.util.Pair;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import io.github.libxposed.api.XposedInterface;

public abstract class HookersHelper implements HookersBase {

    protected final ClassLoader classLoader;

    public HookersHelper(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public Method findMethod(MethodDescriptor methodDescriptor) throws Exception {
        Class<?> clazz = Class.forName(methodDescriptor.getClassName(), false, classLoader);
        Method method;
        try {
            method = clazz.getDeclaredMethod(methodDescriptor.getMethodName(), methodDescriptor.getParameterTypes());
        } catch (NoSuchMethodException e) {
            method = clazz.getMethod(methodDescriptor.getMethodName(), methodDescriptor.getParameterTypes());
        }
        return method;
    }

    public abstract List<Pair<MethodDescriptor, Class<? extends XposedInterface.Hooker>>> getHookList();


    @Override
    public Map<Method, Class<? extends XposedInterface.Hooker>> getHookMap() {
        return getHookList()
                .stream()
                .map(pair -> {
                    try {
                        return Pair.create(findMethod(pair.first), pair.second);
                    } catch (Exception e) {
                        return null;
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toMap(p -> p.first, p -> p.second, (l, r) -> r));
    }

    public static final class MethodDescriptor {

        private String className;

        private String methodName;

        private Class<?>[] parameterTypes;

        public MethodDescriptor(String className, String methodName) {
            this(className, methodName, new Class<?>[0]);
        }

        public MethodDescriptor(String className, String methodName, Class<?>[] parameterTypes) {
            this.className = className;
            this.methodName = methodName;
            this.parameterTypes = parameterTypes;
        }

        public static MethodDescriptor of(String className, String methodName) {
            return new MethodDescriptor(className, methodName);
        }

        public static MethodDescriptor of(String className, String methodName, Class<?>[] parameterTypes) {
            return of(className, methodName, parameterTypes);
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public Class<?>[] getParameterTypes() {
            return parameterTypes;
        }

        public void setParameterTypes(Class<?>[] parameterTypes) {
            this.parameterTypes = parameterTypes;
        }
    }


//    private void hookLocation(ClassLoader classLoader) throws Exception {
//        Class<?> locationClass = Class.forName("android.location.Location", false, classLoader);
//        Method getLatitudeMethod = locationClass.getDeclaredMethod("getLatitude");
//        Method getLongitudeMethod = locationClass.getDeclaredMethod("getLongitude");
//        hook(getLatitudeMethod, LocationHookers.GetLatitudeHooker.class);
//        hook(getLongitudeMethod, LocationHookers.GetLongitudeHooker.class);
//    }
//
//    private void hookWifiManager(ClassLoader classLoader) throws Exception {
//        Class<?> wifiManagerClass = Class.forName("android.net.wifi.WifiManager", false, classLoader);
//        Method getScanResultsMethod = wifiManagerClass.getDeclaredMethod("getScanResults");
//        hook(getScanResultsMethod, WifiManagerHookers.GetScanResultsHooker.class);
//    }

}
