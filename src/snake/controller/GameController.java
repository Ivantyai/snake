package snake.controller;

import snake.model.Game;

/**
 *
 * @author Ivan
 */
public class GameController {
    
    private Game game;
    
    public GameController(Game game) {
        this.game = game;
    }
    
    public void start() {
        game.start();
    }
    
    public void stop() {
        game.stop();
    }
        
    public void resume() {
        game.resume();
    }
    
    public void pause() {
        game.pause();
    }
    
    public void up() {
        game.up();
    }
    
    public void down() {
        game.down();
    }
    
    public void left() {
        game.left();
    }
    
    public void right() {
        game.right();
    }
}
