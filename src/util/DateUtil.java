package util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

    public static List<String> getBetweenDates(String startDate, String endDate) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date start=sdf.parse(startDate);
        Date end=sdf.parse(endDate);
        List<String> listDate = new ArrayList<String>();

        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);

        while(start.getTime()<=end.getTime()){
            Date d=tempStart.getTime();
            listDate.add(sdf.format(d));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
            start = tempStart.getTime();
        }
        return listDate;
    }

}
