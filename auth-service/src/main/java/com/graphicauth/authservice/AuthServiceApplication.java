package com.graphicauth.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

//	@Bean
//	CommandLineRunner commandLineRunner(UserService userService){
//		return args -> {
//			Role appRole =userService.saveRole( new Role(null,"APP_USER", null));
//			Role adminRole = userService.saveRole( new Role(null,"ADMIN_USER", null));
//
//			User thilina = userService.saveUser(new User(null, "thilina", "1,1|2,2|3,3|4,4","thilina@gmail.com",true,true,"1,1|2,2|3,3|4,4",4L,null));
//			User gayathri = userService.saveUser(new User(null, "gayathri", "1,1|2,2|3,3|4,4","gayathri@gmail.com",true,true,"1,1|2,2|3,3|4,4",4L,null));
//
//			userService.addUserRole("thilina","ADMIN_USER");
//			userService.addUserRole("gayathri","APP_USER");
//		};
//	}

}
