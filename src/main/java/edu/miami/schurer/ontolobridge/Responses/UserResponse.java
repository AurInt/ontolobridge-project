package edu.miami.schurer.ontolobridge.Responses;


import edu.miami.schurer.ontolobridge.models.Detail;
import edu.miami.schurer.ontolobridge.models.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Schema(description = "Response returned for all term status requests")
public class UserResponse{

    @Schema(required = true, example = "0")
    public long user_id;

    @Schema(required = true, example = "test")
    public String username;

    @Schema(required = true, example = "test@example.com")
    public String email;

    @Schema(required = true, example = "true")
    public boolean verified;

    @Schema(required = true, example = "true")
    public Map<String,String> details = new HashMap<>();



    public UserResponse() {
    }

    public UserResponse(ResultSet data) throws SQLException {
        this.user_id = data.getInt("id");
        this.username = data.getString("email");
        this.email = data.getString("email");
        this.verified = data.getBoolean("verified");
    }
    public UserResponse(User user){
        this.user_id = user.getId();
        this.username = user.getEmail();
        this.email = user.getEmail();
        this.verified = user.isVerified();
        for(Detail d: new ArrayList<>(user.getDetails())){
            details.put(d.getField(),d.getValue());
        }
    }


    public UserResponse(int user_id, String username, String email, boolean verified) {
        this.user_id = user_id;
        this.username = email;
        this.email = email;
        this.verified = verified;
    }
}