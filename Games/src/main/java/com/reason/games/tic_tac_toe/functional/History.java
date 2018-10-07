package com.reason.games.tic_tac_toe.functional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class History {

    private final static History instance = new History();
    private final static String Path = "d:\\JavaProjects\\Games\\src\\main\\java\\com\\reason\\games\\tic_tac_toe\\results.txt";
    private final File file = new File(Path);
    private String data;

    public History() {
        init();
    }

    public void init() {
        data = prepareData();
    }

    private String prepareData() {
        if (!file.exists()) {
            return "";
        }
        return readResults();
    }

    public static History getInstance() {
        return instance;
    }

    public String getPath() {
        return Path;
    }

    public void saveResults(String text) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            try (PrintWriter out = new PrintWriter(file)) {
                out.print(text);
                out.append("\n");
            }

        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }

    public String readResults() {
        StringBuilder sb = new StringBuilder();
        try {
            if (file.exists()) {
                try (BufferedReader in = new BufferedReader(new FileReader(file))) {
                    String str;
                    while ((str = in.readLine()) != null) {
                        if (str.equals(" ")) {
                            sb.append(" ");
                        } else if (str.equals(System.getProperty("line.separator"))) {
                            sb.append(System.getProperty("line.separator"));
                        } else if (str.equals("\n")) {
                            sb.append(System.getProperty("line.separator"));
                        }
                        sb.append(str);
                        sb.append(System.getProperty("line.separator"));
                    }
                }
            } else {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return sb.toString();
    }

    public void updateResults(String newText) {
        if (!file.exists()) {
            try (PrintWriter out = new PrintWriter(file)) {
                out.print(newText);
                out.append("\n");
            } catch (IOException e) {
                System.err.println(e);
            }

        } else {
            StringBuilder sb = new StringBuilder();
            String oldFile = readResults();
            sb.append(oldFile);
            sb.append(newText);
            saveResults(sb.toString());
        }
    }

    public String getData() {
        return data;
    }

}
