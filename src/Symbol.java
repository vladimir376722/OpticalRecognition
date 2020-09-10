import java.io.Serializable;

public class Symbol implements Serializable {

    //Верхняя левая точка
    private int x;
    private int y;
    //
    private int width;
    private int height;

    private int [][] area;

    private String name = "";

    public Symbol(int x, int y, int width, int height, int[][] area) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.area = new int[width][height];

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                this.area[i][j] = area[x+i][y+j];
                //Support.print2D(area);
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getArea() {
        return area;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
