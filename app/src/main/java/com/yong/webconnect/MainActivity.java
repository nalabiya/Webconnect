package com.yong.webconnect;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder( ).permitAll( ).build( );
        StrictMode.setThreadPolicy(policy);

        String website = "http://sepung.kro.kr/calList.php";
        String receiveMsg;
        TextView textview = (TextView)findViewById(R.id.text);

        receiveMsg = Webconnect(website);

        /// webpaser 함수 //////////////////////////////////////////////////
        String userID = null;
        String date = null;
        String k22 = null;
        String r22 = null;
        String o22 = null;

        try {
            JSONArray jarray = new JSONObject(receiveMsg).getJSONArray("calendar");

            for (int i = 0; i < jarray.length(); i++) {
                HashMap map = new HashMap<>();
                JSONObject jObject = jarray.getJSONObject(i);

                userID = jObject.optString("userID");
                date = jObject.optString("date");
                k22 = jObject.optString("k22");
                r22 = jObject.optString("r22");
                o22 = jObject.optString("o22");
                //arraysum[0] = code;
            }

            textview.setText("userID :" + userID);
        } catch (JSONException e) {
            textview.setText(e.toString());
        }
        /////////////////////////////////////////////////////
    }

    public static String Webconnect(String website) {
        String str, receiveMsg = null;

        URL url = null;
        try {
            url = new URL(website);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                Log.i("receiveMsg : ", receiveMsg);

                reader.close();
            } else {
                Log.i("통신 결과", conn.getResponseCode() + "에러");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receiveMsg;
    }
}