package com.daboerp.gestion.domain.specification;

/**
 * Specification pattern interface.
 * Encapsulates business rules that can be combined and reused.
 */
public interface Specification<T> {
    
    /**
     * Check if the candidate satisfies the specification.
     */
    boolean isSatisfiedBy(T candidate);
    
    /**
     * Create a new specification that is the AND operation of this and another.
     */
    default Specification<T> and(Specification<T> other) {
        return new AndSpecification<>(this, other);
    }
    
    /**
     * Create a new specification that is the OR operation of this and another.
     */
    default Specification<T> or(Specification<T> other) {
        return new OrSpecification<>(this, other);
    }
    
    /**
     * Create a new specification that is the NOT operation of this.
     */
    default Specification<T> not() {
        return new NotSpecification<>(this);
    }
}