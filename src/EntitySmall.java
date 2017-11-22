import mapper.JsonAnnotation;

class EntitySmall {
    @JsonAnnotation(value = "x#3,2", debug = true)
    X x;

    static class X {
        @JsonAnnotation(value = "one?3", debug = true)
        String one;
        @JsonAnnotation(value = "two?2", debug = true)
        String two;
    }
}