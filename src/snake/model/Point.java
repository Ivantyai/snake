package snake.model;

import snake.view.Drawable;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Created by Ivan on 23.06.2015.
 */
public class Point {

    private int x;
    private int y;
    private PointType pointType;
    private Drawable drawable;
    
    public static final Drawable SnakePointDrawable;
    public static final Drawable SnakeHeadDrawable;
    public static final Drawable WallPointDrawable;
    public static final Drawable ApplePointDrawable;
    public static final Drawable FreePointDrawable;
    
    static {
        SnakePointDrawable = new Drawable() {
            @Override
            public void draw(Graphics g, int x, int y, int width, int height) {
                g.setColor(Color.YELLOW);
                g.fillRect(x, y, width, height);
            }
        };
        SnakeHeadDrawable = new Drawable() {
            @Override
            public void draw(Graphics g, int x, int y, int width, int height) {
                g.setColor(Color.ORANGE);
                g.fillRect(x, y, width, height);
            }
        };
        WallPointDrawable = new Drawable() {
            @Override
            public void draw(Graphics g, int x, int y, int width, int height) {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(x, y, width, height);
            }
        };
        ApplePointDrawable = new Drawable() {
            @Override
            public void draw(Graphics g, int x, int y, int width, int height) {
                g.setColor(Color.RED);
                g.fillOval(x, y, width, height);
            }
        };
        FreePointDrawable = new Drawable() {
            @Override
            public void draw(Graphics g, int x, int y, int width, int height) {
                g.setColor(Color.WHITE);
                g.fillRect(x, y, width, height);
            }
        };
    }   

    public Point(int x, int y, PointType pointType, Drawable drawable) {
        this.x = x;
        this.y = y;
        this.pointType = pointType;
        this.drawable = drawable;
    }

    public Point(int x, int y) {
       this(x,y,PointType.FREE, Point.FreePointDrawable);
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

    public PointType getType() {
        return pointType;
    }

    public void setType(PointType newType) {
        pointType = newType;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(this.getX() == ((Point)o).getX() &&
           this.getY() == ((Point)o).getY()) {
            return true;
        }
        return false;
    }
    
    public boolean equals(int x, int y) {
        if(getX() == x && getY() == y) {
            return true;
        }
        return false;
    }
    
    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
    
    public Drawable getDrawable() {
        return drawable;
    }
}
