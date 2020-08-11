package mainPage;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.sql.*;
import java.util.Date;

import login.Login;

//import java.util.Arrays;
import com.toedter.calendar.JDateChooser;
//import java.beans.PropertyChangeListener;
//import java.beans.PropertyChangeEvent;

public class Attendance extends JFrame {
	private static final long serialVersionUID = 1L;
	private JFrame login;
	private JFrame home;
	
	private static Connection con=null;
	private static Statement stmt=null;
	private static ResultSet rs=null;
	
	private static String[] class_id,class_name;
	private static String[] usn,name,curAttendance,newAttendance;
	 
	private static int studentCount=0;
	private static JComboBox<String> comboBox;
	private static String username;
	private static JDateChooser date;
	private static JPanel attendance;
	private static JLabel nostudent;
	private boolean saveStatus=true;
	
	private static JLabel[] usnL,nameL,l;
	private static JRadioButton[] present,absent;
	private static ButtonGroup[] status;
	private static JButton[] edit,delete;
	private static JButton next,save,prev;
	private static JSeparator[] line;
	private static int start,end;
	
	public Attendance(String user) {
		super("Student Attendance Management System");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(!saveStatus) save();
				int i=JOptionPane.showOptionDialog(null, "Are you sure your want to exit application?","Attendence Management System",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null, new String[] {"Yes","No"}, "No");
				if(i==JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});
		con=Login.getCon();
		login=Login.getLoginFrame();
		home = UserMainPage.getHomeFrame();
		username=user;
		getContentPane().setBackground(new Color(235, 255, 235));
		getContentPane().setLayout(null);
		setVisible(true);
		setSize(700,800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		
		JLabel label = new JLabel("Student Attendance Management System");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(new Color(255, 0, 51));
		label.setFont(new Font("Algerian", Font.PLAIN, 27));
		label.setBounds(10, 10, 676, 36);
		getContentPane().add(label);
		
		JButton logout = new JButton("LOGOUT");
		logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!saveStatus) save();
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
		
		JButton button = new JButton("HOME");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!saveStatus) save();
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
		
		JLabel lblTakeAttendance = new JLabel("Take attendance");
		lblTakeAttendance.setHorizontalAlignment(SwingConstants.CENTER);
		lblTakeAttendance.setFont(new Font("Georgia Pro Cond Semibold", Font.PLAIN, 21));
		lblTakeAttendance.setBounds(132, 40, 421, 36);
		getContentPane().add(lblTakeAttendance);
		
		JLabel label_1 = new JLabel("Select Class");
		label_1.setHorizontalTextPosition(SwingConstants.CENTER);
		label_1.setHorizontalAlignment(SwingConstants.LEFT);
		label_1.setFont(new Font("Georgia", Font.PLAIN, 13));
		label_1.setBounds(29, 115, 93, 20);
		getContentPane().add(label_1);
		
		comboBox = new JComboBox<String>();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBox.getSelectedIndex()!=0)
				{
					if(!saveStatus) save();
					getStudentDetails();
					getCurrentAttendance();
					start=0;
					if(studentCount<20)
						end=studentCount;
					else 
						end=20;
					showStudentDetails();
				}
			}
		});
		comboBox.setBounds(104, 115, 434, 20);
		getContentPane().add(comboBox);
		updateComboBox();
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 74, 664, 2);
		getContentPane().add(separator);
		
		date = new JDateChooser();
//		date.addPropertyChangeListener(new PropertyChangeListener() {
//			public void propertyChange(PropertyChangeEvent evt) {
//				if(!getDate().equals("") && comboBox.getSelectedIndex()!=0)
//				{
//					if(!saveStatus) save();
//					getStudentDetails();
//					getCurrentAttendance();
//					start=0;
//					if(studentCount<20)
//						end=studentCount;
//					else 
//						end=20;
//					showStudentDetails();
//				}
//				else
//				{
//					
//				}
//			}
//		});
		date.getCalendarButton().setFont(new Font("Tahoma", Font.PLAIN, 13));
		date.setBounds(109, 85, 101, 20);
		getContentPane().add(date);
		date.setEnabled(false);
		
		setDate();
		
//		JLabel lblChooseDate = new JLabel("Choose Date");
//		lblChooseDate.setVisible(false);
//		lblChooseDate.setHorizontalTextPosition(SwingConstants.CENTER);
//		lblChooseDate.setHorizontalAlignment(SwingConstants.LEFT);
//		lblChooseDate.setFont(new Font("Georgia", Font.PLAIN, 13));
//		lblChooseDate.setBounds(29, 85, 93, 20);
//		getContentPane().add(lblChooseDate);
//		
//		JButton btnGetTodaysDate = new JButton("Get today's date");
//		btnGetTodaysDate.setVisible(false);
//		btnGetTodaysDate.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				setDate();
//			}
//		});
//		btnGetTodaysDate.setHorizontalTextPosition(SwingConstants.CENTER);
//		btnGetTodaysDate.setForeground(Color.WHITE);
//		btnGetTodaysDate.setFont(new Font("Tahoma", Font.PLAIN, 18));
//		btnGetTodaysDate.setFocusTraversalKeysEnabled(false);
//		btnGetTodaysDate.setFocusPainted(false);
//		btnGetTodaysDate.setBorder(new MatteBorder(2, 2, 1, 1, (Color) new Color(255, 255, 255)));
//		btnGetTodaysDate.setBackground(new Color(50, 205, 50));
//		btnGetTodaysDate.setBounds(220, 81, 162, 28);
//		getContentPane().add(btnGetTodaysDate);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 145, 664, 2);
		getContentPane().add(separator_1);
		
		attendance = new JPanel();
		attendance.setBackground(new Color(255, 255, 255));
		attendance.setBounds(25,197, 645, 560);
		attendance.setSize(new Dimension(645,560));
		getContentPane().add(attendance);
		attendance.setVisible(false);
		attendance.setLayout(null);
		
		nostudent = new JLabel("No data available");
		nostudent.setBackground(new Color(255, 255, 255));
		nostudent.setForeground(new Color(0, 0, 0));
		nostudent.setHorizontalTextPosition(SwingConstants.CENTER);
		nostudent.setHorizontalAlignment(SwingConstants.CENTER);
		nostudent.setFont(new Font("Georgia", Font.PLAIN, 25));
		nostudent.setBounds(62, 300, 560, 36);
		getContentPane().add(nostudent);
		
//		JButton addCol = new JButton("Add New");
//		addCol.setVisible(false);
//		addCol.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//			}
//		});
//		addCol.setHorizontalTextPosition(SwingConstants.CENTER);
//		addCol.setForeground(Color.WHITE);
//		addCol.setFont(new Font("Tahoma", Font.PLAIN, 18));
//		addCol.setFocusTraversalKeysEnabled(false);
//		addCol.setFocusPainted(false);
//		addCol.setBorder(new MatteBorder(2, 2, 1, 1, (Color) new Color(255, 255, 255)));
//		addCol.setBackground(new Color(238, 232, 170));
//		addCol.setBounds(387, 81, 101, 28);
//		getContentPane().add(addCol);
//		
//		JButton delCol = new JButton("Delete Column");
//		delCol.setVisible(false);
//		delCol.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				
//			}
//		});
//		delCol.setHorizontalTextPosition(SwingConstants.CENTER);
//		delCol.setForeground(Color.WHITE);
//		delCol.setFont(new Font("Tahoma", Font.PLAIN, 18));
//		delCol.setFocusTraversalKeysEnabled(false);
//		delCol.setFocusPainted(false);
//		delCol.setBorder(new MatteBorder(2, 2, 1, 1, (Color) new Color(255, 255, 255)));
//		delCol.setBackground(new Color(255, 140, 0));
//		delCol.setBounds(498, 81, 134, 28);
//		getContentPane().add(delCol);
		
		l=new JLabel[5];
		l[0] = new JLabel("USN");
		l[0].setOpaque(true);
		l[0].setBackground(new Color(169, 169, 169));
		l[0].setHorizontalTextPosition(SwingConstants.CENTER);
		l[0].setHorizontalAlignment(SwingConstants.CENTER);
		l[0].setFont(new Font("Georgia", Font.PLAIN, 13));
		l[0].setBounds(29, 178, 116, 20);
		getContentPane().add(l[0]);
		
		l[1] = new JLabel("Name");
		l[1].setOpaque(true);
		l[1].setBackground(new Color(169, 169, 169));
		l[1].setHorizontalTextPosition(SwingConstants.CENTER);
		l[1].setHorizontalAlignment(SwingConstants.CENTER);
		l[1].setFont(new Font("Georgia", Font.PLAIN, 13));
		l[1].setBounds(167, 178, 194, 20);
		getContentPane().add(l[1]);
		
		l[2] = new JLabel("Delete");
		l[2].setOpaque(true);
		l[2].setBackground(new Color(169, 169, 169));
		l[2].setHorizontalTextPosition(SwingConstants.CENTER);
		l[2].setHorizontalAlignment(SwingConstants.CENTER);
		l[2].setFont(new Font("Georgia", Font.PLAIN, 13));
		l[2].setBounds(595, 178, 71, 20);
		getContentPane().add(l[2]);
		
		l[3] = new JLabel("Edit");
		l[3].setOpaque(true);
		l[3].setBackground(new Color(169, 169, 169));
		l[3].setHorizontalTextPosition(SwingConstants.CENTER);
		l[3].setHorizontalAlignment(SwingConstants.CENTER);
		l[3].setFont(new Font("Georgia", Font.PLAIN, 13));
		l[3].setBounds(525, 178, 65, 20);
		getContentPane().add(l[3]);
		
		l[4] = new JLabel("Attendance");
		l[4].setOpaque(true);
		l[4].setBackground(new Color(169, 169, 169));
		l[4].setHorizontalTextPosition(SwingConstants.CENTER);
		l[4].setHorizontalAlignment(SwingConstants.CENTER);
		l[4].setFont(new Font("Georgia", Font.PLAIN, 13));
		l[4].setBounds(398, 178, 101, 20);
		getContentPane().add(l[4]);
		createComponents();
	}
	
	public void createComponents()
	{
		int fromTop=5;
		usnL=new JLabel[20];
		nameL=new JLabel[20];
		status=new ButtonGroup[20];
		present=new JRadioButton[20];
		absent=new JRadioButton[20];
		edit=new JButton[20];
		delete=new JButton[20];
		line=new JSeparator[20];
		for(int i=0; i<20; i++)
		{
			usnL[i]=new JLabel("");
			usnL[i].setVisible(false);
			attendance.add(usnL[i]);
			
			nameL[i]=new JLabel("");
			nameL[i].setVisible(false);
			attendance.add(nameL[i]);
			
			status[i]=new ButtonGroup();	
			
			present[i]=new JRadioButton("Present");
			present[i].setVisible(false);
			attendance.add(present[i]);
			
			absent[i]=new JRadioButton("Absent");
			absent[i].setVisible(false);
			attendance.add(absent[i]);
			status[i].add(present[i]);
			status[i].add(absent[i]);
			
			edit[i]=new JButton("Edit");
			edit[i].setVisible(false);
			edit[i].setEnabled(false);
			attendance.add(edit[i]);
			
			delete[i]=new JButton("Delete");
			delete[i].setVisible(false);
			attendance.add(delete[i]);
			
			line[i]=new JSeparator();
			line[i].setVisible(false);
			attendance.add(line[i]);
			
			
			usnL[i].setBounds(15,fromTop,100,20);
			
			nameL[i].setBounds(130,fromTop,240,20);
			
			present[i].setBounds(345,fromTop,70,20);
			present[i].setName(String.valueOf(i));
			present[i].setOpaque(false);
			present[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae)
				{
					saveStatus=false;
					int index=Integer.parseInt(((JRadioButton)ae.getSource()).getName());
					edit[index].setText("Reset");
					edit[index].setEnabled(true);
					
				}
			});
			
			absent[i].setBounds(420,fromTop,80,20);	
			absent[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae)
				{
					saveStatus=false;
					int index=Integer.parseInt(((JRadioButton)ae.getSource()).getName());
					edit[index].setText("Reset");
					edit[index].setEnabled(true);
					
				}
			});
			absent[i].setName(String.valueOf(i));
			absent[i].setOpaque(false);
			
			edit[i].setBounds(495,fromTop,70,20);
			edit[i].setName(String.valueOf(i));
			edit[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					int index=Integer.parseInt(((JButton)e.getSource()).getName());
					if(e.getActionCommand()=="Reset") {
						status[index].clearSelection();
						if(!present[index].isSelected() && !absent[index].isSelected())
						{
							edit[index].setText("Edit");
							edit[index].setEnabled(false);
						}
					}
					else {
					present[index].setEnabled(true);
					absent[index].setEnabled(true);
					edit[index].setText("Reset");
					}
				}
			});
			delete[i].setBounds(570,fromTop,70,20);
			delete[i].setName(String.valueOf(i));
			delete[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					int j=Integer.parseInt(((JButton)e.getSource()).getName());
					int k=JOptionPane.showOptionDialog(null, "Do you want to delete this student from the class?\nClass : "+class_name[j+1]+"\n"
							+ "USN : "+usn[j]+"\nName : "+name[j],"Attendence Management System",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null, new String[] {"Yes","No"}, "No");
					if(k==JOptionPane.YES_OPTION)
					{
						try {
							Statement stm=con.createStatement();
							String query="DELETE FROM attendance WHERE usn='"+usn[j]+"' AND class_id='"+class_id[getSelected()]+"';";
							if(stm.executeUpdate(query)==1)
							{
								JOptionPane.showMessageDialog(null, "Studnet data deleted successfully.");
								getStudentDetails();
								getCurrentAttendance();
								showStudentDetails();
							}
						}
						catch(SQLException ee)
						{
								JOptionPane.showMessageDialog(null, "Error occured while deleting student data.","Delete failed",JOptionPane.ERROR_MESSAGE);
								ee.printStackTrace();
						}
					}
				}
			});
			line[i].setBounds(10,fromTop+23,630,2);
			line[i].setForeground(Color.lightGray);
			fromTop+=26;
				
		}
		next = new JButton("NEXT>");
		next.setToolTipText("Save current details and shows next page.");
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(saveAttendance())
				{
					if(start+20<=studentCount)
						start=start+20;
					if(end+20<=studentCount)
						end=end+20;
					else
						end=studentCount;
					showStudentDetails();
				}
			}
		});
		next.setHorizontalTextPosition(SwingConstants.CENTER);
		next.setForeground(Color.WHITE);
		next.setFont(new Font("Tahoma", Font.PLAIN, 18));
		next.setFocusTraversalKeysEnabled(false);
		next.setFocusPainted(false);
		next.setBorder(new MatteBorder(2, 2, 1, 1, (Color) new Color(255, 255, 255)));
		next.setBackground(new Color(204, 51, 204));
		next.setBounds(372, 530, 83, 28);
		attendance.add(next);
		
		save = new JButton("SAVE");
		save.setToolTipText("Save details of current page");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(saveAttendance() && save.getText().equals("SUBMIT"))
				{
					JOptionPane.showMessageDialog(null, "Attendance successfully updated.");
					getStudentDetails();
					getCurrentAttendance();
					start=0;
					if(studentCount<20)
						end=studentCount;
					else 
						end=20;
					showStudentDetails();
				}
			}
		});
		save.setHorizontalTextPosition(SwingConstants.CENTER);
		save.setForeground(Color.WHITE);
		save.setFont(new Font("Tahoma", Font.PLAIN, 18));
		save.setFocusTraversalKeysEnabled(false);
		save.setFocusPainted(false);
		save.setBorder(new MatteBorder(2, 2, 1, 1, (Color) new Color(255, 255, 255)));
		save.setBackground(new Color(50, 205, 50));
		save.setBounds(281, 530, 83, 28);
		attendance.add(save);
		
		prev = new JButton("<PREV");
		prev.setToolTipText("Save current details and shows previous page.");
		prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(saveAttendance())
				{
					if(start-20>=0)
						start=start-20;
					end=start+20;
					showStudentDetails();
				}
			}
		});
		prev.setToolTipText("Save current details and shows next students.");
		prev.setHorizontalTextPosition(SwingConstants.CENTER);
		prev.setForeground(Color.WHITE);
		prev.setFont(new Font("Tahoma", Font.PLAIN, 18));
		prev.setFocusTraversalKeysEnabled(false);
		prev.setFocusPainted(false);
		prev.setBorder(new MatteBorder(2, 2, 1, 1, (Color) new Color(255, 255, 255)));
		prev.setBackground(new Color(204, 51, 204));
		prev.setBounds(188, 530, 83, 28);
		attendance.add(prev);
	}
	
	public void showStudentDetails()
	{
		clearAllInfo();
		if(studentCount==0) {
			attendance.setVisible(false);
			nostudent.setVisible(true);
		}
		else {
			attendance.setVisible(true);
			nostudent.setVisible(false);
		}
		
		for(int i=start; i<end; i++)
		{
			usnL[i%20].setText(usn[i]);
			usnL[i%20].setVisible(true);
			nameL[i%20].setText(name[i]);
			nameL[i%20].setVisible(true);
			present[i%20].setVisible(true);
			absent[i%20].setVisible(true);
			if(!curAttendance[i].equals(""))
			{
				if(curAttendance[i].equals("Present")) 
					present[i%20].setSelected(true);
				else if(curAttendance[i].equals("Absent")) 
					absent[i%20].setSelected(true);
				present[i%20].setEnabled(false);
				absent[i%20].setEnabled(false);
				edit[i%20].setEnabled(true);
			}
			else
			{
				present[i%20].setEnabled(true);
				absent[i%20].setEnabled(true);
				edit[i%20].setEnabled(false);
			}
			edit[i%20].setVisible(true);
			delete[i%20].setVisible(true);
		}
		next.setVisible(true);
		save.setVisible(true);
		prev.setVisible(true);

		if(start==0)
		{
			prev.setBackground(Color.LIGHT_GRAY);
			prev.setEnabled(false);
		}
		else
		{
			prev.setBackground(new Color(204,51,204));
			prev.setEnabled(true);
		}
		if(end>=studentCount)
		{
			save.setText("SUBMIT");
			next.setBackground(Color.LIGHT_GRAY);
			next.setEnabled(false);
		}
		else
		{
			save.setText("SAVE");
			next.setBackground(new Color(204,51,204));
			next.setEnabled(true);
		}
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
	
	public static void getStudentDetails()
	{
		try {
			int count=0;
			stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			int index=comboBox.getSelectedIndex();
			if(index==0) return;
			String query="SELECT usn, name FROM attendance WHERE class_id='"+class_id[index]+"' ORDER BY usn;";
			rs=stmt.executeQuery(query);
			while(rs.next()) 
				count++;
			usn=new String[count];
			name=new String[count];
			
			rs.beforeFirst();
			int i=0;
			while(rs.next()) {
				usn[i]=rs.getString(1);
				name[i]=rs.getString(2);
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
	
	public static void getCurrentAttendance()
	{
		try {
			stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			int index=comboBox.getSelectedIndex();
			if(index==0) return;
			String query="SELECT a_status FROM attendance WHERE class_id='"+class_id[index]+"' ORDER BY usn;";
			rs=stmt.executeQuery(query);
			curAttendance=new String[studentCount];
			newAttendance=new String[studentCount];
			for(int i=0; i<studentCount; i++) newAttendance[i]="";
			int i=0;
			while(rs.next()) {
				curAttendance[i]=rs.getString(1);
				i++;
			}
		}
		catch(SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Error occured while fetching class details");
			e.printStackTrace();
		}
	}
	
	public boolean saveAttendance()
	{
		for(int i=start; i<end; i++)
		{
			if(present[i%20].isSelected())
				newAttendance[i]="Present";
			else if(absent[i%20].isSelected())
				newAttendance[i]="Absent";
			else
				newAttendance[i]="";
		}
		try {
			con.setAutoCommit(false);
			Statement stm=con.createStatement();
			int index=comboBox.getSelectedIndex();
			for(int i=0; i<studentCount; i++)
			{
				String query="UPDATE attendance SET a_status='"+newAttendance[i]+"' WHERE usn='"+usn[i]+"' AND class_id='"+class_id[index]+"';";
				stm.executeUpdate(query);
			}
			con.commit();
			saveStatus=true;
			return true;
		}
		catch(SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Error occured while updating attendance. Please retry.","Update error",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			try{ 
				con.rollback();
				return false;
			} catch(SQLException ee) {
				e.printStackTrace();
				return false;
			}
		}
		
	}
	
	public void setDate()
	{
		Date date1=new Date();
		date.setDate(date1);
	}
	
//	public String getDate()
//	{
//		try {
//			StringBuilder date1=new StringBuilder(date.getDate().toString());
//			String date2="";
//			date2+=date1.substring(8, 10)+"-";
//			date2+=date1.substring(4, 7).toUpperCase()+"-";
//			date2+=date1.substring(24);
//			return date2;
//		}catch(Exception e) {return "";}
//	}
	
	public static int getSelected(){
		int index=comboBox.getSelectedIndex();
		if(index==0)
		{
			JOptionPane.showMessageDialog(null, "Please select the your class from drop down.");
		}
		return index;
	}
	
	public void clearAllInfo()
	{
		for(int i=0; i<20; i++)
			status[i].clearSelection();
		for(Component comp:attendance.getComponents())
		{
			if(comp instanceof JLabel)
				((JLabel) comp).setText("");
			comp.setVisible(false);
		}
	}
	
	public void save()
	{
		int i=JOptionPane.showOptionDialog(null, "You have unsaved changes? Do you want to save before you leave this page?","Attendence Management System",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null, new String[] {"Yes","No"}, "No");
		if(i==JOptionPane.YES_OPTION)
			saveAttendance();
		else
			return;
	}
}
