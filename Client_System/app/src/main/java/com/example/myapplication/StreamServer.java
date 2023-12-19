package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import fi.iki.elonen.NanoHTTPD;

public class StreamServer extends NanoHTTPD {

    private volatile boolean streaming = true;
    private ImageCallback imageCallback;

    public StreamServer() {
        super(8080);
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            Method method = session.getMethod();
            String uri = session.getUri();
            InputStream inputStream = session.getInputStream();

//            System.out.println(String.format("%s %s", method, uri));

            byte[] imageData = readImageData(inputStream);
            inputStream.close();
            System.out.println(imageData.toString());
            System.out.println(imageData.length);

            if(imageCallback != null) {
                imageCallback.onImageReceived(imageData);
            }

            String responseText = "Hello, this is your NanoHTTPD server!";
            return newFixedLengthResponse(responseText);
        } catch(Exception e) {
            e.printStackTrace();
            String responseText = "Hello, this is your NanoHTTPD server!";
            return newFixedLengthResponse(responseText);
        }
    }
    private byte[] readImageData(InputStream inputStream) throws IOException {
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int bytesRead;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            while ((bytesRead = inputStream.read(buffer, 0, bufferSize)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return outputStream.toByteArray();
    }


    public interface ImageCallback {
        void onImageReceived(byte[] imageData);
    }
    public void setImageCallback(ImageCallback callback) {
        this.imageCallback = callback;
    }
}

