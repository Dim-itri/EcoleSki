package be.marain.jframes;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import be.marain.classes.Accreditation;
import be.marain.classes.Instructor;
import be.marain.dao.AccreditationDAO;
import be.marain.dao.EcoleSkiConnection;
import be.marain.dao.InstructorDAO;
import be.marain.tableModels.InstructorTableModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;
import javax.swing.JComboBox;

public class InstructorsJFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private InstructorDAO instructorDAO = new InstructorDAO(EcoleSkiConnection.getInstance());
	private AccreditationDAO accreditationDAO = new AccreditationDAO(EcoleSkiConnection.getInstance());
	private JTextField tfName;
	private JTextField tfSurname;
	private JTextField tfPhone;
	private JDateChooser dobChooser;
	private List<Accreditation> selectedAccreditations;

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
	
	public void resetFields() {
		tfName.setText("");
		tfPhone.setText("");
		tfSurname.setText("");
		dobChooser.setDate(null);
		selectedAccreditations = new ArrayList<Accreditation>();
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
		
		//Instructor Creation
		
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
		
		JLabel lblName = new JLabel("Prénom");
		lblName.setBounds(102, 133, 89, 29);
		tablePanel.add(lblName);
		
		JLabel lblSurname = new JLabel("Nom");
		lblSurname.setBounds(102, 173, 89, 29);
		tablePanel.add(lblSurname);
		
		JLabel lblDob = new JLabel("Date de naissance");
		lblDob.setBounds(102, 213, 89, 29);
		tablePanel.add(lblDob);
		
		JLabel lblPhone = new JLabel("Téléphone");
		lblPhone.setBounds(102, 253, 89, 29);
		tablePanel.add(lblPhone);
		
		JLabel lblAccred = new JLabel("Accréditations");
		lblAccred.setBounds(102, 293, 89, 29);
		tablePanel.add(lblAccred);
		
		tfName = new JTextField();
		tfName.setBounds(234, 133, 129, 29);
		tablePanel.add(tfName);
		tfName.setColumns(10);
		
		tfSurname = new JTextField();
		tfSurname.setColumns(10);
		tfSurname.setBounds(234, 173, 129, 29);
		tablePanel.add(tfSurname);
		
		tfPhone = new JTextField();
		tfPhone.setColumns(10);
		tfPhone.setBounds(234, 253, 129, 29);
		tablePanel.add(tfPhone);
		
		dobChooser = new JDateChooser();
		dobChooser.setBounds(234, 213, 133, 29);
		tablePanel.add(dobChooser);
		
		//Instructor Creation
		JButton btnCreation = new JButton("Créer");
		btnCreation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String instructorName = tfName.getText();
					String instructorSurname = tfSurname.getText();
					LocalDate dobInstructor = dobChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					int instructorPhone = Integer.parseInt(tfPhone.getText());
					Instructor newInstructor = new Instructor(instructorName, instructorSurname
							, dobInstructor, instructorPhone, selectedAccreditations.get(0));
					
					for(int i = 1;i<selectedAccreditations.size();i++) {
						newInstructor.addAccreditation(selectedAccreditations.get(i));
					}
					
					if(newInstructor.createInstructor(instructorDAO)) {
						model.addInstructor(newInstructor);
						JOptionPane.showMessageDialog(null, "Instructeur créé");
					}
				}catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Numéro de téléphone incorrect.");
				}catch (IllegalArgumentException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}catch (NullPointerException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
			}
		});
		btnCreation.setBounds(102, 528, 89, 23);
		tablePanel.add(btnCreation);
		
		JComboBox<Accreditation> accredCB = new JComboBox<>();
		//Fetching accreditations
		List<Accreditation> accreditations = Accreditation.getAllAccreditations(accreditationDAO);
		for(Accreditation currAcc:accreditations) {
			accredCB.addItem(currAcc);
		}
		accredCB.setBounds(234, 296, 129, 22);
		tablePanel.add(accredCB);
		
		//Adding accreditations to new Instructor
		selectedAccreditations = new ArrayList<Accreditation>();
		JButton btnAddAccred = new JButton("Ajouter");
		btnAddAccred.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Accreditation selectedAccreditation = (Accreditation)accredCB.getSelectedItem();
				if(selectedAccreditation != null && !selectedAccreditations.contains(selectedAccreditation)) {
					selectedAccreditations.add(selectedAccreditation);
					JOptionPane.showMessageDialog(null, "Accréditation ajoutée : " + selectedAccreditation);
				}
			}
		});
		btnAddAccred.setBounds(373, 296, 89, 23);
		tablePanel.add(btnAddAccred);
	}
}
