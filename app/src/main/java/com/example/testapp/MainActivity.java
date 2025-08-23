package com.example.testapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button calculateButton;
    private RadioButton radioButtonMale, radioButtonFemale;
    private EditText ageEditText, heightEditText, weightEditText;
    private TextView resultTextView;
    private String verdict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViews();
        setupClickListener();
    }
    private void findViews() {
        radioButtonMale = findViewById(R.id.radio_button_male);
        radioButtonFemale = findViewById(R.id.radio_button_female);
        ageEditText = findViewById(R.id.age);
        heightEditText = findViewById(R.id.height);
        weightEditText = findViewById(R.id.weight);
        calculateButton = findViewById(R.id.calculate_button);
        resultTextView = findViewById(R.id.result);
    }
    private void setupClickListener() {
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateBMI();
            }
        });
    }

    private void calculateBMI() {
        String ageStr = ageEditText.getText().toString();
        String heightStr = heightEditText.getText().toString();
        String weightStr = weightEditText.getText().toString();
        if (ageStr.isEmpty() || heightStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // int age = Integer.parseInt(ageStr); // Age is not used in BMI calculation directly, but good to parse if needed elsewhere
            float heightCm = Float.parseFloat(heightStr);
            float weightKg = Float.parseFloat(weightStr);

            // Check for valid height and weight
            if (heightCm <= 0 || weightKg <= 0) {
                Toast.makeText(this, "Height and Weight must be positive values", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convert height from cm to meters
            float heightM = heightCm / 100.0f;

            float bmi = weightKg / (heightM * heightM);
            displayBMIResult(bmi);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers for age, height, and weight", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayBMIResult(float bmi) {
        String verdict = getVerdict(bmi);
        this.resultTextView.setText("BMI: " + String.format("%.2f", bmi) + "\n"+ "You are " + verdict);

    }

    private String getVerdict(float bmi) {
        if (bmi < 18.5) {
            verdict = "Underweight";
        } else if (bmi < 25) {
            verdict = "Normal";
        } else if (bmi < 30) {
           verdict = "Overweight";
        } else {
            verdict = "Obese";
        }
        return verdict;
    }
}