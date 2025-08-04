package org.bloomberg.fx_deals.Helpers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerHelperTest {

    private ControllerHelper controllerHelper;

    @BeforeEach
    void setUp() {
        controllerHelper = new ControllerHelper();
    }

    @Test
    void getUserNotification_AllSuccess() {
        // When
        String result = controllerHelper.getUserNotification(5, 0, 0);

        // Then
        assertEquals("All deals imported successfully.", result);
    }

    @Test
    void getUserNotification_SomeSuccessSomeFailed() {
        // When
        String result = controllerHelper.getUserNotification(3, 2, 0);

        // Then
        assertEquals("Some deals imported successfully, others failed.", result);
    }

    @Test
    void getUserNotification_SomeSuccessSomeDuplicates() {
        // When
        String result = controllerHelper.getUserNotification(3, 0, 2);

        // Then
        assertEquals("Some deals imported, some were skipped due to duplication.", result);
    }

    @Test
    void getUserNotification_AllFailed() {
        // When
        String result = controllerHelper.getUserNotification(0, 5, 0);

        // Then
        assertEquals("All deals failed to import.", result);
    }

    @Test
    void getUserNotification_AllDuplicates() {
        // When
        String result = controllerHelper.getUserNotification(0, 0, 5);

        // Then
        assertEquals("All deals were duplicates and skipped.", result);
    }

    @Test
    void getUserNotification_NoSuccessWithDuplicates() {
        // When
        String result = controllerHelper.getUserNotification(0, 0, 3);

        // Then
        assertEquals("All deals were duplicates and skipped.", result);
    }

    @Test
    void getUserNotification_NoSuccessWithFailures() {
        // When
        String result = controllerHelper.getUserNotification(0, 3, 0);

        // Then
        assertEquals("All deals failed to import.", result);
    }

    @Test
    void getUserNotification_NoSuccessWithMixedIssues() {
        // When
        String result = controllerHelper.getUserNotification(0, 2, 3);

        // Then
        assertEquals("No deals were imported; all were either invalid or duplicates.", result);
    }

    @Test
    void getUserNotification_ComplexScenario() {
        // When
        String result = controllerHelper.getUserNotification(2, 1, 3);

        // Then
        assertEquals("Import completed with mixed results.", result);
    }

    @Test
    void getUserNotification_ZeroCounts() {
        // When
        String result = controllerHelper.getUserNotification(0, 0, 0);

        // Then
        assertEquals("Import completed with mixed results.", result);
    }

    @Test
    void getUserNotification_LargeNumbers() {
        // When
        String result = controllerHelper.getUserNotification(1000, 50, 25);

        // Then
        assertEquals("Import completed with mixed results.", result);
    }
} 