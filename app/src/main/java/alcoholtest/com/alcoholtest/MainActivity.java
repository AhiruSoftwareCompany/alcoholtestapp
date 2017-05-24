package alcoholtest.com.alcoholtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences("settings", 0);

        //Personendatenbank ist leer
        Log.e("",""+ sharedPref.getAll());
        if(sharedPref.getString("persons", null) == null){
            startActivity(new Intent(this, CreatePerson.class));
        } else {
            //Implement switching between persons.
            try {
                JSONArray persons = new JSONArray(sharedPref.getString("persons", "[]"));
                for(int i = 0; i < persons.length(); i++){
                    JSONObject person = (JSONObject) persons.get(i);
                    Toast.makeText(this, "Willkommen zurück, " + person.getString("name"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
