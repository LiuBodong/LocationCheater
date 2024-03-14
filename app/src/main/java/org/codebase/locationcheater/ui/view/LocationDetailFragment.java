package org.codebase.locationcheater.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.codebase.locationcheater.R;
import org.codebase.locationcheater.databinding.LocationDetailFragmentBinding;

import java.util.ArrayList;

public class LocationDetailFragment extends Fragment {

    private LocationDetailFragmentBinding binding;

    public LocationDetailFragment() {
        super(R.layout.location_detail_fragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = LocationDetailFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            binding.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("data", "aaaa");
                    getParentFragmentManager()
                            .setFragmentResult("addLocation", bundle);
                }
            });
        }
    }
}
