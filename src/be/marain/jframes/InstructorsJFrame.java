package be.marain.jframes;

import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import be.marain.classes.Instructor;
import be.marain.dao.EcoleSkiConnection;
import be.marain.dao.InstructorDAO;
import be.marain.tableModels.InstructorTableModel;

public class InstructorsJFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private InstructorDAO instructorDAO = new InstructorDAO(EcoleSkiConnection.getInstance());

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InstructorsJFrame frame = new InstructorsJFrame();
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
	public InstructorsJFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1299, 777);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setTitle("Instructeurs");

		setContentPane(contentPane);
		
		JPanel tablePanel = new JPanel();
		tablePanel.setBounds(5, 5, 1293, 739);
		tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		List<Instructor> instructors = Instructor.getAllInstructors(instructorDAO);

		InstructorTableModel model = new InstructorTableModel(instructors);
		contentPane.setLayout(null);
		tablePanel.setLayout(null);

		JTable table = new JTable(model);

		table.getColumnModel().getColumn(5).setPreferredWidth(200);
		
		JScrollPane scrollPane = new JScrollPane(table);

		scrollPane.setBounds(821, 20, 452, 699);

		tablePanel.add(scrollPane);
		contentPane.add(tablePanel);
	}
}
