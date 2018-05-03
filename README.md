## Fuzzy Logic Engine [![Build Status](https://travis-ci.org/ch-x01/fuzzy.svg?branch=master)](https://travis-ci.org/ch-x01/fuzzy)
A simple and lightweight fuzzy logic engine written in Java 8 and published under the MIT licence, notably without utilising any third party libraries. Currently, the binary size of the JAR file is **less than 40 KB**.

The engine provides a convenient fluent API that lets you model the reference system in a easy way.

### Features
**Controller Type** Mamdani

**Membership Functions** Triangle, Trapezoid

**Reasoning Scheme** Max-Min Composition is used

**Defuzzifier** Center of Mass

### Example
```java
    /**
     * A simple reference system is given, which models the brake behaviour of a car driver
     * depending on the car speed. The inference machine should determine the brake force for a
     * given car speed. The speed is specified by the two linguistic terms 'low' and 'medium', and
     * the brake force by 'moderate' and 'strong'.
     */
    @Test
    public void testCar() {

        FuzzyModel model = model().name("car")
                                  .vars(lv().usage("input")
                                            .name("carSpeed")
                                            .terms(triangle().name("low")
                                                             .start(20)
                                                             .top(60)
                                                             .end(100),
                                                   triangle().name("medium")
                                                             .start(60)
                                                             .top(100)
                                                             .end(140)),
                                        lv().usage("output")
                                            .name("brakeForce")
                                            .terms(triangle().name("moderate")
                                                             .start(40)
                                                             .top(60)
                                                             .end(80),
                                                   triangle().name("strong")
                                                             .start(70)
                                                             .top(85)
                                                             .end(100)))
                                  .rules("if carSpeed is low then brakeForce is moderate",
                                         "if carSpeed is medium then brakeForce is strong");

        FuzzyEngine engine = new FuzzyEngine(model);

        OutputVariable output = engine.evaluate(new InputVariable("carSpeed", 70));

        // test output value
        assertEquals(65.9939, output.getValue(), 0.01);

        System.out.println(output);

    }

```
If you would like to compute output values for a range of input values then do the following

```java
        for (int i = 0; i < 50; ++i) {
            double speed = 20 + i * (120.0 / 50);
            InputVariable input = new InputVariable("carSpeed", speed);
            OutputVariable output = engine.evaluate(input);
            System.out.println(engine.printResult(input, output, 6, 2));
        }

    }
```


### Build
To build the project with Maven from the command line go to the directory `fuzzy` and run 
```bash
mvn clean install
```
