package lab3.translator;

import java.util.List;
import java.util.Optional;
import java.util.regex.MatchResult;

public class Translator {
    private final Dictionary dictionary;

    public Translator(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public String translate(String text) {
        if (text == null || dictionary.getMaxPhraseWords() == 0) {
            return text;
        }
        List<MatchResult> words = Words.WORD.matcher(text).results().toList();
        StringBuilder result = new StringBuilder();
        int position = 0;
        int i = 0;

        while (i < words.size()) {
            MatchResult first = words.get(i);
            result.append(text, position, first.start());

            int length = Math.min(dictionary.getMaxPhraseWords(), words.size() - i);
            Optional<String> translation = lookup(text, words.subList(i, i + length));
            while (translation.isEmpty() && length > 1) {
                length--;
                translation = lookup(text, words.subList(i, i + length));
            }

            MatchResult last = words.get(i + length - 1);
            result.append(translation.orElse(text.substring(first.start(), last.end())));
            position = last.end();
            i += length;
        }
        return result.append(text.substring(position)).toString();
    }

    private Optional<String> lookup(String text, List<MatchResult> phrase) {
        for (int j = 1; j < phrase.size(); j++) {
            if (!text.substring(phrase.get(j - 1).end(), phrase.get(j).start()).isBlank()) {
                return Optional.empty();
            }
        }
        String key = String.join(" ", phrase.stream().map(match -> match.group().toLowerCase()).toList());
        return dictionary.find(key);
    }
}
