package org.codebase.locationcheater.ui.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityTdscdma;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoTdscdma;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
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

import org.apache.commons.lang3.StringUtils;
import org.codebase.locationcheater.R;
import org.codebase.locationcheater.databinding.LocationDetailFragmentBinding;

import java.util.List;

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

        binding.confirmButton.setOnClickListener(v -> this.saveProfile());

        binding.refreshWifiInformationButton.setOnClickListener(v -> this.initWifiInformation());
        this.initWifiInformation();
        binding.addWifiItemButton.setOnClickListener(v -> this.addWifiInformationItem(null));

        this.initTelephoneInfo();
        binding.refreshTelephoneInformationButton.setOnClickListener(v -> this.initTelephoneInfo());

        this.initGnss();
        binding.refreshGnssInformationButton.setOnClickListener(v -> this.initGnss());
    }

    private void saveProfile() {
        String profileName = binding.locationProfileName.getText().toString();
        boolean wifiEnabled = binding.switchWifiEnabled.isEnabled();
        getParentFragmentManager().popBackStack();
    }

    private void addWifiInformationItem(ScanResult scanResult) {
        LocationWifiItem item = new LocationWifiItem(getContext());
        if (scanResult != null) {
            item.setSsid(scanResult.SSID);
            item.setMac(scanResult.BSSID);
        }
        item.onItemDelete(v -> binding.wifiListView.removeView(item));
        binding.wifiListView.addView(item);
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
            wifiManager.getScanResults()
                    .stream()
                    .filter(scanResult -> StringUtils.isNotEmpty(scanResult.SSID))
                    .forEach(this::addWifiInformationItem);
        }
    }

    private void initTelephoneInfo() {
        this.binding.telephoneInformationListView.removeAllViews();
        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Could not access location", Toast.LENGTH_SHORT).show();
            return;
        }

        for (CellInfo cellInfo : telephonyManager.getAllCellInfo()) {
            String type = "";
            int lac = -1;
            int cid = -1;
            LocationTelephoneItem locationTelephoneItem = new LocationTelephoneItem(getContext());
            if (cellInfo instanceof CellInfoCdma cellInfoCdma) {
                CellIdentityCdma cellIdentity = cellInfoCdma.getCellIdentity();
                type = "CDMA";
                cid = cellIdentity.getNetworkId();
                lac = cellIdentity.getBasestationId();
            } else if (cellInfo instanceof CellInfoTdscdma cellInfoTdscdma) {
                CellIdentityTdscdma cellIdentity = cellInfoTdscdma.getCellIdentity();
                type = "TDSCDMA";
                cid = cellIdentity.getCid();
                lac = cellIdentity.getLac();
            } else if (cellInfo instanceof CellInfoWcdma cellInfoWcdma) {
                CellIdentityWcdma cellIdentity = cellInfoWcdma.getCellIdentity();
                type = "WCDMA";
                cid = cellIdentity.getCid();
                lac = cellIdentity.getLac();
            } else if (cellInfo instanceof CellInfoGsm cellInfoGsm) {
                CellIdentityGsm cellIdentity = cellInfoGsm.getCellIdentity();
                type = "GSM";
                cid = cellIdentity.getCid();
                lac = cellIdentity.getLac();
            } else if (cellInfo instanceof CellInfoLte cellInfoLte) {
                CellIdentityLte cellIdentity = cellInfoLte.getCellIdentity();
                type = "LTE";
                cid = cellIdentity.getCi();
                lac = cellIdentity.getTac();
            } else if (cellInfo instanceof CellInfoNr cellInfoNr) {
                CellIdentityNr cellIdentity = (CellIdentityNr) cellInfoNr.getCellIdentity();
                type = "NR";
                cid = cellIdentity.getPci();
                lac = cellIdentity.getTac();
            }
            locationTelephoneItem.setType(type);
            locationTelephoneItem.setCid(String.valueOf(cid));
            locationTelephoneItem.setLac(String.valueOf(lac));
            locationTelephoneItem.onItemDelete(v -> binding.telephoneInformationListView.removeView(locationTelephoneItem));
            binding.telephoneInformationListView.addView(locationTelephoneItem);
        }

    }

    private void initGnss() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                binding.latitude.setText(String.valueOf(location.getLatitude()));
                binding.longitude.setText(String.valueOf(location.getLongitude()));
            }

            @Override
            public void onLocationChanged(@NonNull List<Location> locations) {
                locations.forEach(this::onLocationChanged);
            }

        };
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isLocationEnabled()) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        } else {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 0F, locationListener);
            }
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 0F, locationListener);
            }
        }
        Handler handler = Handler.createAsync(Looper.getMainLooper());
        handler.postDelayed(() -> locationManager.removeUpdates(locationListener), 1001L);
    }

}
