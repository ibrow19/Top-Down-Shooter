package gameobject;

import processing.core.PVector;
import grid.Grid;
import grid.Coordinates;
import grid.Cell;
import texture.Texture;
import config.Config;


/// Enemy that traverse the play area and gives score when destroyed.
public abstract class Enemy extends GridObject {

    /// ID to use for the enemy.
    private final int mId;

    /// Score this enemy is worth.
    private final int mScoreValue;

    /// Initialise enemy.
    public Enemy(Texture texture, 
                 Grid grid, 
                 int id,
                 int scoreValue) {

        super(texture, grid);
        mId = id;
        mScoreValue = scoreValue;

    }

    /// Get this enemy's Id.
    public int getId() {

        return mId;

    }

    /// Get the score this enemy is worth.
    public int getScoreValue() {

        return mScoreValue;

    }

    /// Add the enemy to a cell in the grid.
    /// \param cell the cell in the grid to add the enemy to.
    protected void addToCell(Cell cell) {

        cell.enemies.add(this);

    }

    /// Remove the enemy from a cell in the grid.
    /// \param cell cell in the grid to remove the enemy from.
    protected void removeFromCell(Cell cell) {

        cell.enemies.remove(this);

    }

}
