package example.ignored;

import mapper.JsonAnnotation;

public class IgnoredEntity {

    @JsonAnnotation("")
    String ignoredField;

    @JsonAnnotation("")
    Tuple tuple;
    
    static class Tuple {
       String key;
       String value;
    }
}