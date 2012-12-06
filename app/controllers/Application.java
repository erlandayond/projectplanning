package controllers;

import play.*;
import play.db.jpa.JPA;
import play.mvc.*;

import groovy.ui.Console;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import models.*;

public class Application extends Controller {

    public static void index() {
    	
    	EntityManager em= JPA.newEntityManager();
    	
    	em.getTransaction().begin();
    	
    	/*//Query employees
    	Query query=JPA.em().createQuery("select empName, empType from Employee");
    	List<Object> listObjEmployee =query.getResultList();
    	List<Employee> listEmployee=new ArrayList<Employee>();
    	
    	Logger.info("number of employees fetched:"+listObjEmployee.size());
    	
    	if(listObjEmployee.size()>0){
    		
    		for(Object objEmployee : listObjEmployee){
    			Object[] objResult=(Object[])objEmployee;
    		
    			String strEmpName=(String)objResult[0];
    			String strEmpType=(String)objResult[1];
    			
    			Employee tempEmp=new Employee();
    			tempEmp.setEmpId(99);
    			tempEmp.setEmpName(strEmpName);
    			tempEmp.setEmpType(strEmpType);
    			listEmployee.add(tempEmp);
    		}
    	}*/
    	
    	EmployeeListAPI objEmployeeListAPI=new EmployeeListAPI();
    	objEmployeeListAPI.MakeAPIObject();
    	render("Application/index.html");
    }
    
    public static void addEmployee(String ename){
    	EntityManager em= JPA.newEntityManager();
    	
    	Logger.info("employee name"+ename);
    	em.getTransaction().begin();
    	
    	// Insert Employee record
    	Employee emp=new Employee();
    	emp.setEmpName(ename);
    	emp.setEmpType("Contractor");
    	
    	em.persist(emp);
    	em.getTransaction().commit();
        
    	String message="Employee record has been inserted";
    	render("Application/index.html");
    }
    

}