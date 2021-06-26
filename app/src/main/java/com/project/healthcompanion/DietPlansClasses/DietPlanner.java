package com.project.healthcompanion.DietPlansClasses;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fatsecret.platform.model.CompactFood;
import com.fatsecret.platform.model.CompactRecipe;
import com.fatsecret.platform.model.Food;
import com.fatsecret.platform.model.Recipe;
import com.fatsecret.platform.services.Response;
import com.fatsecret.platform.services.android.ResponseListener;
import com.project.healthcompanion.HomePage;
import com.project.healthcompanion.R;
import com.project.healthcompanion.Records;
import com.project.healthcompanion.ReminderClasses.Reminder_main;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import android.os.Bundle;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.fatsecret.platform.model.CompactFood;
import com.fatsecret.platform.model.CompactRecipe;
import com.fatsecret.platform.model.Food;
import com.fatsecret.platform.model.Recipe;
import com.fatsecret.platform.services.Response;
import com.fatsecret.platform.services.android.Request;
import com.fatsecret.platform.services.android.ResponseListener;

import java.util.List;

public class DietPlanner extends AppCompatActivity {

    DrawerLayout drawerLayout;

    TextView protein, carbs, fats;

    PieChart pieChart;

    Button add_breakfast, add_lunch, add_dinner, add_snacks, dp_confirm;

    Dialog dialog;

    private TextView empty;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_planner);

        drawerLayout = findViewById(R.id.drawer_Layout);

        protein = findViewById(R.id.Protein);
        carbs = findViewById(R.id.Carbs);
        fats = findViewById(R.id.Fats);
        pieChart = findViewById(R.id.piechart);

        add_breakfast = findViewById(R.id.add_breakfast);
        dp_confirm = findViewById(R.id.dp_confirm);

        empty = findViewById(R.id.deitplan_empty_text);
        recyclerView = findViewById(R.id.dietplan_RecyclerView);

        add_breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBreakfast();
            }
        });

        dp_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDietPlan();
            }
        });

        setChart();
    }

    //navigation drawer
    public void ClickMenu(View view) { HomePage.openDrawer(drawerLayout); }

    public void ClickLogo(View view) { HomePage.closeDrawer(drawerLayout); }

    public void ClickProfile(View view) { /*HomePage.redirectActivity(this, Profile.class);*/ }

    public void ClickDashboard(View view) { /*HomePage.redirectActivity(this, Dashboard.class);*/ }

    public void ClickRecords(View view) { HomePage.redirectActivity(this, Records.class); }

    public void ClickDietPlans(View view) { HomePage.redirectActivity(this, DietPlans.class); }

    public void ClickReminders(View view) { HomePage.redirectActivity(this, Reminder_main.class); }

    public void ClickLogout(View view) { HomePage.logout(this); }

    @Override
    protected void onPause() {
        super.onPause();
        HomePage.closeDrawer(drawerLayout);
    }
    //end of navigation drawer

    //Piechart
    private void setChart() {
        //setting predefined values
        protein.setText(Integer.toString(0));
        carbs.setText(Integer.toString(0));
        fats.setText(Integer.toString(0));

        //creating pie divisions and assigning colours to them
        pieChart.addPieSlice(new PieModel("Protein", Integer.parseInt(protein.getText().toString()), Color.parseColor("#FFA726")));
        pieChart.addPieSlice(new PieModel("Carbohydrates", Integer.parseInt(carbs.getText().toString()), Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(new PieModel("Fats", Integer.parseInt(fats.getText().toString()), Color.parseColor("#EF5350")));
    }

    public void addBreakfast() {
        dialog = new Dialog(DietPlanner.this);
        dialog.setContentView(R.layout.diet_planner_popup);

        EditText dp_search;     //contains search text
        ImageView dp_search_btn;
        TextView textView;

        dp_search = findViewById(R.id.dp_search);
        dp_search_btn = findViewById(R.id.dp_search_btn);
        textView = findViewById(R.id.food_list);

        String key = "47ad1fed29954dedb106428b7a8b51bc";
        String secret = "8e2ddb0136f9481581f60f02cdca934b";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Listener listener = new Listener();

        Request req = new Request(key, secret, listener);

        /*dp_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });

        /*dp_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //req.getFoods(requestQueue, dp_search);
            }
        });*/

        dialog.show();
    }

    private void jsonParse(){
        String url = "";
    }

    class Listener implements ResponseListener {
        @Override
        public void onFoodListRespone(Response<CompactFood> response) {
            System.out.println("TOTAL FOOD ITEMS: " + response.getTotalResults());

            List<CompactFood> foods = response.getResults();
            //This list contains summary information about the food items

            System.out.println("=========FOODS============");
            for (CompactFood food: foods) {
                System.out.println(food.getName());
            }
        }

        @Override
        public void onFoodResponse(Food food) {
            System.out.println("FOOD NAME: " + food.getName());
        }
    }

    public void ConfirmDietPlan() {
        //empty.setVisibility(View.INVISIBLE);
        //recyclerView.setVisibility(View.VISIBLE);
    }
}
