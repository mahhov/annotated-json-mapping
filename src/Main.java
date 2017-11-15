import java.nio.file.Files;
import java.nio.file.Paths;

class Main {
    private static final String JSON_INPUT = "input.json";

    public static void main(String[] arg) throws Exception {
        String jsonInput = new String(Files.readAllBytes(Paths.get(JSON_INPUT)));

        NestedListEntity mappedJson = (NestedListEntity) Mapper.map(NestedListEntity.class, jsonInput);
        Printer.printObject(mappedJson);
    }
}


// todo: nested arrays
// todo: conditional mappings