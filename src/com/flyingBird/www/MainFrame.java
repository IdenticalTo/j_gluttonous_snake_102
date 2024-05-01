package com.flyingBird.www;

import com.sun.xml.internal.ws.handler.HandlerException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class MainFrame extends JFrame {
    private JPanel jPanel;
    private Snake snake;
    private Node food;
    private Timer timer;
    private JButton startButton = null;
    private static int count = 0;

    private JPanel eastPanel;
    private Timer userTimer;
    private int score = 0; // 玩家得分
    private long startTime; // 游戏开始时间
    private JLabel timeLabel; // 显示游戏时间的标签
    private JLabel scoreLabel; // 显示得分的标签

    public MainFrame() throws HandlerException {
        initFrame();
        initSnake();
        initFood();
        initPanel();
        initEastPanel();
        initTimeAndScoreLabels();
        initStartButton();
    }

    private void initUseTimer() {
        // ... 现有的定时器初始化代码 ...
        userTimer = new Timer();

        // 记录游戏开始时间
        startTime = System.currentTimeMillis();

        // 添加一个任务来更新时间标签
        TimerTask updateTimeTask = new TimerTask() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = (currentTime - startTime) / 1000; // 转换为秒
                int seconds = (int) (elapsedTime % 60);
                int minutes = (int) (elapsedTime / 60);
                String timeString = String.format("%02d:%02d", minutes, seconds);
                timeLabel.setText("Time: " + timeString);
            }
        };

        // 使用另一个定时器来定期更新时间，例如每秒更新一次
        userTimer.scheduleAtFixedRate(updateTimeTask, 1000, 1000);
    }

    private void initTimeAndScoreLabels() {
        timeLabel = new JLabel("Time: 00:00");
        timeLabel.setFont(new Font("宋体", 1, 20));
        // 上、左、下、右
        timeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 150));
        timeLabel.setForeground(new Color(255, 255, 255, 255));
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("宋体", 1, 20));
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 150));
        scoreLabel.setForeground(new Color(255, 255, 255, 255));
        // 将标签添加到面板上，并设置位置和大小等属性
        eastPanel.add(timeLabel);
        eastPanel.add(scoreLabel);
        add(eastPanel, BorderLayout.EAST);
        // ... 设置标签的布局和其他属性
    }

    private void initStartButton() {
        startButton = new JButton("开始游戏");
        startButton.setBackground(new Color(255, 255, 255, 255));
        startButton.setFont(new Font("宋体", Font.PLAIN, 20));
        startButton.setForeground(new Color(59, 59, 59, 255));
        startButton.setVisible(true);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initUseTimer();
                startButton.setVisible(false);
                count++;
                if (count > 1) {
                    initSnake();
                    initFood();
                    score = 0; // 得分重新初始化
                    scoreLabel.setText("Score: " + score); // 更新得分标签
                    timeLabel.setText("Time: 00:00");
                }
                initTimer();
                setkeyListener();
            }
        });
        eastPanel.add(startButton);
    }

    private void initEastPanel() {
        eastPanel = new JPanel(); // 创建一个新的JPanel作为WEST区域的容器
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS)); // 设置BoxLayout以便在垂直方向上堆叠标签
        eastPanel.setBackground(new Color(59, 59, 59, 255));
        eastPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
    }

    private void setkeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if(snake.getDirection() != Direction.DOWN){
                            snake.setDirection(Direction.UP);
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if(snake.getDirection() != Direction.UP){
                            snake.setDirection(Direction.DOWN);
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        if(snake.getDirection() != Direction.RIGHT){
                            snake.setDirection(Direction.LEFT);
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if(snake.getDirection() != Direction.LEFT){
                            snake.setDirection(Direction.RIGHT);
                        }
                        break;
                }
            }
        });
    }

    private void initTimer() {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                snake.move();
                // 判断是否吃到食物
                Node head = snake.getBody().getFirst();
                if (head.getX() == food.getX() && head.getY() == food.getY()) {
                    snake.eat(food);
                    food.random();
                    score++; // 增加得分
                    scoreLabel.setText("Score: " + score); // 更新得分标签
                }
                jPanel.repaint();
                // 判断是否蛇是否死掉了
                System.out.println(snake.isLiving());
                if (!snake.isLiving()) {
                    startButton.setVisible(true);
                    timer.cancel();
                    userTimer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 100);
    }

    private void initFood() {
        food = new Node();
        food.random();
    }

    private void initSnake() {
        snake = new Snake();
    }

    private void initPanel() {
        jPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // 调用父类的paintComponent方法
                g.clearRect(0,0,600,600);
                for (int i = 0; i <= 40; i++) {
                    g.drawLine(0, i*15, 600, i*15);
                }
                for (int j = 0; j <= 40; j++) {
                    g.drawLine(j*15, 0, j*15, 600);
                }
                LinkedList<Node> body = snake.getBody();
                for (Node node:body) {
                    g.fillRect(node.getX()*15, node.getY()*15, 15,15);
                }
                g.fillRect(food.getX()*15, food.getY()*15,15,15);
            }
        };
        add(jPanel, BorderLayout.CENTER);
    }

    private void initFrame() {
        // 0 0 610 640 蛇和食物运动区域
        // 640 0 90 640 计分与计时面板
        setSize(910, 640);
        setLocation(10, 10);
        setLayout(new BorderLayout());
        setTitle("贪吃蛇");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);
    }

    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }
}
