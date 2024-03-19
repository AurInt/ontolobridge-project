package edu.miami.schurer.ontolobridge.Responses;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response returned for all term requests")
public class RequestResponse {

    @Schema( example = "25")
    public Integer requestID;

    @Schema( example = "http://ontolobridge.ccs.miami.edu/ONTB_25")
    public String provisional_uri;

    @Schema( example = "ONTB_25")
    public String provisional_curie;

    public RequestResponse(Integer requestID, String provisional_uri, String provisional_curie) {
        this.requestID = requestID;
        this.provisional_uri = provisional_uri;
        this.provisional_curie = provisional_curie;
    }
}