package org.codebase.locationcheater.ui;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.codebase.locationcheater.R;
import org.codebase.locationcheater.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        for (int i = 0; i < 10; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText("This is " + i);
            radioButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            radioButton.setPadding(0, 10, 0, 10);
            radioButton.setTextSize(16F);
            radioButton.setChecked(false);
            radioButton.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
                MenuItem deleteItem = menu.add(0, 0, 0, "删除");
                deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem item) {
                        binding.locationRadioGroup.removeView(radioButton);
                        return true;
                    }
                });
            });
            binding.locationRadioGroup.addView(radioButton);
        }

    }
}