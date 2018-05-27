package me.botunit.lab3;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Map;

public class ActionFragment extends Fragment {

    Spinner spinner;
    EditText input1, input2;

    DBHelper db;

    private class InputWatcher implements TextWatcher{

        String inputName;
        InputWatcher(String name){
            inputName = name;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            db.updateKey(inputName, s.toString());
        }
    }

    public void onAttach(Context c){
        super.onAttach(c);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new DBHelper(getActivity());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.action_fragment, container, false);

        spinner = (Spinner) v.findViewById(R.id.spinner);
        initSpinner();

        input1 = (EditText) v.findViewById(R.id.firstInput);
        input2 = (EditText) v.findViewById(R.id.secondInput);

        addTextWatchers();

        loadValues();

        Button button = (Button) v.findViewById(R.id.button);
        button.setOnClickListener(view -> {
            if(!validateInputs()){
                Toast.makeText(this.getActivity(), "inputs cant be empty", Toast.LENGTH_SHORT).show();
                saveAndDisplayResult("");
                return;
            }
            double value1 = Double.parseDouble(input1.getText().toString());
            double value2 = Double.parseDouble(input2.getText().toString());
            String result = calculate(value1, value2, (int) spinner.getSelectedItemId());
            saveAndDisplayResult(result);
        });
        return v;
    }
    private void initSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.choises_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                db.updateKey("spinner", String.valueOf(id));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private boolean validateInputs(){
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
                    Toast.makeText(this.getActivity(), "second value cant be 0", Toast.LENGTH_SHORT).show();
                    return "";
                }
                result = v1 / v2;
                break;
        }
        return String.valueOf(result);
    }
    private void saveAndDisplayResult(String result){
        db.updateKey("result", result);
        displayResult(result);
    }
    private void displayResult(String result){
        Activity a = this.getActivity();
        if(a instanceof Displayable){
            ((Displayable) a).display();
        }else {
            Toast.makeText(a, "THIS ACTIVITY CANT DISPLAY RESULT", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadValues(){
        Map<String, String> values = db.getValues();
        input1.setText(values.get("input1"));
        input2.setText(values.get("input2"));
        try {
            spinner.setSelection(Integer.valueOf(values.get("spinner")));
        }catch (Exception e){}
    }

    private void addTextWatchers(){
        input1.addTextChangedListener(new InputWatcher("input1"));
        input2.addTextChangedListener(new InputWatcher("input2"));
    }

}
