package snake.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.JLabel;
import javax.swing.JPanel;
import snake.controller.GameController;
import snake.model.Point;

/**
 *
 * @author Ivan
 */
public class FieldPanel extends JPanel implements KeyListener {
    private int customWidth = 400;
    private int customHeight = 400; 
    
    private int cellWidth;
    private int cellHeight;    
    
    private Color backgroundColor = Color.WHITE;
    private Color cellBorderColor = Color.BLACK;
    
    private Point[][] field;
    private List<Point> changed;    
    private boolean drawOnlyChanged;    
    
    private JLabel gameOverLabel;
    private GameController controller;         
    
    public FieldPanel() {
        setSize(customWidth, customHeight); 
        setFocusable(true);
        gameOverLabel = new JLabel("GAME OVER");
        gameOverLabel.setVisible(false);         
    }    
    
    @Override
    public void paint(Graphics g) { 
       if(drawOnlyChanged) {
           drawOnlyChanged(g);
       }
       else {
           drawAll(g);
       }
    } 
    
    public void paint(List<Point> changed) {
        this.changed = changed;
        //drawOnlyChanged = true;
        repaint();
    }
    
    public void paint(Point[][] field) {
        this.field = field;
        drawOnlyChanged = false;
        cellWidth = customWidth / field[0].length;
        cellHeight = customHeight / field.length;
        repaint();
    }
    
    public void drawAll(Graphics g) {
         // background
        g.setColor(backgroundColor);
        g.fillRect(0, 0, customWidth, customHeight);
        
        // cell grid
        g.setColor(cellBorderColor);
        for(int i = 0; i < field[0].length; i++) {
             g.drawLine(i * cellWidth, 0, i * cellWidth, customHeight);
        }
        for(int j = 0; j < field.length; j++) {
             g.drawLine(0, j * cellHeight, customWidth, j * cellHeight);
        }     
        
        for(int j = 0; j < field.length; j++) {
            for(int i = 0; i < field[j].length; i++) {
                field[j][i].getDrawable().draw(g, i * cellWidth + 1, 
                                               j * cellHeight + 1, 
                                               cellWidth - 2, cellHeight - 2);
            }
        }
    } 
    
    public void drawOnlyChanged(Graphics g) {
        for(Point point : changed) {
            point.getDrawable().draw(g, point.getX() * cellWidth + 1, 
                                    point.getY() * cellHeight + 1, 
                                    cellWidth - 2, cellHeight - 2);
            /*
            if(point.getType() == PointType.SNAKE) {
                 g.setColor(snakeColor);
                 g.fillRect(point.getX() * cellWidth + 1, point.getY() * cellHeight + 1, cellWidth, cellHeight);
            }
            else if(point.getType() == PointType.FREE) {
                g.setColor(backgroundColor);
                g.fillRect(point.getX() * cellWidth + 1, point.getY() * cellHeight + 1, cellWidth, cellHeight);
            }
            else if(point.getType() == PointType.APPLE) {
                g.setColor(appleColor);
                g.fillOval(point.getX() * cellWidth + 1, point.getY() * cellHeight + 1, cellWidth, cellHeight);
            }*/
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        
        if(e.getKeyChar() == 'a') {
            controller.left();
        }
        else if(e.getKeyChar() == 'd') {
           controller.right();
        }
        else if(e.getKeyChar() == 'w') {
           controller.up(); 
        }
        else if(e.getKeyChar() == 's') {
           controller.down();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }  
    
    public void setTextInfoLabel(String text) {
        GameFrame.infoLabel.setText(text);
    }
    
    public void setTextLevelLabel(String text) {
        GameFrame.levelLabel.setText(text);
    }
    
    public void setController(GameController controller) {
        this.controller = controller;
    }
  
}
