package ssamppong.fitchingWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssamppong.fitchingWeb.entity.BodyPart;
import ssamppong.fitchingWeb.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface BodyPartRepository extends JpaRepository<BodyPart, Long> {
     Optional<BodyPart> findByUserAndPartName(User user, String partName);
     List<BodyPart> findByUser(User user);
}
