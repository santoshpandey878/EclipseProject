package com.javabrains.messenger.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.javabrains.messenger.database.DatabaseClass;
import com.javabrains.messenger.exception.DataNotFoundException;
import com.javabrains.messenger.model.Message;


public class MessageService {

	private Map<Long,Message> messages = DatabaseClass.getMessages();
	
	
	
	public MessageService(){
		messages.put(1L,new Message(1,"hello world","santosh"));
		messages.put(2L,new Message(2,"hello world","pandey"));
	}
	
	public List<Message> getAllMessagesforYear(int year){
	
		List<Message> messageforyear = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		for(Message message : messages.values()){
			cal.setTime(message.getCreated());
			if(cal.get(Calendar.YEAR) == year){
				messageforyear.add(message);
			}
		}
		
		return messageforyear;
	}
	
	public List<Message> getAllMessagespaginated(int start, int size){
		
		ArrayList<Message> list = new ArrayList<Message>(messages.values());
		
		return list.subList(start, start+size);
	}
	
	public List<Message> getAllMessages(){
		/*
		Message m1 = new Message(1,"hello world","santosh");
		Message m2 = new Message(2,"hello jersey","pandey");
		
		List<Message> list = new ArrayList<>();
		list.add(m1);
		list.add(m2);
		return list;   
		*/
		
		return new ArrayList<Message>(messages.values());
		
	}
	
	public Message getMessage(long id){
		
		Message message = messages.get(id);
		if(message == null){
			throw new DataNotFoundException("Message with id : "+id+" not found");
		}
		return message;
		
	}
	
	public Message addMessage(Message message){
		
		message.setId(messages.size() + 1);
		messages.put((long) message.getId(),message);
		return message;
		
		
	}
	
public Message updateMessage(Message message){
		
		if(message.getId() == 0){
			return null;
		}
		messages.put((long) message.getId(),message);
		return message;
		
		
	}
	
public Message removeMessage(long id){
	return messages.remove(id);
		
}
	
}
