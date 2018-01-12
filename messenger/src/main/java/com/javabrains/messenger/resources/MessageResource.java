package com.javabrains.messenger.resources;

import java.net.URI;
import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.internal.util.Base64;

import com.javabrains.messenger.model.Message;
import com.javabrains.messenger.resources.beans.MessageFilterBean;
import com.javabrains.messenger.service.MessageService;

@Path("/messagestp")
public class MessageResource {

	MessageService messagService = new MessageService();
	
	
	//get param bean by using query param annotations
	
	/*
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<Message> getMessages(@QueryParam("year") int year
			,@QueryParam("start") int start
			,@QueryParam("size") int size)
	{
		if(year > 0){
			return messagService.getAllMessagesforYear(year);
		}
		
		if(start >= 0 &&  size > 0){
			return messagService.getAllMessagespaginated(start,size);
		}
		return messagService.getAllMessages();
	}
	*/
	
	//get param by using bean param annotation
	
	@GET
	@Produces(value = {MediaType.APPLICATION_XML, MediaType.TEXT_XML})
	public List<Message> getMessages(@BeanParam MessageFilterBean filterBean,@Context HttpHeaders headers)
	{
		List<String> securityParam = headers.getRequestHeader("Authorization");
		
		if(securityParam != null && securityParam.size() > 0){
			String authToken = securityParam.get(0);
			authToken = authToken.replaceFirst("Basic ", "");
			String decodeString = Base64.decodeAsString(authToken);
			StringTokenizer tokenizer = new StringTokenizer(decodeString, ":");
			String userName = tokenizer.nextToken();
			String password = tokenizer.nextToken();
			
			if("user".equals(userName) && "password".equals(password)){
				System.out.println("userName: "+userName+" password: "+password);
			}
			else
			{
				System.out.println("invalid username password");
				System.out.println("userName: "+userName+" password: "+password);
			}
		}
		
		
		if(filterBean.getYear() > 0){
			return messagService.getAllMessagesforYear(filterBean.getYear());
		}
		
		if(filterBean.getStart() >= 0 &&  filterBean.getSize() > 0){
			return messagService.getAllMessagespaginated(filterBean.getStart(),filterBean.getSize());
		}
		return messagService.getAllMessages();
	}
	
	@GET
	@Path("/{messageId}")
	@Produces(value = {MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
	public Message getMessage(@PathParam("messageId") long Id,@Context UriInfo uriInfo)
	{
		Message message = messagService.getMessage(Id);
		String uri = getUriForSelf(uriInfo, message);
		String uriProfile = getUriForProfile(uriInfo, message);
		String uriComment = getUriForComment(uriInfo, message);
		message.addLink(uri, "self");
		message.addLink(uriProfile, "profile");
		message.addLink(uriComment, "comments");
		return message;
	}

	private String getUriForComment(UriInfo uriInfo, Message message) {
		//bcz it is subclass 
		//so first is base class than method of that class which will throw in subclass
		String uri = uriInfo.getBaseUriBuilder()
							.path(MessageResource.class)
							.path(MessageResource.class, "getCommentResource")
							.path(CommentResource.class)
							.resolveTemplate("messageId", message.getId())
							.build()
							.toString();
		return uri;
	}
	
	private String getUriForProfile(UriInfo uriInfo, Message message) {
		String uri = uriInfo.getBaseUriBuilder()
							.path(ProfileResource.class)
							.path(message.getAuthor())
							.build()
							.toString();
		return uri;
	}
	
	private String getUriForSelf(UriInfo uriInfo, Message message) {
		String uri = uriInfo.getBaseUriBuilder()
							.path(MessageResource.class)
							.path(Long.toString(message.getId()))
							.build()
							.toString();
		return uri;
	}
	
	//add
	
	@POST
	@Consumes(value = {MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public Response  addMessage(Message message,@Context UriInfo uriInfo)
	{
		//return messagService.addMessage(message);
		
		// used to retun status with message
		Message newMessage = messagService.addMessage(message);
		
		String newId = String.valueOf(newMessage.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(uri)
				.entity(newMessage)
				.build();
		
		
	/*	return Response.status(Status.CREATED)
				.entity(newMessage)
				.build();
		*/
		/*
		 // used to get location means uri info with status code
		  // created method is used to get status code or you can use the above method
		 public Response  addMessage(Message message,@Context UriInfo uriInfo)
	{
		Message newMessage = messagService.addMessage(message);
		
		String newId = String.valueOf(newMessage.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(uri)
				.entity(newMessage)
				.build();

		
		
	}
		 */

		
		
	}
	
	// update
	
	@PUT
	@Path("/{messageId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Message updateMessage(@PathParam("messageId") long Id, Message message)
	{
		message.setId(Id);
		return messagService.updateMessage(message);
		
		
	}
	
	//remove
	
	@DELETE
	@Path("/{messageId}")
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteMessage(@PathParam("messageId") long Id)
	{
		
		 messagService.removeMessage(Id);
		
		
	}
	
	// nested resources
	
	
	@Path("/{messageId}/commentstp")
	public CommentResource getCommentResource(@PathParam("messageId") long Id)
	{
		return new CommentResource();
		
	}
	
}
