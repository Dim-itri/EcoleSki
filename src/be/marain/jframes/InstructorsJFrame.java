package be.marain.jframes;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
	private int selectedRow;
	private Instructor selectedInstructor;
	JPanel tablePanel;
	InstructorTableModel model;
	JTable table;
	JScrollPane scrollPane;
	JButton btnCreation;
	JComboBox<Accreditation> accredCB;
	JButton btnAddAccred;
	List<Accreditation> accreditations;
	JButton btnDeleteAccred;
	JButton btnDeletion;
	JButton btnUpdate;
	List<Instructor> instructors;
	
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
	
	private void resetFields() {
		tfName.setText("");
		tfPhone.setText("");
		tfSurname.setText("");
		dobChooser.setDate(null);
		selectedAccreditations = new ArrayList<Accreditation>();
		selectedRow = -1;
		selectedInstructor = null;
	}
	
	private void handleClick() {
		table.getSelectionModel().addListSelectionListener(event -> {
			if(!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
				selectedRow = table.getSelectedRow();
				selectedInstructor = model.getInstructorAt(selectedRow);
				selectedAccreditations = selectedInstructor.getInstructorAccreditations();
				
				tfSurname.setText(selectedInstructor.getSurname());
				tfName.setText(selectedInstructor.getName());
				tfPhone.setText(String.valueOf(selectedInstructor.getPhoneNumber()));
				dobChooser.setDate(Date.from(selectedInstructor.getDateOfBirth().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			}
		});
	}

	private void handleCreateButton() {
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
						resetFields();
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
	}
	
	private void handleDeleteButton() {
		btnDeletion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(selectedInstructor.deleteInstructor(instructorDAO)) {
						model.deleteInstructor(selectedRow);
						resetFields();
						JOptionPane.showMessageDialog(null, "Instructeur supprimé avec succès !");
					}
				}catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(null, "Veuillez sélectionner un instructeur.");
				}catch (IndexOutOfBoundsException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
			}
		});
	}
	
	private void handleUpdateButton() {
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String instructorName = tfName.getText();
					String instructorSurname = tfSurname.getText();
					int instructorPhone = Integer.parseInt(tfPhone.getText());
					LocalDate instructorDob = dobChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					
					selectedInstructor.setName(instructorName);
					selectedInstructor.setSurname(instructorSurname);
					selectedInstructor.setPhoneNumber(instructorPhone);
					selectedInstructor.setDateOfBirth(instructorDob);
					
					List<Accreditation> accreditationscopy = new CopyOnWriteArrayList<Accreditation>(selectedAccreditations);
					
					selectedInstructor.getInstructorAccreditations().clear();
					
					for(Accreditation curr:accreditationscopy) {
						selectedInstructor.addAccreditation(curr);
					}
					
					if(selectedInstructor.updateInstructor(instructorDAO)) {
						model.updateInstructor(selectedRow, selectedInstructor);
						JOptionPane.showMessageDialog(null, "Instructeur modifié !");
					}
				}catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(null, "Veuillez sélectionner un instructeur");
				}catch (IndexOutOfBoundsException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
			}
		});
	}
	
	private void handleAddAccred() {
		btnAddAccred.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Accreditation selectedAccreditation = (Accreditation)accredCB.getSelectedItem();
				if(selectedAccreditation != null && !selectedAccreditations.contains(selectedAccreditation)) {
					selectedAccreditations.add(selectedAccreditation);
					JOptionPane.showMessageDialog(null, "Accréditation ajoutée : " + selectedAccreditation);
				}
			}
		});
	}
	
	private void handleDeleteAccred() {
		btnDeleteAccred.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Accreditation selectedAccreditation = (Accreditation)accredCB.getSelectedItem();
				if(selectedAccreditation != null && selectedAccreditations.contains(selectedAccreditation)) {
					selectedAccreditations.remove(selectedAccreditation);
					JOptionPane.showMessageDialog(null, "Accréditation désélectionnée.");
				}
			}
		});
	}
	
	private void initializeComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1299, 763);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setTitle("Instructeurs");

		setContentPane(contentPane);
		
		tablePanel = new JPanel();
		tablePanel.setBounds(5, 5, 1293, 735);
		tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

		model = new InstructorTableModel(instructors);
		contentPane.setLayout(null);
		tablePanel.setLayout(null);

		table = new JTable(model);

		table.getColumnModel().getColumn(5).setPreferredWidth(200);
		
		scrollPane = new JScrollPane(table);

		scrollPane.setBounds(821, 20, 452, 699);

		tablePanel.add(scrollPane);
		contentPane.add(tablePanel);
		
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
		
		btnCreation = new JButton("Créer");
		btnCreation.setBounds(102, 529, 89, 23);
		tablePanel.add(btnCreation);
		
		accredCB = new JComboBox<>();
		for(Accreditation currAcc:accreditations) {
			accredCB.addItem(currAcc);
		}
		accredCB.setBounds(234, 296, 129, 22);
		tablePanel.add(accredCB);
		
		btnAddAccred = new JButton("Ajouter");
		btnAddAccred.setBounds(373, 296, 89, 23);
		tablePanel.add(btnAddAccred);
		
		btnDeleteAccred = new JButton("Supprimer");
		btnDeleteAccred.setBounds(472, 296, 89, 23);
		tablePanel.add(btnDeleteAccred);
		
		btnDeletion = new JButton("Supprimer");
		btnDeletion.setBounds(201, 529, 89, 23);
		tablePanel.add(btnDeletion);
		
		btnUpdate = new JButton("Modifier");
		btnUpdate.setBounds(300, 529, 89, 23);
		tablePanel.add(btnUpdate);
		
		Index.createHomeButton(tablePanel);
	}
	
	public InstructorsJFrame() {
		accreditations = Accreditation.getAllAccreditations(accreditationDAO);
		instructors = Instructor.getAllInstructors(instructorDAO);
		selectedAccreditations = new ArrayList<Accreditation>();
		
		initializeComponents();
		
		handleClick();
		
		handleCreateButton();
		
		handleAddAccred();
		
		handleDeleteAccred();
	
		handleDeleteButton();
		
		handleUpdateButton();
	}
}
