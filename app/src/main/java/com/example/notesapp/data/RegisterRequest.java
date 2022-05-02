package com.example.notesapp.data;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://studev.groept.be/api/a21pt103/ add member";
    private Map<String, String> params;

    public RegisterRequest(String lastname, String firstname, String username, String password,String email, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("LastName", lastname);
        params.put("FirstName", firstname);
        params.put("UserName", username);
        params.put("Password", password);
        params.put("Email", email);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
