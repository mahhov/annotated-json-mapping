import java.util.ArrayList;

public class NestedListEntity {
    ArrayList<NestedList> nestedList;

    static class NestedList {
        ArrayList<Integer> innerList;
    }

    @JsonAnnotation("")
    ArrayList<TransposedNestedList> transposedNestedList;

    static class TransposedNestedList {
        @JsonAnnotation("nestedList.1/innerList.0")
        ArrayList<Integer> innerList;
    }
}