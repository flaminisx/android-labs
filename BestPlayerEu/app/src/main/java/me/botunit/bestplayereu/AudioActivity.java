package me.botunit.bestplayereu;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AudioActivity extends AppCompatActivity {

    FloatingActionButton fab;
    MediaPlayer mp;
    ListView songsList;

    JSONArray songs;

    TextView tvArtist, tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvArtist = (TextView) findViewById(R.id.artistText);
        tvTitle = (TextView) findViewById(R.id.titleText);
        Toast.makeText(this, getFilesDir().toString(), Toast.LENGTH_SHORT).show();
        songsList = (ListView) findViewById(R.id.lvMain);
        songsList.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            int songId = 0;
            try {
                songId = songs.getJSONObject(position).getInt("id");
                Toast.makeText(this, "loading " + songs.getJSONObject(position).getString("title"), Toast.LENGTH_SHORT).show();
            }catch(Exception e){
                e.printStackTrace();
            }
            loadSong(songId);
        });

        mp = new MediaPlayer();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener( v -> {
            if(mp.isPlaying()) pause();
            else play();
        });

        findViewById(R.id.loadListBtn).setOnClickListener( v -> {
            loadSongList();
        });

        initPlayer(getIntent().getData());

    }

    private void loadSongList() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://bestplayereu-api.botunit.me/songs";

        JsonArrayRequest stringRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    songs = response;
                    initList(parseTitleList());
                },
                error -> {

                }
        );
        queue.add(stringRequest);
    }

    private void initPlayer(Uri data) {
        try {
            mp.reset();
            mp.setDataSource(this, data);
            mp.prepare();
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
            metaRetriever.setDataSource(this, data);
            String artist =  metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            tvArtist.setText(artist);
            tvTitle.setText(title);
            play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void play(){
        mp.start();
        fab.setImageResource(android.R.drawable.ic_media_pause);
    }
    private void pause(){
        mp.pause();
        fab.setImageResource(android.R.drawable.ic_media_play);
    }

    private void initList(List<String> array){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, array);

        // присваиваем адаптер списку
        songsList.setAdapter(adapter);
    }

    private List<String> parseTitleList(){
        List<String> parsed = new LinkedList<String>();
        try {
            for (int i = 0; i < songs.length(); i++) {
                parsed.add(songs.getJSONObject(i).getString("title"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return parsed;
    }

    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                inFiles.add(file);
            }
        }
        return inFiles;
    }
    private void loadSong(int id){
        String url = "http://bestplayereu-api.botunit.me/songs/" + id;
        Toast.makeText(this, "loading id: " + id, Toast.LENGTH_SHORT).show();
        JsonObjectRequest songRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        String loadUrl = response.getString("url");
                        String[] parts = loadUrl.split("/");
                        String name = parts[parts.length - 1];
                        loadSongData(loadUrl, name);
                    } catch (JSONException e) {
                        Toast.makeText(this, "bad server response", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {

                }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(songRequest);
    }
    private void loadSongData(String url, String filename){
        Toast.makeText(this, "loading " + filename, Toast.LENGTH_SHORT).show();
        InputStreamVolleyRequest request = new InputStreamVolleyRequest(
                Request.Method.GET,
                url,
                (byte[] response) -> {
                    try {
                        if (response!=null) {

                            FileOutputStream outputStream;
                            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                            outputStream.write(response);
                            outputStream.close();
                            Toast.makeText(this, "Download complete.", Toast.LENGTH_LONG).show();
                            playSong(filename);
                        }
                    } catch (Exception e) {
                        Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
                        e.printStackTrace();
                    }
                },
                (VolleyError error) -> {
                    error.printStackTrace();
                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                },
                null
        );
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack());
        mRequestQueue.add(request);
    }
    private void playSong(String filename){
        initPlayer(Uri.fromFile(new File(getFilesDir().toString() + "/" + filename)));
    }
}
