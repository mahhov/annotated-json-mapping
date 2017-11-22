package mapper;

import utility.ArrayGrower;
import utility.Stack;
import utility.UnusedTracker;

import java.lang.reflect.Field;

class Path {
    static final Path EMPTY_PATH = new Path(null, "");

    private String value;
    Segment[] segments;
    private String[] flags;
    private String condition;

    // for construction
    private UnusedTracker consumedArrays;

    private Path(Path basePath, String append) {
        String[] appendSplit;
        int basePathSize = 0;

        // copy # (flags) from basePath
        if (basePath != null) {
            flags = basePath.flags;
            basePathSize = basePath.segments.length;
        } else
            flags = new String[0];

        // ~ (home path)
        if (append.contains("~")) {
            basePathSize = 0;
            appendSplit = append.split("~");
            append = appendSplit[appendSplit.length - 1];
        }

        // ? (condition)
        appendSplit = append.split("\\?");
        if (appendSplit.length > 1)
            condition = appendSplit[appendSplit.length - 1];
        else
            condition = "";
        append = appendSplit[0];

        // # (flags , delimited)
        appendSplit = append.split("#");
        if (appendSplit.length > 1)
            flags = ArrayGrower.concatS(flags, appendSplit[appendSplit.length - 1].split(","));
        append = appendSplit[0];

        // / (traverse down path)
        String segmentStrs[] = append.split("/");

        // ^ (traverse up path)
        Stack<Integer> nonBlanks = new Stack<>(segmentStrs.length);
        for (int i = 0; i < segmentStrs.length; i++)
            if (segmentStrs[i].equals("^")) {
                if (nonBlanks.size() > 0)
                    nonBlanks.pop();
                else if (--basePathSize < 0)
                    throw new RuntimeException("Path construction invalid, too many ^'s");
            } else if (!segmentStrs[i].isEmpty())
                nonBlanks.push(i);

        // . (array index) and convert to segments
        segments = new Segment[basePathSize + nonBlanks.size()];
        if (basePathSize == 0)
            consumedArrays = new UnusedTracker();
        else {
            consumedArrays = new UnusedTracker(basePath.consumedArrays);
            for (int i = 0; i < basePathSize; i++)
                segments[i] = new Segment(basePath.segments[i]);
        }
        int count = basePathSize; // todo : rename and merge to 1 variable
        while (nonBlanks.size() > 0) {
            String segmentStr = segmentStrs[nonBlanks.popFront()];
            if (segmentStr.contains(".")) {
                String[] segmentArraySplit = segmentStr.split("\\.");
                String segmentValue = segmentArraySplit[0];
                int segmentArray = segmentArraySplit.length > 1 ? Integer.valueOf(segmentArraySplit[1]) : consumedArrays.nextUnused();
                int segmentArrayLayers = segmentArraySplit.length > 2 ? Integer.valueOf(segmentArraySplit[2]) : 1;
                consumedArrays.setUsed(segmentArray);
                segments[count++] = new Segment(segmentValue, segmentArray, segmentArrayLayers);
            } else
                segments[count++] = new Segment(segmentStr);
        }
    }

    static Path createPath(Path basePath, Field field, boolean list) {
        JsonAnnotation annotation = (JsonAnnotation) field.getAnnotation(JsonAnnotation.class);

        Path path;
        if (annotation == null) {
            if (list)
                path = new Path(basePath, field.getName() + ".");
            else
                path = new Path(basePath, field.getName());
        } else {
            if (annotation.value().isEmpty())
                path = basePath;
            else if (isLeaf(annotation.value()))
                path = new Path(basePath, annotation.value());
            else
                path = new Path(basePath, annotation.value() + field.getName());

            if (annotation.debug())
                System.out.println("DEBUG " + field.getName() + " - " + path);
        }

        return path;
    }

    static Path nestList(Path basePath) {
        Path nestedPath = new Path(basePath, "");
        nestedPath.segments[nestedPath.segments.length - 1].arrayLayers++;
        return nestedPath;
    }

    boolean isConditionMet() {
        if (condition.isEmpty())
            return true;
        for (String flag : flags)
            if (flag.equals(condition))
                return true;
        return false;
    }

    private static boolean isLeaf(String value) {
        return !value.isEmpty() && value.charAt(value.length() - 1) != '/';
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < segments.length - 1; i++)
            stringBuilder.append(segments[i]).append("/");
        if (segments.length > 0)
            stringBuilder.append(segments[segments.length - 1]);

        if (flags.length > 0) {
            stringBuilder.append("#");
            for (String flag : flags)
                stringBuilder.append(flag).append(",");
        }

        if (!condition.isEmpty())
            stringBuilder.append("?").append(condition);

        return stringBuilder.toString();
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

        private Segment(Segment baseSegment) {
            this.value = baseSegment.value;
            this.array = baseSegment.array;
            this.arrayLayers = baseSegment.arrayLayers;
        }

        public String toString() {
            StringBuilder segment = new StringBuilder();
            segment.append(value);
            if (array != -1)
                segment.append(".").append(array).append(".").append(arrayLayers);
            return segment.toString();
        }
    }
}