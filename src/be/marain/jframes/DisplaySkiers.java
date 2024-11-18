package be.marain.jframes;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import be.marain.classes.Skier;
import be.marain.dao.EcoleSkiConnection;
import be.marain.dao.SkierDAO;
import be.marain.tableModels.SkierTableModel;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.awt.event.ActionEvent;
import java.awt.Color;
import com.toedter.calendar.JDateChooser;

public class DisplaySkiers extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private SkierDAO skierDao = new SkierDAO(EcoleSkiConnection.getInstance());
	private JTextField surnameTF;
	private JTextField nameTF;
	private JTextField phoneTF;
	private String skierName;
	private String skierSurname;
	private LocalDate skierDob;
	private int skierPhone;

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
		boolean success = false;
		String nameRegEx = "^[A-ZÀ-Ÿ][a-zà-ÿ]+(?:[-\\s][A-ZÀ-Ÿ][a-zà-ÿ]+)*$";
		String phoneRegEx = "^(\\+\\d{1,3})?\\s?(\\(?\\d{1,4}\\)?)?[\\s.-]?\\d{2,4}[\\s.-]?\\d{2,4}[\\s.-]?\\d{2,4}$";
		String dobRegEx = "^(19[0-9]{2}|20[0-9]{2})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";
 		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1317, 786);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setTitle("Skieurs");

		setContentPane(contentPane);

		JPanel tablePanel = new JPanel();
		tablePanel.setBounds(5, 5, 1293, 739);
		tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

		// Handling table of skiers
		List<Skier> skiers = Skier.getAllSkiers(skierDao);

		SkierTableModel model = new SkierTableModel(skiers);
		contentPane.setLayout(null);
		tablePanel.setLayout(null);

		JTable table = new JTable(model);

		JScrollPane scrollPane = new JScrollPane(table);

		scrollPane.setBounds(821, 20, 452, 699);

		tablePanel.add(scrollPane);
		contentPane.add(tablePanel);
		
		JLabel surnameLabel = new JLabel("Nom");
		surnameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		surnameLabel.setBounds(88, 76, 127, 29);
		tablePanel.add(surnameLabel);
		
		JLabel nameLabel = new JLabel("Prénom");
		nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		nameLabel.setBounds(88, 151, 127, 29);
		tablePanel.add(nameLabel);
		
		JLabel dobLabel = new JLabel("Date de naissance");
		dobLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		dobLabel.setBounds(88, 226, 127, 29);
		tablePanel.add(dobLabel);
		
		JLabel phoneLabel = new JLabel("Téléphone");
		phoneLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		phoneLabel.setBounds(88, 301, 127, 29);
		tablePanel.add(phoneLabel);
		
		JLabel surnameELabel = new JLabel("");
		surnameELabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		surnameELabel.setForeground(new Color(255, 0, 0));
		surnameELabel.setBounds(261, 112, 214, 19);
		tablePanel.add(surnameELabel);
		
		JLabel nameELabel = new JLabel("");
		nameELabel.setForeground(new Color(255, 0, 0));
		nameELabel.setBackground(new Color(240, 240, 240));
		nameELabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		nameELabel.setBounds(261, 187, 214, 19);
		tablePanel.add(nameELabel);
		
		JLabel dobELabel = new JLabel("");
		dobELabel.setForeground(new Color(255, 0, 0));
		dobELabel.setBounds(261, 262, 214, 19);
		tablePanel.add(dobELabel);
		
		JLabel phoneELabel = new JLabel("");
		phoneELabel.setForeground(new Color(255, 0, 0));
		phoneELabel.setBounds(261, 337, 214, 19);
		tablePanel.add(phoneELabel);
		
		surnameTF = new JTextField();
		surnameTF.setBounds(261, 83, 133, 19);
		tablePanel.add(surnameTF);
		surnameTF.setColumns(10);
		
		nameTF = new JTextField();
		nameTF.setBounds(261, 158, 133, 19);
		tablePanel.add(nameTF);
		nameTF.setColumns(10);
		
		phoneTF = new JTextField();
		phoneTF.setBounds(261, 308, 133, 19);
		tablePanel.add(phoneTF);
		phoneTF.setColumns(10);
		
		JDateChooser dobChooser = new JDateChooser();
		dobChooser.setBounds(261, 236, 133, 19);
		tablePanel.add(dobChooser);
		
		//User creation
		JButton btnCreateSkier = new JButton("Créer");
		btnCreateSkier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				try {
					skierSurname = surnameTF.getText();
					skierName = nameTF.getText();
					skierPhone = Integer.parseInt(phoneTF.getText());
					skierDob =  dobChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					Skier newSkier = new Skier(skierName, skierSurname, skierDob, skierPhone);
					
					if(newSkier.createSkier(skierDao)) {
						model.addSkier(newSkier);
						JOptionPane.showMessageDialog(null, "Skieur créé !");
					}
				}catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Le numéro de téléphone ne doit contenir que des nombres");
				}
				catch (IllegalArgumentException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
			}
		});
		
		btnCreateSkier.setBounds(88, 393, 85, 21);
		tablePanel.add(btnCreateSkier);
		
		JButton btnHome = new JButton("Accueil");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Index index = new Index();
				dispose();
				index.setVisible(true);
			}
		});
		btnHome.setBounds(10, 696, 89, 23);
		tablePanel.add(btnHome);
	}
}
