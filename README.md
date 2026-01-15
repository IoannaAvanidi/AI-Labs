# Artificial Intelligence Projects 2025

This repository contains the laboratory assignments for the **Artificial Intelligence**. The projects focus on search algorithms and game theory, implemented in **Java**.

## 🤖 Exercise 1: Maze Search 
 Implementation of a search program to solve a robot navigation problem in an $N \times N$ grid.

### Key Features
*  **Grid Generation:** An $N \times N$ grid where each cell is free or blocked based on a probability $p$.
*  **Movement:** * Horizontal, vertical, or diagonal moves to adjacent free cells (Cost: 1).
    *  Direct teleportation between the bottom-left (Cell A) and top-right (Cell B) corners (Cost: 2).
* **Algorithms Implemented:**
    *  **Uniform Cost Search (UCS)**.
    *  **A* Search** using an optimal admissible heuristic function $h(n)$.
*  **Reporting:** Comparison of methods based on path cost and number of node expansions.

---

## 🎮 Exercise 2: Game Construction 
 Development of an AI agent (MAX) that plays a custom adversarial game against a human user (MIN) on a $4 \times 3$ grid.

### Game Rules
*  **Symbols:** MAX uses 'X' and MIN uses 'O'.
*  **Winning Condition:** The game ends when a player forms the sequence **'XOX'** (MAX wins) or **'OXO'** (MIN wins).
* **AI Logic:**
    *  Uses the **Minimax algorithm** with recursion.
    *  Does **not** use $\alpha$-$\beta$ pruning or heuristic evaluation functions.
*  **Initial State:** To reduce state space, the game starts with one 'X' and one 'O' already placed in non-adjacent positions.

---

## 📂 Repository Structure
 According to the requirements, each exercise is organized into its own directory :
*  **Source Code:** Original `.c` or `.java` files .
*  **Executable:** Compiled binaries .
*  **Report (PDF):** Documentation of the algorithms, heuristic admissibility, and results.
