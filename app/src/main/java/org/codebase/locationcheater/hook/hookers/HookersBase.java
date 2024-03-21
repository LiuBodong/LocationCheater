package org.codebase.locationcheater.hook.hookers;

import java.lang.reflect.Method;
import java.util.Map;

import io.github.libxposed.api.XposedInterface;

public interface HookersBase {

    Map<Method, Class<? extends XposedInterface.Hooker>> getHookMap();

}
