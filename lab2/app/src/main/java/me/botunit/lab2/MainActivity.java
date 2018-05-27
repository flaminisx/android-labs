package me.botunit.lab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements Displayable{


    ResultFragment resultFragment;

    @Override
    public void display(String value) {
        resultFragment.display(value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultFragment = (ResultFragment) getFragmentManager().findFragmentById(R.id.resultFragment);
    }
}
