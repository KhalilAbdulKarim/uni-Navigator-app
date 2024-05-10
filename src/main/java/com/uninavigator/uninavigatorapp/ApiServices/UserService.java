package com.uninavigator.uninavigatorapp.ApiServices;
import com.uninavigator.uninavigatorapp.controllers.user.User;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserService {
    private final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String BASE_URL = "http://localhost:8080/api/users";


    JSONObject jsonObject = new JSONObject();

    JSONArray jsonArray = new JSONArray();

    public boolean createUser(String username, String password, String email, String firstName, String lastName, String role, LocalDate dob, String requestStatus) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        jsonObject.put("email", email);
        jsonObject.put("firstName", firstName);
        jsonObject.put("lastName", lastName);
        jsonObject.put("role", role);
        jsonObject.put("dob", dob.toString());
        jsonObject.put("requestStatus", requestStatus);

        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/register")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();  // Returns true if the HTTP code is in the 200-299 range
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String updateUser(int userId, String username, String email, String password, String firstName, String lastName, LocalDate dob) throws Exception {

        jsonObject.put("username", username);
        jsonObject.put("email", email);
        jsonObject.put("password", password);
        jsonObject.put("firstName", firstName);
        jsonObject.put("lastName", lastName);
        jsonObject.put("dob", dob.toString());

        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/users/" + userId)
                .put(body)
                .build();
        return executeRequest(request);
    }


//    public User authenticateUser(String username, String password) throws Exception {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("username", username);
//        jsonObject.put("password", password);
//
//        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json; charset=utf-8"));
//        Request request = new Request.Builder()
//                .url(BASE_URL + "/authenticate")
//                .post(body)
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            if (response.isSuccessful() && response.body() != null) {
//                String responseBody = response.body().string();
//                JSONObject userJson = new JSONObject(responseBody);
//
//                return new User(
//                        userJson.getInt("userId"),
//                        userJson.getString("username"),
//                        userJson.getString("email"),
//                        userJson.getString("firstName"),
//                        userJson.getString("lastName"),
//                        userJson.getString("role"),
//                        userJson.getString("dob")
//                );
//            } else {
//                throw new Exception("Authentication failed: " + response);
//            }
//        }
//    }

    public User authenticateUser(String username, String password) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);

        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(BASE_URL + "/authenticate")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONObject userJson = new JSONObject(responseBody);

                return new User(
                        userJson.optInt("userId", -1),  // Use optInt to provide a default if key is missing
                        userJson.getString("username"),
                        userJson.getString("email"),
                        userJson.getString("firstName"),
                        userJson.getString("lastName"),
                        userJson.getString("role"),
                        userJson.getString("dob")
                );
            } else {
                throw new Exception("Authentication failed: " + response);
            }
        }
    }



    public boolean doesUsernameExist(String username) throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/users/exists/" + username)
                .get()
                .build();
        return Boolean.parseBoolean(executeRequest(request));
    }

    public boolean requestInstructorStatus(int userId) throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/users/request-instructor-status/" + userId)
                .post(RequestBody.create("", null))
                .build();
        return Boolean.parseBoolean(executeRequest(request));
    }


    //    public boolean approveInstructorRequest(int userId) throws Exception {
//        Request request = new Request.Builder()
//                .url("http://localhost:8080/api/users/approve-instructor-request/" + userId)
//                .post(RequestBody.create("", null))
//                .build();
//        return Boolean.parseBoolean(executeRequest(request));
//    }
    public boolean approveInstructorRequest(int userId) throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/users/approve-instructor-request/" + userId)
                .post(RequestBody.create("", MediaType.parse("application/json")))
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();  // Check if the response status code is in the range 200-299
        }
    }

    public boolean declineInstructorRequest(int userId) throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/users/decline-instructor-request/" + userId)
                .post(RequestBody.create("", MediaType.parse("application/json")))
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();  // Check if the response status code is in the range 200-299
        }
    }


    public List<JSONObject> getAllInstructorRequests() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/users/instructor-requests")
                .get()
                .build();
        return parseJsonArray(executeRequest(request));
    }

    public User findInstructorByName(String firstName, String lastName) throws Exception {
        List<JSONObject> instructors = getAllInstuctors();
        for (JSONObject instructor : instructors) {
            if (instructor.getString("firstName").equalsIgnoreCase(firstName) &&
                    instructor.getString("lastName").equalsIgnoreCase(lastName)) {
                return new User(
                        instructor.optInt("userId", -1), // Default to -1 if userId is missing
                        instructor.optString("username", "defaultUsername"),
                        instructor.optString("email", "no-reply@example.com"),
                        instructor.optString("firstName", "Unknown"),
                        instructor.optString("lastName", ""),
                        instructor.optString("role", "User"),
                        instructor.optString("dob", LocalDate.now().toString())
                );
            }
        }
        return null;
    }


    public List<JSONObject> getAllUsers() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/users")
                .get()
                .build();
        return parseJsonArray(executeRequest(request));
    }

    public List<JSONObject> getAllInstuctors() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/users/instructors")
                .get()
                .build();
        return parseJsonArray(executeRequest(request));
    }


    public Optional<JSONObject> getUserById(int userId) throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/users/" + userId)
                .get()
                .build();
        String response = executeRequest(request);
        return response.isEmpty() ? Optional.empty() : Optional.of(new JSONObject(response));
    }

    public String saveUser(JSONObject user) throws Exception {
        RequestBody body = RequestBody.create(user.toString(), JSON);
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/users")
                .post(body)
                .build();
        return executeRequest(request);
    }

    public void deleteUser(int userId) throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/users/" + userId)
                .delete()
                .build();
        executeRequest(request);
    }

    private String executeRequest(Request request) throws Exception {
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return Objects.requireNonNull(response.body()).string();
            } else {
                throw new IOException("Request failed: " + response);
            }
        }
    }

    private List<JSONObject> parseJsonArray(String jsonArrayString) {
        jsonArray = new JSONArray(jsonArrayString);
        List<JSONObject> jsonObjects = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObjects.add(jsonArray.getJSONObject(i));
        }
        return jsonObjects;
    }


    public User getUserDetailsByUsername(String username) throws Exception {
        Request request = new Request.Builder()
                .url(BASE_URL + "/username/" + username)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                int userId = jsonObject.getInt("userId");
                String email = jsonObject.getString("email");
                String firstName = jsonObject.getString("firstName");
                String lastName = jsonObject.getString("lastName");
                String role = jsonObject.getString("role");
                String dob = jsonObject.getString("dob");

                return new User(userId, username, email, firstName, lastName, role, dob);
            } else {
                throw new Exception("Request failed: " + response);
            }
        }
    }
}



