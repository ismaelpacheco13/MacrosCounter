package com.ismael.macroscounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.List;
import java.util.UUID;

public class SettingActivity extends AppCompatActivity {

    private EditText weightEditText;
    private EditText ageEditText;
    private EditText heightEditText;
    private Button saveButtonSetting;
    private Spinner spinnerGender;
    private Spinner spinnerPhysicalActivity;
    private Spinner spinnerGoal;

    private Setting setting;

    private int bmr = 0;
    private int kcal = 0;
    private int protein = 0;
    private int carbs = 0;
    private int fats = 0;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColor));
        }

        setting = new Setting();

        // Inicio de Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        weightEditText = findViewById(R.id.weightEditText);
        ageEditText = findViewById(R.id.ageEditText);
        heightEditText = findViewById(R.id.heightEditText);
        saveButtonSetting = findViewById(R.id.saveButtonSetting);

        // Spinner elements
        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerPhysicalActivity = findViewById(R.id.spinnerPhysicalActivity);
        spinnerGoal = findViewById(R.id.spinnerGoal);

        // Spinner click listener
        //spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> genders = new ArrayList<String>();
        genders.add("Hombre");
        genders.add("Mujer");

        List<String> physicalActivities = new ArrayList<String>();
        physicalActivities.add("Sedentario (Poco o nada de ejercicio)");
        physicalActivities.add("Levemente activo (Deporte 1-3 veces por semana)");
        physicalActivities.add("Moderadamente activo (Deporte 3-5 veces por semana)");
        physicalActivities.add("Muy activo (Deporte 6-7 veces por semana)");
        physicalActivities.add("Hiperactivo (Deporte todos los días + de 2h)");

        List<String> goals = new ArrayList<String>();
        goals.add("Perder peso lentamente");
        goals.add("Perder peso rápidamente");
        goals.add("Mantener peso");
        goals.add("Ganar peso lentamente");
        goals.add("Ganar peso rápidamente");

        // Creating adapters for spinners
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genders);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, physicalActivities);
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, goals);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerGender.setAdapter(dataAdapter);
        spinnerPhysicalActivity.setAdapter(dataAdapter2);
        spinnerGoal.setAdapter(dataAdapter3);

        getUserSetting(); // Se cargan las settings en caso de que ya hayan sido introducidas

        // Botón guardar
        saveButtonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weightEditText.getText().toString().equals("")) {
                    weightEditText.setError("Peso requerido");
                } else if (ageEditText.getText().toString().equals("")) {
                    ageEditText.setError("Edad requerida");
                } else if (heightEditText.getText().toString().equals("")) {
                    heightEditText.setError("Altura requerida");
                } else { // Se añade la configuración del usuario a la base de datos

                    calculateProfile();

                    setting.setHeight(Integer.parseInt(heightEditText.getText().toString()));
                    setting.setAge(Integer.parseInt(ageEditText.getText().toString()));
                    setting.setWeight(Integer.parseInt(weightEditText.getText().toString()));
                    setting.setGender(spinnerGender.getSelectedItem().toString());
                    setting.setPhysicalActivity(spinnerPhysicalActivity.getSelectedItem().toString());
                    setting.setGoal(spinnerGoal.getSelectedItem().toString());

                    setting.setBmr(bmr);
                    setting.setKcal(kcal);
                    setting.setProtein(protein);
                    setting.setCarbs(carbs);
                    setting.setFats(fats);

                    databaseReference.child("User").child(mAuth.getUid()).child("Setting").setValue(setting);

                    Toast.makeText(SettingActivity.this, "Guardada la configuración", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void calculateProfile() {
        // Calculo de calorías metabolismo basal (Fórmula de Harris-Benedict)
        if (spinnerGender.getSelectedItem().toString().equals("Hombre")) {
            bmr = (int) (66.473 + (13.751 * Integer.parseInt(weightEditText.getText().toString())) + (5.0033 * Integer.parseInt(heightEditText.getText().toString())) - (6.7550 * Integer.parseInt(ageEditText.getText().toString())));
        } else {
            bmr = (int) (655.1 + (9.463 * Integer.parseInt(weightEditText.getText().toString())) + (1.8 * Integer.parseInt(heightEditText.getText().toString())) - (4.6756 * Integer.parseInt(ageEditText.getText().toString())));
        }

        // Cálculo del requerimiento calórico basado en la actividad
        if (spinnerPhysicalActivity.getSelectedItem().toString().equals("Sedentario (Poco o nada de ejercicio)")) {
            kcal = (int) (bmr * 1.2);
        } else if (spinnerPhysicalActivity.getSelectedItem().toString().equals("Levemente activo (Deporte 1-3 veces por semana)")) {
            kcal = (int) (bmr * 1.375);
        } else if (spinnerPhysicalActivity.getSelectedItem().toString().equals("Moderadamente activo (Deporte 3-5 veces por semana)")) {
            kcal = (int) (bmr * 1.55);
        } else if (spinnerPhysicalActivity.getSelectedItem().toString().equals("Muy activo (Deporte 6-7 veces por semana)")) {
            kcal = (int) (bmr * 1.725);
        } else {
            kcal = (int) (bmr * 1.9);
        }

        // Cálculo del requerimiento calórico basado en el objetivo (perder, mantener o ganar peso)
        if (spinnerGoal.getSelectedItem().toString().equals("Perder peso lentamente")) {
            kcal -= (kcal * 0.1);
        } else if (spinnerGoal.getSelectedItem().toString().equals("Perder peso rápidamente")) {
            kcal -= (kcal * 0.2);
        } else if (spinnerGoal.getSelectedItem().toString().equals("Ganar peso lentamente")) {
            kcal += (kcal * 0.1);
        } else if (spinnerGoal.getSelectedItem().toString().equals("Ganar peso rápidamente")) {
            kcal += (kcal * 0.2);
        }

        // Cálculo de proteinas, hidratos y grasas
        protein = (int) ((kcal * 0.25) / 4); // Las proteinas son un 25% de las calorias y aproximadamente 4 calorias por gramo
        carbs = (int) ((kcal * 0.5) / 4); // Los carbohidratos son un 50% de las calorias y aproximadamente 4 calorias por gramo
        fats = (int) ((kcal * 0.25) / 9); // Las grasas son un 25% de las calorias y aproximadamente 9 calorias por gramo
    }

    private void getUserSetting() {
        // Obtenemos el id del usuario con el que iniciamos sesión
        String id = mAuth.getCurrentUser().getUid();
        databaseReference.child("User").child(id).child("Setting").addListenerForSingleValueEvent(new ValueEventListener() {
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
        weightEditText.setText(Integer.toString(setting.getWeight()));
        ageEditText.setText(Integer.toString(setting.getAge()));
        heightEditText.setText(Integer.toString(setting.getHeight()));

        switch (setting.getGoal()) { // Se pone en el spinner el que ya esta en la configuración del firebase
            case "Perder peso lentamente":
                spinnerGoal.setSelection(0);
                break;
            case "Perder peso rápidamente":
                spinnerGoal.setSelection(1);
                break;
            case "Mantener peso":
                spinnerGoal.setSelection(2);
                break;
            case "Ganar peso lentamente":
                spinnerGoal.setSelection(3);
                break;
            case "Ganar peso rápidamente":
                spinnerGoal.setSelection(4);
                break;
            default:
        }

        switch (setting.getPhysicalActivity()) { // Se pone en el spinner el que ya esta en la configuración del firebase
            case "Sedentario (Poco o nada de ejercicio)":
                spinnerPhysicalActivity.setSelection(0);
                break;
            case "Levemente activo (Deporte 1-3 veces por semana)":
                spinnerPhysicalActivity.setSelection(1);
                break;
            case "Moderadamente activo (Deporte 3-5 veces por semana)":
                spinnerPhysicalActivity.setSelection(2);
                break;
            case "Muy activo (Deporte 6-7 veces por semana)":
                spinnerPhysicalActivity.setSelection(3);
                break;
            case "Hiperactivo (Deporte todos los días + de 2h)":
                spinnerPhysicalActivity.setSelection(4);
                break;
            default:
        }

        switch (setting.getGender()) { // Se pone en el spinner el que ya esta en la configuración del firebase
            case "Hombre":
                spinnerGender.setSelection(0);
                break;
            case "Mujer":
                spinnerGender.setSelection(1);
                break;
            default:
        }
    }
}