//Chi Ngo
//cngongoc

package ds.cmu.edu.cngongoc.betterdoctor;

import android.app.ActionBar;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import org.json.simple.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    final MainActivity ma = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the view to be activity_main.xml
        setContentView(R.layout.activity_main);
        //I took the code to create a spinner from
        //https://mkyong.com/android/android-spinner-drop-down-list-example/
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.specialty, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //create a new submit button
        Button submitButton = findViewById(R.id.search_submit);
        //when the submit button is clicked on
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the city name from the text box
                String city = ((EditText)findViewById(R.id.city)).getText().toString();
                //get the state name the text box
                String state = ((EditText)findViewById(R.id.state)).getText().toString();
                //get the zipcode from the text box
                String zip = ((EditText)findViewById(R.id.zip)).getText().toString();
                //get the selected item from the dropdown menu
                String specialty = ((Spinner)findViewById(R.id.spinner)).getSelectedItem().toString();
                //search for a doctor
                GetDoctors gd = new GetDoctors();
                gd.search(city, state, zip, specialty, ma);
            }
        });

    }

    //when doctors are found
    public void doctorsReady(final HashMap<String,JSONObject> doctors) {
        //get the group of radio buttons
        final RadioGroup rgp= findViewById(R.id.radiogroup);
        //get the submit button for the chosen doctor
        final Button submitButton = findViewById(R.id.doc_submit);
        //get the submit button from the main view
        final Button submit = findViewById(R.id.search_submit);
        //get the spinner from the main view
        final Spinner specialty = findViewById(R.id.spinner);
        //get the city text box from the main view
        final TextView city = (EditText)findViewById(R.id.city);
        //get the state text box from the main view
        final TextView state = (EditText)findViewById(R.id.state);
        //get the zip code text box from the main view
        final TextView zip = (EditText)findViewById(R.id.zip);
        //get all the texts displayed in the main view
        final TextView prompt = findViewById(R.id.prompt);
        final TextView prompt2 = findViewById(R.id.prompt2);
        final TextView cityPrompt = findViewById(R.id.cityPrompt);
        final TextView statePrompt = findViewById(R.id.statePrompt);
        final TextView zipPrompt = findViewById(R.id.zipPrompt);
        //make all views from the main page disappear
        submit.setVisibility(View.GONE);
        prompt.setVisibility(View.GONE);
        prompt2.setVisibility(View.GONE);
        cityPrompt.setVisibility(View.GONE);
        statePrompt.setVisibility(View.GONE);
        zipPrompt.setVisibility(View.GONE);
        specialty.setVisibility(View.GONE);
        city.setVisibility(View.GONE);
        state.setVisibility(View.GONE);
        zip.setVisibility(View.GONE);
        //get the button to return to the main page
        final Button homeButton = findViewById(R.id.home);
        //make this button visible
        homeButton.setVisibility(View.VISIBLE);
        //if this home button is clicked, return to the main search page for a new search
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set the result view to disappear
                rgp.setVisibility(View.GONE);
                submitButton.setVisibility(View.GONE);
                findViewById(R.id.doc_res).setVisibility(View.GONE);
                //remove the new search button
                homeButton.setVisibility(View.GONE);
                //reset the text in the text box
                city.setText("");
                state.setText("");
                zip.setText("");
                specialty.setSelection(0);
                //make the main view visible again for a new search
                prompt.setVisibility(View.VISIBLE);
                prompt2.setVisibility(View.VISIBLE);
                cityPrompt.setVisibility(View.VISIBLE);
                statePrompt.setVisibility(View.VISIBLE);
                zipPrompt.setVisibility(View.VISIBLE);
                specialty.setVisibility(View.VISIBLE);
                city.setVisibility(View.VISIBLE);
                state.setVisibility(View.VISIBLE);
                zip.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
            }
        });
        final TextView resView = findViewById(R.id.doc_res);
        resView.setText("");
        //clear all checks from the radio buttons
        rgp.clearCheck();
        rgp.removeAllViews();
        //if some doctors were found
        if (doctors.size()!=0) {
            //prompt the user to pick a doctor
            resView.append("Here are the doctors that matches your search \n");
            //make the view visible
            resView.setVisibility(View.VISIBLE);
            //make the radio buttons visible
            rgp.setVisibility(View.VISIBLE);
            RadioGroup.LayoutParams rprms;
            int i = 0;
            //populate the radio buttons with the doctors found
            for(String d: doctors.keySet()){
                //I get this code from
                //https://mkyong.com/android/android-radio-buttons-example/
                //and
                //https://stackoverflow.com/questions/46385518/dynamically-adding-radio-button-list-according-to-string-values-in-android-studi
                RadioButton radioButton = new RadioButton(this);
                //set the text on the button to the name of the doctor
                radioButton.setText(d);
                radioButton.setId(i);
                rprms= new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                rgp.addView(radioButton, rprms);
                i+=1;
            }
            //make the submit button for the clicked radio button visible
            submitButton.setVisibility(View.VISIBLE);
            //if this submit button is clicked
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //if a radio button is chosen before the user clicks submit
                    if (rgp.getCheckedRadioButtonId() != -1) {
                        //get the text from the radio button
                        String value = ((RadioButton)findViewById(rgp.getCheckedRadioButtonId())).getText().toString();
                        //remove all the radio buttons
                        rgp.setVisibility(View.GONE);
                        //remove the submit button
                        submitButton.setVisibility(View.GONE);
                        //reset the displayed text
                        resView.setText("");
                        //display the chosen doctor information
                        resView.append("Name: " + value + "\n");
                        //get the chosen doctor from the hashmap using the name value as key
                        JSONObject docInfo = doctors.get(value);
                        //display the address
                        resView.append("Address: " + docInfo.get("address_1") + "\n");
                        if (docInfo.containsKey("address_2")) {
                            resView.append("                 " + docInfo.get("address_2") + "\n");
                        }
                        //display the city, state, and zip code information
                        resView.append("                 " + docInfo.get("city") + ", " + docInfo.get("state") + " " + docInfo.get("zip") + "\n");
                        //display the phone number
                        resView.append("Phone number: " + docInfo.get("phone"));
                    }
                }
            });
            //if no doctor was found
        } else {
            //display the status
            resView.append("There's no doctor that matches your search");
            resView.setVisibility(View.VISIBLE);
        }
        //invalidate result view and all radio buttons
        resView.invalidate();
        rgp.invalidate();
    }
}
