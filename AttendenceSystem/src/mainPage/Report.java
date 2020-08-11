package mainPage;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.sql.*;

import login.Login;

public class Report extends JFrame {
	private static final long serialVersionUID = 1L;
	private static Connection con=null;
	private static Statement stmt=null;
	private static ResultSet rs=null;
	private static String[] class_id,class_name;
	private static String[] usn,name,attend;
	private static int studentCount;
	private JScrollPane scrollTable;
	private JTable table;
	private DefaultTableModel model;
	private JLabel noStudent,l1,l2,l3;
	
	private static String username;
	private JFrame login;
	private JFrame homePage;
	private static JComboBox<String> comboBox;
	public Report(String user) {
		super("Student Attendance Management System");
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
		homePage=UserMainPage.getHomeFrame();
		username=user;
		setSize(700,700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		getContentPane().setBackground(new Color(235, 255, 235));
		getContentPane().setLayout(null);
		setVisible(true);
		
		JLabel label = new JLabel("Student Attendance Management System");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(new Color(255, 0, 51));
		label.setFont(new Font("Algerian", Font.PLAIN, 27));
		label.setBounds(10, 10, 676, 36);
		getContentPane().add(label);
		
		JButton home = new JButton("HOME");
		home.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				homePage.setVisible(true);
				dispose();
			}
		});
		home.setHorizontalTextPosition(SwingConstants.CENTER);
		home.setForeground(Color.WHITE);
		home.setFont(new Font("Tahoma", Font.PLAIN, 18));
		home.setFocusTraversalKeysEnabled(false);
		home.setFocusPainted(false);
		home.setBorder(new MatteBorder(2, 2, 1, 1, (Color) new Color(255, 255, 255)));
		home.setBackground(Color.BLUE);
		home.setBounds(488, 40, 83, 28);
		getContentPane().add(home);
		
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
		logout.setBounds(581, 40, 93, 28);
		getContentPane().add(logout);
		
		JLabel lblAttendanceReport = new JLabel("Attendance Report");
		lblAttendanceReport.setHorizontalAlignment(SwingConstants.CENTER);
		lblAttendanceReport.setFont(new Font("Georgia Pro Cond Semibold", Font.PLAIN, 21));
		lblAttendanceReport.setBounds(130, 40, 421, 36);
		getContentPane().add(lblAttendanceReport);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(14, 74, 660, 2);
		getContentPane().add(separator);
		
		JLabel label_1 = new JLabel("Select Class");
		label_1.setHorizontalTextPosition(SwingConstants.CENTER);
		label_1.setHorizontalAlignment(SwingConstants.LEFT);
		label_1.setFont(new Font("Georgia", Font.PLAIN, 13));
		label_1.setBounds(41, 86, 93, 20);
		getContentPane().add(label_1);
		
		comboBox = new JComboBox<String>();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBox.getSelectedIndex()!=0)
				{
					getStudentDetails();
					generate();
				}
			}
		});
		comboBox.setBounds(117, 86, 434, 20);
		getContentPane().add(comboBox);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(22, 116, 664, 2);
		getContentPane().add(separator_1);
		
		scrollTable = new JScrollPane();
		scrollTable.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		scrollTable.setBackground(new Color(255, 255, 255));
		scrollTable.setVisible(false);
		scrollTable.setBounds(72, 167, 538, 367);
		getContentPane().add(scrollTable);
		
		model=new DefaultTableModel();
		table = new JTable(model);
		table.setUpdateSelectionOnSort(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setShowGrid(true);
		table.setEnabled(false);
		table.setSelectionBackground(Color.BLACK);
		table.setIntercellSpacing(new Dimension(5,5));
		table.setRowHeight(20);
		table.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
		table.setForeground(Color.BLACK);
		table.setFillsViewportHeight(true);
		table.setBackground(Color.WHITE);
		model.addColumn("USN");
		model.addColumn("Student Name");
		model.addColumn("Attendance");
		table.getColumnModel().getColumn(0).setMaxWidth(120);
		table.getColumnModel().getColumn(2).setMaxWidth(100);
		((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		DefaultTableCellRenderer render=new DefaultTableCellRenderer();
		render.setHorizontalAlignment(SwingConstants.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(render);
		table.getColumnModel().getColumn(2).setCellRenderer(render);
		scrollTable.setViewportView(table);
		
		noStudent = new JLabel("No data available");
		noStudent.setBackground(new Color(255, 255, 255));
		noStudent.setForeground(new Color(0, 0, 0));
		noStudent.setHorizontalTextPosition(SwingConstants.CENTER);
		noStudent.setHorizontalAlignment(SwingConstants.CENTER);
		noStudent.setFont(new Font("Georgia", Font.PLAIN, 25));
		noStudent.setBounds(62, 300, 560, 36);
		getContentPane().add(noStudent);
		
		 l1 = new JLabel("");
		l1.setHorizontalTextPosition(SwingConstants.CENTER);
		l1.setHorizontalAlignment(SwingConstants.LEFT);
		l1.setForeground(Color.BLACK);
		l1.setFont(new Font("Georgia", Font.PLAIN, 20));
		l1.setBackground(Color.WHITE);
		l1.setBounds(72, 544, 560, 28);
		getContentPane().add(l1);
		
		 l2 = new JLabel("");
		l2.setHorizontalTextPosition(SwingConstants.CENTER);
		l2.setHorizontalAlignment(SwingConstants.LEFT);
		l2.setForeground(Color.BLACK);
		l2.setFont(new Font("Georgia", Font.PLAIN, 20));
		l2.setBackground(Color.WHITE);
		l2.setBounds(72, 571, 560, 28);
		getContentPane().add(l2);
		
		 l3 = new JLabel("");
		l3.setHorizontalTextPosition(SwingConstants.CENTER);
		l3.setHorizontalAlignment(SwingConstants.LEFT);
		l3.setForeground(Color.BLACK);
		l3.setFont(new Font("Georgia", Font.PLAIN, 20));
		l3.setBackground(Color.WHITE);
		l3.setBounds(72, 597, 560, 27);
		getContentPane().add(l3);
		
		getAllData();
		getStudentDetails();
	}
	
	public void getAllData()
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
			getStudentDetails();
		}
		catch(SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Error occured while fetching class details");
			e.printStackTrace();
		}
	}
	
	public static void getStudentDetails()
	{
		try {
			int count=0;
			stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			int index=comboBox.getSelectedIndex();
			if(index==0) return;
			String query="SELECT usn, name,a_status FROM attendance WHERE class_id='"+class_id[index]+"' ORDER BY usn;";
			rs=stmt.executeQuery(query);
			while(rs.next()) 
				count++;
			usn=new String[count];
			name=new String[count];
			attend=new String[count];
			
			rs.beforeFirst();
			int i=0;
			while(rs.next()) {
				usn[i]=rs.getString(1);
				name[i]=rs.getString(2);
				attend[i]=rs.getString(3);
				i++;
			}
			studentCount=count;
		}
		catch(SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Error occured while fetching class details");
			e.printStackTrace();
		}
	}
	
	public void generate()
	{
		int count=0;
		if(studentCount==0)
		{
			scrollTable.setVisible(false);
			noStudent.setVisible(true);
			l1.setVisible(false);
			l2.setVisible(false);
			l3.setVisible(false);
			model.setRowCount(0);
		}
		else
		{
			model.setRowCount(0);
			l1.setVisible(true);
			l2.setVisible(true);
			l3.setVisible(true);
			noStudent.setVisible(false);
			scrollTable.setVisible(true);
			for(int i=studentCount-1; i>=0; i--) {
				if(attend[i].equals("Present")) count++;
				model.insertRow(0, new String[] {usn[i],name[i],attend[i]});
			}
			}
		l1.setText("Present : "+count);
		l2.setText("Absent : "+(studentCount-count));
		l3.setText("Total count : "+studentCount);
		}
}
	
