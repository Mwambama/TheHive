package com.example.thehiveapp.service.jobPosting;

import com.example.thehiveapp.enums.status.Status;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import com.example.thehiveapp.dto.jobPosting.JobPostingDto;
import com.example.thehiveapp.dto.jobPosting.JobPostingSearchDto;
import com.example.thehiveapp.entity.application.Application;
import com.example.thehiveapp.entity.jobPosting.JobPosting;
import com.example.thehiveapp.entity.user.Employer;
import com.example.thehiveapp.entity.user.Student;
import com.example.thehiveapp.mapper.jobPosting.JobPostingMapper;
import com.example.thehiveapp.repository.application.ApplicationRepository;
import com.example.thehiveapp.repository.jobPosting.JobPostingRepository;
import com.example.thehiveapp.service.user.StudentService;
import com.example.thehiveapp.service.user.UserService;
import com.example.thehiveapp.specifications.jobPosting.JobPostingSpecification;
import com.example.thehiveapp.service.user.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.thehiveapp.enums.status.Status.*;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class JobPostingServiceImpl implements JobPostingService{

    @Autowired private JobPostingRepository jobPostingRepository;
    @Autowired private JobPostingMapper mapper;
    @Autowired private EmployerService employerService;
    @Autowired private UserService userService;
    @Autowired private StudentService studentService;
    @Autowired private ApplicationRepository applicationRepository;

    public JobPostingServiceImpl() {}

    @Override
    public List<JobPostingDto> getJobPostings() {
        return jobPostingRepository.findAll().stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobPostingDto> getJobPostingsByEmployerId(Long employerId) {
        Employer employer = employerService.getEmployerById(employerId);
        return jobPostingRepository.findByEmployer(employer).stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<JobPostingDto> getJobPostingsForStudent(Long studentId) {
        List<JobPosting> jobPostings = jobPostingRepository.findJobPostings(studentId);
        return jobPostings.stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public JobPostingDto createJobPosting(JobPostingDto dto) {
        JobPosting jobPosting = jobPostingRepository.save(mapper.dtoToEntity(dto));
        dto = mapper.entityToDto(jobPosting);
        return dto;
    }

    @Override
    public JobPostingDto getJobPostingById(Long id) {
        return mapper.entityToDto(
                jobPostingRepository.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException("Job Posting not found with id " + id)
                )
        );
    }

    @Override
    public byte[] getJobPostingAnalytics(Long jobPostingId) {
        JobPosting jobPosting = jobPostingRepository.findById(jobPostingId).orElseThrow(
                () -> new ResourceNotFoundException("Job Posting not found with id " + jobPostingId)
        );
        DefaultPieDataset defaultPieDataset = new DefaultPieDataset();
        int pendingCount = applicationRepository.findApplicationsByJobPostingAndStatus(jobPosting, PENDING).size();
        int acceptedCount = applicationRepository.findApplicationsByJobPostingAndStatus(jobPosting, ACCEPTED).size();
        int rejectedCount = applicationRepository.findApplicationsByJobPostingAndStatus(jobPosting, REJECTED).size();
        double total = pendingCount + acceptedCount + rejectedCount;
        String pendingLabel = String.format("PENDING (%.1f%%)", (pendingCount / total * 100));
        String acceptedLabel = String.format("ACCEPTED (%.1f%%)", (acceptedCount / total * 100));
        String rejectedLabel = String.format("REJECTED (%.1f%%)", (rejectedCount / total * 100));
        defaultPieDataset.setValue(pendingLabel, pendingCount);
        defaultPieDataset.setValue(acceptedLabel, acceptedCount);
        defaultPieDataset.setValue(rejectedLabel, rejectedCount);
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Job Posting: " + jobPosting.getTitle() + " Status Distribution",
                defaultPieDataset,
                true,
                true,
                false
        );
        String subHeading = String.format("Total Applications: %d | Pending: %d | Accepted: %d | Rejected: %d",
                (int) total, pendingCount, acceptedCount, rejectedCount);
        TextTitle subtitle = new TextTitle(subHeading, new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 13));
        pieChart.addSubtitle(subtitle);
        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setSectionPaint(pendingLabel, new java.awt.Color(232, 217, 109)); // Orange
        plot.setSectionPaint(acceptedLabel, new java.awt.Color(69, 176, 69)); // Green
        plot.setSectionPaint(rejectedLabel, new java.awt.Color(219, 43, 22));   // Red
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ChartUtils.writeChartAsPNG(outputStream, pieChart, 640, 480);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate pie chart", e);
        }
    }


    @Override
    public JobPostingDto updateJobPosting(JobPostingDto dto) {
        Long id = dto.getJobPostingId();
        jobPostingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Job Posting not found with id " + id)
        );
        JobPosting jobPosting = mapper.dtoToEntity(dto);
        return mapper.entityToDto(jobPostingRepository.save(jobPosting));
    }

    @Override
    public void deleteJobPosting(Long id) {
        JobPosting jobPosting = jobPostingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Job Posting not found with id " + id)
        );
        jobPostingRepository.delete(jobPosting);
    }

    @Override
    public List<JobPostingDto> searchJobPostings(JobPostingSearchDto searchDto) {
        List<JobPostingDto> results = jobPostingRepository.findAll(
                JobPostingSpecification.matchesOptionalFields(searchDto)
        ).stream().map(mapper::entityToDto).collect(Collectors.toList());

        Student student = studentService.getStudentById(userService.getCurrentUser().getUserId());
        if (Boolean.TRUE.equals(searchDto.getIsQualified()) && student.getGpa() != null) {
            results = results.stream().filter(
                    job -> student.getGpa() >= job.getMinimumGpa()
            ).collect(Collectors.toList());
        }
        if (Boolean.TRUE.equals(searchDto.getHasNotAppliedTo())) {
            List<Long> appliedJobIds =  applicationRepository.findApplicationsByStudent(student).stream().map(
                    application -> application.getJobPosting().getJobPostingId()
            ).toList();
            results = results.stream().filter(
                    job -> !appliedJobIds.contains(job.getJobPostingId())
            ).collect(Collectors.toList());
        }
        return results;
    }

    @Override
    public List<JobPostingDto> getJobPostingSuggestions(Long studentId) {
        // Get the last 10 applications
        Student student = studentService.getStudentById(studentId);
        List<JobPosting> recentJobs = applicationRepository.findTop10ByStudentOrderByAppliedOnDesc(student)
                .stream()
                .map(Application::getJobPosting)
                .collect(Collectors.toList());

        // Get all applications
        List<Long> appliedJobIds = applicationRepository.findApplicationsByStudent(student)
                .stream()
                .map(application -> application.getJobPosting().getJobPostingId())
                .toList();

        // Fetch all job postings
        List<JobPosting> allPostings = jobPostingRepository.findAll();

        // Filter and rank postings based on criteria
        return allPostings.stream()
                .filter(posting -> student.getGpa() == null || // Unqualified
                        posting.getMinimumGpa() <= student.getGpa())
                .filter(posting -> posting.getApplicationEnd() == null || // Application closed
                        posting.getApplicationEnd().isAfter(LocalDate.now()))
                .filter(posting -> !appliedJobIds.contains(posting.getJobPostingId())) // Already applied
                .sorted((p1, p2) -> {
                    // Sort by relevance
                    double score1 = calculateRelevanceScore(student, p1, recentJobs);
                    double score2 = calculateRelevanceScore(student, p2, recentJobs);
                    return Double.compare(score2, score1); // Higher score first
                })
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    private double calculateRelevanceScore(Student student, JobPosting posting, List<JobPosting> recentJobs) {
        // Closer GPAs score higher
        double gpaScore = student.getGpa() != null ? 1.0 / (1 + Math.abs(student.getGpa() - posting.getMinimumGpa())) : 0;

        // Closer dates score higher
        double startDateProximity = 0.0;
        if (student.getGraduationDate() != null) {
            LocalDate gradDate = LocalDate.parse(student.getGraduationDate());
            if (posting.getJobStart() != null) {
                long daysDiff = Math.abs(DAYS.between(gradDate, posting.getJobStart()));
                startDateProximity = 1.0 / (1 + daysDiff);
            }
        }

        // Similar titles to last 10 applications score higher
        double totalSimilarity = 0.0;
        for (JobPosting recent : recentJobs) {
            totalSimilarity += computeSimilarity(posting.getTitle(), recent.getTitle());
        }
        double textSimilarityScore = totalSimilarity / recentJobs.size();

        // Return weighted relevance score
        return gpaScore + 1.2 * startDateProximity + 4 * textSimilarityScore;
    }

    private double computeSimilarity(String text1, String text2) {
        if (text1 == null || text2 == null) return 0.0;
        String[] words1 = text1.toLowerCase().split("\\s+");
        String[] words2 = text2.toLowerCase().split("\\s+");
        long commonWords = Arrays.stream(words1)
                .filter(word -> Arrays.asList(words2).contains(word))
                .count();
        return (double) commonWords / Math.max(words1.length, words2.length); // Similarity ratio
    }
}
