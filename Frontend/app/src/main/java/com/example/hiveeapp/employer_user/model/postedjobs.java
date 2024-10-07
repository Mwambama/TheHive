package com.example.hiveeapp.employer_user.model;


// this class has all posted jobs variables defined
public class postedjobs {
        private String jobTitle;
        private String jobDescription;
        private String jobType;
        private String salaryRequirements;
        private String ageRequirement;
        private String minimumGPA;

        // Constructor
        public postedjobs(String jobTitle, String jobDescription, String jobType, String salaryRequirements, String ageRequirement, String minimumGPA) {
            this.jobTitle = jobTitle;
            this.jobDescription = jobDescription;
            this.jobType = jobType;
            this.salaryRequirements = salaryRequirements;
            this.ageRequirement = ageRequirement;
            this.minimumGPA = minimumGPA;
        }

        // Getters
        public String getJobTitle() {
            return jobTitle;
        }

        public String getJobDescription() {
            return jobDescription;
        }

        public String getJobType() {
            return jobType;
        }

        public String getSalaryRequirements() {
            return salaryRequirements;
        }

        public String getAgeRequirement() {
            return ageRequirement;
        }

        public String getMinimumGPA() {
            return minimumGPA;
        }
    }

