package be.marain.jframes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;
import javax.swing.JComboBox;

public class LessonsJFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private LessonDAO lessonDAO = new LessonDAO(EcoleSkiConnection.getInstance());
	private InstructorDAO instructorDAO = new InstructorDAO(EcoleSkiConnection.getInstance());
	private LessonTypeDAO lessonTypeDAO = new LessonTypeDAO(EcoleSkiConnection.getInstance());
	private JPanel contentPane;
	private JTextField tfMin;
	private JTextField tfMax;

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
		lblInstructor.setBounds(10, 285, 86, 24);
		tablePanel.add(lblInstructor);
		
		JLabel lblLessonType = new JLabel("Type");
		lblLessonType.setBounds(10, 342, 86, 24);
		tablePanel.add(lblLessonType);
		
		tfMin = new JTextField();
		tfMin.setBounds(106, 104, 96, 19);
		tablePanel.add(tfMin);
		tfMin.setColumns(10);
		
		tfMax = new JTextField();
		tfMax.setColumns(10);
		tfMax.setBounds(106, 167, 96, 19);
		tablePanel.add(tfMax);
		
		JDateChooser dclessonDate = new JDateChooser();
		dclessonDate.setBounds(106, 220, 96, 19);
		tablePanel.add(dclessonDate);
		
		JComboBox<Instructor> cbInstructor = new JComboBox<Instructor>();
		for(Instructor curr:instructors) {
			cbInstructor.addItem(curr);
		}		
		cbInstructor.setBounds(106, 287, 292, 21);
		tablePanel.add(cbInstructor);
		
		JComboBox<LessonType> cbLessonType = new JComboBox<LessonType>();
		for(LessonType lt:lessonTypes) {
			cbLessonType.addItem(lt);
		}
		cbLessonType.setBounds(106, 344, 292, 21);
		tablePanel.add(cbLessonType);
	}
}
