package com.project.healthcompanion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

public class Dashboard extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.drawer_Layout);
    }

    public void ClickMenu(View view) {
        HomePage.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view) {
        HomePage.closeDrawer(drawerLayout);
    }

    public void ClickProfile(View view) { /*Homepage.redirectActivity(this, Profile.class);*/ }

    public void ClickHome(View view) { HomePage.redirectActivity(this, HomePage.class); }

    public void ClickDashboard(View view) { HomePage.closeDrawer(drawerLayout); }

    public void ClickGraphs(View view) { /*HomePage.redirectActivity(this, Graphs.class);*/ }

    public void ClickDietPlans(View view) { /*HomePage.redirectActivity(this, DietPlans.class);*/ }

    public void ClickReminders(View view) { HomePage.redirectActivity(this, Reminder_main.class); }

    public void ClickSocial(View view) { /*HomePage.redirectActivity(this, Social.class);*/ }

    public void ClickLogout(View view) { HomePage.logout(this); }

    @Override
    protected void onPause() {
        super.onPause();
        HomePage.closeDrawer(drawerLayout);
    }
}