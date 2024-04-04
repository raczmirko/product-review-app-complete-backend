package hu.okrim.productreviewappcomplete.specification;

import hu.okrim.productreviewappcomplete.model.Country;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BrandSpecificationBuilder<Brand> {

    private final List<Specification<Brand>> specifications;

    public BrandSpecificationBuilder() {
        this.specifications = new ArrayList<>();
    }

    public BrandSpecificationBuilder<Brand> withName(String name) {
        if (name != null && !name.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("name"), "%" + name + "%"));
        }
        return this;
    }

    public BrandSpecificationBuilder<Brand> withDescription(String description) {
        if (description != null && !description.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("description"), "%" + description + "%"));
        }
        return this;
    }

    public BrandSpecificationBuilder<Brand> withCountry(String countryOfOrigin) {
        if (countryOfOrigin != null && !countryOfOrigin.isEmpty()) {
            specifications.add((root, query, builder) -> {
                Join<Brand, Country> countryJoin = root.join("countryOfOrigin");
                return builder.like(countryJoin.get("name"), "%" + countryOfOrigin + "%");
            });
        }
        return this;
    }

    public Specification<Brand> build() {
        if (specifications.isEmpty()) {
            return null; // No criteria specified
        }
        Specification<Brand> result = specifications.get(0);
        for (int i = 1; i < specifications.size(); i++) {
            result = Specification.where(result).and(specifications.get(i));
        }
        return result;
    }
}
