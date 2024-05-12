package com.uninavigator.uninavigatorapp.ApiServices;
import com.uninavigator.uninavigatorapp.controllers.Project.Project;
import com.uninavigator.uninavigatorapp.controllers.Project.ProjectDTO;
import com.uninavigator.uninavigatorapp.controllers.user.User;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ProjectService {
    private final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private static final String BASE_URL = "http://localhost:8080/api/projects";
    private static final String SECOND_BASE_URL = "http://localhost:8080/api";

    // Fetch all students
    public List<User> getAllStudents() {
        Request request = new Request.Builder()
                .url(SECOND_BASE_URL + "/users/students")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                System.out.println("Response Body: " + responseBody);
                JSONArray jsonArray = new JSONArray(responseBody);
                return convertJSONArrayToUserList(jsonArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    private List<User> convertJSONArrayToUserList(JSONArray jsonArray) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            User user = new User(
                    jsonObject.getInt("userId"),
                    jsonObject.getString("username"),
                    jsonObject.getString("email"),
                    jsonObject.getString("firstName"),
                    jsonObject.getString("lastName"),
                    jsonObject.getString("role"),
                    jsonObject.optString("dob","")
            );
            users.add(user);
        }
        return users;
    }
    public List<Project> getAllProjects() {
        Request request = new Request.Builder()
                .url(BASE_URL+"/allProjects")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONArray jsonArray = new JSONArray(responseBody);
                return convertJSONArrayToProjectList(jsonArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    public  List<ProjectDTO> getProjectsForStudent(int studentId) {
        String url = BASE_URL + "/student/" + studentId;
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonData = response.body().string();
                JSONArray jsonArray = new JSONArray(jsonData);
                return convertJSONArrayToProjectStudentList(jsonArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<ProjectDTO> convertJSONArrayToProjectStudentList(JSONArray jsonArray) {
        List<ProjectDTO> projectList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            ProjectDTO project = new ProjectDTO(
                    jsonObject.getString("projectName"),
                    jsonObject.getString("description"),
                    jsonObject.getString("instructorName")
            );
            projectList.add(project);
        }
        return projectList;
    }

    // Convert JSONArray to List of Project
    private List<Project> convertJSONArrayToProjectList(JSONArray jsonArray) {
        List<Project> projects = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Project project = new Project(
                    jsonObject.getLong("projectId"),
                    jsonObject.getString("projectName"),
                    jsonObject.getString("description")
            );
            projects.add(project);
        }
        return projects;
    }

    // Add a user to a project
    public boolean addUserToProject(Long projectId, int userId) {
        String url = BASE_URL + "/" + projectId + "/addUser/" + userId;
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create("", JSON))
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Remove a user from a project
    public boolean removeUserFromProject(Long projectId, int userId) {
        String url = BASE_URL + "/" + projectId + "/removeUser/" + userId;
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Save or update a project
    public boolean saveOrUpdateProject(Project project) {
        JSONObject json = new JSONObject();
        json.put("projectId", project.getProjectId());
        json.put("projectName", project.getProjectName());
        json.put("description", project.getDescription());

        RequestBody body = RequestBody.create(json.toString(), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
