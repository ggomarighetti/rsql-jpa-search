package io.github.ggomarighetti.searchhelper.property;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import io.github.ggomarighetti.searchhelper.definition.SearchDefinition;
import io.github.ggomarighetti.searchhelper.integration.bench.domain.Product;
import io.github.ggomarighetti.searchhelper.internal.RsqlSearchGuard;
import io.github.ggomarighetti.searchhelper.internal.SearchPageableGuard;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

class RsqlSearchGuardFuzzTest {
    private static final SearchDefinition<Product> RSQL_DEFINITION = SearchPropertyFixtures.rsqlDefinition();
    private static final SearchDefinition<Product> PAGEABLE_DEFINITION = SearchPropertyFixtures.pageableDefinition();
    private static final String[] SORT_PROPERTIES = {
            "sku",
            "name",
            "amount",
            "legacyAmount",
            "releaseDate",
            "supplierName",
            "passwordHash",
            "price",
            "supplier.name"
    };

    @FuzzTest(maxDuration = "30s")
    void fuzzRsqlGuard(FuzzedDataProvider data) {
        String input = data.consumeString(4_128);
        try {
            new RsqlSearchGuard().specification(input, RSQL_DEFINITION);
        } catch (Throwable throwable) {
            if (!SearchPropertyFixtures.isExpectedRsqlThrowable(throwable)) {
                throw throwable;
            }
        }
    }

    @FuzzTest(maxDuration = "30s")
    void fuzzPageableGuard(FuzzedDataProvider data) {
        Pageable pageable = pageable(data);
        try {
            new SearchPageableGuard().pageable(pageable, PAGEABLE_DEFINITION);
        } catch (Throwable throwable) {
            if (!SearchPropertyFixtures.isExpectedPageableThrowable(throwable)) {
                throw throwable;
            }
        }
    }

    private static Pageable pageable(FuzzedDataProvider data) {
        Sort sort = sort(data);
        if (data.consumeBoolean()) {
            return Pageable.unpaged(sort);
        }
        int page = data.consumeInt(0, 150);
        int size = data.consumeInt(1, 150);
        return PageRequest.of(page, size, sort);
    }

    private static Sort sort(FuzzedDataProvider data) {
        int orders = data.consumeInt(0, 5);
        if (orders == 0) {
            return Sort.unsorted();
        }
        java.util.List<Sort.Order> result = new java.util.ArrayList<>();
        for (int index = 0; index < orders; index++) {
            Sort.Order order = new Sort.Order(
                    data.consumeBoolean() ? Sort.Direction.ASC : Sort.Direction.DESC,
                    SORT_PROPERTIES[data.consumeInt(0, SORT_PROPERTIES.length - 1)],
                    nullHandling(data));
            if (data.consumeBoolean()) {
                order = order.ignoreCase();
            }
            result.add(order);
        }
        return Sort.by(result);
    }

    private static Sort.NullHandling nullHandling(FuzzedDataProvider data) {
        Sort.NullHandling[] values = Sort.NullHandling.values();
        return values[data.consumeInt(0, values.length - 1)];
    }
}
