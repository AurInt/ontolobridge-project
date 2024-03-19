package edu.miami.schurer.ontolobridge.Controllers;

import com.opencsv.CSVWriter;
import edu.miami.schurer.ontolobridge.Responses.ExceptionResponse;
import edu.miami.schurer.ontolobridge.Responses.RequestResponse;
import edu.miami.schurer.ontolobridge.utilities.OntoloException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/retrieve")
public class RetrieveController extends BaseController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful requests",
                    content = { @Content( schema = @Schema(implementation = RequestResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = { @Content( schema = @Schema(implementation = ExceptionResponse.class)) })
    }
    )
    @RequestMapping(path="/Requests", method= RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    //@Operation(value = "", authorizations = { @Authorization(value="jwtToken"),@Authorization(value="token") })
    public @ResponseBody ResponseEntity requestTerm(@Parameter(name = "Ontology to get new terms for" ,required = true) @RequestParam(value="ontology") String ontology,
                               @Parameter(name = "Minimum status to return", required = false) @RequestParam(value="status") String status,
                               @Parameter(name = "Type of request to return to return", required = false) @RequestParam(value="type") String type ) throws OntoloException {

        //ResponseEntity respEntity = null;


        String sql = "select label,description,uri_ontology,uri_identifier,uri_superclass,superclass_ontology,superclass_id,superclass_label,current_message,request_type,id from requests where uri_ontology = ? and submission_status != 'rejected'";
        StringBuilder csv = new StringBuilder();
        List<Object> args = new ArrayList<>();
        args.add(ontology);

        HashMap<String,Object> termMap = new HashMap<>();
        Set<String> keys = new LinkedHashSet<>();
        JDBCTemplate.query(sql,
                args.toArray(),
                (RowMapper<Object>) (rs, rowNum) -> {
                    ResultSetMetaData metadata = rs.getMetaData();
                    HashMap<String, String> array = new HashMap<>();
                    array.put("termType", "new " + rs.getString("request_type"));
                    if (rowNum == 1)
                        keys.add("termType");
                    for (int i = 1; i <= metadata.getColumnCount(); i++) {
                        if (metadata.getColumnName(i).equals("uri_superclass") && rowNum != 1) {
                            keys.add("superclass_label");
                            keys.add("superclass");
                        }
                        if (metadata.getColumnName(i).contains("superclass"))
                            continue;
                        array.put(metadata.getColumnName(i), rs.getString(i));
                        if (rowNum != 1)
                            keys.add(metadata.getColumnName(i));
                    }
                    if (rs.getString("uri_superclass") != null && !rs.getString("uri_superclass").isEmpty())
                        array.put("superclass", rs.getString("uri_superclass"));
                    else if (rs.getString("superclass_ontology") != null && !rs.getString("superclass_ontology").isEmpty() && rs.getString("superclass_id") != null && !rs.getString("superclass_id").isEmpty())
                        array.put("superclass", rs.getString("superclass_ontology") + "_" + rs.getString("superclass_id"));
                    else if (rs.getString("superclass_id") != null && isInteger(rs.getString("superclass_id")))
                        array.put("superclass", "ONTB_" + rs.getString("superclass_id"));
                    else
                        array.put("superclass", "");
                    termMap.put("ONTB_" + rs.getString("id"), array);
                    return 0;
                 });
        List<Object> outList = new ArrayList<>();
        int i = 0;
        //loop through excess array and get only those that don't have a parent still in the array and add them to the end of output
        System.out.println("\r\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        for (String key : termMap.keySet()) {
            System.out.println(key);
        }
        System.out.println("\r\n\n\n\n");
        while(termMap.size() > outList.size()){
            for (String key : termMap.keySet()) {
                if(!termMap.get(key).getClass().getName().equals("java.util.HashMap"))
                    throw new OntoloException("Invalid object");
                HashMap o = (HashMap)termMap.get(key);
                if(!outList.contains(o) && (!termMap.containsKey((String)o.get("superclass")) || outList.contains(termMap.get((String)o.get("superclass"))))){
                    if(termMap.containsKey((String)o.get("superclass"))){
                        HashMap p = (HashMap)termMap.get((String)o.get("superclass"));
                        o.put("superclass_label", p.get("label"));
                    }
                    outList.add(o);
                }
            }
            i++;
            if(i>10000){
                throw new OntoloException("Too many iterations");
            }
        }
        StringWriter s = new StringWriter();
        CSVWriter writer = new CSVWriter(s);
        String[] Keys =  ((HashMap<String,Object>)outList.get(0)).keySet().toArray(new String[((HashMap)outList.get(0)).size()]);
        writer.writeNext(Keys);
        for(Object o: outList){
            HashMap<String,String> array =(HashMap)o;
            String[] values = new String[array.values().size()];
            values = array.values().toArray(values);
            writer.writeNext(values);
        }
        try {
            writer.close();
        }catch (IOException e){
            throw new OntoloException("Failed to close");
        }
        DateFormat formatter = new SimpleDateFormat("ddMMyyyy");

        Date today = new Date();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type","text/csv");
        responseHeaders.add( "content-disposition", "attachment;filename="+ontology+"-newTerms-"+formatter.format(today)+".csv");
        return new ResponseEntity(s.toString(), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(path="/ontologies", method= RequestMethod.GET)
    @PreAuthorize("permitAll()")
    public List<Map<String, Object>> requestOntologies() throws OntoloException {
        return req.GetAllOntologies();
    }
}
