import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Support {


    public static void print2D(int[][] imageArea){
        //Двумерный рисунок
        for(int i = 0; i < imageArea[0].length; i++){
            for(int j = 0; j < imageArea.length; j++){
                System.out.print(imageArea[j][i]);
            }
            System.out.println();
        }
    }

    //перевод в монохром
    public static int [][] toMonochrome(BufferedImage image){
        int[][] imageArea = new int[image.getWidth()][image.getHeight()];

        for(int i = 0; i < imageArea[0].length; i++){
            for(int j = 0; j < imageArea.length; j++){
                //System.out.println(image.getRGB(i, j));
                if(image.getRGB(j, i) == -1)
                    imageArea[j][i] = 0;
                else
                    imageArea[j][i] = 1;
            }
        }
        return imageArea;
    }

    //Начало поиска
    //возврщает координату первой точки символа
    public static Coordinate searchStartPointCard(int[][] area, int startPositionX, int startPositionY, int empty){

        Coordinate c = new Coordinate(startPositionX, startPositionY);

        try {
            while (area[c.getX()][c.getY()] == empty){
                c.incrementX();
            }
            return c;
        }catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }

    //Поход по ребру изображения
    //Необходимо указать координату точки для начала обхода
    public static Symbol edgeImage(int[][] area, Coordinate startCoordinate, int empty) {

        Coordinate c = startCoordinate.clone(); //координата текущей позиции
        Coordinate cPas = startCoordinate.clone();  //координата придыдущей позиции

        //Определение координаты cPas.
        //Находим ближайшую закрашенную область
        if(area[c.getX()-1][c.getY()] != empty){
            cPas.setX(c.getX()-1);
            cPas.setY(c.getY());
        }else if(area[c.getX()-1][c.getY()+1] != empty) {
            cPas.setX(c.getX()-1);
            cPas.setY(c.getY()+1);
        }else if(area[c.getX()][c.getY()+1] != empty){
            cPas.setX(c.getX());
            cPas.setY(c.getY()+1);
        }else if(area[c.getX()+1][c.getY()+1] != empty){
            cPas.setX(c.getX()+1);
            cPas.setY(c.getY()+1);
        }else if(area[c.getX()+1][c.getY()] != empty) {
            cPas.setX(c.getX()+1);
            cPas.setY(c.getY());
        }else if(area[c.getX()+1][c.getY()-1] != empty){
            cPas.setX(c.getX()+1);
            cPas.setY(c.getY()-1);
        }else if(area[c.getX()][c.getY()-1] != empty) {
            cPas.setX(c.getX());
            cPas.setY(c.getY()-1);        }
        else if(area[c.getX()-1][c.getY()-1] != empty){
            cPas.setX(c.getX()-1);
            cPas.setY(c.getY()-1);
        } else ;


        Direct direct;

        //Определяем направление прохода с 1 сдвигом по часовой стрелке
        if(cPas.getX() < c.getX() && cPas.getY() == c.getY()) direct = Direct.LEFT_UP;
        else if(cPas.getX() < c.getX() && cPas.getY() < c.getY()) direct = Direct.UP;
        else if(cPas.getX() == c.getX() && cPas.getY() < c.getY()) direct = Direct.RIGHT_UP;
        else if(cPas.getX() > c.getX() && cPas.getY() < c.getY()) direct = Direct.RIGHT;
        else if(cPas.getX() > c.getX() && cPas.getY() == c.getY()) direct = Direct.RIGHT_DOWN;
        else if(cPas.getX() > c.getX() && cPas.getY() > c.getY()) direct = Direct.DOWN;
        else if(cPas.getX() == c.getX() && cPas.getY() > c.getY()) direct = Direct.LEFT_DOWN;
        else if(cPas.getX() < c.getX() && cPas.getY() > c.getY()) direct = Direct.LEFT;
        else return null;

        //координаты краёв фигуры
        int maxX = 0;
        int maxY = 0;
        int minX = area.length;
        int minY = area[0].length;

        //Определение краёв фигуры
        while (true){

            if(minX > c.getX()) minX = c.getX();
            if(minY > c.getY()) minY = c.getY();
            if(maxX < c.getX()) maxX = c.getX();
            if(maxY < c.getY()) maxY = c.getY();

            //Сдвиг на новую позицию
            switch (direct){

                case LEFT: if(area[c.getX()-1][c.getY()] == empty){
                    direct = Direct.LEFT_UP;
                    continue;
                }else {
                    direct = Direct.revers(direct);
                    cPas.setX(c.getX());
                    cPas.setY(c.getY());
                    c.setX(c.getX()-1);
                    c.setY(c.getY());
                    if(minX > c.getX()) minX = c.getX(); //Определяем границы символа
                    if (c.getX() == startCoordinate.getX() && c.getY() == startCoordinate.getY()){//Выход если вернулись к началу символа
                        break;
                    }
                    continue;
                }

                case LEFT_UP: if(area[c.getX()-1][c.getY()-1] == empty){
                    direct = Direct.UP;
                    continue;
                }else {
                    direct = Direct.revers(direct);
                    cPas.setX(c.getX());
                    cPas.setY(c.getY());
                    c.setX(c.getX()-1);
                    c.setY(c.getY()-1);
                    if(minX > c.getX()) minX = c.getX(); //Определяем границы символа
                    if(minY > c.getY()) minY = c.getY();
                    if (c.getX() == startCoordinate.getX() && c.getY() == startCoordinate.getY()){//Выход если вернулись к началу символа
                        break;
                    }
                    continue;
                }

                case UP: if(area[c.getX()][c.getY()-1] == empty){
                    direct = Direct.RIGHT_UP;
                    continue;
                }else {
                    direct = Direct.revers(direct);
                    cPas.setX(c.getX());
                    cPas.setY(c.getY());
                    c.setX(c.getX());
                    c.setY(c.getY()-1);
                    //Определяем границы символа
                    if(minY > c.getY()) minY = c.getY();
                    if (c.getX() == startCoordinate.getX() && c.getY() == startCoordinate.getY()){//Выход если вернулись к началу символа
                        break;
                    }
                    continue;
                }

                case RIGHT_UP: if(area[c.getX()+1][c.getY()-1] == empty){
                    direct = Direct.RIGHT;
                    continue;
                }else {
                    direct = Direct.revers(direct);
                    cPas.setX(c.getX());
                    cPas.setY(c.getY());
                    c.setX(c.getX()+1);
                    c.setY(c.getY()-1);
                    if(maxX < c.getX()) maxX = c.getX(); //Определяем границы символа
                    if(minY > c.getY()) minY = c.getY();
                    if (c.getX() == startCoordinate.getX() && c.getY() == startCoordinate.getY()){//Выход если вернулись к началу символа
                        break;
                    }
                    continue;
                }
                case RIGHT: if(area[c.getX()+1][c.getY()] == empty){
                    direct = Direct.RIGHT_DOWN;
                    continue;
                }else {
                    direct = Direct.revers(direct);
                    cPas.setX(c.getX());
                    cPas.setY(c.getY());
                    c.setX(c.getX()+1);
                    c.setY(c.getY());
                    if(maxX < c.getX()) maxX = c.getX(); //Определяем границы символа
                    if (c.getX() == startCoordinate.getX() && c.getY() == startCoordinate.getY()){//Выход если вернулись к началу символа
                        break;
                    }
                    continue;
                }
                case RIGHT_DOWN: if(area[c.getX()+1][c.getY()+1] == empty){
                    direct = Direct.DOWN;
                    continue;
                }else {
                    direct = Direct.revers(direct);
                    cPas.setX(c.getX());
                    cPas.setY(c.getY());
                    c.setX(c.getX()+1);
                    c.setY(c.getY()+1);
                    if(maxX < c.getX()) maxX = c.getX(); //Определяем границы символа
                    if(maxY < c.getY()) maxY = c.getY(); //Определяем границы символа
                    if (c.getX() == startCoordinate.getX() && c.getY() == startCoordinate.getY()){//Выход если вернулись к началу символа
                        break;
                    }
                    continue;
                }
                case DOWN: if(area[c.getX()][c.getY()+1] == empty){
                    direct = Direct.LEFT_DOWN;
                    continue;
                }else {
                    direct = Direct.revers(direct);
                    cPas.setX(c.getX());
                    cPas.setY(c.getY());
                    c.setX(c.getX());
                    c.setY(c.getY()+1);
                    if(maxY < c.getY()) maxY = c.getY(); //Определяем границы символа
                    if (c.getX() == startCoordinate.getX() && c.getY() == startCoordinate.getY()){//Выход если вернулись к началу символа
                        break;
                    }
                    continue;
                }
                case LEFT_DOWN: if(area[c.getX()-1][c.getY()+1] == empty){
                    direct = Direct.LEFT;
                    continue;
                }else {
                    direct = Direct.revers(direct);
                    cPas.setX(c.getX());
                    cPas.setY(c.getY());
                    c.setX(c.getX()-1);
                    c.setY(c.getY()+1);
                    if(minX > c.getX()) minX = c.getX();
                    if(maxY < c.getY()) maxY = c.getY(); //Определяем границы символа
                    if (c.getX() == startCoordinate.getX() && c.getY() == startCoordinate.getY()){//Выход если вернулись к началу символа
                        break;
                    }
                    continue;
                }
            }

            maxX = maxX-minX+1;
            maxY = maxY-minY+1;

            //int[][] newArea = Support.partOfArea(minX, minY, maxX, maxY, area);
            //Support.print2D(newArea);

            return new Symbol(minX, minY, maxX, maxY, area);
        }
    }

    //поиск и сравнение символа
    //возвращает имя самого похожего
    public static String comparison(Symbol symbolTarget, ArrayList<Symbol> symbolsPattern){

        int coin = 0;
        int maxCoin = 0;
        int idealCount = 0;

        Symbol favorite = null;
        for(Symbol s: symbolsPattern){
            coin = 0;
            idealCount = 0;
            for(int i = 0; i < s.getHeight(); i++){
                for(int j = 0; j < s.getWidth(); j++){
                    try{
                        if(s.getArea()[j][i] == symbolTarget.getArea()[j][i]) coin++;
                        idealCount++;
                    }catch (ArrayIndexOutOfBoundsException e){
                    }
                }
            }

            if(maxCoin < coin){
                maxCoin = coin;
                favorite = s;
            }

            if((idealCount - coin) < (idealCount/20)){
                return favorite.getName();
            }
        }

        return "null";
    }

    public static void searchOfSymbol(File f, ArrayList<Symbol> patterns) throws IOException {

        ArrayList<Symbol> listCard = new ArrayList<>();
        ArrayList<Symbol> symbols = new ArrayList<>();
        Coordinate c = null;

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

        Symbol symbol = null;
        for (Symbol s: listCard){
            //Верхний
            c = Support.searchStartPointCard(s.getArea(), 0, s.getHeight() / 8, 0);
            if(c != null)
                symbol = Support.edgeImage(s.getArea(), c, 0);
            symbols.add(symbol);
            //Нижний
            c = Support.searchStartPointCard(s.getArea(), 0, s.getHeight() / 4 * 3, 0);
            if(c != null)
                symbol = Support.edgeImage(s.getArea(), c, 0);
            symbols.add(symbol);
        }

        StringBuilder sb = new StringBuilder("на этой картинке " + f.getAbsolutePath() + " на столе лежат карты ");

        for(Symbol s: symbols){
            String name = "";
            name = comparison(s, patterns);
            sb.append(name);
        }

        System.out.println(sb);

    }
}
