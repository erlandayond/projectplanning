package models;

import javax.persistence.EntityManager;

import play.db.jpa.JPA;

public class ProjectAPI {

	
//	public enum ProjectType{
//		EXTERNAL(1), INTERNAL(2);
//		
//		private short type;
//		
//		private ProjectType(short t){
//			type=t;
//		}
//		
//		public short getType(){
//			return type;
//		}
//	}
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
