package br.com.jcv.commons.library.utility;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DateTimeImpl implements DateTime {
    @Override
    public Date getToday() {
        return new Date();
    }

    @Override
    public Date now() {
        return getToday();
    }
}
