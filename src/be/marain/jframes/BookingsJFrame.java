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
	List<Booking> bookings;
	List<Lesson> lessons;
	List<Skier> skiers;
	List<Period> periods;
	JPanel tablePanel;
	BookingTableModel model;
	JTable table;
	JScrollPane scrollPane;
	JButton btnCreate;
	int selectedRow;
	Booking selectedBooking;
	JButton btnDelete;
	
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
	
	private void resetFields() {
		selectedLesson = null;
		selectedSkier = null;
		cbLesson.removeAllItems();
		chckbxInsurance.setSelected(false);
	}
	
	private void handleClick() {
		table.getSelectionModel().addListSelectionListener(event -> {
			if(!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
				selectedRow = table.getSelectedRow();
				selectedBooking = model.getBookingAt(selectedRow);
				selectedLesson = selectedBooking.getLesson();
				selectedSkier = selectedBooking.getSkier();
								
				chckbxInsurance.setSelected(selectedBooking.getIsInsured());
				cbSkier.setSelectedItem(selectedSkier);
				cbLesson.setSelectedItem(selectedLesson);
			}
		});
	}
	
	private void handleCbLesson() {
		cbLesson.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedLesson = (Lesson)cbLesson.getSelectedItem();
			}
		});
	}
	
	private void handleCbSkier() {
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
	}
	
	private void handleCreateButton() {
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				try {
					Period period = null;
					
					for(Period currPeriod:periods) {
						if (selectedLesson.getDate().isAfter(currPeriod.getStartDate()) || selectedLesson.getDate().equals(currPeriod.getStartDate())
								&& selectedLesson.getDate().isBefore(currPeriod.getEndDate()) || selectedLesson.getDate().equals(currPeriod.getEndDate())) {
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
				}catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
				
			}
		});
	}
	
	public void handleDeleteButton() {
		btnDelete.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(selectedBooking.deleteBooking(bookingDAO)) {
						model.deleteBooking(selectedRow);
						JOptionPane.showMessageDialog(null, "Réservation supprimée avec succès");
					}
				}catch (NullPointerException e2) {
					JOptionPane.showMessageDialog(null, "Veuillez sélectionner une réservation");
				}catch (IndexOutOfBoundsException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
			}
		});
	}
	
	private void initializeComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1385, 769);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tablePanel = new JPanel();
		tablePanel.setBounds(5, 5, 1373, 725);
		tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		tablePanel.setLayout(null);
		
		model = new BookingTableModel(bookings);
		
		table = new JTable(model);
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(407, 11, 953, 699);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);	
		tablePanel.add(scrollPane);
		contentPane.add(tablePanel);
		
		for (int column = 0; column < table.getColumnCount(); column++) {
		    TableColumn tableColumn = table.getColumnModel().getColumn(column);
		    tableColumn.setPreferredWidth(300);
		}	
		
		
		JLabel lblLesson = new JLabel("Leçon");
		lblLesson.setBounds(10, 224, 46, 14);
		tablePanel.add(lblLesson);
		
		chckbxInsurance = new JCheckBox("");
		chckbxInsurance.setBounds(94, 159, 93, 21);
		tablePanel.add(chckbxInsurance);
		
		cbLesson = new JComboBox<Lesson>();
		cbLesson.setBounds(94, 220, 257, 22);
		tablePanel.add(cbLesson);
		
		cbSkier = new JComboBox<Skier>();
		for(int i=0;i<skiers.size();i++) {
			cbSkier.addItem(skiers.get(i));
		}
		cbSkier.setBounds(94, 191, 257, 22);
		tablePanel.add(cbSkier);
		
		btnCreate = new JButton("Créer");
		btnCreate.setBounds(10, 335, 89, 23);
		tablePanel.add(btnCreate);
		
		btnDelete = new JButton("Supprimer");
		btnDelete.setBounds(114, 335, 89, 23);
		tablePanel.add(btnDelete);
		
		JLabel lblSkier = new JLabel("Skieur");
		lblSkier.setBounds(10, 195, 46, 14);
		tablePanel.add(lblSkier);
		
		JLabel lblInsurance = new JLabel("Assurance");
		lblInsurance.setBounds(10, 159, 78, 13);
		tablePanel.add(lblInsurance);
		
		Index.createHomeButton(tablePanel);
	}
	
	public BookingsJFrame() {
		bookings = Booking.getAllBookings(bookingDAO);
		lessons = Lesson.getAllLessons(lessonDAO);
		skiers = Skier.getAllSkiers(skierDAO);
		periods = Period.getAllPeriods(periodDAO);
		
		initializeComponents();
		
		handleClick();
		
		handleCbLesson();
		
		handleCbSkier();
		
		handleCreateButton();
		
		handleDeleteButton();
	}
}
