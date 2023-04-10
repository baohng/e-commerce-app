package com.example.ebn.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ebn.R;
import com.example.ebn.activities.EditProfile;
import com.example.ebn.activities.LoginActivity;
import com.example.ebn.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private Button button;
    private TextView textView, textViewEditPersonalProfile;
    private String userID;


    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_account, container, false);

        auth = FirebaseAuth.getInstance();
        button = view.findViewById(R.id.buttonLogout);
        textView = view.findViewById(R.id.currentUser);
        textViewEditPersonalProfile = view.findViewById(R.id.textviewEditPersonalProfile);
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        final TextView textViewUserProfileFullName = view.findViewById(R.id.textViewUserProfileFullName);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String fullName = userProfile.fullName;
                    String email = userProfile.email;
                    String phoneNumb = userProfile.phoneNumber;

                    textViewUserProfileFullName.setText(fullName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

        if (user == null) {
            Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        else {
            textView.setText(user.getEmail());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity().getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        textViewEditPersonalProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EditProfile.class);
                startActivity(intent);
            }
        });

        return view;
    }
}