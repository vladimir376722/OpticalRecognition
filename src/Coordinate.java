public class Coordinate {

    private int x;
    private int y;

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void incrementX(){
        x++;
    }
    public void incrementY(){
        y++;
    }
    public void decrementX(){
        x--;
    }
    public void decrementY(){
        y--;
    }

    @Override
    protected Coordinate clone(){
        return new Coordinate(x, y);
    }
}
