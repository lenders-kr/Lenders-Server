package kr.api.lenders.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByPostAndUser(Post post, User currentUser);
}
