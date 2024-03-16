package org.codebase.locationcheater.ui.view;

import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import org.codebase.locationcheater.databinding.WifiInformationItemBinding;

import java.util.function.Consumer;

public class LocationWifiItem extends LinearLayout {

    private WifiInformationItemBinding binding;

    public LocationWifiItem(Context context) {
        super(context);
        this.init(context);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        binding = WifiInformationItemBinding.inflate(layoutInflater, this, true);
    }

    public CharSequence getSsid() {
        Editable editable = binding.wifiItemSsidTextInput.getText();
        return editable == null ? null : editable.toString();
    }

    public void setSsid(String ssid) {
        binding.wifiItemSsidTextInput.setText(ssid);
    }

    public CharSequence getMac() {
        Editable editable = binding.wifiItemMacTextInput.getText();
        return editable == null ? null : editable.toString();
    }

    public void setMac(String mac) {
        binding.wifiItemMacTextInput.setText(mac);
    }

    public void onItemDelete(Consumer<View> action) {
        binding.wifiItemDeleteButton.setOnClickListener(action::accept);
    }

}
