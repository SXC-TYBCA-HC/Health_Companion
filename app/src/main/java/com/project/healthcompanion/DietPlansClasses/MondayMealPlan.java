package com.project.healthcompanion.DietPlansClasses;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.project.healthcompanion.HomePage;
import com.project.healthcompanion.R;
import com.project.healthcompanion.ReminderClasses.Reminder_main;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class MondayMealPlan extends AppCompatActivity {

    DrawerLayout drawerLayout;

    TextView protein, carbs, fats;

    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monday_meal_plan);

        drawerLayout = findViewById(R.id.drawer_Layout);

        protein = findViewById(R.id.Protein);
        carbs = findViewById(R.id.Carbs);
        fats = findViewById(R.id.Fats);
        pieChart = findViewById(R.id.piechart);

        setChart();
    }

    public void ClickMenu(View view) { HomePage.openDrawer(drawerLayout); }

    public void ClickLogo(View view) { HomePage.closeDrawer(drawerLayout); }

    public void ClickProfile(View view) { /*HomePage.redirectActivity(this, Profile.class);*/ }

    public void ClickHome(View view) { HomePage.redirectActivity(this, HomePage.class); }

    public void ClickDashboard(View view) { /*HomePage.redirectActivity(this, Dashboard.class);*/ }

    public void ClickGraphs(View view) { /*HomePage.redirectActivity(this, WeightProgress.class);*/ }

    public void ClickDietPlans(View view) { HomePage.closeDrawer(drawerLayout); }

    public void ClickReminders(View view) { HomePage.redirectActivity(this, Reminder_main.class);}

    public void ClickSocial(View view) { /*HomePage.redirectActivity(this, Social.class);*/ }

    public void ClickLogout(View view) { HomePage.logout(this); }

    @Override
    protected void onPause(){
        super.onPause();
        HomePage.closeDrawer(drawerLayout);
    }

    //Piechart
    private void setChart() {
        //setting predefined values
        protein.setText(Integer.toString(120));
        carbs.setText(Integer.toString(40));
        fats.setText(Integer.toString(80));

        //creating pie divisions and assigning colours to them
        pieChart.addPieSlice(new PieModel("Protein", Integer.parseInt(protein.getText().toString()), Color.parseColor("#FFA726")));
        pieChart.addPieSlice(new PieModel("Carbohydrates", Integer.parseInt(carbs.getText().toString()), Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(new PieModel("Fats", Integer.parseInt(fats.getText().toString()), Color.parseColor("#EF5350")));
    }
}