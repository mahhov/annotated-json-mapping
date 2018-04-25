package testdoc;

import mapper.Mapper;
import printer.Printer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestdocExample extends Testdoc {
    private String jsonInput;
    private String entityAsText;
    private String expectedOutput;
    private String output;

    private Class entityClass;
    private String name;
    private String description;

    private static boolean GENERATE_EXPECTED_OUTPUT;
    private boolean generateExpectedOutput;
    private Path expectedOutputPath;

    private String html;
    private String markdown;

    public TestdocExample(Path basePath, Class entityClass, String name) throws IOException {
        Path jsonInputPath = basePath.resolve("input.json");
        jsonInput = new String(Files.readAllBytes(jsonInputPath));

        Path entityPath = basePath.resolve(entityClass.getSimpleName() + ".java");
        entityAsText = new String(Files.readAllBytes(entityPath));

        expectedOutputPath = basePath.resolve("expectedOutput.txt");
        expectedOutput = new String(Files.readAllBytes(expectedOutputPath));

        this.entityClass = entityClass;
        this.name = name;

        Path descriptionPath = basePath.resolve("description.txt");
        description = new String(Files.readAllBytes(descriptionPath));
    }

    public TestdocExample(Path basePath, Class entityClass, String name, boolean generateExpectedOutput) throws IOException {
        this(basePath, entityClass, name);
        this.generateExpectedOutput = generateExpectedOutput;
    }

    public void execute() throws Throwable {
        Object mappedObject = Mapper.map(entityClass, jsonInput);

        if (mappedObject != null)
            output = Printer.printObject(mappedObject);
        else
            output = "null";

        if (GENERATE_EXPECTED_OUTPUT || generateExpectedOutput)
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

    public String getName() {
        return name;
    }

    public String getHtml() {
        return html;
    }

    public String getMarkdown() {
        return markdown;
    }

    public static void enableGlobalGenerateExpectedOutput() {
        GENERATE_EXPECTED_OUTPUT = true;
    }
}