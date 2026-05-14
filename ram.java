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
}
