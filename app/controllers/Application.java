package controllers;

import java.util.List;

import javax.persistence.EntityManager;

import models.Employee;
import models.EmployeeInfo;
import models.EmployeeListAPI;
import models.EmployeeQuarter;
import models.Project;
import models.ProjectInfo;
import models.ProjectOccupied;
import models.WeekInfo;
import play.Logger;
import play.db.jpa.JPA;
import play.mvc.Controller;
import flexjson.JSONSerializer;

public class Application extends Controller {

    public static void index() {
    	
    	int nStartWeek=1;
    	int nEndWeek=13;
    	
    	EmployeeListAPI objEmployeeListAPI=new EmployeeListAPI();
    	List<EmployeeInfo> listEmployeeInfo=objEmployeeListAPI.MakeAPIObject(nStartWeek, nEndWeek);
    	List<Project> listProjects=new EmployeeListAPI().getAllProjects();
    	render("Application/index.html",listEmployeeInfo,listProjects);
    }
    
    public static void getJSONEmployeeInfo(){
    	/*EmployeeListAPI objEmployeeListAPI=new EmployeeListAPI();
        List<EmployeeInfo> listObjEmployeeInfo= objEmployeeListAPI.MakeAPIObject();
       
        
        JSONSerializer modelSerializer = new JSONSerializer().exclude("class").include("listProjectWorking").rootName("employees").exclude("listProjectWorking.class","nEmpId","listProjectWorking.nProjectId", "listProjectWorking.nWeekNumber", "listProjectWorking.nOccupied");
        renderJSON(modelSerializer.serialize(listObjEmployeeInfo));*/
        
    }
    
    // Add a project for employee
    public static void addProject(String projectId, String employeeId ){
    	   	
    	int nProjectId=Integer.parseInt(projectId);
    	int nEmployeeId=Integer.parseInt(employeeId);
    	//TODO remove fixed value for nWeekNum
    	int nWeekNum=1;
    	
        if(nProjectId>0 && nEmployeeId>0){
        	ProjectOccupied.addProject(nEmployeeId, nProjectId,ProjectOccupied.getProjectName(nProjectId),nWeekNum);
        	Logger.info("project name:"+ ProjectOccupied.getProjectName(nProjectId));
        }
        
    }
    
    public static void addNewProject(String projectName){
    	
    	EmployeeListAPI objEmployeeListAPI=new EmployeeListAPI();
    	if(projectName.length()>0){
    		objEmployeeListAPI.addNewProject(projectName);
        	Logger.info("New project added :"+projectName);
    	}
    }
    public static void addNewEmployee(String employeeName){
    	
    	EmployeeListAPI objEmployeeListAPI=new EmployeeListAPI();
    	String strEmpType="regular";
    	if(employeeName.length()>0){
    		objEmployeeListAPI.addNewEmployee(employeeName, strEmpType);
    		Logger.info("New employee added :"+employeeName);
    	}
    }
    public static void addNewContractor(String contractorName){
    	EmployeeListAPI objEmployeeListAPI=new EmployeeListAPI();
    	String strEmpType="contractor";
    	
    	if(contractorName.length()>0){
    		objEmployeeListAPI.addNewEmployee(contractorName, strEmpType);
    		Logger.info("New contractor added :"+contractorName);
    	}
    }
    public static void getProjects(){
    	
    	// Get projects for Employee with Id : nEmpId
    	List<Project> listProjectsForEmployee=new EmployeeListAPI().getAllProjects();
    	
    }
    
   public static void getEmployees(String strStartWeek, String strEndWeek){
    	
    	int nStartWeek=Integer.parseInt(strStartWeek);
    	int nEndWeek=Integer.parseInt(strEndWeek);
    	
    	Logger.info("startweek :"+nStartWeek);
    	Logger.info("EndWeek :"+nEndWeek);
    	
    	EmployeeListAPI objEmployeeListAPI=new EmployeeListAPI();
        List<EmployeeInfo> listEmployeeInfo=objEmployeeListAPI.MakeAPIObject(nStartWeek, nEndWeek);
        JSONSerializer modelSerializer = new JSONSerializer().include("listProjectInfo","listProjectInfo.listWeekInfo").exclude("class","NEmpId")
        									.exclude("listProjectInfo.NProjectId","listProjectInfo.class")
        									.exclude("listProjectInfo.listWeekInfo.NOccupied", "listProjectInfo.listWeekInfo.NWeekNum","listProjectInfo.listWeekInfo.class");
        
        
        List<Project> listProjects=new EmployeeListAPI().getAllProjects();
        // renderJSON(modelSerializer.serialize(listEmployeeInfo));
        
        List<Employee> listEmployees=objEmployeeListAPI.getAllEmployees();
        
       
       render("Application/index.html",listEmployeeInfo, listProjects);
    }
    
    /**
     * 
     * @param sEmpId 
     * @param sProjId
     * @param sQuarter
     * @param sWeekNumber
     */
   public static void updateEmpProjOccupied(String sEmpId, String sProjId, String sQuarter, String sWeekNumber, String sOccupied){
	
	   Logger.info("employee Id to be added:"+sEmpId);
	   Logger.info("Project Id to be added:"+sProjId);
	   Logger.info("Quarter to be added:"+sQuarter);
	   Logger.info("WeekNumber to be added:"+sWeekNumber);
	   Logger.info("Occupied :"+sOccupied);
	   
	   // testing
	   
	   
	   String sProjName=ProjectOccupied.getProjectName((Integer.parseInt(sProjId)));
	   Logger.info("Project Name :"+sProjName);
	   
	   int nWeekNum= ProjectOccupied.getWeekNumber(sQuarter, sWeekNumber);
	   // TODO : remove
	   nWeekNum=Integer.parseInt(sWeekNumber);
	   Logger.info("week number:"+nWeekNum);
	   new ProjectOccupied().updateResourcePlan(sEmpId, sProjId, sProjName, nWeekNum, sOccupied);
   }
   
   public static void deleteEmployee(int nEmpId){
	   
	   Logger.info("employee Id to be deleted:"+nEmpId);
	   ProjectOccupied objProjOccupied=new ProjectOccupied();
	   objProjOccupied.makeInActive(nEmpId);
	   redirect("/index");
   }

}