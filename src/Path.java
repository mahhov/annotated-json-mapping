import java.lang.reflect.Field;

class Path {
    private String value;
    Segment[] segments;

    Path(String value) {
        // ~ (base path)
        String[] shortenValue = value.split("~");

        // / (traverse down path)
        String segmentStrs[] = shortenValue[shortenValue.length - 1].split("/");

        // ^ (traverse up path)
        Stack<Integer> nonBlanks = new Stack<>(segmentStrs.length);
        for (int i = 0; i < segmentStrs.length; i++)
            if (segmentStrs[i].equals("^")) {
                segmentStrs[i] = "";
                segmentStrs[nonBlanks.pop()] = "";
            } else if (!segmentStrs[i].isEmpty())
                nonBlanks.push(i);

        // . (array index) and convert to segments
        segments = new Segment[nonBlanks.size()];
        int array;
        boolean[] availableArrays = new boolean[nonBlanks.size()];
        int nextArray = 0;
        int count = 0;
        while (nonBlanks.size() > 0) {
            String[] valueArraySplit = value.split("\\.");
            this.value = valueArraySplit[0];
            if (valueArraySplit.length > 1)
                if (valueArraySplit[1].isEmpty()) {
                    while (!availableArrays[nextArray])
                        nextArray++;
                    array = nextArray;
                } else {
                    array = Integer.valueOf(valueArraySplit[1]);
                    availableArrays[array] = false;
                }
            else
                array = -1;
            segments[count++] = new Segment(segmentStrs[nonBlanks.popFront()], array);
        }

        // value
        StringBuilder valueBuilder = new StringBuilder(value.length());
        for (int i = 0; i < segments.length - 1; i++)
            valueBuilder.append(segments[i].value).append("/");
        if (segments.length > 0)
            valueBuilder.append(segments[segments.length - 1].value);
        this.value = valueBuilder.toString();
    }

    static Path createPath(Path basePath, Field field, boolean list) {
        JsonAnnotation annotation = (JsonAnnotation) field.getAnnotation(JsonAnnotation.class);

        Path path;
        if (annotation == null) {
            if (list)
                path = append(basePath, field.getName() + ".");
            else
                path = append(basePath, field.getName());
        } else {
            if (annotation.value().isEmpty())
                path = basePath;
            else if (isLeaf(annotation.value()))
                path = append(basePath, annotation.value());
            else
                path = append(basePath, annotation.value() + field.getName());

            if (annotation.debug())
                System.out.println("DEBUG " + field.getName() + " - " + path);
        }

        return path;
    }

    private static Path append(Path basePath, String value) {
        if (isLeaf(basePath.value))
            return new Path(basePath.value + "/" + value);
        else
            return new Path(basePath.value + value);
    }

    private static boolean isLeaf(String value) {
        return !value.isEmpty() && value.charAt(value.length() - 1) != '/';
    }

    public String toString() {
        return value;
    }

    static class Segment {
        String value;
        int array;

        private Segment(String value, int array) {
            this.value = value;
            this.array = array;
        }
    }
}