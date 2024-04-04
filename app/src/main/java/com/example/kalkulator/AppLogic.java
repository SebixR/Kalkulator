package com.example.kalkulator;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.License;

import java.text.DecimalFormat;
import java.util.Locale;

public class AppLogic {

    private MaterialButton button;
    private StringBuilder expression;
    TextView resultView;

    public AppLogic(TextView resultView, String expression) {
        this.resultView = resultView;
        this.expression = new StringBuilder(expression);
    }

    public void setButton(View view) {
        this.button = (MaterialButton) view;
    }

    public void setExpression(String expression) {
        this.expression.replace(0, this.expression.length(), expression);
    }

    public StringBuilder handleClick() {

        String buttonText = button.getText().toString();

        if (button.getId() == R.id.buttonAC) {
            expression = new StringBuilder();
            resultView.setText("0");
        } else if (button.getId() == R.id.buttonSign) {
            handleSign(expression);
        }else if (button.getId() == R.id.buttonComma) {
            expression = handleComma(expression);
        } else if (button.getId() == R.id.buttonBack && (expression.length() > 0)) {
            expression = handleBack(expression);
        } else if (button.getId() == R.id.buttonEquals) {
            handleEquals(expression);
        } else {
            if (validate(expression.toString(), button.getId())) {

                expression.append(buttonText);

                if (button.getId() == R.id.buttonSin || button.getId() == R.id.buttonCos || button.getId() == R.id.buttonTan
                        || button.getId() == R.id.buttonLog || button.getId() == R.id.buttonLn) {
                    expression.append("(");
                }
            }
        }

        return expression;
    }

    public void handleSign(StringBuilder expression) {
        if (expression.length() > 0) {
            for (int i = expression.length() - 1; i >= 0; i--) {
                if (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == 'π')
                {
                    while (i >= 0 && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == 'π')) {
                        i--;
                    }
                    if (i >= 0) {
                        if (expression.charAt(i) == '-') expression.setCharAt(i, '+');
                        else if (expression.charAt(i) == '+') expression.setCharAt(i, '-');
                        else expression.insert(i + 1, '-');
                        break;
                    }
                    else expression.insert(0, '-');
                }
            }

        }
    }

    public StringBuilder handleComma(StringBuilder expression) {
        if (expression.length() > 0) {
            boolean isDot = false;
            for (int i = expression.length() - 1; i >= 0 && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.'); i--) {
                if (expression.charAt(i) == '.') {
                    isDot = true;
                    break;
                }
            }
            if (Character.isDigit(expression.charAt(expression.length() - 1)) && !isDot) {
                expression.append(".");
            }
        }
        return expression;
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

}
