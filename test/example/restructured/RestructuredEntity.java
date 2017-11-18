package example.restructured;

import mapper.JsonAnnotation;

public class RestructuredEntity {
    @JsonAnnotation("unestMe/value")
    String outer;

    @JsonAnnotation("unestMe/middle")
    Nester middle;

    static class Nester {
        @JsonAnnotation("middleValue")
        String value;
    }

    @JsonAnnotation("")
    NesterOuter nesterOuter;

    static class NesterOuter {
        @JsonAnnotation("")
        NesterInner nesterInner;
    }

    static class NesterInner {
        @JsonAnnotation("nestMe")
        String value;
    }
}