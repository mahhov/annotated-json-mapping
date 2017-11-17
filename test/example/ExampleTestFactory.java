package example;

import example.ArrayTranspose.Entity;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.nio.file.Paths;
import java.util.Iterator;

public class ExampleTestFactory implements Iterator<DynamicTest> {
    private static final Testdoc[] testdocs = new Testdoc[] {
            new Testdoc(Paths.get("test/example/ArrayTranspose/"), Entity.class, "Array Transpose", "description")
    };
    private int testdocIndex;

    @TestFactory
    Iterator<DynamicTest> mapperPrinterTests() {
        return new ExampleTestFactory();
    }

    public boolean hasNext() {
        return testdocIndex < testdocs.length;
    }

    public DynamicTest next() {
        Testdoc testdoc = testdocs[testdocIndex++];
        return DynamicTest.dynamicTest(testdoc.name, testdoc);
    }
}
