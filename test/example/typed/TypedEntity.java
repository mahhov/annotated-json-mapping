package example.typed;

import mapper.JsonAnnotation;

public class TypedEntity {
    @JsonAnnotation("number")
    int integer;

    @JsonAnnotation("number")
    Integer integerWrapped;

    @JsonAnnotation("numericString")
    int integerFromString;
    
    @JsonAnnotation("number")
    byte bite;
    
    String string;
    
    @JsonAnnotation("number")
    String stringFromInteger;

    @JsonAnnotation("boolean")
    String stringFromBoolean;

    @JsonAnnotation("boolean")
    Boolean booleen;

    @JsonAnnotation("booleanString")
    Boolean booleanFromString;
}