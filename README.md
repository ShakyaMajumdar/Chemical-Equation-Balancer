# Chemical Equation Balancer
A command line application which balances chemical equations.

Examples:
```
$ java Balancer.Balancer
>> KClO3 -> KClO4 + KCl
Is balanced: false
4KClO3 -> 3KClO4 + KCl
>> KMnO4 + H2SO4 + H2C2O4 -> K2SO4 + MnSO4 + CO2 + H2O
Is balanced: false
2KMnO4 + 3H2SO4 + 5H2C2O4 -> K2SO4 + 2MnSO4 + 10CO2 + 8H2O

$ java Balancer.Balancer --pretty
>> KClO3 -> KClO4 + KCl
Is balanced: false
4KClO₃ → 3KClO₄ + KCl
>> KMnO4 + H2SO4 + H2C2O4 -> K2SO4 + MnSO4 + CO2 + H2O
Is balanced: false
2KMnO₄ + 3H₂SO₄ + 5H₂C₂O₄ → K₂SO₄ + 2MnSO₄ + 10CO₂ + 8H₂O
```