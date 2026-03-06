package com.daboerp.gestion.api.dto;

import java.util.List;

/**
 * Generic DTO for paginated responses.
 */
public record PaginatedResponse<T>(
    List<T> content,
    int currentPage,
    int pageSize,
    long totalPages,
    long totalElements,
    boolean first,
    boolean last
) {
    
    public static <T> PaginatedResponse<T> of(List<T> content, int currentPage, int pageSize, 
                                              long totalPages, long totalElements) {
        return new PaginatedResponse<>(
            content,
            currentPage,
            pageSize,
            totalPages,
            totalElements,
            currentPage == 0,
            currentPage >= totalPages - 1
        );
    }
}