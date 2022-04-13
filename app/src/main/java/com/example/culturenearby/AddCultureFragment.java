package com.example.culturenearby;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.culturenearby.databinding.FragmentAddCultureBinding;

import java.io.IOException;

public class AddCultureFragment extends Fragment {

    private FragmentAddCultureBinding binding;
    private String imageUrl;
    private SQLiteDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentAddCultureBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DBHelper dbHelper = new DBHelper(requireActivity());
        db = dbHelper.getWritableDatabase();

        binding.buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.editName.getText().toString().trim();
                String info = binding.editInfo.getText().toString().trim();
                String mapLink = binding.editMapLink.getText().toString().trim();
                String address = binding.editAddress.getText().toString().trim();

                if (name.isEmpty() || info.isEmpty() || mapLink.isEmpty() || address.isEmpty() || imageUrl == null) {
                    Toast.makeText(requireActivity(), "Լրացրեք բոլոր դաշտերը", Toast.LENGTH_SHORT).show();
                    return;
                }

                insertItem(name, imageUrl, info, address, mapLink);
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main).popBackStack();
            }
        });
    }

    private void insertItem(String name, String imageUrl, String info, String address, String mapLink) {
        if (!isExist(name)) {
            ContentValues cv = new ContentValues();
            cv.put("imageUrl", imageUrl);
            cv.put("name", name);
            cv.put("info", info);
            cv.put("address", address);
            cv.put("mapLink", mapLink);
            cv.put("isDefault", false);

            db.insert("culturstable", null, cv);
        }
    }

    public boolean isExist(String name) {
        Cursor c = null;
        try {
            String query = "select count(*) from culturstable where name = ?";
            c = db.rawQuery(query, new String[]{name});
            if (c.moveToFirst()) {
                return c.getInt(0) != 0;
            }
            return false;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 121);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 121) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        imageUrl = data.getData().toString();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                        binding.appCompatImageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}