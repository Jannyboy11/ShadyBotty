package main;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.UIManager;

public class Frame extends JFrame {

	private JPanel contentPane;
	private static JLabel a;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try {
			ShadyBottyMain.main(args);
		} catch (Exception e) {
			a.setText("could not connect :(");
		}
	}

	/**
	 * Create the frame.
	 */
	public Frame() {
		setTitle("ShadyBOTTEH");
		setFont(UIManager.getFont("InternalFrame.titleFont"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 346, 147);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		a = new JLabel("Botty is now running :D");
		setContentPane(contentPane);
		contentPane.add(a,BorderLayout.NORTH);
		
		JButton btnNewButton = new JButton("Your Shutdown button is in another Castle Kappa");
		btnNewButton.setEnabled(false);
		contentPane.add(btnNewButton, BorderLayout.SOUTH);
	}

}
