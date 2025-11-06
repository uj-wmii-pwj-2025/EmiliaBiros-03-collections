package uj.wmii.pwj.collections;

import java.io.InputStream;
import java.io.PrintStream;

public interface Brainfuck {

    void execute();

    static Brainfuck createInstance(String program) {
        return createInstance(program, System.out, System.in, 1024);
    }

    static Brainfuck createInstance(String program, PrintStream out, InputStream in, int stackSize) {
        if (program == null || program.isEmpty() || out == null || in == null || stackSize < 1) {
            throw new IllegalArgumentException("Invalid arguments for interpreter initialization.");
        }
        return new Interpreter(program, out, in, stackSize);
    }
}