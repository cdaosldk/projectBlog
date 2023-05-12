package com.example.projectblog.domain.user.repository;

import com.example.projectblog.domain.user.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
