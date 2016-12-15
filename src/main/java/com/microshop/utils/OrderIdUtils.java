package com.microshop.utils;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by:   Lijian
 * Created on:   2015/10/21
 * Descriptions:
 */
public class OrderIdUtils implements IdentifierGenerator {

    private String getDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyMMdd");
        df.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        return df.format(new Date());
    }

    private int getSeconds() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return hour * 3600 + minute * 60 + second;
    }

    private int getRandom() {
        return new Random().nextInt(10000);
    }

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        return String.format("%s%05d%04d", getDate(), getSeconds(), getRandom());
    }
}
