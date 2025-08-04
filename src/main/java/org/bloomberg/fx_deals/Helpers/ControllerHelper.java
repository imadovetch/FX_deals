package org.bloomberg.fx_deals.Helpers;

import org.springframework.stereotype.Component;

@Component
public class ControllerHelper {


    public String getUserNotification(int success, int failed, int duplicates) {
        if (success > 0 && failed == 0 && duplicates == 0) {
            return "All deals imported successfully.";
        } else if (success > 0 && failed > 0 && duplicates == 0) {
            return "Some deals imported successfully, others failed.";
        } else if (success > 0 && duplicates > 0 && failed == 0) {
            return "Some deals imported, some were skipped due to duplication.";
        } else if (success == 0 && failed > 0 && duplicates == 0) {
            return "All deals failed to import.";
        } else if (success == 0 && duplicates > 0 && failed == 0) {
            return "All deals were duplicates and skipped.";
        } else if (success == 0 && (duplicates > 0 || failed > 0)) {
            return "No deals were imported; all were either invalid or duplicates.";
        } else {
            return "Import completed with mixed results.";
        }
    }


}
