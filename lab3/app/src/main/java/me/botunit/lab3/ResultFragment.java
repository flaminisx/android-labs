package me.botunit.lab3;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ResultFragment extends Fragment implements Displayable{

    TextView result;
    DBHelper db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.result_fragment, container, false);
        result = (TextView) v.findViewById(R.id.resultText);
        db = new DBHelper(getActivity());
        display();
        return v;
    }

    @Override
    public void display() {
        String value = getValue();
        display(value);
    }

    private void display(String value){
        if (value.isEmpty()) {
            result.setText("");
            return;
        }
        result.setText("Result is: " + value);
    }

    private String getValue(){
        return db.getKey("result");
    }
}
