```bnf
content          ::= {line}
line             ::= (config | polynomial | comment) "\n"

config           ::= variables_config | ordering_config | field_config | monomial_config
variables_config ::= "@variables(" variables ")"
variables        ::= symbol { "," symbol }
symbol           ::= lower_alpha {alpha | digit}
ordering_config  ::= "@ordering(" ordering ")"
ordering         ::= "lex" | "grlex" | "grevlex"
field_config     ::= "@field(" field ")"
field            ::= "R" | "Q" | "C" | galois_field
galois_field     ::= "GF[" integer "]"
monomial_config  ::= "@dense" | "@sparse"

polynomial       ::= monomial { ("+" | "-") monomial}
monomial         ::= [(coefficient "*")] factor | coefficient
factor           ::= symbol ["^" integer] { "*" symbol ["^" integer] }

coefficient      ::= real | rational | integer | complex
real             ::= ["-"] integer ["." integer]
rational         ::= integer "/" integer
complex          ::= real | [real] "I" | real ("+" | "-") real "I"

comment          ::= "#" {any character except "\n"}

lower_alpha      ::= "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" | "j" | "k" | "l" | "m"
                         | "n" | "o" | "p" | "q" | "r" | "s" | "t" | "u" | "v" | "w" | "x" | "y"
                         | "z" | "_"
alpha            ::= "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" | "j" | "k" | "l" | "m"
                         | "n" | "o" | "p" | "q" | "r" | "s" | "t" | "u" | "v" | "w" | "x" | "y"
                         | "z" | "A" | "B" | "C" | "D" | "E" | "F" | "G" | "H" | "I" | "J" | "K"
                         | "L" | "M" | "N" | "O" | "P" | "Q" | "R" | "S" | "T" | "U" | "V" | "W"
                         | "X" | "Y" | "Z" | "_"
integer          ::= digit {digit}
digit            ::= "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
```

### Reserved keywords
- ordering
- field
- variables
- lex
- grlex
- grevlex
- dense
- sparse

### Example

```
@variables(x, y, z)
@field(GF[5])
@ordering(grevlex)
@dense

4 + 2 * z^2 + 3 * y^2 + 2 * x^2
4 + 2 * z^3 + 3 * y^3 + 2 * x^3
4 + 2 * z^4 + 3 * y^4 + 2 * x^4

# For complex numbers, use 'I' to define the imaginary unit
# Uppercases cannot be used as first characters in indeterminates
# It is to avoid confusion with the imaginary unit, eg: I and I (I am confused too)
# 'xI' for example is allowed as a variable name
```
