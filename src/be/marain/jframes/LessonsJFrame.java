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

import be.marain.classes.Lesson;
import be.marain.dao.EcoleSkiConnection;
import be.marain.dao.LessonDAO;
import be.marain.dao.LessonTypeDAO;
import be.marain.tableModels.LessonTableModel;

public class LessonsJFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private LessonDAO lessonDAO = new LessonDAO(EcoleSkiConnection.getInstance());
	private JPanel contentPane;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1301, 789);
		setTitle("Leçons");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		JPanel tablePanel = new JPanel();
		tablePanel.setBounds(5, 5, 1293, 739);
		tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		List<Lesson> lessons = Lesson.getAllLessons(lessonDAO);
		
		LessonTableModel model = new LessonTableModel(lessons);
		contentPane.setLayout(null);
		tablePanel.setLayout(null);
		
		JTable table = new JTable(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(5);
		table.getColumnModel().getColumn(1).setPreferredWidth(5);
		table.getColumnModel().getColumn(2).setPreferredWidth(5);
		table.getColumnModel().getColumn(3).setPreferredWidth(50);
		table.getColumnModel().getColumn(4).setPreferredWidth(50);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(645, 20, 732, 699);
		scrollPane.setPreferredSize(new Dimension(800, 600));
		
		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(scrollPane, BorderLayout.EAST);
		contentPane.add(tablePanel);
	}

}
