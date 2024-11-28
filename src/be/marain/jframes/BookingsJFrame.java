package be.marain.jframes;

import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;

import be.marain.classes.Booking;
import be.marain.classes.Lesson;
import be.marain.classes.Period;
import be.marain.classes.Skier;
import be.marain.dao.BookingDAO;
import be.marain.dao.EcoleSkiConnection;
import be.marain.dao.LessonDAO;
import be.marain.dao.PeriodDAO;
import be.marain.dao.SkierDAO;
import be.marain.tableModels.BookingTableModel;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

public class BookingsJFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private BookingDAO bookingDAO = new BookingDAO(EcoleSkiConnection.getInstance());
	private LessonDAO lessonDAO = new LessonDAO(EcoleSkiConnection.getInstance());
	private SkierDAO skierDAO = new SkierDAO(EcoleSkiConnection.getInstance());
	private PeriodDAO periodDAO = new PeriodDAO(EcoleSkiConnection.getInstance());
	private JPanel contentPane;
	private Skier selectedSkier;
	private Lesson selectedLesson;
	JCheckBox chckbxInsurance;
	JComboBox<Skier> cbSkier;
	JComboBox<Lesson> cbLesson;

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
	
	public void resetFields() {
		selectedLesson = null;
		selectedSkier = null;
		cbLesson.removeAllItems();
		chckbxInsurance.setSelected(false);
	}

	/**
	 * Create the frame.
	 */
	public BookingsJFrame() {
		List<Booking> bookings = Booking.getAllBookings(bookingDAO);
		List<Lesson> lessons = Lesson.getAllLessons(lessonDAO);
		List<Skier> skiers = Skier.getAllSkiers(skierDAO);
		List<Period> periods = Period.getAllPeriods(periodDAO);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1385, 769);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel tablePanel = new JPanel();
		tablePanel.setBounds(5, 5, 1373, 725);
		tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		tablePanel.setLayout(null);
		
		BookingTableModel model = new BookingTableModel(bookings);
		
		JTable table = new JTable(model);
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(407, 11, 953, 699);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);	
		tablePanel.add(scrollPane);
		contentPane.add(tablePanel);
		
		for (int column = 0; column < table.getColumnCount(); column++) {
		    TableColumn tableColumn = table.getColumnModel().getColumn(column);
		    tableColumn.setPreferredWidth(300);
		}	
		
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
		lblLesson.setBounds(10, 224, 46, 14);
		tablePanel.add(lblLesson);
		
		chckbxInsurance = new JCheckBox("");
		chckbxInsurance.setBounds(94, 159, 93, 21);
		tablePanel.add(chckbxInsurance);
		
		cbLesson = new JComboBox<Lesson>();
		cbLesson.setBounds(94, 220, 257, 22);
		tablePanel.add(cbLesson);
		
		cbLesson.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedLesson = (Lesson)cbLesson.getSelectedItem();
			}
		});
		
		cbSkier = new JComboBox<Skier>();
		for(int i=0;i<skiers.size();i++) {
			cbSkier.addItem(skiers.get(i));
		}
		cbSkier.setBounds(94, 191, 257, 22);
		tablePanel.add(cbSkier);
		
		cbSkier.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cbLesson.removeAllItems();
				selectedSkier = (Skier)cbSkier.getSelectedItem();
				
				for(Lesson curr:lessons) {									
					if(selectedSkier.isOldEnough(curr) && !curr.isFull() && selectedSkier.isAvailable(curr.getDate(), curr.getStartHour(), curr.getEndHour())) {
						cbLesson.addItem(curr);
					}
				}
			}
		});
		
		JButton btnCreate = new JButton("Créer");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Period period = null;
					
					for(Period currPeriod:periods) {
						if (selectedLesson.getDate().isAfter(currPeriod.getStartDate()) && selectedLesson.getDate().isBefore(currPeriod.getEndDate())) {
							period = currPeriod;
						}
					}
					
					Booking newBooking = new Booking(LocalDate.now(), selectedLesson.getInstructor(), selectedSkier, selectedLesson, period, chckbxInsurance.isSelected());
					
					if(newBooking.canBook(selectedLesson)) {
						
						if(newBooking.createBooking(bookingDAO)) {
							model.addBooking(newBooking);
							selectedLesson.addBooking(newBooking);
							selectedSkier.addBooking(newBooking);
							
							JOptionPane.showMessageDialog(null, "Réservation créée !");
							resetFields();
						}	
						
					}else {
						JOptionPane.showMessageDialog(null, "Réservation en dehors des délais");
					}
				}catch (Exception ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
				
			}
		});
		btnCreate.setBounds(10, 335, 89, 23);
		tablePanel.add(btnCreate);
		
		JLabel lblSkier = new JLabel("Skieur");
		lblSkier.setBounds(10, 195, 46, 14);
		tablePanel.add(lblSkier);
		
		JLabel lblInsurance = new JLabel("Assurance");
		lblInsurance.setBounds(10, 159, 78, 13);
		tablePanel.add(lblInsurance);
	}
}
