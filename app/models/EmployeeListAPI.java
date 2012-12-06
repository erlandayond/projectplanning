package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import play.Logger;
import play.db.jpa.JPA;

public class EmployeeListAPI {
	
	public void MakeAPIObject(){
		
		List<Employee> listEmployee=getAllEmployees();
		
	}
	
	private List<Employee> getAllEmployees(){
		
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

}
