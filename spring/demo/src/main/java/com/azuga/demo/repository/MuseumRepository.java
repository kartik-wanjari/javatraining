package com.azuga.demo.repository;

import com.azuga.demo.model.Museum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MuseumRepository extends JpaRepository<Museum,Integer> {

}
