package be.marain.jframes;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Index extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JButton displaySkiers;
	JButton displayInstructors;
	JButton btnNewButton;
	JButton displayBookings;

	
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
	
	public static void createHomeButton(JPanel tablePanel) {
		JButton btnHome = new JButton("Accueil");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Index index = new Index();
				((JFrame) SwingUtilities.getWindowAncestor(tablePanel)).dispose();
				index.setVisible(true);
			}
		});
		btnHome.setBounds(10, 696, 89, 23);
		tablePanel.add(btnHome);
	}

	public void handleSkierButton() {
		displaySkiers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SkiersJFrame skiersJFrame = new SkiersJFrame();
				dispose();
				skiersJFrame.setVisible(true);
			}
		});
	}
	
	public void handleInstructorButton() {
		displayInstructors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InstructorsJFrame instructorsJFrame = new InstructorsJFrame();
				dispose();
				instructorsJFrame.setVisible(true);
			}
		});
	}
	
	public void handleLessonButton() {
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LessonsJFrame lessonsJFrame = new LessonsJFrame();
				dispose();
				lessonsJFrame.setVisible(true);
			}
		});
	}

	public void handleBookingButton() {
		displayBookings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BookingsJFrame bookingsJFrame = new BookingsJFrame();
				bookingsJFrame.setVisible(true);
				dispose();
			}
		});
	}
	
	public void initializeComponents() {
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

		displaySkiers = new JButton("Skieurs");
		displaySkiers.setFont(new Font("Tahoma", Font.BOLD, 17));
		displaySkiers.setBounds(66, 310, 155, 49);
		contentPane.add(displaySkiers);
		
		displayInstructors = new JButton("Instructeurs");
		displayInstructors.setFont(new Font("Tahoma", Font.BOLD, 17));
		displayInstructors.setBounds(451, 310, 148, 49);
		contentPane.add(displayInstructors);
		
		btnNewButton = new JButton("Leçons");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnNewButton.setBounds(808, 310, 125, 49);
		contentPane.add(btnNewButton);
		
		displayBookings = new JButton("Réservations");
		displayBookings.setFont(new Font("Tahoma", Font.BOLD, 17));
		displayBookings.setBounds(1084, 313, 155, 49);
		contentPane.add(displayBookings);
	}
	
	public Index() {
		initializeComponents();
		
		handleSkierButton();
		
		handleInstructorButton();

		handleLessonButton();
		
		handleBookingButton();
	}
}