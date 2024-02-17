# Project #1: State Machine

* Author: Josh Miller, Jack Garcia
* Class: CS361 Section #2
* Semester: Spring 2024

## Overview

This is a program that allows you to create simple finite automata with its own transitions and alphabet.

## Reflection

The hardest parts of this project in the DFA file were the toString and the SWAP method. Those two methods took a lot 
of trial and error to start functioning correctly. There was random white spaces being added when we were trying to get 
tests to pass, and it was doing things in the wrong order. We also ran into issues when using a HashMap and HashSet. it
was ordering each element by their HashCode and not in the order it was inserted in.
    
However, we were able to iron out the bugs and get all the tests to pass. This was a very fun and interesting project 
and seeing all the concepts we have been learning so far come to fruition into code was a very cool experience. 
We hope the projects in the future are just as interesting.

## Compiling and Using

To compile DFATest.java, use this command: javac -cp .:/usr/share/java/junit.jar ./test/dfa/DFATest.java
To run test.DFATest use this command: 
java -cp .:/usr/share/java/junit.jar:/usr/share/java/hamcrest/core.jar org.junit.runner.JUnitCore test.dfa.DFATest

## Sources used

Class sources only

----------
This README template is using Markdown. To preview your README output,
you can copy your file contents to a Markdown editor/previewer such
as [https://stackedit.io/editor](https://stackedit.io/editor).