package com.example.testapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    public static final String UNDERWEIGHT = "Underweight";
    public static final String NORMAL = "Normal";
    public static final String OVERWEIGHT = "Overweight";
    public static final String OBESE = "Obese";
    private Button calculateButton;
    private RadioButton radioButtonMale, radioButtonFemale;
    private EditText ageEditText, heightEditText, weightEditText;
    private TextView resultTextView;
    private String verdict;
    private Switch unitSwitch;

    private final Handler tipHandler = new Handler(Looper.getMainLooper());
    private final Runnable tipRunnable = () -> {
        Toast toast = Toast.makeText(this,
                "Tip: use the switch to change the metric system.",
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
        toast.show();
    };

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
        // Show toast after 5 seconds
        tipHandler.postDelayed(tipRunnable, 3000);
        findViews();
        setupListeners();
    }

    private void findViews() {
        radioButtonMale = findViewById(R.id.radio_button_male);
        radioButtonFemale = findViewById(R.id.radio_button_female);
        ageEditText = findViewById(R.id.age);
        heightEditText = findViewById(R.id.height);
        weightEditText = findViewById(R.id.weight);
        calculateButton = findViewById(R.id.calculate_button);
        resultTextView = findViewById(R.id.result);
        unitSwitch = findViewById(R.id.unitSwitch);
    }

    private void setupListeners() {
        unitSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                heightEditText.setHint("Height (in)");
                weightEditText.setHint("Weight (lbs)");
            } else {
                heightEditText.setHint("Height (cm)");
                weightEditText.setHint("Weight (kg)");
            }
        });

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
            float height = Float.parseFloat(heightStr);
            float weight = Float.parseFloat(weightStr);
            float heightCm = 0, weightKg=0;

            // Check for valid height and weight
            if (height <= 0 || weight <= 0) {
                Toast.makeText(this, "Height and Weight must be positive values", Toast.LENGTH_SHORT).show();
                return;
            }
            if (unitSwitch.isChecked()) {
                heightCm = height * 2.54f;        // inches → cm
                weightKg = weight * 0.453592f;
            }else{
                heightCm = height;
                weightKg = weight;
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
        this.resultTextView.setText("BMI: " + String.format("%.2f", bmi) + "\n" + "You are " + verdict);
        // set color depending on verdict
        switch (verdict) {
            case UNDERWEIGHT:
                this.resultTextView.setTextColor(Color.parseColor("#1E90FF")); // blue-ish
                break;
            case NORMAL:
                this.resultTextView.setTextColor(Color.parseColor("#228B22")); // green
                break;
            case OVERWEIGHT:
                this.resultTextView.setTextColor(Color.parseColor("#FFA500")); // orange
                break;
            case OBESE:
                this.resultTextView.setTextColor(Color.RED);
                break;
        }
    }

    private String getVerdict(float bmi) {
        if (bmi < 18.5) {
            verdict = UNDERWEIGHT;
        } else if (bmi < 25) {
            verdict = NORMAL;
        } else if (bmi < 30) {
            verdict = OVERWEIGHT;
        } else {
            verdict = OBESE;
        }
        return verdict;
    }
}