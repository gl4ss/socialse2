package org.example.socialse2.util;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

/**
 * Utility class for handling pagination-related operations
 */
public class PaginationUtil {

    /**
     * Adds common pagination attributes to the model
     * 
     * @param model The Spring MVC model
     * @param page The Page object containing paginated data
     * @param currentPage The current page number (1-based)
     */
    public static void addPaginationAttributes(Model model, Page<?> page, int currentPage) {
        model.addAttribute("items", page.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        
        // Add the content with the original attribute name for backward compatibility
        String contentType = determineContentType(page);
        if (contentType != null) {
            model.addAttribute(contentType, page.getContent());
        }
    }
    
    /**
     * Attempts to determine the content type based on the generic type of the Page
     */
    private static String determineContentType(Page<?> page) {
        if (page.isEmpty()) {
            return null;
        }
        
        Object firstItem = page.getContent().get(0);
        String className = firstItem.getClass().getSimpleName().toLowerCase();
        
        // Remove "dto" suffix if present and pluralize
        if (className.endsWith("dto")) {
            className = className.substring(0, className.length() - 3) + "s";
        } else {
            className = className + "s";
        }
        
        return className;
    }
}
