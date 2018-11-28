package com.appsdeveloperblog.app.ws.ui.model.response;

//class used to return back as response
// this is used in converting java object into outgoing json
//this class used in UI layer
public class UserRest {
	//
private String userId; // not Database id not autoincrement
private String firstName;
private String lastName;
public String getUserId() {
	return userId;
}
public void setUserId(String userId) {
	this.userId = userId;
}
public String getFirstName() {
	return firstName;
}
public void setFirstName(String firstName) {
	this.firstName = firstName;
}
public String getLastName() {
	return lastName;
}
public void setLastName(String lastName) {
	this.lastName = lastName;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
private String email;
// password dont return , so not involve in respose(any sensitive info)

}
