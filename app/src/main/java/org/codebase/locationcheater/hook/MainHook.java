package org.codebase.locationcheater.hook;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.databind.json.JsonMapper;

import org.codebase.locationcheater.hook.hookers.HookersHelper;
import org.codebase.locationcheater.ui.dao.ProfileDto;

import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.XposedModule;

public class MainHook extends XposedModule {

    public static XposedModule module;


    /**
     * Instantiates a new Xposed module.<br/>
     * When the module is loaded into the target process, the constructor will be called.
     *
     * @param base  The implementation interface provided by the framework, should not be used by the module
     * @param param Information about the process in which the module is loaded
     */
    public MainHook(@NonNull XposedInterface base, @NonNull ModuleLoadedParam param) {
        super(base, param);
        module = this;
    }

    @Override
    public void onPackageLoaded(@NonNull PackageLoadedParam param) {
        super.onPackageLoaded(param);
        if (!param.isFirstPackage()) {
            return;
        }

        SharedPreferences settings = getRemotePreferences("settings");
        if (settings.getBoolean("enabled", false)) {
            String currentProfileJson = settings.getString("current_profile", null);
            try {
               ProfileDto profileDto = JsonMapper.builder().build().readValue(currentProfileJson, ProfileDto.class);
               ProfileDtoHolder.setProfileDto(profileDto);
            } catch (Exception ignored) {

            }
            ClassLoader classLoader = param.getClassLoader();
            Hookers hookers = new Hookers(classLoader);
            hookers.getHookMap().forEach(this::hook);
        }
    }



}
