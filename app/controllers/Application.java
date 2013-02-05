package controllers;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;


import models.Employee;
import models.EmployeeInfo;
import models.EmployeeQuarter;
import models.EmployeeListAPI;
import models.ProjectAPI;

import models.ProjectOccupied;
import models.Login;
import models.Project;
import models.Login;
import models.Utility;
import play.db.jpa.JPA;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Scope.Session;


public class Application extends Controller {

	public static int AUTOCOMPLETE_MAX=3;
	
	@Before(unless={"index","login", "current"})
    public static void isUserConnected(){
    	
    	String tempVal=Session.current().get("login");
    	Logger.info("login value:"+tempVal);
    	
    	if(tempVal!=null && tempVal.length()>0){
    		if(tempVal.equals("ayond")){
        		Logger.info("user is connected...");
        	}
    	}
    	else{
    		Logger.info("user is not connected...");
    	}
    }
	
	
    public static void index() {
    	
    	
    	render("Application/index.html");
    }
    
    public static void login(String sPassword, String sUsername){
    	Logger.info("username:"+sUsername+" Password:"+sPassword);
    
    	Login objLogin=new Login(sPassword,sUsername);
    	boolean flag=objLogin.authenticateUser();
    	if(flag){
    		Logger.info("login successful");
    		Session.current().put("login", "ayond");
    		
    	}else{
    		Logger.info("login not successful");
    		
    		
    	}
    	
    	renderJSON(flag);
    }
    
    public static void view1(){
       	
       	int nStartWeek=1; //Integer.parseInt(strStartWeek);
       	int nEndWeek=13; //Integer.parseInt(strEndWeek);
       	
       	Logger.info("startweek :"+nStartWeek);
       	Logger.info("EndWeek :"+nEndWeek);
       	
       	EmployeeListAPI objEmployeeListAPI=new EmployeeListAPI();
           List<EmployeeInfo> listEmployeeInfo=objEmployeeListAPI.MakeAPIObject(nStartWeek, nEndWeek);
         
           
           List<Project> listProjects=new EmployeeListAPI().getAllProjects();
           // renderJSON(modelSerializer.serialize(listEmployeeInfo));
        
          
          render(listEmployeeInfo, listProjects, nStartWeek, nEndWeek);
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
    
    /**
     *  Adding new project to the system. Once it is added, it will be available to add to employees
     * @param projectName
     */
    public static void addNewProject(String projectName){
    	//TODO: take parameter project type
    	
    	short nProjectType=1; //1 --- external
    	
    	ProjectAPI objProjectAPI=new ProjectAPI();
    	
    	// project name not null ?
    	if(projectName.length()>0){
    		boolean flag=new ProjectOccupied().projectNameExists(projectName);
    		if(!flag){
    			
    			objProjectAPI.addNewProject(projectName, nProjectType);
            	Logger.info("New project added :"+projectName);
    		}
    		
    	}
    }
    public static void addNewEmployee(String employeeName){
    	
    	EmployeeListAPI objEmployeeListAPI=new EmployeeListAPI();
    	String strEmpType="regular";
    	if(employeeName.length()>0){
    		boolean flag=objEmployeeListAPI.employeeNameExists(employeeName);
    		if(!flag){
    		
    			objEmployeeListAPI.addNewEmployee(employeeName, strEmpType);
        		Logger.info("New employee added :"+employeeName);
    		}
    		
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
    public static void autocompleteLabel(final String term){
    	
    	Logger.info("term received for autocomplete:"+term);
    	// get all projects
    	List<Project> listProjects=new EmployeeListAPI().getAllProjects();
    	
    	final List<String> response=new ArrayList<String>();
    	for(Project proj: listProjects){
    		if(proj.getProjectName().toLowerCase().startsWith(term.toLowerCase())){
    			response.add(proj.getProjectName());
    		}
    		if(response.size()==AUTOCOMPLETE_MAX){
    			break;
    		}
    	}
    	
    	renderJSON(response);
    }
    
   
   
   public static void view2(){
   	
   	int nStartWeek=14; //Integer.parseInt(strStartWeek);
   	int nEndWeek=26; //Integer.parseInt(strEndWeek);
   	
   	Logger.info("startweek :"+nStartWeek);
   	Logger.info("EndWeek :"+nEndWeek);
   	
   	EmployeeListAPI objEmployeeListAPI=new EmployeeListAPI();
       List<EmployeeInfo> listEmployeeInfo=objEmployeeListAPI.MakeAPIObject(nStartWeek, nEndWeek);
     
       
       List<Project> listProjects=new EmployeeListAPI().getAllProjects();
       // renderJSON(modelSerializer.serialize(listEmployeeInfo));
    
      
      
      render(listEmployeeInfo, listProjects, nStartWeek, nEndWeek);
   }
   
   public static void view3(){
   	
   	int nStartWeek=27; //Integer.parseInt(strStartWeek);
   	int nEndWeek=39; //Integer.parseInt(strEndWeek);
   	
   	Logger.info("startweek :"+nStartWeek);
   	Logger.info("EndWeek :"+nEndWeek);
   	
   	EmployeeListAPI objEmployeeListAPI=new EmployeeListAPI();
       List<EmployeeInfo> listEmployeeInfo=objEmployeeListAPI.MakeAPIObject(nStartWeek, nEndWeek);
     
       
       List<Project> listProjects=new EmployeeListAPI().getAllProjects();
       // renderJSON(modelSerializer.serialize(listEmployeeInfo));
      
      
      render(listEmployeeInfo, listProjects, nStartWeek, nEndWeek);
   }
   
   public static void view4(){
   	
   	int nStartWeek=40; //Integer.parseInt(strStartWeek);
   	int nEndWeek=52; //Integer.parseInt(strEndWeek);
   	
   	Logger.info("startweek :"+nStartWeek);
   	Logger.info("EndWeek :"+nEndWeek);
   	
   	EmployeeListAPI objEmployeeListAPI=new EmployeeListAPI();
       List<EmployeeInfo> listEmployeeInfo=objEmployeeListAPI.MakeAPIObject(nStartWeek, nEndWeek);
       
       List<Project> listProjects=new EmployeeListAPI().getAllProjects();
       // renderJSON(modelSerializer.serialize(listEmployeeInfo));
       
      render(listEmployeeInfo, listProjects, nStartWeek, nEndWeek);
   }
   
   /**
    * Current view shows weeks occupied from current week to next 13 weeks
    */
   public static void current(){
	  
	   	int nStartWeek=Utility.getCurrentWeek(); // gets current week
	   	int nEndWeek=nStartWeek+12; 
	   	
	   	Logger.info("startweek :"+nStartWeek);
	   	Logger.info("EndWeek :"+nEndWeek);
	   	
	   	EmployeeListAPI objEmployeeListAPI=new EmployeeListAPI();
	       List<EmployeeInfo> listEmployeeInfo=objEmployeeListAPI.MakeAPIObject(nStartWeek, nEndWeek);
	       
	       List<Project> listProjects=new EmployeeListAPI().getAllProjects();
	       // renderJSON(modelSerializer.serialize(listEmployeeInfo));
	       
	      render(listEmployeeInfo, listProjects, nStartWeek, nEndWeek);
   }
    /**
     * 
     * @param sEmpId 
     * @param sProjId
     * @param sQuarter
     * @param sWeekNumber
     */
   public static void updateEmpProjOccupied(String sEmpId, String sProjId, String sQuarter, String sWeekNumber, String sOccupied){
	
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
	   
   }
   
   public static void deleteProjectForEmployee(int nEmpId, int nProjId){
	   Logger.info("deleting a project "+nProjId+"for employee:"+nEmpId);
	   ProjectOccupied objProjOccupied=new ProjectOccupied();
	   objProjOccupied.makeInActiveProjectForEmployee(nEmpId, nProjId);
   }
   
   public static void deleteProject(int nProjId){
	   Logger.info("deleting a project :"+nProjId+" from system");
	   
	   ProjectOccupied objProjOccupied=new ProjectOccupied();
	   objProjOccupied.makeInActiveProject(nProjId);
	   
   }

}