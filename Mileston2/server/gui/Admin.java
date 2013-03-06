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

public class Admin extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Admin frame = new Admin();
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
	public Admin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 773, 621);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(5, 159, 0, 0);
		contentPane.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel lblPollserverV = new JLabel("PollServer v0.1 - Admin");
		lblPollserverV.setBounds(12, 13, 146, 16);
		contentPane.add(lblPollserverV);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.setBounds(12, 42, 97, 25);
		contentPane.add(btnCreate);
		
		JButton btnFetchPolls = new JButton("Stop");
		btnFetchPolls.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnFetchPolls.setBounds(12, 159, 97, 25);
		contentPane.add(btnFetchPolls);
		
		JButton btnStart = new JButton("Start");
		btnStart.setBounds(12, 90, 97, 25);
		contentPane.add(btnStart);
		
		JButton btnPause = new JButton("Pause");
		btnPause.setBounds(12, 128, 97, 25);
		contentPane.add(btnPause);
		
		JButton btnNewButton = new JButton("Results");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(12, 235, 97, 25);
		contentPane.add(btnNewButton);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(142, 42, 603, 22);
		contentPane.add(textArea);
		
		JButton btnPolls = new JButton("Polls");
		btnPolls.setBounds(12, 197, 97, 25);
		contentPane.add(btnPolls);
		
		TextArea textArea_1 = new TextArea();
		textArea_1.setBounds(142, 90, 603, 168);
		contentPane.add(textArea_1);
		
		JButton btnNewButton_1 = new JButton("Add Question");
		btnNewButton_1.setBounds(12, 273, 109, 25);
		contentPane.add(btnNewButton_1);
		
		JTextArea textArea_2 = new JTextArea();
		textArea_2.setBounds(142, 274, 603, 22);
		contentPane.add(textArea_2);
		
		JButton btnAddOption = new JButton("Add Option");
		btnAddOption.setBounds(12, 309, 109, 25);
		contentPane.add(btnAddOption);
		
		JTextArea textArea_3 = new JTextArea();
		textArea_3.setBounds(155, 417, 46, 22);
		contentPane.add(textArea_3);
		
		TextArea textArea_4 = new TextArea();
		textArea_4.setBounds(194, 310, 551, 92);
		contentPane.add(textArea_4);
		
		JButton button = new JButton("Modify Option");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button.setBounds(12, 416, 121, 25);
		contentPane.add(button);
		
		JTextArea textArea_5 = new JTextArea();
		textArea_5.setBounds(213, 417, 46, 22);
		contentPane.add(textArea_5);
		
		JTextArea textArea_6 = new JTextArea();
		textArea_6.setBounds(142, 309, 46, 22);
		contentPane.add(textArea_6);
		
		JLabel lblNewLabel = new JLabel("PollNo");
		lblNewLabel.setBounds(213, 446, 46, 16);
		contentPane.add(lblNewLabel);
		
		JLabel lblOption = new JLabel("Option");
		lblOption.setBounds(158, 446, 46, 16);
		contentPane.add(lblOption);
		
		JLabel label = new JLabel("Option");
		label.setBounds(142, 336, 46, 16);
		contentPane.add(label);
		
		TextArea textArea_7 = new TextArea();
		textArea_7.setBounds(265, 421, 480, 92);
		contentPane.add(textArea_7);
		
		JLabel lblDescription = new JLabel("Description");
		lblDescription.setBounds(265, 519, 71, 16);
		contentPane.add(lblDescription);
		
		JLabel lblOutput = new JLabel("Output");
		lblOutput.setBounds(378, 68, 71, 16);
		contentPane.add(lblOutput);
	}
}
