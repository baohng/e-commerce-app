package com.example.ebn.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.ebn.R;
import com.example.ebn.databinding.ActivityMainBinding;
import com.example.ebn.fragments.AccountFragment;
import com.example.ebn.fragments.HomepageFragment;
import com.example.ebn.fragments.QrScanFragment;
import com.example.ebn.fragments.RoomFragment;
import com.example.ebn.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

//    FirebaseAuth auth;
//    FirebaseUser user;
//    Button button;
//    TextView textView;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomepageFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.botNavHome:
                    replaceFragment(new HomepageFragment());
                    break;
                case R.id.botNavSearch:
                    replaceFragment(new SearchFragment());
                    break;
                case R.id.botNavRoom:
                    replaceFragment(new RoomFragment());
                    break;
                case  R.id.botNavAccount:
                    replaceFragment(new AccountFragment());
                    break;
                case R.id.botNavQRScan:
                    replaceFragment(new QrScanFragment());
                    break;
            }


            return true;
        });

//        auth = FirebaseAuth.getInstance();
//        button = findViewById(R.id.buttonLogout);
//        textView = findViewById(R.id.currentUser);
//        user = auth.getCurrentUser();
//
//        if (user == null) {
//            Intent intent = new Intent(getApplicationContext(),Login.class);
//            startActivity(intent);
//            finish();
//        }
//        else {
//            textView.setText(user.getEmail());
//        }
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getApplicationContext(),Login.class);
//                startActivity(intent);
//                finish();
//            }
//        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}