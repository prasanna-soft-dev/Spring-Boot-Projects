package com.demo.vote.Repository;


import com.demo.vote.Entity.Voter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VoterRepository extends JpaRepository<Voter, Long> {
    Optional<Voter> findByNameAndVoterId(String name, Long voterId);
}
