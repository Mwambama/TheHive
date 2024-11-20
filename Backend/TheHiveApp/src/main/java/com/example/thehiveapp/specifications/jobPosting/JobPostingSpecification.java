package com.example.thehiveapp.specifications.jobPosting;

import com.example.thehiveapp.dto.jobPosting.JobPostingSearchDto;
import com.example.thehiveapp.entity.jobPosting.JobPosting;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JobPostingSpecification {

    public static Specification<JobPosting> matchesOptionalFields(JobPostingSearchDto searchDto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Keyword search across multiple fields
            addKeywordPredicate(criteriaBuilder, predicates, root, searchDto.getQ());

            // Range filters for salary and job start date
            addRangePredicate(criteriaBuilder, predicates, root.get("salary"), searchDto.getMinSalary(), searchDto.getMaxSalary());
            addRangePredicate(criteriaBuilder, predicates, root.get("jobStart"), searchDto.getMinJobStart(), searchDto.getMaxJobStart());

            // Check if the application is open
            if (Boolean.TRUE.equals(searchDto.getIsApplicationOpen())) {
                LocalDate today = LocalDate.now();
                predicates.add(criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(root.get("applicationStart"), today),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("applicationEnd"), today)
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addKeywordPredicate(
            jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
            List<Predicate> predicates,
            jakarta.persistence.criteria.Root<JobPosting> root,
            String q) {
        if (q != null && !q.isEmpty()) {
            String[] words = q.toLowerCase().split("\\s+"); // Split on whitespace
            for (String word : words) {
                String wordPattern = "%" + word + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), wordPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), wordPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("summary")), wordPattern)
                ));
            }
        }
    }

    private static <T extends Comparable<? super T>> void addRangePredicate(
            jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
            List<Predicate> predicates,
            jakarta.persistence.criteria.Path<T> field,
            T minValue,
            T maxValue) {
        if (minValue != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(field, minValue));
        }
        if (maxValue != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(field, maxValue));
        }
    }
}
