package be.marain.jframes;

import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import be.marain.classes.Booking;
import be.marain.classes.Instructor;
import be.marain.classes.Lesson;
import be.marain.classes.Skier;
import be.marain.dao.BookingDAO;
import be.marain.dao.EcoleSkiConnection;
import be.marain.dao.LessonDAO;
import be.marain.dao.SkierDAO;
import be.marain.tableModels.BookingTableModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JComboBox;

public class BookingsJFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private BookingDAO bookingDAO = new BookingDAO(EcoleSkiConnection.getInstance());
	private LessonDAO lessonDAO = new LessonDAO(EcoleSkiConnection.getInstance());
	private SkierDAO skierDAO = new SkierDAO(EcoleSkiConnection.getInstance());
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BookingsJFrame frame = new BookingsJFrame();
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
	public BookingsJFrame() {
		List<Booking> bookings = Booking.getAllBookings(bookingDAO);
		List<Lesson> lessons = Lesson.getAllLessons(lessonDAO);
		List<Skier> skiers = Skier.getAllSkiers(skierDAO);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1942, 769);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel tablePanel = new JPanel();
		tablePanel.setBounds(5, 5, 1911, 725);
		tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		tablePanel.setLayout(null);
		
		BookingTableModel model = new BookingTableModel(bookings);
		
		JTable table = new JTable(model);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(407, 11, 1504, 699);
		
		tablePanel.add(scrollPane);
		contentPane.add(tablePanel);
		
		JButton btnHome = new JButton("Accueil");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Index index = new Index();		
				index.setVisible(true);
				dispose();
			}
		});
		btnHome.setBounds(10, 687, 89, 23);
		tablePanel.add(btnHome);
		
		JLabel lblLesson = new JLabel("Leçon");
		lblLesson.setBounds(10, 196, 46, 14);
		tablePanel.add(lblLesson);
		
		JComboBox cbLesson = new JComboBox();
		cbLesson.setBounds(94, 192, 109, 22);
		tablePanel.add(cbLesson);
		
		JButton btnCreate = new JButton("Créer");
		btnCreate.setBounds(10, 335, 89, 23);
		tablePanel.add(btnCreate);
		
		JLabel lblSkier = new JLabel("Skieur");
		lblSkier.setBounds(10, 239, 46, 14);
		tablePanel.add(lblSkier);
		
		JComboBox cbSkier = new JComboBox();
		cbSkier.setBounds(94, 235, 109, 22);
		tablePanel.add(cbSkier);
	}
}
