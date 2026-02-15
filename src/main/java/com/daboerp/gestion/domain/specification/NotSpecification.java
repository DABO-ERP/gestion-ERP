package com.daboerp.gestion.domain.specification;

import java.util.Objects;

/**
 * NOT operation for specifications.
 */
public class NotSpecification<T> implements Specification<T> {
    
    private final Specification<T> specification;
    
    public NotSpecification(Specification<T> specification) {
        this.specification = Objects.requireNonNull(specification, "Specification cannot be null");
    }
    
    @Override
    public boolean isSatisfiedBy(T candidate) {
        return !specification.isSatisfiedBy(candidate);
    }
}