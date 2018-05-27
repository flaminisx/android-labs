package me.botunit.lab1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    EditText input1, input2;
    TextView resultText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spinner);
        initSpinner();

        input1 = (EditText) findViewById(R.id.firstInput);
        input2 = (EditText) findViewById(R.id.secondInput);
        Button button = (Button) findViewById(R.id.button);
        resultText = (TextView) findViewById(R.id.textViewResult);

        button.setOnClickListener(v -> {
            if(!validateInputs()){
                Toast.makeText(this, "inputs cant be empty", Toast.LENGTH_SHORT).show();
                displayResult("");
                return;
            }
            double value1 = Double.parseDouble(input1.getText().toString());
            double value2 = Double.parseDouble(input2.getText().toString());
            String result = calculate(value1, value2, (int) spinner.getSelectedItemId());
            displayResult(result);
        });
    }
    private boolean validateInputs(){
        Log.d("LOG_TAG", input1.getText().toString().isEmpty() || input2.getText().toString().isEmpty() ? "true": "false");
        if(input1.getText().toString().isEmpty() || input2.getText().toString().isEmpty())
            return false;
        return true;
    }
    private String calculate(double v1, double v2, int selectedId){
        double result = 0;
        switch (selectedId){
            case 0:
                result = v1 + v2;
                break;
            case 1:
                result = v1 - v2;
                break;
            case 2:
                result = v1 * v2;
                break;
            case 3:
                if(v2 == 0){
                    Toast.makeText(this, "second value cant be 0", Toast.LENGTH_SHORT).show();
                    return "";
                }
                result = v1 / v2;
                break;
        }
        return String.valueOf(result);
    }
    private void initSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.choises_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    private void displayResult(String result){
        if(result.isEmpty()) resultText.setText("");
        else resultText.setText("Result is: " + result);
    }
}
