package com.company.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileUtils {
    private static final String currentDirectory = System.getProperty("user.dir");
    /**
     * methode qui permet de sauvegarder une chaine de caractere dans un  fichier
     *
     * @param filename
     * @param content
     */
    public static String saveToFile(String filename, String content) throws IOException {

        if (filename == null || content == null || filename.isEmpty() || content.isEmpty()) {
            return "file and content are required ";
        }
        File file = new File(currentDirectory + File.separator + filename );
        synchronized (file) {
            Files.write(Paths.get(file.getName()), content.getBytes(), StandardOpenOption.CREATE);
        }
        return "file create sucessfuly";
    }
}
