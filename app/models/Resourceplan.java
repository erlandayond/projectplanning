package models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the resourceplan database table.
 * 
 */
@Entity
public class Resourceplan extends play.db.jpa.GenericModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String resId;

	private int occupied;

	private int projectId;

	private String projectName;

	private int week;

	//bi-directional many-to-one association to Employee
	@ManyToOne
	@JoinColumn(name="empId")
	private Employee employee;

	public Resourceplan() {
	}

	public String getResId() {
		return this.resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public int getOccupied() {
		return this.occupied;
	}

	public void setOccupied(int occupied) {
		this.occupied = occupied;
	}

	public int getProjectId() {
		return this.projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getWeek() {
		return this.week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public Employee getEmployee() {
		return this.employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}