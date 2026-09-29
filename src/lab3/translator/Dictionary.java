package lab3.translator;

import lab3.exception.FileReadException;
import lab3.exception.InvalidFileFormatException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Dictionary {
    private final Map<String, String> entries = new HashMap<>();
    private int maxPhraseWords;

    public static Dictionary load(Path path) throws FileReadException, InvalidFileFormatException {
        List<String> lines;
        try {
            lines = Files.readAllLines(path);
        } catch (NoSuchFileException e) {
            throw new FileReadException("file not found: " + path, e);
        } catch (AccessDeniedException e) {
            throw new FileReadException("access denied: " + path, e);
        } catch (IOException e) {
            throw new FileReadException("cannot read file: " + path, e);
        }

        Dictionary dictionary = new Dictionary();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (!line.isBlank()) {
                dictionary.parseLine(line, i + 1);
            }
        }
        return dictionary;
    }

    private void parseLine(String line, int number) throws InvalidFileFormatException {
        String[] parts = line.split("\\|", -1);
        if (parts.length != 2) {
            throw new InvalidFileFormatException("line " + number + ": expected 'word | translation'");
        }

        List<String> words = Words.split(parts[0]);
        String translation = parts[1].strip();
        if (words.isEmpty() || translation.isEmpty()) {
            throw new InvalidFileFormatException("line " + number + ": empty word or translation");
        }

        entries.put(String.join(" ", words), translation);
        maxPhraseWords = Math.max(maxPhraseWords, words.size());
    }

    public Optional<String> find(String phrase) {
        return Optional.ofNullable(entries.get(phrase));
    }

    public int getMaxPhraseWords() {
        return maxPhraseWords;
    }

    public int size() {
        return entries.size();
    }
}
