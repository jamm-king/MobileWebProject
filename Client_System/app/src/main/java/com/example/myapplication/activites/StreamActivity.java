package com.example.myapplication.activites;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.HttpClient;
import com.example.myapplication.R;
import com.example.myapplication.StreamServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

import com.example.myapplication.utils.Network;

public class StreamActivity extends AppCompatActivity implements StreamServer.ImageCallback {

    private StreamServer server;
    private ImageView imageView;
    private Button button1;
    private Button button2;
    private boolean isStreaming = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);

        server = new StreamServer();
        server.setImageCallback(this);
        imageView = findViewById(R.id.kidState);
        button1 = findViewById(R.id.myButton1);
        button2 = findViewById(R.id.myButton2);

        try {
            server.start(10000, false);
            System.out.println("Start stream server.");
            String ipAddress = Network.getDeviceIpAddress(this);

            if (ipAddress != null) {
                Toast.makeText(StreamActivity.this, String.format("Device IP Address: %s", ipAddress), Toast.LENGTH_SHORT).show();
                System.out.println(String.format("Device IP Address: %s", ipAddress));

                Map<String, Object> data = new HashMap<String, Object>();
                data.put("id", 1);
                data.put("name", "pos05169");
                data.put("state", "waiting_stream");
                data.put("host", ipAddress);
                HttpClient httpClient = new HttpClient();
                new Thread(() -> {
                    httpClient.updateClient(data);
                }).start();
            } else {
                Toast.makeText(StreamActivity.this, String.format("There's no IP Address"), Toast.LENGTH_SHORT).show();
                System.out.println("Unable to retrieve IP address");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectToVideoServer();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform the disconnection from the video server and stop streaming
                disconnectFromVideoServer();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (server != null) {
            server.stop();
            System.out.println("End stream server.");
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("id", 1);
        data.put("name", "pos05169");
        data.put("state", "sleep");
        // data.put("host", ipAddress);
        HttpClient httpClient = new HttpClient();
        new Thread(() -> {
            httpClient.updateClient(data);
        }).start();
    }
//    @Override
//    public void onImageReceived(byte[] imageData) {
//        runOnUiThread(() -> updateImageView(imageData));
//    }
//    private void updateImageView(byte[] imageData) {
//        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
//        imageView.setImageBitmap(bitmap);
//    }
    @Override
    public void onImageReceived(byte[] imageData) {
        // Execute AsyncTask to process image data in the background
        new ImageProcessingTask().execute(imageData);
    }

    // AsyncTask to perform image processing in the background
    private class ImageProcessingTask extends AsyncTask<byte[], Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(byte[]... params) {
            // Process image data in the background
            byte[] imageData = params[0];
            return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // Update the UI on the main thread with the processed image
            updateImageView(bitmap);
        }
    }

    private void updateImageView(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
    private void connectToVideoServer() {
        button1.setEnabled(false);
        button2.setEnabled(true);
        isStreaming = true;
    }
    private void disconnectFromVideoServer() {
        button1.setEnabled(true);
        button2.setEnabled(false);
        isStreaming = false;
    }
    @Override
    public void onBackPressed() {
        if (isStreaming) {
            disconnectFromVideoServer();
        }
        super.onBackPressed();
    }
}
