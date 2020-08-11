package login;
import javax.swing.*;

import mainPage.UserMainPage;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Login {
	private static JFrame frame;
	private JTextField user;
	private JPasswordField pass;

	private static Connection con=null;
	private static Statement stmt=null;
	private static ResultSet rs=null;
	
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL="jdbc:mysql://localhost:3306?useSSL=false";
	
	static final String USER = "DBMS_Project";
	static final String PASS = "project";
	private JTextField name;
	private JTextField phone;
	private JTextField uname;
	private JPasswordField pass1;
	private JPasswordField pass2;
	private JPanel registerPanel,loginPanel;

	public Login() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame("Attendence Management System");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int i=JOptionPane.showOptionDialog(null, "Are you sure your want to exit application?","Attendence Management System",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null, new String[] {"Yes","No"}, "No");
				if(i==JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});
		frame.getContentPane().setBackground(new Color(255, 248, 220));
		frame.getContentPane().setLayout(null);
		frame.setSize(700,700);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		JLabel title = new JLabel("Student Attendance Management System");
		title.setFont(new Font("Algerian", Font.PLAIN, 27));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(10, 94, 676, 77);
		frame.getContentPane().add(title);
		
		loginPanel = new JPanel();
		loginPanel.setBackground(new Color(0, 255, 127));
		loginPanel.setBounds(127, 163, 441, 376);
		frame.getContentPane().add(loginPanel);
		loginPanel.setLayout(null);
		
		JLabel username = new JLabel("USERNAME");
		username.setBounds(165, 88, 110, 26);
		loginPanel.add(username);
		username.setHorizontalTextPosition(SwingConstants.CENTER);
		username.setHorizontalAlignment(SwingConstants.CENTER);
		username.setFont(new Font("Georgia", Font.PLAIN, 15));
		
		user = new JTextField("");
		user.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
					pass.requestFocus();
			}
			public void keyTyped(KeyEvent e) {
				if(user.getText().length()>10) e.consume();
			}
		});
		user.setBounds(109, 124, 223, 35);
		loginPanel.add(user);
		user.setToolTipText("Username");
		user.setSelectionColor(SystemColor.textHighlight);
		user.setHorizontalAlignment(SwingConstants.CENTER);
		user.setFont(new Font("Tahoma", Font.PLAIN, 16));
		user.setColumns(10);
		user.setBackground(new Color(245, 255, 250));
		
		JLabel password = new JLabel("PASSWORD");
		password.setBounds(165, 185, 110, 20);
		loginPanel.add(password);
		password.setHorizontalTextPosition(SwingConstants.CENTER);
		password.setHorizontalAlignment(SwingConstants.CENTER);
		password.setFont(new Font("Georgia", Font.PLAIN, 15));
		
		pass = new JPasswordField();
		pass.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
					login();
			}
			public void keyTyped(KeyEvent e) {
				if(String.valueOf(pass.getPassword()).length()>16) e.consume();
			}
		});
		pass.setBounds(109, 215, 223, 35);
		loginPanel.add(pass);
		pass.setToolTipText("Password");
		pass.setHorizontalAlignment(SwingConstants.CENTER);
		pass.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pass.setDisabledTextColor(SystemColor.textInactiveText);
		pass.setBackground(new Color(245, 255, 250));
		
		JButton login = new JButton("LOGIN");
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		
		login.setBounds(175, 283, 91, 35);
		loginPanel.add(login);
		login.setForeground(Color.WHITE);
		login.setFont(new Font("Century Gothic", Font.BOLD, 15));
		login.setFocusTraversalPolicyProvider(true);
		login.setDefaultCapable(false);
		login.setBorder(null);
		login.setBackground(Color.BLUE);
		
		JLabel register = new JLabel("New user? Click here to register!");
		register.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				registerPanel.setVisible(true);
				loginPanel.setVisible(false);
			}
		});
		register.setBounds(99, 340, 242, 19);
		loginPanel.add(register);
		register.setToolTipText("Click here to register");
		register.setHorizontalTextPosition(SwingConstants.CENTER);
		register.setHorizontalAlignment(SwingConstants.CENTER);
		register.setForeground(Color.BLUE);
		register.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JLabel lblLogin = new JLabel("LOGIN");
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogin.setFont(new Font("Georgia Pro Cond Semibold", Font.PLAIN, 21));
		lblLogin.setBounds(10, 10, 421, 77);
		loginPanel.add(lblLogin);
		
		registerPanel = new JPanel();
		registerPanel.setBackground(new Color(255, 204, 255));
		registerPanel.setBounds(127, 163, 441, 468);
		frame.getContentPane().add(registerPanel);
		registerPanel.setLayout(null);
		registerPanel.setVisible(false);
		
		JLabel nameL = new JLabel("Full Name : ");
		nameL.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 15));
		nameL.setBounds(29, 71, 138, 19);
		registerPanel.add(nameL);
		
		name = new JTextField();
		name.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if(name.getText().length()>=50) e.consume();
			}
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					phone.requestFocus();
				}
			}
		});
		name.setFont(new Font("Tahoma", Font.PLAIN, 14));
		name.setColumns(10);
		name.setBounds(29, 100, 373, 29);
		registerPanel.add(name);
		
		JLabel phoneL = new JLabel("Phone number : ");
		phoneL.setName("");
		phoneL.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 14));
		phoneL.setBounds(29, 149, 169, 19);
		registerPanel.add(phoneL);
		
		phone = new JTextField();
		phone.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if(phone.getText().length()>=10) e.consume();
			}
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					uname.requestFocus();
				}
			}
		});
		phone.setHorizontalAlignment(SwingConstants.CENTER);
		phone.setFont(new Font("Tahoma", Font.PLAIN, 12));
		phone.setColumns(10);
		phone.setBounds(161, 144, 121, 29);
		registerPanel.add(phone);
		
		JLabel lblRegister = new JLabel("REGISTER");
		lblRegister.setHorizontalAlignment(SwingConstants.CENTER);
		lblRegister.setFont(new Font("Georgia Pro Cond Semibold", Font.PLAIN, 21));
		lblRegister.setBounds(0, 0, 421, 77);
		registerPanel.add(lblRegister);
		
		JLabel createUsername = new JLabel("Create a new username  :  ");
		createUsername.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 14));
		createUsername.setBounds(19, 216, 243, 14);
		registerPanel.add(createUsername);
		
		uname = new JTextField();
		uname.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(uname.getText().length()>=10) e.consume();
			}
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					pass1.requestFocus();
				}
			}
		});
		uname.setFont(new Font("Tahoma", Font.PLAIN, 12));
		uname.setColumns(10);
		uname.setBounds(19, 240, 178, 29);
		registerPanel.add(uname);
		
		JLabel passL = new JLabel("Create a new password  :  ");
		passL.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 14));
		passL.setBounds(19, 289, 243, 14);
		registerPanel.add(passL);
		
		pass1 = new JPasswordField();
		pass1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(String.valueOf(pass1.getPassword()).length()>=16) e.consume();
			}
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					pass2.requestFocus();
				}
			}
		});
		pass1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		pass1.setColumns(10);
		pass1.setBounds(20, 313, 178, 21);
		registerPanel.add(pass1);
		
		JLabel cpassL = new JLabel("Confirm password :");
		cpassL.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 14));
		cpassL.setBounds(19, 344, 179, 14);
		registerPanel.add(cpassL);
		
		pass2 = new JPasswordField();
		pass2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(String.valueOf(pass2.getPassword()).length()>=10) e.consume();
			}
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					register();
				}
			}
		});
		pass2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		pass2.setColumns(10);
		pass2.setBounds(19, 368, 178, 21);
		registerPanel.add(pass2);
		
		JButton registerButton = new JButton("Register");
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				register();
			}
		});
		registerButton.setForeground(Color.WHITE);
		registerButton.setFont(new Font("Century Gothic", Font.BOLD, 15));
		registerButton.setFocusTraversalPolicyProvider(true);
		registerButton.setDefaultCapable(false);
		registerButton.setBorder(null);
		registerButton.setBackground(Color.BLUE);
		registerButton.setBounds(175, 399, 91, 35);
		registerPanel.add(registerButton);
		
		JLabel cancel = new JLabel("Already registered? Click here to login");
		cancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loginPanel.setVisible(true);
				registerPanel.setVisible(false);
			}
		});
		cancel.setToolTipText("");
		cancel.setHorizontalTextPosition(SwingConstants.CENTER);
		cancel.setHorizontalAlignment(SwingConstants.CENTER);
		cancel.setForeground(Color.BLUE);
		cancel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cancel.setBounds(29, 439, 392, 19);
		registerPanel.add(cancel);
		
	}

	public void login()
	{
		if(user.getText().equals("")) {
			JOptionPane.showMessageDialog(frame, "Username is required !","Invalid credentials" , JOptionPane.WARNING_MESSAGE);
			user.requestFocus();
			return;
		}
		if(String.valueOf(pass.getPassword()).equals("")) {
			JOptionPane.showMessageDialog(frame, "Password cannot be blank !","Invalid credentials" , JOptionPane.WARNING_MESSAGE);
			pass.requestFocus();
			return;
		}
		String user,pass;
		user=this.user.getText();
		pass=String.valueOf(this.pass.getPassword());
		try {
			stmt=con.createStatement();
			String qry="SELECT username,password FROM UserAccounts WHERE username=\'"+user+"\' AND password=\'"+pass+"\';" ;
			rs=stmt.executeQuery(qry);
			if(rs.next()) {
				this.user.setText("");
				this.pass.setText("");
			   UserMainPage userPage = new UserMainPage(user);
			   this.user.requestFocus();
			   userPage.setVisible(true);
			   frame.setVisible(false);
			}
			else
			{
				JOptionPane.showMessageDialog(frame, "Invalid username or password");
				this.pass.setText("");
				this.user.requestFocus();
			}
		} 
		catch(SQLException e)
		{
			JOptionPane.showMessageDialog(frame, e.getMessage(), e.getMessage()	, JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Login();
					Login.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		try {
			Class.forName(JDBC_DRIVER);
		} catch(ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Unable to load driver!");
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Connecting to database...");
		try {
			con=DriverManager.getConnection(DB_URL,USER,PASS);
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, "Unable to connect to the database!");
			e.printStackTrace();
			System.exit(1);
		}

		try 
		{
			stmt=con.createStatement();
			System.out.println("Connection established.");
			
			if(!checkDBExists(con,"attendance_system")) {
				System.out.println("Creating database...");
				stmt.executeUpdate("CREATE DATABASE attendance_system");
				System.out.println("Database created successfully.");
			}
			stmt.executeUpdate("USE attendance_system");
			
			//Create userAccounts Table
			rs=con.getMetaData().getTables("attendance_system",null,"UserAccounts",null);
			if(!rs.next()) //If table not available create it 
			{
				System.out.println("Creating table UserAccounts...");
				stmt.executeUpdate("CREATE TABLE UserAccounts (username varchar(10) PRIMARY KEY, name varchar(50), phone varchar(15), password varchar(16) NOT NULL) ENGINE=InnoDB;");
				System.out.println("Table created successfully.");
			}

			
			//Create class table
			rs=con.getMetaData().getTables("attendance_system",null,"Class",null);
			if(!rs.next()) //If table not available create it 
			{
				System.out.println("Creating table Class...");
				String table="CREATE TABLE Class (class_id varchar(10) PRIMARY KEY, class_name varchar(50), username varchar(10),"
						+ " FOREIGN KEY(username) REFERENCES UserAccounts(username) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB; ";
				stmt.executeUpdate(table);
				System.out.println("Table created successfully.");
			}
			
			//Create attendance table
			rs=con.getMetaData().getTables("attendance_system",null,"Attendance",null);
			if(!rs.next()) //If table not available create it 
			{
				System.out.println("Creating table Attendance...");
				String table="CREATE TABLE Attendance (usn varchar(10), name varchar(30), a_status varchar(10), class_id varchar(10),"
						+ " PRIMARY KEY(usn,class_id),"
						+ " FOREIGN KEY(class_id) REFERENCES Class(class_id) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB; ";
				stmt.executeUpdate(table);
				System.out.println("Table created successfully.");
			}
		}
		catch(SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Error encountered");
			e.printStackTrace();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Error encountered");
			e.printStackTrace();
		}
	}
	
	public static boolean checkDBExists(Connection con, String DB) throws SQLException
	{
		rs=con.getMetaData().getCatalogs();
		while(rs.next())
		{	
			if(DB.equals(rs.getString(1)))
				return true;
		}
		return false;
	}
	
	public void register()
	{
		if(!successfulValidateDetails())
			{
					return;
				}
		if(!usernameDomainSatisfied(uname.getText()))
		{
			uname.setBackground(new Color(255,255,180));
			JOptionPane.showMessageDialog(null, "Username can contailn alphabets, digits and _@.", "Invalid field value", JOptionPane.WARNING_MESSAGE);
			return;
		}
		uname.setBackground(Color.white);
		
		if(!validatedUsername()) {
			uname.requestFocus();
			return;
		} 
		
		if(!passwordValidate()) {
			pass1.requestFocus();
			return;
		} 
		
		
		
		try {
			stmt=con.createStatement();
			int i=stmt.executeUpdate("INSERT INTO UserAccounts VALUES('"+uname.getText()+"','"+name.getText()+"','"+phone.getText()+"','"+String.valueOf(pass1.getPassword())+"');");
			if(i>0) {
				JOptionPane.showMessageDialog(null, "Registration Successful");
				loginPanel.setVisible(true);
				registerPanel.setVisible(false);
			}
			else
				JOptionPane.showMessageDialog(null, "Registration Failed!");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	public boolean validatedUsername() {
		int count=-1;
		if(uname.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Username is required.", "Username Required", JOptionPane.INFORMATION_MESSAGE);
			uname.requestFocus();
			return false;
		}
		try {
			stmt=con.createStatement();
			rs=stmt.executeQuery("SELECT COUNT(username) FROM UserAccounts WHERE username='"+uname.getText()+"';");
			rs.next();
			count=rs.getInt(1);
		}
		catch(SQLException e) { e.printStackTrace(); }
		
		if(count>0)
		{
			JOptionPane.showMessageDialog(null, "Username already exists!");
			return false;
		}
		return true;
	}
	
	public boolean passwordValidate() 
	{
		if(String.valueOf(pass1.getPassword()).equals("")) {
			JOptionPane.showMessageDialog(null, "Please enter password (8-15 characters)", "Password Required", JOptionPane.INFORMATION_MESSAGE);
			pass1.requestFocus();
			return false;
		}
		if(String.valueOf(pass1.getPassword()).length()==0) {
			JOptionPane.showMessageDialog(null, "Password should have atleast one character", "Invalid Password", JOptionPane.INFORMATION_MESSAGE);
			pass1.requestFocus();
			return false;
		}
		if(!String.valueOf(pass1.getPassword()).equals(String.valueOf(pass2.getPassword()))) {
			JOptionPane.showMessageDialog(null, "Passwords do not match!", "Password Mismatch", JOptionPane.INFORMATION_MESSAGE);
			pass2.requestFocus();
			return false;
		}
		return true;
	}
	
	public static boolean usernameDomainSatisfied(String text)
	{
		Matcher m;
		Pattern r;
		r=Pattern.compile("^[a-zA-Z0-9_@.]+$");
		m=r.matcher(text);
		if(m.find())
			return true;
		return false;
	}
	public boolean successfulValidateDetails()
	{
		if(name.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Full name is required ..", "Requirements", JOptionPane.INFORMATION_MESSAGE);
			name.requestFocus();
			name.setBackground(new Color(255,255,180));
			return false;
		}
		name.setBackground(Color.white);
		
		if(phone.getText().length()<10) {
			JOptionPane.showMessageDialog(null, "Phone number must contain 10 digits.", "Requirements", JOptionPane.INFORMATION_MESSAGE);
			phone.requestFocus();
			phone.setBackground(new Color(255,255,180));
			return false;
		}
		phone.setBackground(Color.white);
		
		
		if(!nameDomainSatisfied())
		{
			name.setBackground(new Color(255,255,180));
			JOptionPane.showMessageDialog(null, "Name can contain alphabets and whitespace only!", "Invalid field value", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		name.setBackground(Color.white);
		
		
		if(!phoneDomainSatisfied())
		{
			phone.setBackground(new Color(255,255,180));
			JOptionPane.showMessageDialog(null, "Contact number should contain only 0-9 digits", "Invalid field value", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		phone.setBackground(Color.white);
		
		return true;
	}
	
	public  boolean nameDomainSatisfied()
	{
		Matcher m;
		Pattern r;
		r=Pattern.compile("^[a-zA-Z ]*$");
		m=r.matcher(name.getText());
		if(m.find())
			return true;
		return false;
	}
	
	public boolean phoneDomainSatisfied()
	{
		Matcher m;
		Pattern r;
		r=Pattern.compile("^[0-9]{10}$");
		m=r.matcher(phone.getText());
		if(m.find())
			return true;
		return false;
	}
	
	public static Connection getCon() {
		return con;
	}
	
	public static JFrame getLoginFrame() {
		return frame;
	}
}
