package org.codebase.locationcheater.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import org.codebase.locationcheater.databinding.ProfileItemBinding;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ProfileItemView extends LinearLayout {

    private final ProfileItemBinding binding;

    public ProfileItemView(@NonNull Context context) {
        super(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        binding = ProfileItemBinding.inflate(layoutInflater, this, true);
    }

    public void setProfileName(String profileName) {
        binding.profileName.setText(profileName);
    }

    public void setCreateTime(long createTime) {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        ZonedDateTime zonedDateTime = Instant.ofEpochMilli(createTime).atZone(ZoneId.systemDefault());
        String formatted = dateTimeFormatter.format(zonedDateTime);
        binding.createTime.setText(formatted);
    }

    public void setChecked(boolean checked) {
        binding.checkedButton.setChecked(checked);
    }

    public boolean isChecked() {
        return binding.checkedButton.isChecked();
    }

    public void setOnChecked(CompoundButton.OnCheckedChangeListener onChecked) {
        binding.checkedButton.setOnCheckedChangeListener(onChecked);
    }
}
