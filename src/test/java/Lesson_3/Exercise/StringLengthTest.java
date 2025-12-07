package Lesson_3.Exercise;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringLengthTest {

    @Test
    public void testStringLengthGreaterThan15() {
        String hello = "Hello, world";
        assertTrue(hello.length() > 15, "Строка должна быть длиннее 15 символов");
    }
}
