package edu.utah.cdmcc.exceptions;

public class UtahToolboxException extends RuntimeException {

	private static final long serialVersionUID = -6988413749320267277L;
	private ErrorCode errorCode = ErrorCode.OK;
	private String message;
	
	public enum ErrorCode {
		OK, HIBERNATE_ERROR, DROOLS_FIRE_ENGINE_ERROR,DROOLS_DEBUG_ERROR,
		REPORT_FORMAT_NOT_OPENED, SCHEMA_UPDATE_ERROR,DATE_PARSE_ERROR,
		PART_INIT_ERROR, REPORT_ENGINE_FAILURE,LAB_RANGE_ERROR,
		NULL_PATIENT_ERROR
	}
	
	public UtahToolboxException(ErrorCode errorCode, final Throwable cause){
		this.errorCode = errorCode;
		try {
			handleException();
		} catch (Exception e) {
			e.printStackTrace();
		}
		cause.printStackTrace();
	}
	
	public UtahToolboxException(ErrorCode errorCode){
		this.errorCode = errorCode;
		try {
			handleException();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public UtahToolboxException(ErrorCode errorCode, String message){
		this.errorCode = errorCode;
		this.message = message;
		try {
			handleException();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void handleException() throws Exception {
		System.out.println("Utah Toolbox Exception fired:");
		switch (errorCode) {
		case OK:
			throw new Exception("TILT:  Should never get here.");
		case DATE_PARSE_ERROR:
			//TODO figure out how to handle errors
		case DROOLS_DEBUG_ERROR:
			//TODO figure out how to handle errors
		case DROOLS_FIRE_ENGINE_ERROR:
			//TODO figure out how to handle errors
		case HIBERNATE_ERROR:
			//TODO figure out how to handle errors
		case PART_INIT_ERROR:
			//TODO figure out how to handle errors
		case REPORT_ENGINE_FAILURE:
			//TODO figure out how to handle errors
		case REPORT_FORMAT_NOT_OPENED:
			//TODO figure out how to handle errors
		case SCHEMA_UPDATE_ERROR:
			//TODO figure out how to handle errors
		case LAB_RANGE_ERROR:
			//TODO figure out how to handle errors
		case NULL_PATIENT_ERROR:
			System.out.println(message);
		}
	}

	
	
	
}
