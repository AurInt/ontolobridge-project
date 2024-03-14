package edu.miami.schurer.ontolobridge.Responses;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "Response returned for all term status requests")
public class StatusResponse {

    @Schema( required = true, example = "submitted")
    public String status;

    @Schema( required = true, example = "1")
    public Long request_id;

    @Schema( required = true, example = "1")
    public String ontology;

    @Schema( required = true, example = "http://ontolobridge.ccs.miami.edu/ONTB_25")
    public String provisional_uri;

    @Schema( required = true, example = "ONTB_25")
    public String provisional_curie;

    @Schema( required = true, example = "Accepted without changes")
    public String message;

    @Schema( required = false, example = "BAO_25")
    public String uri;

    @Schema( required = false, example = "BAO_25")
    public String curie;

    @Schema( required = false, example = "16748786")
    public long timestamp;

    @Schema( required = false, example = "2018-11-05 16:32:13.442207")
    public String datetime;

    @Schema( required = false, example = "Term")
    public String type;

    @Schema( required = true, example = "submitted")
    List<Map<String,Object>> history;

    public void setHistory(List<Map<String, Object>> history) {
        this.history = history;
    }

    public StatusResponse(){

    }

    public StatusResponse(String status,Long request_id, String provisional_uri, String provisional_curie, String message, String uri, String curie,String type, long timestamp,String datetime,String ontology) {
        this.status = status;
        this.request_id = request_id;
        this.provisional_uri = provisional_uri;
        this.provisional_curie = provisional_curie;
        this.message = message;
        this.uri = uri;
        this.curie = curie;
        this.datetime = datetime;
        this.timestamp = timestamp;
        this.type = type;
        this.ontology = ontology;
    }
}
