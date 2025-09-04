package org.example.stage_back.repository;



import org.example.stage_back.entities.FactureEntete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactureRepository extends JpaRepository<FactureEntete, Integer> {

}
