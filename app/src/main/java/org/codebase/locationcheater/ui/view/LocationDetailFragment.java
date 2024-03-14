package org.codebase.locationcheater.ui.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.codebase.locationcheater.R;
import org.codebase.locationcheater.databinding.LocationDetailFragmentBinding;

public class LocationDetailFragment extends Fragment {

    private LocationDetailFragmentBinding binding;

    private ActivityResultLauncher<String> requestPermissionLauncher;

    public LocationDetailFragment() {
        super(R.layout.location_detail_fragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (!isGranted) {
                Toast.makeText(getContext(), "Permission not granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission granted!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = LocationDetailFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.checkAndAcquirePermissions();

        if (savedInstanceState == null) {
            binding.confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("data", "aaaa");
                    getParentFragmentManager().setFragmentResult("addLocation", bundle);
                }
            });
        }

        binding.refreshWifiInformationButton.setOnClickListener(v -> this.initWifiInformation());

        this.initWifiInformation();

    }

    private void checkAndAcquirePermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void initWifiInformation() {
        binding.wifiListView.removeAllViews();
        WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
        int wifiState = wifiManager.getWifiState();
        if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            binding.connectedWifiSsid.setText(connectionInfo.getSSID().replace("\"", ""));
            binding.connectedWifiMac.setText(connectionInfo.getBSSID());
            wifiManager.getScanResults().forEach(scanResult -> {
                LocationWifiItem item = new LocationWifiItem(getContext());
                item.setSsid(scanResult.SSID);
                item.setMac(scanResult.BSSID);
                item.onItemDelete(v -> binding.wifiListView.removeView(item));
                binding.wifiListView.addView(item);
            });
        }
    }
}
