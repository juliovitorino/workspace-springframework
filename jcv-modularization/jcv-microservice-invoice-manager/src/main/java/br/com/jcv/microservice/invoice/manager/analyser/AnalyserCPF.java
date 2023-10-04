package br.com.jcv.microservice.invoice.manager.analyser;

import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AnalyserCPF extends AbstractAnalyser implements IAnalyser<String> {
    public static String REGEX_CPF = "[0-9]{3}(\\.[0-9]{3}){2}-[0-9]{2}";

    public static final String FORMAT_CPF = "999.999.999-99";
    @Override
    public void execute(String input) {
        if( Objects.isNull(input)) {
            throw new RuntimeException("CPF Está nulo");
        }
        Pattern er = Pattern.compile(REGEX_CPF);
        Matcher result = er.matcher(input);
        if (!result.matches()) {
            throw new RuntimeException(getErrorMessage(input, FORMAT_CPF));
        }

        if(!computeDigits(input.replaceAll("[.-]",""))) {
            throw new RuntimeException("CPF não é válido. Verifique o CPF enviado.");
        }
    }

    private boolean computeDigits(String cpfOnlyNumber) {
        int length = cpfOnlyNumber.length();
        String firstSequence = cpfOnlyNumber.substring(0,length-2);
        int d2 = Integer.parseInt(cpfOnlyNumber.substring(length-1, length));
        int d1 = Integer.parseInt(cpfOnlyNumber.substring(length-2, length-1));

        int d1Calc = -1;

        int rod1 = (int) getSum(firstSequence) % 11;
        d1Calc = rod1 < 2 ? 0 : 11 - rod1;

        int d2Calc = -1;
        String secondSequence = cpfOnlyNumber.substring(1,length-2).concat(String.valueOf(d1Calc));
        int rod2 = getSum(secondSequence) % 11;
        d2Calc = rod2 < 2 ? 0 : 11 - rod2;


        return (d1 == d1Calc) && (d2 == d2Calc);
    }

    private int getSum(String numbers) {
        int sumcalc = 0;
        int counter = 10;
        for(int i=0; i < 9; i++) {
            sumcalc += Integer.parseInt(numbers.substring(i,i+1)) * counter--;
        }
        return sumcalc;
    }

}
