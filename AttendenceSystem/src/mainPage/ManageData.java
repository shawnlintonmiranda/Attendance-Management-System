package mainPage;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import login.Login;

public class ManageData extends JFrame {
	private static final long serialVersionUID = 1L;
	private static Connection con=null;
	private static Statement stmt=null;
	private static ResultSet rs=null;
	
	private static String username;
	private JFrame login;
	private JFrame home;
	private JFrame frame;
	
	private static String[] class_id,class_name;
	private static JTextField usn;
	private static JTextField name;
	private static JLabel usnStatus;
	
	private static JComboBox<String> comboBox;
	
	public ManageData(String user) {
		super("Student Attendance Management System");
		frame=this;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int i=JOptionPane.showOptionDialog(null, "Are you sure your want to exit application?","Attendence Management System",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null, new String[] {"Yes","No"}, "No");
				if(i==JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});
		con=Login.getCon();
		login=Login.getLoginFrame();
		home=UserMainPage.getHomeFrame();
		username=user;
		getContentPane().setBackground(new Color(235, 255, 235));
		getContentPane().setLayout(null);
		setSize(700,700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
		JLabel title = new JLabel("Student Attendance Management System");
		title.setForeground(new Color(255, 0, 51));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Algerian", Font.PLAIN, 27));
		title.setBounds(10, 10, 676, 36);
		getContentPane().add(title);
		
		JButton logout = new JButton("LOGOUT");
		logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login.setVisible(true);
				dispose();
			}
		});
		logout.setHorizontalTextPosition(SwingConstants.CENTER);
		logout.setForeground(Color.WHITE);
		logout.setFont(new Font("Tahoma", Font.PLAIN, 18));
		logout.setFocusTraversalKeysEnabled(false);
		logout.setFocusPainted(false);
		logout.setBorder(new MatteBorder(2, 2, 1, 1, (Color) new Color(255, 255, 255)));
		logout.setBackground(new Color(255, 51, 51));
		logout.setBounds(581, 40, 92, 28);
		getContentPane().add(logout);
		
		JButton button = new JButton("HOME");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				home.setVisible(true);
				dispose();
			}
		});
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Tahoma", Font.PLAIN, 18));
		button.setFocusTraversalKeysEnabled(false);
		button.setFocusPainted(false);
		button.setBorder(new MatteBorder(2, 2, 1, 1, (Color) new Color(255, 255, 255)));
		button.setBackground(Color.BLUE);
		button.setBounds(488, 40, 83, 28);
		getContentPane().add(button);
		
		JLabel lblAddStudent = new JLabel("Add Student / Class Details");
		lblAddStudent.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddStudent.setFont(new Font("Georgia Pro Cond Semibold", Font.PLAIN, 21));
		lblAddStudent.setBounds(137, 78, 421, 36);
		getContentPane().add(lblAddStudent);
		
		JButton btnAddNewCollege = new JButton("Add New Class");
		btnAddNewCollege.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addClass();
			}
		});
		btnAddNewCollege.setHorizontalTextPosition(SwingConstants.CENTER);
		btnAddNewCollege.setForeground(Color.WHITE);
		btnAddNewCollege.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnAddNewCollege.setFocusTraversalKeysEnabled(false);
		btnAddNewCollege.setFocusPainted(false);
		btnAddNewCollege.setBorder(new MatteBorder(2, 2, 1, 1, (Color) new Color(255, 255, 255)));
		btnAddNewCollege.setBackground(Color.GRAY);
		btnAddNewCollege.setBounds(261, 124, 173, 40);
		getContentPane().add(btnAddNewCollege);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(68, 217, 569, 2);
		getContentPane().add(separator);
		
		comboBox = new JComboBox<String>();
		comboBox.setBounds(131, 272, 434, 28);
		getContentPane().add(comboBox);
		
		updateComboBox();
		
		JLabel lblSelectClass = new JLabel("Select Class");
		lblSelectClass.setHorizontalTextPosition(SwingConstants.CENTER);
		lblSelectClass.setHorizontalAlignment(SwingConstants.LEFT);
		lblSelectClass.setFont(new Font("Georgia", Font.PLAIN, 16));
		lblSelectClass.setBounds(131, 236, 148, 26);
		getContentPane().add(lblSelectClass);
		
		JLabel lblEnterUsn = new JLabel("Enter USN");
		lblEnterUsn.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterUsn.setFont(new Font("Georgia Pro", Font.PLAIN, 15));
		lblEnterUsn.setBounds(294, 401, 108, 26);
		getContentPane().add(lblEnterUsn);
		
		usn = new JTextField(30);
		usn.setHorizontalAlignment(SwingConstants.CENTER);
		usn.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(usn.getText().length()>10) e.consume();
			}
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
					name.requestFocus();
			}
		});
		usn.setFont(new Font("Tahoma", Font.PLAIN, 13));
		usn.setBounds(274, 429, 148, 28);
		getContentPane().add(usn);
		
		JLabel lblEnterName = new JLabel("Enter Name");
		lblEnterName.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterName.setFont(new Font("Georgia Pro", Font.PLAIN, 15));
		lblEnterName.setBounds(294, 467, 108, 26);
		getContentPane().add(lblEnterName);
		
		name = new JTextField(30);
		name.setHorizontalAlignment(SwingConstants.CENTER);
		name.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(name.getText().length()>30) e.consume();
			}
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
					addStudent();
			}
		});
		name.setFont(new Font("Tahoma", Font.PLAIN, 13));
		name.setBounds(210, 492, 276, 28);
		getContentPane().add(name);
		
		JButton btnAddStudent = new JButton("Add Student");
		btnAddStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addStudent();
			}
		});
		btnAddStudent.setHorizontalTextPosition(SwingConstants.CENTER);
		btnAddStudent.setForeground(Color.WHITE);
		btnAddStudent.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnAddStudent.setFocusTraversalKeysEnabled(false);
		btnAddStudent.setFocusPainted(false);
		btnAddStudent.setBorder(new MatteBorder(2, 2, 1, 1, (Color) new Color(255, 255, 255)));
		btnAddStudent.setBackground(new Color(186, 85, 211));
		btnAddStudent.setBounds(261, 540, 173, 40);
		getContentPane().add(btnAddStudent);
		
		usnStatus = new JLabel("Entered USN is already exists.");
		usnStatus.setVisible(false);
		usnStatus.setForeground(new Color(255, 0, 0));
		usnStatus.setHorizontalAlignment(SwingConstants.CENTER);
		usnStatus.setFont(new Font("Georgia Pro", Font.PLAIN, 15));
		usnStatus.setBounds(192, 365, 311, 26);
		getContentPane().add(usnStatus);
		
		JButton btnDeleteClass = new JButton("Delete Class");
		btnDeleteClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteClass();
			}
		});
		btnDeleteClass.setHorizontalTextPosition(SwingConstants.CENTER);
		btnDeleteClass.setForeground(Color.WHITE);
		btnDeleteClass.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnDeleteClass.setFocusTraversalKeysEnabled(false);
		btnDeleteClass.setFocusPainted(false);
		btnDeleteClass.setBorder(new MatteBorder(2, 2, 1, 1, (Color) new Color(255, 255, 255)));
		btnDeleteClass.setBackground(new Color(50, 205, 50));
		btnDeleteClass.setBounds(261, 310, 173, 40);
		getContentPane().add(btnDeleteClass);
		
	}

	
	public void addClass()
	{
		new AddClassDialog(frame,username);
	}
	
	public void addStudent()
	{
		try {
			Statement stm=con.createStatement();
			int index=getSelected();
			ResultSet rs=stm.executeQuery("SELECT usn FROM Attendance WHERE usn='"+usn.getText()+"' and class_id='"+class_id[index]+"';");
			if(rs.next()) {
				usnStatus.setVisible(true);
				usn.requestFocus();
			}
			else {
				usnStatus.setVisible(false);
				if(usn.getText().equals("") || name.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null,"All field should be filled.");
					usn.requestFocus();
				}
				else if(!usnDomainSatisfied())
					JOptionPane.showMessageDialog(null, "USN should not have spaces.","Message",JOptionPane.WARNING_MESSAGE);
				else if(!nameDomainSatisfied())
					JOptionPane.showMessageDialog(null, "Student name can only contain alphabets.","Message",JOptionPane.WARNING_MESSAGE);
				else
				{
					if(index==0) return;
					String query="INSERT INTO Attendance VALUES('"+usn.getText()+"','"+name.getText()+"','','"+class_id[index]+"');";
					if(stm.executeUpdate(query)>0)
					{
						JOptionPane.showMessageDialog(null, "Student data inserted successfully.");
						ManageData.updateComboBox();
						usn.setText("");
						name.setText("");
						usn.requestFocus();
						comboBox.setSelectedIndex(index);
					}
				}
			}
		}
		catch(SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Error occured adding new class");
			e.printStackTrace();
		}
	}
	
	public void deleteClass()
	{
		try {
			stmt=con.createStatement();
			int index=getSelected();
			if(index==0) return;
			int i=JOptionPane.showOptionDialog(null, "Do you really want to delete class "+class_name[index]
					+ "? \nDeleting will remove class details as well as all student details of the class from database!","Attendence Management System",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null, new String[] {"Yes","No"}, "No");
			if(i==JOptionPane.NO_OPTION)
				return;
			if((stmt.executeUpdate("DELETE FROM Class WHERE class_id='"+class_id[index]+"';"))!=0)
			{
				JOptionPane.showMessageDialog(null, "Class \'"+class_name[index]+"\' deleted successfully.");
				updateComboBox();
			}
			else
				JOptionPane.showMessageDialog(null, "Error occured while deleting the class entry.");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public int getSelected(){
		int index=comboBox.getSelectedIndex();
		if(index==0)
		{
			JOptionPane.showMessageDialog(null, "Please select the your class from drop down.");
		}
		return index;
	}
	
	public static void updateComboBox()
	{
		try {
			int count=0;
			stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			String query="SELECT class_id,class_name FROM Class WHERE username='"+username+"' ORDER BY class_name;";
			rs=stmt.executeQuery(query);
			while(rs.next()) 
				count++;
			class_id=new String[count+1];
			class_name=new String[count+1];
			rs.beforeFirst();
			int i=1;
			class_name[0]="--- Select your class --- ";
			while(rs.next()) {
				class_id[i]=rs.getString(1);
				class_name[i]=rs.getString(2)+" - "+rs.getString(1);
				i++;
			}
			comboBox.setModel(new DefaultComboBoxModel<String>(class_name));
			
		}
		catch(SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Error occured while fetching class details");
			e.printStackTrace();
		}
	}
	
	public  boolean usnDomainSatisfied()
	{
		Matcher m;
		Pattern r;
		r=Pattern.compile("^[a-zA-Z0-9]*$");
		m=r.matcher(usn.getText());
		if(m.find())
			return true;
		return false;
	}
	public  boolean nameDomainSatisfied()
	{
		Matcher m;
		Pattern r;
		r=Pattern.compile("^[a-zA-Z0-9()-_& ]*$");
		m=r.matcher(name.getText());
		if(m.find())
			return true;
		return false;
	}
}
