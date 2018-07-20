package com.cs673.teamA.Iteration2;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {

	public List<Comment> findByIssueId(Long issueId);
	
}
