package ua.edu.ukma;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class InboxArchiver {

    public static void archiveTmpFiles(Path inbox, Path archive) throws IOException {
        if (Files.notExists(archive)) Files.createDirectories(archive);
        try (Stream<Path> paths = Files.list(inbox)) {
            paths.filter(p -> Files.isRegularFile(p))
                    .filter(p -> p.getFileName().toString().endsWith(".tmp"))
                    .forEach(p -> {
                        Path target = archive.resolve(p.getFileName());
                        try {
                            Files.move(p, target, StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            System.out.println("Move failed: " + p);
                        }
                    });
        }
    }
}