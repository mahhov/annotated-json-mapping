import mapper.Mapper;
import printer.Printer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Main {
    private static final Path INPUT_JSON_PATH = Paths.get("test/example/nestedlist/input.json");

    public static void main(String[] arg) throws Exception {
        String jsonInput = new String(Files.readAllBytes(INPUT_JSON_PATH));

        Object mappedObject = Mapper.map(NestedListEntity.class, jsonInput);
        if (mappedObject != null)
            System.out.println(Printer.printObject(mappedObject));
    }
}


// todo: conditional mappings
// todo: compose testdocs to readme/documentation
// todo: allow transposing raw nested lists