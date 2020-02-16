package date;

import org.junit.jupiter.api.Test;

import java.time.*;

public class PeriodDemo {

    @Test
    public void caculate(){
        final LocalDate from = LocalDate.of( 1999, Month.AUGUST, 21);
        final LocalDate to = LocalDate.of( 2015, Month.APRIL, 16);
        Period period = Period.between(from, to);
        System.out.println( "Period in days: " + period.getDays() );
        System.out.println( "Period in months: " + period.getMonths());
        System.out.println( "Period in years: " + period.getYears());
    }
}
