package me.botunit.bestplayereu;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final int INTENT_VIDEO = 1;
    private final int INTENT_AUDIO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button videoButton = (Button) findViewById(R.id.buttonVideo);
        Button audioButton = (Button) findViewById(R.id.buttonMusic);

        videoButton.setOnClickListener(v -> {
            Intent pickMedia = new Intent(Intent.ACTION_GET_CONTENT);
            pickMedia.setType("video/*");
            startActivityForResult(pickMedia, INTENT_VIDEO);
        });

        audioButton.setOnClickListener(v -> {
            Intent pickMedia = new Intent(Intent.ACTION_GET_CONTENT);
            pickMedia.setType("audio/*");
            startActivityForResult(pickMedia, INTENT_AUDIO);
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == INTENT_VIDEO){
            Toast.makeText(this, "opening video...", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(this, VideoActivity.class);
            String dataString = data.getDataString();
            intent.putExtra("video", dataString);
            startActivity(intent);
        }else if (resultCode == RESULT_OK && requestCode == INTENT_AUDIO){
            Toast.makeText(this, "opening audio...", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(this, AudioActivity.class);
            intent.setData(data.getData());
            startActivity(intent);
        }
    }
}
