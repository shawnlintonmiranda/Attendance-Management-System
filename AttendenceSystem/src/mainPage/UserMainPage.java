package mainPage;

import javax.swing.*;

import login.Login;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.border.MatteBorder;

public class UserMainPage extends JFrame {
	private static final long serialVersionUID = 1L;
	private static JFrame home;
	private static Connection con=null;
	private static Statement stmt=null;
	private static ResultSet rs=null;
	
	private String username;
	private static JFrame login;
	private JLabel name;

	public UserMainPage(String user) {
		super("Student Attendance Management System");
		home=this;
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
		username=user;
		getContentPane().setBackground(new Color(235, 255, 235));
		getContentPane().setLayout(null);
		setSize(700,700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		
		JLabel title = new JLabel("Student Attendance Management System");
		title.setForeground(new Color(255, 0, 51));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Algerian", Font.PLAIN, 27));
		title.setBounds(10, 95, 676, 40);
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
		logout.setBounds(532, 163, 105, 28);
		getContentPane().add(logout);
		
		JLabel lblLoggedInAs = new JLabel("Logged in as ");
		lblLoggedInAs.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 12));
		lblLoggedInAs.setBounds(54, 149, 101, 19);
		getContentPane().add(lblLoggedInAs);
		
		name = new JLabel("");
		name.setFont(new Font("Rockwell Nova", Font.PLAIN, 19));
		name.setBounds(74, 162, 470, 29);
		getContentPane().add(name);
		
		JButton btnManaage = new JButton("Manage Class & Students details");
		btnManaage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ManageData(username);
				home.setVisible(false);
			}
		});
		btnManaage.setHorizontalTextPosition(SwingConstants.CENTER);
		btnManaage.setForeground(Color.WHITE);
		btnManaage.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnManaage.setFocusTraversalKeysEnabled(false);
		btnManaage.setFocusPainted(false);
		btnManaage.setBorder(new MatteBorder(2, 2, 1, 1, (Color) new Color(255, 255, 255)));
		btnManaage.setBackground(new Color(51, 0, 255));
		btnManaage.setBounds(207, 267, 281, 40);
		getContentPane().add(btnManaage);
		
		JButton btnAttendence = new JButton("Take Attendance");
		btnAttendence.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Attendance(username);
				home.setVisible(false);
			}
		});
		btnAttendence.setHorizontalTextPosition(SwingConstants.CENTER);
		btnAttendence.setForeground(Color.WHITE);
		btnAttendence.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnAttendence.setFocusTraversalKeysEnabled(false);
		btnAttendence.setFocusPainted(false);
		btnAttendence.setBorder(new MatteBorder(2, 2, 1, 1, (Color) new Color(255, 255, 255)));
		btnAttendence.setBackground(new Color(255, 153, 0));
		btnAttendence.setBounds(207, 333, 281, 40);
		getContentPane().add(btnAttendence);
		
		JButton btnViewAttendenceReport = new JButton("View Attendence Report");
		btnViewAttendenceReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Report(username);
				home.setVisible(false);
			}
		});
		btnViewAttendenceReport.setHorizontalTextPosition(SwingConstants.CENTER);
		btnViewAttendenceReport.setForeground(Color.WHITE);
		btnViewAttendenceReport.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnViewAttendenceReport.setFocusTraversalKeysEnabled(false);
		btnViewAttendenceReport.setFocusPainted(false);
		btnViewAttendenceReport.setBorder(new MatteBorder(2, 2, 1, 1, (Color) new Color(255, 255, 255)));
		btnViewAttendenceReport.setBackground(new Color(0, 204, 0));
		btnViewAttendenceReport.setBounds(207, 398, 281, 40);
		getContentPane().add(btnViewAttendenceReport);
		
		JButton btnDeleteAccount = new JButton("Delete Account");
		btnDeleteAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteAccount();
			}
		});
		btnDeleteAccount.setHorizontalTextPosition(SwingConstants.CENTER);
		btnDeleteAccount.setForeground(Color.WHITE);
		btnDeleteAccount.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnDeleteAccount.setFocusTraversalKeysEnabled(false);
		btnDeleteAccount.setFocusPainted(false);
		btnDeleteAccount.setBorder(new MatteBorder(2, 2, 1, 1, (Color) new Color(255, 255, 255)));
		btnDeleteAccount.setBackground(new Color(255, 51, 51));
		btnDeleteAccount.setBounds(501, 201, 136, 28);
		getContentPane().add(btnDeleteAccount);
		getUserDetails();
	}
	
	public void getUserDetails()
	{
		try {
			stmt=con.createStatement();
			rs=stmt.executeQuery("SELECT name FROM UserAccounts WHERE username='"+username+"';");
			rs.next();
			name.setText(rs.getString(1));
		}
		catch(SQLException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getMessage(), JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	public static JFrame getHomeFrame()
	{
		return home;
	}
	
	public void deleteAccount()
	{
		try {
			stmt=con.createStatement();
			int i=JOptionPane.showOptionDialog(null, "Do you really want to delete account?"
					+ "? \nDeleting will wipe out all your class and student details!","Attendence Management System",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null, new String[] {"Yes","No"}, "No");
			if(i==JOptionPane.NO_OPTION)
				return;
			if((stmt.executeUpdate("DELETE FROM useraccounts WHERE username='"+username+"';"))!=0)
			{
				JOptionPane.showMessageDialog(null, "Account deleted successfully.");
				login.setVisible(true);
				dispose();
			}
			else
				JOptionPane.showMessageDialog(null, "Error occured while deleting account.");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
}
