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
        boolean[] consumedArrays = new boolean[nonBlanks.size()];
        int nextArray = 0;
        int count = 0;
        while (nonBlanks.size() > 0) {
            String segmentStr = segmentStrs[nonBlanks.popFront()];
            if (segmentStr.contains(".")) {
                String[] segmentArraySplit = segmentStr.split("\\.");
                int array;
                if (segmentArraySplit.length > 1) {
                    array = Integer.valueOf(segmentArraySplit[1]);
                    consumedArrays[array] = true;
                } else {
                    while (consumedArrays[nextArray])
                        nextArray++;
                    array = nextArray;
                }
                segments[count++] = new Segment(segmentArraySplit[0], array);
            } else
                segments[count++] = new Segment(segmentStr, -1);
        }

        // value
        StringBuilder valueBuilder = new StringBuilder(value.length());
        for (int i = 0; i < segments.length - 1; i++)
            valueBuilder.append(segments[i]).append("/");
        if (segments.length > 0)
            valueBuilder.append(segments[segments.length - 1]);
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

        public String toString() {
            if (array == -1)
                return value;
            return value + "." + array;
        }
    }
}