package usi.si.seart.analysis;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * A git repository cloned on the filesystem.
 */
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public
class ClonedRepo implements AutoCloseable {

    @Getter
    Path path;

    /**
     * When used in a try-with-resources block, automatically deletes the folder on the filesystem once the clause is exited.
     */
    @Override
    public void close() {
        try (Stream<Path> files = Files.walk(path)) {
            files
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            log.error("Exception when trying to delete cloned repo folder", e);
        }
    }
}