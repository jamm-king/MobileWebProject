package com.example.myapplication;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class HttpClient {
    // static final String serverUrl = "http://127.0.0.1:8000/api_root/client/1/";
    static final String serverUrl = "https://pos05169.pythonanywhere.com/api_root/client/1/";

    public void updateClient(Map<String, Object> data) {
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            Gson gson = new Gson();
            String jsonData = gson.toJson(data);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonData.getBytes());
            }

            int responseCode = connection.getResponseCode();
            System.out.println("Server response code: " + responseCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

