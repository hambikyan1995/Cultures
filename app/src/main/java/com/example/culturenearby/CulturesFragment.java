package com.example.culturenearby;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.culturenearby.databinding.FragmentCulturesBinding;

import java.util.ArrayList;

public class CulturesFragment extends Fragment {

    private FragmentCulturesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCulturesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getCulturesDb();
    }

    private void getCulturesDb() {
        ArrayList<CultureData> list = new ArrayList<>();
        DBHelper dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("culturstable", null, null, null, null, null, null);
        if (c.moveToFirst()) {

            int nameIndex = c.getColumnIndex("name");
            int imageUrlIndex = c.getColumnIndex("imageUrl");
            int infoIndex = c.getColumnIndex("info");
            int addressIndex = c.getColumnIndex("address");
            int mapLinkIndex = c.getColumnIndex("mapLink");

            do {
                String imageUrl = c.getString(imageUrlIndex);
                String name = c.getString(nameIndex);
                String info = c.getString(infoIndex);
                String address = c.getString(addressIndex);
                String mapLink = c.getString(mapLinkIndex);
                list.add(new CultureData(imageUrl, name, info, address, mapLink));

            } while (c.moveToNext());
        } else
            c.close();

        setCulturesAdapter(list);
    }

    private void setCulturesAdapter(ArrayList<CultureData> data) {
        binding.rvCultures.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        binding.rvCultures.setAdapter(new CulturesAdapter(data, cultureData -> {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cultureData.mapLink));
                startActivity(browserIntent);
            } catch (ActivityNotFoundException exception) {
                Toast.makeText(requireActivity(), "Հասցեի խնդիր", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}