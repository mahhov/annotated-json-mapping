package example.ArrayTranspose;

import mapper.JsonAnnotation;

import java.util.ArrayList;

public class Entity {
    ArrayList<ArrayList<Integer>> nestedList;

    @JsonAnnotation("nestedList.1/innerList.0")
    ArrayList<ArrayList<Integer>> transposedList;
}
