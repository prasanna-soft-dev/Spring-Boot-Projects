//use only when adding parties details to two the database....if run all the time it will create 
//duplicate in the Db table

/*package com.demo.vote.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.demo.vote.Entity.Party;
import com.demo.vote.Repository.PartyRepository;

//it loads basic details of the parties into the database such as name, initial vote
@Component
public class DataLoader implements CommandLineRunner{
    @Autowired
    private PartyRepository partyRepository;

    @Override
    public void run(String... args)throws Exception{
        partyRepository.save(new Party(null, "Democratic Party", 0));
        partyRepository.save(new Party(null, "Republican Party", 0));
        partyRepository.save(new Party(null, "Green Party", 0));
        partyRepository.save(new Party(null, "Libertarian Party", 0));
        partyRepository.save(new Party(null, "Independent Party", 0));
    }

}*/
