package kr.api.lenders.domain;

import kr.api.lenders.domain.type.UserSsoProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSsoDetailRepository extends JpaRepository<UserSsoDetail, Long> {
    Optional<UserSsoDetail> findByProviderAndIdentifier(UserSsoProviderType provider, String identifier);
}


