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


public class SimpleCalculatorActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView expressionView, resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        assignIds();
    }

    public void assignId(int id) {
        MaterialButton button = findViewById(id);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;

        String buttonText = button.getText().toString();
        StringBuilder expression = new StringBuilder(expressionView.getText().toString());

        if (button.getId() == R.id.buttonSwitch) {
            handleSwitch();
        }

        if (button.getId() == R.id.buttonAC){
            expression = new StringBuilder();
            resultView.setText("0");
        }
        else if (button.getId() == R.id.buttonSign) {
            handleSign(expression);
        }
        else if (button.getId() == R.id.buttonBack && (expression.length() > 0)) {
            expression = handleBack(expression);
        }
        else if (button.getId() == R.id.buttonEquals) {
            handleEquals(expression);
        }
        else {
            if (validate(expression.toString(), button.getId())) {

                expression.append(buttonText);

                if (button.getId() == R.id.buttonSin || button.getId() == R.id.buttonCos || button.getId() == R.id.buttonTan
                        || button.getId() == R.id.buttonLog || button.getId() == R.id.buttonLn) {
                    expression.append("(");
                }
            }
        }

        expressionView.setText(expression.toString());
    }

    public void handleSwitch() {
        Intent intent = new Intent(this, ComplexCalculatorActivity.class);

        startActivity(intent);
    }

    public void handleSign(StringBuilder expression) {
        if (expression.length() > 0) {
            for (int i = expression.length() - 1; i >= 0; i--) {
                if (expression.charAt(i) == '+' || expression.charAt(i) == '-' || expression.charAt(i) == '/'
                        || expression.charAt(i) == '*' || expression.charAt(i) == '%'  || expression.charAt(i) == '√'
                        || i == 0) {
                    if (expression.charAt(i) == '-') expression.setCharAt(i, '+');
                    else if (expression.charAt(i) == '+') expression.setCharAt(i, '-');
                    else expression.insert(i, '-');
                    break;
                }
            }
        }
    }

    public StringBuilder handleBack(StringBuilder expression) {
        if (expression.length() >= 4) {
            String subString = expression.substring(expression.length() - 4);
            if (subString.equals("sin(") || subString.equals("cos(") || subString.equals("tan(")) {
                expression = new StringBuilder(expression.substring(0, expression.length() - 4));
            }
            else expression = new StringBuilder(expression.substring(0, expression.length() - 1));
        }
        else if (expression.length() >= 3) {
            String subString = expression.substring(expression.length() - 3);
            if (subString.equals("ln(") || subString.equals("lg(")) {
                expression = new StringBuilder(expression.substring(0, expression.length() - 3));
            }
            else expression = new StringBuilder(expression.substring(0, expression.length() - 1));
        }
        else expression = new StringBuilder(expression.substring(0, expression.length() - 1));

        return expression;
    }

    public void handleEquals(StringBuilder expression) {
        if (expression.length() > 0) {

            int openBrackets = 0, closedBrackets = 0;
            for (int i = 0; i < expression.length(); i++){
                if (expression.charAt(i) == '(') openBrackets++;
                if (expression.charAt(i) == ')') closedBrackets++;
            }
            for (int i = 0; i < openBrackets - closedBrackets; i++) {
                expression.append(')');
            }

            String result = calculate(expression.toString());
            resultView.setText(result);
        }
    }

    public boolean validate(String expression, int buttonId) {

        if (!expression.isEmpty()) {
            boolean isDigit = !Character.isDigit(expression.charAt(expression.length() - 1));
            if ((buttonId == R.id.buttonAdd || buttonId == R.id.buttonSubtract || buttonId == R.id.buttonMultiply || buttonId == R.id.buttonDivide)
                && isDigit && expression.charAt(expression.length() - 1) != 'e' && expression.charAt(expression.length() - 1) != ')'
                    && expression.charAt(expression.length() - 1) != '(' && expression.charAt(expression.length() - 1) != '%'
                    && expression.charAt(expression.length() - 1) != '!' && expression.charAt(expression.length() - 1) != 'π') {
                return false;
            }
            else return true;
        }
        else {
            if (buttonId == R.id.buttonAdd || buttonId == R.id.buttonMultiply || buttonId == R.id.buttonDivide
                    || buttonId == R.id.buttonPercent || buttonId == R.id.buttonFactor || buttonId == R.id.buttonPower) {
                return false;
            }
            else return true;
        }
    }

    public String calculate(String expression) {
        try {

            License.iConfirmNonCommercialUse("Sebastian Rogaczewski");
            Expression mxExpression = new Expression(expression);

            double number = mxExpression.calculate();

            DecimalFormat df = new DecimalFormat("#.########");
            String result = df.format(number);
            if (result.equals("NaN")) {
                return "Error";
            }
            if (result.endsWith(".0")) {
                result = result.substring(0, result.length() - 2);
            }
            if (result.length() > 10) {
                result = String.format(Locale.US, "%.4E", number);
            }
            return result;
        } catch (Exception e) {
            return "Error";
        }
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