import mapper.Mapper;
import printer.Printer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Main {
    private static final Path INPUT_JSON_PATH = Paths.get("input.json");
    private static final Class ENTITY = EntitySmall.class;

    public static void main(String[] arg) throws Exception {
        String jsonInput = new String(Files.readAllBytes(INPUT_JSON_PATH));
        
        Object mappedObject = Mapper.map(ENTITY, jsonInput);
        if (mappedObject != null)
            System.out.println(Printer.printObject(mappedObject));
        else
            System.out.println("null");
    }
}


// todo: conditional mappings
// todo: allow transposing raw nested lists

// todo : change git author