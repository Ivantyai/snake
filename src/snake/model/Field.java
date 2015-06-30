package snake.model;

import snake.exceptions.GameOverException;
import snake.exceptions.LevelCompleteException;
import snake.exceptions.NoPointWithSuchCoordsException;
import java.util.*;

/**
 * Created by Ivan on 23.06.2015.
 */
public class Field {

    private int sizeX;
    private int sizeY;
    private Point[][] points;
    private List<Point> freePoints;
    private List<Apple> eatenApples;
    private Snake snake;
    private Apple apple;
    private AppleManager appleManager;    
    private boolean alreadyChange;

    class AppleManager {

        Random random = new Random();

        public void createNewApple() {
            int randomIndex = random.nextInt(freePoints.size());
            Point randomPointFromFree = freePoints.get(randomIndex);
            apple = new Apple(randomPointFromFree, eatenApples.size() + 1);
            Field.this.setApplePoint(randomPointFromFree);
        }

        public void eatCurrentApple() {
            eatenApples.add(apple);            
            apple = null;
        }
        
        public void deleteAllApples() {
            eatenApples = new ArrayList<>();
            Field.this.setFreePoint(apple.place);
            apple = null;
        }
    }

    class Apple {

        public static final int INITIAL_PRICE = 1;
        private Point place;
        private int price;

        public Apple(Point place, int price) {
            this.place = place;
            this.price = price;
        }

        public Point getPlace() {
            return place;
        }
    }

    interface NextAfterSnakePoint {
        Point getNext();
    }

    class Snake {

        private LinkedList<Point> bodyPoints;
        private SnakeDirection direction;
        private SnakeStatus currentStatus;
        private int maxSize;
        private SnakeDirection startDirection = SnakeDirection.RIGHT;
        private int[][] startPoints;

        private NextAfterSnakePoint nextIsUp;
        private NextAfterSnakePoint nextIsDown;
        private NextAfterSnakePoint nextIsRight;
        private NextAfterSnakePoint nextIsLeft;
        
        private NextAfterSnakePoint nextAfterSnake;

        public Snake(int[][] snakePoints, int maxSnakeSize) {
            startPoints = snakePoints;
            this.maxSize = maxSnakeSize;
            init();
        }

        private void init() {           
            nextIsUp = new NextAfterSnakePoint() {
                @Override
                public Point getNext() {
                    return Field.this.getUpNeignborOfPoint(bodyPoints.getFirst());
                }
            };            
            nextIsDown = new NextAfterSnakePoint() {
                @Override
                public Point getNext() {
                    return Field.this.getDownNeignborOfPoint(bodyPoints.getFirst());
                }
            };            
            nextIsRight = new NextAfterSnakePoint() {
                @Override
                public Point getNext() {
                    return Field.this.getRightNeignborOfPoint(bodyPoints.getFirst());
                }
            };            
            nextIsLeft = new NextAfterSnakePoint() {
                @Override
                public Point getNext() {
                    return Field.this.getLeftNeignborOfPoint(bodyPoints.getFirst());
                }
            };           
           
            createSnake();            
        }

        public void move() {  
            Point next = nextAfterSnake.getNext();
            tryMove(next);
        }

        private void tryMove(Point next) {
            if (next.getType() == PointType.WALL || next.getType() == PointType.SNAKE) {
                currentStatus = SnakeStatus.CRASHED;
                return;
            }
            Field.this.setSnakePoint(bodyPoints.getFirst());
            bodyPoints.addFirst(next);
            
            if (next.getType() == PointType.FREE) {
                currentStatus = SnakeStatus.MOVED_WITHOUT_GROWTH;
                deleteLast();                
            } else {
                if (bodyPoints.size() == maxSize) {
                    currentStatus = SnakeStatus.ACHIEVE_MAX_SIZE;
                } else {
                    currentStatus = SnakeStatus.MOVED_WITH_GROWTH;
                }
            }
            Field.this.setSnakeHeadPoint(next);                        
        }

        public void changeDirection(SnakeDirection newDirection) {
            direction = newDirection;
            
            if(direction == SnakeDirection.DOWN) {
                nextAfterSnake = nextIsDown;
            }
            else if(direction == SnakeDirection.UP) {
                nextAfterSnake = nextIsUp;
            }
            else if(direction == SnakeDirection.LEFT) {
                nextAfterSnake = nextIsLeft;
            }
            else if(direction == SnakeDirection.RIGHT) {
                nextAfterSnake = nextIsRight;
            }
        }

        public int getSize() {
            return bodyPoints.size();
        }

        public SnakeStatus getSnakeStatusOnCurrentStep() {
            return currentStatus;
        }

        public void reset() {
            deleteSnake();            
            createSnake(); 
        }
        
        private void deleteSnake() {
            for(Point point : bodyPoints) {
                Field.this.setFreePoint(point);
            }
        }
        
        private void createSnake() {
            bodyPoints = new LinkedList<Point>(); 
            
            for (int i = 0; ; i++) {
                try {
                    Point currentPoint = Field.this.getPointByCoordinates(startPoints[i][0], startPoints[i][1]);
                    bodyPoints.addFirst(currentPoint);
                    if(i != startPoints.length - 1) {
                        Field.this.setSnakePoint(currentPoint);
                    }
                    else {
                        Field.this.setSnakeHeadPoint(currentPoint);
                        break;
                    }
                } catch (NoPointWithSuchCoordsException ignore) {}
            }
            changeDirection(startDirection);
        }
        
        private void deleteLast() {
            Point removed = bodyPoints.removeLast();
            Field.this.setFreePoint(removed);
        }
    }

    public Field(int[][] field, int[][] snakePoints, int maxSnakeSize) {
        sizeX = field[0].length;
        sizeY = field.length;
        Point current;
        points = new Point[sizeY][sizeX];
        freePoints = new ArrayList<>();
        for (int j = 0; j < field.length; j++) {
            for (int i = 0; i < field[j].length; i++) {
                PointType currentType;
                if (field[j][i] == 0) {
                    current = new Point(i, j, PointType.FREE, Point.FreePointDrawable);
                    System.out.println();
                    freePoints.add(current);
                } else {
                    current = new Point(i, j, PointType.WALL, Point.WallPointDrawable);
                }
                points[j][i] = current;
            }
        }
        snake = new Snake(snakePoints, maxSnakeSize);
        eatenApples = new ArrayList<Apple>();
        appleManager = new AppleManager();
        appleManager.createNewApple();
    }

    public Field(LevelInitParameters parameters) {
        this(parameters.getField(),
                parameters.getSnakeCoordinates(),
                parameters.getMaxSnakeSize());
    }

    public void step() throws GameOverException, LevelCompleteException {
        snake.move();
        alreadyChange = false;
        SnakeStatus currentSnakeStatus = snake.getSnakeStatusOnCurrentStep();

        if (currentSnakeStatus == SnakeStatus.CRASHED) {
            throw new GameOverException();
        } else if (currentSnakeStatus != SnakeStatus.MOVED_WITHOUT_GROWTH) {
            createNewApple();
            if (currentSnakeStatus == SnakeStatus.ACHIEVE_MAX_SIZE) {
                throw new LevelCompleteException();
            }
        }
    }

    public Point[][] getPointsArray() {
        return points;
    }

    public void changeSnakeDirection(SnakeDirection newDirection) {
        if (snake.direction.opposite() != newDirection && !alreadyChange) {
            snake.changeDirection(newDirection);
            alreadyChange = true;
        }
    }

    private Point getUpNeignborOfPoint(Point p) {
        int x = p.getX();
        int y = p.getY();

        if (y == 0) {
            return points[sizeY - 1][x];
        }

        return points[y - 1][x];
    }

    private Point getLeftNeignborOfPoint(Point p) {
        int x = p.getX();
        int y = p.getY();

        if (x == 0) {
            return points[y][sizeX - 1];
        }

        return points[y][x - 1];
    }

    private Point getRightNeignborOfPoint(Point p) {
        int x = p.getX();
        int y = p.getY();

        if (x == sizeX - 1) {
            return points[y][0];
        }

        return points[y][x + 1];
    }

    private Point getDownNeignborOfPoint(Point p) {
        int x = p.getX();
        int y = p.getY();

        if (y == sizeY - 1) {
            return points[0][x];
        }

        return points[y + 1][x];
    }

    private void setFreePoint(Point point) {
        point.setType(PointType.FREE);
        point.setDrawable(Point.FreePointDrawable);
        freePoints.add(point);
    }

    private void setApplePoint(Point point) {
        point.setType(PointType.APPLE);
        point.setDrawable(Point.ApplePointDrawable);
        freePoints.remove(point);
    }

    private void setSnakePoint(Point point) {
        point.setType(PointType.SNAKE);
        point.setDrawable(Point.SnakePointDrawable);
        freePoints.remove(point);
    }
    
    private void setSnakeHeadPoint(Point point) {
        point.setType(PointType.SNAKE);
        point.setDrawable(Point.SnakeHeadDrawable);
        freePoints.remove(point);
    }

    private void createNewApple() {
        appleManager.eatCurrentApple();
        appleManager.createNewApple();
    }

    public Point getPointByCoordinates(int x, int y) throws NoPointWithSuchCoordsException {
        for (int j = 0; j < points.length; j++) {
            for (int i = 0; i < points[j].length; i++) {
                if (points[j][i].equals(x, y)) {
                    return points[j][i];
                }
            }
        }
        throw new NoPointWithSuchCoordsException();
    }

    public List<Point> getWalles() {
        List<Point> walles = new ArrayList<>();
        for (int j = 0; j < points.length; j++) {
            for (int i = 0; i < points[j].length; i++) {
                if (points[j][i].getType() == PointType.WALL) {
                    walles.add(points[j][i]);
                }
            }
        }
        return walles;
    }

    public Point getApplePoint() {
        return apple.getPlace();
    }

    public LinkedList<Point> getSnakePoints() {
        return snake.bodyPoints;
    }

    public void reset() {
        appleManager.deleteAllApples();
        snake.reset();
        appleManager.createNewApple();
    }    
}
