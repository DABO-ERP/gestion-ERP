package com.daboerp.gestion.domain.specification;

import java.util.Objects;

/**
 * AND operation for specifications.
 */
public class AndSpecification<T> implements Specification<T> {
    
    private final Specification<T> left;
    private final Specification<T> right;
    
    public AndSpecification(Specification<T> left, Specification<T> right) {
        this.left = Objects.requireNonNull(left, "Left specification cannot be null");
        this.right = Objects.requireNonNull(right, "Right specification cannot be null");
    }
    
    @Override
    public boolean isSatisfiedBy(T candidate) {
        return left.isSatisfiedBy(candidate) && right.isSatisfiedBy(candidate);
    }
}