package alcoholtest.com.alcoholtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import alcoholtest.com.alcoholtest.model.Person;

public class EditPerson extends AppCompatActivity {
    private TextView tvName;
    private TextView tvAge;
    private TextView tvWeight;
    private TextView tvHeight;
    private RadioButton male;
    private RadioButton female;
    private EditPerson cp;
    private long created;
    private Person currentPerson;
    SharedPreferences sharedPref;
    JSONArray persons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_person);
        tvName = (TextView) findViewById(R.id.name);
        tvAge = (TextView) findViewById(R.id.age);
        tvWeight = (TextView) findViewById(R.id.weight);
        tvHeight = (TextView) findViewById(R.id.height);
        male = (RadioButton) findViewById(R.id.sex_male);
        female = (RadioButton) findViewById(R.id.sex_female);
        cp = this;

        created = getIntent().getLongExtra("created", 0);

        if (created == 0) {
            finish();
        }


        sharedPref = getSharedPreferences("settings", 0);
        persons = null;
        try {
            persons = new JSONArray(sharedPref.getString("persons", "[]"));

            for (int i = 0; i < persons.length(); i++) {
                JSONObject j = new JSONObject(persons.get(i).toString());
                if (j.getLong("created") == created) {
                    currentPerson = new Person(new JSONObject(persons.get(i).toString()));
                    tvName.setText(currentPerson.getName());
                    tvAge.setText(currentPerson.getAge()+"");
                    tvWeight.setText(currentPerson.getWeight()+"");
                    tvHeight.setText(currentPerson.getHeight()+"");
                    if(!currentPerson.isMale()){
                        female.toggle();
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button savePerson = (Button) findViewById(R.id.savePerson);
        savePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editPerson()) {
                    startActivity(new Intent(EditPerson.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(cp, cp.getResources().getText(R.string.wronginput), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean editPerson() {
        String name = tvName.getText().toString();

        //"0" makes an empty field into a zero
        double age = Double.parseDouble("0" + tvAge.getText());
        double weight = Double.parseDouble("0" + tvWeight.getText());
        double height = Double.parseDouble("0" + tvHeight.getText());

        //TODO: Maybe split this up
        if (name.compareTo(cp.getResources().getText(R.string.add_person) + "") != 0 && name.length() > 2 && age > 10 && age < 100 && weight > 30 && weight < 200 && height > 100 && height < 230) {
            try {
                sharedPref = getSharedPreferences("settings", 0);
                SharedPreferences.Editor editor = sharedPref.edit();

                Log.i("Added person", sharedPref.getString("persons", "[]"));
                JSONObject person = new JSONObject();
                person.put("name", name);
                person.put("isMale", male.isChecked());
                person.put("age", age);
                person.put("weight", weight);
                person.put("height", height);
                person.put("created", System.currentTimeMillis() / 1000);

                for (int i = 0; i < persons.length(); i++) {
                    JSONObject j = new JSONObject(persons.get(i).toString());
                    if (j.getLong("created") == created) {
                        persons.put(i, person.toString());
                    }
                }
                Log.i("Current persons object", persons.toString());

                editor.putString("persons", persons.toString());
                editor.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }
}
