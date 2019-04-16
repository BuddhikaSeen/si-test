package com.surroundinsurance.user.service.platform.common;

public enum SurroundInsuranceExceptionCodes {

	INTERNAL_SERVER_ERROR("We are unable to process your request at this time. Please try again later."),
	BAD_REQUEST("Not a valid request. Please ensure all required parameters are present and in valid format."),
	BAD_GATEWAY("The proxy server received an invalid response from an upstream server");

    private final String explanation;

    private SurroundInsuranceExceptionCodes(String reasonPhrase) {
        this.explanation = reasonPhrase;
    }

    /**
     * Return the reason phrase of this status code.
     */
    public String getDescription() {
        return explanation;
    }
}
