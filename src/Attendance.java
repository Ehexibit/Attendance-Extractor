import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Comparator;

import java.io.FileOutputStream;
import java.io.FileDescriptor;
import java.io.PrintStream;
import java.io.IOException;


import java.util.ArrayList;
import java.time.Duration;
import java.time.format.DateTimeFormatter;


class Attendance{
	
		 	
			Employee employee;
			List<LocalDateTime> attendanceList; //List of attendance in LocalDateTime format RAW("Meaning Un-edited, multiple Time in is still contain");
			List<LocalDateTime> list_TimeIN;  	//List of time In in LocalDateFormat
			List<LocalDateTime> list_TimeOUT; 	//List of time OUT in LocalDateFormat
			
			public Attendance(){
			}
			public Attendance(Employee employee){

				this.employee = employee;
				list_TimeIN = new ArrayList<>();
				list_TimeOUT = new ArrayList<>();
				
				/* attendanceList = ResourceManager.loadAttendanceOf(employee.getFilePath()); */

			}
			public Employee getEmployee(){
				return employee;
			}
			public void setAttendanceList(List<LocalDateTime> list){
				this.attendanceList = list;
			}	
			public void loadAttendanceList(){

				DateTimeFormatter format = ResourceManager.formatter;
					
				Comparator<LocalDateTime> c = Comparator.naturalOrder();
				attendanceList.sort(c);

				//for(LocalDateTime t: attendanceList) System.out.println(t.format(format));
				

				

				/* If AttendanceList not! Empty  */

				if(!attendanceList.isEmpty()){


					for(int i=0; i<attendanceList.size()-1; i++){

							Duration duration  = Duration.between(attendanceList.get(i),attendanceList.get(i+1));
 						
							if(duration.toMinutes()<30){
	
								//Set First In
								LocalDateTime tmp = attendanceList.get(i);
								attendanceList.set(i,attendanceList.get(i+1));
								attendanceList.set((i+1),tmp);
								continue;

							}
							else if(duration.toHours() > 6 && duration.toHours() < 12){

								//System.out.println("Time In:"+attendanceList.get(i).format(format)+" Time Out:"+attendanceList.get(i+1).format(format));
												
									list_TimeIN.add(attendanceList.get(i));

									//Look for Last out	
									for(int j=attendanceList.size()-1; j>0; j--){

										Duration d = Duration.between(attendanceList.get(i),attendanceList.get(j));

										if(d.toHours()>6 &&d.toHours()<12){ 

											list_TimeOUT.add(attendanceList.get(j)); 
											i=j;
											break;
										}
									}

							}
							else list_TimeIN.add(attendanceList.get(i));
				
					}//End of for loop of AttendanceList
	
				}
				//else	System.out.println("employee.getName() has no record found!"); //else If AttendanceList is Empty print this
			}//End of method loadAttendance


	
			public void printAttendance(){
			



				System.out.println("\n"+employee.getName());
	
				for(int i=0; i<list_TimeIN.size(); i++){

					System.out.print(list_TimeIN.get(i).format(ResourceManager.formatter)+"\t");
		
						for(int j=0; j<list_TimeOUT.size(); j++){

							Duration d = Duration.between(list_TimeIN.get(i),list_TimeOUT.get(j));
							
							if(d.toHours()> 6 && d.toHours()<12)

							System.out.print(list_TimeOUT.get(j).format(ResourceManager.formatter));
   						}
   					System.out.println();
				}			
	

				
	
	
				//System.out.println("Attendance Records Count:"+attendanceList.size()+" Complete In: "+list_TimeIN.size()+" Out: "+list_TimeOUT.size());
			

			}//End of method printAttendance;


			public void printToFile()throws IOException{

			FileOutputStream fileOut = new FileOutputStream("out/"+employee.getDepartment()+"/"+employee.getName()+".txt");

				PrintStream printOut = new PrintStream(fileOut);
				//System.setOut(printOut);
				printOut.println(employee.getName());
				//System.out.println(employee.getName());
				printOut.println("ID:"+employee.getID()+" "+employee.getDepartment()+"\n");
				//System.out.println("ID:"+employee.getID()+" "+employee.getDepartment()+"\n");

				for(LocalDateTime att:attendanceList) //This line wiill print the raw data which is the attendance logs that read and captured from the excel file
					printOut.println(att.format(ResourceManager.formatter));
					//System.out.println(att.format(ResourceManager.formatter));

				printOut.println();
				System.setOut(printOut); //Passing the System.out into our PrintStream which will be written on text file instead of printing on concsole
				printAttendance();
				System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out))); //Revert the System.out to console

				fileOut.close();
				printOut.close();

				//System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));

			}

}