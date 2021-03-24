package com.gieasesales.Http;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class HttpUtilNew {

    public static String makeApiGETRequest(String url, Context context) {
        String result ="";
        String charset = "UTF-8";
        try {
            URL urlObj = new URL(url);
            HttpURLConnection http_con = (HttpURLConnection) urlObj.openConnection();
            http_con.setDoOutput(false);
            http_con.setRequestProperty("Content-Type", "application/json");
            http_con.setRequestProperty("type", "Mobile");
            http_con.setRequestMethod("GET");
            http_con.setRequestProperty("Accept-Charset", charset);
            http_con.setConnectTimeout(15000);
            http_con.connect();
            result = convertStreamToString(http_con, http_con.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String makeApiPOSTRequest(String url, String paramsString, Context context) {
        String result = "";
        String charset = "UTF-8";

        try {
            URL urlObj = new URL(url);

            HttpURLConnection http_con = (HttpURLConnection) urlObj.openConnection();

            http_con.setDoOutput(true);
            http_con.setRequestProperty("Content-Type", "application/json");
            http_con.setRequestProperty("type", "Mobile");
            http_con.setRequestMethod("POST");
            http_con.setRequestProperty("Accept-Charset", charset);
            http_con.setReadTimeout(10000);
            http_con.setConnectTimeout(15000);
            http_con.connect();

            DataOutputStream wr = new DataOutputStream(http_con.getOutputStream());
            wr.writeBytes(paramsString);
            wr.flush();
            wr.close();
            http_con.getInputStream();
            result = convertStreamToString(http_con, http_con.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    public static String makeApiPUTRequest(String url, String paramsString, Context context) {
        String result = "";
        try {
            URL urlObj = new URL(url);
            HttpURLConnection http_con = (HttpURLConnection) urlObj.openConnection();
            http_con.setDoOutput(true);
            http_con.setRequestProperty("Content-Type", "application/json");
            http_con.setRequestProperty("Type", "user");
            http_con.setRequestMethod("PUT");
            DataOutputStream wr = new DataOutputStream(http_con.getOutputStream());
            wr.writeBytes(paramsString);
            wr.flush();
            wr.close();
            result = convertStreamToString(http_con, http_con.getInputStream());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String makeApiDELETERequest(String url, Context context) {
        String result = "";
        try {
            URL urlObj = new URL(url);
            HttpURLConnection http_con = (HttpURLConnection) urlObj.openConnection();
            http_con.setRequestProperty("Content-Type", "application/json");
            http_con.setRequestMethod("DELETE");
            http_con.connect();
            try {
                result = convertStreamToString(http_con, http_con.getInputStream());
            }catch (Exception e){
                result = convertStreamToString(http_con, http_con.getErrorStream());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String convertStreamToString(HttpURLConnection conn, InputStream is) {
        StringBuilder result = null;
        try {
            //Receive the response from the server
            InputStream in = new BufferedInputStream(is);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.d(" HttpUtilNew ", "Response result: " + result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.disconnect();
        return result.toString();
    }
}