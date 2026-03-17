package com.example.caremicalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ConstraintLayout constraintLayout;
    SeekBar interestSeekBar;
    SeekBar tenureSeekBar;
    float interestRate = 1;
    float tenureRate = 1;
    TextView emiTextView;
    float emi;
    EditText loanEditText;
    TextView interestTextView;
    TextView tenureTextView;
    float interestAmount;
    float totalPayment;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("EMI Calculator");
        constraintLayout = findViewById(R.id.constraintLayout);
        constraintLayout.setOnClickListener(this);
        interestSeekBar = findViewById(R.id.interestRateSeekBar);
        tenureSeekBar = findViewById(R.id.tenureSeekBar);
//        emiTextView = findViewById(R.id.emiTextView);
        interestTextView = findViewById(R.id.interestTextView);
        tenureTextView = findViewById(R.id.tenureTextView);
//        emiTextView.setVisibility(View.INVISIBLE);
        loanEditText = findViewById(R.id.loanEditText);
        interestTextView.setText("1");
        tenureTextView.setText("1");
        interestSeekBar.setMax(20);
        interestSeekBar.setProgress(1);

        interestSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                interestRate = (float) i;
                if (i < 1) {
                    seekBar.setProgress(1);
                }
                if (i != 0) {
                    interestTextView.setText(String.valueOf(i));

                }
                Log.i("seekbar", String.valueOf(interestRate));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tenureSeekBar.setMax(7);
        tenureSeekBar.setProgress(1);
        tenureSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tenureRate = (float) i;
                if (i < 1) {
                    seekBar.setProgress(1);

                }
                if (i != 0) {
                    tenureTextView.setText(String.valueOf(i));
                }
                Log.i("seekbar", String.valueOf(tenureRate));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.constraintLayout) {
            InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    public void calculateEMI(View view) {
        if (loanEditText.getText().length() > 0) {


            float newTenure = tenureRate * 12;
            emi = (float) ((float) (Float.parseFloat(loanEditText.getText().toString()) * interestRate / 12 / 100 * Math.pow(1 + interestRate / 12 / 100, newTenure)) / (Math.pow(1 + interestRate / 12 / 100, newTenure) - 1));


            interestAmount = -(Float.parseFloat(loanEditText.getText().toString()) - (emi * 12 * tenureRate));
            totalPayment = interestAmount + Float.parseFloat(loanEditText.getText().toString());

            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("emi", emi);
            intent.putExtra("interestAmount", interestAmount);
            intent.putExtra("totalAmount", totalPayment);
            intent.putExtra("principalAmount", Integer.parseInt(loanEditText.getText().toString()));
            startActivity(intent);

        } else {
            Toast.makeText(this, "Please enter Loan Amount", Toast.LENGTH_SHORT).show();
        }

    }
}