/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import static snakegame.Baglanti.connect;

public class Board extends JPanel implements ActionListener {

private final int Genislik = 300;
private final int Uzunluk = 300;
private final int NokBoy = 10;
private final int TümNoktalar = 900;
private final int RastgeleKon = 29;
private final int DELAY = 140;

    private final int x[] = new int[TümNoktalar];
    private final int y[] = new int[TümNoktalar];

    private int Noktalar;
    private int apple_x;
    private int apple_y;

    private boolean YönSol = false;
    private boolean YönSag = true;
    private boolean YönYukari = false;
    private boolean YönAsagi = false;
    private boolean inGame = true;

    public static int score =0;
    String kulad;
    
    MainMenu menu = new MainMenu();
    Baglanti b = new Baglanti();
    
    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;

    public Board() {
        kulad=JOptionPane.showInputDialog(this, "Kullanıcı Adı Giriniz");
        if(kulad==null){
            System.exit(0);
        }
        else    {
            initBoard();
        }       
    }
    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(Genislik, Uzunluk));
        loadImages();
        initGame();
    }

    private void loadImages() {

    ImageIcon resim = new ImageIcon("src/dot.png");
    ball = resim.getImage();

    ImageIcon elma = new ImageIcon("src/apple.png");
    apple = elma.getImage();

    ImageIcon kafa = new ImageIcon("src/head.png");
    head = kafa.getImage();
}

    private void initGame() {

    Noktalar = 3;

    for (int z = 0; z < Noktalar; z++) {
        x[z] = 50 - z * 10;
        y[z] = 50;
    }

    locateApple();

    timer = new Timer(DELAY, this);
    timer.start();
}

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        if (inGame) {

            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < Noktalar; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }        
    }

    private void gameOver(Graphics g) {
        
        Connection c = connect();
        
        
    try {
        b.st = (Statement)c.createStatement();
        //Statement st=(Statement)c.createStatement();
        b.st.executeUpdate("Insert into Skor(kullanıcı_adı, skor) values('" + kulad + "',"+score+')');
    } catch (SQLException ex) {
        Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
    }
        
        String msg = "Game Over";
        
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (Genislik - metr.stringWidth(msg)) / 2, Uzunluk/ 2);
        
                g.setColor(Color.white);
		g.setFont(small);
		g.drawString("Skor : "+score,125,20);
                
                g.dispose();
        menu.setVisible(true);

    }

    private void checkApple() {

    if ((x[0] == apple_x) && (y[0] == apple_y)) {

        score++;
        Noktalar++;
        locateApple();
    }
}

    private void move() {

        for (int z = Noktalar; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (YönSol) {
            x[0] -= NokBoy;
        }

        if (YönSag) {
            x[0] += NokBoy;
        }

        if (YönYukari) {
            y[0] -= NokBoy;
        }

        if (YönAsagi) {
            y[0] += NokBoy;
        }
    }

    private void checkCollision() {

        for (int z = Noktalar; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= Uzunluk) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= Genislik) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }
        
        if (!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {

        int r = (int) (Math.random() * RastgeleKon);
        apple_x = ((r * NokBoy));

        r = (int) (Math.random() * RastgeleKon);
        apple_y = ((r * NokBoy));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();
            
            

            if ((key == KeyEvent.VK_LEFT) && (!YönSag)) {
                YönSol = true;
                YönYukari = false;
                YönAsagi= false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!YönSol)) {
                YönSag = true;
                YönYukari = false;
                YönAsagi = false;
            }

            if ((key == KeyEvent.VK_UP) && (!YönAsagi)) {
                YönYukari = true;
                YönSag = false;
                YönSol = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!YönYukari)) {
                YönAsagi = true;
                YönSag = false;
                YönSol = false;
            }
        }
    }
}