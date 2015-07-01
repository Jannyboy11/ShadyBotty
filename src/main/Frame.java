package main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.UIManager;

public class Frame extends JFrame {

	private JPanel contentPane;
	private static JLabel a;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
				
						
					Frame frame = new Frame();
					frame.setVisible(true);
					if (args.length > 0 && args[0].equals("minimized"))
						 frame.setState(java.awt.Frame.ICONIFIED);
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
		addWindowListener(new WindowListener() {



			@Override
			public void windowClosing(WindowEvent e) {
				Database.storeUsers();
				Database.getCurrencies().put("lastOn", "lastOn",System.currentTimeMillis());
				Database.storeCurrencies();
				Database.storeAutoreplies();
				
				System.exit(0);
			}

			@Override
			public void windowClosed(WindowEvent e) {
				Database.storeUsers();
				Database.storeCurrencies();
				Database.storeAutoreplies();			
			}
			@Override public void windowIconified(WindowEvent e) {}
			@Override public void windowDeiconified(WindowEvent e) {}
			@Override public void windowActivated(WindowEvent e) {}
			@Override public void windowDeactivated(WindowEvent e) {}
			@Override public void windowOpened(WindowEvent e) {}
			
		});
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
