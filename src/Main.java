import mapper.Mapper;
import printer.Printer;

import java.nio.file.Files;
import java.nio.file.Paths;

class Main {
    private static final String JSON_INPUT = "input.json";

    public static void main(String[] arg) throws Exception {
        String jsonInput = new String(Files.readAllBytes(Paths.get(JSON_INPUT)));

        NestedListEntity mappedObject = (NestedListEntity) Mapper.map(NestedListEntity.class, jsonInput);
        if (mappedObject != null)
            Printer.printObject(mappedObject);
    }
}


// todo: conditional mappings
// todo: test and generated docs