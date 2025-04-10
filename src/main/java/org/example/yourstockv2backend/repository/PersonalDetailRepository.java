package org.example.yourstockv2backend.repository;

import java.util.Optional;
import org.example.yourstockv2backend.model.PersonalDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonalDetailRepository extends JpaRepository<PersonalDetail, Long> {
    Optional<PersonalDetail> findByEmail(String email);
}
