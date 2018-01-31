package grid;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Comparator;
import java.util.PriorityQueue;
import processing.core.PVector;
import rect.Rect;
import config.Config;

/// Grid that creates spacial partition of an area.
public class Grid {

    /// Size of cells in the grid.
    private final float mCellSize;

    /// Area that the grid covers.
    private final Rect mBounds;

    /// Array of cells in the grid.
    private final Cell[][] mCells;

    /// Path data used for A* path finding.
    /// Stores coordinates of a cell and the cost to reach that cell.
    private class PathData {

        public Coordinates coords;
        public float cost;

        public PathData(Coordinates coords, float cost) {

            this.coords = coords;
            this.cost = cost;

        }

    }

    /// Comparator used for comparing path data in A* search.
    private class PathComparator implements Comparator<PathData> {

        public int compare(PathData o1, PathData o2) {

            if (o1.cost < o2.cost) {
                return -1;
            }
            if (o1.cost > o2.cost) {
                return 1;
            }
            return 0;

        }

    }

    /// Initialise grid.
    public Grid(Rect bounds, float cellSize) {

        mCellSize = cellSize;
        mBounds = bounds.copy();

        int xCells = (int)(bounds.width / cellSize);
        int yCells = (int)(bounds.height / cellSize);

        // Initialise each cell.
        mCells = new Cell[xCells][yCells];
        for (int i = 0; i < xCells; ++i) {

            for (int j = 0; j < yCells; ++j) {

                mCells[i][j] = new Cell();

            }

        }

    }

    /// Convert translation in area to grid coordinates.
    /// \param translation position to convert to coordinates.
    /// \return the coordinates corresponding to the translation or null
    ///         if the translation lies outside the grid.
    public Coordinates getCoords(PVector translation) {

        // return null if translation outside bounds.
        if (translation.x < mBounds.x ||
            translation.y < mBounds.y ||
            translation.x >= (mBounds.x + mBounds.width) ||
            translation.y >= (mBounds.y + mBounds.height)) {
            
            return null;

        }

        float x = (translation.x - mBounds.x) / mCellSize;
        float y = (translation.y - mBounds.y) / mCellSize;
        Coordinates result = new Coordinates((int)x, (int)y);
        // Account for potential floating point errors.
        if (result.x >= mCells.length) {

            result.x = mCells.length - 1;

        } else if (result.x < 0) {

            result.x = 0;

        }
        if (result.y >= mCells[0].length) {

            result.y = mCells[0].length - 1;

        } else if (result.y < 0) {

            result.y = 0;

        }
        return result;

    }

    
    /// Get translation corresponding to the centre of a cell.
    /// \param coords coordinates to get the translation for.
    /// \return the translation corresponding to the centre of the cell coordinates.
    public PVector getTranslation(Coordinates coords) {

        assert checkValid(coords);

        float x = (coords.x * mCellSize) + mBounds.x + (mCellSize / 2f);
        float y = (coords.y * mCellSize) + mBounds.y + (mCellSize / 2f);
        return new PVector(x, y);

    }

    /// Get a cell a specified coordinates.
    /// \param coords the coordinates of the cell to get.
    /// \return the cell at the specified coordinates.
    public Cell getCell(Coordinates coords) {

        return mCells[coords.x][coords.y];

    }

    /// Create a list of neighbouring coordinates.
    /// \param coords coordinates to get the neighbouring coordinates for.
    /// \return a list of neighbouring coordinates.
    public ArrayList<Coordinates> getNeighbourCoords(Coordinates coords) {

        assert checkValid(coords);
        ArrayList<Coordinates> neighbours = new ArrayList<Coordinates>();

        boolean left = coords.x > 0;
        boolean right = coords.x < (mCells.length - 1);
        boolean upper = coords.y > 0;
        boolean lower = coords.y < (mCells[0].length - 1);

        if (left) {
            neighbours.add(new Coordinates(coords.x - 1, coords.y));
            if (upper) {
                neighbours.add(new Coordinates(coords.x - 1, coords.y - 1));
            }
            if (lower) {
                neighbours.add(new Coordinates(coords.x - 1, coords.y + 1));
            }
        }
        
        if (right) {
            neighbours.add(new Coordinates(coords.x + 1, coords.y));
            if (upper) {
                neighbours.add(new Coordinates(coords.x + 1, coords.y - 1));
            }
            if (lower) {
                neighbours.add(new Coordinates(coords.x + 1, coords.y + 1));
            }
        }

        if (upper) {
            neighbours.add(new Coordinates(coords.x, coords.y - 1));
        }
        if (lower) {
            neighbours.add(new Coordinates(coords.x, coords.y + 1));
        }

        return neighbours;

    }

    /// Create a list of neighbouring cells.
    /// \param coords coordinates to get the neighbouring cells for.
    /// \return a list of neighbouring cells.
    public ArrayList<Cell> getNeighbourCells(Coordinates coords) {

        ArrayList<Coordinates> neighbourCoords = getNeighbourCoords(coords);
        ArrayList<Cell> neighbours = new ArrayList<Cell>();
        Iterator<Coordinates> it = neighbourCoords.iterator();
        while (it.hasNext()) {
        
            neighbours.add(getCell(it.next()));

        }
        return neighbours;

    }

    /// Get the shortest path from one coordinate to another.
    /// \param start starting cell coordinates.
    /// \param goal goal cell coordinates.
    /// \return a list of coordinates to follow for the shortest path to the goal
    ///         or null if no path exists.
    public ArrayList<Coordinates> getShortestPath(Coordinates start, Coordinates goal) {

        assert checkValid(start);
        assert checkValid(goal);

        // Evaluated coordinates.
        HashSet<Coordinates> evaluated = new HashSet<Coordinates>();

        // Previous step to reach a cell.
        HashMap<Coordinates, Coordinates> previousStep = new HashMap<Coordinates, Coordinates>();

        // Cost of reaching a cell.
        HashMap<Coordinates, Float> baseCost = new HashMap<Coordinates, Float>();
        baseCost.put(start, 0f);

        // Unevaluated coordinates with baseCost + admissable heuristic (Directly to the goal).
        PriorityQueue<PathData> unevaluated = new PriorityQueue<PathData>(11, new PathComparator());
        unevaluated.add(new PathData(start, start.distance(goal)));

        boolean found = false;
        Coordinates current = null;

        // Search until goal found or no more cells to evaluate.
        while (!found && unevaluated.peek() != null) {

            // Get lowest cost unevaluated nodes.
            current = unevaluated.poll().coords;
            found = current.equals(goal);

            // If it is not goal and a better path has not been evaluated already
            // then evaluate its neighbours.
            if (!found && !evaluated.contains(current)) {

                evaluated.add(current);  
                ArrayList<Coordinates> neighbours = getNeighbourCoords(current);
                Iterator<Coordinates> it = neighbours.iterator();

                // Evaluate neighbours.
                while (it.hasNext()) {

                    Coordinates n = it.next();

                    // Skip already evaluated cells and those that contain asteroids.
                    // Unless that cell is the goal.
                    if (!evaluated.contains(n) && 
                        (mCells[n.x][n.y].asteroids.isEmpty() || n.equals(goal))) {

                        float g = baseCost.get(current) + current.distance(n);
                        if (!baseCost.containsKey(n) || baseCost.get(n) > g) {

                            baseCost.put(n, g); 
                            previousStep.put(n, current);
                            float f = g + n.distance(goal);
                            unevaluated.add(new PathData(n, f));

                        }

                    }

                }


            }


        }

        // Reconstruct path if a path was found.
        if (found) {

            ArrayList<Coordinates> path = new ArrayList<Coordinates>();
            while (current != null) {

                path.add(0, current);
                current = previousStep.get(current);

            }
            return path;

        }

        return null;

    }

    /// Check coordinate is valid.
    /// \param coords coordinates to check are valid.
    /// \return whether the coordinates are valid.
    private boolean checkValid(Coordinates coords) {

        return !(coords == null ||
                 coords.x < 0 ||
                 coords.y < 0 ||
                 coords.x >= mCells.length ||
                 coords.y >= mCells[0].length);

    }

}
