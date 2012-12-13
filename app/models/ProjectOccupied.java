/**
 * 
 */
package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;

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
		
		int nResId=projectExists(nEmpId, nProjId, nWeekNumber);
		boolean flag=false;
		// Update record in Resourceplan table
		if(nResId>0){
			updateProject(nEmpId, nProjId, nResId, sProjName, nWeekNumber, nOccupied);
			flag=true;
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
	
	private int projectExists(int nEmpId, int nProjId, int nWeekNum){
		
		CriteriaBuilder cb=JPA.em().getCriteriaBuilder();
		
		Query query=JPA.em().createQuery("select id from Resourceplan where week:weekNum and empId=:eId and projectId=:projId ");
		query.setParameter("eId",nEmpId);
		query.setParameter("projId",nProjId);
		query.setParameter("weekNum", nWeekNum);
		
	    Object objTemp=query.getSingleResult();
	    if(objTemp!=null){
	    
	    	Object []objResult=(Object[])objTemp;
	    	return (int)objResult[0];
	    }else{
	    	
	    	return -1; // If no resource record found, return -1
	    }
	}
	
	private boolean updateProject(int nEmpId, int nProjId, int nResId, String sProjName, int nWeekNum, int nOccupied){
		
		Resourceplan objResourceplan=new Resourceplan().findById(nResId);
		
		if(objResourceplan!=null){
			objResourceplan.setOccupied(nOccupied);
			return true;
		}else{
			return false;
		}
	}
}

