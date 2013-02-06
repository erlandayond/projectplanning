package models;

import javax.persistence.EntityManager;

import play.db.jpa.JPA;

public class ProjectAPI {

	
	public enum ProjectType{
		 EXTERNAL((short)1), INTERNAL((short)2);
		
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
	
	
}
