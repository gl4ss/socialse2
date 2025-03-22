package org.example.socialse2.util;

import java.security.Principal;

/**
 * Utility class for security-related operations
 */
public class SecurityUtil {

    /**
     * Checks if the user is not authenticated
     * 
     * @param principal The Spring Security Principal
     * @return true if user is not authenticated, false otherwise
     */
    public static boolean isNotAuthenticated(Principal principal) {
        return principal == null;
    }
    
    /**
     * Checks if the user is authenticated
     * 
     * @param principal The Spring Security Principal
     * @return true if user is authenticated, false otherwise
     */
    public static boolean isAuthenticated(Principal principal) {
        return principal != null;
    }
}
