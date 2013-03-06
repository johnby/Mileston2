package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JMenuBar;
import javax.swing.JLabel;
//import com.jgoodies.forms.factories.DefaultComponentFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import java.awt.TextArea;

public class Client extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Client() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 636, 360);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(5, 159, 0, 0);
		contentPane.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel lblPollserverV = new JLabel("PollServer v0.1 - Client");
		lblPollserverV.setBounds(12, 13, 146, 16);
		contentPane.add(lblPollserverV);
		
		JButton btnFetchPolls = new JButton("Register (Optional)");
		btnFetchPolls.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnFetchPolls.setBounds(5, 42, 141, 25);
		contentPane.add(btnFetchPolls);
		
		JButton btnNewButton = new JButton("Send");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(12, 259, 97, 25);
		contentPane.add(btnNewButton);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(157, 42, 449, 22);
		contentPane.add(textArea);
		
		JButton btnPolls = new JButton("Polls");
		btnPolls.setBounds(5, 90, 97, 25);
		contentPane.add(btnPolls);
		
		TextArea textArea_1 = new TextArea();
		textArea_1.setBounds(157, 90, 449, 134);
		contentPane.add(textArea_1);
		
		JButton btnDetails = new JButton("Details");
		btnDetails.setBounds(5, 166, 97, 25);
		contentPane.add(btnDetails);
		
		JLabel label = new JLabel("Output");
		label.setBounds(341, 68, 71, 16);
		contentPane.add(label);
		
		JTextArea textArea_2 = new JTextArea();
		textArea_2.setBounds(128, 260, 76, 22);
		contentPane.add(textArea_2);
		
		JTextArea textArea_3 = new JTextArea();
		textArea_3.setBounds(229, 260, 76, 22);
		contentPane.add(textArea_3);
		
		JLabel lblPollno = new JLabel("PollNo");
		lblPollno.setBounds(128, 241, 71, 16);
		contentPane.add(lblPollno);
		
		JLabel lblOption = new JLabel("Option");
		lblOption.setBounds(229, 241, 71, 16);
		contentPane.add(lblOption);
	}
}
