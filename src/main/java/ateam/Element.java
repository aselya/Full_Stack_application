//Comment
package ateam;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
//Comment
import javax.persistence.Id;

@Entity
public class Element{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String elementname;
	private Integer elementorder;
	private String description;
	private Integer projectid;
	
	public Integer getId(){
		return id;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public String getElementname(){
		return elementname;
	}
	public void setElementname(String elementname){
		this.elementname=elementname;
	}
	public Integer getElementorder(){
		return elementorder;
	}
	public void setElementorder(Integer elementorder){
		this.elementorder=elementorder;
	}
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description=description;
	}
	public Integer getProjectid(){
		return projectid;
	}
	public void setProjectid(Integer projectid){
		this.projectid=projectid;
	}
}