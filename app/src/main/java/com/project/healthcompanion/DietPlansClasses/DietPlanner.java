package com.project.healthcompanion.DietPlansClasses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.project.healthcompanion.HomePage;
import com.project.healthcompanion.R;
import com.project.healthcompanion.ReminderClasses.Reminder_main;

public class DietPlanner extends AppCompatActivity {

    DrawerLayout drawerLayout;

    private Button mon, tue, wed, thu, fri, sat, sun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_planner);

        drawerLayout = findViewById(R.id.drawer_Layout);

        mon = findViewById(R.id.add_monday);
        tue = findViewById(R.id.add_tuesday);
        wed = findViewById(R.id.add_wendesday);
        thu = findViewById(R.id.add_thursday);
        fri = findViewById(R.id.add_friday);
        sat = findViewById(R.id.add_saturday);
        sun = findViewById(R.id.add_sunday);

        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMonday();
            }
        });

        tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //navigation drawer
    public void ClickMenu(View view) { HomePage.openDrawer(drawerLayout); }

    public void ClickLogo(View view) { HomePage.closeDrawer(drawerLayout); }

    public void ClickProfile(View view) { /*HomePage.redirectActivity(this, Profile.class);*/ }

    public void ClickHome(View view) { HomePage.redirectActivity(this, HomePage.class); }

    public void ClickDashboard(View view) { /*HomePage.redirectActivity(this, Dashboard.class);*/ }

    public void ClickGraphs(View view) { /*HomePage.redirectActivity(this, WeightProgress.class);*/ }

    public void ClickDietPlans(View view) { HomePage.redirectActivity(this, DietPlans.class); }

    public void ClickReminders(View view) { HomePage.redirectActivity(this, Reminder_main.class); }

    public void ClickSocial(View view) { /*HomePage.redirectActivity(this, Social.class);*/ }

    public void ClickLogout(View view) { HomePage.logout(this); }

    @Override
    protected void onPause() {
        super.onPause();
        HomePage.closeDrawer(drawerLayout);
    }
    //end of navigation drawer

    public void AddMonday() {
        Intent intent1 = new Intent(this, MondayMealPlan.class);
        startActivity(intent1);
    }
}
