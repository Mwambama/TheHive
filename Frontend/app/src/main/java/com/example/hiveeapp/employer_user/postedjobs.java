package com.example.hiveeapp.employer_user;

    import java.io.Serializable;

    public class postedjobs implements Serializable {
        private String jobTitle;
        private String jobDescription;
        private String jobType;
        private String salaryRequirements;
        private String ageRequirement;
        private String minimumGpa;

        // Constructor
        public postedjobs(String jobTitle, String jobDescription, String jobType, String salaryRequirements, String ageRequirement, String minimumGpa) {
            this.jobTitle = jobTitle;
            this.jobDescription = jobDescription;
            this.jobType = jobType;
            this.salaryRequirements = salaryRequirements;
            this.ageRequirement = ageRequirement;
            this.minimumGpa = minimumGpa;
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

        public String getMinimumGpa() {
            return minimumGpa;
        }
    }
