package admin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class AdminClient extends JFrame {

	JPanel panel = null;
	ArrayList<JPanel> answerPanels = null;
	
	public AdminClient(String host, int defaultPort)
	{	
		super("Admin Client");
	
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setPreferredSize(new Dimension(800,600));
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-panel.getPreferredSize().width/2, dim.height/2-panel.getPreferredSize().height/2);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		this.getContentPane().add(panel, BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
		
		addGuiComponents();
	}

	private void addGuiComponents() {
	
		// email component
		JPanel emailPanel = new JPanel();
		emailPanel.add(new JLabel("Email:"));
		JTextField email = new JTextField();
		email.setPreferredSize(new Dimension(300,25));
		emailPanel.add(email);
		panel.add(emailPanel);
		
		// question comp
		JPanel questionPanel = new JPanel();
		questionPanel.add(new JLabel("Question:"));
		JTextField question = new JTextField();
		question.setPreferredSize(new Dimension(300,25));
		questionPanel.add(question);
		panel.add(questionPanel);
		
		// answer comp
		JPanel answerPanel = new JPanel();
		answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.PAGE_AXIS));
		answerPanel.add(new JLabel("Answers:"));
		answerPanel.add(new JButton("Add"));
		
		answerPanel.add(createAnswerPanel(1));
		answerPanel.add(createAnswerPanel(2));
		
		panel.add(answerPanel);
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
		controlPanel.add(new JButton("Pause"));
		controlPanel.add(new JButton("Resume"));
		controlPanel.add(new JButton("Stop"));
		
		
		panel.add(controlPanel);
		
	}

	private JPanel createAnswerPanel(int id)
	{
		JPanel a = new JPanel();
		a.add(new JLabel("Answer " + id));
		JTextField answer = new JTextField();
		answer.setPreferredSize(new Dimension(300,25));
		a.add(answer);
		
		return a;
	}
	
}
