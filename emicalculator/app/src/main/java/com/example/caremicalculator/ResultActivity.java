package com.example.caremicalculator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.NumberFormat;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    // UI Elements
    TextView emiTextView, interestAmountTextView, totalPaymentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();

        // Linking TextViews
        emiTextView = findViewById(R.id.emiTextView);
        interestAmountTextView = findViewById(R.id.interestAmountTextView);
        totalPaymentTextView = findViewById(R.id.totalPaymentTextView);

        // Formatting for Indian Currency (Rupees)
        Locale indialocale = new Locale("en", "IN");
        NumberFormat india = NumberFormat.getCurrencyInstance(indialocale);

        // Extracting data from Intent
        float emiVal = intent.getFloatExtra("emi", 0);
        float interestVal = intent.getFloatExtra("interestAmount", 0);
        float totalVal = intent.getFloatExtra("totalAmount", 0);

        // Formatting strings to remove decimal points for a cleaner look
        String emi = india.format((int) emiVal).split("\\.")[0];
        String interest = india.format((int) interestVal).split("\\.")[0];
        String totalPayment = india.format((int) totalVal).split("\\.")[0];

        // Setting text to TextViews
        emiTextView.setText(emi);
        interestAmountTextView.setText(interest);
        totalPaymentTextView.setText(totalPayment);

        // Optional: Programmatically making the text size larger
        emiTextView.setTextSize(36f); // Make EMI stand out
        interestAmountTextView.setTextSize(24f);
        totalPaymentTextView.setTextSize(24f);
    }
}