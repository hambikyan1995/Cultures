package com.example.culturenearby;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.culturenearby.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AppBarConfiguration mAppBarConfiguration;
    private SQLiteDatabase db;
    private ActivityMainBinding binding;
    private Menu mOptionsMenu;
    boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        insertItem("Սասունցի Դավիթ",
                "https://toptrip.info/wp-content/uploads/2019/09/shutterstock_262713458-scaled.jpg",
                "Քանդակագործ՝ Ե. Քոչար\n" +
                        "ճարտարապետ՝ Մ. Մազմանյան\n" +
                        "Կոփածո պղինձ, բազալտ, 1959 թ.\n",
                "Սասունցի Դավթի հրպ.",
                "https://www.google.com/maps/place/%D0%A1%D1%82%D0%B0%D1%82%D1%83%D1%8F+%D0%94%D0%B0%D0%B2%D0%B8%D0%B4%D0%B0+%D0%A1%D0%B0%D1%81%D1%83%D0%BD%D1%81%D0%BA%D0%BE%D0%B3%D0%BE/@40.1547367,44.5066559,16.25z/data=!4m5!3m4!1s0x406abc6d1753b5a9:0xeeff9e46f8ccfe0d!8m2!3d40.1552777!4d44.5094883",
                "https://hy.wikipedia.org/wiki/%D5%8D%D5%A1%D5%BD%D5%B8%D6%82%D5%B6%D6%81%D5%AB_%D4%B4%D5%A1%D5%BE%D5%AB%D5%A9");
        insertItem("Ավետիք Իսահակյան",
                "https://upload.wikimedia.org/wikipedia/commons/d/d9/Avetik_Isahakyan_Yerevan.jpg",
                "Քանդակագործ՝ Ս. Բաղդասարյան, ճարտարապետ՝ Լ. Սադոյան\n" +
                        "Բրոնզ, գրանիտ, 1965թ.\n",
                "«Օղակաձև» զբոսայգի",
                "https://www.google.com/maps/place/Avetik+Isahakyan+Statue/@40.1893075,44.5053642,15.25z/data=!4m9!1m2!2m1!1zc3RhdHVlbiDRgNGP0LTQvtC8INGBIEF2ZXRpayBJc2FoYWt5YW4gTW9udW1lbnQsINGD0LvQuNGG0LAg0JDQsdC-0LLRj9C90LAsINCV0YDQtdCy0LDQvQ!3m5!1s0x406abce0e78aa06d:0xc7605518ccbc3a68!8m2!3d40.1867012!4d44.5217648!15sClhzdGF0dWVuINGA0Y_QtNC-0Lwg0YEgQXZldGlrIElzYWhha3lhbiBNb251bWVudCwg0YPQu9C40YbQsCDQkNCx0L7QstGP0L3QsCwg0JXRgNC10LLQsNC9kgEJc2N1bHB0dXJl",
                "https://hy.wikipedia.org/wiki/%D4%B1%D5%BE%D5%A5%D5%BF%D5%AB%D6%84_%D4%BB%D5%BD%D5%A1%D5%B0%D5%A1%D5%AF%D5%B5%D5%A1%D5%B6%D5%AB_%D5%B0%D5%B8%D6%82%D5%B7%D5%A1%D6%80%D5%B1%D5%A1%D5%B6_(%D4%B5%D6%80%D6%87%D5%A1%D5%B6)");
    }

    private void insertItem(String name, String imageUrl, String info, String address, String mapLink, String wikipediaLink) {
        if (!isExist(name)) {
            ContentValues cv = new ContentValues();
            cv.put("imageUrl", imageUrl);
            cv.put("name", name);
            cv.put("info", info);
            cv.put("address", address);
            cv.put("mapLink", mapLink);
            cv.put("wikipediaLink", wikipediaLink);
            cv.put("isDefault", true);

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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_menu, menu);
        mOptionsMenu = menu;
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_nearby:
                openMap();
                break;
            case R.id.nav_log_out:
                db.execSQL("delete from " + "currentusertable");
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;

            case R.id.nav_other:
                Toast.makeText(this, "Շուտով", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_about_us:
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.nav_about_us);
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                break;
        }
        binding.drawerLayout.closeDrawers();
        return false;
    }

    public void enableDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
    }

    public Menu getMenu() {
        return mOptionsMenu;
    }

    private void openMap() {
        Uri gmmIntentUri = Uri.parse("http://maps.google.com");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        startActivity(mapIntent);
    }
}