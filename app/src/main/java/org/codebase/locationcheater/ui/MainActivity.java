package org.codebase.locationcheater.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import org.codebase.locationcheater.R;
import org.codebase.locationcheater.databinding.MainActivityBinding;
import org.codebase.locationcheater.ui.view.ShowListFragment;

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

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.setFragmentResultListener("addLocation", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String data = result.getString("data");
                System.out.println(data);
            }
        });
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container_view, ShowListFragment.class, null)
                .setReorderingAllowed(true)
                .commit();
    }
}