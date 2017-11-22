package example.conditional;

import mapper.JsonAnnotation;

public class ConditionalEntity {
    @JsonAnnotation(value = "flags#flag1", debug = true)
    String flags;

    @JsonAnnotation("conditions#flag2?flag2")
    String conditions;

    @JsonAnnotation("#flag3")
    InheritedFlagExample inheritedFlagExample;

    static class InheritedFlagExample {
        @JsonAnnotation("flagsInheriting?flag3")
        String flagsInheriting;
    }
}