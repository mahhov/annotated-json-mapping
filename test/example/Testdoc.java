package example;

import mapper.Mapper;
import org.junit.jupiter.api.function.Executable;
import printer.Printer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

class Testdoc implements Executable {
    private Path jsonInputPath;
    private Path entityPath;
    private Class entityClass;
    String name;
    private String description;
    private String jsonInput;
    private String entityAsText;
    private String output;

    Testdoc(Path basePath, Class entityClass, String name, String description) {
        this.jsonInputPath = basePath.resolve("input.json");
        this.entityPath = basePath.resolve("entity.java");
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

        createTestdocDir();
        toHtml();
    }

    private void createTestdocDir() throws IOException {
        Path root = Paths.get("testdoc/" + name + "/");
        Files.createDirectories(root);

        copyFile(jsonInputPath, "jsonInput.json");
        copyFile(entityPath, "entity.java");
        writeFile("output.txt", output);
        writeFile("description.txt", description);
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

    private void toHtml() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(htmlSection(name, description));
        stringBuilder.append(htmlSection("Input", jsonInput));
        stringBuilder.append(htmlSection("Entity", entityAsText));
        stringBuilder.append(htmlSection("Output", output));

        writeFile("testdoc.html", stringBuilder.toString());
    }

    private StringBuilder htmlSection(String header, String body) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<h1>").append(header).append("</h1>");
        stringBuilder.append("<pre>").append(body).append("</pre>");
        return stringBuilder;
    }
}