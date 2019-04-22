import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

public class My_KeyPanel extends JPanel{
	public void paintComponent(Graphics g){
	super.paintComponent(g);
	this.setLayout(null);
	Graphics2D g2g = (Graphics2D) g;
	
	g2g.setColor(new Color(119,147,60)); //green
	g2g.fillOval(10, 30, 10, 10);
	JLabel the_green = new JLabel ("Ambulance"); the_green.setFont(new Font("SansSerif", Font.PLAIN,10));
	the_green.setBounds(27,0,80,67);
	super.add(the_green);
	
	g2g.setColor(new Color(55,96,146)); //blue
	g2g.fillOval(10, 60, 10, 10);
	JLabel the_blue = new JLabel ("Patient"); the_blue.setFont(new Font("SansSerif", Font.PLAIN,10));
	the_blue.setBounds(27,31,80,67);
	super.add(the_blue);
	
	g2g.setColor(new Color(149,55,53)); //red
	g2g.fillOval(10, 90, 10, 10);
	JLabel the_red = new JLabel ("Station"); the_red.setFont(new Font("SansSerif", Font.PLAIN,10));
	the_red.setBounds(27,61,80,67);
	super.add(the_red);
	
	g2g.setColor(new Color(96,74,123)); //purple
	g2g.fillOval(10, 120, 10, 10);
	JLabel the_purple = new JLabel ("Hospital"); the_purple.setFont(new Font("SansSerif", Font.PLAIN,10));
	the_purple.setBounds(27,91,80,67);
	super.add(the_purple);
	
	JLabel key = new JLabel ("Key"); key.setFont(new Font("SansSerif", Font.PLAIN,12));
	key.setBounds(10,2,50,20);
	super.add(key);
		
	}
}
