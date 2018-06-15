package edu.bu.met.ateam.issuetracker;

import org.springframework.data.repository.CrudRepository;

public interface IssueRepository extends CrudRepository<Issue, Integer> {

}
