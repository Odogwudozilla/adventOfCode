REM Polyglot Script: Works as both Batch (Windows) and Bash (Unix/Linux/macOS)
REM This clever trick allows a single file to work on multiple platforms without file extensions
REM
REM On Windows Command Prompt:
REM   - The @echo off & setlocal enabledelayedexpansion & goto :batch_start line is recognized as valid batch
REM   - Execution jumps to :batch_start label, skipping the shell script content
REM   - Runs gradlew.bat build and executes the puzzle
REM
REM On Unix/Linux/macOS:
REM   - The shebang #!/bin/bash makes it a valid shell script
REM   - The batch line is treated as a comment by the shell
REM   - Executes ./gradlew build and runs the puzzle
REM
REM Usage: puzzle 2025 day1
REM        puzzle 2024 day18

@echo off & setlocal enabledelayedexpansion & goto :batch_start
#!/bin/bash
cd "$(dirname "$0")"
./gradlew build -x test -q
java -cp build/classes/java/main odogwudozilla.Main "$@"
exit $?
:batch_start
cd /d "%~dp0"
call gradlew.bat build -x test -q
java -cp build\classes\java\main odogwudozilla.Main %*

