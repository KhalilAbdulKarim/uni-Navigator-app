package com.uninavigator.uninavigatorapp.ApiServices;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class EnrolmentService {
    private final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String ENROLLMENT_URL = "http://localhost:8080/api/enrollments";


    public boolean enrollStudent(int courseId, int userId) {
        JSONObject enrollmentDetails = new JSONObject();
        enrollmentDetails.put("courseId", courseId);
        enrollmentDetails.put("userId", userId);

        RequestBody body = RequestBody.create(enrollmentDetails.toString(), JSON);
        Request request = new Request.Builder()
                .url(ENROLLMENT_URL + "/enroll")
                .post(body)
                .header("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Failed to enroll student: " + response.body().string());
            }
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }



    public boolean dropStudent(int courseId, int userId) {
        JSONObject dropDetails = new JSONObject();
        dropDetails.put("courseId", courseId);
        dropDetails.put("userId", userId);

        RequestBody body = RequestBody.create(dropDetails.toString(), JSON);
        Request request = new Request.Builder()
                .url(ENROLLMENT_URL + "/drop")
                .post(body)
                .header("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Failed to drop student: " + response.body().string());
            }
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
