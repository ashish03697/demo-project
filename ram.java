package SnakeGame;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ram extends JPanel implements ActionListener{

    private Image apple;
    private Image dot;
    private Image head;

    private int apple_x;
    private int apple_y;
    private Timer timer;

    private final int ALL_DOTS = 900;
    private final int DOTS_SIZE = 10;
    private final int RANDOM_POSITION = 29;

    private int dots;
    private final int y[] = new int [ALL_DOTS];
    private final int x[] = new int [ALL_DOTS];
    private boolean inGame=true;

    private boolean leftDirection=false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = true;

    ram(){
        addKeyListener(new TAdapter());
        setPreferredSize(new Dimension(300,300));

        setBackground(Color.black);
        setFocusable(true);

        loadImage();
        initGame();

    }

    public void loadImage(){
        ImageIcon I1= new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/apple.png"));
        apple = I1.getImage();

        ImageIcon I2 = new ImageIcon(ClassLoader.getSystemResource("icons/dot.png"));
        dot = I2.getImage();

        ImageIcon I3 = new ImageIcon(ClassLoader.getSystemResource("Icons/head.png"));
        head = I3.getImage();

    }

    public void initGame(){
        dots = 3;
        for(int i=0;i<dots;i++){
            y[i]=50;
            x[i]=50-i*DOTS_SIZE;
        }
        locateApple();
        timer = new Timer(140,this);
        timer.start();
    }

    public void locateApple(){
        int r = (int)(Math.random()*RANDOM_POSITION);
        apple_x = r*DOTS_SIZE;

        r=(int)(Math.random()*RANDOM_POSITION);
        apple_y = r*DOTS_SIZE;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        drow(g);
    }
    
    public void drow(Graphics g){
        if(inGame){
            g.drawImage(apple, apple_x, apple_y, this);

            for(int i=0;i<dots;i++){
                if(i==0){
                    g.drawImage(head, x[i], y[i], this);
                }else{
                    g.drawImage(dot, x[i], y[i], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();
        }else{
            gameOver(g);
        }
     }

     public void gameOver(Graphics g){
        String msg = "Game Over!";
        Font font = new Font("SAN_SERLT",Font.BOLD,29);
        FontMetrics metrics = getFontMetrics(font);
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(msg, (300-metrics.stringWidth(msg))/2, 300/2);
     }

    public void checkApple(){
        if(x[0]==apple_x && y[0]==apple_y){
            dots++;
            locateApple();
        }
    }

    public void move(){
        for(int i=dots;i>0;i--){
            x[i] = x[i-1];
            y[i]=y[i-1];
        }
         if(leftDirection){
            x[0]=x[0]-DOTS_SIZE;
        }
        if(rightDirection){
            x[0]=x[0]+DOTS_SIZE;
        }
        if(upDirection){
            y[0]=y[0]-DOTS_SIZE;
        } 
        if(downDirection){
            y[0]=y[0]+DOTS_SIZE;
        }
       // x[0]+=DOTS_SIZE;
       // y[0]+=DOTS_SIZE;

    }
    public void checkCollision(){
        for(int i=dots;i>0;i--){
            if((i>4)&&(x[0]==x[i])&&(y[0]==y[i])){
                inGame=false;
            }
        }
        if(y[0]>=300){
            inGame = false;
        }
        if(x[0]>=300){
            inGame=false;
        }
        if(x[0]<=0){
            inGame=false;
        }
        if(y[0]<=0){
            inGame=false;
        }
        if(!inGame){
            timer.stop();
        }
    }

    public void actionPerformed(ActionEvent ae){
        if(inGame){
            checkApple();
            checkCollision();
            move();
        }
       repaint();

        
    }
    public class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            int key=e.getKeyCode();

            if(key==KeyEvent.VK_LEFT && (!rightDirection)){
                leftDirection=true;
                upDirection =false;
                downDirection = false;
            }
            if(key==KeyEvent.VK_RIGHT && (!leftDirection)){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key==KeyEvent.VK_UP && (!downDirection)){
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            if(key==KeyEvent.VK_DOWN && (!upDirection)){
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }
}/* 
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class ram extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 750;
    int boardHeight = 250;

    //images
    Image dinosaurImg;
    Image dinosaurDeadImg;
    Image dinosaurJumpImg;
    Image cactus1Img;
    Image cactus2Img;
    Image cactus3Img;

    class Block {
        int x;
        int y;
        int width;
        int height;
        Image img;

        Block(int x, int y, int width, int height, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    //dinosaur
    int dinosaurWidth = 88;
    int dinosaurHeight = 94;
    int dinosaurX = 50;
    int dinosaurY = boardHeight - dinosaurHeight;

    Block dinosaur;

    //cactus
    int cactus1Width = 34;
    int cactus2Width = 69;
    int cactus3Width = 102;

    int cactusHeight = 70;
    int cactusX = 700;
    int cactusY = boardHeight - cactusHeight;
    ArrayList<Block> cactusArray;

    //physics
    int velocityX = -12; //cactus moving left speed
    int velocityY = 0; //dinosaur jump speed
    int gravity = 1;

    boolean gameOver = false;
    int score = 0;

    Timer gameLoop;
    Timer placeCactusTimer;

    public ram() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.lightGray);
        setFocusable(true);
        addKeyListener(this);

        dinosaurImg = new ImageIcon(getClass().getResource("./img/dino-run.gif")).getImage();
        dinosaurDeadImg = new ImageIcon(getClass().getResource("./img/dino-dead.png")).getImage();
        dinosaurJumpImg = new ImageIcon(getClass().getResource("./img/dino-jump.png")).getImage();
        cactus1Img = new ImageIcon(getClass().getResource("./img/cactus1.png")).getImage();
        cactus2Img = new ImageIcon(getClass().getResource("./img/cactus2.png")).getImage();
        cactus3Img = new ImageIcon(getClass().getResource("./img/cactus3.png")).getImage();

        //dinosaur
        dinosaur = new Block(dinosaurX, dinosaurY, dinosaurWidth, dinosaurHeight, dinosaurImg);
        //cactus
        cactusArray = new ArrayList<Block>();

        //game timer
        gameLoop = new Timer(1000/60, this); //1000/60 = 60 frames per 1000ms (1s), update
        gameLoop.start();

        //place cactus timer
        placeCactusTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeCactus();
            }
        });
        placeCactusTimer.start();
    }

    void placeCactus() {
        if (gameOver) {
            return;
        }

        double placeCactusChance = Math.random(); //0 - 0.999999
        if (placeCactusChance > .90) { //10% you get cactus3
            Block cactus = new Block(cactusX, cactusY, cactus3Width, cactusHeight, cactus3Img);
            cactusArray.add(cactus);
        }
        else if (placeCactusChance > .70) { //20% you get cactus2
            Block cactus = new Block(cactusX, cactusY, cactus2Width, cactusHeight, cactus2Img);
            cactusArray.add(cactus);
        }
        else if (placeCactusChance > .50) { //20% you get cactus1
            Block cactus = new Block(cactusX, cactusY, cactus1Width, cactusHeight, cactus1Img);
            cactusArray.add(cactus);
        }

        if (cactusArray.size() > 10) {
            cactusArray.remove(0); //remove the first cactus from ArrayList
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //dinosaur
        g.drawImage(dinosaur.img, dinosaur.x, dinosaur.y, dinosaur.width, dinosaur.height, null);

        //cactus
        for (int i = 0; i < cactusArray.size(); i++) {
            Block cactus = cactusArray.get(i);
            g.drawImage(cactus.img, cactus.x, cactus.y, cactus.width, cactus.height, null);
        }

        //score
        g.setColor(Color.black);
        g.setFont(new Font("Courier", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), 10, 35);
        }
        else {
            g.drawString(String.valueOf(score), 10, 35);
        }
    }

    public void move() {
        //dinosaur
        velocityY += gravity;
        dinosaur.y += velocityY;

        if (dinosaur.y > dinosaurY) { //stop the dinosaur from falling past the ground
            dinosaur.y = dinosaurY;
            velocityY = 0;
            dinosaur.img = dinosaurImg;
        }

        //cactus
        for (int i = 0; i < cactusArray.size(); i++) {
            Block cactus = cactusArray.get(i);
            cactus.x += velocityX;

            if (collision(dinosaur, cactus)) {
                gameOver = true;
                dinosaur.img = dinosaurDeadImg;
            }
        }

        //score
        score++;
    }

    boolean collision(Block a, Block b) {
        return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
               a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
               a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
               a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placeCactusTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // System.out.println("JUMP!");
            if (dinosaur.y == dinosaurY) {
                velocityY = -17;
                dinosaur.img = dinosaurJumpImg;
            }
            
            if (gameOver) {
                //restart game by resetting conditions
                dinosaur.y = dinosaurY;
                dinosaur.img = dinosaurImg;
                velocityY = 0;
                cactusArray.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placeCactusTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
*/