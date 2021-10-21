package com.lazday.todo.util;


import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateUtil {

    private final static String FORMAT_DATE = "dd/MM/yyyy";
    private final static String FORMAT_TIME = "dd/MM/yyyy hh:mm:ss";

    public static DatePickerDialog showDialog(Context context, DatePickerDialog.OnDateSetListener datePicker){
        Calendar calendar = Calendar.getInstance();
        return new DatePickerDialog(
                context,
                datePicker,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
    }

    public static String toString(int year, int monthOfYear, int dayOfMonth){
        return dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
    }

    public static String toString(Long date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( date );
        return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get( Calendar.MONTH ) + 1) + "/" + calendar.get(Calendar.YEAR);
    }

    public static Long toLong(String date){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_TIME, Locale.getDefault());
            return dateFormat.parse( date + " 00:00:00" ).getTime();
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static String today(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( System.currentTimeMillis() );
        return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get( Calendar.MONTH ) + 1) + "/" + calendar.get(Calendar.YEAR);
    }

    public static String nextWeek(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( System.currentTimeMillis() );
        return ( calendar.get(Calendar.DAY_OF_MONTH) + 7 ) + "/" + (calendar.get( Calendar.MONTH ) + 1) + "/" + calendar.get(Calendar.YEAR);
    }

//    private final static String FORMAT_CUSTOM = "dd MMM yyyy";
//    public static String custom(String date){
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE, Locale.getDefault());
//            Date dateParse = dateFormat.parse( date );
//            SimpleDateFormat dateResult = new SimpleDateFormat( FORMAT_CUSTOM, Locale.getDefault());
//            return dateResult.format( dateParse );
//        } catch (ParseException exception) {
//            exception.printStackTrace();
//        }
//        return "";
//    }

//    private static String monthToText(Integer intMonth){
//        String stringMonth = "";
//        switch (intMonth) {
//            case 1: stringMonth = "Jan";
//                break;
//            case 2: stringMonth = "Feb";
//                break;
//            case 3: stringMonth = "Mar";
//                break;
//            case 4: stringMonth = "Apr";
//                break;
//            case 5: stringMonth = "Mei";
//                break;
//            case 6: stringMonth = "Jun";
//                break;
//            case 7: stringMonth = "Jul";
//                break;
//            case 8: stringMonth = "Agu";
//                break;
//            case 9: stringMonth = "Sep";
//                break;
//            case 10: stringMonth = "Okt";
//                break;
//            case 11: stringMonth = "Nov";
//                break;
//            case 12: stringMonth = "Des";
//                break;
//        }
//        return stringMonth;
//    }
//
//    public static String monthToNumber(String date){
//        String newDate = "";
//        for (MonthModel model: listMonth()) {
//            if (date.contains( model.getName() )) {
//                newDate = date.replace(model.getName(), model.getNumber().toString());
//            }
//        }
//        return newDate;
//    }
//
//    private static List<MonthModel> listMonth(){
//        List<MonthModel> listMonth = new ArrayList<>();
//        listMonth.add( new MonthModel(1, "Jan") );
//        listMonth.add( new MonthModel(2, "Feb") );
//        listMonth.add( new MonthModel(3, "Mar") );
//        listMonth.add( new MonthModel(4, "Apr") );
//        listMonth.add( new MonthModel(5, "Mei") );
//        listMonth.add( new MonthModel(6, "Jun") );
//        listMonth.add( new MonthModel(7, "Jul") );
//        listMonth.add( new MonthModel(8, "Agu") );
//        listMonth.add( new MonthModel(9, "Sep") );
//        listMonth.add( new MonthModel(10, "Okt") );
//        listMonth.add( new MonthModel(11, "Nov") );
//        listMonth.add( new MonthModel(12, "Des") );
//
//        return listMonth;
//    }

}
