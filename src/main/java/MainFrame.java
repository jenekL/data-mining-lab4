/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * @author Jenya
 */
public class MainFrame extends JFrame {

    private JTextPane textPane;
    private JButton educationButton;
    private Handler handler;
    private JPanel panel;
    private JLabel eEdLabel = new JLabel("Ошибка обучения = ");
    private JLabel eTestLabel = new JLabel("Ошибка тестирования = ");
    private GraphPanel gp = new GraphPanel(null, null, null, null, null, null,
            null, null, null, null, null);

    public MainFrame(Handler handlers) {
        this.handler = handlers;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Лабораторная № 4");

        educationButton = new JButton("ОБУЧИТЬ ЕЩЕ ЛУЧШЕ");
        educationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final ProgressDialog progress = new ProgressDialog();
                Thread testingThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        handler.study();
                        progress.closeDialog();
                        revalidate();
                    }
                });
                testingThread.start();

                try {
                    testingThread.join();
                    panel.removeAll();
                    panel.add(new GraphPanel(handler.getEducationPointsToGraph()[0],
                            handler.getEducationPointsToGraph()[1],
                            handler.getEducationPointsToGraph()[2],
                            handler.getEducationPointsToGraph()[3], Color.BLUE, Color.RED,
                            handler.getPointsToGraph()[0],
                            handler.getPointsToGraph()[1],
                            handler.getPointsToGraph()[2],
                            handler.getPointsToGraph()[3], Color.GREEN));

                    eTestLabel.setText("Ошибка тест = " + handler.geteTest());
                    eEdLabel.setText("Ошибка обуч = " + handler.geteEd());

                    revalidate();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(educationButton);
        buttonPanel.add(eEdLabel);
        buttonPanel.add(eTestLabel);
        buttonPanel.setPreferredSize(new Dimension(600, 50));
        buttonPanel.setMinimumSize(new Dimension(600, 50));
        buttonPanel.setMaximumSize(new Dimension(600, 50));

        textPane = new JTextPane();
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        getContentPane().add(buttonPanel);
        panel = new JPanel();
        panel.add(gp);
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

        getContentPane().add(panel);
        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        final ProgressDialog progress = new ProgressDialog();
        Thread testingThread = new Thread(new Runnable() {
            @Override
            public void run() {

                handler.study();
                progress.closeDialog();
                revalidate();
            }
        });
        testingThread.start();

        try {
            testingThread.join();
            panel.removeAll();
            panel.add(new GraphPanel(handler.getEducationPointsToGraph()[0],
                    handler.getEducationPointsToGraph()[1],
                    handler.getEducationPointsToGraph()[2],
                    handler.getEducationPointsToGraph()[3], Color.BLUE, Color.RED,
                    handler.getPointsToGraph()[0],
                    handler.getPointsToGraph()[1],
                    handler.getPointsToGraph()[2],
                    handler.getPointsToGraph()[3], Color.GREEN));

            eTestLabel.setText("Ошибка тест = " + handler.geteTest());
            eEdLabel.setText("Ошибка обуч = " + handler.geteEd());

            revalidate();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
