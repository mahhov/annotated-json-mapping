package example.conditional;

import mapper.JsonAnnotation;

public class ConditionalEntity {
    @JsonAnnotation("flags#flag1")
    String flags;

    @JsonAnnotation("conditions#flag2?flag2")
    String conditions;

    @JsonAnnotation("#flag3")
    InheritedFlagExample inheritedFlagExample;

    static class InheritedFlagExample {
        @JsonAnnotation("flagsInheriting?flag3")
        String flagsInheriting;
    }

    @JsonAnnotation("amountDue#next")
    Payment nextPayment;
    
    @JsonAnnotation("#last")
    Payment lastPayment;
    
    static class Payment {
        @JsonAnnotation("value?next")
        @JsonAnnotation("lastAmountPaid?last")
        String value;
        @JsonAnnotation("currency?next")
        @JsonAnnotation("lastPaymentCurrency?last")
        String currency;
    }
}