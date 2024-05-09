package com.uninavigator.uninavigatorapp.ApiServices;

import com.uninavigator.uninavigatorapp.controllers.Course;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
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
        courseDetails.put("instructor", new JSONObject().put("id", instructorId));
        courseDetails.put("schedule", schedule);
        courseDetails.put("description", description);
        courseDetails.put("capacity", capacity);
        courseDetails.put("startDate", startDate);
        courseDetails.put("endDate", endDate);

        RequestBody body = RequestBody.create(courseDetails.toString(), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

//    public boolean updateCourse(int courseId, String courseName, int instructorId, String schedule, String description, int capacity, String startDate, String endDate) throws Exception {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("courseName", courseName);
//        jsonObject.put("instructorId", instructorId);
//        jsonObject.put("schedule", schedule);
//        jsonObject.put("description", description);
//        jsonObject.put("capacity", capacity);
//        jsonObject.put("startDate", startDate);
//        jsonObject.put("endDate", endDate);
//
//        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
//        Request request = new Request.Builder()
//                .url(BASE_URL + "/update/" + courseId)
//                .put(body)
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            return response.isSuccessful();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

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
                .url(BASE_URL + "/")
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

    public JSONObject getCourse(int courseId) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + courseId)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return new JSONObject(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject();
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

    public boolean enrollStudent(int courseId, int studentId) {
        JSONObject enrollmentDetails = new JSONObject();
        enrollmentDetails.put("courseId", courseId);
        enrollmentDetails.put("studentId", studentId);

        RequestBody body = RequestBody.create(enrollmentDetails.toString(), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/enroll")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean dropStudent(int courseId, int studentId) {
        JSONObject dropDetails = new JSONObject();
        dropDetails.put("courseId", courseId);
        dropDetails.put("studentId", studentId);

        RequestBody body = RequestBody.create(dropDetails.toString(), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/drop")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Searches for courses by name using the backend API.
     *
     * @param courseName The name of the course to search for.
     * @return A list of courses matching the search criteria.
     */
    public List<Course> searchCourse(String courseName) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "/search").newBuilder();
        urlBuilder.addQueryParameter("courseName", courseName);

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONArray coursesArray = new JSONArray(responseBody);
                return convertJSONArrayToCourses(coursesArray);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
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
