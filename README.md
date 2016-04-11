# Quantum

> Quantum entanglement is a physical phenomenon that occurs when pairs or groups of particles are generated or interact in ways such that the quantum state of each particle cannot be described independently — instead, a quantum state must be described for the system as a whole.

From [Quantum Entanglement, Wikipedia.](https://en.wikipedia.org/wiki/Quantum_entanglement)

## What?
 
 This library is intended as an annotation processor which will allow you to annotate views with an ID, 
 register a listener of some sort for that ID, and have the View respond to changes picked up by the
 listener. The view and the listener would be _quantumly entangled_.  
 
## Why?
 
 The example use case that led to the creation of this library was showing/hiding views 
 based on the value of a Firebase object, in real time, via an annotation. There's no reason it has 
 to be Firebase-driven. Theoretically, any source of data, whether from sensors, network, database,
 could be entangled. 
 
## How?

 It is heavily inspired by Jake Wharton's excellent library, ButterKnife.
 
## When?

 It's something I'll be chipping away at. Don't hold your breath.

 It is at an incredibly early stage of development. At this point, all the `@Entangle` annotation
 does is set the visibility of a view to `View.GONE`. 
 
## Who?

 Contributions, feedback, and suggestions, are very welcome. 
  
## Sample App

 The sample app shows nothing an empty Activity. If you comment out the `Quantum.entangle(this)` line
  in `onCreate`, it will show "Hello World!", because the generated `QuantumEntangler` is never
    being applied to the `TextView`. 