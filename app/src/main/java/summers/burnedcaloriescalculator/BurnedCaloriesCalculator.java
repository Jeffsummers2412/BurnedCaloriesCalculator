package summers.burnedcaloriescalculator;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import java.text.NumberFormat;

import static android.R.attr.format;

public class BurnedCaloriesCalculator extends AppCompatActivity implements OnEditorActionListener, OnSeekBarChangeListener {

    //define widget variables
    private TextView milesTotalTV;
    private TextView calBurnedTV;
    private TextView bmiAmountTV;
    private EditText weightET;
    private SeekBar seekBar;
    private Spinner footSpinner;
    private Spinner inchSpinner;
    private SharedPreferences savedValues;

    // define instance variables
    private String weightAmountString = "";
    private float milesRan = 1f;
    int foot = 1;
    int inch = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burned_calories_calculator);

        //get references to the widget
        milesTotalTV = (TextView) findViewById(R.id.milesTotalTV);
        calBurnedTV = (TextView) findViewById(R.id.calBurnedTV);
        bmiAmountTV = (TextView) findViewById(R.id.bmiAmountTV);
        weightET = (EditText) findViewById(R.id.weightET);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        footSpinner = (Spinner) findViewById(R.id.footSpinner);
        inchSpinner = (Spinner) findViewById(R.id.inchSpinner);


        //set the seek bar to OnProgresschange
        seekBar.setOnSeekBarChangeListener(this);

        //set array adapter to foot spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.foot_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        footSpinner.setAdapter(adapter);

        //set array adapter to inch spiner
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(
                this, R.array.inch_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inchSpinner.setAdapter(ad);

        //
        footSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                foot = position + 1;
                calculateAndDisplay();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        inchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inch = position + 1;
                calculateAndDisplay();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        weightET.setOnEditorActionListener(this);

        //get the shared preferences
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        milesTotalTV.setText(progress + "mi");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        milesRan = (float) progress;

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {

                calculateAndDisplay();
            }

        return false;
    }

    public void calculateAndDisplay(){

        weightAmountString = weightET.getText().toString();
        float weight;
        double calBurned;
        double bmiAmount;

        if( weightAmountString.equals("")){
            weight = 0;
        }
        else{
            weight = Float.parseFloat(weightAmountString);
        }

        float footAmount;
        if(foot == 1){
            footAmount = 3;
        }else if(foot == 2){
            footAmount = 4;
        }else if(foot == 3){
            footAmount = 5;
        }else if(foot == 4){
            footAmount = 6;
        }else if(foot == 5){
            footAmount = 7;
        }else if(foot == 6){
            footAmount = 8;
        }else{
            footAmount = 0;
        }

        float inchAmount;
        if(inch == 1){
            inchAmount = 1;
        }else if(inch == 2){
            inchAmount = 2;
        }else if(inch == 3){
            inchAmount = 3;
        }else if(inch == 4){
            inchAmount = 4;
        }else if(inch == 5){
            inchAmount = 5;
        }else if(inch == 6){
            inchAmount = 6;
        }else if(inch == 7){
            inchAmount = 7;
        }else if(inch == 8){
            inchAmount = 8;
        }else if(inch == 9){
            inchAmount = 9;
        }else if(inch == 10){
            inchAmount = 10;
        }else if(inch == 11){
            inchAmount = 11;
        }else{
            inchAmount = 0;
        }


        calBurned = 0.75 * weight * milesRan;
        bmiAmount = (weight *703)/((12 * footAmount+inchAmount) * (12 * footAmount+inchAmount));

        NumberFormat number = NumberFormat.getNumberInstance();
        calBurnedTV.setText(number.format(calBurned));
        bmiAmountTV.setText(number.format(bmiAmount));


    }

    @Override
    protected void onPause() {
        //save the instance variables
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("weightAmountString", weightAmountString);
        editor.putInt("mileRand", 1);
        editor.putInt("foot", foot);
        editor.putInt("inch", inch);

        editor.apply();
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //get the instance variables
       weightAmountString = savedValues.getString("weightAmountString", "");
        milesRan = savedValues.getInt("milesRan", 1);



        //set the split on spinner
        int position = foot -1;
        int position2 = inch -1;
        footSpinner.setSelection(position);
        inchSpinner.setSelection(position2);
    }

}
