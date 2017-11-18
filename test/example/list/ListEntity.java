package example.list;

import mapper.JsonAnnotation;

import java.util.ArrayList;

public class ListEntity {
    ArrayList<Integer> list;

    @JsonAnnotation("list.0")
    ArrayList<Integer> listWithAnnotation;

    ArrayList<Value> listObjs;
    
    @JsonAnnotation("listObjs.0")
    ArrayList<Value> listObjsAnnotated;

    static class Value {
        int value;
    }
}