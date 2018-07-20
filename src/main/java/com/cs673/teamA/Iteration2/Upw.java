package com.cs673.teamA.Iteration2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Upw {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String un;
	private String pw;
	
	public Upw() {
		
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the un
	 */
	public String getUn() {
		return un;
	}

	/**
	 * @param un the un to set
	 */
	public void setUn(String un) {
		this.un = un;
	}

	/**
	 * @return the pw
	 */
	public String getPw() {
		return pw;
	}

	/**
	 * @param pw the pw to set
	 */
	public void setPw(String pw) {
		this.pw = pw;
	}
	
	
}
