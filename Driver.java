import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.LinkedList;

//starter code for MazeSolver
//CST-201

public class Driver {

	/**
	 * 
	 * @param start
	 * @param end
	 *              find a path through the maze
	 *              if found, output the path from start to end
	 *              if not path exists, output a message stating so
	 * 
	 */
	// implement this method
	public static void depthFirst(MazeCell start, MazeCell end, MazeCell[][] cells) {
		MyStack<MazeCell> stack = new MyStack<>();
		LinkedList<MazeCell> path = new LinkedList<>();

		stack.push(start);

		while (!stack.isEmpty()) {
			MazeCell current = stack.pop();
			current.visit();

			if (current.equals(end)) {
				path.addFirst(current);
				MazeCell parent = current.getParent();

				while (parent != null) {
					path.addFirst(parent);
					parent = parent.getParent();
				}

				System.out.println("Path found:");
				for (MazeCell cell : path) {
					System.out.print(cell.toString() + " -> ");
				}
				System.out.println("End");
				return;
			}

			// Explore neighbors of the current cell and push them onto the stack if valid.
			int row = current.getRow();
			int col = current.getCol();

			// Check north neighbor
			if (row > 0) {
				MazeCell neighbor = cells[row - 1][col];
				if (neighbor != null && neighbor.unVisited() && neighbor.isPath()) {
					stack.push(neighbor);
					neighbor.setParent(current);
				}
			}

			// Check east neighbor
			if (col < cells[0].length - 1) {
				MazeCell neighbor = cells[row][col + 1];
				if (neighbor != null && neighbor.unVisited() && neighbor.isPath()) {
					stack.push(neighbor);
					neighbor.setParent(current);
				}
			}

			// Check south neighbor
			if (row < cells.length - 1) {
				MazeCell neighbor = cells[row + 1][col];
				if (neighbor != null && neighbor.unVisited() && neighbor.isPath()) {
					stack.push(neighbor);
					neighbor.setParent(current);
				}
			}

			// Check west neighbor
			if (col > 0) {
				MazeCell neighbor = cells[row][col - 1];
				if (neighbor != null && neighbor.unVisited() && neighbor.isPath()) {
					stack.push(neighbor);
					neighbor.setParent(current);
				}
			}
		}
		System.out.println("No path found.");
	}

	public static void main(String[] args) throws FileNotFoundException {

		// create the Maze from the file
		Scanner fin = new Scanner(new File("Maze.in"));
		// read in the rows and cols
		int rows = fin.nextInt();
		int cols = fin.nextInt();

		// create the maze
		int[][] grid = new int[rows][cols];

		// read in the data from the file to populate
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				grid[i][j] = fin.nextInt();
			}
		}

		// look at it
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (grid[i][j] == 3)
					System.out.print("S ");
				else if (grid[i][j] == 4)
					System.out.print("E ");
				else
					System.out.print(grid[i][j] + " ");
			}
			System.out.println();
		}

		// make a 2-d array of cells
		MazeCell[][] cells = new MazeCell[rows][cols];

		// populate with MazeCell obj - default obj for walls

		MazeCell start = new MazeCell(), end = new MazeCell();

		// iterate over the grid, make cells and set coordinates
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				// make a new cell
				cells[i][j] = new MazeCell();
				// if it isn't a wall, set the coordinates
				if (grid[i][j] != 0) {
					cells[i][j].setCoordinates(i, j);
					// look for the start and end cells
					if (grid[i][j] == 3)
						start = cells[i][j];
					if (grid[i][j] == 4)
						end = cells[i][j];

				}

			}
		}

		// testing
		System.out.println("start:" + start + " end:" + end);

		// solve it!
		depthFirst(start, end, cells);

	}
}

// Start of MyStack
class MyStack<T> {
	private LinkedList<T> stack;

	public MyStack() {
		stack = new LinkedList<>();
	}

	public void push(T value) {
		stack.push(value);
	}

	public T pop() {
		if (!isEmpty()) {
			return stack.pop();
		}
		throw new IllegalStateException("Stack is empty.");
	}

	public T peek() {
		if (!isEmpty()) {
			return stack.peek();
		}
		throw new IllegalStateException("Stack is empty.");
	}

	public boolean isEmpty() {
		return stack.isEmpty();
	}

	public int size() {
		return stack.size();
	}
}
// End MyStack

// Start MyDequeue
class MyQueue<T> {
	private LinkedList<T> queue;

	public MyQueue() {
		queue = new LinkedList<>();
	}

	public void push(T value) {
		queue.offer(value);
	}

	public T pop() {
		if (!isEmpty()) {
			return queue.poll();
		}
		throw new IllegalStateException("Queue is empty.");
	}

	public T front() {
		if (!isEmpty()) {
			return queue.peek();
		}
		throw new IllegalStateException("Queue is empty.");
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public int size() {
		return queue.size();
	}
}
// End MyDequeue

/*
 *
 * Provided starter code MazeCell class DO NOT CHANGE THIS CLASS
 *
 * models an open cell in a maze each cell knows its coordinates (row, col),
 * direction keeps track of the next unchecked neighbor\ cell is considered
 * 'visited' once processing moves to a neighboring cell the visited variable is
 * necessary so that a cell is not eligible for visits from the cell just
 * visited
 *
 */

class MazeCell {
	private int row, col;
	// direction to check next
	// 0: north, 1: east, 2: south, 3: west, 4: complete
	private int direction;
	private int[][] grid;
	private boolean visited;
	private MazeCell parent;

	// set row and col with r and c
	public MazeCell(int r, int c) {
		row = r;
		col = c;
		direction = 0;
		visited = false;
	}

	public MazeCell getParent() {
		return parent;
	}

	public void setParent(MazeCell parent) {
		this.parent = parent;
	}

	public boolean isPath() {
		return grid[row][col] != 0; // Assuming 0 represents a wall in the maze
	}

	// no-arg constructor
	public MazeCell() {
		row = col = -1;
		direction = 0;
		visited = false;
	}

	// copy constructor
	MazeCell(MazeCell rhs) {
		this.row = rhs.row;
		this.col = rhs.col;
		this.direction = rhs.direction;
		this.visited = rhs.visited;
	}

	public int getDirection() {
		return direction;
	}

	// update direction. if direction is 4, mark cell as visited
	public void advanceDirection() {
		direction++;
		if (direction == 4)
			visited = true;
	}

	// change row and col to r and c
	public void setCoordinates(int r, int c) {
		row = r;
		col = c;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MazeCell other = (MazeCell) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	// set visited status to true
	public void visit() {
		visited = true;
	}

	// reset visited status
	public void reset() {
		visited = false;
	}

	// return true if this cell is unvisited
	public boolean unVisited() {
		return !visited;
	}

	// may be useful for testing, return string representation of cell
	public String toString() {
		return "(" + row + "," + col + ")";
	}
} // end of MazeCell class
