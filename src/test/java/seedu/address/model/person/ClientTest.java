package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class ClientTest {

    @Test
    public void hashCode_sameValues_sameHashCode() {
        Client client = new Client(new Name("Alice"), new Phone("81234567"),
                new Phone("91234567"), new Name("John"), new HashSet<>(), 2000, 300);
        Client copy = new Client(new Name("Alice"), new Phone("81234567"),
                new Phone("91234567"), new Name("John"), new HashSet<>(), 2000, 300);

        assertEquals(client.hashCode(), copy.hashCode());
    }
}
