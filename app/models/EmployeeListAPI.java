package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import play.Logger;
import play.db.jpa.JPA;

public class EmployeeListAPI {
	
	public List<EmployeeInfo> MakeAPIObject(){
		
		List<Employee> listEmployee=getAllEmployees();
		List<EmployeeInfo> listEmployeeInfo=new ArrayList<EmployeeInfo>();
		for(Employee emp: listEmployee){
			
			EmployeeInfo tempEmployeeInfo=new EmployeeInfo();
			tempEmployeeInfo.nEmpId=emp.getEmpId();
			tempEmployeeInfo.strEmpName=emp.getEmpName();
			tempEmployeeInfo.strEmpType=emp.getEmpType();
			tempEmployeeInfo.listProjectWorking= getProjectsForEmployee(emp.getEmpId());
			
			listEmployeeInfo.add(tempEmployeeInfo);
		}
		
		return listEmployeeInfo;
	}
	
	public List<Employee> getAllEmployees(){
		
		//Query employees
    	Query query=JPA.em().createQuery("select id, empName, empType from Employee");
    	List<Object> listObjEmployee =query.getResultList();
    	List<Employee> listtempEmployee=new ArrayList<Employee>();
    	
    	Logger.info("number of employees fetched:"+listObjEmployee.size());
    	
    	if(listObjEmployee.size()>0){
    		
    		for(Object objEmployee : listObjEmployee){
    			Object[] objResult=(Object[])objEmployee;
    		    int nEmpId=(int)objResult[0];
    			String strEmpName=(String)objResult[1];
    			String strEmpType=(String)objResult[2];
    			
    			//Logging employee info
    			Logger.info("empId:"+nEmpId);
    			Logger.info("empName:"+strEmpName);
    			Logger.info("empType:"+strEmpType);
    			
    			Employee tempEmp=new Employee();
    			tempEmp.setEmpId(nEmpId);
    			tempEmp.setEmpName(strEmpName);
    			tempEmp.setEmpType(strEmpType);
    			listtempEmployee.add(tempEmp);
    			
    		  
    		}
    	}
		
    	return listtempEmployee;
	}
    
	private List<ProjectOccupied> getProjectsForEmployee(int nEmpId){
	
		Query query=JPA.em().createQuery("select projectId, projectName, week, occupied from Resourceplan where empId=:id ");
		query.setParameter("id",nEmpId);
		
		List<Object> listResult=query.getResultList();
		List<ProjectOccupied> listProjectOccupied=new ArrayList<ProjectOccupied>();
		
		if(listResult.size()>0){
			for(Object tempObj: listResult){
				Object[] objResult=(Object[])tempObj;
				ProjectOccupied tmpProjectOccupied=new ProjectOccupied();
				tmpProjectOccupied.nProjectId=(int)objResult[0];
				tmpProjectOccupied.strProjectName=(String)objResult[1];
				tmpProjectOccupied.nWeekNumber=(int)objResult[2];
				tmpProjectOccupied.nOccupied=(int)objResult[3];
				
				//Logging employee-project info
				Logger.info("emp Id:"+nEmpId);
				Logger.info("Project Name:"+tmpProjectOccupied.strProjectName);
				Logger.info("Week number:"+tmpProjectOccupied.nWeekNumber);
				Logger.info("Occupied:"+tmpProjectOccupied.nOccupied);
				
				listProjectOccupied.add(tmpProjectOccupied);
			}
		}
		
		return listProjectOccupied;
		
	}
}
