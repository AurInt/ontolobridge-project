package edu.miami.schurer.ontolobridge.Controllers;

import edu.miami.schurer.ontolobridge.NotifierService;
import edu.miami.schurer.ontolobridge.Responses.ExceptionResponse;
import edu.miami.schurer.ontolobridge.Responses.OperationResponse;
import edu.miami.schurer.ontolobridge.Responses.RequestResponse;
import edu.miami.schurer.ontolobridge.Responses.StatusResponse;
import edu.miami.schurer.ontolobridge.utilities.OntoloException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@PreAuthorize("isAuthenticated() and @OntoloSecurityService.isRegistered(authentication)")
@RequestMapping("/requests")
public class RequestController extends BaseController {

    public NotifierService notifier;

    @Value("${spring.profiles.active:Unknown}")
    private String activeProfile;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful requests",
                    content = { @Content( schema = @Schema(implementation = RequestResponse.class)) }),     // Doc: https://www.baeldung.com/swagger-operation-vs-apiresponse
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = { @Content( schema = @Schema(implementation = ExceptionResponse.class)) })
        }
    )
    @RequestMapping(path="/RequestTerm", method= RequestMethod.POST)
    public Object requestTerm(@Parameter(name = "Label of suggested term" ,required = true) @RequestParam(value="label") @NotBlank String label,
                              @Parameter(name = "Description of suggested term",required = true) @RequestParam(value="description") @NotBlank String description,
                              @Parameter(name = "Parent URI of suggested term",required = true) @RequestParam(value="superclass") @NotBlank String uri_superclass,
                              @Parameter(name = "Parent URI ontology of suggested term") @RequestParam(value="superclass_ontology", defaultValue = "") String superclass_ontology,
                              @Parameter(name = "Any references for this requests") @RequestParam(value="reference",defaultValue = "") @NotBlank String reference,
                              @Parameter(name = "Justification if any for adding this term") @RequestParam(value="justification",defaultValue = "") String justification,
                              @Parameter(name = "Name of the submitter if provided") @RequestParam(value="submitter",defaultValue = "") String submitter,
                              @Parameter(name = "Email of the submitter") @RequestParam(value="email",defaultValue = "") String submitter_email,
                              @Parameter(name = "Anonymize Email") @RequestParam(value="anon",defaultValue = "false") boolean anonymize,
                              @Parameter(name = "Ontology Request ") @RequestParam(value="ontology",defaultValue = "") String ontology,
                              @Parameter(name = "Should submitter be notified of changes ") @RequestParam(value="notify",defaultValue = "false") boolean notify) throws OntoloException {

        Integer id =req.RequestsTerm(label,
                description,
                uri_superclass,
                superclass_ontology,
                reference,
                justification,
                submitter,
                submitter_email,
                anonymize,
                notify,
                ontology,
                "term");
        if(id < 0)
            throw new OntoloException("Error Making Requests");

        return new RequestResponse(id,
                "http://ontolobridge.ccs.miami.edu/ONTB_"+String.format("%9d",id).replace(' ','0'),
                "ONTB_"+String.format("%9d",id).replace(' ','0'));
    }

    @RequestMapping(path="/RequestDataProperty", method= RequestMethod.POST)
    public Object requestDataProperty(@Parameter(name = "Label of suggested term" ,required = true) @RequestParam(value="label") String label,
                              @Parameter(name = "Description of suggested term",required = true) @RequestParam(value="description") String description,
                              @Parameter(name = "Superclass of suggested term",required = true) @RequestParam(value="parent_uri") String uri_superclass,
                              @Parameter(name = "Superclass ontology of suggested term") @RequestParam(value="superclass_ontology", defaultValue = "") String superclass_ontology,
                              @Parameter(name = "Any references for this requests") @RequestParam(value="reference",defaultValue = "") String reference,
                              @Parameter(name = "Justification if any for adding this term") @RequestParam(value="justification",defaultValue = "") String justification,
                              @Parameter(name = "Name of the submitter if provided") @RequestParam(value="submitter",defaultValue = "") String submitter,
                              @Parameter(name = "Email of the submitter") @RequestParam(value="email",defaultValue = "") String submitter_email,
                              @Parameter(name = "Anonymize Email") @RequestParam(value="anon",defaultValue = "false") boolean anonymize,
                              @Parameter(name = "Ontology Request ") @RequestParam(value="ontology",defaultValue = "") String ontology,
                              @Parameter(name = "Should submitter be notified of changes ") @RequestParam(value="notify",defaultValue = "false") boolean notify) {

        if(label.length() == 0)
           return new ExceptionResponse("Label is required");
        if(description.length() == 0)
            return new ExceptionResponse("Description is required");
        Integer id = req.RequestsTerm( label,
                description,
                uri_superclass,
                superclass_ontology,
                reference,
                justification,
                submitter,
                submitter_email,
                anonymize,
                notify,
                ontology,
                "Data");
        return new RequestResponse(id,
                "http://ontolobridge.ccs.miami.edu/ONTB_"+String.format("%9d",id).replace(' ','0'),
                "ONTB_"+String.format("%9d",id).replace(' ','0'));
    }

    @RequestMapping(path="/RequestObjectProperty", method= RequestMethod.POST)
    public Object requestObjectProperty(@Parameter(name = "Label of suggested term" ,required = true) @RequestParam(value="label") String label,
                                      @Parameter(name = "Description of suggested term",required = true) @RequestParam(value="description") String description,
                                      @Parameter(name = "Superclass of suggested term",required = true) @RequestParam(value="parent_uri") String uri_superclass,
                                      @Parameter(name = "Superclass ontology of suggested term") @RequestParam(value="superclass_ontology", defaultValue = "") String superclass_ontology,
                                      @Parameter(name = "Any references for this requests") @RequestParam(value="reference",defaultValue = "") String reference,
                                      @Parameter(name = "Justification if any for adding this term") @RequestParam(value="justification",defaultValue = "") String justification,
                                      @Parameter(name = "Name of the submitter if provided") @RequestParam(value="submitter",defaultValue = "") String submitter,
                                      @Parameter(name = "Email of the submitter") @RequestParam(value="email",defaultValue = "") String submitter_email,
                                      @Parameter(name = "Anonymize Email") @RequestParam(value="anon",defaultValue = "false") boolean anonymize,
                                      @Parameter(name = "Ontology Request ") @RequestParam(value="ontology",defaultValue = "") String ontology,
                                      @Parameter(name = "Should submitter be notified of changes ") @RequestParam(value="notify",defaultValue = "false") boolean notify) {

        Integer id = req.RequestsTerm(label,
                description,
                uri_superclass,
                superclass_ontology,
                reference,
                justification,
                submitter,
                submitter_email,
                anonymize,
                notify,
                ontology,
                "Object");


        return new RequestResponse(id,
                "http://ontolobridge.ccs.miami.edu/ONTB_"+String.format("%9d",id).replace(' ','0'),
                "ONTB_"+String.format("%9d",id).replace(' ','0'));
    }

    @RequestMapping(path="/RequestAnnotationProperty", method= RequestMethod.POST)
    public Object requestAnnotationProperty(@Parameter(name = "Label of suggested term" ,required = true) @RequestParam(value="label") String label,
                                        @Parameter(name = "Description of suggested term",required = true) @RequestParam(value="description") String description,
                                        @Parameter(name = "Superclass of suggested term",required = true) @RequestParam(value="parent_uri") String uri_superclass,
                                        @Parameter(name = "Superclass ontology of suggested term") @RequestParam(value="superclass_ontology", defaultValue = "") String superclass_ontology,
                                        @Parameter(name = "Any references for this requests") @RequestParam(value="reference",defaultValue = "") String reference,
                                        @Parameter(name = "Justification if any for adding this term") @RequestParam(value="justification",defaultValue = "") String justification,
                                        @Parameter(name = "Name of the submitter if provided") @RequestParam(value="submitter",defaultValue = "") String submitter,
                                        @Parameter(name = "Email of the submitter") @RequestParam(value="email",defaultValue = "") String submitter_email,
                                        @Parameter(name = "Anonymize Email") @RequestParam(value="anon",defaultValue = "false") boolean anonymize,
                                        @Parameter(name = "Ontology Request ") @RequestParam(value="ontology",defaultValue = "") String ontology,
                                        @Parameter(name = "Should submitter be notified of changes ") @RequestParam(value="notify",defaultValue = "false") boolean notify) {

        Integer id = req.RequestsTerm(label,
                description,
                uri_superclass,
                superclass_ontology,
                reference,
                justification,
                submitter,
                submitter_email,
                anonymize,
                notify,
                ontology,
                "Annotation");

        return new RequestResponse(id,
                "http://ontolobridge.ccs.miami.edu/ONTB_"+String.format("%9d",id).replace(' ','0'),
                "ONTB_"+String.format("%9d",id).replace(' ','0'));
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful requests",
                    content = { @Content( schema = @Schema(implementation = StatusResponse.class)) }),     // Doc: https://www.baeldung.com/swagger-operation-vs-apiresponse
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = { @Content( schema = @Schema(implementation = ExceptionResponse.class)) })
    }
    )
    @PreAuthorize("permitAll()")
    //@Operation(value = "", authorizations = { })
    @RequestMapping(path="/RequestStatus", method= RequestMethod.GET)
    public Object termStatus(@Parameter(name = "ID of requests",example = "0") @RequestParam(value="requestID",defaultValue = "0") Integer id,
                             @Parameter(hidden = true) @RequestParam(value="include",defaultValue = "0") String include){
        if(activeProfile.equals("prod")){
            include="";
        }
        List<StatusResponse> result = req.TermStatus(Long.valueOf(id),include);
        if(result.size() == 1)
            return result.get(0);
        return result;

    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful requests",
                    content = { @Content( schema = @Schema(implementation = OperationResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = { @Content( schema = @Schema(implementation = ExceptionResponse.class)) })
    }
    )
    @RequestMapping(path="/RequestsSetStatus", method= RequestMethod.POST)
    public Object termStatus(@Parameter(name = "ID of Forms" ,required = true,example = "0") @RequestParam(value="requestID") Integer id,
                             @Parameter(name = "New Status" ,required = true, example = "submitted,accepted,requires-response,rejected") @RequestParam(value="status")String status,
                             @Parameter(name = "Message of status" ) @RequestParam(value="message",defaultValue = "")String message){
        return req.TermUpdateStatus(id,status,message);

    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful requests",
                    content = { @Content( schema = @Schema(implementation = OperationResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = { @Content( schema = @Schema(implementation = ExceptionResponse.class)) })
    }
    )
    @RequestMapping(path="/UpdateRequest", method= RequestMethod.POST)
    //@Operation(value = "", authorizations = { @Authorization(value="jwtToken"),@Authorization(value="token") })
    public Object updateTerm(@RequestParam Map<String, String> parameters ){
        if(parameters.containsKey("id") && !parameters.get("id").isEmpty())
            return req.TermUpdate(parameters.get("id"),parameters);
        return new OperationResponse("Missing ID",false,0);
    }
}
