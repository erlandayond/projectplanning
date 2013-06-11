package models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProjectInfo {

		public int nProjectId;
		public String strProjectName;
		public String strProjectType;
	    public List<WeekInfo> listWeekInfo=new ArrayList<WeekInfo>();
	    public List<EmployeeInfo> listEmployeeInfo=new ArrayList<EmployeeInfo>();
	    
	    public static Comparator<ProjectInfo> ProjectNameComparator=new Comparator<ProjectInfo>(){
			
			@Override
			public int compare(ProjectInfo o1, ProjectInfo o2) {
				String projName1=o1.strProjectName.toUpperCase();
				String projName2=o2.strProjectName.toUpperCase();
			return projName1.compareTo(projName2);
			}
		};
}
