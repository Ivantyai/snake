package snake.model;

import sun.print.DialogOwner;

/**
 * Created by Ivan on 23.06.2015.
 */
public enum SnakeDirection {
    
    LEFT() {      
        public SnakeDirection opposite() {
            return RIGHT;
        }
    },
    RIGHT() {       
        public SnakeDirection opposite() {
            return LEFT;
        }
    },
    UP(){      
        public SnakeDirection opposite() {
            return DOWN;
        }        
    },
    DOWN() {       
        public SnakeDirection opposite() {
            return UP;
        }             
    };

    SnakeDirection() {
    }
    
    public abstract SnakeDirection opposite();
      
}
