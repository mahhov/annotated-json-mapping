package testdoc;

import mapper.Mapper;
import org.junit.jupiter.api.function.Executable;
import printer.Printer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

// todo: make this generic to any input/output test
public class Testdoc implements Executable {
    private String jsonInput;
    private String entityAsText;
    private String expectedOutput;
    private String output;

    private Class entityClass;
    private String name;
    private String description;

    private Path expectedOutputPath;
    private boolean generatedExpectedOutput;

    private String html;
    private String markdown;

    public Testdoc(Path basePath, Class entityClass, String name, String description) throws IOException {
        Path jsonInputPath = basePath.resolve("input.json");
        jsonInput = new String(Files.readAllBytes(jsonInputPath));

        Path entityPath = basePath.resolve(entityClass.getSimpleName() + ".java");
        entityAsText = new String(Files.readAllBytes(entityPath));

        expectedOutputPath = basePath.resolve("expectedOutput.txt");
        expectedOutput = new String(Files.readAllBytes(expectedOutputPath));

        this.entityClass = entityClass;
        this.name = name;
        this.description = description;
    }

    public Testdoc(Path basePath, Class entityClass, String name, String description, boolean generatedExpectedOutput) throws IOException {
        this(basePath, entityClass, name, description);
        this.generatedExpectedOutput = generatedExpectedOutput;
    }

    public void execute() throws Throwable {
        Object mappedObject = Mapper.map(entityClass, jsonInput);

        if (mappedObject != null)
            output = Printer.printObject(mappedObject);
        else
            output = "null";

        if (generatedExpectedOutput)
            Files.write(expectedOutputPath, output.getBytes());
        else
            assertEquals(output, expectedOutput);

        composeDocumentation();
    }

    private void composeDocumentation() throws IOException {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append(htmlSection(name, description));
        htmlBuilder.append(htmlSection("Input", jsonInput));
        htmlBuilder.append(htmlSection("Entity", entityAsText));
        htmlBuilder.append(htmlSection("Output", output));
        html = htmlBuilder.toString();

        StringBuilder markdownBuilder = new StringBuilder();
        markdownBuilder.append(markdownTitle(name));
        markdownBuilder.append(markdownSection(null, description, null));
        markdownBuilder.append(markdownSection("Input", jsonInput, "json"));
        markdownBuilder.append(markdownSection("Entity", entityAsText, "java"));
        markdownBuilder.append(markdownSection("Output", output, "text"));
        markdown = markdownBuilder.toString();
    }

    private StringBuilder htmlSection(String header, String body) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<h1>").append(header).append("</h1>").append("\n");
        stringBuilder.append("<pre>").append(body).append("</pre>").append("\n");
        return stringBuilder;
    }

    private StringBuilder markdownTitle(String header) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("# ").append(header).append("\n\n");
        return stringBuilder;
    }

    private StringBuilder markdownSection(String header, String body, String language) {
        StringBuilder stringBuilder = new StringBuilder();
        if (header != null)
            stringBuilder.append("### ").append(header).append("\n\n");
        if (language != null)
            stringBuilder.append("```").append(language).append("\n").append(body).append("\n```\n\n");
        else
            stringBuilder.append(body).append("\n\n");
        return stringBuilder;
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