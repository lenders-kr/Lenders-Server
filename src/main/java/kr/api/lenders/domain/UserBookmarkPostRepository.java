package kr.api.lenders.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBookmarkPostRepository extends JpaRepository<UserBookmarkPost, Long> {
    boolean existsByUserIdAndPostId(long userId, long postId);

    void deleteAllByPostId(long postId);

    Page<UserBookmarkPost> findAllByUserId(long userId, Pageable pageable);
}
