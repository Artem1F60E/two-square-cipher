package org.example;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public class Main {

    public static void main(String[] args) {
        InputData inputData = getInputData(args);

        TwoSquareCipher cipher = TwoSquareCipher.builder()
                .text(inputData.getText())
                .key1(inputData.getKey1())
                .key2(inputData.getKey2())
                .build();

        String result = "";
        switch (inputData.getMode()) {
            case ENCRYPT:
                result = cipher.encrypt();
                break;
            case DECRYPT:
                result = cipher.decrypt();
                break;
        }

        System.out.printf("Start %s with keys %s, %s....\n" +
                        "Input message: %s\n" +
                        "Result message: %s\n", inputData.getMode().getName(),
                inputData.getKey1(), inputData.getKey2(),
                inputData.getText(), result);
    }

    private static InputData getInputData(String[] args) {
        CommandLineArgs parameters = new CommandLineArgs();
        JCommander commander = JCommander.newBuilder()
                .addObject(parameters)
                .build();
        try {
            commander.parse(args);
            if (!parameters.isHelp()) {
                if (parameters.getDecryptArgs() == null && parameters.getEncryptArgs() == null)
                    throw new ParameterException("Please choose mode");
                else {
                    if (parameters.getDecryptArgs() != null && parameters.getDecryptArgs().size() != 3)
                        throw new ParameterException("Incorrect count of parameters");
                    else if (parameters.getEncryptArgs() != null && parameters.getEncryptArgs().size() != 3)
                        throw new ParameterException("Incorrect count of parameters");
                }
            }
        } catch (Exception parEx) {
            parEx.printStackTrace();
            badArgsExit();
        }
        if (parameters.isHelp()) {
            commander.usage();
            System.exit(0);
        }
        return new InputData(
                parameters.getEncryptArgs(),
                parameters.getDecryptArgs(),
                parameters.isFile()
        );
    }

    private static void badArgsExit() {
        System.err.println("Invalid parameter. Program call example:");
        System.err.println("java -jar two-square-cipher.jar -f -e path/test.txt,hello,world");
        System.err.println("java -jar two-square-cipher.jar -e \"privet mir\",hello,world");
        System.exit(-1);
    }

}