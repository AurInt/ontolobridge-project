package edu.miami.schurer.ontolobridge;

import edu.miami.schurer.ontolobridge.library.AuthLibrary;
import edu.miami.schurer.ontolobridge.library.NotificationLibrary;
import edu.miami.schurer.ontolobridge.library.RequestsLibrary;
import edu.miami.schurer.ontolobridge.library.RoleRepository;
import edu.miami.schurer.ontolobridge.models.Role;
import edu.miami.schurer.ontolobridge.models.RoleName;
import edu.miami.schurer.ontolobridge.utilities.AppProperties;
import edu.miami.schurer.ontolobridge.utilities.OntoloException;
import edu.miami.schurer.ontolobridge.utilities.OntoloSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.annotation.PostConstruct;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseController {

    //Named template to allow insertion of array into query in SigCSmallMoleculeLibrary
    @Autowired
    protected JdbcTemplate JDBCTemplate;

    @Value("${api.cpanel.apitoken}")
    String cpanelApiKey;

    @Value("${app.general.frontendURL}")
    String frontendURL;

    @Autowired
    protected AppProperties appProp;

    @Autowired
    public OntologyManagerService Manager;

    @Autowired
    OntoloSecurityService securityService;

    @Autowired
    public NotifierService notifier;

    NotificationLibrary notLib ;
    AuthLibrary auth;
    RequestsLibrary req;


    @Autowired
    RoleRepository roleRepository;


    @PostConstruct
    void Init(){
        notLib = new NotificationLibrary(appProp);
        auth = new AuthLibrary(JDBCTemplate);
        req = new RequestsLibrary(JDBCTemplate,cpanelApiKey,notifier,Manager,notLib,appProp,securityService);
        Logger logger = Logger.getLogger("org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver");
        logger.setLevel(Level.SEVERE);

        logger = Logger.getLogger("org.springframework.web.servlet.PageNotFound");
        logger.setLevel(Level.SEVERE);
    }
    protected  boolean hasRole(Authentication auth, RoleName role) throws OntoloException{
        Role retrievedRole = roleRepository.findByName(role)
                .orElseThrow(() -> new OntoloException("Role not found.",5));
        return auth.getAuthorities().contains(new SimpleGrantedAuthority(retrievedRole.getName().toString()));
    }

    protected HashMap<String, Object> formatResults(HashMap<String, Object> results, List<?> data){
        results.put("data",cleanData(data));
        results.put("count",data.size());
        return results;
    }
    protected HashMap<String, Object> formatResults(HashMap<String, Object> results, Object data){
        results.put("data",cleanData(data));
        results.put("count",1);
        return results;
    }
    protected HashMap<String, Object> formatResults(List<?> data){
        HashMap<String, Object> results = new HashMap<>();
        results.put("data",cleanData(data));
        results.put("count",data.size());
        return results;
    }
    protected HashMap<String, Object> formatResults(HashMap<?,?> data){
        HashMap<String, Object> results = new HashMap<>();
        results.put("data",cleanData(data));
        results.put("count",data.size());
        return results;
    }
    protected HashMap<String, Object> formatResults(Object data){
        HashMap<String, Object> results = new HashMap<>();
        results.put("data",cleanData(data));
        results.put("count",1);
        return results;
    }

    protected HashMap<String, Object> formatResults(HashMap<String, Object> results, List<?> data,String name){
        results.put(name,cleanData(data));
        results.put("count",data.size());
        return results;
    }
    protected HashMap<String, Object> formatResults(HashMap<String, Object> results, Object data,String name){
        results.put(name,cleanData(data));
        results.put("count",1);
        return results;
    }
    protected HashMap<String, Object> formatResults(List<?> data,String name){
        HashMap<String, Object> results = new HashMap<>();
        results.put(name,cleanData(data));
        results.put("count",data.size());
        return results;
    }
    protected HashMap<String, Object> formatResults(HashMap<?,?> data,String name){
        HashMap<String, Object> results = new HashMap<>();
        results.put(name,cleanData(data));
        results.put("count",data.size());
        return results;
    }

    protected HashMap<String, Object> formatResults(Object data,String name){
        HashMap<String, Object> results = new HashMap<>();
        results.put(name,cleanData(data));
        results.put("count",1);
        return results;
    }

    protected HashMap<String, Object> formatResultsWithoutCount(HashMap<String, Object> results, List<?> data){
        results.put("data",cleanData(data));
        return results;
    }
    protected HashMap<String, Object> formatResultsWithoutCount(HashMap<String, Object> results, Object data){
        results.put("data",cleanData(data));
        return results;
    }
    protected HashMap<String, Object> formatResultsWithoutCount(List<?> data){
        HashMap<String, Object> results = new HashMap<>();
        results.put("data",cleanData(data));
        return results;
    }
    protected HashMap<String, Object> formatResultsWithoutCount(HashMap<?,?> data){
        HashMap<String, Object> results = new HashMap<>();
        results.put("data",cleanData(data));
        return results;
    }
    protected HashMap<String, Object> formatResultsWithoutCount(Object data){
        HashMap<String, Object> results = new HashMap<>();
        results.put("data",cleanData(data));
        return results;
    }

    protected HashMap<String, Object> formatResultsWithoutCount(HashMap<String, Object> results, List<?> data,String name){
        results.put(name,cleanData(data));
        return results;
    }
    protected HashMap<String, Object> formatResultsWithoutCount(HashMap<String, Object> results, Object data,String name){
        results.put(name,cleanData(data));
        return results;
    }
    protected HashMap<String, Object> formatResultsWithoutCount(List<?> data,String name){
        HashMap<String, Object> results = new HashMap<>();
        results.put(name,cleanData(data));
        return results;
    }
    protected HashMap<String, Object> formatResultsWithoutCount(HashMap<?,?> data,String name){
        HashMap<String, Object> results = new HashMap<>();
        results.put(name,cleanData(data));
        return results;
    }
    protected HashMap<String, Object> formatResultsWithoutCount(Object data,String name){
        HashMap<String, Object> results = new HashMap<>();
        results.put(name,cleanData(data));
        return results;
    }

    //function to remove the SQLtypes
    protected Object cleanData(Object data){
        if(data instanceof Map) {
            Map<String, Object> dataMap = (Map<String, Object>) data;
            List<String> removeList = new ArrayList<>();
            if (dataMap != null) {
                for (String key : dataMap.keySet()) {
                    if(key == null) {
                        System.out.println("Null key found in response, please check database");
                        System.out.println(dataMap);
                        removeList.add(key);
                    }
                    //get rid of null items
                    if (dataMap.get(key) instanceof List || dataMap.get(key) instanceof Map) {
                        dataMap.put(key, cleanData(dataMap.get(key)));
                    }
                    if (dataMap.get(key) instanceof Array newArray) {
                        try {
                            dataMap.put(key, newArray.getArray());
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    if(dataMap.get(key) == null ||
                            dataMap.get(key).toString().equals(""))
                        removeList.add(key);
                }
                for(String k:removeList){
                    dataMap.remove(k);
                }
                data = dataMap;
            }
        }
        else if(data instanceof List) {
            List<Object> dataList = (List<Object>) data;
            if (dataList != null) {
                for(int i = (dataList.size()-1); i >= 0; i--) {
                    if (dataList.get(i) instanceof List ||dataList.get(i) instanceof Map) {
                        dataList.set(i,cleanData(dataList.get(i)));
                    }
                    if (dataList.get(i) instanceof Array newArray) {
                        try {
                            dataList.set(i,newArray.getArray());
                        } catch (Exception e) {
                            dataList.remove(i);
                            continue;
                        }
                    }
                    if(dataList.get(i) == null ||
                            dataList.get(i).toString().equals(""))
                        dataList.remove(i);
                }
                data = dataList;
            }
        }
        return data;
    }

    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
}