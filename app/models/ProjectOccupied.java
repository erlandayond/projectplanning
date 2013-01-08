/**
 * 
 */
package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;

import play.Logger;
import play.db.jpa.JPA;

/**
 * @author ayond
 *
 */
public class ProjectOccupied {
	
    public int nProjectId;
	public String strProjectName;
	public int	  nWeekNumber;
	public int	  nOccupied;
	
	public boolean updateResourcePlan(String sEmpId, String sProjId, String sProjName, int nWeekNumber, String sOccupied){
	
		int nEmpId=Integer.parseInt(sEmpId);
		int nProjId=Integer.parseInt(sProjId);
		int nOccupied=Integer.parseInt(sOccupied);
		
		
		long nResId=projectExists(nEmpId, nProjId, nWeekNumber);
		boolean flag=false;
		// Update record in Resourceplan table
		if(nResId>0){
			updateProject(nEmpId, nProjId, nResId, sProjName, nWeekNumber, nOccupied);
			flag=true;
		}else{
			//add occupied recored to Resource plan
			addProject(nEmpId,nProjId, sProjName,nWeekNumber, nOccupied );
		}
		
		return flag;
	}
	
	/**
	 * Get week number in the fiscal year
	 * @param sQuarter 
	 * @param sWeekNumber
	 * @return 
	 */
	public static int getWeekNumber(String sQuarter, String sWeekNumber){
		
		int nQuarter=0;
		int nWeekNum=0;
		if(sQuarter!=null && sWeekNumber!=null && !sQuarter.isEmpty() && !sWeekNumber.isEmpty())
		{
			nQuarter=Integer.parseInt(sQuarter);
			nWeekNum=Integer.parseInt(sWeekNumber);
		}
		
		return nQuarter*13+nWeekNum;
	}
	
	public static void addProject(int nEmpId, int nProjId, String strProjName, int nWeekNum, int nOccupied){
		Resourceplan objResourceplan=new Resourceplan();
		objResourceplan.setProjectId(nProjId);
		objResourceplan.setProjectName(strProjName);
		objResourceplan.setWeek(nWeekNum);
		objResourceplan.setEmployee(EmployeeListAPI.getEmployee(nEmpId));
		objResourceplan.setOccupied(nOccupied);
		objResourceplan.save();
		
	}
	public static void addProject(int nEmpId, int nProjId, String strProjName, int nWeekNum){
		
		Resourceplan objResourceplan=new Resourceplan();
		objResourceplan.setProjectId(nProjId);
		objResourceplan.setProjectName(strProjName);
		objResourceplan.setWeek(nWeekNum);
		objResourceplan.setEmployee(EmployeeListAPI.getEmployee(nEmpId));
		objResourceplan.save();
	}
	
	public void makeInActiveProject(int nEmpId, int nProjId){
		
		Query query=JPA.em().createQuery("update Resourceplan r set r.projActive=0 where r.employee.empId=:eId and r.projectId=:projId");
		query.setParameter("eId",nEmpId);
		query.setParameter("projId", nProjId);
		Object obj=query.executeUpdate();
		
	}
	private long projectExists(int nEmpId, int nProjId, int nWeekNum){
		
		long nResId=0;
		try{
			
			Query query=JPA.em().createQuery("select r.resId from Resourceplan r where r.week=:weekNum and r.employee.empId=:eId and r.projectId=:projId ");
			query.setParameter("eId",nEmpId);
			query.setParameter("projId",nProjId);
			query.setParameter("weekNum", nWeekNum);
		
			Object obj=query.getSingleResult();
			if(obj!=null){
				nResId= Long.parseLong((String) query.getSingleResult());
			}
			
			Logger.info("project already exists and record Id:"+nResId);
			
			
			List<Object> listResPlan=query.getResultList();
			if(listResPlan.size()==1){
				Object[] tempObj=(Object[])listResPlan.get(0);
				nResId=(Long)tempObj[0];
				
			}else{
				nResId=-1; // to mark more than one record is returning
			}
		    return nResId;
		    
		    
		    
		    
		}catch(Exception exp){
			
			return nResId; // Returns zero
		}
		
		
		
	    
		
	}
	
	private boolean updateProject(int nEmpId, int nProjId, long nResId, String sProjName, int nWeekNum, int nOccupied){
		
		Resourceplan objResourceplan=new Resourceplan().findById(String.valueOf(nResId)); // in Model class, resId is a string type. So we have to convert 
		
		if(objResourceplan!=null){
			objResourceplan.setOccupied(nOccupied);
			objResourceplan.save();
			return true;
		}else{
			return false;
		}
	}
	
	
	public static String getProjectName(int nProjectId){
		
		TypedQuery<Project> query=JPA.em().createQuery("select p from Project p where p.projectId= :nProjectId",Project.class);
		
		query.setParameter("nProjectId", nProjectId);
		List<Project> listProjects=query.getResultList();
		
		if(listProjects.size()==1){
			
			Project proj=listProjects.get(0);
			return proj.getProjectName();
			
		}else{
			
			return "";
		}
		
		
	}
	
	public void makeInActive(int nEmployeeId){
			Employee objEmployee=new Employee().findById(nEmployeeId);
			
			if(objEmployee!=null){
				objEmployee.setEmpActive(false);
				objEmployee.save();
			}
	}
}

