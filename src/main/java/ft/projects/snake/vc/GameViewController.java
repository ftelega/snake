package ft.projects.snake.vc;

import ft.projects.snake.model.Block;
import ft.projects.snake.util.Direction;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ft.projects.snake.util.Constants.*;

public class GameViewController {

    //misc
    private static final Random random = new Random();

    //ui properties
    private final VBox root = new VBox();
    private final Canvas canvas = new Canvas(GAME_WIDTH, GAME_HEIGHT);
    private final GraphicsContext gc = canvas.getGraphicsContext2D();
    private final Text text = new Text();

    //game properties
    private List<Block> snake = new ArrayList<>();
    private Block apple;
    private Direction currentDirection;
    private int score = 0;
    private boolean isPaused = true;
    private boolean isGameOver;

    public GameViewController() {
        text.setFont(Font.font(48));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(
                text,
                canvas
        );
    }

    public VBox getRoot() {
        return root;
    }

    public void startGame() {
        initApple();
        initSnakeHead();
        new Thread(() -> {
            while(true) {
                if(!isGameOver) {
                    clear();
                    drawBoard();
                    drawSnake();
                    drawApple();
                    if(!isPaused) {
                        checkApple();
                        moveSnake();
                        checkHitBorder();
                        checkHitSelf();
                    }
                }
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void checkApple() {
        var snakeHead = snake.get(0);
        if(snakeHead.getX() == apple.getX() && snakeHead.getY() == apple.getY()) {
            snake.add(new Block(-1, -1));
            initApple();
            ++score;
            setScoreText();
        }
    }

    private void checkHitBorder() {
        var snakeHead = snake.get(0);
        if(snakeHead.getX() >= GAME_WIDTH || snakeHead.getX() < 0 || snakeHead.getY() >= GAME_HEIGHT || snakeHead.getY() < 0)  {
            isGameOver = true;
            setGameOverText();
        }
    }

    private void checkHitSelf() {
        for(int i = 0; i<snake.size(); i++) {
            for(int j = 0; j<snake.size(); j++) {
                if(i == j) continue;
                var seg1 = snake.get(i);
                var seg2 = snake.get(j);
                if(seg1.getX() == seg2.getX() && seg1.getY() == seg2.getY()) {
                    isGameOver = true;
                    setGameOverText();
                    return;
                }
            }
        }
    }

    private void moveSnake() {
        for(int i = snake.size() - 1; i>0; i--) {
            var seg1 = snake.get(i);
            var seg2 = snake.get(i - 1);
            seg1.setX(seg2.getX());
            seg1.setY(seg2.getY());
        }
        var snakeHead = snake.get(0);
        switch(currentDirection) {
            case LEFT -> snakeHead.setX(snakeHead.getX() - SNAKE_HORIZONTAL_SPEED);
            case DOWN -> snakeHead.setY(snakeHead.getY() + SNAKE_VERTICAL_SPEED);
            case UP -> snakeHead.setY(snakeHead.getY() - SNAKE_VERTICAL_SPEED);
            case RIGHT -> snakeHead.setX(snakeHead.getX() + SNAKE_HORIZONTAL_SPEED);
        }
    }

    private void clear() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
    }

    private void drawBoard() {
        gc.setFill(Color.BLACK);
        for(int i = 0; i<=GAME_WIDTH; i+=BLOCK_WIDTH) {
            gc.strokeLine(i, 0, i, GAME_HEIGHT);
        }
        for(int i = 0; i<=GAME_HEIGHT; i+=BLOCK_HEIGHT) {
            gc.strokeLine(0, i, GAME_WIDTH, i);
        }
    }

    private void drawSnake() {
        gc.setFill(Color.GREEN);
        for(Block segment : snake) {
            gc.fillRect(segment.getX(), segment.getY(), BLOCK_WIDTH, BLOCK_HEIGHT);
        }
    }

    private void drawApple() {
        gc.setFill(Color.RED);
        gc.fillRect(apple.getX(), apple.getY(), BLOCK_WIDTH, BLOCK_HEIGHT);
    }

    private void initSnakeHead() {
        int xBound = GAME_WIDTH / BLOCK_WIDTH;
        int yBound = GAME_HEIGHT / BLOCK_HEIGHT;
        int randomX = random.nextInt(1, xBound - 1) * BLOCK_WIDTH;
        int randomY = random.nextInt(1, yBound - 1) * BLOCK_HEIGHT;
        snake.add(new Block(randomX, randomY));
    }

    private void initApple() {
        int xBound = GAME_WIDTH / BLOCK_WIDTH;
        int yBound = GAME_HEIGHT / BLOCK_HEIGHT;
        int randomX = random.nextInt(0, xBound) * BLOCK_WIDTH;
        int randomY = random.nextInt(0, yBound) * BLOCK_HEIGHT;
        apple = new Block(randomX, randomY);
    }

    private void setScoreText() {
        text.setText(String.format("Current score: %s", score));
    }

    private void setPauseText() {
        text.setText("PAUSE");
    }

    private void setGameOverText() {
        text.setText("GAME OVER");
    }

    //inner listener
    public class GameViewListener implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent keyEvent) {
            if(isGameOver) {
                isGameOver = false;
                isPaused = true;
                snake = new ArrayList<>();
                initSnakeHead();
                initApple();
                currentDirection = null;
                score = 0;
                text.setText("");
            }
            final int code = keyEvent.getCode().getCode();
            switch(code) {
                case A -> {
                    if(currentDirection != Direction.RIGHT) {
                        currentDirection = Direction.LEFT;
                        isPaused = false;
                        setScoreText();
                    }
                }
                case S -> {
                    if(currentDirection != Direction.UP) {
                        currentDirection = Direction.DOWN;
                        isPaused = false;
                        setScoreText();
                    }
                }
                case W -> {
                    if(currentDirection != Direction.DOWN) {
                        currentDirection = Direction.UP;
                        isPaused = false;
                        setScoreText();
                    }
                }
                case D -> {
                    if(currentDirection != Direction.LEFT) {
                        currentDirection = Direction.RIGHT;
                        isPaused = false;
                        setScoreText();
                    }
                }
                case ESC -> {
                    isPaused = true;
                    setPauseText();
                }
            }
        }
    }
}
