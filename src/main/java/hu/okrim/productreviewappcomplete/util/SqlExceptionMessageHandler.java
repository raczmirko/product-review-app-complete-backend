package hu.okrim.productreviewappcomplete.util;

import java.sql.SQLException;

public class SqlExceptionMessageHandler {
    public static String brandDeleteErrorMessage (SQLException ex) {
        String errorMessage = ex.getMessage();
        if (errorMessage.contains("fk_review_c")) {
            errorMessage = "";
        }
        return errorMessage;
    }
    public static String countryDeleteErrorMessage (Exception ex) {
        String errorMessage = ex.getMessage();
        if (errorMessage.contains("fk_user_country")) {
            errorMessage = "DELETION FAILED: Country is referenced by at least one user.";
        }
        else if (errorMessage.contains("fk_brand_country")) {
            errorMessage = "DELETION FAILED: Country is referenced by at least one brand.";
        }
        else if (errorMessage.contains("fk_review_head_country")) {
            errorMessage = "DELETION FAILED: Country is referenced by at least one review.";
        }
        return errorMessage;
    }
}
