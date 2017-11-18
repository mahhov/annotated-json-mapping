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

# Restructured Object

description

### Input

```json
{
  "nestMe": "i was originally unnested, but am afraid jsonAnnotation will hide me deep inside",
  "unestMe": {
    "value": "i was originally nested, but am confident jsonAnnotation will let me out",
    "middle": {
      "middleValue": "put me in the middle"
    }
  }
}
```

### Entity

```java
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
```

### Output

```text
  outer     : i was originally nested, but am confident jsonAnnotation will let me out
  middle    
    value     : put me in the middle
  nesterOuter
    nesterInner
      value     : i was originally unnested, but am afraid jsonAnnotation will hide me deep inside

```

# Traversing Paths

description

### Input

```json
{
  "zero": 0,
  "one": 1,
  "parent": {
    "two": 2,
    "three": 3,
    "root": {
      "four": 4,
      "five": 5,
      "child": {
        "five": 5,
        "six": 6
      }
    }
  }
}
```

### Entity

```java
package example.traversal;

import mapper.JsonAnnotation;

public class TraversalEntity {

    @JsonAnnotation("parent/root")
    Numbers numbers;

    static class Numbers {
        @JsonAnnotation("~/")
        String zero;
        @JsonAnnotation("^/^/")
        String one;
        @JsonAnnotation("~/parent/")
        String two;
        @JsonAnnotation("^/")
        String three;
        String four;
        @JsonAnnotation("child/five")
        String fiveRenamed;
        @JsonAnnotation("child/")
        String six;
    }
}
```

### Output

```text
  numbers   
    zero      : 0
    one       : 1
    two       : 2
    three     : 3
    four      : 4
    fiveRenamed: 5
    six       : 6

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

