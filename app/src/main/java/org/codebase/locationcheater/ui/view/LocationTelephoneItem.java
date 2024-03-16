package org.codebase.locationcheater.ui.view;

import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import org.codebase.locationcheater.R;
import org.codebase.locationcheater.databinding.TelephoneInformationItemBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class LocationTelephoneItem extends LinearLayout {

    private TelephoneInformationItemBinding binding;

    public LocationTelephoneItem(Context context) {
        super(context);
        this.init(context);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        binding = TelephoneInformationItemBinding.inflate(layoutInflater, this, true);
        List<String> types = Arrays.asList("CDMA", "GSM", "LTE", "NR", "TDSCDMA", "WCDMA");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.telephone_information_type_item, types);
        binding.telephoneType.setAdapter(arrayAdapter);
    }

    public void setType(String type) {
        binding.telephoneType.setText(type);
    }

    public void setLac(String lac) {
        binding.telephoneInformationLac.setText(lac);
    }

    public void setCid(String cid) {
        binding.telephoneInformationCid.setText(cid);
    }

    public void onItemDelete(Consumer<View> action) {
        binding.telephoneInformationItemDeleteButton.setOnClickListener(action::accept);
    }

}
