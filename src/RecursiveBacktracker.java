import java.util.Collections;
import java.util.ArrayList;

// This class produces a randomly generated, fully traversable maze
// using recursive backtracking.

public class RecursiveBacktracker {
    private int[][] grid;
    private int size;
    private int startx;
    private int starty;

    // Enumeration of traversable directions.
    private enum Direction {
        N, S, E, W;

        public static int getValue(Direction direction) {
            int value = (direction == N || direction == E) ? 2 : -2;
            return value;
        }
    }

    public RecursiveBacktracker() {
        this.size = 35;
        this.startx = 1;
        this.starty = 1;
        this.grid = initializeGrid();

        walk(startx, starty);
    }

    public RecursiveBacktracker(int size, int startx, int starty) throws MazeGenerationException {
        // Throw exceptions: 
        // size, startx, and starty must be odd or the maze will be malformed.
        // startx and starty must be in the bounds of the grid.
        if(size % 2 == 0) throw new MazeGenerationException("Size must be an odd integer.");
        if(startx % 2 == 0 || starty % 2 == 0) throw new MazeGenerationException("startx and starty must be odd integers.");
        if(startx > size - 1 || startx < 0) throw new MazeGenerationException("startx is out of bounds.");
        if(starty > size - 1 || starty < 0) throw new MazeGenerationException("start y is out of bounds.");
        
        this.size = size;
        this.startx = startx;
        this.starty = starty;
        this.grid = initializeGrid();

        walk(startx, starty);
    }

    // Recursive method:
    private void walk(int cx, int cy) {
        // Create a list of directions that can be traveled from the current position.
        // These directions are not neccessarily valid reachable directions. They may have been: 
        //     (1) already visited
        //     (2) exist outside of the bounds of the array

        ArrayList<Direction> directions = new ArrayList<Direction>();
        directions.add(Direction.N);
        directions.add(Direction.S);
        directions.add(Direction.E);
        directions.add(Direction.W);
        // Shuffle the list of directions to randomize each direction's precedence.
        Collections.shuffle(directions);

        int nx, ny, start, end;
        for (Direction direction : directions) {

            // Determine new coordinates given the direction traveled from the previous coordinates.
            int value = Direction.getValue(direction);
            ny = (direction == Direction.N || direction == Direction.S) ? cy + value : cy;
            nx = (direction == Direction.N || direction == Direction.S) ? cx : cx + value;
            
            // Determine how to reach the new coordinates from the current coordinates.
            if(direction == Direction.N || direction == Direction.S) {
                start = (cy < ny) ? cy : ny;
                end = (ny > cy) ? ny : cy;
            }
            else {
                start = (cx < nx) ? cx : nx;
                end = (nx > cx) ? nx : cx;
            }

            // Check if the new coordinates are within the bounds of the grid.
            boolean withinBounds = (nx > 0 && nx < grid.length - 1) && (ny > 0 && ny < grid.length - 1);

            // If the new coordinates are within bounds and they have not been previously visited:
            //     (1) Remove the wall seperating the new coords and the previous coords.
            //     (2) Recurse from the new coords.
            if(withinBounds && grid[ny][nx] == 2) {
                if(direction == Direction.N || direction == Direction.S)
                    for(int i = start; i <= end; i++) grid[i][cx] = 0;
                else
                    for(int i = start; i <= end; i++) grid[cy][i] = 0;

                walk(nx, ny);
            }
        }
    }

    // Make the grid accessible.
    public int[][] getGrid() {
        return this.grid;
    }

    // Print the grid in an easy to look at format.
    public void printGrid() {
        for(int i = grid[0].length - 1; i >= 0 ; i--) {
            for(int j = 0; j < grid.length; j++) {
                if(grid[i][j] == 1) System.out.print("[#]");
                else System.out.print("   ");
            }
            System.out.println();
        }
    }

    // Helper method: Initializes the grid, populating it with 1's and 2's.
    // 1 represents nontraversable terrain; a permanent wall.
    // 2 represents a cell that has not yet been visited.
    private int[][] initializeGrid() {
        int[][] temp = new int[size][size];
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                temp[i][j] = (i % 2 == 0 || j % 2 == 0) ? 1 : 2;
            }
        }
        return temp;
    }
}