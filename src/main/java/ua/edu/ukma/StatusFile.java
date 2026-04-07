package ua.edu.ukma;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

public class StatusFile {

    public static void createFile(Path file, int n) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file.toFile(), "rw")) {
            raf.setLength(n);
        }
    }

    public static void updateStatus(Path file, int index, byte status) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file.toFile(), "rw");
             FileChannel channel = raf.getChannel()) {
            if (index < 0 || index >= channel.size()) throw new IllegalArgumentException("Invalid index: " + index);
            channel.position(index);
            ByteBuffer buffer = ByteBuffer.wrap(new byte[]{status});
            channel.write(buffer);
        }
    }

    public static byte readStatus(Path file, int index) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file.toFile(), "r"); FileChannel channel = raf.getChannel()) {
            if (index < 0 || index >= channel.size()) throw new IllegalArgumentException("Invalid index: " + index);
            channel.position(index);
            ByteBuffer buffer = ByteBuffer.allocate(1);
            channel.read(buffer);
            buffer.flip();
            return buffer.get();
        }
    }
}