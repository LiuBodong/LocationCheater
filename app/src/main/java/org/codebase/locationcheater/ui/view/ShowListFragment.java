package org.codebase.locationcheater.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import org.codebase.locationcheater.R;
import org.codebase.locationcheater.databinding.ShowListFragmentBinding;

public class ShowListFragment extends Fragment {

    private ShowListFragmentBinding binding;

    public ShowListFragment() {
        super(R.layout.show_list_fragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = ShowListFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            binding.addLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getParentFragmentManager()
                            .beginTransaction()
                            .addToBackStack("showListFragment")
                            .replace(R.id.fragment_container_view, LocationDetailFragment.class, null)
                            .setReorderingAllowed(true)
                            .commit();
                }
            });
        }
    }

}
