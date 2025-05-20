```bnf
content          ::= {line}
line             ::= (config | polynomial | comment) "\n"

config           ::= variables_config | ordering_config | field_config
variables_config ::= "@variables(" variables ")"
variables        ::= symbol { "," symbol }
symbol           ::= alphanumeric
ordering_config  ::= "@ordering(" ordering ")"
ordering         ::= "lex" | "grlex" | "grevlex"
field_config     ::= "@field(" field ")"
field            ::= "R" | "Q" | "C" | galois_field
galois_field     ::= "GF[" integer "]"


polynomial       ::= monomial { "+" monomial }
monomial         ::= [coefficient] factor | coefficient
factor           ::= symbol ["^" integer] {factor}

coefficient      ::= real | rational | integer | complex
real             ::= ["-"] integer ["." integer]
rational         ::= integer "/" integer
complex          ::= real | [real] "i" | real ("+" | "-") real "i"

comment          ::= "#" {any character except "\n"}

alphanumeric     ::= (alpha | digit) {alpha | digit}
alpha            ::= "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" | "j" | "k" | "l" | "m"
                         | "n" | "o" | "p" | "q" | "r" | "s" | "t" | "u" | "v" | "w" | "x" | "y"
                         | "z" | "A" | "B" | "C" | "D" | "E" | "F" | "G" | "H" | "I" | "J" | "K"
                         | "L" | "M" | "N" | "O" | "P" | "Q" | "R" | "S" | "T" | "U" | "V" | "W"
                         | "X" | "Y" | "Z" | "_"
integer          ::= digit {digit}
digit            ::= "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
```

### Example

```
@variables(x, y, z, t)
@field(Q)
@ordering(lex)

2x^2 + y^2 + 2z^2 + 2t^2 + u^2 - u,
xy + 2yz + 2zt + 2tu - t,
2xz + 2yt + t^2 + 2zu - z,
2xt + 2zt + 2yu - y,
2x + 2y + 2z + 2t + u - 1
```
