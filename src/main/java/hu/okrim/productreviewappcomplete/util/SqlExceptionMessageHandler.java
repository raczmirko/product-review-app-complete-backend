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
    public static String categoryCreateErrorMessage (Exception ex) {
        String errorMessage = ex.getMessage();
        if (errorMessage.contains("Category hierarchy can not exceed 3 in depth.")) {
            errorMessage = "CREATION FAILED: Category hierarchy can not exceed 3 in depth.";
        }
        if (errorMessage.contains("uq_category_name")) {
            errorMessage = "CREATION FAILED: This category name already exists. Duplicate names are not allowed.";
        }
        return errorMessage;
    }
    public static String categoryDeleteErrorMessage (Exception ex) {
        String errorMessage = ex.getMessage();
        if(errorMessage.contains("fk_category_category")) {
            errorMessage = "DELETION FAILED: This category has subcategories.";
        }
        else if (errorMessage.contains("fk_category_x_characteristic_category")) {
            errorMessage = "DELETION FAILED: This category has characteristics assigned already.";
        }
        else if (errorMessage.contains("fk_article_category")) {
            errorMessage = "DELETION FAILED: This category has articles.";
        }
        else if (errorMessage.contains("fk_aspect_category")) {
            errorMessage = "DELETION FAILED: This category has review aspects associated to it.";
        }
        return errorMessage;
    }

    public static String categoryUpdateErrorMessage (Exception ex){
        String errorMessage = ex.getMessage();
        if (errorMessage.contains("characteristics are already assigned")) {
            errorMessage = "UPDATE FAILED: Changing this category is not allowed because characteristics are already assigned to it or it's subcategories, and updating would re-define the inheritance hierarchy.";
        }
        return errorMessage;
    }

    public static String characteristicDeleteErrorMessage(Exception ex) {
        String errorMessage = ex.getMessage();
        if(errorMessage.contains("fk_category_x_characteristic_characteristic")) {
            errorMessage = "DELETION FAILED: This characteristic is already assigned to at " +
                    "least one category.";
        }
        return errorMessage;
    }

    public static String characteristicCreateErrorMessage(Exception ex) {
        String errorMessage = ex.getMessage();
        if(errorMessage.contains("Modifying characteristics that already describe a category is not allowed.")) {
            errorMessage = "UPDATE FAILED: Modifying characteristics that already describe a category is not allowed.";
        }
        return errorMessage;
    }
}
