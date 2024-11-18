package com.example.thehiveapp.specifications.jobPosting;

import com.example.thehiveapp.dto.jobPosting.JobPostingSearchDto;
import com.example.thehiveapp.entity.jobPosting.JobPosting;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class JobPostingSpecification {

    public static Specification<JobPosting> matchesOptionalFields(JobPostingSearchDto searchDto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Query filter
            addKeywordPredicate(criteriaBuilder, predicates, root, searchDto.getQ());

            // Equality filters
            addEqualPredicate(criteriaBuilder, predicates, root.get("employer").get("id"), searchDto.getEmployerId());

            // Like filters
            addLikePredicate(criteriaBuilder, predicates, root.get("title"), searchDto.getTitle());
            addLikePredicate(criteriaBuilder, predicates, root.get("description"), searchDto.getDescription());
            addLikePredicate(criteriaBuilder, predicates, root.get("summary"), searchDto.getSummary());

            // Range filters
            addRangePredicate(criteriaBuilder, predicates, root.get("salary"), searchDto.getMinSalary(), searchDto.getMaxSalary());
            addRangePredicate(criteriaBuilder, predicates, root.get("minimumGpa"), searchDto.getMinGpa(), searchDto.getMaxGpa());
            addRangePredicate(criteriaBuilder, predicates, root.get("jobStart"), searchDto.getMinJobStart(), searchDto.getMaxJobStart());
            addRangePredicate(criteriaBuilder, predicates, root.get("applicationStart"), searchDto.getMinApplicationStart(), searchDto.getMaxApplicationStart());
            addRangePredicate(criteriaBuilder, predicates, root.get("applicationEnd"), searchDto.getMinApplicationEnd(), searchDto.getMaxApplicationEnd());

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

    private static void addLikePredicate(
            jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
            List<Predicate> predicates,
            jakarta.persistence.criteria.Path<String> field,
            String value) {
        if (value != null && !value.isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(field), "%" + value.toLowerCase() + "%"));
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

    private static void addEqualPredicate(
            jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
            List<Predicate> predicates,
            jakarta.persistence.criteria.Path<Long> field,
            Long id) {
        if (id != null) {
            predicates.add(criteriaBuilder.equal(field, id));
        }
    }

}

