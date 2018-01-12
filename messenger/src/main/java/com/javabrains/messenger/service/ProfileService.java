package com.javabrains.messenger.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.javabrains.messenger.database.DatabaseClass;
import com.javabrains.messenger.model.Message;
import com.javabrains.messenger.model.Profile;

public class ProfileService {

	private Map<String,Profile> profiles = DatabaseClass.getProfiles();
	
	public ProfileService(){
		profiles.put("Santosh",new Profile(1L,"Santosh","santosh","pandey"));
		profiles.put("Beenu",new Profile(2L,"Beenu","beenu","pandy"));
	}
	
	
	public List<Profile> getAllProfiles(){
		/*
		Message m1 = new Message(1,"hello world","santosh");
		Message m2 = new Message(2,"hello jersey","pandey");
		
		List<Message> list = new ArrayList<>();
		list.add(m1);
		list.add(m2);
		return list;   
		*/
		
		return new ArrayList<Profile>(profiles.values());
		
	}
	
	public Profile getProfile(String profileName){
		
		return profiles.get(profileName);
		
	}
	
	public Profile addProfile(Profile profile){
		
		profile.setId(profiles.size() + 1);
		profiles.put(profile.getProfileName(),profile);
		return profile;
		
		
	}
	
public Profile updateProfile(Profile profile){
		
		if(profile.getProfileName().isEmpty()){
			return null;
		}
		profiles.put(profile.getProfileName(),profile);
		return profile;
		
		
	}
	
public Profile removeProfile(String profileName){
	return profiles.remove(profileName);
		
}
}
