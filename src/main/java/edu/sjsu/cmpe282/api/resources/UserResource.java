package edu.sjsu.cmpe282.api.resources;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.sjsu.cmpe282.domain.User;
import edu.sjsu.cmpe282.dto.UserDao;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
	
	private UserDao userdao = new UserDao();
	@POST
	@Path("/signup")
	public User signUp(User user) throws ClassNotFoundException {
		
		System.out.print("user created: "+user.getFirstName());
		userdao.addUser(user);
		return user;//Response.status(201).entity("User Created : \n"+ user.getFirstName()).build();
	}

	@POST
	@Path("/signin")
	public boolean signIn(User user)
	{
		return userdao.checkUser(user);
	}
}
