package com.devsync.DevSync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
//.\bin\windows\kafka-server-start.bat .\config\server.properties
@SpringBootApplication
public class DevSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevSyncApplication.class, args);
	}

}
