import java.io.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;


class ResourceManager{

	static final String root = "C:/Users/IT-Asst/Documents/JVP/project/Attendance/";
  	static final String[] directoryList = {root+"Team Alondra/",root+"Team Shaira/",root+"Team Ronalyn/",root+"Util/"};
  	static final List<Employee> employee = new ArrayList<>();
  	static List<Attendance> attendanceList = new ArrayList<>();
 	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yy hh:mm a");
	static String outputFileName = "Output";

	//Method loading file names in directory

 	public static void loadFileNames(){
		
		//System.out.println("Loading File Name in Directory!");
	
		for(String dir:directoryList){

			File directory = new File(dir);
			File[] files = directory.listFiles();

			for(File f:files){

			Employee emp = new Employee(f.getName().replaceFirst("[.][^.]+$",""));
				   emp.setFilePath(""+dir+f.getName()+"");
				   employee.add(emp);
			//System.out.println(emp.getName()+""+dir+f.getName());

			}
	
	
		}

		//System.out.println("Loading File Name in Directory Done!");


  	}//End of load file names

	public void loadListofEmployeeNameID(){
		
	}


	public static void loadFileEmployee(){
		
		//System.out.println("Loading Employee Name in Directory!");
		

		for(Employee emp: employee){
			Attendance attn = new Attendance(emp);
			attendanceList.add(attn);
		}

	}


     	//Method loading Attendance 
  	public static List<LocalDateTime> loadAttendanceOf(String path){

		List<LocalDateTime> list = new ArrayList<>();
		//System.out.println("Loading attendance!");

		File file = new File(path);
		if(file.isFile()){

			 try (FileInputStream inputStream = new FileInputStream(file)) {

                        Scanner sc = new Scanner(inputStream);
				while(sc.hasNext()){
					
					try{
						LocalDateTime t = LocalDateTime.parse(sc.nextLine(),formatter);
								list.add(t);

					}catch(Exception e){
						continue;
					}

				}
				sc.close();

                    } catch (IOException e) {
                        // Handle the exception
                        e.printStackTrace();
                    }

		}
		else{
            System.out.println("Directory does not exist or is not a directory");
        	}

		return list;
  	}



	//Method load Excel

	public static List<Attendance> readLocalDateTimesFromExcelFile(String filePath)throws IOException,InvalidFormatException{
	
	
	


    		List<LocalDateTime> localDateTimes = new ArrayList<>();
    		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
		
    		
		//try{
			File file = new File(filePath);
			Workbook workbook = WorkbookFactory.create(file);
    			Sheet sheet = workbook.getSheetAt(0);

			Cell currentCell = sheet.getRow(2).getCell(0);
			Cell currentID = sheet.getRow(2).getCell(1);
			Cell currentName = sheet.getRow(2).getCell(2);
			Cell currentSurname = sheet.getRow(2).getCell(3);
			Cell currentDept = sheet.getRow(2).getCell(4);

			List<LocalDateTime> attendanceListRaw = new ArrayList<>();
			String cname = currentName.getStringCellValue();
			if(currentSurname != null) cname = cname+" "+currentSurname.getStringCellValue();

			Employee e = new Employee(cname);
				   e.setID(currentID.getStringCellValue());
				   e.setDepartment(currentDept.getStringCellValue());
	
			Attendance currentAtt = new Attendance(e);
				attendanceList.add(currentAtt);

			//Map<LocalDateTime
			//Row headerRow = sheet.getRow(0);
			

    			for (Row row : sheet) {

				
				if(row.getRowNum() >=2 ){
					
        				Cell cell = row.getCell(0);
					Cell cellID = row.getCell(1);
					Cell cellName = row.getCell(2);
					Cell cellSurname = row.getCell(3);
					Cell cellDept = row.getCell(4);

					//System.out.println("Time: "+cell+" ID:"+cellID+" Name: "+cellName+" Dept: "+cellDept+"Row Number: "+row.getRowNum());
					double cID=0; double nID=0; //Hold Value of current and next ID;

					switch(currentID.getCellType()){
					 case NUMERIC: cID = currentID.getNumericCellValue(); nID = cellID.getNumericCellValue(); break;
					 case STRING: cID = Double.valueOf(currentID.getStringCellValue()); nID = Double.valueOf(cellID.getStringCellValue()); break;
					}

				
					if(cID == nID ){
						
						//System.out.println("Check me here!");
						/**
						if (DateUtil.isCellDateFormatted(cell)) {
							
							Date date = DateUtil.getJavaDate(Double.valueOf(cell.getStringCellValue()));
							Instant instant = date.toInstant();
                					LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

                					attendanceListRaw.add(localDateTime);
            				} else {
                					String stringValue = cell.getStringCellValue();
                					LocalDateTime localDateTime = LocalDateTime.parse(stringValue, format);
                					attendanceListRaw.add(localDateTime);
            				} **/
						
							String stringValue = cell.getStringCellValue();
                					LocalDateTime localDateTime = LocalDateTime.parse(stringValue, format);
                					attendanceListRaw.add(localDateTime);

						
					
					}
					else if(cID != nID){

						//System.out.println("Else Im called!"+currentID);
					
					 		currentAtt.setAttendanceList(attendanceListRaw);
							currentAtt.loadAttendanceList();
								attendanceListRaw = new ArrayList<>();

							String name = cellName.getStringCellValue();
							if(cellSurname != null) name = name+" "+cellSurname.getStringCellValue();

							Employee emp = new Employee(name);
					  		 	   emp.setID(""+cellID.getStringCellValue());
					  		         emp.setDepartment(cellDept.getStringCellValue());
							Attendance att = new Attendance(emp);
								currentAtt = att;
								attendanceList.add(currentAtt);
					 			currentID = cellID;
					   				   
							
					}
	
									
		
					if(row.getRowNum() == sheet.getLastRowNum()){
							currentAtt.setAttendanceList(attendanceListRaw);
							currentAtt.loadAttendanceList();
					}





				}


			}
			workbook.close();

		//}catch(IOException e){
		//	System.out.println("Cannot Load File or File Doesn't Exist or wrong file name!");
		//	e.printStackTrace();
			
			
		//}catch(InvalidFormatException e){
		//	System.out.println("Cannot Load File or File Doesn't Exist or wrong file name!");
		//	e.printStackTrace();
		//}
		//throw new InvalidFormatException("File Reading Failed!");
		//throw new InvalidFormatException("File is not supported or corrupted!");
        	return attendanceList;
		
	}

	
	public static void fileWriteOut(String name)throws IOException{
		FileOutputStream fileOut = new FileOutputStream(name);
		PrintStream printOut = new PrintStream(fileOut);
		
	}

	
}