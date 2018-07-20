package com.cs673.teamA.Iteration2;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {
	public List<UserProfile> findByUsernameContainingIgnoreCase(String username);
	public UserProfile findByUsername(String username);
}
