package ssamppong.fitchingWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssamppong.fitchingWeb.entity.BodyPart;

@Repository
public interface BodyPartRepository extends JpaRepository<BodyPart, Long> {
}
