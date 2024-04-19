package org.codebase.locationcheater.ui.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import org.apache.commons.lang3.StringUtils;
import org.codebase.locationcheater.R;
import org.codebase.locationcheater.databinding.ShowListFragmentBinding;
import org.codebase.locationcheater.ui.dao.ProfileDatabase;
import org.codebase.locationcheater.ui.dao.ProfileDto;

public class ShowListFragment extends Fragment {

    private ProfileDatabase profileDatabase;

    private SharedPreferences settings;

    private ShowListFragmentBinding binding;

    public ShowListFragment() {
        super(R.layout.show_list_fragment);
    }

    public ShowListFragment(ProfileDatabase profileDatabase, SharedPreferences settings) {
        this();
        this.profileDatabase = profileDatabase;
        this.settings = settings;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager()
                .setFragmentResultListener("data", this, (requestKey, result) -> {
                    if (requestKey.equals("data")) {
                        String data = result.getString("data");
                        boolean isChecked = result.getBoolean("isChecked", false);
                        if (StringUtils.isNotEmpty(data)) {
                            ProfileDto profileDto = null;
                            try {
                                profileDto = JsonMapper.builder().build().readValue(data, ProfileDto.class);
                                profileDatabase.profileDao().insertAll(profileDto);
                                if (isChecked) {
                                    settings.edit().putString("current_profile", data).apply();
                                }
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ShowListFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.initList();
        if (savedInstanceState == null) {
            boolean enabled = settings.getBoolean("enabled", false);
            binding.switchLocationEnable.setChecked(enabled);
            binding.addLocationButton.setOnClickListener(v ->
                    getParentFragmentManager()
                            .beginTransaction()
                            .addToBackStack("showListFragment")
                            // .replace(R.id.fragment_container_view, LocationDetailFragment.class, null)
                            .replace(R.id.fragment_container_view, new LocationDetailFragment())
                            .setReorderingAllowed(true)
                            .commit());
        }
        binding.switchLocationEnable.setOnCheckedChangeListener((buttonView, isChecked) ->
                settings.edit().putBoolean("enabled", isChecked).commit());
    }

    @Override
    public void onResume() {
        super.onResume();
        this.initList();
    }

    private void initList() {
        String currentProfileJson = settings.getString("current_profile", null);
        ProfileDto currentProfile = null;
        if (currentProfileJson != null) {
            try {
                currentProfile = JsonMapper.builder().build().readValue(currentProfileJson, ProfileDto.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        final ProfileDto currentProfileFinal = currentProfile;
        binding.locationRadioGroup.removeAllViews();
        profileDatabase.profileDao().getAllProfiles()
                .forEach(profileDto -> {
                    ProfileItemView profileItemView = new ProfileItemView(getContext());
                    profileItemView.setProfileName(profileDto.getProfileName());
                    profileItemView.setCreateTime(profileDto.getCreateTime());
                    profileItemView.setChecked(currentProfileFinal != null && currentProfileFinal.getId() == profileDto.getId());
                    profileItemView.setOnChecked((buttonView, isChecked) -> {
                        if (isChecked) {
                            int n = binding.locationRadioGroup.getChildCount();
                            for (int i = 0; i < n; i++) {
                                View child = binding.locationRadioGroup.getChildAt(i);
                                if (child != profileItemView) {
                                    ((ProfileItemView) child).setChecked(false);
                                }
                            }
                            try {
                                String data = JsonMapper.builder().build()
                                                .writeValueAsString(profileDto);
                                settings.edit().putString("current_profile", data).apply();
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                    profileItemView.setOnLongClickListener(v -> {
                        PopupMenu popupMenu = new PopupMenu(getContext(), v);
                        popupMenu.getMenu().add(0, 0, 0, "Delete");
                        popupMenu.getMenu().add(0, 1, 0, "Edit");

                        popupMenu.setOnMenuItemClickListener(item -> {
                            switch (item.getItemId()) {
                                case 0:
                                    binding.locationRadioGroup.removeView(profileItemView);
                                    profileDatabase.profileDao().deleteAll(profileDto);
                                    break;
                                case 1:
                                    LocationDetailFragment locationDetailFragment = new LocationDetailFragment(profileDto, profileItemView.isChecked());
                                    getParentFragmentManager()
                                            .beginTransaction()
                                            .addToBackStack("showListFragment")
                                            .replace(R.id.fragment_container_view, locationDetailFragment)
                                            .setReorderingAllowed(true)
                                            .commit();
                                    break;
                                default:
                                    break;
                            }
                            return true;
                        });

                        popupMenu.show();
                        return true;
                    });
                    binding.locationRadioGroup.addView(profileItemView);
                });
    }
}
