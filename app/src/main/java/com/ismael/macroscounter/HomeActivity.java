package com.ismael.macroscounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismael.macroscounter.model.Food;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeActivity extends AppCompatActivity {

    private String email = "";

    private AlertDialog.Builder madb;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setup();

        // Inicio de Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        breakfastListV = findViewById(R.id.breakfastListV);
        lunchListV = findViewById(R.id.lunchListV);
        dinnerListV = findViewById(R.id.dinnerListV);

        list();
    }

    private void list() {
        databaseReference.child("User").child(mAuth.getUid()).child("Desayuno").child("Food").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                breakfastList.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    Food food = objSnapshot.getValue(Food.class);
                    breakfastList.add(food);
                }
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
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    Food food = objSnapshot.getValue(Food.class);
                    lunchList.add(food);
                }
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
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    Food food = objSnapshot.getValue(Food.class);
                    dinnerList.add(food);
                }
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
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        email = bundle.getString("email");

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
        }
        return super.onOptionsItemSelected(item);
    }

    // Setup

}