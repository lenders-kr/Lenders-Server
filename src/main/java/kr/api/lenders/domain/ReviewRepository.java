package kr.api.lenders.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByPostAndUser(Post post, User currentUser);

    Page<Review> findAllByUserId(long userId, Pageable pageable);

    Page<Review> findAllByPostId(long postId, Pageable pageable);
}
