class Employee{
	
	private String ID;	
	private final String name;
	private String filePath;
	private String department;
	public Employee(String name){
		this.name = name;
	}
	public void setID(String ID){
		this.ID = ID;
	}
	public void setDepartment(String department){
		this.department = department;
	}
	public void setFilePath(String filePath){
		this.filePath = filePath;
	}
	public String getFilePath(){
		return filePath;
	}
	public String getName(){
		return name;
	}
	public String getID(){
		return ID;
	}
	public String getDepartment(){
		return department;
	}
	
}