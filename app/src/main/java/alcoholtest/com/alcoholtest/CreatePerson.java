package alcoholtest.com.alcoholtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreatePerson extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_person);

        Button createPerson = (Button) findViewById(R.id.createPerson);
        createPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPerson();
            }
        });

    }

    public void addPerson() {
        TextView name = (TextView) findViewById(R.id.name);
        TextView age = (TextView) findViewById(R.id.age);
        TextView weight = (TextView) findViewById(R.id.weight);
        TextView height = (TextView) findViewById(R.id.height);
        RadioButton male = (RadioButton) findViewById(R.id.sex_male);
        boolean isMale = male.isChecked();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        try {
            JSONArray persons = new JSONArray(sharedPref.getString("persons", "[]"));
            JSONObject person = new JSONObject();
            person.put("name", name.getText());
            person.put("isMale", isMale);
            person.put("age", Double.parseDouble("" + age.getText()));
            person.put("weight", Double.parseDouble("" + weight.getText()));
            person.put("height", Double.parseDouble("" + height.getText()));
            person.put("created", System.currentTimeMillis() / 1000);
            persons.put(person);
            Log.i("danach", persons.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.commit();

    }
}
