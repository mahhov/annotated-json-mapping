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

    Testdoc(Path basePath, Class entityClass, String name, String description) {
        this.jsonInputPath = basePath.resolve("input.json");
        this.entityPath = basePath.resolve("entity.java");
        this.entityClass = entityClass;
        this.name = name;
        this.description = description;
    }

    public void execute() throws Throwable {
        String jsonInput = new String(Files.readAllBytes(jsonInputPath));
        Object mappedObject = Mapper.map(entityClass, jsonInput);

        String printed;
        if (mappedObject != null)
            printed = Printer.printObject(mappedObject);
        else
            printed = "null";

        // todo: do some expect between printed and expectedPrinted or mappedObject and expectedMappedObject
        
        createTestdocDir(printed);
    }

    private void createTestdocDir(String output) throws IOException {
        Path root = Paths.get("testdoc/" + name + "/");
        Files.createDirectories(root);

        copyFile(jsonInputPath, "jsonInput");
        copyFile(entityPath, "entity");
        writeFile("output", output);
        writeFile("description", description);
    }

    private void copyFile(Path srcFileName, String dstFileName) throws IOException {
        Files.copy(srcFileName, getTestdocPath(dstFileName), StandardCopyOption.REPLACE_EXISTING);
    }

    private void writeFile(String fileName, String content) throws IOException {
        Files.write(getTestdocPath(fileName), content.getBytes());
    }

    private Path getTestdocPath(String fileName) {
        return Paths.get("testdoc/" + name + "/" + fileName + ".testdoc");
    }
}