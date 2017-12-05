package testdoc;

import org.junit.jupiter.api.function.Executable;

public abstract class Testdoc implements Executable {
    public abstract String getName();

    public abstract String getHtml();

    public abstract String getMarkdown();

    StringBuilder htmlSection(String header, String body) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<h1>").append(header).append("</h1>").append("\n");
        stringBuilder.append("<pre>").append(body).append("</pre>").append("\n");
        return stringBuilder;
    }

    StringBuilder markdownTitle(String header) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("# ").append(header).append("\n\n");
        return stringBuilder;
    }

    StringBuilder markdownSection(String header, String body, String language) {
        StringBuilder stringBuilder = new StringBuilder();
        if (header != null)
            stringBuilder.append("### ").append(header).append("\n\n");
        if (language != null)
            stringBuilder.append("```").append(language).append("\n").append(body).append("\n```\n\n");
        else
            stringBuilder.append(body).append("\n\n");
        return stringBuilder;
    }

}