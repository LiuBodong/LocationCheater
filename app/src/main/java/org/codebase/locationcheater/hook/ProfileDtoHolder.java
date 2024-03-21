package org.codebase.locationcheater.hook;

import org.codebase.locationcheater.ui.dao.ProfileDto;

import java.util.function.Consumer;

public class ProfileDtoHolder {

    private static ProfileDto profileDto;

    public static synchronized void setProfileDto(ProfileDto profileDto) {
        ProfileDtoHolder.profileDto = profileDto;
    }

    public static void doIfNonNull(Consumer<ProfileDto> consumer) {
        if (profileDto != null) {
            consumer.accept(profileDto);
        }
    }

    public static synchronized ProfileDto getProfileDto() {
        return ProfileDtoHolder.profileDto;
    }

}
