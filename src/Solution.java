import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class Solution {

    private static final int DOWN = 0;
    private static final int RIGHT = 1;
    private static final int UP = 2;

    public static void main(String ... args) throws IOException, ClassNotFoundException {

        ArrayList<Symbol> patterns = new ArrayList<>();
        //patterns = Helper.createPattern();    //Создание шаблонов

        patterns = Helper.readInFile(Helper.PATTERN);

        System.out.println("Укажите путь до папки с картинками");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String filesPath = reader.readLine();

        ArrayList<File> files = Helper.fileWalk(filesPath);

        for (File f: files){
            Support.searchOfSymbol(f, patterns);
        }


    }

}
