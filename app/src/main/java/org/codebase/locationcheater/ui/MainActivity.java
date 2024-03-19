package org.codebase.locationcheater.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import org.codebase.locationcheater.R;
import org.codebase.locationcheater.databinding.MainActivityBinding;
import org.codebase.locationcheater.ui.dao.ProfileDatabase;
import org.codebase.locationcheater.ui.dao.ProfileTypeConverters;
import org.codebase.locationcheater.ui.view.ShowListFragment;

import io.github.libxposed.service.XposedService;
import io.github.libxposed.service.XposedServiceHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        MainActivityBinding binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragment_container_view), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        XposedServiceHelper.registerListener(new XposedServiceHelper.OnServiceListener() {
            @Override
            public void onServiceBind(@NonNull XposedService service) {
                SharedPreferences settings = service.getRemotePreferences("settings");
                getSupportFragmentManager()
                        .setFragmentResultListener("switch", MainActivity.this, (requestKey, result) -> {
                            boolean enabled = result.getBoolean("enabled");
                            boolean success = settings.edit().putBoolean("enabled", enabled)
                                    .commit();
                            if (!success) {
                                Toast.makeText(MainActivity.this, "Save failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
                getSupportFragmentManager()
                        .setFragmentResultListener("save_current_profile", MainActivity.this, (requestKey, result) -> {
                            String data = result.getString("data");
                            boolean success = settings.edit().putString("current_profile", data)
                                    .commit();
                            if (!success) {
                                Toast.makeText(MainActivity.this, "Save failed!", Toast.LENGTH_SHORT).show();
                            }
                        });

                ProfileDatabase profileDatabase = Room.databaseBuilder(MainActivity.this, ProfileDatabase.class, "profile")
                        .addTypeConverter(new ProfileTypeConverters())
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();

                FragmentManager supportFragmentManager = getSupportFragmentManager();
                supportFragmentManager
                        .beginTransaction()
                        // .add(R.id.fragment_container_view, ShowListFragment.class, null)
                        .replace(R.id.fragment_container_view, new ShowListFragment(profileDatabase, settings))
                        .setReorderingAllowed(true)
                        .commit();
            }

            @Override
            public void onServiceDied(@NonNull XposedService service) {

            }
        });
    }
}