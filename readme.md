# Renamed Field

description

### Input

```json
{
  "firstName": "Manuel",
  "lastName": "Reuter"
}
```

### Entity

```java
package example.renamed;

import mapper.JsonAnnotation;

public class RenamedEntity {
    String firstName;

    @JsonAnnotation("lastName")
    String familyName;
}
```

### Output

```text
  firstName : Manuel
  familyName: Reuter

```

# Nested List

description

### Input

```json
{
  "nestedList": [
    [
      11,
      12,
      13
    ],
    [
      21,
      22,
      23
    ]
  ]
}
```

### Entity

```java
package example.nestedlist;

import mapper.JsonAnnotation;

import java.util.ArrayList;

public class NestedListEntity {
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
```

### Output

```text
  nestedList: (size 2)
    0         : (size 3)
      11        
      12        
      13        
    1         : (size 3)
      21        
      22        
      23        
  nestedListRename: (size 2)
    0         : (size 3)
      11        
      12        
      13        
    1         : (size 3)
      21        
      22        
      23        
  nestedListExpanded: (size 2)
    0         
      innerList : (size 3)
        11        
        12        
        13        
    1         
      innerList : (size 3)
        21        
        22        
        23        

```

