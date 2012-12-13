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
    	
    	EmployeeListAPI objEmployeeListAPI=new EmployeeListAPI();
    	objEmployeeListAPI.MakeAPIObject();
    	
    	render("Application/index.html");
    }
    
    public static void addEmployee(String ename, String eType){
    	EntityManager em= JPA.newEntityManager();
    	
    	if(ename!=null && ! ename.isEmpty())
    	{
    		em.getTransaction().begin();
    	
    		// Insert Employee record
    		Employee emp=new Employee();
    		emp.setEmpName(ename);
    		emp.setEmpType(eType);
    		em.persist(emp);
    		em.getTransaction().commit();
    	
    		Logger.info("employee added to database:"+ename+"Type:"+emp.getEmpType());
    	
    	}
    	
    	redirect("/getEmployees");
    }
    
    public static void getJSONEmployeeInfo(){
    	EmployeeListAPI objEmployeeListAPI=new EmployeeListAPI();
        List<EmployeeInfo> listObjEmployeeInfo= objEmployeeListAPI.MakeAPIObject();
       
        
        JSONSerializer modelSerializer = new JSONSerializer().exclude("class").include("listProjectWorking").rootName("employees").exclude("listProjectWorking.class","nEmpId","listProjectWorking.nProjectId", "listProjectWorking.nWeekNumber", "listProjectWorking.nOccupied");
        renderJSON(modelSerializer.serialize(listObjEmployeeInfo));
        
    }
    
    // Add project for employee
    public static void addProject(String eId, String project ){
    	EntityManager em=JPA.newEntityManager();
    	
    	Logger.info("Project to be added"+project+" for employee:"+eId);
    	
    	em.getTransaction().begin();
    	
    	//Insert project for employee
    	
    }
    
    public static void getProjects(){
    	
    	// Get projects for Employee with Id : nEmpId
    	
    	List<Project> listProjectsForEmployee=new EmployeeListAPI().getAllProjects();
    	
    }
    
    public static void getEmployees(){
    	
    	
    	EmployeeListAPI objEmployeeListAPI=new EmployeeListAPI();
        List<EmployeeInfo> listObjEmployeeInfo= objEmployeeListAPI.MakeAPIObject();
       
        
    	EmployeeListAPI objEmpListAPI=new EmployeeListAPI();
        List<Employee> listEmployees= objEmpListAPI.getAllEmployees();
       
        JSONSerializer modelSerializer=new JSONSerializer().exclude("class","entityId","persistent").rootName("employees");
        Logger.info("number of employees:"+listEmployees.size());
        render("Application/index.html", listEmployees, listObjEmployeeInfo);
    }
    
    /**
     * 
     * @param sEmpId 
     * @param sProjId
     * @param sQuarter
     * @param sWeekNumber
     */
   public static void updateEmpProjOccupied(String sEmpId, String sProjId, String sProjName, String sQuarter, String sWeekNumber, String sOccupied){
	
	   // Testing 
	   sEmpId="1";
	   sProjId="1";
	   sProjName="BUS";
	   sQuarter="4";
	   sOccupied="89";
	   
	   int nWeekNum= ProjectOccupied.getWeekNumber(sQuarter, sWeekNumber);
	   new ProjectOccupied().updateResourcePlan(sEmpId, sProjId, sProjName, nWeekNum, sOccupied);
   }

}