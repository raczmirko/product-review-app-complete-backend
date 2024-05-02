package hu.okrim.productreviewappcomplete.util;

public class SqlExceptionMessageHandler {
    public static String brandDeleteErrorMessage (Exception ex) {
        String errorMessage = ex.getMessage();
        if (errorMessage.contains("fk_article_brand")) {
            errorMessage = "DELETION FAILED: This brand already has at least one article.";
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
