package br.com.jcv.commons.library.utility;

import br.com.jcv.commons.library.commodities.annotation.RegexValidation;
import br.com.jcv.commons.library.commodities.exception.InvalidRegexException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegexValidator {

    private RegexValidator() {}

    public static <T> Boolean execute(Class<T> inspectClass, String field, String content) {
        try {
            RegexValidation exp = inspectClass.getDeclaredField(field).getAnnotation(RegexValidation.class);
            if(execute(exp.regex(), content)) {
                return true;
            } else {
                throw new InvalidRegexException("Content " + content + " does not matches with annotation regex " + exp.regex(),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (NoSuchFieldException e) {
            throw new InvalidRegexException("Field " + field + " does not exist ",
                    HttpStatus.BAD_REQUEST);
        }
    }

    private static Boolean execute(String regex, String content) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        return matcher.matches();
    }
}
