public class Path {
    private String value;
    String[] segments;

    Path(String value) {
        // ~
        String[] shortenValue = value.split("~");

        // /
        segments = shortenValue[shortenValue.length - 1].split("/");

        // ^
        Stack<Integer> nonBlanks = new Stack<>(segments.length);
        for (int i = 0; i < segments.length; i++) {
            if (segments[i].equals("^")) {
                segments[i] = "";
                segments[nonBlanks.pop()] = "";
            } else if (!segments[i].isEmpty())
                nonBlanks.push(i);
        }

        // value
        StringBuilder valueBuilder = new StringBuilder(value.length());
        for (int i = 0; i < segments.length - 1; i++)
            if (!segments[i].isEmpty())
                valueBuilder.append(segments[i]).append("/");
        valueBuilder.append(segments[segments.length - 1]);
        this.value = valueBuilder.toString();
    }

    static Path append(Path basePath, String value) {
        if (isLeaf(basePath.value))
            return new Path(basePath.value + "/" + value);
        else
            return new Path(basePath.value + value);
    }

    static boolean isLeaf(String value) {
        return !value.isEmpty() && value.charAt(value.length() - 1) != '/';
    }

    public String toString() {
        return value;
    }
}