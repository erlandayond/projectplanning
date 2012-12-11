package models;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the employee database table.
 * 
 */
@Entity
public class Employee extends play.db.jpa.GenericModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int empId;

	private String empName;

	private String empType;
    
	private boolean empActive=true;
	
	//bi-directional many-to-one association to Resourceplan
	@OneToMany(mappedBy="employee")
	private List<Resourceplan> resourceplans;

	public Employee() {
	}

	public int getEmpId() {
		return this.empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return this.empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpType() {
		return this.empType;
	}

	public void setEmpType(String empType) {
		this.empType = empType;
	}
    
	public boolean getEmpActive(){
		return this.empActive;
	}
	
	public void setEmpActive(boolean empActive){
		this.empActive=empActive;
	}
	
	public List<Resourceplan> getResourceplans() {
		return this.resourceplans;
	}

	public void setResourceplans(List<Resourceplan> resourceplans) {
		this.resourceplans = resourceplans;
	}

}