import java.lang.reflect.Field;

class Path {
    static final Path EMPTY_PATH = new Path("");

    private String value;
    Segment[] segments;

    private Path(String value) {
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
        UnusedTracker consumedArrays = new UnusedTracker();
        int count = 0;
        while (nonBlanks.size() > 0) {
            String segmentStr = segmentStrs[nonBlanks.popFront()];
            if (segmentStr.contains(".")) {
                String[] segmentArraySplit = segmentStr.split("\\.");
                String segmentValue = segmentArraySplit[0];
                int segmentArray = segmentArraySplit.length > 1 ? Integer.valueOf(segmentArraySplit[1]) : consumedArrays.getUnused();
                int segmentArrayLayers = segmentArraySplit.length > 2 ? Integer.valueOf(segmentArraySplit[2]) : 1;
                consumedArrays.setUsed(segmentArray);
                segments[count++] = new Segment(segmentValue, segmentArray, segmentArrayLayers);
            } else
                segments[count++] = new Segment(segmentStr);
        }

        // value
        createValue();
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

    static Path nestList(Path basePath) {
        Path nestedPath = new Path(basePath.value);
        nestedPath.segments[nestedPath.segments.length - 1].arrayLayers++;
        nestedPath.createValue();
        return nestedPath;
    }

    private static boolean isLeaf(String value) {
        return !value.isEmpty() && value.charAt(value.length() - 1) != '/';
    }

    private void createValue() {
        StringBuilder valueBuilder = new StringBuilder();
        for (int i = 0; i < segments.length - 1; i++)
            valueBuilder.append(segments[i]).append("/");
        if (segments.length > 0)
            valueBuilder.append(segments[segments.length - 1]);
        value = valueBuilder.toString();
    }

    public String toString() {
        return value;
    }

    static class Segment {
        String value;
        int array;
        int arrayLayers;

        private Segment(String value) {
            this.value = value;
            this.array = -1;
        }

        private Segment(String value, int array, int arrayLayers) {
            this.value = value;
            this.array = array;
            this.arrayLayers = arrayLayers;
        }

        public String toString() {
            if (array == -1)
                return value;
            return value + "." + array + "." + arrayLayers;
        }
    }
}