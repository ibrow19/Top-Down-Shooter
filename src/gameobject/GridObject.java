package gameobject;

import java.util.Iterator;
import java.util.ArrayList;
import processing.core.PVector;
import texture.Texture;
import grid.Grid;
import grid.Coordinates;
import grid.Cell;
import config.Config;

/// An object that uses the grid spatial partition.
public abstract class GridObject extends CollidableObject {

    /// Whether the object has been destroyed.
    private boolean mDestroyed;

    /// Grid that the object is in.
    protected final Grid mGrid;

    /// Current coordinates is the grid (null coordinates means the object
    /// is not currently in the grid).
    protected Coordinates mCoords;

    /// Whether or not the grid object should check for collisions.
    protected boolean mCanCollide;

    /// initialise grid object.
    public GridObject(Texture texture, Grid grid) {

        super(texture);
        mDestroyed = false;
        mGrid = grid;

        // Initialise with null coordinates, initialise on first update.
        mCoords = null;

        mCanCollide = true;


    }

    /// Get the coordinates of the object in the grid.
    public Coordinates getCoords() {

        if (mCoords == null) {

            return null;

        }
        return mCoords.copy();

    }

    /// Mark the object for destruction.
    public void destroy() {

        mDestroyed = true;

    }

    /// Check if the object is destroyed.
    public boolean isDestroyed() {

        return mDestroyed;

    }

    /// Remove the object from the grid.
    public void removeFromGrid() {

        // Remove from grid on destruction.
        if (mDestroyed  && mCoords != null) {

            removeFromCell(mGrid.getCell(mCoords));

        }

    }

    
    public void update(float delta) {

        // Initialise grid position on first update.
        if (!mDestroyed && mCoords == null) {

            updateGrid();

        }
        // Do not update if destroyed.
        if (!mDestroyed) {

            updateCurrent(delta);
            updateGrid();
            if (mCanCollide) {

                handleCollisions();

            }

        }

    }

    /// Subclass specific updates.
    /// \param delta time since last update.
    protected abstract void updateCurrent(float delta);

    /// Add the object to the specified cell.
    /// \param cell cell to add the object to.
    protected abstract void addToCell(Cell cell);

    /// Remove the object from the specified cell.
    /// cell cell to remove object from.
    protected abstract void removeFromCell(Cell cell);

    // Collision subclasses may choose to implement for
    // each type of collidable object in the grid.
    protected void handleCollision(Ship ship) {}
    protected void handleCollision(Laser laser) {}
    protected void handleCollision(Asteroid asteroid) {}
    protected void handleCollision(PowerUp powerUp) {}
    protected void handleCollision(Enemy enemy) {}

    /// Update the grid with the object's current position.
    private void updateGrid() {

        Coordinates newCoords = mGrid.getCoords(getTranslation());

        // Destroy if out of bounds.
        if (newCoords == null) {

            destroy();

        }

        // Update cells.
        if (newCoords != mCoords) {
            
            if (mCoords != null) {

                removeFromCell(mGrid.getCell(mCoords));

            }
            if (newCoords != null) {

                addToCell(mGrid.getCell(newCoords));

            }
            mCoords = newCoords;

        }

    }

    /// Handle any collisions with nearby objects.
    private void handleCollisions() {

        if (mCoords != null) {

            Cell current = mGrid.getCell(mCoords);
            handleCollisions(current);

            ArrayList<Cell> neighbours = mGrid.getNeighbourCells(mCoords);
            Iterator<Cell> it = neighbours.iterator();
            while (it.hasNext() && !isDestroyed()) {
            
                Cell cell = it.next();
                handleCollisions(cell);

            }

        }

    }


    /// Handle collision with objects in a specific cell.
    /// \param cell cell to handle collisions with.
    private void handleCollisions(Cell cell) {

        if (cell.ship != null && collides(cell.ship)) {

            handleCollision(cell.ship);

        }
        handleLasers(cell.lasers);
        handleAsteroids(cell.asteroids);
        handleEnemies(cell.enemies);
        handlePowerUps(cell.powerUps);

    }

    /// Handle collision with all lasers in a list.
    /// \param lasers list of lasers to check collision with.
    private void handleLasers(ArrayList<Laser> lasers) {

        Iterator<Laser> it = lasers.iterator();
        while (it.hasNext() && !isDestroyed()) {
        
            Laser laser = it.next();
            if (collides(laser)) {

                handleCollision(laser);

            }

        }

    }

    /// Handle collision with all asteroids in a list.
    /// \param asteroids list of asteroids to check collision with.
    private void handleAsteroids(ArrayList<Asteroid> asteroids) {

        Iterator<Asteroid> it = asteroids.iterator();
        while (it.hasNext() && !isDestroyed()) {
        
            Asteroid asteroid = it.next();
            if (collides(asteroid)) {

                handleCollision(asteroid);

            }

        }

    }

    /// Handle collision with all power-ups in a list.
    /// \param power-ups list of power-ups to check collision with.
    private void handlePowerUps(ArrayList<PowerUp> powerUps) {

        Iterator<PowerUp> it = powerUps.iterator();
        while (it.hasNext() && !isDestroyed()) {
        
            PowerUp powerUp = it.next();
            if (collides(powerUp)) {

                handleCollision(powerUp);

            }

        }

    }

    /// Handle collision with all enemies in a list.
    /// \param enemies list of enemies to check collision with.
    private void handleEnemies(ArrayList<Enemy> enemies) {

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext() && !isDestroyed()) {
        
            Enemy enemy = it.next();
            if (collides(enemy)) {

                handleCollision(enemy);

            }

        }

    }

}
