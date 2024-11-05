# Command Line Interpreter (CLI)

## Project Overview
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

---

### Error Handling:
- If an invalid command or incorrect parameters are entered (e.g., invalid file paths), the CLI will display an error message without terminating.
