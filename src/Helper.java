import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Helper {

    public static final String PATTERN = "Pattern";
    private static final String PATTERN_RESERVE = "PatternR";

    public static ArrayList<File> fileWalk(String dir){
        File folder = new File(dir);
        ArrayList<File> files = new ArrayList<>();
        for (File fileEntry : folder.listFiles()) {
            files.add(fileEntry);
        }
        return files;
    }


    public static List<Symbol> makePattern(List<File> fileList) throws IOException, ClassNotFoundException {

        //пока с одним изображением
        //fileList = fileList.subList(0, 10);

        ArrayList<Symbol> symbolsTemp = new ArrayList<>();
        ArrayList<Symbol> symbolsTotal = new ArrayList<>();

        ArrayList <Symbol> listCard = new ArrayList<>();
        Coordinate c = null;

        for(File f: fileList){

            BufferedImage image = ImageIO.read(f);

            //region search image
            int widthImage = image.getTileWidth();
            int heightImage = image.getTileHeight();
            int regionXStartImage = widthImage/3 - widthImage/6;
            int regionYStartImage = heightImage/2 - heightImage/35;
            int regionXFinishImage = widthImage/2 + widthImage/6;
            int regionYFinishImage = heightImage/7;

            BufferedImage image1 = image.getSubimage(regionXStartImage, regionYStartImage, regionXFinishImage, regionYFinishImage);
            //в монохром
            int[][] imageArea = Support.toMonochrome(image1);

            //поиск первой точки карты
            c = Support.searchStartPointCard(imageArea, 0, imageArea[0].length/2, 1);
            Symbol cs = null;

            //собираем карты в массив
            while (c != null){
                cs = Support.edgeImage(imageArea, c, 1);
                listCard.add(cs);
                c = Support.searchStartPointCard(imageArea, cs.getX() + cs.getWidth() + 3, imageArea[0].length/2, 1);
            }

        }
        System.out.println("count card equals "+listCard.size());

        //Собираем массив символов
        Symbol symbol = null;
        for (Symbol s: listCard){
            //Верхний
            c = Support.searchStartPointCard(s.getArea(), 0, s.getHeight() / 8, 0);
            if(c != null)
                symbol = Support.edgeImage(s.getArea(), c, 0);
            symbolsTemp.add(symbol);
            //Нижний
            c = Support.searchStartPointCard(s.getArea(), 0, s.getHeight() / 4 * 3, 0);
            if(c != null)
                symbol = Support.edgeImage(s.getArea(), c, 0);
            symbolsTemp.add(symbol);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String name = "";
        //строим шаблоны.
        for(Symbol s : symbolsTemp){

            boolean exist = false;

            for (Symbol s1: symbolsTotal){

                String temp = Support.comparison(s, symbolsTotal);
                System.out.println("suitable name ---" + temp);
                if(s1.getName().equals(temp)){
                    System.out.println("this symbol name is exist");
                    exist = true;
                    break;
                }
            }

            if(exist){
                System.out.println("continue");
                continue;
            }

            Support.print2D(s.getArea());
            System.out.println("имя карты");
            name = reader.readLine();
            if(name == "stop"){
                writeToFile(symbolsTotal);
                return symbolsTotal;
            }

            s.setName(name);
            symbolsTotal.add(s);

        }

        writeToFile(symbolsTotal);
        return symbolsTotal;
    }

    public static void writeToFile(ArrayList<Symbol> s) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Pattern"));
        oos.writeObject(s);
        oos.close();
    }
    public static ArrayList<Symbol> readInFile(String name) throws IOException, ClassNotFoundException {
        ArrayList<Symbol> list = new ArrayList<>();
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(name));

        list = (ArrayList<Symbol>) ois.readObject();
        ois.close();
        return list;
    }

    public static ArrayList<Symbol> createPattern() throws IOException, ClassNotFoundException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<Symbol> sPattern = new ArrayList<>();
        String pathToPattern = "";
        String sup = "";
        System.out.println("Создать новые шаблоны?  yes/no");
        sup = reader.readLine();
        if(sup.equals("yes")){
            System.out.println("specify the path to the folder with images");
            pathToPattern = reader.readLine();
            ArrayList<File> files = Helper.fileWalk(pathToPattern);
            sPattern = (ArrayList<Symbol>) Helper.makePattern(files);
        }else {
            sPattern = readInFile(PATTERN);
            if(sPattern == null) sPattern = readInFile(PATTERN_RESERVE);
            if(sPattern == null) System.out.println("template file not available");
        }
        return sPattern;

    }



}
