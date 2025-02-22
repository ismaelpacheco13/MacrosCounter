package com.ismael.macroscounter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismael.macroscounter.model.Food;
import com.ismael.macroscounter.model.Setting;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeActivity extends AppCompatActivity {

    private String email;

    private AlertDialog.Builder madb;
    private AlertDialog.Builder madb2;

    private TextView kcalTextView;
    private TextView proteinTextView;
    private TextView carbsTextView;
    private TextView fatsTextView;

    private int actualKcal;
    private int actualProtein;
    private int actualCarbs;
    private int actualFats;

    private int actualBreakfastKcal;
    private int actualBreakfastProtein;
    private int actualBreakfastCarbs;
    private int actualBreakfastFats;

    private int actualLunchKcal;
    private int actualLunchProtein;
    private int actualLunchCarbs;
    private int actualLunchFats;

    private int actualDinnerKcal;
    private int actualDinnerProtein;
    private int actualDinnerCarbs;
    private int actualDinnerFats;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private ListView breakfastListV;
    private ListView lunchListV;
    private ListView dinnerListV;

    private ArrayList<Food> breakfastList = new ArrayList<>();
    private ArrayList<Food> lunchList = new ArrayList<>();
    private ArrayList<Food> dinnerList = new ArrayList<>();
    ArrayAdapter<Food> arrayAdapterFood;

    private Food foodSelected;

    private Setting setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColor));
        }

        // Inicio de Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        kcalTextView = findViewById(R.id.kcalTextView);
        proteinTextView = findViewById(R.id.proteinTextView);
        carbsTextView = findViewById(R.id.carbsTextView);
        fatsTextView = findViewById(R.id.fatsTextView);

        getUserInfo();
        getUserSetting();

        breakfastListV = findViewById(R.id.breakfastListV);
        lunchListV = findViewById(R.id.lunchListV);
        dinnerListV = findViewById(R.id.dinnerListV);

        list();

        breakfastListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodSelected = (Food) parent.getItemAtPosition(position);

                createFoodInfoDialog(foodSelected);

            }
        });

        lunchListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodSelected = (Food) parent.getItemAtPosition(position);

                createFoodInfoDialog(foodSelected);

            }
        });

        dinnerListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodSelected = (Food) parent.getItemAtPosition(position);

                createFoodInfoDialog(foodSelected);

            }
        });
    }

    private void list() {
        databaseReference.child("User").child(mAuth.getUid()).child("Desayuno").child("Food").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                breakfastList.clear();
                actualBreakfastKcal = 0;
                actualBreakfastProtein = 0;
                actualBreakfastCarbs = 0;
                actualBreakfastFats = 0;
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    Food food = objSnapshot.getValue(Food.class);
                    actualBreakfastKcal += food.getKcal();
                    actualBreakfastProtein += food.getProtein();
                    actualBreakfastCarbs += food.getCarbs();
                    actualBreakfastFats += food.getFats();
                    breakfastList.add(food);
                }

                getUserSetting();
                updateEditTextSetting();

                arrayAdapterFood = new ArrayAdapter<Food>(HomeActivity.this, android.R.layout.simple_list_item_1, breakfastList);
                breakfastListV.setAdapter(arrayAdapterFood);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("User").child(mAuth.getUid()).child("Almuerzo").child("Food").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lunchList.clear();
                actualLunchKcal = 0;
                actualLunchProtein = 0;
                actualLunchCarbs = 0;
                actualLunchFats = 0;
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    Food food = objSnapshot.getValue(Food.class);
                    actualLunchKcal += food.getKcal();
                    actualLunchProtein += food.getProtein();
                    actualLunchCarbs += food.getCarbs();
                    actualLunchFats += food.getFats();
                    lunchList.add(food);
                }

                getUserSetting();
                updateEditTextSetting();

                arrayAdapterFood = new ArrayAdapter<Food>(HomeActivity.this, android.R.layout.simple_list_item_1, lunchList);
                lunchListV.setAdapter(arrayAdapterFood);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("User").child(mAuth.getUid()).child("Cena").child("Food").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dinnerList.clear();
                actualDinnerKcal = 0;
                actualDinnerProtein = 0;
                actualDinnerCarbs = 0;
                actualDinnerFats = 0;
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    Food food = objSnapshot.getValue(Food.class);
                    actualDinnerKcal += food.getKcal();
                    actualDinnerProtein += food.getProtein();
                    actualDinnerCarbs += food.getCarbs();
                    actualDinnerFats += food.getFats();
                    dinnerList.add(food);
                }

                getUserSetting();
                updateEditTextSetting();

                arrayAdapterFood = new ArrayAdapter<Food>(HomeActivity.this, android.R.layout.simple_list_item_1, dinnerList);
                dinnerListV.setAdapter(arrayAdapterFood);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setup() {
        setTitle("Macros Counter");

        // Cuadro de diálogo cierre de sesión
        madb = new AlertDialog.Builder(this);
        madb.setTitle("Cerrar sesión");
        madb.setMessage("¿Estas seguro de que deseas cerrar la sesión de su usuario " + email + "?");
        // Para que la ventana desaparezca al pulsar fuera de ella
        madb.setCancelable(true | false);
        // Se crea el boton de aceptar del diálogo
        madb.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }

    private void createFoodInfoDialog(final Food foodSelected) {
        // Cuadro de diálogo información comida seleccionada
        madb2 = new AlertDialog.Builder(HomeActivity.this);
        madb2.setTitle(foodSelected.getNombre());
        madb2.setMessage(Html.fromHtml("Gr / Ml: " + foodSelected.getGrMl() + " gr / ml<br>"
                + "Kcal: " + foodSelected.getKcal() + " kcal<br>"
                + "Proteinas: " + foodSelected.getProtein() + " gr<br>"
                + "Carbohidratos: " + foodSelected.getCarbs() + " gr<br>"
                + "Grasas: " + foodSelected.getFats() + " gr<br>"));
        // Para que la ventana desaparezca al pulsar fuera de ella
        madb2.setCancelable(true | false);
        // Se crea el boton de aceptar del diálogo
        madb2.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(HomeActivity.this, EditActivity.class);
                intent.putExtra("id", foodSelected.getId());
                intent.putExtra("foodName", foodSelected.getNombre());
                intent.putExtra("timeOfEating", foodSelected.getTimeOfEating());
                intent.putExtra("grMl", foodSelected.getGrMl());
                intent.putExtra("kcal", foodSelected.getKcal());
                intent.putExtra("protein", foodSelected.getProtein());
                intent.putExtra("carbs", foodSelected.getCarbs());
                intent.putExtra("fats", foodSelected.getFats());
                startActivity(intent);
            }
        });

        madb2.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        madb2.create();
        madb2.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflado del menú
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logOut:
                madb.create();
                madb.show();
                break;
            case R.id.menu_add:
                Intent intent = new Intent(this, AddActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
                break;
            case R.id.menu_setting:
                Intent intentSetting = new Intent(this, SettingActivity.class);
                startActivity(intentSetting);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUserInfo() {
        // Obtenemos el id del usuario con el que iniciamos sesión
        String id = mAuth.getCurrentUser().getUid();
        databaseReference.child("User").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    email = snapshot.child("email").getValue().toString();
                    setup();
                } else {
                    setup();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

    }

    private void getUserSetting() {
        // Obtenemos el id del usuario con el que iniciamos sesión
        String id = mAuth.getCurrentUser().getUid();
        databaseReference.child("User").child(id).child("Setting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    setting = snapshot.getValue(Setting.class);
                    updateEditTextSetting();
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

    }

    private void updateEditTextSetting() {
        actualKcal = actualBreakfastKcal + actualLunchKcal + actualDinnerKcal;
        actualProtein = actualBreakfastProtein + actualLunchProtein + actualDinnerProtein;
        actualCarbs = actualBreakfastCarbs + actualLunchCarbs + actualDinnerCarbs;
        actualFats = actualBreakfastFats + actualLunchFats + actualDinnerFats;

        if (setting != null) {
            kcalTextView.setText(actualKcal + " / " + (Integer.toString(setting.getKcal())) + " kcal");
            proteinTextView.setText(actualProtein + " / " + (Integer.toString(setting.getProtein())) + " gr");
            carbsTextView.setText(actualCarbs + " / " + (Integer.toString(setting.getCarbs())) + " gr");
            fatsTextView.setText(actualFats + " / " + (Integer.toString(setting.getFats())) + " gr");
        } else {
            kcalTextView.setText(actualKcal + " / 0 kcal");
            proteinTextView.setText(actualProtein + " / 0 gr");
            carbsTextView.setText(actualCarbs + " / 0 gr");
            fatsTextView.setText(actualFats + " / 0 gr");
        }



    }

    @Override
    public void onBackPressed() {
    }

}