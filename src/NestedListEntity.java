import java.util.ArrayList;

public class NestedListEntity {
    ArrayList<NestedList> nestedList;

    static class NestedList {
        ArrayList<Integer> innerList;
    }
}
