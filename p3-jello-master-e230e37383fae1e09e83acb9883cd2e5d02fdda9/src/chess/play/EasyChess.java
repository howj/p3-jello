package chess.play;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import cse332.chess.server.Hub;

/**
 * This class will allow you to face other bots on the chess server.
 * You will (at the very least) need to use it to get the points for
 * beating clamps.  Though, it can also be very useful for debugging to
 * have your bot play on the server.
 */
public class EasyChess extends JFrame {

    private static final long serialVersionUID = -627544530172904783L;

    private Hub hub;

    private JButton buttonConnect;

    private JTextField text;

    private JPasswordField password;

    private GridLayout grid1;

    private JLabel user_label, pass_label;

    private JLabel status_bar;

    public EasyChess() {
        grid1 = new GridLayout(5, 2, 5, 5);
        setTitle("CSE 332 Chess!");
        Container c = getContentPane();

        c.setLayout(grid1);

        MyHandler handler = new MyHandler();

        user_label = new JLabel("User Name:");
        user_label.setToolTipText("Enter your User Name");
        c.add(user_label);

        text = new JTextField(10);
        c.add(text);
        text.addActionListener(handler);

        pass_label = new JLabel("Password:");
        pass_label.setToolTipText("Enter your Password");
        c.add(pass_label);
        password = new JPasswordField(20);
        c.add(password);
        password.addActionListener(handler);

        buttonConnect = new JButton("Connect!");
        c.add(buttonConnect);
        buttonConnect.addActionListener(handler);

        status_bar = new JLabel("Not Connected...");
        c.add(status_bar);

        hub = new Hub(this, null);

        setLocation(100, 100);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        validate();
        setVisible(true);
    }

    public void loginFailed(String msg) {
        status_bar.setText("Status: " + msg);
        buttonConnect.setEnabled(true);
    }
    
    public void loginSucceeded(String msg) {
        status_bar.setText("Status: " + msg);
        buttonConnect.setEnabled(false);
    }


    public static void main(String[] args) {
        System.setProperty("swing.aatext", "true");
        new EasyChess();
    }

    private class MyHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String u1 = text.getText();
            String p1 = new String(password.getPassword());

            buttonConnect.setEnabled(false);
            hub.login(u1, p1);
        }
    }
}
