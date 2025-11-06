package uj.wmii.pwj.collections;

import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;

class Interpreter implements Brainfuck {

    private final String program;
    private final PrintStream out;
    private final InputStream in;
    private final byte[] memory;
    private int pointer;

    public Interpreter(String program, PrintStream out, InputStream in, int stackSize) {
        this.program = program;
        this.out = out;
        this.in = in;
        this.memory = new byte[stackSize];
        this.pointer = 0;
    }

    @Override
    public void execute() {
        int programPointer = 0;

        while (programPointer < program.length()) {
            char instruction = program.charAt(programPointer);

            switch (instruction) {
                case '>': pointer++; checkOverflow(); break;
                case '<': pointer--; checkUnderflow(); break;
                case '+': memory[pointer]++; break;
                case '-': memory[pointer]--; break;
                case '.': out.write(memory[pointer]); break;
                case ',': readInput(); break;
                case '[':
                    if (memory[pointer] == 0) {
                        programPointer = findClosingBracket(programPointer);
                    }
                    break;
                case ']':
                    if (memory[pointer] != 0) {
                        programPointer = findOpeningBracket(programPointer);
                    }
                    break;
                default: break;
            }
            programPointer++;
        }
    }

    private void checkOverflow() {
        if (pointer >= memory.length) {
            throw new IllegalStateException("Data pointer overflow.");
        }
    }

    private void checkUnderflow() {
        if (pointer < 0) {
            throw new IllegalStateException("Data pointer underflow.");
        }
    }

    private void readInput() {
        try {
            int readByte = in.read();
            if (readByte != -1) {
                memory[pointer] = (byte) readByte;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading input", e);
        }
    }

    private int findClosingBracket(int startPointer) {
        int balance = 1;
        int pos = startPointer + 1;

        while (pos < program.length()) {
            char c = program.charAt(pos);
            if (c == '[') balance++;
            else if (c == ']') balance--;
            if (balance == 0) return pos;
            pos++;
        }

        throw new IllegalStateException("Unmatched '[' at index: " + startPointer);
    }

    private int findOpeningBracket(int startPointer) {
        int balance = 1;
        int pos = startPointer - 1;

        while (pos >= 0) {
            char c = program.charAt(pos);
            if (c == ']') balance++;
            else if (c == '[') balance--;
            if (balance == 0) return pos;
            pos--;
        }

        throw new IllegalStateException("Unmatched ']' at index: " + startPointer);
    }
}