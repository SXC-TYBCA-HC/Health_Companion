package com.project.healthcompanion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.healthcompanion.ReminderPackage.Reminder_main;

import java.util.List;

public class DietPlans extends AppCompatActivity {

    DrawerLayout drawerLayout;

    private TextView empty;
    private RecyclerView recyclerView;
    private ImageView add;
    private List<DietPlans> temp;       //collects list of diet plans from DB
    //private AdapterDietPlans adapterDietPlans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plans);

        drawerLayout = findViewById(R.id.drawer_Layout);

        add = findViewById(R.id.add_dietplan);
        empty = findViewById(R.id.deitplan_empty_text);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDietPlans();
            }
        });

        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DietPlans.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //setItemsInRecyclerView();
    }

    public void ClickMenu(View view) { HomePage.openDrawer(drawerLayout); }

    public void ClickLogo(View view) { HomePage.closeDrawer(drawerLayout); }

    public void ClickProfile(View view) { /*HomePage.redirectActivity(this, Profile.class);*/ }

    public void ClickHome(View view) { HomePage.redirectActivity(this, HomePage.class); }

    public void ClickDashboard(View view) { HomePage.redirectActivity(this, Dashboard.class); }

    public void ClickGraphs(View view) { HomePage.redirectActivity(this, Records.class); }

    public void ClickDietPlans(View view) { HomePage.closeDrawer(drawerLayout); }

    public void ClickReminders(View view) { HomePage.redirectActivity(this, Reminder_main.class);}

    public void ClickLogout(View view) { HomePage.logout(this); }

    @Override
    protected void onPause(){
        super.onPause();
        HomePage.closeDrawer(drawerLayout);
    }

    //adds diet plans
    public void addDietPlans() {
        Intent intent = new Intent(this, DietPlanner.class);
        startActivity(intent);
    }

    //checks of there is a diet plan or not. if there is a diet plan, it makes the dietplan_empty_text invisible
    /*
    public void setItemsInRecyclerView() {
        if(temp.size > 0) {
            empty.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        adapterDietPlans = new AdapterDietPlans(temp);
        recyclerView.setAdapter(adapterDietPlans);
    }*/
}