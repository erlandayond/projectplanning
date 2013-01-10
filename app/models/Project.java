package models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the project database table.
 * 
 */
@Entity
public class Project extends play.db.jpa.GenericModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int projectId;

	private String projectName;
    
	private boolean active;
	
	public Project() {
	}

	public int getProjectId() {
		return this.projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public boolean getActive(){
		return this.active;
	}
	
	public void setActive(boolean active){
		this.active=active;
	}
	
	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}