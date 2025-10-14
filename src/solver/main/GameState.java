package solver.main;

import java.util.ArrayList;
import java.util.HashSet;
import solver.utils.Heuristics;
import solver.utils.Prune;

public class GameState {

  public static final int MAX_MOVES = 4;
  private final int cachedHashCode;
  private final LevelData level;
  private final Point playerPosition;
  private final HashSet<Point> boxPosition;
  private final int heuristicValue;
  private final char move;

  public GameState(Point playerPosition, HashSet<Point> boxPosition, LevelData level, char move) {
    this(playerPosition, boxPosition, level, move,
        Heuristics.divideAndConquerHeuristic(boxPosition, level.getTargets(), 3));
  }

  public GameState(Point playerPosition, HashSet<Point> boxPosition, LevelData level) {
    this(playerPosition, boxPosition, level, ' ');
  }

  public GameState(Point playerPosition, HashSet<Point> boxPosition, LevelData level, char move, int heuristic) {
    this.playerPosition = playerPosition;
    this.boxPosition = new HashSet<>(boxPosition);
    this.level = level;
    this.move = move;
    this.heuristicValue = heuristic;
    this.cachedHashCode = computeHashCode();
  }

  public Point getPlayerPosition() {
    return playerPosition;
  }

  public HashSet<Point> getBoxPosition() {
    return boxPosition;
  }

  public int getHeuristicValue() {
    return heuristicValue;
  }

  private int computeHashCode() {
    int hash = 31 * playerPosition.hashCode();
    for (Point p : boxPosition)
      hash = 31 * hash + p.hashCode();
    return hash;
  }

  public boolean isValidAction(char dir) {
    Point next = playerPosition.pointAtMove(dir);
    if (level.getWalls().contains(next))
      return false;

    if (boxPosition.contains(next)) {
      Point nextNext = next.pointAtMove(dir);

      // avoid pushing into walls
      if (level.getWalls().contains(nextNext) || boxPosition.contains(nextNext))
        return false;

      // static deadlock check
      if (level.getDeadlocks().contains(nextNext))
        return false;

      // dynamic deadlock check
      // Only check frozen states if a box is actually moved into a new cell
      if (Prune.isFrozen(level, boxPosition, next))
        return false;
    }

    return true;
  }

  public GameState generateState(char dir) {
    if (!isValidAction(dir))
      throw new IllegalArgumentException("Invalid move: " + dir);

    Point nextPlayer = playerPosition.pointAtMove(dir);
    HashSet<Point> newBoxes = boxPosition;
    int newHeuristic = heuristicValue;

    if (boxPosition.contains(nextPlayer)) {
      Point from = nextPlayer;
      Point to = nextPlayer.pointAtMove(dir);
      newBoxes = new HashSet<>(boxPosition);
      newBoxes.remove(from);
      newBoxes.add(to);

      // Incremental heuristic update
      newHeuristic = Heuristics.divideAndConquerHeuristic(newBoxes, level.getTargets(), 3);
    }

    return new GameState(nextPlayer, newBoxes, level, dir, newHeuristic);
  }

  public ArrayList<GameState> getNextStates() {
    ArrayList<GameState> states = new ArrayList<>(MAX_MOVES);
    for (char move : new char[] { 'u', 'd', 'l', 'r' })
      if (isValidAction(move))
        states.add(generateState(move));
    return states;
  }

  public char getMoveMadeFrom(GameState previous) {
    return this.move;
  }

  public LevelData getLevel() {
    return this.level;
  }

  @Override
  public int hashCode() {
    return cachedHashCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof GameState))
      return false;
    GameState g = (GameState) o;
    return playerPosition.equals(g.playerPosition) && boxPosition.equals(g.boxPosition);
  }

  @Override
  public String toString() {
    return "Player=" + playerPosition + ", Boxes=" + boxPosition + ", Move=" + move;
  }
}
