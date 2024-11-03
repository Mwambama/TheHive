package com.example.hiveeapp.student_user.setting;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.hiveeapp.student_user.chat.ChatDto;
import com.example.hiveeapp.student_user.swipe.JobPosting;
import com.example.hiveeapp.volley.VolleyMultipartRequest;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentApi {

    private static final String BASE_URL = "http://coms-3090-063.class.las.iastate.edu:8080/student";
    private static final String TAG = "StudentApi";

    /**
     * Generates the headers for API requests with authorization.
     *
     * @param context The application context used to retrieve user credentials.
     * @return A map of headers including content type and authorization credentials.
     */
    public static Map<String, String> getHeaders(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        // Mocked username and password for testing purposes
        String username = "iiik@gmail.com";
        String password = "Anondwdb##444fedo";

        String credentials = username + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        return headers;
    }

    /**
     * Retrieves a list of students from the server.
     *
     * @param context       The application context.
     * @param listener      Response listener for successful fetch.
     * @param errorListener Error listener for handling errors.
     */
    public static void getStudent(Context context, int userId, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/" + userId;
        Log.d(TAG, "GET Student Request URL: " + url);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                listener,
                error -> handleErrorResponse("Error fetching student", error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return StudentApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }


    /**
     * Adds a new student to the server.
     *
     * @param context       The application context.
     * @param studentData   JSON object containing student details.
     * @param listener      Response listener for successful student creation.
     * @param errorListener Error listener for handling errors.
     */
    public static void addStudent(Context context, JSONObject studentData, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL;
        Log.d(TAG, "POST Student Request URL: " + url);
        Log.d(TAG, "Request Payload: " + studentData.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                studentData,
                listener,
                error -> handleErrorResponse("Error adding student", error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return StudentApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Updates an existing student.
     *
     * @param context       The application context.
     * @param studentData   JSON object containing student details.
     * @param listener      Response listener for successful student update.
     * @param errorListener Error listener for handling errors.
     */
    public static void updateStudent(Context context, JSONObject studentData, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL;
        Log.d(TAG, "PUT Student Request URL: " + url);
        Log.d(TAG, "Student Data Payload: " + studentData.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                studentData,
                listener,
                error -> handleErrorResponse("Error updating student", error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return StudentApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Deletes a student from the server.
     *
     * @param context       The application context.
     * @param studentId     ID of the student to be deleted.
     * @param listener      Response listener for successful student deletion.
     * @param errorListener Error listener for handling errors.
     */
    public static void deleteStudent(Context context, long studentId, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/" + studentId;
        Log.d(TAG, "DELETE Student Request URL: " + url);

        // Create a StringRequest for the DELETE method
        StringRequest request = new StringRequest(
                Request.Method.DELETE,
                url,
                response -> {
                    Log.d(TAG, "Student deleted successfully: " + response);
                    listener.onResponse(response);  // Notify the listener of success
                },
                error -> {
                    String errorMsg = getErrorMessage(error);
                    handleErrorResponse("Error deleting student: " + errorMsg, error, errorListener);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return StudentApi.getHeaders(context);
            }
        };

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    private static byte[] getBytesFromUri(Context context, Uri uri) throws IOException {
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream()) {
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            return byteBuffer.toByteArray();
        }
    }


    /**
     * Uploads a PDF resume to the server for a specific user.
     *
     * @param context       The application context.
     * @param userId        ID of the student.
     * @param pdfUri        Uri of the PDF file to upload.
     * @param listener      Response listener for successful upload.
     * @param errorListener Error listener for handling errors.
     */
    /**
     * Uploads a PDF resume to the server for a specific user.
     *
     * @param context       The application context.
     * @param userId        ID of the student.
     * @param pdfUri        Uri of the PDF file to upload.
     * @param listener      Response listener for successful upload.
     * @param errorListener Error listener for handling errors.
     */
    public static void uploadPdfToServer(Context context, int userId, Uri pdfUri,
                                         Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/" + userId + "/upload-resume";
        Log.d(TAG, "POST Upload PDF Request URL: " + url);

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(
                Request.Method.POST,
                url,
                response -> listener.onResponse(new String(response.data)),
                error -> handleErrorResponse("Error uploading PDF", error, errorListener)
        );

        try {
            byte[] pdfData = getBytesFromUri(context, pdfUri);
            Map<String, VolleyMultipartRequest.DataPart> byteData = new HashMap<>();
            byteData.put("file", new VolleyMultipartRequest.DataPart("resume.pdf", pdfData, "application/pdf"));
            multipartRequest.setByteData(byteData);
        } catch (IOException e) {
            Log.e(TAG, "Error reading PDF file: " + e.getMessage());
        }

        VolleySingleton.getInstance(context).addToRequestQueue(multipartRequest);
    }


    public static void applyForJob(Context context, int studentId, int jobPostingId,
                                   Response.Listener<String> listener,
                                   Response.ErrorListener errorListener) {
        String url = BASE_URL + "/applications/apply";

        // Create the request payload
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("studentId", studentId);
            requestBody.put("jobPostingId", jobPostingId);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestBody,
                response -> {
                    // Success response
                    listener.onResponse("Application submitted successfully!");
                },
                error -> {
                    // Error response
                    if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                        String errorMsg = "Student has already applied for this job posting!";
                        handleErrorResponse(errorMsg, error, errorListener);
                    } else {
                        handleErrorResponse("Error applying for job", error, errorListener);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return StudentApi.getHeaders(context);
            }
        };

        // Add the request to the queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void getStudentApplications(Context context, int studentId,
                                              Response.Listener<JSONArray> listener,
                                              Response.ErrorListener errorListener) {
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/applications/student?studentId=" + studentId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                listener,
                error -> handleErrorResponse("Error fetching applications", error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return StudentApi.getHeaders(context);
            }
        };

        // Add the request to the queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    public static void getJobPostings(Context context,
                                      Response.Listener<List<JobPosting>> listener,
                                      Response.ErrorListener errorListener) {
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/job-posting";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    List<JobPosting> jobPostings = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jobJson = response.getJSONObject(i);
                            JobPosting job = new JobPosting(
                                    jobJson.getInt("jobPostingId"),
                                    jobJson.getString("title"),
                                    jobJson.getString("description"),
                                    jobJson.getString("summary"),
                                    jobJson.getDouble("salary"),
                                    jobJson.getString("jobType"),
                                    jobJson.getDouble("minimumGpa"),
                                    jobJson.getString("jobStart"),
                                    jobJson.getString("applicationStart"),
                                    jobJson.getString("applicationEnd"),
                                    jobJson.getInt("employerId"),
                                    jobJson.optString("companyName", "Unknown Company")
                            );
                            jobPostings.add(job);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    listener.onResponse(jobPostings);
                },
                error -> {
                    errorListener.onErrorResponse(error);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return StudentApi.getHeaders(context);
            }
        };

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    public static void getChats(Context context, Response.Listener<List<ChatDto>> successListener, Response.ErrorListener errorListener) {
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/chat";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<ChatDto> chatList = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject chatObject = response.getJSONObject(i);

                            // Extract jobPostingId from the response or set a default if not available
                            int jobPostingId = chatObject.has("jobPostingId") ? chatObject.getInt("jobPostingId") : -1;

                            String jobTitle = "Unknown Title";

                            ChatDto chat = new ChatDto(
                                    chatObject.getInt("chatId"),
                                    chatObject.getInt("employerId"),
                                    chatObject.getInt("studentId"),
                                    jobPostingId,
                                    jobTitle
                            );
                            chatList.add(chat);
                        }
                        successListener.onResponse(chatList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorListener.onErrorResponse(new VolleyError("JSON parsing error: " + e.getMessage()));
                    }
                },
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return StudentApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static void getChatsByJobPostingId(Context context, int jobPostingId, int studentId,
                                              Response.Listener<List<ChatDto>> successListener,
                                              Response.ErrorListener errorListener) {
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/chat";
        Log.d(TAG, "Fetching chats from URL: " + url);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<ChatDto> chatList = new ArrayList<>();
                    try {
                        Log.d(TAG, "Full chat response: " + response.toString());

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject chatObject = response.getJSONObject(i);

                            int parsedStudentId = chatObject.getInt("studentId");
                            int parsedJobPostingId = chatObject.optInt("jobPostingId", -1);
                            int chatId = chatObject.getInt("chatId");

                            Log.d(TAG, "Parsed chat: chatId=" + chatId +
                                    ", studentId=" + parsedStudentId +
                                    ", jobPostingId=" + parsedJobPostingId);

                            if (parsedStudentId == studentId) {
                                ChatDto chat = new ChatDto(
                                        chatId,
                                        chatObject.getInt("employerId"),
                                        parsedStudentId,
                                        parsedJobPostingId,
                                        chatObject.optString("jobTitle", "Unknown Title")
                                );
                                chatList.add(chat);
                            }
                        }
                        successListener.onResponse(chatList);
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing chat JSON: " + e.getMessage());
                        errorListener.onErrorResponse(new VolleyError("JSON parsing error: " + e.getMessage()));
                    }
                },
                error -> {
                    Log.e(TAG, "Error fetching chats: " + error.getMessage());
                    errorListener.onErrorResponse(error);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return StudentApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static void getJobPostingsByEmployerId(Context context, long employerId, Response.Listener<List<JobPosting>> successListener, Response.ErrorListener errorListener) {
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/job-posting?employerId=" + employerId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<JobPosting> jobPostings = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jobJson = response.getJSONObject(i);
                            JobPosting job = new JobPosting(
                                    jobJson.getInt("jobPostingId"),
                                    jobJson.getString("title"),
                                    jobJson.getString("description"),
                                    jobJson.getString("summary"),
                                    jobJson.getDouble("salary"),
                                    jobJson.getString("jobType"),
                                    jobJson.getDouble("minimumGpa"),
                                    jobJson.getString("jobStart"),
                                    jobJson.getString("applicationStart"),
                                    jobJson.getString("applicationEnd"),
                                    jobJson.getInt("employerId"),
                                    jobJson.optString("companyName", "Unknown Company")
                            );
                            jobPostings.add(job);
                        }
                        successListener.onResponse(jobPostings);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorListener.onErrorResponse(new VolleyError("JSON parsing error: " + e.getMessage()));
                    }
                },
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return StudentApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Handles error responses from the server, logs the details, and invokes the error listener.
     *
     * @param errorMessagePrefix Prefix for the error message to log.
     * @param error              The VolleyError object.
     * @param errorListener      Error listener to handle the error response.
     */
    private static void handleErrorResponse(String errorMessagePrefix, VolleyError error, Response.ErrorListener errorListener) {
        String errorMsg = getErrorMessage(error);
        String fullErrorMessage = errorMessagePrefix + ": " + errorMsg;
        Log.e(TAG, fullErrorMessage);
        errorListener.onErrorResponse(new VolleyError(fullErrorMessage));
    }

    /**
     * Retrieves the list of applications for the student and initializes chats with the employers.
     */
    public static void getChatsForStudentApplications(Context context, int studentId,
                                                      Response.Listener<List<ChatDto>> successListener,
                                                      Response.ErrorListener errorListener) {
        String url = BASE_URL + "/applications/student?studentId=" + studentId;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    List<ChatDto> chatList = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject application = response.getJSONObject(i);
                            int jobPostingId = application.getInt("jobPostingId");

                            // Initialize chat for the application
                            initializeChatForJobPosting(context, jobPostingId, studentId, chatList,
                                    () -> successListener.onResponse(chatList),
                                    errorListener);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            errorListener.onErrorResponse(new VolleyError("JSON parsing error: " + e.getMessage()));
                        }
                    }
                },
                error -> {
                    Log.e(TAG, "Error fetching applications: " + error.getMessage());
                    errorListener.onErrorResponse(error);
                }
        );

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Initializes a chat for each job application.
     */
    private static void initializeChatForJobPosting(Context context, int jobPostingId, int studentId,
                                                    List<ChatDto> chatList, Runnable onComplete,
                                                    Response.ErrorListener errorListener) {
        String jobUrl = BASE_URL + "/job-posting/" + jobPostingId;

        JsonArrayRequest jobRequest = new JsonArrayRequest(
                Request.Method.GET, jobUrl, null,
                response -> {
                    try {
                        JSONObject job = response.getJSONObject(0);
                        int employerId = job.getInt("employerId");

                        // Placeholder for jobTitle
                        String jobTitle = "Unknown Title";

                        // Create a new ChatDto using the appropriate constructor
                        ChatDto chat = new ChatDto(-1, employerId, studentId, jobPostingId, jobTitle);
                        chatList.add(chat);

                        // Check if all chats are added
                        onComplete.run();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorListener.onErrorResponse(new VolleyError("JSON parsing error: " + e.getMessage()));
                    }
                },
                error -> {
                    Log.e(TAG, "Error fetching job posting: " + error.getMessage());
                    errorListener.onErrorResponse(error);
                }
        );

        VolleySingleton.getInstance(context).addToRequestQueue(jobRequest);
    }

    /**
     * Extracts a meaningful error message from a VolleyError.
     *
     * @param error The VolleyError object.
     * @return A string containing the error message.
     */
    private static String getErrorMessage(VolleyError error) {
        String errorMsg = "An unexpected error occurred";
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String errorData = new String(error.networkResponse.data, "UTF-8");

                // Attempt to parse errorData as JSON
                try {
                    JSONObject jsonError = new JSONObject(errorData);
                    errorMsg = jsonError.optString("message", errorMsg);
                } catch (JSONException jsonException) {
                    errorMsg = errorData;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                errorMsg = "Error parsing error message";
            }
        } else if (error.getMessage() != null) {
            errorMsg = error.getMessage();
        }
        return errorMsg;
    }
}
