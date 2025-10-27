# CSINTSY MCO1 Sokobot

**Sokobot** is an artificial intelligence project that automatically solves **Sokoban puzzles** using advanced heuristic-based search algorithms. Built in **Java**, it features efficient state representation, heuristic evaluation functions, and pruning strategies to optimize search performance in the complex state space of Sokoban.

---

## Table of Contents

- [Overview](#overview)
- [Project Structure](#project-structure)
- [Core Components](#core-components)
  - [Game Representation](#game-representation)
  - [Search Algorithms](#search-algorithms)
  - [Heuristic Functions](#heuristic-functions)
  - [Pruning Strategies](#pruning-strategies)
- [How It Works](#how-it-works)
- [Example Usage](#example-usage)

---

## Overview

Sokobot is designed to **automatically solve Sokoban puzzles** using intelligent search algorithms such as:

- **A\*** (A-star) Search
- **Greedy Best-First Search (GBFS)**

These algorithms explore possible game states, guided by **heuristic functions** that estimate the distance to the goal, allowing Sokobot to find efficient solutions while pruning dead-end states.

The project is implemented in **Java** and organized into several modular packages:

- `solver.main` – Game representation and logic
- `solver.searchAlgorithms` – AI search strategies
- `solver.utils` – Heuristics and pruning utilities
- `solver` – Main solver interface

---

## Project Structure

```
src/
├── solver/
│ ├── SokoBot.java
│ ├── main/
│ │ ├── GameState.java
│ │ ├── LevelData.java
│ │ └── Point.java
│ ├── searchAlgorithms/
│ │ ├── AstarAlgorithm.java
│ │ └── GreedyBestFirstSearch.java
│ └── utils/
│ ├── Heuristics.java
│ └── Prune.java
```

---

## Core Components

### Game Representation

#### `GameState.java`

**Defines the current state of the game:**

- Player position (`Point`)
- Box positions (`HashSet<Point>`)
- Level data reference (`LevelData`)
- Cached heuristic values for fast lookup
- Functions to generate valid moves and compute successor states

**Heuristics are precomputed using:**

```java
Heuristics.hungarianHeuristic(level, boxPositions, level.getTargets());
```

#### `LevelData.java`

**Handles map parsing and initialization:**

- Loads walls, floors, targets, boxes, and player position

- Identifies deadlock cells using reverse flood fill (Prune.reverseFloodFill)

- Generates the origin state for the solver

#### `Point.java`

**Lightweight coordinate representation supporting:**

- Directional movement (u, d, l, r)

- Branchless lookup tables for performance

- Precomputed hash codes for fast comparisons

### Search Algorithms

#### `AstarAlgorithm.java`

Implements the A\* search algorithm combining:

- g(n) = cost so far (moves made)

- h(n) = heuristic estimate of remaining distance

```java
f(n) = g(n) + h(n)
```

Uses a priority queue to always expand the most promising nodes while avoiding revisiting higher-cost paths.

#### `GreedyBestFirstSearch.java`

Implements Greedy Best-First Search (GBFS):

- Expands nodes based solely on heuristic value (h(n))

- Faster but less optimal than A\*

- Used as the default algorithm for Sokobot

### Heuristic Functions

#### `Heuristics.java`

**Contains various distance-based heuristics:**

Minimum Manhattan Distance:

- Finds the shortest Manhattan distance between each box and any target.

Total Manhattan Distance:

- Sum of minimum distances for all boxes.

Divide and Conquer Heuristic:

- Groups boxes and targets to reduce computational complexity.

Hungarian Algorithm Heuristic (Default):

- Solves the assignment problem optimally between boxes and targets, ensuring the lowest total distance.

**Example:**

```java
Heuristics.hungarianHeuristic(level, boxes, level.getTargets());
```

### Pruning Strategies

#### `Prune.java`

Detects unwinnable states early to reduce search space:

- Corner Deadlocks: Boxes stuck in non-target corners

- Tunnel Deadlocks: Boxes blocked in narrow corridors

- Frozen Boxes: Boxes immobilized by walls or other boxes

- Reverse Flood Fill: Precomputes unreachable cells before solving

**Example:**

```
if (Prune.isFrozen(level, boxPositions, box)) return false;
```

---

## How It Works

1. Load Level Data

   - LevelData parses the map and item arrays to identify all walls, targets, boxes, and player position.

2. Initialize Origin State

   - A GameState object represents the initial puzzle configuration.

3. Run Search Algorithm

   - Sokobot runs either AstarSearch() or GBFS() to explore valid moves.

4. Apply Heuristics and Pruning

   - At each expansion, Sokobot computes the heuristic and prunes deadlocks.

5. Construct Solution Path
   - When all boxes reach targets, Sokobot reconstructs the move sequence.

---

## Example Usage

Example Input: Map

```java
char[][] mapData = {
  {'#', '#', '#', '#', '#'},
  {'#', '.', ' ', ' ', '#'},
  {'#', ' ', '$', '@', '#'},
  {'#', '#', '#', '#', '#'}
};

char[][] itemsData = {
  {'#', '#', '#', '#', '#'},
  {'#', '.', ' ', ' ', '#'},
  {'#', ' ', '$', '@', '#'},
  {'#', '#', '#', '#', '#'}
};

SokoBot bot = new SokoBot();
String solution = bot.solveSokobanPuzzle(5, 4, mapData, itemsData);
System.out.println("Solution moves: " + solution);
```

Example Output: Moves

```
Solution moves: rruull
```
