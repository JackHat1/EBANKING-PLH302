package com.bank.utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GlobalClock {
    private static LocalDateTime dateTime = LocalDateTime.now();


    public static LocalDate getDate(){
        return dateTime.toLocalDate();
    }

    public static void setDate(LocalDate newDate){
        dateTime= LocalDateTime.of(newDate, dateTime.toLocalTime());
    }


    public static LocalDateTime getDateTime(){
        return dateTime;
    }

    public static void setDateTime(LocalDateTime newDateTime){
        dateTime= newDateTime;
    }


    public static void addTime(int hours, int minutes, int seconds){
        dateTime= dateTime.plusHours(hours);
        dateTime= dateTime.plusMinutes(minutes);
        dateTime= dateTime.plusSeconds(seconds);
    }

    public static void addDate(int years, int months, int days){
        dateTime= dateTime.plusYears(years);
        dateTime= dateTime.plusMonths(months);
        dateTime= dateTime.plusDays(days);
    }






}
