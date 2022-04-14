package com.example.culturenearby;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.culturenearby.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private SQLiteDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DBHelper dbHelper = new DBHelper(requireActivity());
        db = dbHelper.getWritableDatabase();

        getCurrentUser();

        binding.textCultures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.nav_cultures);
            }
        });

        binding.textChurches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireActivity(), "Շուտով", Toast.LENGTH_SHORT).show();
            }
        });

        binding.textMuseum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireActivity(), "Շուտով", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                        .navigate(R.id.action_nav_home_to_nav_add_culture);
            }
        });
    }

    public void getCurrentUser() {
        Cursor c = null;
        try {
            String query = "select * from currentusertable";
            c = db.rawQuery(query, new String[]{});
            if (c.moveToFirst()) {
                int ind = c.getColumnIndex("email");
                String email = c.getString(ind);
                if (email == null || email.isEmpty()) {
                    email = getString(R.string.admin);
                    ((MainActivity) requireActivity()).isAdmin = true;
                } else binding.btnAdd.setVisibility(View.GONE);
                binding.email.setText(email);
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).enableDrawer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}