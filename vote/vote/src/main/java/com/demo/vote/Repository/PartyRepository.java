package com.demo.vote.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.vote.Entity.Party;

@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {
   //using repository various CRUD operation can be performed 
} 
