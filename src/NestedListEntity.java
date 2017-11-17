import mapper.JsonAnnotation;

import java.util.ArrayList;

class NestedListEntity {
    ArrayList<ArrayList<Integer>> nestedList;
    
    @JsonAnnotation("nestedList.0")
    ArrayList<ArrayList<Integer>> nestedListRename;

    @JsonAnnotation("nestedList.0.2")
    ArrayList<NestedList> nestedListExpanded;

    static class NestedList {
        @JsonAnnotation("")
        ArrayList<Integer> innerList;
    }
}