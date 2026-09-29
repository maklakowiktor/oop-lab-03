package lab3;

import lab3.exception.FileReadException;
import lab3.exception.InvalidFileFormatException;
import lab3.translator.Dictionary;
import lab3.translator.Translator;

import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Path path = Path.of(args.length > 0 ? args[0] : "dictionary.txt");

        Dictionary dictionary;
        try {
            dictionary = Dictionary.load(path);
        } catch (FileReadException | InvalidFileFormatException e) {
            System.out.println("error: " + e.getMessage());
            return;
        }
        System.out.println("dictionary loaded: " + dictionary.size() + " entries");

        Translator translator = new Translator(dictionary);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("text (empty line to exit): ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String text = scanner.nextLine();
            if (text.isBlank()) {
                break;
            }
            System.out.println(translator.translate(text));
        }
    }
}
