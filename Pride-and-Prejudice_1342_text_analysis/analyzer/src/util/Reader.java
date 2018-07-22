package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Reader {
    private BufferedReader br;
    private StringBuffer text;

    public Reader(String filePath) throws IOException {
        File file = new File(filePath);
        this.br = new BufferedReader(new FileReader(file));

        this.text = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null)
            text.append(line+"\n");
    }

    public String getText() throws IOException{
        return text.toString();
    }

    public List<String> getTextByChapter() {
        List<String> chapText;

        String chapterStartMarker = "Chapter [0-9]+\n";
        return null;
    }
}
