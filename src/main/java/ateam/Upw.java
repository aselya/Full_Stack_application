package ateam;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Upw{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String un;
	private String pw;
	public Integer getId(){
		return id;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public String getUn(){
		return un;
	}
	public void setUn(String un){
		this.un=un;
	}
	public String getPw(){
		return pw;
	}
	public void setPw(String pw){
		this.pw=pw;
	}
}