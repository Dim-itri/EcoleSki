package be.marain.jframes;

import java.awt.EventQueue;
import java.util.Date;
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

public class SkiersJFrame extends JFrame {

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
	private Skier selectedSkier;
	private int selectedRow;
	JDateChooser dobChooser;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SkiersJFrame frame = new SkiersJFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void resetSelectedSkier() {
		surnameTF.setText("");
		nameTF.setText("");
		phoneTF.setText("");
		dobChooser.setDate(null);
		
		selectedRow = -1;
		selectedSkier = null;
	}
	/**
	 * Create the frame.
	 */	
	public SkiersJFrame() {
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
		
		//Handling click on a row from the table
				table.getSelectionModel().addListSelectionListener(event -> {
					if(!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
						selectedRow = table.getSelectedRow();
						
						selectedSkier = model.getSkierAt(selectedRow);
						
						surnameTF.setText(selectedSkier.getSurname());
						nameTF.setText(selectedSkier.getName());
						phoneTF.setText(String.valueOf(selectedSkier.getPhoneNumber()));
						dobChooser.setDate(Date.from(selectedSkier.getDateOfBirth().atStartOfDay(ZoneId.systemDefault()).toInstant()));
					}
				});
		
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
		
		dobChooser = new JDateChooser();
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
					
					resetSelectedSkier();
					
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
		
		btnCreateSkier.setBounds(88, 393, 127, 21);
		tablePanel.add(btnCreateSkier);
		
		//User deletion	
		JButton btnDelete = new JButton("Supprimer");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(selectedSkier.deleteSkier(skierDao)) {
						model.deleteSkier(selectedRow);
						resetSelectedSkier();
						JOptionPane.showMessageDialog(null, "Skieur supprimé !");
					}
				}catch(NullPointerException ex) {
					JOptionPane.showMessageDialog(null, "Veuillez sélectionner un skieur");
				}catch (IndexOutOfBoundsException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
			}
		});
		
		btnDelete.setBounds(232, 392, 127, 23);
		tablePanel.add(btnDelete);
		
		//Update skier
		JButton btnUpdate = new JButton("Modifier");
		btnUpdate.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				try {
					skierSurname = surnameTF.getText();
					skierName = nameTF.getText();
					skierPhone = Integer.parseInt(phoneTF.getText());
					skierDob =  dobChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					
					selectedSkier.setName(skierName);
					selectedSkier.setSurname(skierSurname);
					selectedSkier.setDateOfBirth(skierDob);
					selectedSkier.setPhoneNumber(skierPhone);
					
					if(selectedSkier.updateSkier(skierDao)) {
						model.updateSkier(selectedRow, selectedSkier);
						JOptionPane.showMessageDialog(null, "Skieur modifié !");
					}
					
					resetSelectedSkier();
				}catch(NullPointerException ex){
					JOptionPane.showMessageDialog(null, "Veuillez sélectionner un skieur.");
				}catch (IndexOutOfBoundsException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
			}
		});
		btnUpdate.setBounds(369, 392, 127, 23);
		tablePanel.add(btnUpdate);
		
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
