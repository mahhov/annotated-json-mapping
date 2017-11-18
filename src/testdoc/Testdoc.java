package testdoc;

import mapper.Mapper;
import org.junit.jupiter.api.function.Executable;
import printer.Printer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

// todo: make this generic to any input/output test
public class Testdoc implements Executable {
    private Path jsonInputPath;
    private Path entityPath;
    private Class entityClass;
    private String name;
    private String description;
    private String jsonInput;
    private String entityAsText;
    private String output;
    private String html;
    private String markdown;

    public Testdoc(Path basePath, Class entityClass, String name, String description) {
        this.jsonInputPath = basePath.resolve("input.json");
        this.entityPath = basePath.resolve(entityClass.getSimpleName() + ".java");
        this.entityClass = entityClass;
        this.name = name;
        this.description = description;
    }

    public void execute() throws Throwable {
        jsonInput = new String(Files.readAllBytes(jsonInputPath));
        entityAsText = new String(Files.readAllBytes(entityPath));
        Object mappedObject = Mapper.map(entityClass, jsonInput);

        if (mappedObject != null)
            output = Printer.printObject(mappedObject);
        else
            output = "null";

        // todo: do some expect between printed and expectedPrinted or mappedObject and expectedMappedObject

        // createTestdocDir();
        composeDocumentation();
    }

    private void createTestdocDir() throws IOException {
        Path root = Paths.get("testdoc/" + name + "/");
        Files.createDirectories(root);

        copyFile(jsonInputPath, "jsonInput.json");
        copyFile(entityPath, "entity.java");
        writeFile("output.txt", output);
        writeFile("description.txt", description);
        writeFile("testdoc.html", html);
        writeFile("testdoc.md", markdown);
    }

    private void copyFile(Path srcFileName, String dstFileName) throws IOException {
        Files.copy(srcFileName, getTestdocPath(dstFileName), StandardCopyOption.REPLACE_EXISTING);
    }

    private void writeFile(String fileName, String content) throws IOException {
        Files.write(getTestdocPath(fileName), content.getBytes());
    }

    private Path getTestdocPath(String fileName) {
        return Paths.get("testdoc/" + name + "/" + fileName);
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