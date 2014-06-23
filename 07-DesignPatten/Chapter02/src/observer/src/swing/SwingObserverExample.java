package swing;    
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
    
public class SwingObserverExample {
    JFrame mFrame;
    
    public static void main(String[] args) {
        SwingObserverExample example = new SwingObserverExample();
        example.go();
    }
    
    public void go() {
        mFrame = new JFrame();

        JButton button = new JButton("Should I do it?");
        button.addActionListener(new AngelListener());
        button.addActionListener(new DevilListener());
        mFrame.getContentPane().add(BorderLayout.CENTER, button);

        // Set frame properties 
        mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mFrame.getContentPane().add(BorderLayout.CENTER, button);
        mFrame.setSize(300,300);
        mFrame.setVisible(true);
    }
    
    class AngelListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            System.out.println("Don't do it, you might regret it!");
        }
    }

    class DevilListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            System.out.println("Come on, do it!");
        }
    }
}
