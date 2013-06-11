package models;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.List;

public class EmployeeInfo {

		public int nEmpId;
		public String strEmpName;
		public String strEmpType;
		
		public List<ProjectInfo> listProjectInfo=new ArrayList<ProjectInfo>();
		public List<ProjectOccupied> listProjectWorking= new ArrayList<ProjectOccupied>();
	    public List<WeekInfo> listWeekInfo=new ArrayList<WeekInfo>();
		
		public List<Integer> listQuarterOccupied =new ArrayList<Integer>(13);  // 13 is quarter size
		
		public static Comparator<EmployeeInfo> EmployeeNameComparator=new Comparator<EmployeeInfo>(){
			
			@Override
			public int compare(EmployeeInfo o1, EmployeeInfo o2) {
				String empName1=o1.strEmpName.toUpperCase();
				String empName2=o2.strEmpName.toUpperCase();
			return empName1.compareTo(empName2);
			}
		};
		
}
