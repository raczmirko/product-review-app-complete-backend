package hu.okrim.productreviewappcomplete.specification;

import hu.okrim.productreviewappcomplete.model.Article;
import hu.okrim.productreviewappcomplete.model.Packaging;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecificationBuilder<Product> {
    private final List<Specification<Product>> specifications;

    public ProductSpecificationBuilder() {
        this.specifications = new ArrayList<>();
    }

    public ProductSpecificationBuilder<Product> withId(String id) {
        try {
            int numericId = Integer.parseInt(id);
            specifications.add((root, query, builder) -> builder.equal(root.get("id"), numericId));
            return this;
        }
        catch (NumberFormatException e) {
            return this;
        }
    }

    public ProductSpecificationBuilder<Product> withArticleName(String name) {
        if (name != null && !name.isEmpty()) {
            specifications.add((root, query, builder) -> {
                Join<Product, Article> articleJoin = root.join("article");
                return builder.like(articleJoin.get("name"), "%" + name + "%");
            });
        }
        return this;
    }

    public ProductSpecificationBuilder<Product> withPackagingName(String name) {
        if (name != null && !name.isEmpty()) {
            specifications.add((root, query, builder) -> {
                Join<Product, Packaging> packagingJoin = root.join("packaging");
                return builder.like(packagingJoin.get("name"), "%" + name + "%");
            });
        }
        return this;
    }

    public Specification<Product> build() {
        if (specifications.isEmpty()) {
            return null; // No criteria specified
        }
        Specification<Product> result = specifications.get(0);
        for (int i = 1; i < specifications.size(); i++) {
            result = Specification.where(result).and(specifications.get(i));
        }
        return result;
    }
}
