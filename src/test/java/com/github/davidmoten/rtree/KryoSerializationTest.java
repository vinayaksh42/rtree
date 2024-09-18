package com.github.davidmoten.rtree;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoSerializationTest {

    public static class MyClass {
        private int x;
        private String y;

        public MyClass() {
        }

        public MyClass(int x, String y) {
            this.x = x;
            this.y = y;
        }

        // Getters and setters
        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }

        // Override equals for assertion
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof MyClass))
                return false;
            MyClass other = (MyClass) obj;
            return x == other.x && (y == null ? other.y == null : y.equals(other.y));
        }
    }

    @Test
    public void testSerializationWithoutRegistration() {
        Kryo kryo = new Kryo();
        // Do not set kryo.setRegistrationRequired(false); // Use default settings

        MyClass originalObject = new MyClass(42, "Hello, Kryo!");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);

        // Attempt to serialize without registering MyClass
        try {
            kryo.writeObject(output, originalObject);
            output.close();

            byte[] serializedData = outputStream.toByteArray();

            Input input = new Input(new ByteArrayInputStream(serializedData));
            MyClass deserializedObject = kryo.readObject(input, MyClass.class);
            input.close();

            // Verify that the deserialized object matches the original
            assertEquals(originalObject, deserializedObject);
            System.out.println("Serialization and deserialization succeeded without class registration.");
        } catch (KryoException e) {
            // If an exception is thrown, it indicates a change in behavior
            System.err.println("Serialization failed: " + e.getMessage());
            fail("Serialization failed due to class registration requirement.");
        }
    }
}
