package snake.model;

import java.util.Arrays;

/**
 * Created by Ivan on 24.06.2015.
 */
public class LevelInitParameters {

    private int number;
    private int[][] field;
    private int[][] snakeCoordinates;
    private int maxSnakeSize;

    public LevelInitParameters() {}

    public LevelInitParameters(int number, int[][] field, int[][] snakeCoordinates, int maxSnakeSize) {
        this.number = number;
        this.field = field;
        this.snakeCoordinates = snakeCoordinates;
        this.maxSnakeSize = maxSnakeSize;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int[][] getField() {
        return field;
    }

    public void setField(int[][] field) {
        this.field = field;
        Arrays.deepToString(field);
    }

    public int[][] getSnakeCoordinates() {
        return snakeCoordinates;
    }

    public void setSnakeCoordinates(int[][] snakeCoordinates) {
        this.snakeCoordinates = snakeCoordinates;
    }

    public int getMaxSnakeSize() {
        return maxSnakeSize;
    }

    public void setMaxSnakeSize(int maxSnakeSize) {
        this.maxSnakeSize = maxSnakeSize;
    }
}
