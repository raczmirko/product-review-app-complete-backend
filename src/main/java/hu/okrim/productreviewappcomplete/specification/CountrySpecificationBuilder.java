package hu.okrim.productreviewappcomplete.specification;

import hu.okrim.productreviewappcomplete.model.Country;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CountrySpecificationBuilder<Country> {
    private final List<Specification<Country>> specifications;

    public CountrySpecificationBuilder() {
        this.specifications = new ArrayList<>();
    }

    public CountrySpecificationBuilder<Country> withCountryCode(String countryCode) {
        if (countryCode != null && !countryCode.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("countryCode"), "%" + countryCode + "%"));
        }
        return this;
    }

    public CountrySpecificationBuilder<Country> withName(String name) {
        if (name != null && !name.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("name"), "%" + name + "%"));
        }
        return this;
    }

    public Specification<Country> build() {
        if (specifications.isEmpty()) {
            return null; // No criteria specified
        }
        Specification<Country> result = specifications.get(0);
        for (int i = 1; i < specifications.size(); i++) {
            result = Specification.where(result).and(specifications.get(i));
        }
        return result;
    }
}
