package com.appsdeveloperblog.app.ws.ui.model.response;

public enum ErrorMessages {
	MISSING_REQUIRED_FIELD("Missing Required field,provide mandatory field"),
	RECORD_ALREADY_EXISTS("record already exists"),
	INTERNAL_SERVER_ERROR("Internal server erroe"),
	NO_RECORD_FOUND("No record found"),
	AUTHENTICATION_FAILED("Authentication failed"),
	COULD_NOT_UPDATE("could not update"),
	COULD_NOT_DELETE("could not delete"),
	EMAIL_ADDRESS_NOT_VERIFIED("Email address not verified");
	
	private String ErrorMessages;

	ErrorMessages(String errorMessages) {
		ErrorMessages = errorMessages;
	}

	public String getErrorMessages() {
		return ErrorMessages;
	}

	public void setErrorMessages(String errorMessages) {
		ErrorMessages = errorMessages;
	}

	
}
