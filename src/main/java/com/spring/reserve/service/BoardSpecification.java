package com.spring.reserve.service;

import com.spring.reserve.entity.BoardEntity;
import com.spring.reserve.entity.SearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class BoardSpecification implements Specification<BoardEntity> {

    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate
            (Root<BoardEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if(criteria.getSearchKey() != null){
            if (root.get(criteria.getSearchKey()).getJavaType() == String.class) {
                   return builder.like(
                   root.<String>get(criteria.getSearchKey()), "%" + criteria.getSearchValue() + "%");
               } else {
                  return builder.equal(root.get(criteria.getSearchKey()), criteria.getSearchValue());
            }
        }
        return null;
    }

}
