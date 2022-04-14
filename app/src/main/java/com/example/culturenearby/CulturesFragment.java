package com.example.culturenearby;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.culturenearby.databinding.FragmentCulturesBinding;

import java.util.ArrayList;

public class CulturesFragment extends Fragment implements MenuItem.OnMenuItemClickListener {

    private FragmentCulturesBinding binding;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCulturesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        getCulturesDb();
    }

    private void getCulturesDb() {
        ArrayList<CultureData> list = new ArrayList<>();
        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        String QUERY_SELECT = "SELECT * FROM " + "culturstable" + " ORDER BY " + "id" + " DESC";
        Cursor c = db.rawQuery(QUERY_SELECT, null);
        if (c.moveToFirst()) {

            int nameIndex = c.getColumnIndex("name");
            int imageUrlIndex = c.getColumnIndex("imageUrl");
            int infoIndex = c.getColumnIndex("info");
            int addressIndex = c.getColumnIndex("address");
            int mapLinkIndex = c.getColumnIndex("mapLink");
            int wikipediaLinkIndex = c.getColumnIndex("wikipediaLink");

            do {
                String imageUrl = c.getString(imageUrlIndex);
                String name = c.getString(nameIndex);
                String info = c.getString(infoIndex);
                String address = c.getString(addressIndex);
                String mapLink = c.getString(mapLinkIndex);
                String wikipediaLink = c.getString(wikipediaLinkIndex);
                list.add(new CultureData(imageUrl, name, info, address, mapLink, wikipediaLink));

            } while (c.moveToNext());
        } else
            c.close();

        setCulturesAdapter(list);
    }

    private void setCulturesAdapter(ArrayList<CultureData> data) {
        binding.rvCultures.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        binding.rvCultures.setAdapter(new CulturesAdapter(data, new CulturesAdapter.ItemClickListener() {
            @Override
            public void imageClick(CultureData data) {
                openByIntent(data.wikipediaLink);
            }

            @Override
            public void addressClick(CultureData data) {
                openByIntent(data.mapLink);
            }
        }));
    }

    private void openByIntent(String link) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(browserIntent);
        } catch (Exception exception) {
            Toast.makeText(requireActivity(), "Հասցեի խնդիր", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteAddedCultures() {
        db.execSQL("DELETE FROM culturstable WHERE isDefault = '0'");
        getCulturesDb();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.delete_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.menu_delete) {
            deleteAddedCultures();
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity activity = ((MainActivity) requireActivity());
        if (activity.isAdmin) {
            activity.getMenu().findItem(R.id.menu_delete).setVisible(true);
            activity.getMenu().findItem(R.id.menu_delete).setOnMenuItemClickListener(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) requireActivity()).getMenu().findItem(R.id.menu_delete).setVisible(false);
        binding = null;
    }
}