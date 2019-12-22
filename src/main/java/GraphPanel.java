/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author Jenya
 */
public class GraphPanel extends JPanel {
    
    public GraphPanel(final double[] X, final double[] Y, final double[] Xn, final double[] Yn,
                      final Color mainColor, final Color testColor,
                      final double[] tX, final double[] tY, final double[] tXn, final double[] tYn,
                      final Color tTestColor) {
        
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(new JPanel() {
            {
                setPreferredSize(new Dimension(500, 500));
            }
            
            @Override
            public void paint(Graphics g) {
                int WIDTH = getWidth();
                int HEIGHT = getHeight();
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, new Color(250, 250, 250), 0, HEIGHT, new Color(238, 238, 238)));
                g2d.fillRect(0, 0, WIDTH, HEIGHT);
                
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                if (X != null) {
                    double minX = 1000, maxX = -1000, minY = 1000, maxY = -1000;
                    for (int i = 0; i < X.length; i++) {
                        
                        if (X[i] > maxX) {
                            maxX = X[i];
                        }
                        if (X[i] < minX) {
                            minX = X[i];
                        }
                        if (Y[i] > maxY) {
                            maxY = Y[i];
                        }
                        if (Y[i] < minY) {
                            minY = Y[i];
                        } 
                    }
                    for(int i = 0; i < Xn.length; i++){
                        if (Xn[i] > maxX) {
                            maxX = Xn[i];
                        }
                        if (Xn[i] < minX) {
                            minX = Xn[i];
                        }
                        if (Yn[i] > maxY) {
                            maxY = Yn[i];
                        }
                        if (Yn[i] < minY) {
                            minY = Yn[i];
                        }
                    }
                    for (int i = 0; i < tX.length; i++) {

                        if (tX[i] > maxX) {
                            maxX = tX[i];
                        }
                        if (tX[i] < minX) {
                            minX = tX[i];
                        }
                        if (tY[i] > maxY) {
                            maxY = tY[i];
                        }
                        if (tY[i] < minY) {
                            minY = tY[i];
                        }
                    }
                    for(int i = 0; i < tXn.length; i++){
                        if (tXn[i] > maxX) {
                            maxX = tXn[i];
                        }
                        if (tXn[i] < minX) {
                            minX = tXn[i];
                        }
                        if (tYn[i] > maxY) {
                            maxY = tYn[i];
                        }
                        if (tYn[i] < minY) {
                            minY = tYn[i];
                        }
                    }
                    double d = 1;
                    minX *= d;
                    maxX *= d;
                    minY *= d;
                    maxY *= d;
                    //if(minX > 0) minX = 0;
                    double m_x = (double) (WIDTH - 15) / (maxX - minX);
                    double m_y = (double) (HEIGHT - 25) / (maxY - minY);
                    g2d.setColor(Color.BLACK);
                    int s_x = (int) ((0 - minX) * m_x);
                    int s_y = (int) (maxY * m_y);
                    g2d.drawLine(s_x, 0, s_x, HEIGHT);
                    g2d.drawLine(0, s_y, WIDTH, s_y);
                    g2d.setColor(Color.GRAY);
                    double d_x = (maxX - minX) / 20;
                    for (double i = minX; i <= maxX; i += d_x) {
                        int x = (int) ((i - minX) * m_x);
                        g2d.drawLine(x, s_y + 5, x, s_y - 5);
                        g2d.drawString(Double.toString((double) ((int) (i * 100)) / 100), x, s_y + 15);
                    }
                    double d_y = (maxY - minY) / 20;
                    for (double i = minY; i <= maxY; i += d_y) {
                        int y = (int) ((maxY - i) * m_y);
                        g2d.drawLine(s_x + 5, y, s_x - 5, y);
                        g2d.drawString(Double.toString((double) ((int) (i * 100)) / 100), s_x + 15, y);
                    }
                    g2d.setColor(mainColor);
                    for (int j = 0; j < X.length - 1; j++) {
                        drawLine(X[j], Y[j], X[j + 1], Y[j + 1], g2d, m_x, m_y, minX, maxY);
                    }
                    for (int j = 0; j < tX.length - 1; j++) {
                        drawLine(tX[j], tY[j], tX[j + 1], tY[j + 1], g2d, m_x, m_y, minX, maxY);
                    }
                    g2d.setColor(testColor);
                    for (int j = 0; j < Xn.length - 1; j++) {
                        drawLine(Xn[j], Yn[j], Xn[j + 1], Yn[j + 1], g2d, m_x, m_y, minX, maxY);
                    }


                    g2d.setColor(tTestColor);
                    for (int j = 0; j < tXn.length - 1; j++) {
                        drawLine(tXn[j], tYn[j], tXn[j + 1], tYn[j + 1], g2d, m_x, m_y, minX, maxY);
                    }
                }
                
            }
            
            private void drawLine(double X, double Y, double X1, double Y1, Graphics2D g, double m_x, double m_y, double minX, double maxY) {
                g.fillOval((int) ((X - minX) * m_x), (int) ((maxY - Y) * m_y), 5, 5);
                g.drawLine((int) ((X - minX) * m_x), (int) ((maxY - Y) * m_y), (int) ((X1 - minX) * m_x), (int) ((maxY - Y1) * m_y));
            }
        });
        setSize(new Dimension(500, 700));
        setVisible(true);
    }
}
