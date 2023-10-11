package com.jwick.continental.deathagreement.service.impl;

import com.jwick.continental.deathagreement.service.DateTime;
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
