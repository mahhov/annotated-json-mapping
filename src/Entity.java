import java.util.ArrayList;

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

        @JsonAnnotation("^/")
        String zero;

        @JsonAnnotation("^/five")
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
        Integer w;
    }

    String trueStr;

    Boolean truee;

    Boolean falsee;

    boolean falsePrim;

    short twelvePrim;

    @JsonAnnotation("twelvePrim")
    char onee;

    char nine;

    // todo support list declarations (arraylist / linked list as defined in entity)
    ArrayList<String> list;

    @JsonAnnotation("list.")
    ArrayList listGeneric;

    @JsonAnnotation("list.")
    ArrayList<Integer> intList;

    @JsonAnnotation("listz.")
    ArrayList<Z> objList;

    static class Z {
        @JsonAnnotation("x")
        int x_50_52;
        @JsonAnnotation("y")
        int y_51_53;
        //        @JsonAnnotation("^/listz2.")
        //        int z_54_55;
        //        @JsonAnnotation("^/listz3./w")
        //        int w_56_57;
    }
}