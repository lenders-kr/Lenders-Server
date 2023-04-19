package kr.api.lenders.domain;

import kr.api.lenders.domain.type.PostStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByIdAndStatusNot(long id, PostStatusType status);

    Page<Post> findAllByLocationDistrict(String district, Pageable pageable);
}
