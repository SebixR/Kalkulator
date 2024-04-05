package com.example.kalkulator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.License;

import java.text.DecimalFormat;
import java.util.Locale;

public class ComplexCalculatorActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView expressionView, resultView;
    private StringBuilder expression;
    private AppLogic appLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complex);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        assignIds();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String expression = extras.getString("expression");
            String result = extras.getString("result");
            expressionView.setText(expression);
            resultView.setText(result);
        }

        appLogic = new AppLogic(resultView, expressionView.getText().toString());
        expression = new StringBuilder();
    }

    public void assignId(int id) {
        MaterialButton button = findViewById(id);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        appLogic.setButton(view);
        appLogic.setExpression(expressionView.getText().toString());

        if (button.getId() == R.id.buttonSwitch) {
            handleSwitch();
        }

        expression = appLogic.handleClick();

        expressionView.setText(expression.toString());
    }

    public void handleSwitch() {
        Intent intent = new Intent(this, SimpleCalculatorActivity.class);

        intent.putExtra("expression", expressionView.getText().toString());
        intent.putExtra("result", resultView.getText().toString());

        startActivity(intent);
        finish();
    }

    public void assignIds()
    {
        resultView = findViewById(R.id.resultView);
        expressionView = findViewById(R.id.expressionView);

        assignId(R.id.buttonAC);
        assignId(R.id.buttonBack);
        assignId(R.id.buttonSign);
        assignId(R.id.buttonDivide);
        assignId(R.id.buttonMultiply);
        assignId(R.id.buttonSubtract);
        assignId(R.id.buttonAdd);
        assignId(R.id.buttonSwitch);
        assignId(R.id.buttonEquals);
        assignId(R.id.buttonComma);

        assignId(R.id.button1);
        assignId(R.id.button2);
        assignId(R.id.button3);
        assignId(R.id.button4);
        assignId(R.id.button5);
        assignId(R.id.button6);
        assignId(R.id.button7);
        assignId(R.id.button8);
        assignId(R.id.button9);
        assignId(R.id.button0);

        assignId(R.id.buttonSin);
        assignId(R.id.buttonCos);
        assignId(R.id.buttonTan);
        assignId(R.id.buttonPi);
        assignId(R.id.buttonE);
        assignId(R.id.buttonFactor);
        assignId(R.id.buttonPower);
        assignId(R.id.buttonRoot);
        assignId(R.id.buttonLog);
        assignId(R.id.buttonLn);
        assignId(R.id.buttonPercent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("result", resultView.getText().toString());
        outState.putString("expression", expressionView.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        resultView.setText(savedInstanceState.getString("result"));
        expressionView.setText(savedInstanceState.getString("expression"));
        super.onRestoreInstanceState(savedInstanceState);
    }
}
