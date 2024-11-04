# Operating System Course Assignments

## Overview
This repository contains two assignments as part of our coursework. Each assignment is located in its respective folder:

- **Assignment 1**: Command Line Interpreter (CLI)
- **Assignment 2**: Multithreaded Parking System Simulation

---

## Assignment 1: Command Line Interpreter (CLI)

### Project Overview
This project implements a basic Command Line Interpreter (CLI), similar to a Unix/Linux shell, in Java. The CLI supports various system and internal commands and includes comprehensive unit testing using JUnit.

### Command List & Usage

#### System Commands:
| Command            | Description                                                         |
|--------------------|---------------------------------------------------------------------|
| `pwd`              | Prints the current working directory.                               |
| `cd [dir]`         | Changes to the specified directory.                                 |
| `ls`               | Lists the files in the current directory.                           |
| `ls -a`            | Lists all files, including hidden ones.                             |
| `ls -r`            | Lists files in reverse order.                                       |
| `mkdir [dir]`      | Creates a new directory.                                            |
| `rmdir [dir]`      | Removes an empty directory.                                         |
| `touch [file]`     | Creates a new empty file.                                           |
| `mv [src] [dst]`   | Moves or renames a file or directory.                               |
| `rm [file]`        | Deletes a file.                                                     |
| `cat [file]`       | Displays the content of a file.                                     |
| `> [file]`         | Redirects output to a file (overwrites the file).                   |
| `>> [file]`        | Appends output to a file.                                           |
| `[cmd1] | [cmd2]`  | Pipes the output of `cmd1` as input to `cmd2`.                      |

#### Internal Commands:
| Command        | Description                                                         |
|----------------|---------------------------------------------------------------------|
| `help`         | Displays a list of available commands and their usage.              |
| `exit`         | Exits the CLI.                                                      |

### Error Handling
- The CLI displays an error message for invalid commands or incorrect parameters without terminating.

---

## Assignment 2: Multithreaded Parking System Simulation

### Project Overview
This assignment simulates a parking system using semaphores and threads to manage limited parking spots across multiple gates. Cars arrive at specific times, occupy parking spots for a given duration, and then depart. Thread synchronization and concurrency management are implemented to ensure a realistic simulation.

### Objectives:
- **Thread Synchronization**: Use threads and semaphores to manage access to parking spots.
- **Concurrency Management**: Handle concurrent car arrivals and departures accurately.
- **Simulation Realism**: Simulate car arrivals at specific times with accurate timing.
- **Status Reporting**: Report the number of cars currently parked and the total served over time.

### System Specifications:
- **Parking Spots**: 4 available spots.
- **Gates**: 3 gates (Gate 1, Gate 2, Gate 3).
- **Car Arrival**: Each gate receives cars at different times, specified in an input file.

### Tasks:
- **Setup Parking Lot**: Create a parking lot with 4 spots.
- **Implement Gates**: Simulate car arrivals at three gates using separate threads.
- **Car Threads**: Each car is represented by a thread that attempts to enter the parking lot.
- **Semaphores**: Use semaphores to manage spot availability.
- **Logging and Reporting**: Log car activity and report the current and total parked cars at the simulation’s end.

### Implementation Details:
- **Thread Function**: Each car thread acquires a spot, stays for a set duration, and then releases it.
- **Arrival Times**: Simulate arrival times with `sleep()`.
- **Duration in Parking**: Use `sleep()` to simulate each car's stay duration.
- **Concurrency Control**: Manage parking spots using a semaphore to avoid race conditions.
- **Input Parsing**: Read input data from a `txt` file for car arrivals and durations.

