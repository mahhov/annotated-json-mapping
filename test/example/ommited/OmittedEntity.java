package example.ommited;

import mapper.JsonAnnotation;

public class OmittedEntity {
    @JsonAnnotation("tips/tip1")
    String tip1;

    @JsonAnnotation("tips/")
    String tip2;

    @JsonAnnotation("tip3")
    String tip3;
}