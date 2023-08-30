package br.com.jcv.commons.library.analyser;

import br.com.jcv.commons.library.constantes.GenericConstantes;
import br.com.jcv.commons.library.constantes.RegexConstantes;
import br.com.jcv.commons.library.exception.AnalyserException;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalyserCPF extends AbstractAnalyser {

    @Override
    @Transactional(
            transactionManager = "transactionManager",
            propagation = Propagation.SUPPORTS,
            rollbackFor = Throwable.class)
    public void execute(String input) {
        if( Objects.isNull(input)) {
            throw new AnalyserException(getErrorNullMessage("CPF"),
                    HttpStatus.NOT_ACCEPTABLE);
        }
        Pattern er = Pattern.compile(RegexConstantes.REGEX_CPF);
        Matcher result = er.matcher(input);
        if (!result.matches()) {
            throw new AnalyserException(getErrorMessage(input, GenericConstantes.FORMAT_CPF),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if(!computeDigits(input.replaceAll("[.-]",""))) {
            throw new AnalyserException("CPF não é válido. Verifique o CPF enviado.", HttpStatus.BAD_REQUEST);
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
