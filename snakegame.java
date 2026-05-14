package SnakeGame;
import java.awt.Dimension;

import javax.swing.*;

public class snakegame extends JFrame{
     snakegame(){
        super("Snake Game");
        add(new ram());
        pack();
        int boardHeight = 250;
        int boardWidth = 750;
        setPreferredSize(new Dimension(boardWidth,boardHeight));

        setLocationRelativeTo(null);
        //setSize(300,300);
        setResizable(false);
        setVisible(true);
    }

    /*private void setPreferredSize(int boardWidth, int boardHeight) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPreferredSize'");
    }*/

    public static void main(String ashish[]){
        new snakegame();
    }



}