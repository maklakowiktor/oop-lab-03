package lab3.translator;

import java.util.List;
import java.util.regex.Pattern;

final class Words {
    static final Pattern WORD = Pattern.compile("[\\p{L}\\p{N}'-]+");

    private Words() {
    }

    static List<String> split(String text) {
        return WORD.matcher(text).results()
                .map(match -> match.group().toLowerCase())
                .toList();
    }
}
