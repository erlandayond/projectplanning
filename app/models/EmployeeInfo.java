package models;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class EmployeeInfo {

		public int nEmpId;
		public String strEmpName;
		public String strEmpType;
		
		public List<ProjectInfo> listProjectInfo=new ArrayList<ProjectInfo>();;
		public List<ProjectOccupied> listProjectWorking= new ArrayList<ProjectOccupied>();
		
		public List<Integer> listQuarterOccupied =new ArrayList<Integer>(13);  // 13 is quarter size
		
}
