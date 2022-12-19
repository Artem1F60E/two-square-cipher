package org.example;

import com.beust.jcommander.ParameterException;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Getter
public class InputData {

    private Mode mode;
    private String text;
    private String key1;
    private String key2;

    public InputData(List<String> encrypt_args, List<String> decrypt_args, boolean file) {

        if (encrypt_args != null) {
            this.text = file ? getMessageFromFile(encrypt_args.get(0)) : encrypt_args.get(0);
            this.mode = Mode.ENCRYPT;
            this.key1 = encrypt_args.get(1);
            this.key2 = encrypt_args.get(2);
        } else if (decrypt_args.size() == 3) {
            this.text = file ? getMessageFromFile(decrypt_args.get(0)) : decrypt_args.get(0);
            this.mode = Mode.DECRYPT;
            this.key1 = decrypt_args.get(1);
            this.key2 = decrypt_args.get(2);
        } else {
            throw new ParameterException("Please choose mode");
        }
    }

    public String getMessageFromFile(String filename) {
        try {
            Path path = Paths.get(filename);
            return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
