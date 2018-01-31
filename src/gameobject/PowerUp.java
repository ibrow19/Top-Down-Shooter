package gameobject;

import processing.core.PVector;
import texture.Texture;
import grid.Grid;
import grid.Coordinates;
import grid.Cell;
import config.Config;

/// Power-up that drifts through space.
public class PowerUp extends GridObject {

    // Constant power-up IDs.
    public static final int HEALTH_ID = 1;
    public static final int FIRE_RATE_ID = 2;
    public static final int PENETRATE_ID = 3;
    public static final int REFLECT_ID = 4;

    /// Id used by this power-up.
    private final int mId;

    /// Current velocity of power-up.
    private final PVector mVelocity;

    /// Initialise power-up.
    public PowerUp(Texture texture, Grid grid, int id, PVector velocity) {

        super(texture, grid);
        mVelocity = velocity.copy();
        mId = id;
        setClip(mId);

        // Don't need to check collisions for power-up (other
        // objects instead check if the collide with the power-up).
        mCanCollide = false;

    }

    /// Get the id used by this power-up.
    public int getId() {

        return mId;

    }

    /// Update the power-up by moving it.
    /// \param delta time since the last update.
    protected void updateCurrent(float delta) {

        translate(PVector.mult(mVelocity, delta));

    }

    /// Add the power-up to a cell in the grid.
    /// \param cell cell to add the power-up to in the grid.
    protected void addToCell(Cell cell) {

        cell.powerUps.add(this);

    }

    /// Remove the power-up from a cell in the grid.
    /// \param cell cell to remove the power-up from in the grid.
    protected void removeFromCell(Cell cell) {

        cell.powerUps.remove(this);

    }

}
