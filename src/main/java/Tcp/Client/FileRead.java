package Tcp.Client;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileRead {

    private final File file;

    public FileRead(String filePath) {
        file = new File(filePath);
    }

    public String getMessage() {
        StringBuilder message = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                message.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return message.toString();
    }

    public String getMessage2() {
//        FileInputStream fileInputStream =
        return " ";
    }

    public boolean fileExists() {
        return file.exists();
    }
}