package models;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import play.db.jpa.JPA;

public class ProjectAPI {

	
	public enum ProjectType{
		 NONE((short)0), EXTERNAL((short)1), INTERNAL((short)2), INNOVATION((short)3), NONWORKING((short)4);
		
		 private final short value;
		 ProjectType(short val){
			 this.value=val;
		 }
		 public short getValue(){
			 return value;
		 }
	}
	
	/**
	 *  Add new project to the system 
	 * @param strProjectName
	 */
	public void addNewProject(String strProjectName, short nProjectType){
		EntityManager em=JPA.newEntityManager();
		Project objProject=new Project();
			
		
			em.getTransaction().begin();
			objProject.setProjectName(strProjectName);
			objProject.setActive(true);
			objProject.setProjectType(nProjectType);
			em.persist(objProject);
			em.getTransaction().commit();
			
	}
	
	/**
	 * Get project type for the given project. If there is more than one project for a project id, then it returns 0
	 * @param nProjectId
	 * @return project type
	 */
  public static String getProjectType(int nProjectId){
	  TypedQuery<Project> query=JPA.em().createQuery("select p from Project p where p.projectId=:projId", Project.class);
	  query.setParameter("projId", nProjectId);
	  List<Project> listProjects=query.getResultList();
	  String strType=ProjectType.NONE.name();
	  
	  if(listProjects.size()==1){
		  
		  short nTempType=listProjects.get(0).getProjectType();
		  
		  if(nTempType==1){
			 strType= ProjectType.EXTERNAL.name();
		  }else if(nTempType==2){
			  strType= ProjectType.INTERNAL.name();
		  }else if(nTempType==3){
			  strType=ProjectType.INNOVATION.name();
		  }else if(nTempType==4){
			  strType=ProjectType.NONWORKING.name();
		  }
		  
	  }
	
	  return strType;
	  
  }
	
	
}
