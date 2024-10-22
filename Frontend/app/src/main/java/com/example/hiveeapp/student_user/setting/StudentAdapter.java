package com.example.hiveeapp.student_user.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiveeapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Adapter class for managing and displaying a list of students in a RecyclerView.
 * Supports both read-only mode and editable mode (with update and delete options).
 */
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private JSONArray students = new JSONArray();  // Holds the list of students
    private final Context context;  // The context in which the adapter is used
    private final boolean isEditable;  // Indicates whether the list is editable (shows update/delete buttons)
    private static final String USER_PREFS = "UserPrefs";  // SharedPreferences key for user data

    /**
     * Constructor for the StudentAdapter.
     *
     * @param context    The context in which the adapter is used
     * @param isEditable Indicates if the student list is editable (shows update/delete buttons)
     */
    public StudentAdapter(Context context, boolean isEditable) {
        this.context = context;
        this.isEditable = isEditable;
    }

    /**
     * Updates the student list and refreshes the RecyclerView.
     *
     * @param students The JSONArray containing the student data
     */
    public void setStudents(JSONArray students) {
        this.students = students;
        notifyDataSetChanged();  // Notify the adapter to refresh the data
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each student item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_edit_student, null);
        return new StudentViewHolder(view);
    }

    /**
     * Binds the data for a specific student to the view holder and handles
     * the display of student information such as name, email, phone, and university.
     * It also sets up functionality for updating and deleting students, depending
     * on the editability of the list.
     *
     * @param holder   The view holder to bind the student data to.
     * @param position The position of the student in the RecyclerView list.
     */
    @Override
    public void onBindViewHolder(StudentViewHolder holder, int position) {
        try {
            // Get the student object at the current position
            JSONObject student = students.getJSONObject(position);

            // Extract student details, handling null cases with default values
            String name = student.optString("name", "Unknown");
            String email = student.optString("email", "N/A");
            String phone = student.optString("phone", "N/A");
            String university = student.optString("university", "Unknown university");

            // Set student details to the corresponding TextViews
            holder.nameTextView.setText(name);
            holder.emailTextView.setText(email);
            holder.phoneTextView.setText(phone);
            holder.universityTextView.setText(university);

            // If the list is not editable, hide the update and delete buttons
            if (!isEditable) {
                holder.updateButton.setVisibility(View.GONE);
                holder.deleteButton.setVisibility(View.GONE);
            } else {
                // Setup update button click listener
                holder.updateButton.setOnClickListener(v -> {
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        try {
                            JSONObject currentStudent = students.getJSONObject(currentPosition);
                            showEditBottomSheet(currentStudent, currentPosition);  // Show edit dialog
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                // Setup delete button click listener
                holder.deleteButton.setOnClickListener(v -> {
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        try {
                            JSONObject currentStudent = students.getJSONObject(currentPosition);
                            new AlertDialog.Builder(context)
                                    .setTitle("Delete Student")
                                    .setMessage("Are you sure you want to delete this student?")
                                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                        try {
                                            long studentId = currentStudent.getLong("userId");
                                            StudentApi.deleteStudent(context, studentId,
                                                    response -> {
                                                        // Remove deleted student and refresh RecyclerView
                                                        students.remove(currentPosition);
                                                        notifyItemRemoved(currentPosition);
                                                        notifyItemRangeChanged(currentPosition, students.length());
                                                        Toast.makeText(context, "Student deleted successfully", Toast.LENGTH_SHORT).show();
                                                    },
                                                    error -> Toast.makeText(context, "Error deleting student: " + error.getMessage(), Toast.LENGTH_SHORT).show()
                                            );
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(context, "Error deleting student", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null)
                                    .show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error retrieving student data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error parsing student data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return students.length();  // Return the number of students in the list
    }

    /**
     * Displays a BottomSheetDialog for editing student details.
     *
     * @param student The JSONObject containing the student details
     * @param position The position of the student in the list
     */
    private void showEditBottomSheet(JSONObject student, int position) {
        try {
            // Inflate the bottom sheet layout
            View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_edit_student, null);
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(view);

            // Initialize the fields from the layout
            TextView editName = view.findViewById(R.id.editName);
            TextView editEmail = view.findViewById(R.id.editEmail);
            TextView editPhone = view.findViewById(R.id.editPhone);
            TextView editUniversity = view.findViewById(R.id.editUniversity);
            View saveChangesButton = view.findViewById(R.id.saveChangesButton);

            // Pre-fill the fields with the current student data
            editName.setText(student.optString("name"));
            editEmail.setText(student.optString("email"));
            editPhone.setText(student.optString("phone"));
            editUniversity.setText(student.optString("university"));

            // Save changes when button is clicked
            saveChangesButton.setOnClickListener(v -> {
                // Get updated values from the fields
                String updatedName = editName.getText().toString().trim();
                String updatedEmail = editEmail.getText().toString().trim();
                String updatedPhone = editPhone.getText().toString().trim();
                String updatedUniversity = editUniversity.getText().toString().trim();

                try {
                    // Retrieve userId from the student object
                    long studentId = student.getLong("userId");

                    // Construct a new JSONObject with updated data
                    JSONObject updatedStudent = new JSONObject();
                    updatedStudent.put("userId", studentId);
                    updatedStudent.put("name", updatedName);
                    updatedStudent.put("email", updatedEmail);
                    updatedStudent.put("phone", updatedPhone);
                    updatedStudent.put("university", updatedUniversity);
                    updatedStudent.put("role", "STUDENT");

                    // Update student via StudentApi
                    StudentApi.updateStudent(context, updatedStudent,
                            response -> {
                                try {
                                    // Update the student in the list and refresh RecyclerView
                                    students.put(position, updatedStudent);
                                    notifyItemChanged(position);
                                    bottomSheetDialog.dismiss();
                                    Toast.makeText(context, "Student updated successfully!", Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "Error updating student list", Toast.LENGTH_SHORT).show();
                                }
                            },
                            error -> Toast.makeText(context, "Error updating student", Toast.LENGTH_SHORT).show());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error constructing update request", Toast.LENGTH_SHORT).show();
                }
            });

            bottomSheetDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error opening edit form", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ViewHolder class to hold views for each student item.
     */
    class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, emailTextView, phoneTextView, universityTextView;
        ImageButton updateButton, deleteButton;

        public StudentViewHolder(View itemView) {
            super(itemView);
            // Initialize views from the layout
            nameTextView = itemView.findViewById(R.id.nameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            universityTextView = itemView.findViewById(R.id.universityTextView);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}

