package com.javabrains.messenger.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.javabrains.messenger.model.ErrorMessage;

//provider is used to register this class in jax-rs

@Provider
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException>{

	@Override
	public Response toResponse(DataNotFoundException ex) {
		
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(),404,"santoshpandey878@gmail.com");
		return Response.status(Status.NOT_FOUND)
				.entity(errorMessage)
				.build();
		
	}

}
