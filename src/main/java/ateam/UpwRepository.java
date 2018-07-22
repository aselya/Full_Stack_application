package ateam;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
import ateam.Upw;

public interface UpwRepository extends CrudRepository<Upw, Integer>{
	@Nullable
	Upw findByUnAndPw(String un, String pw);
	boolean existsById(Integer id);
}