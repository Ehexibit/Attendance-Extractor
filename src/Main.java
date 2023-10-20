import java.util.List;
import java.util.Scanner;
import java.util.Comparator;
import java.util.Collections;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileDescriptor;
import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

class Main{


	static List<Attendance> attList;

	public static void main(String args[]){

		//ResourceManager.loadFileNames();
		//ResourceManager.loadFileEmployee();

		System.out.println("Welcome Time Manager!\n\n\n\n");
		System.out.print("Input File Source: ");
		Scanner sc = new Scanner(System.in);
			 


		try{
			attList = ResourceManager.readLocalDateTimesFromExcelFile(sc.nextLine());

			System.out.println("File contain Employee Count:"+attList.size()+" in Total");
			System.out.println("Team Name");
			for(Attendance list: attList) System.out.println("ID: "+list.getEmployee().getID()+" "+list.getEmployee().getName());
				
			Comparator<Attendance> byEmployeeID = (e1,e2) -> Integer.valueOf(e1.getEmployee().getID()) - Integer.valueOf(e2.getEmployee().getID());
			Collections.sort(attList,byEmployeeID); 
		
		}
		catch(IOException e){
		 	e.printStackTrace();
	
		}
		catch(InvalidFormatException e){
			System.out.println("Cannot Read file! Reason: Invalid Format, File not found or doesnt exist please check file name correctly!");
			e.printStackTrace();
		}
		
		System.out.print("\n\nOutput File Name: ");



		

		try{
				 
				ResourceManager.outputFileName = sc.nextLine();


				FileOutputStream fileOut = new FileOutputStream("./out/"+ResourceManager.outputFileName+".txt");
				PrintStream printOut = new PrintStream(fileOut);
				System.setOut(printOut);
				System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));

		}catch(IOException e){

				e.printStackTrace();
		}




		for(Attendance att:attList){
			
	
			att.printAttendance();

			
		}

		for(Attendance att:attList){
		     try{
			att.printToFile();

		     }catch(IOException e){
			  e.printStackTrace();
		     }
		}
		


		System.out.println("Success!");
		System.out.println("Press any key to exit!");
			   
		

	}
}