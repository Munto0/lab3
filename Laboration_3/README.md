# Laboration 3

## Environments & Tools

## Purpose
The aim of this lab is to implement a text-based version of the **Tower of Hanoi** game, while applying key software 
engineering concepts such as the **Command** Design Pattern, **Singleton** pattern, and custom logging for recording 
move history. The finished product needs to support undo and redo mechanics, as well as offer replay functionality.

### Concrete Goals
- Implement the **``HanoiLogger``** class as a **Lazy Singleton** to log move activities during the game, adhering to\
lazy initialization and utilizing a customized formatter for log contents.
- Develop the **``MoveCommand``** and **``NewGameCommand``** classes as concrete commands implementing the\
**``CommandInterface``**, ensuring proper interaction with the **``HanoiEngine``**.
- Implement the **``CommandManager``** class as an **Eager Singleton** to handle command invocations, manage undo / redo\
mechanics, and maintain the state of executed, undone, and redoable commands.
- Create the **``Replayer``** class for replaying previous games using recorded log files, leveraging the\
**``CommandManager``** for command invocations and handling output silencing.
- Ensuring that the final implementation is in line with the design specifications, passes all provided unit tests, and\
demonstrates proficiency in applying relevant software engineering concepts.

## Procedures

## Discussion