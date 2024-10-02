package com.example.hiveeapp.model;

public class employerProfile {
        private String companyName;
        private String companyLogoUrl;
        private String companyDescription;
        private String industry;
        private String location;
        private String website;
        private String recruiterName;
        private String contactEmail;
        private String phoneNumber;
        private String[] socialMediaLinks;

        // Constructor, getters, and setters
        public employerProfile(String companyName, String companyLogoUrl, String companyDescription, String industry,
                               String location, String website, String recruiterName, String contactEmail,
                               String phoneNumber, String[] socialMediaLinks) {
            this.companyName = companyName;
            this.companyLogoUrl = companyLogoUrl;
            this.companyDescription = companyDescription;
            this.industry = industry;
            this.location = location;
            this.website = website;
            this.recruiterName = recruiterName;
            this.contactEmail = contactEmail;
            this.phoneNumber = phoneNumber;
            this.socialMediaLinks = socialMediaLinks;
        }
        // Getters and Setters
        // ...
    }

}
