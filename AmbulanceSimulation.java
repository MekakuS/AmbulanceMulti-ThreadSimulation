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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;
import java.util.TimerTask;

import java.util.Timer;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;



//Name: Seung Min Lee UPI: slee828
//NOTE:::::::::: Please don't click on the JTABLE as it will break the table.
//Assignment 3
//NOTE:::::::::: There is System prints, commented out to check if the thread is working.

public class AmbulanceSimulation extends JPanel implements ActionListener {
	//first frame:
	private int super_timer_counter = 0;
	private JLabel Introduction;
	private JLabel Duration;
	private JButton Start;
	private JButton Stop;
	private JFrame Frame;
	private JPanel Ambulance_Simulation_Panel;
	private JTable table;
	public Border border = new LineBorder(new Color(56,93,138), 3);
	private JScrollPane scrollPane;
	private JTextField textfield;
	private int user_click_start_or_stop = 0;
	private int super_my_duration =0;
	
	private ArrayList<String> ambulance_data;// for showing the data on the screen.
	private ArrayList<String> patients_data; 
	private ArrayList<ArrayList> final_ambulance_data;
	private ArrayList<ArrayList> final_patient_data = new ArrayList<ArrayList>();
	private ArrayList<SwingWorkerClass> for_stoping_threads = new ArrayList<SwingWorkerClass>();
	private String[] columnNames = {"ID",  "Location", "Status", "Patient"};
	private DefaultTableModel ambulance_table_updater;
	
	//second frame:
	private JLabel Introduction_2;
	private JPanel the_simulation;
	private JFrame Simulation_frame;
	private My_SimulationPanel the_simulation_itself;
	static int greenCount = 0;
	static int blueCount = 0;
	static int redCount = 0;
	static ReentrantLock counterLock = new ReentrantLock(true);
	static ReentrantLock counterLock2 = new ReentrantLock(true);
	static ReentrantLock counterLock3 = new ReentrantLock(true);
	public AmbulanceSimulation(){
		
		FileReader input_stream = null;
		ambulance_data = new ArrayList<String>(); // this has all the information of each row in the file as a string format.
		final_ambulance_data = new ArrayList<ArrayList>();
		int the_counter = 1;
		Scanner scan = null;
		try {
			input_stream = new FileReader("ambulances.csv");
			scan = new Scanner(input_stream);
			while (scan.hasNextLine()) {
			    String[] row = scan.nextLine().trim().split(",",5);
			    if(the_counter != 1){
			    String String1 = row[0].trim();
				String String2 = row[1].trim();
				String String3 = row[2].trim();
				String String4 = row[3].trim();
				String String5 = row[4].trim();
				if (String1.isEmpty() || String1.equals("None")){String1 = "-";} if (String2.isEmpty() || String2.equals("None")){String2 = "-";}
				if (String3.isEmpty() || String3.equals("None")){String3 = "-";} if (String4.isEmpty() || String4.equals("None")){String4 = "-";}
				if (String5.isEmpty() || String5.equals("None")){String5 = "-";}
				String final_string = String1 + ","+String2+","+String3+","+String4+","+String5;
			    ambulance_data.add(final_string);
			    ArrayList<String>  myList = new ArrayList<String>(Arrays.asList(final_string.split(",")));
			    final_ambulance_data.add(myList);
			    }
				the_counter+=1;}} 
		
		catch (FileNotFoundException e) {
				e.printStackTrace();}
		finally{scan.close();}
		int realcounter = 0;
		Object[][] my_data = new Object[ambulance_data.size()][4];
		for(String i: ambulance_data){
			String[] testing = i.split(",",5);
			String location = "("+ testing[1]+ ","+ testing[2]+")".replace("\"","");
			my_data[realcounter][0] = testing[0].replace("\"","");my_data[realcounter][1] = location;
			my_data[realcounter][2] = testing[3].replace("\"","");my_data[realcounter][3] = testing[4];
			realcounter+=1;}
		
		ambulance_table_updater = new DefaultTableModel(my_data, columnNames);
		table = new JTable(ambulance_table_updater){private static final long serialVersionUID = 1L; public boolean isCellEditable(int row, int column){return false;}};
		
		Start = new JButton("Start");Start.addActionListener(this);
		Stop = new JButton("Stop");Stop.addActionListener(this);
		table.setPreferredScrollableViewportSize(new Dimension(450,75));
		table.setFillsViewportHeight(true); 
		Introduction = new JLabel("Ambulance Simulation");Introduction.setFont(new Font("SansSerif", Font.PLAIN,26)); Introduction.setBounds(115,1,2000,65);
		Duration = new JLabel("Duration (seconds):");Duration.setFont(new Font("SansSerif", Font.PLAIN,18)); Duration.setBounds(20,300,185,60);
		
		Ambulance_Simulation_Panel = new JPanel();Ambulance_Simulation_Panel.setLayout(null);
		Frame = new JFrame("Ambulance Simulation");Frame.setLayout(null);
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(20, 60, 450, 230); scrollPane.setBorder(border);
		Stop.setBounds(285,380,185,60); Stop.setBackground(new Color(79,129,189)); Stop.setForeground(Color.white); Stop.setBorder(border);
		Start.setBounds(20,380,185,60); Start.setBackground(new Color(79,129,189)); Start.setForeground(Color.white); Start.setBorder(border);
		textfield = new JTextField("60"); textfield.addActionListener(this);textfield.setBounds(180,317,290,25);
		Ambulance_Simulation_Panel.add(textfield );
		Ambulance_Simulation_Panel.add(Start);
		Ambulance_Simulation_Panel.add(Stop);
		Ambulance_Simulation_Panel.add(scrollPane);scrollPane.setBackground(Color.WHITE);
		Ambulance_Simulation_Panel.add(Introduction);
		Ambulance_Simulation_Panel.add(Duration);
		Ambulance_Simulation_Panel.setBackground(new Color(75,172,198));
		Frame.setContentPane(Ambulance_Simulation_Panel);
		Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Frame.setSize(500,500);
		Frame.setResizable(false);
		Frame.setVisible(true);
		
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = ((JButton) e.getSource()).getActionCommand();
		
		if (command == "Start" && user_click_start_or_stop == 2){
			FileReader input_stream = null;
			ambulance_data = new ArrayList<String>(); // this has all the information of each row in the file as a string format.
			final_ambulance_data = new ArrayList<ArrayList>();
			int the_counter = 1;
			Scanner scan = null;
			try {
				input_stream = new FileReader("ambulances-2.csv");
				scan = new Scanner(input_stream);
				while (scan.hasNextLine()) {
				    String[] row = scan.nextLine().trim().split(",",5);
				    if(the_counter != 1){
				    String String1 = row[0].trim();
					String String2 = row[1].trim();
					String String3 = row[2].trim();
					String String4 = row[3].trim();
					String String5 = row[4].trim();
					if (String1.isEmpty() || String1.equals("None")){String1 = "-";} if (String2.isEmpty() || String2.equals("None")){String2 = "-";}
					if (String3.isEmpty() || String3.equals("None")){String3 = "-";} if (String4.isEmpty() || String4.equals("None")){String4 = "-";}
					if (String5.isEmpty() || String5.equals("None")){String5 = "-";}
					String final_string = String1 + ","+String2+","+String3+","+String4+","+String5;
				    ambulance_data.add(final_string);
				    ArrayList<String>  myList = new ArrayList<String>(Arrays.asList(final_string.split(",")));
				    final_ambulance_data.add(myList);
				    }
					the_counter+=1;}} 
			
			catch (FileNotFoundException edsds) {
					edsds.printStackTrace();}
			finally{scan.close();}
			
			FileReader input_stream1 = null;
			patients_data = new ArrayList<String>(); // this has all the information of each row in the file as a string format.
			int the_counter1 = 1;
			Scanner scan1 = null;
			final_patient_data = new ArrayList<ArrayList>();
			try {
				input_stream1 = new FileReader("patients-2.csv");
				scan1 = new Scanner(input_stream1);
				while (scan1.hasNextLine()) {
				    String[] row = scan1.nextLine().trim().split(",",5);
				    if(the_counter1 != 1){
				    String String1 = row[0].trim();
					String String2 = row[1].trim();
					String String3 = row[2].trim();
					String String4 = row[3].trim();
					String String5 = row[4].trim();
					if (String1.isEmpty() || String1.equals("None")){String1 = "-";} if (String2.isEmpty() || String2.equals("None")){String2 = "-";}
					if (String3.isEmpty() || String3.equals("None")){String3 = "-";} if (String4.isEmpty() || String4.equals("None")){String4 = "-";}
					if (String5.isEmpty() || String5.equals("None")){String5 = "-";}
					String final_string = String1 + ","+String2+","+String3+","+String4+","+String5;
					 patients_data.add(final_string);
					 ArrayList<String>  myList = new ArrayList<String>(Arrays.asList(final_string.split(",")));
					 final_patient_data.add(myList); 
				    }
					the_counter1+=1;}} 
			
			catch (FileNotFoundException ex) {
					ex.printStackTrace();}
			finally{scan1.close();}
			for_stoping_threads = new ArrayList<SwingWorkerClass>();
			user_click_start_or_stop = 1;
			try{
				super_my_duration =Integer.parseInt(textfield.getText());}
				catch(java.lang.NumberFormatException sdfs){
				JFrame warning = new JFrame();
				JLabel warning_ = new JLabel("<<Warning>> please enter a number AND RESTART",SwingConstants.CENTER);
			    warning.add(warning_);
				warning.setSize(500, 100);
				warning.setBounds(50, 100, 500, 100);
				warning.setVisible(true);
				warning.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);}
			super_my_duration = super_my_duration *1000;
		//swing worker starts here:
			Timer timer = new Timer();
			timer.schedule(new TimerTask() { 
			   @Override  
			   public void run() {
				   user_click_start_or_stop = 2;
					for(SwingWorkerClass i: for_stoping_threads){
						i.cancel(true);}
					try {
						FileWriter fileWriter = new FileWriter("ambulances-2.csv");
						
					
						fileWriter.append("id");fileWriter.append(",");fileWriter.append("x.location");fileWriter.append(",");
						fileWriter.append("y.location");fileWriter.append(",");fileWriter.append("status");fileWriter.append(",");
						fileWriter.append("patient");
						for(ArrayList i: final_ambulance_data){
							String value1 = ((String) i.get(0));
							String value2 =((String) i.get(1));
							String value3 = ((String) i.get(2));
							String value4 = ((String) i.get(3));
							String value5 = ""+ i.get(4);
							fileWriter.append("\n");
							fileWriter.append(value1);fileWriter.append(",");
							fileWriter.append(value2);fileWriter.append(",");
							fileWriter.append(value3);fileWriter.append(",");
							fileWriter.append(value4);fileWriter.append(",");
							fileWriter.append(value5);}
						fileWriter.flush();
						fileWriter.close();
							
					
					} catch (IOException fds) {
				
					}
					
					try{
						FileWriter fileWriter2 = new FileWriter("patients-2.csv");
					fileWriter2.append("id");fileWriter2.append(",");fileWriter2.append("x.location");fileWriter2.append(",");
					fileWriter2.append("y.location");fileWriter2.append(",");fileWriter2.append("status");fileWriter2.append(",");
					fileWriter2.append("ambulance");
					for(ArrayList i: final_patient_data){
						fileWriter2.append("\n");
						fileWriter2.append(((String) i.get(0)));fileWriter2.append(",");
						fileWriter2.append(((String) i.get(1)));fileWriter2.append(",");
						fileWriter2.append(((String) i.get(2)));fileWriter2.append(",");
						fileWriter2.append(((String) i.get(3)));fileWriter2.append(",");
						fileWriter2.append(((String) i.get(4)));}
					fileWriter2.flush();
					fileWriter2.close();}catch(IOException fdsfsd){}
			}}, super_my_duration);
			for(ArrayList i : final_ambulance_data){
				SwingWorkerClass my_swing = new SwingWorkerClass(i);
				for_stoping_threads.add(my_swing);
				my_swing.execute();
			}
			
		}
		
		else if (command == "Start" && user_click_start_or_stop == 0){
			user_click_start_or_stop = 1;
			Introduction_2 = new JLabel("Ambulance Simulation");Introduction_2.setFont(new Font("SansSerif", Font.PLAIN,26)); Introduction_2.setBounds(110,0,800,40);
			
			//need to open the patients file://////////////////////////////
			FileReader input_stream = null;
			patients_data = new ArrayList<String>(); // this has all the information of each row in the file as a string format.
			int the_counter = 1;
			Scanner scan = null;
			try {
				input_stream = new FileReader("patients.csv");
				scan = new Scanner(input_stream);
				while (scan.hasNextLine()) {
				    String[] row = scan.nextLine().trim().split(",",5);
				    if(the_counter != 1){
				    String String1 = row[0].trim();
					String String2 = row[1].trim();
					String String3 = row[2].trim();
					String String4 = row[3].trim();
					String String5 = row[4].trim();
					if (String1.isEmpty() || String1.equals("None")){String1 = "-";} if (String2.isEmpty() || String2.equals("None")){String2 = "-";}
					if (String3.isEmpty() || String3.equals("None")){String3 = "-";} if (String4.isEmpty() || String4.equals("None")){String4 = "-";}
					if (String5.isEmpty() || String5.equals("None")){String5 = "-";}
					String final_string = String1 + ","+String2+","+String3+","+String4+","+String5;
					 patients_data.add(final_string);
					 ArrayList<String>  myList = new ArrayList<String>(Arrays.asList(final_string.split(",")));
					 final_patient_data.add(myList); 
				    }
					the_counter+=1;}} 
			
			catch (FileNotFoundException ex) {
					ex.printStackTrace();}
			finally{scan.close();}
			////////////////////////////////////////////////////////////////////
			
			
			the_simulation_itself = new My_SimulationPanel(); //drawing the dots
			the_simulation_itself.setBounds(10,45,370,265);
			
			My_KeyPanel the_key_itself = new My_KeyPanel();
			the_key_itself.setBounds(390,45,90,265);
			
			the_simulation = new JPanel();
			the_simulation.setLayout(null);
			the_simulation.setBackground(new Color(75,172,198));
			the_simulation.setBounds(0, 0, 100, 100);
			
			the_simulation.add(Introduction_2);
			the_simulation.add(the_simulation_itself);
			the_simulation.add(the_key_itself);
			
			Simulation_frame = new JFrame("Simulation Frame");
			Simulation_frame.setLayout(null);
			Simulation_frame.setContentPane(the_simulation);
			Simulation_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Simulation_frame.setSize(500,350);
			Simulation_frame.setResizable(false);
			Simulation_frame.setVisible(true);
			Simulation_frame.setLocation(500, 0);
			user_click_start_or_stop = 1;
			try{
			super_my_duration =Integer.parseInt(textfield.getText());}
			catch(java.lang.NumberFormatException sdfs){
			JFrame warning = new JFrame();
			JLabel warning_ = new JLabel("<<Warning>> please enter a number AND RESTART",SwingConstants.CENTER);
		    warning.add(warning_);
			warning.setSize(500, 100);
			warning.setBounds(50, 100, 500, 100);
			warning.setVisible(true);
			warning.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);}
			super_my_duration = super_my_duration *1000;
		//swing worker starts here:
			Timer timer = new Timer();
			timer.schedule(new TimerTask() { 
			   @Override  
			   public void run() {
				   user_click_start_or_stop = 2;
					for(SwingWorkerClass i: for_stoping_threads){
						i.cancel(true);}
					try {
						FileWriter fileWriter = new FileWriter("ambulances-2.csv");
						
					
						fileWriter.append("id");fileWriter.append(",");fileWriter.append("x.location");fileWriter.append(",");
						fileWriter.append("y.location");fileWriter.append(",");fileWriter.append("status");fileWriter.append(",");
						fileWriter.append("patient");
						for(ArrayList i: final_ambulance_data){
							String value1 = ((String) i.get(0));
							String value2 =((String) i.get(1));
							String value3 = ((String) i.get(2));
							String value4 = ((String) i.get(3));
							String value5 = ""+ i.get(4);
							fileWriter.append("\n");
							fileWriter.append(value1);fileWriter.append(",");
							fileWriter.append(value2);fileWriter.append(",");
							fileWriter.append(value3);fileWriter.append(",");
							fileWriter.append(value4);fileWriter.append(",");
							fileWriter.append(value5);}
						fileWriter.flush();
						fileWriter.close();
							
					
					} catch (IOException fds) {
				
					}
					
					try{
						FileWriter fileWriter2 = new FileWriter("patients-2.csv");
					fileWriter2.append("id");fileWriter2.append(",");fileWriter2.append("x.location");fileWriter2.append(",");
					fileWriter2.append("y.location");fileWriter2.append(",");fileWriter2.append("status");fileWriter2.append(",");
					fileWriter2.append("ambulance");
					for(ArrayList i: final_patient_data){
						fileWriter2.append("\n");
						fileWriter2.append(((String) i.get(0)));fileWriter2.append(",");
						fileWriter2.append(((String) i.get(1)));fileWriter2.append(",");
						fileWriter2.append(((String) i.get(2)));fileWriter2.append(",");
						fileWriter2.append(((String) i.get(3)));fileWriter2.append(",");
						fileWriter2.append(((String) i.get(4)));}
					fileWriter2.flush();
					fileWriter2.close();}catch(IOException fdsfsd){}
			}}, super_my_duration);
			
			for(ArrayList i : final_ambulance_data){
				SwingWorkerClass my_swing = new SwingWorkerClass(i);
				for_stoping_threads.add(my_swing);
				my_swing.execute();}
			
			//end ********************* 
			

		}
		else if(command == "Stop" && user_click_start_or_stop == 1){
			user_click_start_or_stop = 2;
			for(SwingWorkerClass i: for_stoping_threads){
				i.cancel(true);
			}
			try {
				FileWriter fileWriter = new FileWriter("ambulances-2.csv");
				
			
				fileWriter.append("id");fileWriter.append(",");fileWriter.append("x.location");fileWriter.append(",");
				fileWriter.append("y.location");fileWriter.append(",");fileWriter.append("status");fileWriter.append(",");
				fileWriter.append("patient");
				for(ArrayList i: final_ambulance_data){
					String value1 = ((String) i.get(0));
					String value2 =((String) i.get(1));
					String value3 = ((String) i.get(2));
					String value4 = ((String) i.get(3));
					String value5 = ""+ i.get(4);
					fileWriter.append("\n");
					fileWriter.append(value1);fileWriter.append(",");
					fileWriter.append(value2);fileWriter.append(",");
					fileWriter.append(value3);fileWriter.append(",");
					fileWriter.append(value4);fileWriter.append(",");
					fileWriter.append(value5);}
				fileWriter.flush();
				fileWriter.close();
					
			
			} catch (IOException fds) {
		
			}
			
			try{
				FileWriter fileWriter2 = new FileWriter("patients-2.csv");
			fileWriter2.append("id");fileWriter2.append(",");fileWriter2.append("x.location");fileWriter2.append(",");
			fileWriter2.append("y.location");fileWriter2.append(",");fileWriter2.append("status");fileWriter2.append(",");
			fileWriter2.append("ambulance");
			for(ArrayList i: final_patient_data){
				fileWriter2.append("\n");
				fileWriter2.append(((String) i.get(0)));fileWriter2.append(",");
				fileWriter2.append(((String) i.get(1)));fileWriter2.append(",");
				fileWriter2.append(((String) i.get(2)));fileWriter2.append(",");
				fileWriter2.append(((String) i.get(3)));fileWriter2.append(",");
				fileWriter2.append(((String) i.get(4)));}
			fileWriter2.flush();
			fileWriter2.close();}catch(IOException fdsfsd){}

		}
		
		
	}
	class SwingWorkerClass extends SwingWorker<Void,Void>{
		private ArrayList the_variable;
		private int the_amb_total_station =  (int) Math.ceil(final_ambulance_data.size()/3.0);
		
		
		public SwingWorkerClass(ArrayList i) {
			the_variable = i; //current ambulance
			
		}

		@Override
		protected Void doInBackground() throws Exception {
			int my_x = Integer.parseInt((String) the_variable.get(1));
			int my_y = Integer.parseInt((String) the_variable.get(2));
			String the_status = ((String) the_variable.get(3)).replace("\"","");
			ArrayList first_patient_that_is_pending_CURRENT_ASSIGNED = null; //current patient //FIX HERE;
		    String the_assigned_patient_num = ((String) the_variable.get(4)).replace("\"",""); //the_patient_number
		    
		    if(the_assigned_patient_num.equals("-")){//so doesn't exist;
			for(ArrayList iv : final_patient_data){
				String Status = ((String) iv.get(3)).replace("\"","");
				if(Status.equals("Pending")){first_patient_that_is_pending_CURRENT_ASSIGNED = iv; break;}
			}}
		    else{
		    	for (ArrayList zeb : final_patient_data){
		    		String the_assigned_patient_id = ((String) zeb.get(0)).replace("\"","");
		    		if(the_assigned_patient_num.equals(the_assigned_patient_id)){first_patient_that_is_pending_CURRENT_ASSIGNED  = zeb;break;}
		    	}
		    }
			
			
			while(!(this.isCancelled())){
				if(the_status.equals("At Station")){//#####################################################
			    String checking_if_ambulance_assigned = ((String) the_variable.get(4)).replace("\"","");
			    //System.out.println(the_variable + " "+ first_patient_that_is_pending_CURRENT_ASSIGNED + " " + "I STATION");
			    if(checking_if_ambulance_assigned.equals("-")){
				for(ArrayList z: final_patient_data){
					String Status = ((String) z.get(3)).replace("\"","");
					if(Status.equals("Pending")){
						int patient_x =  Integer.parseInt((String) first_patient_that_is_pending_CURRENT_ASSIGNED.get(1));
						int patient_y =  Integer.parseInt((String) first_patient_that_is_pending_CURRENT_ASSIGNED.get(2));
						double the_current_patient_ambulance = Math.hypot(my_x-patient_x,my_y - patient_y);	
						int new_patient_x =Integer.parseInt((String) z.get(1));
						int new_patient_y =Integer.parseInt((String) z.get(2));
						double the_compare_patient_ambulance = Math.hypot(my_x-new_patient_x,my_y - new_patient_y);
						if(the_current_patient_ambulance > the_compare_patient_ambulance){first_patient_that_is_pending_CURRENT_ASSIGNED = z;}
				}}
				int the_patient_itself =Integer.parseInt((String) first_patient_that_is_pending_CURRENT_ASSIGNED.get(0));
				first_patient_that_is_pending_CURRENT_ASSIGNED.set(3,"Assigned");
				first_patient_that_is_pending_CURRENT_ASSIGNED.set(4,  the_variable.get(0));
				the_variable.set(3, "Responding");
				the_variable.set(4, the_patient_itself);
				the_status = "Responding";
				Process();}
			    else{the_variable.set(3, "Responding");
			    the_status = "Responding";}
			    Process();
				}//ends ###############################################################################

				if(the_status.equals("Responding")){//#####################################################
			    //System.out.println(the_variable + " "+ first_patient_that_is_pending_CURRENT_ASSIGNED);
			    my_x = Integer.parseInt((String) the_variable.get(1));
				my_y = Integer.parseInt((String) the_variable.get(2));
				int patient_x =  Integer.parseInt((String) first_patient_that_is_pending_CURRENT_ASSIGNED.get(1));
				int patient_y =  Integer.parseInt((String) first_patient_that_is_pending_CURRENT_ASSIGNED.get(2));
				while(true){
					if(my_x > patient_x){my_x -= 4;String newx = "" + my_x; the_variable.set(1, newx);Thread.sleep(100);Process();}
					if(my_x < patient_x){my_x += 4;String newx = "" + my_x; the_variable.set(1, newx);Thread.sleep(100);Process();}
					if(my_y > patient_y){my_y -= 4;String newy = "" + my_y; the_variable.set(2, newy);Thread.sleep(100);Process();}
					if(my_y < patient_y){my_y += 4;String newy = "" + my_y; the_variable.set(2, newy);Thread.sleep(100);Process();}
					if((my_x-4 == patient_x ||my_x-3 == patient_x ||my_x+4 == patient_x ||my_x+3 == patient_x || my_x == patient_x || my_x+1 == patient_x || my_x+2 == patient_x || my_x-2 == patient_x || my_x-1 == patient_x)&& (my_y-4 == patient_y ||my_y-3 == patient_y ||my_y+4 == patient_y ||my_y+3 == patient_y ||my_y == patient_y || my_y+1 == patient_y || my_y+2 == patient_y || my_y-2 == patient_y || my_y-1 == patient_y)){
						first_patient_that_is_pending_CURRENT_ASSIGNED.set(3,"None");
						the_variable.set(3, "At Scene");
					    //System.out.println(the_variable + " "+ first_patient_that_is_pending_CURRENT_ASSIGNED+ "fianl");
					    the_status = "At Scene";
						Thread.sleep(1000);
						break;}
					
				}
			}//ends ###############################################################################	

			if(the_status.equals("At Scene")){//#####################################################
				first_patient_that_is_pending_CURRENT_ASSIGNED.set(3,"None");
				//System.out.println(the_variable + " "+ first_patient_that_is_pending_CURRENT_ASSIGNED+ "At Scene");
				Thread.sleep(3000);
				Process(); //updates
				first_patient_that_is_pending_CURRENT_ASSIGNED.set(3,"Transporting");
				the_variable.set(3, "Transporting");
				the_status = "Transporting";
				Process();
			}//ends ###############################################################################	
			
			if(the_status.equals("Transporting")){//#####################################################
				int the_hospital_x = 50;
				int the_hospital_y =50;
				my_x = Integer.parseInt((String) the_variable.get(1));
				my_y = Integer.parseInt((String) the_variable.get(2));
				while(true){//need to also change x and y for patients part;
					if(my_x > the_hospital_x){my_x -= 3;String newx = "" + my_x; the_variable.set(1, newx);Thread.sleep(100);Process();}
					if(my_x < the_hospital_x){my_x += 3;String newx = "" + my_x; the_variable.set(1, newx);Thread.sleep(100);Process();}
					if(my_y > the_hospital_y){my_y -= 3;String newy = "" + my_y; the_variable.set(2, newy);Thread.sleep(100);Process();}
					if(my_y < the_hospital_y){my_y += 3;String newy = "" + my_y; the_variable.set(2, newy);Thread.sleep(100);Process();}
					if((my_x-3 == the_hospital_x ||my_x-2 == the_hospital_x ||my_x-1 == the_hospital_x ||my_x+3 == the_hospital_x ||my_x+2 == the_hospital_x ||my_x+1 == the_hospital_x || my_x == the_hospital_x) && (my_y-3 == the_hospital_y||my_y-2 == the_hospital_y||my_y-1 == the_hospital_y||my_y+3 == the_hospital_y||my_y+2 == the_hospital_y||my_y+1 == the_hospital_y||my_y == the_hospital_y)){
						the_variable.set(3, "At destination");
					    first_patient_that_is_pending_CURRENT_ASSIGNED.set(3,"None");
					    //System.out.println(the_variable + " "+ first_patient_that_is_pending_CURRENT_ASSIGNED+ "Hospital");
					    the_status = "At destination";
						Process();
						break;
					}
				}
				
			}//ends ###############################################################################	
			
			if(the_status.equals("At destination") || the_status.equals("At Destination")){//#####################################################
				Thread.sleep(2000);
				first_patient_that_is_pending_CURRENT_ASSIGNED.set(3,"Completed");
				//System.out.println(the_variable + " "+ first_patient_that_is_pending_CURRENT_ASSIGNED+ "At destination");
				
				//after
				the_status = "Returning";
				the_variable.set(3, "Returning");
			}
			if(the_status.equals("Returning")){
				the_variable.set(4, "-");
				int the_greenfield_x = 10;
				int the_greenfield_y = 0;
				int the_bluelane_x = 30;
				int the_bluelane_y = 80;
				int the_redvill_x = 90;
				int the_redvill_y = 20;
				int station_current_x = 10;
				int station_current_y = 0;
				while(true){
					herez:
					my_x = Integer.parseInt((String) the_variable.get(1));
					my_y = Integer.parseInt((String) the_variable.get(2));
					String the_station_name = "greenFields";
					double ambulance_shortest_distance_check = Math.hypot(my_x-the_greenfield_x,my_y-the_greenfield_y);
					
					if(ambulance_shortest_distance_check > Math.hypot(my_x-the_bluelane_x,my_y-the_bluelane_y)){
						ambulance_shortest_distance_check = Math.hypot(my_x-the_bluelane_x,my_y-the_bluelane_y); 
						the_station_name = "blueLane"; station_current_x=30;station_current_y = 80;}
					
					else if(ambulance_shortest_distance_check > Math.hypot(my_x-the_redvill_x,my_y-the_redvill_y)){
						ambulance_shortest_distance_check = Math.hypot(my_x-the_redvill_x,my_y-the_redvill_y); 
						the_station_name = "redVill"; station_current_x=90;station_current_y = 20;
					}
					
					if(greenCount >= the_amb_total_station){the_station_name = "blueLane";station_current_x=30;station_current_y = 80;}
					if(blueCount >= the_amb_total_station){the_station_name = "redVill";station_current_x=90;station_current_y = 20;}
					if(redCount >= the_amb_total_station){the_station_name = "greenFields";station_current_x=10;station_current_y = 0;}

					if(my_x > station_current_x){my_x -= 3;String newx = "" + my_x; the_variable.set(1, newx);Thread.sleep(100);Process();}
					if(my_x < station_current_x){my_x += 3;String newx = "" + my_x; the_variable.set(1, newx);Thread.sleep(100);Process();}
					if(my_y > station_current_y){my_y -= 3;String newy = "" + my_y; the_variable.set(2, newy);Thread.sleep(100);Process();}
					if(my_y < station_current_y){my_y += 3;String newy = "" + my_y; the_variable.set(2, newy);Thread.sleep(100);Process();}

					if((my_x-3 == station_current_x||my_x-2 == station_current_x||my_x-1 == station_current_x||my_x+3 == station_current_x||my_x+2 == station_current_x||my_x+1 == station_current_x||my_x == station_current_x) && (my_y-3 == station_current_y||my_y-2 == station_current_y||my_y-1 == station_current_y||my_y+3 == station_current_y||my_y+2 == station_current_y||my_y+1 == station_current_y||my_y == station_current_y)){
						//System.out.println(the_station_name);
						counterLock.lock();
						try{ //station need to -1 if its leaving
						
						
						if(station_current_x == 10 && station_current_y == 0){greenCount+=1;}
						else if(station_current_x == 30 && station_current_y == 80){blueCount+=1;}
						else if(station_current_x == 90 && station_current_y == 20){redCount+=1;}
						//System.out.println("thegreen " +greenCount + " " +  "bluelane "+blueCount + " " + "redvill " + redCount);
						//System.out.println(the_amb_total_station);
						the_variable.set(3, "At Station");
						the_variable.set(4, "-");
					    first_patient_that_is_pending_CURRENT_ASSIGNED.set(3,"Completed");
					   // System.out.println(the_variable + " "+ first_patient_that_is_pending_CURRENT_ASSIGNED+ "Returned " + the_station_name);
					    the_status = "At Station";}
						finally{
					    counterLock.unlock();}
						Process();
						first_patient_that_is_pending_CURRENT_ASSIGNED = null; 
					    the_assigned_patient_num = ((String) the_variable.get(4)).replace("\"",""); 
					    Thread.sleep(1000);
					    if(the_assigned_patient_num.equals("-")){//so doesn't exist;
						for(ArrayList iv : final_patient_data){
							String Status = ((String) iv.get(3)).replace("\"","");
							if(Status.equals("Pending")){first_patient_that_is_pending_CURRENT_ASSIGNED = iv;
							counterLock2.lock();
					    	try{
					    		if(station_current_x == 10 && station_current_y == 0){greenCount-=1;}
								else if(station_current_x == 30 && station_current_y == 80){blueCount-=1;}
								else if(station_current_x == 90 && station_current_y == 20){redCount-=1;}
					    	}finally{counterLock2.unlock();}
							break;}
						}}
					    else{
					    	for (ArrayList zeb : final_patient_data){
					    		String the_assigned_patient_id = ((String) zeb.get(0)).replace("\"","");
					    		if(the_assigned_patient_num.equals(the_assigned_patient_id)){first_patient_that_is_pending_CURRENT_ASSIGNED  = zeb;
					    		counterLock2.lock();
						    	try{
						    		if(station_current_x == 10 && station_current_y == 0){greenCount-=1;}
									else if(station_current_x == 30 && station_current_y == 80){blueCount-=1;}
									else if(station_current_x == 90 && station_current_y == 20){redCount-=1;}
						    	}finally{counterLock2.unlock();}
					    		break;}
					    	}
					    }
						break;
					}
					
				}
				
			}//ends #####################################################

			
			
			
		}

			return null;
	}
		private void Process() {
			
			int realcounter = 0;
			Object[][] my_dataz = new Object[ambulance_data.size()][4];
			for(ArrayList i: final_ambulance_data){
				String location = "("+ i.get(1)+ ","+ i.get(2)+")";
				String the_name =  ((String) i.get(0)).replace("\"","");
				String pick_up =  ((String) i.get(3)).replace("\"","");
				my_dataz[realcounter][0] = the_name; my_dataz[realcounter][1] = location;
				my_dataz[realcounter][2] = pick_up; my_dataz[realcounter][3] = i.get(4);
				realcounter+=1;}
			ambulance_table_updater = new DefaultTableModel(my_dataz,columnNames);
			table = new JTable(ambulance_table_updater); 
			scrollPane = new JScrollPane(table);scrollPane.setBackground(Color.WHITE);
			scrollPane.setBounds(20, 60, 450, 230); scrollPane.setBorder(border);//230
			Ambulance_Simulation_Panel.add(scrollPane);
			the_simulation_itself.repaint();
			
		}

		
	}
	
	

	public static void main(String[] args){
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				AmbulanceSimulation newContent = new AmbulanceSimulation();
			}
		});
	}

	class My_SimulationPanel extends JPanel {


		public void paintComponent(Graphics g){
			
			super.paintComponent(g);
			Graphics2D g2g = (Graphics2D) g;
			
			g2g.setColor(new Color(96,74,123));//hospital
			g2g.fillOval(370*50/100, 132, 13, 13);
			
			g2g.setColor(new Color(149,55,53));//Station Greenfields
			g2g.fillOval(370*10/100, 265*0/100, 13, 13);
			
			g2g.setColor(new Color(149,55,53));//Station Bluelane
			g2g.fillOval(370*30/100, 265*80/100, 13, 13);
			
			g2g.setColor(new Color(149,55,53));//Station Redvill
			g2g.fillOval(370*90/100, 265*20/100, 13, 13);
			for(ArrayList i : final_ambulance_data){
				int my_x = Integer.parseInt((String) i.get(1));
				int my_y =  Integer.parseInt((String) i.get(2));
				g2g.setColor(new Color(119,147,60));// each ambulance 
				g2g.fillOval(370*my_x/100, 265*my_y/100, 10, 10);
			}
			
			for(ArrayList i : final_patient_data){
				int my_x = Integer.parseInt((String) i.get(1));
				int my_y =  Integer.parseInt((String) i.get(2));
				String my_status =  ((String) i.get(3)).replace("\"",""); 
				if(my_status.equals("Assigned") || my_status.equals("Pending")){
				g2g.setColor(new Color(55,96,146));// each patient 
				g2g.fillOval(370*my_x/100, 265*my_y/100, 13, 13);}
			}
		
			
		}
	
}

	

}


