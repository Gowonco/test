package util;

public class TimeTranport {
    private int timeInt;
    private String year,month,day;
    public TimeTranport(String timeStr){

        year=timeStr.substring(0,4);
        month=timeStr.substring(5,7);
        day=timeStr.substring(8,10);
    }
   public  int output(){
        return timeInt=Integer.parseInt(month+day);
    }

}
