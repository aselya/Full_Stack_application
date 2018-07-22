package ateam;

import org.springframework.data.repository.CrudRepository;

import ateam.Task;

public interface TaskRepository extends CrudRepository<Task, Integer>{

}