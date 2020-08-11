package mainPage;

import mainPage.ManageData;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import login.Login;

public class AddClassDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private JTextField class_id;
	private static Connection con=null;
	private JLabel status;
	private JTextField cname;
	private static String username;
	private JButton btn ;
	public AddClassDialog(JFrame frame, String uname) {
		super(frame, "Add new class", Dialog.ModalityType.DOCUMENT_MODAL);
		con=Login.getCon();
		username=uname;
		getContentPane().setBackground(new Color(255, 248, 220));
		setSize(400,150);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		JLabel cid = new JLabel("Class ID :");
		cid.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cid.setBounds(10, 16, 73, 13);
		getContentPane().add(cid);
		
		status = new JLabel("Class ID already exists. ");
		status.setVisible(false);
		
		class_id = new JTextField(30);
		class_id.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(class_id.getText().length()>10) e.consume();
			}
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
					cname.requestFocus();
			}
		});
		class_id.setBounds(74, 10, 95, 24);
		getContentPane().add(class_id);
		status.setForeground(Color.RED);
		status.setFont(new Font("Tahoma", Font.PLAIN, 14));
		status.setBounds(179, 10, 156, 25);
		getContentPane().add(status);
		
		JLabel cn=new JLabel("Class Name :");
		cn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cn.setBounds(10, 50, 88, 13);
		getContentPane().add(cn);
		
		cname=new JTextField();
		cname.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(class_id.getText().length()>50) e.consume();
			}
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
					addClass();
			}
		});
		cname.setBounds(98, 44, 278, 25);
		getContentPane().add(cname);
		
		btn = new JButton("ADD");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addClass();
			}
		});
		btn.setHorizontalTextPosition(SwingConstants.CENTER);
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btn.setFocusTraversalKeysEnabled(false);
		btn.setFocusPainted(false);
		btn.setBorder(new MatteBorder(2, 2, 1, 1, (Color) new Color(255, 255, 255)));
		btn.setBackground(Color.BLUE);
		btn.setBounds(156, 73, 73, 30);
		getContentPane().add(btn);
		setVisible(true);
	}
	
	public void addClass()
	{
		try {
			Statement stm=con.createStatement();
			ResultSet rs=stm.executeQuery("SELECT class_id FROM Class WHERE class_id='"+class_id.getText()+"';");
			if(rs.next())
				status.setVisible(true);
			else {
				status.setVisible(false);
				if(class_id.getText().equals("") || cname.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null,"All field should be filled.");
					class_id.requestFocus();
				}
				else if(!idDomainSatisfied())
					JOptionPane.showMessageDialog(null, "Class id should not contain spaces.","Message",JOptionPane.WARNING_MESSAGE);
				else if(!nameDomainSatisfied())
					JOptionPane.showMessageDialog(null, "Class name can only contain alphabets, digits and -_&()[]","Message",JOptionPane.WARNING_MESSAGE);
				else
				{
					String query="INSERT INTO Class VALUES('"+class_id.getText()+"','"+cname.getText()+"','"+username+"');";
					if(stm.executeUpdate(query)>0)
					{
						JOptionPane.showMessageDialog(null, "New Class inserted successfully.");
						ManageData.updateComboBox();
						dispose();
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
	public  boolean idDomainSatisfied()
	{
		Matcher m;
		Pattern r;
		r=Pattern.compile("^[a-zA-Z0-9]*$");
		m=r.matcher(class_id.getText());
		if(m.find())
			return true;
		return false;
	}
	public  boolean nameDomainSatisfied()
	{
		Matcher m;
		Pattern r;
		r=Pattern.compile("^[a-zA-Z0-9()-_& ]*$");
		m=r.matcher(cname.getText());
		if(m.find())
			return true;
		return false;
	}
}
