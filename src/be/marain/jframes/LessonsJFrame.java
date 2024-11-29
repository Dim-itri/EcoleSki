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
import javax.swing.table.TableColumn;


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
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

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
	private JComboBox<LessonType> cbLessonType;
	private Lesson selectedLesson;
	private int selectedRow;
	private JDateChooser dclessonDate;
	private JCheckBox chckbxIsIndividual;
	private JRadioButton rdbtnMorning;
	private JRadioButton rdbtnAfternoon;
	private int startHour;
	private int endHour;
	private JRadioButton rdbtn1Hour;
	private JRadioButton rdbtn2Hours;
	private List<Lesson> lessons;
	private List<LessonType> lessonTypes;
	private List<Instructor> instructors;
	JButton btnCreate;
	JButton btnDelete;
	JButton btnUpdate;
	LessonTableModel model;
	JTable table;
	JPanel tablePanel;
	JScrollPane scrollPane;
	
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
	
	private void resetFields() {
		tfMax.setText("");
		tfMin.setText("");
		dclessonDate.setDate(null);
		selectedRow = -1;
		selectedInstructor = null;
		selectedLesson = null;
		selectedLessonType = null;
		rdbtnAfternoon.setSelected(false);
		rdbtnMorning.setSelected(false);
	}
	
	private void handleClickTable(JTable table, LessonTableModel model) {
		table.getSelectionModel().addListSelectionListener(event -> {
			resetFields();
			if(!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
				selectedRow = table.getSelectedRow();
				selectedLesson = model.getLessonAt(selectedRow);
				selectedInstructor = selectedLesson.getInstructor();
				selectedLessonType = selectedLesson.getLessonType();
				
				tfMin.setText(String. valueOf(selectedLesson.getMinBookings()));
				tfMax.setText(String.valueOf(selectedLesson.getMaxBookings()));
				dclessonDate.setDate(Date.from(selectedLesson.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
				if(selectedLesson.getStartHour() == 9) {
					rdbtnMorning.setSelected(true);
				}else{
					rdbtnAfternoon.setSelected(true);
				}
				chckbxIsIndividual.setSelected(selectedLesson.getIsIndividual());
				
				cbLessonType.setSelectedItem(selectedLessonType);
				
				if(cbInstructor.getItemCount() > 0) {
					cbInstructor.removeAllItems();
				}
			}
		});
	}

	private void handleRdBtn1Hour() {
		rdbtn1Hour.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				endHour = 13;
				rdbtn2Hours.setSelected(false);
				rdbtnAfternoon.setSelected(false);
				rdbtnMorning.setSelected(false);
			}
		});
	}

	public void handleRdBtn2Hour() {
		rdbtn2Hours.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				endHour = 14;
				rdbtn1Hour.setSelected(false);
				rdbtnAfternoon.setSelected(false);
				rdbtnMorning.setSelected(false);				
			}
		});
	}
	
	private void handleRdBtnMorning() {
		rdbtnMorning.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startHour = 9;
				endHour = 12;
				rdbtnAfternoon.setSelected(false);
				chckbxIsIndividual.setSelected(false);
				rdbtn1Hour.setSelected(false);
				rdbtn2Hours.setSelected(false);
			}
		});
	}

	private void handleRdbtnAfternoon() {
		rdbtnAfternoon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startHour = 14;
				endHour = 17;
				rdbtnMorning.setSelected(false);
				chckbxIsIndividual.setSelected(false);
				rdbtn1Hour.setSelected(false);
				rdbtn2Hours.setSelected(false);
			}
		});
	}
	
	private void handleCbLessonType() {
		cbLessonType.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					selectedLessonType = (LessonType)cbLessonType.getSelectedItem(); 
					LocalDate date = dclessonDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					
					cbInstructor.removeAllItems();
					
					for(Instructor curr:instructors) {
							if(curr.isAccreditate(selectedLessonType)
									&& curr.isInstructorAvailable(date, lessons, startHour, endHour)) {
								cbInstructor.addItem(curr);
							}
					}
				}catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(null, "Veuillez choisir une date.");
				}
				catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Veuillez sélectionner une heure de début et de fin.");
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}
	
	private void handleChckBoxIsIndividual() {
		chckbxIsIndividual.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chckbxIsIndividual.isSelected()) {
					startHour = 12;
					rdbtnAfternoon.setSelected(false);
					rdbtnMorning.setSelected(false);
				}
				
			}
		});
	}
	
	private void handleCreateButton() {
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int minBook = Integer.parseInt(tfMin.getText());
					int maxBook = Integer.parseInt(tfMax.getText());
					LocalDate date = dclessonDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					selectedInstructor = (Instructor)cbInstructor.getSelectedItem();

					boolean isIndividual = chckbxIsIndividual.isSelected();
					int duration = endHour - startHour;
					
					Lesson newLesson = new Lesson(minBook, maxBook, date, selectedInstructor, selectedLessonType, isIndividual, startHour, endHour, duration);
					
					if(newLesson.canBeCreated()) {
						if(newLesson.addLesson(lessonDAO)) {
							model.addLesson(newLesson);
							JOptionPane.showMessageDialog(null, "Leçon créée !");
						}
					}else {
						JOptionPane.showMessageDialog(null, "Impossible de créer. La station ouvre du samedi 06/12/2024 au dimanche 03/05/2025.");
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
	}
	
	private void handleDeleteButton() {
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
	}
	
	private void handleUpdateButton() {
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int minBook = Integer.parseInt(tfMin.getText());
					int maxBook = Integer.parseInt(tfMax.getText());
					
					boolean isIndividual = chckbxIsIndividual.isSelected();
					int duration = endHour - startHour;
					LocalDate date = dclessonDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					
					if(cbLessonType.getSelectedItem() != null) {
						selectedLessonType = (LessonType)cbLessonType.getSelectedItem();
						selectedLesson.setLessonType(selectedLessonType);
					}		
					
					if(cbInstructor.getSelectedItem() != null) {
						selectedInstructor = (Instructor)cbInstructor.getSelectedItem();
						selectedLesson.setInstructor(selectedInstructor);
					}
					
					selectedLesson.setStartHour(startHour);
					selectedLesson.setEndHour(endHour);
					selectedLesson.setDuration(duration);
					selectedLesson.setIndividual(isIndividual);
					selectedLesson.setMinBookings(minBook);
					selectedLesson.setMaxBookings(maxBook);
					selectedLesson.setDate(date);
					
					if(selectedLesson.updateLesson(lessonDAO)) {
						model.updateLesson(selectedRow, selectedLesson);
						JOptionPane.showMessageDialog(null, "Leçon modifiée !");
						resetFields();
					}
				}catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Nombre de réservations incorrect.");
				}catch (IllegalArgumentException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
			}
		});
	}
	
	private void initializeComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1542, 784);
		setTitle("Leçons");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		tablePanel = new JPanel();
		tablePanel.setBounds(5, 5, 1659, 739);	
		tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		model = new LessonTableModel(lessons);
		contentPane.setLayout(null);
		tablePanel.setLayout(null);
		
		table = new JTable(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(5);
		table.getColumnModel().getColumn(1).setPreferredWidth(5);
		table.getColumnModel().getColumn(2).setPreferredWidth(5);
		table.getColumnModel().getColumn(3).setPreferredWidth(50);
		table.getColumnModel().getColumn(4).setPreferredWidth(50);
		tablePanel.setLayout(null);
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(408, 10, 1075, 699);
		scrollPane.setPreferredSize(new Dimension(800, 600));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		tablePanel.add(scrollPane);
		contentPane.add(tablePanel);
		
		for (int column = 0; column < table.getColumnCount(); column++) {
		    TableColumn tableColumn = table.getColumnModel().getColumn(column);
		    tableColumn.setPreferredWidth(150); // Largeur par défaut de chaque colonne
		}
		
		rdbtn1Hour = new JRadioButton("1 heure");
		rdbtn1Hour.setBounds(147, 265, 103, 21);
		tablePanel.add(rdbtn1Hour);
		
		rdbtn2Hours = new JRadioButton("2 heures");
		rdbtn2Hours.setBounds(252, 265, 103, 21);
		tablePanel.add(rdbtn2Hours);
		
		rdbtnMorning = new JRadioButton("Matin");
		rdbtnMorning.setBounds(106, 312, 103, 21);
		tablePanel.add(rdbtnMorning);
		
		rdbtnAfternoon = new JRadioButton("Après-midi");
		rdbtnAfternoon.setBounds(106, 347, 103, 21);
		tablePanel.add(rdbtnAfternoon);
		
		cbLessonType = new JComboBox<LessonType>();
		for(LessonType lt:lessonTypes) {
			cbLessonType.addItem(lt);
		}
		cbLessonType.setBounds(106, 377, 292, 21);
		tablePanel.add(cbLessonType);
		
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
		lblInstructor.setBounds(10, 410, 86, 24);
		tablePanel.add(lblInstructor);
		
		JLabel lblLessonType = new JLabel("Type");
		lblLessonType.setBounds(10, 375, 86, 24);
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
		
		cbInstructor = new JComboBox<Instructor>();
		cbInstructor.setBounds(106, 412, 292, 21);
		tablePanel.add(cbInstructor);
		
		JLabel lblIndividual = new JLabel("Individuelle");
		lblIndividual.setBounds(10, 265, 70, 21);
		tablePanel.add(lblIndividual);
		
		JLabel lblHour = new JLabel("Horaire");
		lblHour.setBounds(10, 315, 86, 14);
		tablePanel.add(lblHour);
		
		chckbxIsIndividual = new JCheckBox("");
		chckbxIsIndividual.setBounds(105, 264, 29, 23);
		tablePanel.add(chckbxIsIndividual);
		
		btnCreate = new JButton("Créer");
		btnCreate.setBounds(7, 492, 89, 23);
		tablePanel.add(btnCreate);	
		
		btnDelete = new JButton("Supprimer");
		btnDelete.setBounds(106, 492, 89, 23);
		tablePanel.add(btnDelete);
		
		btnUpdate = new JButton("Modifier");
		btnUpdate.setBounds(205, 492, 89, 23);
		tablePanel.add(btnUpdate);
		
		Index.createHomeButton(tablePanel);
	}
	
	public LessonsJFrame() {
		lessons = Lesson.getAllLessons(lessonDAO);
		lessonTypes = LessonType.getAllLessonTypes(lessonTypeDAO);
		instructors = Instructor.getAllInstructors(instructorDAO);
		
		initializeComponents();
		
		handleRdBtn1Hour();
	
		handleRdBtn2Hour();
			
		handleRdBtnMorning();
		
		handleRdbtnAfternoon();
		
		handleCbLessonType();
			
		handleClickTable(table, model);
		
		handleChckBoxIsIndividual();
		
		handleCreateButton();
		
		handleDeleteButton();
		
		handleUpdateButton();
	}
}