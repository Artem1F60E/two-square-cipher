package org.example;

import com.beust.jcommander.Parameter;

import java.util.List;

public class CommandLineArgs {
    @Parameter(
            names = {"--encrypt", "-e"},
            description = "Encrypt MESSAGE using KEY1 and KEY2",
            required = false,
            listConverter = ListConverter.class
    )
    private List<String> encryptArgs;

    @Parameter(
            names = {"--decrypt", "-d"},
            description = "Decrypt MESSAGE using KEY1 and KEY2",
            required = false,
            listConverter = ListConverter.class
    )
    private List<String> decryptArgs;

    @Parameter(names = {"--file", "-f"}, description = "Read message from file")
    private boolean file = false;

    @Parameter(names = {"--help", "-h"}, help = true)
    private boolean help = false;


    public List<String> getEncryptArgs() {
        return encryptArgs;
    }

    public List<String> getDecryptArgs() {
        return decryptArgs;
    }

    public boolean isFile() {
        return file;
    }

    public boolean isHelp() {
        return help;
    }
}
