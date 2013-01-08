package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import play.Logger;
import play.db.jpa.JPA;

public class EmployeeListAPI {
	
	public List<EmployeeInfo> MakeAPIObject(int nStartWeek, int nEndWeek){
		
		List<Employee> listEmployee=getAllEmployees();
		List<EmployeeInfo> listEmployeeInfo=new ArrayList<EmployeeInfo>();
		
		
		for(Employee emp: listEmployee){
			
			EmployeeInfo tempEmployeeInfo=new EmployeeInfo();
			EmployeeQuarter tempEmployeeQuarter=new EmployeeQuarter();
			tempEmployeeInfo.nEmpId=emp.getEmpId();
			tempEmployeeInfo.strEmpName=emp.getEmpName();
			tempEmployeeInfo.strEmpType=emp.getEmpType();
			tempEmployeeInfo.listProjectWorking= getProjectsForEmployee(emp.getEmpId());
			tempEmployeeInfo.listProjectInfo=getEmployeeProjectInfo(emp.getEmpId(), nStartWeek, nEndWeek);
		
		
			listEmployeeInfo.add(tempEmployeeInfo);
			
		}
		
		return listEmployeeInfo;
	}
	
	public List<Employee> getAllEmployees(){
		
		//Get all active employees from db
    	Query query=JPA.em().createQuery("select id, empName, empType from Employee where empActive=1");
    	List<Object> listObjEmployee =query.getResultList();
    	List<Employee> listtempEmployee=new ArrayList<Employee>();
    	
    	Logger.info("number of employees fetched:"+listObjEmployee.size());
    	
    	if(listObjEmployee.size()>0){
    		
    		for(Object objEmployee : listObjEmployee){
    			Object[] objResult=(Object[])objEmployee;
    		    int nEmpId=(Integer)objResult[0];
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
    
	public List<Project> getAllProjects(){
	
		Query query=JPA.em().createQuery("select id, projectName from Project ORDER BY projectName");
		List<Object> listResult=query.getResultList();
		List<Project> listProjects=new ArrayList<Project>();
		if(listResult.size()>0){
			for(Object tempObj: listResult){
				Object[] objResult=(Object[])tempObj;
				Project tmpProject=new Project();
				tmpProject.setProjectId((Integer)objResult[0]);
				tmpProject.setProjectName((String)objResult[1]);
				
				//Logging
				Logger.info("Project Id:"+tmpProject.getProjectId());
				Logger.info("Project Name:"+tmpProject.getProjectName());
				listProjects.add(tmpProject);
			}
		}
		
		return listProjects;
	}
	
	//Get employee
	public static Employee getEmployee(int nEmpId){
		TypedQuery<Employee> query=JPA.em().createQuery("select e from Employee e where e.empId=:nEmpId",Employee.class);
		query.setParameter("nEmpId", nEmpId);
		List<Employee> listEmployees=query.getResultList();
		if(listEmployees.size()==1){
			return listEmployees.get(0);
		}else{
			return null;
		}
		
	}
	public void addNewProject(String strProjectName){
		EntityManager em=JPA.newEntityManager();
		Project objProject=new Project();
			
		
			em.getTransaction().begin();
			//Adding a new employee to the database
			objProject.setProjectName(strProjectName);
			
			em.persist(objProject);
			em.getTransaction().commit();
			
	}
	
   public void addNewEmployee(String strEmployeeName, String strEmpType){
	   EntityManager em=JPA.newEntityManager();
	   Employee objEmployee=new Employee();
	   em.getTransaction().begin();
	   	objEmployee.setEmpName(strEmployeeName);
	   	objEmployee.setEmpType(strEmpType);
	   em.persist(objEmployee);
	   em.getTransaction().commit();
   }
	private List<ProjectOccupied> getProjectsForEmployee(int nEmpId){
	    
		Query query=JPA.em().createQuery("select projectId, projectName, week, occupied from Resourceplan where empId=:id and projActive=1 ");
		query.setParameter("id",nEmpId);
		
		List<Object> listResult=query.getResultList();
		List<ProjectOccupied> listProjectOccupied=new ArrayList<ProjectOccupied>();
		
		if(listResult.size()>0){
			for(Object tempObj: listResult){
				Object[] objResult=(Object[])tempObj;
				ProjectOccupied tmpProjectOccupied=new ProjectOccupied();
				tmpProjectOccupied.nProjectId=(Integer)objResult[0];
				tmpProjectOccupied.strProjectName=(String)objResult[1];
				tmpProjectOccupied.nWeekNumber=(Integer)objResult[2];
				tmpProjectOccupied.nOccupied=(Integer)objResult[3];
				
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
	
	
	
	public List<ProjectInfo> getEmployeeProjectInfo(int nEmpId, int startWeek, int endWeek){
		
		
		int nStartWeek=startWeek; 
		int nLastWeek=endWeek;
		String strProjectName="";
		int nProjectId=0;
		final int NOTCHANGED=-1;
		final int DEFAULT=0;
		
		//Get ProjectInfo for the employee
		Query query=JPA.em().createQuery("select distinct(projectId), projectName FROM Resourceplan where empId=:nEId and projActive=1");
		query.setParameter("nEId",nEmpId);
		
		List<Object> listObjResult=query.getResultList();
		
		// Project Count greater than 0 ?
		// Loop through number of projects times
		List<ProjectInfo> listProjectInfo=new ArrayList<ProjectInfo>();
		if(listObjResult.size()>0){
		
			for(Object tempObj: listObjResult){
				Object[] objResult=(Object[])tempObj;
				
				// Create a temp projectInfo object
				ProjectInfo objProjectInfo=new ProjectInfo();
				
				// Set to local variables
				nProjectId=(Integer)objResult[0];
				strProjectName=objResult[1].toString();
				
				//set values to ProjectInfo object
				objProjectInfo.nProjectId=nProjectId;
				objProjectInfo.strProjectName=strProjectName;
				
				//Get week info - week number and occupied for each project
				Query tempQuery=JPA.em().createQuery("SELECT week, occupied FROM Resourceplan where empId=:nEid and projectId=:nProjId and week>=:nWeek and projActive=1");
				tempQuery.setParameter("nEid", nEmpId);
				tempQuery.setParameter("nProjId", nProjectId); 
				tempQuery.setParameter("nWeek",nStartWeek);
				
				List<Object> listWeekObjResult=tempQuery.getResultList();
				
				// WeekInfo count greater than 0 ?
				// Loop through number of number of times - here we are looping 13 weeks ( Quarter)
				List<WeekInfo> listWeekInfo=new ArrayList<WeekInfo>();
				
				//Initialize key with week numbers and values are -1
				//TODO: remove 12 here
				int nTempWeek=nStartWeek;
				for(int i=nStartWeek;i<=nLastWeek;i++){
					WeekInfo week = new WeekInfo();
					week.nOccupied=NOTCHANGED;
					week.nWeekNum=nTempWeek;
					listWeekInfo.add(week);
					nTempWeek++;
				}
				
				if(listWeekObjResult.size()>0){
					
					for(Object tempWeekObj: listWeekObjResult){
						Object[] objTempWeekResult=(Object[])tempWeekObj;
						
						// Create local week object
						WeekInfo objWeekInfo=new WeekInfo();
						
						// Set to local variables;
						int nWeek=(Integer)objTempWeekResult[0];
						int nOccupied=(Integer)objTempWeekResult[1];
						
						objWeekInfo.nWeekNum=nWeek;
						
						
						for(WeekInfo week: listWeekInfo){
							
							if(week.nWeekNum==objWeekInfo.nWeekNum){
								
								week.nOccupied=nOccupied;
								Logger.info("nWeekNum:"+week.nWeekNum);
								Logger.info("nOccupied:"+week.nOccupied);
							}
							
						}
						
					}
					
					
				}
				
				// If week occupied are not found in db, change that values to Zero
				for(WeekInfo week: listWeekInfo){
					
					if(week.nOccupied==NOTCHANGED){
						week.nOccupied=DEFAULT;
					}
					
					
				}
				
				objProjectInfo.listWeekInfo=listWeekInfo;
				listProjectInfo.add(objProjectInfo);
			}
		}
		
		return listProjectInfo;
	}
}
