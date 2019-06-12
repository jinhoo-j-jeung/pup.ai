package com.example.shepi13.testlogin.Utils;

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Server related requests are made in ApiConnector. Loaders usually accesss methods in ApiConnector.
 */

public class ApiConnector {
  public static String mApiToken = "";
  private static final String LOGIN_URL = "http://52.165.223.103:8000/user/login";
  private static final String SIGNUP_URL = "http://52.165.223.103:8000/user/register";
  private static final String IMAGE_URL = "http://52.165.223.103:8000/classify";
  private static final String PROFILE_URL = "http://52.165.223.103:8000/user/profile";

  // Wrapper method for Login request
  public static Boolean login(String email, String password) {
    return registerRequest(LOGIN_URL, email, password, null, null);
  }

  // Wrapper method for Register request
  public static Boolean register(String email, String password, String name, String location) {
    return registerRequest(SIGNUP_URL, email, password, name, location);
  }

  // Use MakePostRequest to log or sign the user in. Sends JSONObject.
  private static Boolean registerRequest(String urlString, String email, String password, String name, String location) {
    try {
      JSONObject jsonObject = new JSONObject()
              .put("email", email)
              .put("password", password);

      // if null is passed to name parameter, then it is a login request.
      if (name != null) {
        jsonObject.put("name", name)
                .put("location", location);
      }

      String json = jsonObject.toString();
      String response = MakePostRequest(new URL(urlString), json.getBytes(), "application/json");

      // Saves the token for the future use
      mApiToken = new JSONObject(response).getString("token");
      return mApiToken != null && !mApiToken.equals("");
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    } catch (JSONException e) {
      e.printStackTrace();
      return false;
    }
  }

  // Use MakeGetRequest to retrieves the user's profile data
  public static String profileRequest() {
    try {
      return MakeGetRequest(PROFILE_URL);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  // Use MakePostRequest to send an image data
  public static String sendPhoto(byte[] imageData) {
    try {
      return MakePostRequest(new URL(IMAGE_URL), imageData, "image/png");
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  // Post Request to server.
  @SuppressLint("LogConditional")
  public static String MakePostRequest(URL url, byte[] data, String contentType) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setDoOutput(true);
    connection.setDoInput(true);
    connection.addRequestProperty("Content-type", contentType);

    // If data being sent is an image, set the proper header for the request
    if (contentType.equals("image/png")) {
      connection.addRequestProperty("Content-Disposition", "attachment; filename=upload.jpg");
    }

    // Sets the authorization header for the request
    if (mApiToken != null && !mApiToken.equals("")) {
      connection.addRequestProperty("Authorization", "Token " + mApiToken);
    }

    connection.setFixedLengthStreamingMode(data.length);

    // If takes more than 10 seconds to connect, timeout.
    connection.setConnectTimeout(10000);

    try {
      OutputStream os = connection.getOutputStream();
      os.write(data);

      Log.d("Response Code Post", String.valueOf(connection.getResponseCode()));

      InputStream inputStream = connection.getInputStream();
      Scanner scanner = new Scanner(inputStream);
      scanner.useDelimiter("\\A");

      return scanner.hasNext() ? scanner.next() : null;
    } finally {
      connection.disconnect();
    }
  }

  // Get Request to server
  @SuppressLint("LogConditional")
  public static String MakeGetRequest(String urlString) throws IOException {
    URL url = new URL(urlString);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    // Sets the authorization header for the request
    if (mApiToken != null && !mApiToken.equals("")) {
      connection.addRequestProperty("Authorization", "Token " + mApiToken);
    }

    connection.setConnectTimeout(3000);
    connection.connect();

    try {
      InputStream inputStream = (InputStream) connection.getContent();
      Scanner scanner = new Scanner(inputStream);
      scanner.useDelimiter("\\A");

      Log.d("Response Code Get", String.valueOf(connection.getResponseCode()));

      return scanner.hasNext() ? scanner.next() : null;
    } finally {
      connection.disconnect();
    }
  }

  // Delete Request to server. Used to remove friends or favorite breeds.
  @SuppressLint("LogConditional")
  public static void MakeDeleteRequest(String urlString, byte[] data, @SuppressWarnings("SameParameterValue") String contentType) throws IOException {
    URL url = new URL(urlString);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setDoOutput(true);
    connection.setDoInput(true);
    connection.addRequestProperty("Content-type", contentType);
    connection.setRequestMethod("DELETE");

    // Sets the authorization header for the request
    if (mApiToken != null && !mApiToken.equals("")) {
      connection.addRequestProperty("Authorization", "Token " + mApiToken);
    }

    connection.setFixedLengthStreamingMode(data.length);
    connection.setConnectTimeout(3000);
    connection.connect();

    try {
      OutputStream os = connection.getOutputStream();
      os.write(data);
      InputStream inputStream = (InputStream) connection.getContent();
      Scanner scanner = new Scanner(inputStream);
      scanner.useDelimiter("\\A");

      Log.d("Response Code Delete", String.valueOf(connection.getResponseCode()));
    } finally {
      connection.disconnect();
    }
  }

  // Patch request to server. Only used in ProfileEditActivity.
  @SuppressLint("LogConditional")
  public static String MakePatchRequest(byte[] data, @SuppressWarnings("SameParameterValue") String contentType) throws IOException {
    URL url = new URL(PROFILE_URL);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setDoOutput(true);
    connection.setDoInput(true);
    connection.addRequestProperty("Content-type", contentType);

    // Sets the authorization header for the request
    if (mApiToken != null && !mApiToken.equals("")) {
      connection.addRequestProperty("Authorization", "Token " + mApiToken);
    }

    // sets the proper header for the request
    connection.addRequestProperty("X-HTTP-Method-Override", "PATCH");
    connection.setRequestMethod("POST");
    connection.setFixedLengthStreamingMode(data.length);
    connection.setConnectTimeout(3000);
    connection.connect();

    try {
      OutputStream os = connection.getOutputStream();
      os.write(data);
      InputStream inputStream = (InputStream) connection.getContent();
      Scanner scanner = new Scanner(inputStream);
      scanner.useDelimiter("\\A");

      Log.d("Response Code Patch", String.valueOf(connection.getResponseCode()));

      return scanner.hasNext() ? scanner.next() : null;
    } finally {
      connection.disconnect();
    }
  }
}