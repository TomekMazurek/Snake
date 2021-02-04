package com.github.mazurek.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

  static final int SCREEN_WIDTH = 600;
  static final int SCREEN_HEIGHT = 600;
  static final int UNIT_SIZE = 25;
  static final int GAME_UNITS = SCREEN_WIDTH * SCREEN_HEIGHT / UNIT_SIZE;
  // delay at the start of game
  static final int DELAY = 250;
  // ratio of speed increase
  static final int SPEED_INCREASE_WITH_LEVEL_CHANGE = 10;
  // increase the speed of moving snake by every another amount of apples eaten
  static final int LEVEL_CHANGE = 10;
  final int y[] = new int[GAME_UNITS];
  final int x[] = new int[GAME_UNITS];
  int bodyParts = 3;
  int applesEaten;
  int appleX;
  int appleY;
  char direction = 'R';
  char pressedDirection;
  boolean running = false;
  Timer timer;
  Random random;


  GamePanel() {
    random = new Random();
    this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
    this.setBackground(Color.BLACK);
    this.setFocusable(true);
    this.addKeyListener(new myKeyAdapter());
    startGame();
  }

  public void startGame() {
    newApple();
    running = true;
    timer = new Timer(DELAY, this);
    timer.start();
    move();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  public void draw(Graphics g) {
    if (running) {
      g.setColor(Color.RED);
      g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

      for (int i = 0; i < bodyParts; i++) {
        if (i == 0) {
          g.setColor(Color.green);
          g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        } else {
          g.setColor(Color.YELLOW);
          g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        }
      }
      g.setColor(Color.RED);
      g.setFont(new Font("Arial", Font.BOLD, 25));
      FontMetrics metrics = getFontMetrics(g.getFont());
      g.drawString(
          "Score: " + applesEaten,
          (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
          (SCREEN_HEIGHT - 20));

    } else {
      gameOver(g);
    }
  }

  public void newApple() {
    appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
    appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
  }

  public void move() {
    if (running) {
      for (int i = bodyParts; i > 0; i--) {
        x[i] = x[i - 1];
        y[i] = y[i - 1];
      }
      switch (direction) {
        case 'U':
          y[0] = y[0] - UNIT_SIZE;
          break;
        case 'D':
          y[0] = y[0] + UNIT_SIZE;
          break;
        case 'L':
          x[0] = x[0] - UNIT_SIZE;
          break;
        case 'R':
          x[0] = x[0] + UNIT_SIZE;
          break;
      }
    }
  }

  public void checkApple() {
    if ((x[0] == appleX) && (y[0] == appleY)) {
      bodyParts++;
      applesEaten++;
      newApple();
      increaseSpeed();
    }
  }

  public void checkCollisions() {
    // checks if head collides with body
    for (int i = bodyParts; i > 0; i--) {
      if ((x[0] == x[i]) && (y[0] == y[i])) {
        running = false;
        break;
      }
    }
    // checks if head does not touch the vertical edge of window
    if (x[0] < 0 || x[0] > SCREEN_WIDTH) {
      running = false;
    }
    // checks if head does not touch the horizontal edge of window
    if (y[0] < 0 || y[0] > SCREEN_HEIGHT) {
      running = false;
    }
  }

  public void gameOver(Graphics g) {
    String gameOverText = "Game Over";
    String gameOverScoreText = "Your score : " + applesEaten;

    g.setColor(Color.RED);
    g.setFont(new Font("Arial", Font.BOLD, 75));
    FontMetrics metrics = getFontMetrics(g.getFont());
    g.drawString(
        gameOverText,
        (SCREEN_WIDTH - metrics.stringWidth(gameOverText)) / 2,
        (SCREEN_HEIGHT / 2));
    g.setFont(new Font("Verdana", Font.BOLD, 30));
    metrics = getFontMetrics(g.getFont());
    g.drawString(
        gameOverScoreText,
        ((SCREEN_WIDTH - metrics.stringWidth(gameOverScoreText)) / 2),
        SCREEN_HEIGHT / 2 + 100);
  }

  public void increaseSpeed() {
    if (applesEaten % LEVEL_CHANGE == 0) {
      timer.setDelay(timer.getDelay() - SPEED_INCREASE_WITH_LEVEL_CHANGE);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (running) {
      move();
      checkApple();
      checkCollisions();
    }
    repaint();

  }

  public class myKeyAdapter extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {

      switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
          pressedDirection = 'L';
          if (direction != 'R') {
            direction = pressedDirection;
          }
          break;
        case KeyEvent.VK_RIGHT:
          pressedDirection = 'R';
          if (direction != 'L') {
            direction = pressedDirection;
          }
          break;
        case KeyEvent.VK_UP:
          pressedDirection = 'U';
          if (direction != 'D') {
            direction = pressedDirection;
          }
          break;
        case KeyEvent.VK_DOWN:
          pressedDirection = 'D';
          if (direction != 'U') {
            direction = pressedDirection;
          }
          break;
      }
    }
  }
}
