package org.example;

import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.example.Mode.DECRYPT;
import static org.example.Mode.ENCRYPT;
import static org.example.Properties.*;
import static org.example.ValidateUtils.validateKey;
import static org.example.ValidateUtils.validateMessage;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwoSquareCipher {
    private String text;
    private String key1;
    private String key2;

    public String decrypt() {
        return cipher(DECRYPT);
    }

    public String encrypt() {
        return cipher(ENCRYPT);
    }

    private String cipher(Mode mode) {
        if (!validateKey(key1) && !validateKey(key2))
            throw new IllegalArgumentException("Not valid keys");

        if (!validateMessage(text, mode))
            throw new IllegalArgumentException("Not valid text");

        List<Character> filteredText = getFilteredText(mode);

        List<List<Character>>  digraphs = new ArrayList<>();
//        Разделим зашифрованный текст на биграммы -
//                получаем по две буквы за раз
        for (int i = 0; i < filteredText.size(); i += 2) {
            digraphs.add(List.of(filteredText.get(i), filteredText.get(i + 1)));
        }

        List<List<Character>> table1 = createTable(key1);
        List<List<Character>> table2 = createTable(key2);

        StringBuilder processedText = new StringBuilder();

        for (List<Character> digraph : digraphs) {
            Character letter1 = digraph.get(0);
            Character letter2 = digraph.get(1);

            Coordinate coordinate1 = getCoordinate(table1, letter1);
            Coordinate coordinate2 = getCoordinate(table2, letter2);

            if (!coordinate1.isValid() || !coordinate2.isValid())
                throw new RuntimeException("Error coordinate");

            Character processedLetter1;
            Character processedLetter2;

//            случай 1: буквы находятся в разных столбцах - поменяем местами номера столбцов
            if (coordinate1.getColumn() != coordinate2.getColumn()) {
                int temp = coordinate1.getColumn();
                coordinate1.setColumn(coordinate2.getColumn());
                coordinate2.setColumn(temp);

                processedLetter1 = table1.get(coordinate1.getRow()).get(coordinate1.getColumn());
                processedLetter2 = table2.get(coordinate2.getRow()).get(coordinate2.getColumn());
//            случай 2: буквы находятся в том же столбце - оставим как есть
            } else {
                processedLetter1 = letter1;
                processedLetter2 = letter2;
            }

            processedText.append(processedLetter1).append(processedLetter2);
        }
        if (mode.equals(DECRYPT))
            if (processedText.substring(processedText.length() - 1).equals("Z"))
                processedText = new StringBuilder(processedText.substring(0, processedText.length() - 1));
        return processedText.toString();
    }

    private List<Character> getFilteredText(Mode mode) {
        List<Character> characters = IntStream.range(0, text.length())
                .mapToObj(text::charAt)
                .collect(Collectors.toList());
        switch (mode) {
            case DECRYPT:
                return characters;
            case ENCRYPT:
                List<Character> characterList = characters.stream()
                        .map(Character::toUpperCase)
                        .filter(letter -> alphabet.indexOf(letter) != -1)
//                         Еще одна вещь, которую следует иметь в виду,
//                         - это то, что все буквенные символы 'J'
//                         заменяются символом 'I' с помощью этого шифра
                        .map(letter -> (letter == 'J') ? 'I' : letter)
                        .collect(Collectors.toList());
//                если длина нечетная, добавим "Z" в конец, чтобы сделать ее четной
                if (characterList.size() % 2 != 0)
                    characterList.add('Z');
                return characterList;
            default:
                throw new RuntimeException("Error during filtering text");
        }
    }

    private Coordinate getCoordinate(List<List<Character>> table, Character letter) {

        Character finalLetter = letter;
        if (finalLetter.equals('J'))
            finalLetter = 'I';


        for (int i = 0; i < table.size(); i++) {
            for (int j = 0; j < table.get(i).size(); j++) {
                if (table.get(i).get(j).equals(finalLetter))
                    return new Coordinate(i, j);
            }
        }

        return new Coordinate();
    }

    private List<List<Character>> createTable(String key) {
        List<Character> keyAsLetters = new ArrayList<>();
        List<Character> lettersNotInKey = new ArrayList<>();

        List<List<Character>> table = new ArrayList<>();

        key = key.toUpperCase();

        for (char letter : key.toCharArray()) {
            if (letter == 'J')
                letter = 'I';
            keyAsLetters.add(letter);
        }

//        Добавим все буквы алфавита в список и объединим I и J в одно
//        целое это будет использоваться для отслеживания того, какие буквы не находятся в ключе
        for (char letter : alphabet.toCharArray()) {
            if (letter == 'J') {
                letter = 'I';
                lettersNotInKey.remove(Character.valueOf('I'));
            }
            lettersNotInKey.add(letter);
        }

//        удалим ключевые буквы из списка букв, которых нет в ключе
        for (Character letter : keyAsLetters) {
            lettersNotInKey.remove(letter);
        }

//        поменяем порядок в двух списках для повышения эффективности при создании таблицы
        Collections.reverse(keyAsLetters);
        Collections.reverse(lettersNotInKey);

//        заполним таблицу
        for (int i = 0; i < MAX_ROW; i++) {
            List<Character> row = new ArrayList<>();
            for (int j = 0; j < MAX_COLUMN; j++) {
                Character current_letter;
//                пишем буквы из ключа
                if (keyAsLetters.size() > 0) {
                    current_letter = keyAsLetters.get(keyAsLetters.size() - 1);
                    keyAsLetters.remove(keyAsLetters.size() - 1);
//                    затем заполняем оставшимися буквами алфавита
                } else {
                    current_letter = lettersNotInKey.get(lettersNotInKey.size() - 1);
                    lettersNotInKey.remove(lettersNotInKey.size() - 1);
                }
                row.add(current_letter);
            }
            table.add(row);
        }
        return table;
    }
}
