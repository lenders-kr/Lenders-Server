package kr.api.lenders.domain;

import kr.api.lenders.domain.type.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndStatus(String email, StatusType status);
    Optional<User> findByIdAndStatus(long id, StatusType status);
}
