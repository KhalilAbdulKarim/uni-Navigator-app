package com.uninavigator.uninavigatorapp.ApiServices;

import com.uninavigator.uninavigatorapp.controllers.course.Course;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CourseService {
    private final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String BASE_URL = "http://localhost:8080/api/courses";

    JSONObject jsonObject = new JSONObject();
    JSONArray jsonArray = new JSONArray();




    public boolean createCourse(String courseName, int instructorId, String schedule, String description, int capacity, String startDate, String endDate) {
        JSONObject courseDetails = new JSONObject();
        courseDetails.put("courseName", courseName);
        courseDetails.put("instructor", new JSONObject().put("userId", instructorId));  // Nested JSON for instructor
        courseDetails.put("schedule", schedule);
        courseDetails.put("description", description);
        courseDetails.put("capacity", capacity);
        courseDetails.put("startDate", startDate);
        courseDetails.put("endDate", endDate);

        RequestBody body = RequestBody.create(courseDetails.toString(), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/createCourse")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateCourse(int courseId, String courseName, int instructorId, String schedule, String description, int capacity, String startDate, String endDate) {
        JSONObject courseDetails = new JSONObject();
        courseDetails.put("courseName", courseName);
        courseDetails.put("instructor", new JSONObject().put("id", instructorId));
        courseDetails.put("schedule", schedule);
        courseDetails.put("description", description);
        courseDetails.put("capacity", capacity);
        courseDetails.put("startDate", startDate);
        courseDetails.put("endDate", endDate);

        RequestBody body = RequestBody.create(courseDetails.toString(), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + courseId)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCourse(int courseId) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + courseId)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public JSONArray getAllCourses() {
        Request request = new Request.Builder()
                .url(BASE_URL + "/AllCourses")
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                return new JSONArray(responseBody);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public JSONArray getCourseById(int courseId) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + courseId)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                return new JSONArray(responseBody);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }


    public JSONArray getCoursesByInstructor(int instructorId) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/instructor/" + instructorId)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return new JSONArray(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }




    public JSONArray getCoursesByStudent(int studentId) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/student/" + studentId)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return new JSONArray(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }



    /**
     * Searches for courses by name using the backend API.
     *
     * @param courseName The name of the course to search for.
     * @return A list of courses matching the search criteria.
     */

    public JSONArray searchCoursesByName(String courseName) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/search?courseName=" + courseName)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return new JSONArray(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    /**
     * Helper method to convert a JSONArray of courses to a List<Course>.
     *
     * @param coursesArray The JSONArray of course JSON objects.
     * @return A list of Course objects.
     */
    private List<Course> convertJSONArrayToCourses(JSONArray coursesArray) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < coursesArray.length(); i++) {
            JSONObject courseObject = coursesArray.getJSONObject(i);
            Course course = new Course(
                    courseObject.getInt("courseId"),
                    courseObject.getString("courseName"),
                    courseObject.getJSONObject("instructor").getString("name"), // Make sure the JSON structure matches
                    courseObject.getString("schedule"),
                    courseObject.getString("description"),
                    courseObject.getInt("capacity"),
                    dateFormat.parse(courseObject.getString("startDate")),
                    dateFormat.parse(courseObject.getString("endDate"))
            );
            courses.add(course);
        }
        return courses;
    }

}
