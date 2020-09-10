public enum Direct {

    LEFT, LEFT_UP, UP, RIGHT_UP, RIGHT, RIGHT_DOWN, DOWN, LEFT_DOWN;


    public static Direct revers(Direct d){

        switch (d){
            case UP: return LEFT_DOWN;
            case RIGHT_UP: return LEFT;
            case RIGHT: return LEFT_UP;
            case RIGHT_DOWN: return UP;
            case DOWN: return RIGHT_UP;
            case LEFT_DOWN: return RIGHT;
            case LEFT: return RIGHT_DOWN;
            case LEFT_UP: return DOWN;
        }
        return null;
    }
}
