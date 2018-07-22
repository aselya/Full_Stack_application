package ateam;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Userinfo{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String fn;
	private String ln;
	private String email;
	public Integer getId(){
		return id;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public String getFn(){
		return fn;
	}
	public void setFn(String fn){
		this.fn=fn;
	}
	public String getLn(){
		return ln;
	}
	public void setLn(String ln){
		this.ln=ln;
	}
	public String getEmail(){
		return email;
	}
	public void setEmail(String email){
		this.email=email;
	}
}