package be.marain.jframes;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import be.marain.classes.Skier;
import be.marain.dao.EcoleSkiConnection;
import be.marain.dao.SkierDAO;
import be.marain.tableModels.SkierTableModel;

import javax.swing.JTable;

public class DisplaySkiers extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private SkierDAO skierDao = new SkierDAO(EcoleSkiConnection.getInstance());

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DisplaySkiers frame = new DisplaySkiers();
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
	public DisplaySkiers() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1317, 786);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setTitle("Skieurs");

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());
		
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		//Handling table of skiers	
		List<Skier> skiers = Skier.getAllSkiers(skierDao);
		
		SkierTableModel model= new SkierTableModel(skiers);
		
		JTable table = new JTable(model);
		
		JScrollPane scrollPane = new JScrollPane(table);
		
		scrollPane.setBounds(0, 627, 355, 0);
        
		tablePanel.add(scrollPane, BorderLayout.EAST);
		contentPane.add(tablePanel, BorderLayout.CENTER);
	}
}
