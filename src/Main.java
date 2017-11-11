import java.nio.file.Files;
import java.nio.file.Paths;

class Main<T> {
    private static final String JSON_INPUT = "input.json";

    public static void main(String[] arg) throws Exception {
        Mapper mapper = new Mapper(Entity.class);

        String jsonInput = new String(Files.readAllBytes(Paths.get(JSON_INPUT)));

        Entity mappedJson = (Entity) mapper.map(jsonInput);
        mapper.print(mappedJson);
    }
}