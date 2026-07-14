package project.projectblog.domain.user.repository;

import project.projectblog.domain.user.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
