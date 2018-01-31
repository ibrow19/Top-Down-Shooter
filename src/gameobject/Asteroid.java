package gameobject;

import processing.core.PVector;
import texture.Texture;
import grid.Grid;
import grid.Coordinates;
import grid.Cell;

/// Asteroid that drifts slowly through the play area.
public class Asteroid extends GridObject {

    /// Speed that asteroid rotates.
    private static final float ROTATE_RATE = 20f;

    /// The velocity that the asteroid is traveling at.
    private final PVector mVelocity;

    /// Initialise asteroid properties.
    public Asteroid(Texture texture, Grid grid, PVector velocity) {

        super(texture, grid);
        mVelocity = velocity.copy();

        // Don't need to check collisions for asteroid (other
        // objects instead check if the collide with the asteroid).
        mCanCollide = false;

    }

    /// Update the asteroid by moving and rotating.
    /// \param delta the time since the last update.
    protected void updateCurrent(float delta) {

        translate(PVector.mult(mVelocity, delta));
        rotate(ROTATE_RATE * delta);

    }

    /// Update a grid cell with this asteroid.
    /// \param cell the cell to add the asteroid to.
    protected void addToCell(Cell cell) {

        cell.asteroids.add(this);

    }

    /// Remove this asteroid from a grid cell.
    /// \param cell the cell to remove the asteroid from.
    protected void removeFromCell(Cell cell) {

        cell.asteroids.remove(this);

    }

}
