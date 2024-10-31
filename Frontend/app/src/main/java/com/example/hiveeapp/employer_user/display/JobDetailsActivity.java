//package com.example.hiveeapp.employer_user.display;
//
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.fragment.app.DialogFragment;
//import com.example.hiveeapp.R;
////import com.example.hiveeapp.employer_user.EditJobActivity;
////import com.example.hiveeapp.employer_user.PostedJobs;
//
//public class JobDetailsActivity extends DialogFragment {
//
//    private static final String ARG_JOB_DETAILS = "jobDetails";
//    private static final int EDIT_JOB_REQUEST_CODE = 2;
//    private PostedJobs jobDetails;
//
//    public static JobDetailsActivity newInstance(PostedJobs jobDetails) {
//        JobDetailsActivity fragment = new JobDetailsActivity();
//        Bundle args = new Bundle();
//        args.putSerializable(ARG_JOB_DETAILS, jobDetails);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            jobDetails = (PostedJobs) getArguments().getSerializable(ARG_JOB_DETAILS);
//        }
//    }
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
//
//        LayoutInflater inflater = requireActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.job_details, null);
//
//        TextView jobDetailsTextView = view.findViewById(R.id.job_details_text_view);
//        Button updateButton = view.findViewById(R.id.update_button);
//        Button deleteButton = view.findViewById(R.id.delete_button);
//
//        if (jobDetails != null) {
//            String jobDetailsText = "Job Title: " + jobDetails.getJobTitle() +
//                    "\nDescription: " + jobDetails.getJobDescription() +
//                    "\nType: " + jobDetails.getJobType() +
//                    "\nSalary: " + jobDetails.getSalaryRequirements() +
//                    "\nAge Requirement: " + jobDetails.getAgeRequirement() +
//                    "\nMinimum GPA: " + jobDetails.getMinimumGpa();
//            jobDetailsTextView.setText(jobDetailsText);
//        }
//
//        updateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), EditJobActivity.class);
//                intent.putExtra("jobDetails", jobDetails);
//                startActivityForResult(intent, EDIT_JOB_REQUEST_CODE);
//            }
//        });
//
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle delete logic here
//                // Example: remove job from the list and notify the adapter
//                // CreateJobsActivity.deleteJob(jobDetails);
//                dismiss();
//            }
//        });
//
//        builder.setView(view)
//                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return builder.create();
//    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == EDIT_JOB_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
//            if (data != null && data.hasExtra("updatedJob")) {
//                jobDetails = (PostedJobs) data.getSerializableExtra("updatedJob");
//                // Update the job details view
//                String jobDetailsText = "Job Title: " + jobDetails.getJobTitle() +
//                        "\nDescription: " + jobDetails.getJobDescription() +
//                        "\nType: " + jobDetails.getJobType() +
//                        "\nSalary: " + jobDetails.getSalaryRequirements() +
//                        "\nAge Requirement: " + jobDetails.getAgeRequirement() +
//                        "\nMinimum GPA: " + jobDetails.getMinimumGpa();
//                ((TextView) getView().findViewById(R.id.job_details_text_view)).setText(jobDetailsText);
//            }
//        }
//    }
//}
