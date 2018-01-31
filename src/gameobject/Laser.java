package gameobject;

import java.util.Iterator;
import java.util.ArrayList;
import processing.core.PVector;
import grid.Grid;
import grid.Cell;
import texture.Texture;


/// Laser that shoots down enemies.
public abstract class Laser extends GridObject {

    /// Score accumulated by this laser.
    private int mScore;

    /// This laser's current velocity.
    protected PVector mVelocity;

    /// Initialise the laser.
    public Laser(Texture texture, Grid grid, int id, PVector velocity) {

        super(texture, grid);
        mScore = 0;
        mVelocity = velocity.copy();
        setClip(id);

    }

    /// Get the score accumulated by the laser.
    /// \return the score accumulated by the laser.
    public int consumeScore() {

        int score = mScore;
        mScore = 0;
        return score;

    }

    /// Update the laser.
    /// \param delta the time since the last update.
    protected void updateCurrent(float delta) {

        // Get rotation direction of velocity.
        float rotation = (float)Math.atan(mVelocity.y / mVelocity.x) * (180f / (float)Math.PI);

        // Orientate rotation to match laser.
        if (mVelocity.x >= 0f) {

            rotation += 90f;

        } else {

            rotation += 270f;

        }
        setRotation(rotation);

        translate(PVector.mult(mVelocity, delta));

    }


    /// Add laser to a cell in the grid.
    /// \param cell cell to add laser to.
    protected void addToCell(Cell cell) {

        cell.lasers.add(this);

    }

    /// Remove laser from a cell in the grid.
    /// \param cell cell to remove laser from.
    protected void removeFromCell(Cell cell) {

        cell.lasers.remove(this);

    }

    /// Allow subclasses to carry out additional actions on collision.
    protected abstract void onCollision(CollidableObject collidable);

    /// Handle collision with an asteroid based on laser type.
    /// \param asteroid asteroid that was collided with.
    protected void handleCollision(Asteroid asteroid) {

        onCollision(asteroid);

    }

    /// Handle collision with an enemy based on laser type.
    /// \param enemy enemy that was collided with.
    protected void handleCollision(Enemy enemy) {

        mScore += enemy.getScoreValue();
        onCollision(enemy);
        enemy.destroy();

    }

}
