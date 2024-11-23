package be.marain.jframes;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import be.marain.classes.Instructor;
import be.marain.classes.Lesson;
import be.marain.classes.LessonType;
import be.marain.dao.EcoleSkiConnection;
import be.marain.dao.InstructorDAO;
import be.marain.dao.LessonDAO;
import be.marain.dao.LessonTypeDAO;
import be.marain.tableModels.LessonTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;

import javax.swing.JComboBox;
import javax.swing.JButton;

public class LessonsJFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private LessonDAO lessonDAO = new LessonDAO(EcoleSkiConnection.getInstance());
	private InstructorDAO instructorDAO = new InstructorDAO(EcoleSkiConnection.getInstance());
	private LessonTypeDAO lessonTypeDAO = new LessonTypeDAO(EcoleSkiConnection.getInstance());
	private JPanel contentPane;
	private JTextField tfMin;
	private JTextField tfMax;
	private Instructor selectedInstructor;
	private LessonType selectedLessonType;
	private JComboBox<Instructor> cbInstructor;
	private Lesson selectedLesson;
	private int selectedRow;
	private JDateChooser dclessonDate;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LessonsJFrame frame = new LessonsJFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void resetFields() {
		tfMax.setText("");
		tfMin.setText("");
		dclessonDate.setDate(null);
		selectedRow = -1;
		selectedInstructor = null;
		selectedLesson = null;
		selectedLessonType = null;
	}

	/**
	 * Create the frame.
	 */
	public LessonsJFrame() {
		List<Lesson> lessons = Lesson.getAllLessons(lessonDAO);
		List<LessonType> lessonTypes = LessonType.getAllLessonTypes(lessonTypeDAO);
		List<Instructor> instructors = Instructor.getAllInstructors(instructorDAO);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1301, 789);
		setTitle("Leçons");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		JPanel tablePanel = new JPanel();
		tablePanel.setBounds(5, 5, 1293, 739);
		tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		LessonTableModel model = new LessonTableModel(lessons);
		contentPane.setLayout(null);
		tablePanel.setLayout(null);
		
		JTable table = new JTable(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(5);
		table.getColumnModel().getColumn(1).setPreferredWidth(5);
		table.getColumnModel().getColumn(2).setPreferredWidth(5);
		table.getColumnModel().getColumn(3).setPreferredWidth(50);
		table.getColumnModel().getColumn(4).setPreferredWidth(50);
		tablePanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(473, 20, 800, 699);
		scrollPane.setPreferredSize(new Dimension(800, 600));
		tablePanel.add(scrollPane);
		contentPane.add(tablePanel);
		
		table.getSelectionModel().addListSelectionListener(event -> {
			if(!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
				selectedRow = table.getSelectedRow();
				selectedLesson = model.getLessonAt(selectedRow);
				selectedInstructor = selectedLesson.getInstructor();
				selectedLessonType = selectedLesson.getLessonType();
				
				tfMin.setText(String. valueOf(selectedLesson.getMinBookings()));
				tfMax.setText(String.valueOf(selectedLesson.getMaxBookings()));
				dclessonDate.setDate(Date.from(selectedLesson.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			}
		});
		
		JLabel lblMinBooking = new JLabel("Min. Réservations");
		lblMinBooking.setBounds(10, 101, 86, 24);
		tablePanel.add(lblMinBooking);
		
		JLabel lblMaxBooking = new JLabel("Max. Réservations");
		lblMaxBooking.setBounds(10, 164, 86, 24);
		tablePanel.add(lblMaxBooking);
		
		JLabel lblDate = new JLabel("Date");
		lblDate.setBounds(10, 220, 86, 24);
		tablePanel.add(lblDate);
		
		JLabel lblInstructor = new JLabel("Instructeur");
		lblInstructor.setBounds(10, 347, 86, 24);
		tablePanel.add(lblInstructor);
		
		JLabel lblLessonType = new JLabel("Type");
		lblLessonType.setBounds(10, 285, 86, 24);
		tablePanel.add(lblLessonType);
		
		tfMin = new JTextField();
		tfMin.setBounds(106, 104, 96, 19);
		tablePanel.add(tfMin);
		tfMin.setColumns(10);
		
		tfMax = new JTextField();
		tfMax.setColumns(10);
		tfMax.setBounds(106, 167, 96, 19);
		tablePanel.add(tfMax);
		
		dclessonDate = new JDateChooser();
		dclessonDate.setBounds(106, 220, 96, 19);
		tablePanel.add(dclessonDate);
		
		JComboBox<LessonType> cbLessonType = new JComboBox<LessonType>();
		for(LessonType lt:lessonTypes) {
			cbLessonType.addItem(lt);
		}
		cbLessonType.setBounds(106, 287, 292, 21);
		tablePanel.add(cbLessonType);
		
		cbLessonType.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedLessonType = (LessonType)cbLessonType.getSelectedItem(); 
				
				cbInstructor = new JComboBox<Instructor>();
				for(Instructor curr:instructors) {
					for(int i=0;i<curr.getInstructorAccreditations().size();i++) {
						if(selectedLessonType.getAccreditation().getAccreditationId() == curr.getInstructorAccreditations().get(i).getAccreditationId()) {
							cbInstructor.addItem(curr);
						}
					}
				}
				cbInstructor.setBounds(106, 344, 292, 21);
				tablePanel.add(cbInstructor);
			}
		});
		
		
		JButton btnHome = new JButton("Accueil");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Index index = new Index();
				index.setVisible(true);
				dispose();
			}
		});
		btnHome.setBounds(7, 705, 89, 23);
		tablePanel.add(btnHome);
		
		JButton btnCreate = new JButton("Créer");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int minBook = Integer.parseInt(tfMin.getText());
					int maxBook = Integer.parseInt(tfMax.getText());
					LocalDate date = dclessonDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					selectedInstructor = (Instructor)cbInstructor.getSelectedItem();
					
					Lesson newLesson = new Lesson(minBook, maxBook, date, selectedInstructor, selectedLessonType);
					
					if(newLesson.addLesson(lessonDAO)) {
						model.addLesson(newLesson);
						JOptionPane.showMessageDialog(null, "Leçon créée !");
					}
				}catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Veuillez entrer un nombre de réservations correct.");
				}catch (IllegalArgumentException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
				
			}
		});
		btnCreate.setBounds(7, 492, 89, 23);
		tablePanel.add(btnCreate);	
		
		JButton btnDelete = new JButton("Supprimer");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(selectedLesson.deleteLesson(lessonDAO)) {
						model.deleteLesson(selectedRow);
						resetFields();
						JOptionPane.showMessageDialog(null, "Leçon supprimée !");
					}
				} catch (NullPointerException e2) {
					JOptionPane.showMessageDialog(null, "Veuillez sélectionner un instructeur.");
				}catch (IndexOutOfBoundsException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
			}
		});
		btnDelete.setBounds(106, 492, 89, 23);
		tablePanel.add(btnDelete);
	}
}
