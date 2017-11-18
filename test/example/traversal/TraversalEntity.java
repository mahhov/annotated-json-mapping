package example.traversal;

import mapper.JsonAnnotation;

public class TraversalEntity {

    @JsonAnnotation("parent/root")
    Numbers numbers;

    static class Numbers {
        @JsonAnnotation("~/")
        String zero;
        @JsonAnnotation("^/^/")
        String one;
        @JsonAnnotation("~/parent/")
        String two;
        @JsonAnnotation("^/")
        String three;
        String four;
        @JsonAnnotation("child/five")
        String fivee;
        @JsonAnnotation("child/")
        String six;
    }
}