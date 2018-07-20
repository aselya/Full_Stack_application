package com.cs673.teamA.Iteration2;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface IssueRepository extends CrudRepository<IssueTicket, Long> {
	
	public List<IssueTicket> findByProjectId(Long projectId);
}
