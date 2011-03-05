This repository contains messy code for practicing the Mikado Method,
refactorings and restructurings, clean code and object-oriented design.

RUNNING THE CODE
================
The project has a `Space` class that is the main class. The `main()` method takes no parameters.

The Space class has two static boolean members that control behavior, `IS_BOUNCING_BALLS` and
`IS_BREAKOUT`.

If `IS_BOUNCING_BALLS` is `false`, the application is a simulation of a solar system,
not that different from ours.
The value of `IS_BREAKOUT` doesn't matter.

If `IS_BOUNCING_BALLS` is `true`, the application shows a box of bouncing balls.
If `IS_BREAKOUT` is `true`, the balls can exit through the lower side of the box.
If `IS_BREAKOUT` is `false`, the balls just keep bouncing in the box.

There are also some tests available. They don't cover all the code, just as in real life. ;-)

EXERCISES
=========
Each of the exercises can start from a fresh pull of the code. They can also start from
the previous (working) state as the exercises are completed.

Easy level
----------
##### Background
The `PhysicalObject` is to be used in another project. However, the entire `Space` class must NOT be shared.
Your task is to extract the `PhysicalObject` to a new project.
##### Goal
Enable reuse of `PhysicalObject`


Intermediate level
------------------
##### Background
The solar system and bouncing balls applications are to be sold in separate delivery packages.
Due to legal issues, the solar system may not contain any bouncing balls logic and vice versa.
##### Goal
Two separate, minimal, deliverables for bouncing balls and solar system.


Difficult level
---------------
##### Background
The application is a huge success, and will be ported to a limited
device without Swing/AWT support. The exact API of the new graphics support is
not ready, but to be first on the market when it arrives, you need to start
separating presentation logic from domain ASAP.
##### Goal
The domain logic is compilable without Swing/AWT dependencies. When Swing/AWT +
any bridging code is available, the application should be runnable.
