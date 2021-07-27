package com.project.healthcompanion;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.project.healthcompanion.DietPlansClasses.DietPlans;
import com.project.healthcompanion.Model.DietPlan;
import com.project.healthcompanion.Model.Food;
import com.project.healthcompanion.ReminderClasses.Reminder_main;
import com.project.healthcompanion.databinding.ActivityDashboardBinding;
import com.project.healthcompanion.databinding.DialogDashboardBinding;

import java.sql.Date;

public class DashboardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DrawerLayout drawerLayout;

    DietPlan dietPlan;
    ActivityResultLauncher<Intent> searchResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    if (intent == null) {
                        Toast.makeText(DashboardActivity.this, "Error getting search result", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Food selectedFood = (Food) intent.getSerializableExtra("selectedFood");
                    int qty = intent.getIntExtra("quantity", 1);

                    Log.d("dash", selectedFood.getServingUnit());
                }
            });

    private ActivityDashboardBinding binding;
    private Date today;
    private double totalCaloriesConsumed, totalCaloriesPlanned;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.textViewMacroBreakdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                //loadDialog();
            }
        });
        dietPlan = getDietPlan();
        displayDietPlan(dietPlan);
        setContentView(view);

        drawerLayout = findViewById(R.id.drawer_Layout);

        Intent intent = new Intent(this, SearchFoodActivity.class);
        searchResultLauncher.launch(intent);



    }

    private void displayDietPlan(DietPlan dietPlan) {
    }

    private DietPlan getDietPlan() {
        DietPlan dietPlan = new DietPlan();
        //connect to db and retrieve Diet Plan
        //return diet plan
        return dietPlan;
    }

    private void loadDialog() {
        DialogDashboardBinding binding = DialogDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


    }

    //navigation drawer
    public void ClickMenu(View view) {
        HomePage.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view) {
        HomePage.closeDrawer(drawerLayout);
    }

    public void ClickProfile(View view) {
        HomePage.redirectActivity(this, Profile.class);
    }

    public void ClickHome(View view) {
        HomePage.redirectActivity(this, HomePage.class);
    }

    public void ClickDashboard(View view) {
        HomePage.closeDrawer(drawerLayout);
    }

    public void ClickRecords(View view) {
        HomePage.redirectActivity(this, Records.class);
    }

    public void ClickDietPlans(View view) {
        HomePage.redirectActivity(this, DietPlans.class);
    }

    public void ClickReminders(View view) {
        HomePage.redirectActivity(this, Reminder_main.class);
    }

    public void ClickHelp(View view) {
        HomePage.redirectActivity(this, HelpActivity.class);
    }

    public void ClickLogout(View view) {
        HomePage.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //HomePage.closeDrawer(drawerLayout);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("DashPos", String.valueOf(position));
        Object item = parent.getItemAtPosition(position);
        //Log.d()
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    //end of navigation drawer
}