package testdoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestdocSimple extends Testdoc {
    private String name;
    private String description;

    private String html;
    private String markdown;

    public TestdocSimple(Path basePath, String name) throws IOException {
        this.name = name;

        Path descriptionPath = basePath.resolve("description.txt");
        description = new String(Files.readAllBytes(descriptionPath));
    }

    public void execute() throws Throwable {
        composeDocumentation();
    }

    private void composeDocumentation() throws IOException {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append(htmlSection(name, description));
        html = htmlBuilder.toString();

        StringBuilder markdownBuilder = new StringBuilder();
        markdownBuilder.append(markdownTitle(name));
        markdownBuilder.append(markdownSection(null, description, null));
        markdown = markdownBuilder.toString();
    }

    public String getName() {
        return name;
    }

    public String getHtml() {
        return html;
    }

    public String getMarkdown() {
        return markdown;
    }
}