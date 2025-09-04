# jgb: Java Gröbner Basis Computation Library

**jgb** is a Java-based symbolic algebra library focused on polynomial computation.
It includes several algorithms for computing Gröbner bases, various monomial orderings,
polynomial ring operations, and polynomial generation tools.

---
## Requirements

- Java 21 or later
- No external dependencies (pure Java)
---

## Installing

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
  <groupId>io.github.ola-jed.jgb</groupId>
  <artifactId>lib</artifactId>
  <version>0.0.3</version>
</dependency>
```
### Gradle
Add the following to your build.gradle dependencies :

````groovy
implementation 'io.github.ola-jed.jgb:lib:0.0.3'
````
Or for the Kotlin DSL (build.gradle.kts):

````kotlin
implementation("io.github.ola-jed.jgb:lib:0.0.3")
````
---
## Features

* Polynomial rings over different fields
  * Galois Fields
  * Rationals
  * Reals
  * Complex numbers
* Generation of benchmark polynomial systems (Katsura, Reimer)
* Multiple Gröbner basis algorithms:
    * Buchberger’s Algorithm (with multiple selection strategies)
    * F4 Algorithm
    * Improved F4 Algorithm
    * M4GB Algorithm
* Basis reduction support
* Ordering change with FGLM
* Monomial orderings:
    * Lexicographic
    * Graded Lexicographic
    * Graded Reverse Lexicographic
    * Elimination Ordering
    * Weighted Ordering
* Dense and Sparse polynomial support
---

## Usage

```java
var ring = new PolynomialRing(GaloisFieldElement.class, new String[]{"x1", "x2", "x3", "x4", "x5"});
var polynomials = KatsuraGenerator.get(4);

// Print input polynomials
for (var poly : polynomials) {
    System.out.println(ring.format(poly));
}

// Compute Gröbner bases using Buchberger’s algorithm with different strategies
var gb = BuchbergerAlgorithm.compute(polynomials, PairSelectionStrategy.DEGREE);

// Reduce the basis
var reduced = GrobnerBasisAlgorithms.reduceGrobnerBasis(gb);

// Use other algorithms
gb = F4Algorithm.compute(polynomials);
gb = ImprovedF4Algorithm.compute(polynomials);
gb = M4GBAlgorithm.compute(polynomials);
```

---

## Selection Strategies

When using Buchberger’s algorithm, select the S-pair processing strategy via:

```java
PairSelectionStrategy.FIRST
PairSelectionStrategy.DEGREE
PairSelectionStrategy.NORMAL
PairSelectionStrategy.SUGAR
```

---

## Monomial Ordering Setup

Monomial orderings can be configured using:

```java
MonomialOrdering<Real> ordering = new LexOrdering<>();
ordering = new GrlexOrdering<>();
ordering = new GrevlexOrdering<>();
ordering = new EliminationOrdering<>(block1, block2, new GrlexOrdering<>());
ordering = new WeightedOrdering<>(weights);
```

Each ordering influences how terms are compared and can significantly affect algorithm performance.

---
