package io.github.olajed.jgb.dsl.tokens;

import java.util.Map;

/**
 * Enumeration of token types recognized by the lexer.
 *
 * <p>Includes basic operators, numeric and variable tokens, configuration keywords,
 * and delimiters used in the polynomial DSL.</p>
 *
 * <p>Provides static mappings from characters and keywords to corresponding token types
 * to facilitate token recognition during lexical analysis.</p>
 */
public enum TokenType {
    /**
     * Real number field identifier (ℝ).
     */
    R,

    /**
     * Complex number field identifier (ℂ).
     */
    C,

    /**
     * Rational number field identifier (ℚ).
     */
    Q,

    /**
     * Galois Field identifier (GF).
     */
    GF,

    /**
     * Minus operator ('-').
     */
    MINUS,

    /**
     * Plus operator ('+').
     */
    PLUS,

    /**
     * Multiplication operator ('*').
     */
    TIMES,

    /**
     * Division operator ('/').
     */
    DIVIDE,

    /**
     * Exponentiation operator ('^').
     */
    EXPONENT,

    /**
     * Numeric literal (e.g., 42, 3.14).
     */
    NUMBER,

    /**
     * Indeterminate variable (e.g., x, y, z).
     */
    INDETERMINATE,

    /**
     * Left parenthesis ('(').
     */
    LEFT_PARENTHESIS,

    /**
     * Right parenthesis (')').
     */
    RIGHT_PARENTHESIS,

    /**
     * Left square bracket ('[').
     */
    LEFT_SQUARE_BRACKET,

    /**
     * Right square bracket (']').
     */
    RIGHT_SQUARE_BRACKET,

    /**
     * Line break or newline character.
     */
    LINE_BREAK,

    /**
     * At symbol ('@'), used for annotations or directives.
     */
    AT,

    /**
     * Hash symbol ('#'), often used for comments or special directives.
     */
    HASH,

    /**
     * Imaginary unit symbol ('i').
     */
    I,

    /**
     * Variables definition directive token.
     */
    VARIABLES_DEFINITION,

    /**
     * Ordering definition directive token.
     */
    ORDERING_DEFINITION,

    /**
     * Field definition directive token.
     */
    FIELD_DEFINITION,

    /**
     * Dense monomial type specifier.
     */
    DENSE,

    /**
     * Sparse monomial type specifier.
     */
    SPARSE,

    /**
     * Lexicographic monomial ordering specifier.
     */
    LEX,

    /**
     * Graded lexicographic monomial ordering specifier.
     */
    GRLEX,

    /**
     * Graded reverse lexicographic monomial ordering specifier.
     */
    GREVLEX,

    /**
     * Comma separator (',').
     */
    COMMA,

    /**
     * End of file/input token.
     */
    EOF;

    /**
     * Maps single characters to their corresponding {@link TokenType}.
     */
    public static final Map<Character, TokenType> tokenMappings = Map.ofEntries(
            Map.entry('+', PLUS),
            Map.entry('-', MINUS),
            Map.entry('/', DIVIDE),
            Map.entry('*', TIMES),
            Map.entry('^', EXPONENT),
            Map.entry('(', LEFT_PARENTHESIS),
            Map.entry(')', RIGHT_PARENTHESIS),
            Map.entry('[', LEFT_SQUARE_BRACKET),
            Map.entry(']', RIGHT_SQUARE_BRACKET),
            Map.entry('@', AT),
            Map.entry('#', HASH),
            Map.entry('I', I),
            Map.entry('R', R),
            Map.entry('C', C),
            Map.entry('Q', Q),
            Map.entry(',', COMMA)
    );

    /**
     * Maps reserved keyword strings to their corresponding {@link TokenType}.
     */
    public static final Map<String, TokenType> keywords = Map.of(
            "dense", TokenType.DENSE,
            "sparse", TokenType.SPARSE,
            "GF", TokenType.GF,
            "lex", TokenType.LEX,
            "grlex", TokenType.GRLEX,
            "grevlex", TokenType.GREVLEX,
            "variables", TokenType.VARIABLES_DEFINITION,
            "field", TokenType.FIELD_DEFINITION,
            "ordering", TokenType.ORDERING_DEFINITION
    );
}
