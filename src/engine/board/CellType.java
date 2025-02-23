package engine.board;
// Represents the different types of cells on the game board.

public enum CellType {
	NORMAL, // A standard track cell where marbles move during gameplay.
	SAFE, // A protected zone where marbles are immune from attacks.
	BASE, // The starting position where marbles first enter the track.
	ENTRY // A special cell located just before the Safe Zone.
}
