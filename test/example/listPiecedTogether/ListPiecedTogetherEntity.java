package example.listPiecedTogether;

import mapper.JsonAnnotation;

import java.util.ArrayList;

public class ListPiecedTogetherEntity {
    @JsonAnnotation("parent/billData.0")
    ArrayList<Bill> bills;

    static class Bill {
        @JsonAnnotation("value")
        String amount;
        String currency;
        @JsonAnnotation("~/billPaidDates.0")
        String date;
    }
}