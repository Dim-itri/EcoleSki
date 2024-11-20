package be.marain.jframes;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Index extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Index frame = new Index();
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
	public Index() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1365, 752);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle("Accueil");

		JLabel title = new JLabel("Ecole de ski");
		title.setFont(new Font("Tahoma", Font.BOLD, 23));
		title.setBounds(604, 11, 197, 27);
		contentPane.add(title);

		JButton displaySkiers = new JButton("Skieurs");
		displaySkiers.setFont(new Font("Tahoma", Font.BOLD, 17));
		displaySkiers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SkiersJFrame skiersJFrame = new SkiersJFrame();
				dispose();
				skiersJFrame.setVisible(true);
			}
		});

		displaySkiers.setBounds(66, 310, 155, 49);
		contentPane.add(displaySkiers);

		JButton displayInstructors = new JButton("Instructeurs");
		displayInstructors.setFont(new Font("Tahoma", Font.BOLD, 17));
		displayInstructors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InstructorsJFrame instructorsJFrame = new InstructorsJFrame();
				dispose();
				instructorsJFrame.setVisible(true);
			}
		});

		displayInstructors.setBounds(451, 310, 148, 49);
		contentPane.add(displayInstructors);

		JButton btnNewButton = new JButton("Leçons");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		btnNewButton.setBounds(808, 310, 125, 49);
		contentPane.add(btnNewButton);

		JButton displayBookings = new JButton("Réservations");
		displayBookings.setFont(new Font("Tahoma", Font.BOLD, 17));
		displayBookings.setBounds(1084, 313, 155, 49);
		contentPane.add(displayBookings);
	}
}