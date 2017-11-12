import java.nio.file.Files;
import java.nio.file.Paths;

class Main<T> {
    private static final String JSON_INPUT = "input.json";

    public static void main(String[] arg) throws Exception {
        String jsonInput = new String(Files.readAllBytes(Paths.get(JSON_INPUT)));

        Entity mappedJson = (Entity) Mapper.map(Entity.class, jsonInput);
        Mapper.printObject(mappedJson);
    }
}


// todo: arrays
// todo: conditional mappings
// todo: primitives