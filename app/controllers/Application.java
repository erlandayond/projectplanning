package controllers;

import play.*;
import play.db.jpa.JPA;
import play.mvc.*;

import groovy.ui.Console;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import flexjson.JSONSerializer;

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
    
    public static void getJSONEmployeeInfo(){
    	EmployeeListAPI objEmployeeListAPI=new EmployeeListAPI();
        List<EmployeeInfo> listObjEmployeeInfo= objEmployeeListAPI.MakeAPIObject();
       
        
        JSONSerializer modelSerializer = new JSONSerializer().exclude("class").include("listProjectWorking").rootName("employees").exclude("listProjectWorking.class","nEmpId","listProjectWorking.nProjectId", "listProjectWorking.nWeekNumber", "listProjectWorking.nOccupied");
        renderJSON(modelSerializer.serialize(listObjEmployeeInfo));
        
    }
    
    // Add project for employee
    public static void addProject(String ename, String project ){
    	EntityManager em=JPA.newEntityManager();
    	Logger.info("employee name to be added:"+ename);
    	Logger.info("Project to be added"+project);
    	
    	em.getTransaction().begin();
    	
    	//Insert project for employee
    	
    }
    
    public static void getProjects(int nEmpId){
    	
    	// Get projects for Employee with Id : nEmpId
    }
    
    public static void getEmployees(){
    	EmployeeListAPI objEmpListAPI=new EmployeeListAPI();
        List<Employee> listEmployees= objEmpListAPI.getAllEmployees();
        JSONSerializer modelSerializer=new JSONSerializer().exclude("class","entityId","persistent").rootName("employees");
        render("Application/index.html", listEmployees);
    }

}