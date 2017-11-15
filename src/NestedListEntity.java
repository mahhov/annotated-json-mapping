import java.util.ArrayList;

class NestedListEntity {
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

    ArrayList<ArrayList<Integer>> nestedListRaw;
}