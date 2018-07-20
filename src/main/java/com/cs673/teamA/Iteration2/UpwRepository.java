package com.cs673.teamA.Iteration2;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface UpwRepository extends CrudRepository<Upw, Integer> {

	public Upw findByUnAndPw(String un, String pw);
	public List<Upw> findByUnContaining(String un);
}
