class Entity {
    @JsonAnnotation("three")
    String threeMap;

    X x;

    @JsonAnnotation("x/")
    String one;

    @JsonAnnotation("x/two")
    String two;

    String notFound;

    @JsonAnnotation()
    String blank;

    static class X {
        String two;

        @JsonAnnotation("../")
        String zero;

        @JsonAnnotation("../m")
        String fiveMap;
    }

    @JsonAnnotation()
    Y ten;

    @JsonAnnotation("tw")
    Y twenty;

    @JsonAnnotation("tw/tr")
    Y thirty;

    @JsonAnnotation("fr/")
    Y fourty;

    static class Y {
        String w;
    }
}