package io.github.olajed.jgb.structures;

import io.github.olajed.jgb.number.Real;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MonomialDivisionIteratorTest {
    @Test
    void testOneMonomialDivisors() {
        var one = new DenseMonomial<>(new int[]{0, 0}, new Real(1));
        var divisionIterator = new MonomialDivisionIterator<>(one);
        List<Monomial<Real>> divisors = new ArrayList<>();
        divisionIterator.forEachRemaining(divisors::add);
        Assertions.assertEquals(0, divisors.size());
    }

    @Test
    void testDivisorsEffectivelyDivideMonomial() {
        var monomial = new DenseMonomial<>(new int[]{3, 3}, new Real(1));
        var divisionIterator = new MonomialDivisionIterator<>(monomial);
        while (divisionIterator.hasNext()) {
            Assertions.assertFalse(divisionIterator.next().disjointWith(monomial));
            Assertions.assertFalse(monomial.divide(divisionIterator.next()).isZero());
        }
    }

    @Test
    void testKnownListOfDivisors() {
        var monomial = new DenseMonomial<>(new int[]{2, 2}, new Real(1));
        var expectedDivisors = Set.of(
                new DenseMonomial<>(new int[]{2, 1}, new Real(1)),
                new DenseMonomial<>(new int[]{2, 0}, new Real(1)),
                new DenseMonomial<>(new int[]{1, 0}, new Real(1)),
                new DenseMonomial<>(new int[]{0, 2}, new Real(1)),
                new DenseMonomial<>(new int[]{0, 1}, new Real(1)),
                new DenseMonomial<>(new int[]{1, 1}, new Real(1)),
                new DenseMonomial<>(new int[]{1, 2}, new Real(1))
        );

        var divisionIterator = new MonomialDivisionIterator<>(monomial);
        List<Monomial<Real>> divisors = new ArrayList<>();
        divisionIterator.forEachRemaining(divisors::add);
        var set = new HashSet<>(divisors);
        Assertions.assertEquals(expectedDivisors.size(), set.size());
        Assertions.assertEquals(expectedDivisors, new HashSet<>(divisors));
    }
}
