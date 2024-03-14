package edu.miami.schurer.ontolobridge.Responses;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response returned for all errors")
public class ExceptionResponse {


    public int error;


    @Schema( required = true, example = "An Internal Error Has Occured")
    public String message;

    public ExceptionResponse(String message) {
        this.message = message;
    }
}
