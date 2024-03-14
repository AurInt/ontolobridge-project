package edu.miami.schurer.ontolobridge.Responses;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response returned for all operations on a requests")
public class OperationResponse {

    @Schema( required = true, example = "success")
    public String status;

    @Schema( required = true, example = "true")
    public boolean success;

    @Schema( required = true, example = "25")
    public long requests_id;

    public OperationResponse(String status, boolean success, long requestsID) {
        this.status = status;
        this.success = success;
        this.requests_id = requestsID;
    }
}
