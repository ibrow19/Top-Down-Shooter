package gameobject;

import java.util.Iterator;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PVector;
import texture.Texture;
import grid.Grid;
import grid.Coordinates;
import grid.Cell;
import config.Config;

/// Spaceship with physics based movement.
public class Ship extends GridObject {

    // Constants.
    public static final int MAX_HEALTH = 10;
    private static final float INVULNERABILITY_DURATION = 2f;
    private static final float FLASH_DURATION = 0.2f;
    private static final float ACCELERATION = 300f;
    private static final float MAX_SPEED = 500f;
    private static final float DAMPENING = 0.5f;
    private static final float ROTATE_RATE = 180f;
    private static final float BOUNCE_MULTIPLIER = 0.5f;
    private static final float MIN_BOUNCE_SPEED = 40f;

    /// The ship's current health.
    private int mHealth;

    /// The ship's current velocity.
    private PVector mVelocity;

    /// How long the ship has been invulnerable.
    private float mInvulnerableProgress;

    /// How long the ship had been flashing.
    private float mFlashProgress;

    /// Whether the ship is boosting forward.
    private boolean mBoost;

    /// Whether the ship is turning left.
    private boolean mLeft;

    /// Whether the ship is turning right.
    private boolean mRight;

    /// Whether the ship is invulnerable.
    private boolean mInvulnerable;

    /// Whether the ship is visible.
    private boolean mVisible;

    /// Whether cheat mode is active.
    private boolean mCheat;

    /// IDs of picked up power-ups.
    private final ArrayList<Integer> mPowerUps;

    /// IDs of enemies that have damaged the ship.
    private final ArrayList<Integer> mEnemyDamage;

    /// Initialise the ship.
    public Ship(Texture texture, Grid grid) {

        super(texture, grid);
        mHealth = MAX_HEALTH;
        mVelocity = new PVector(0.f, 0.f);
        mInvulnerableProgress = 0f;
        mFlashProgress = 0f;
        
        mInvulnerable = false;
        mVisible = true;
        mBoost = false;
        mLeft = false;
        mRight = false;
        mCheat = false;

        mPowerUps = new ArrayList<Integer>();
        mEnemyDamage = new ArrayList<Integer>();
    }

    /// Get the current health of the ship.
    /// \return the ship's health.
    public int getHealth() {

        return mHealth;

    }

    /// Check if the ship has picked up power-ups.
    public boolean hasPowerUps() {

        return !mPowerUps.isEmpty();

    }

    /// Get the next power-up picked up by the ship.
    /// \return power-up ID picked up by ship.
    public int getPowerUp() {

        assert hasPowerUps();
        int id = mPowerUps.get(0);
        mPowerUps.remove(0);
        return id;

    }

    /// Check if the ship has been damaged by an enemy.
    public boolean hasEnemyDamage() {

        return !mEnemyDamage.isEmpty();

    }

    /// Get the ID of the next enemy that damaged the ship.
    /// \return the ID of the enemy that damaged the ship.
    public int getEnemyDamage() {

        assert hasEnemyDamage();
        int id = mEnemyDamage.get(0);
        mEnemyDamage.remove(0);
        return id;

    }

    /// Activate or deactivate cheat mode.
    /// \param cheat status to set cheat mode to.
    public void setCheat(boolean cheat) {

        mCheat = cheat;

    }

    /// Activate or deactivate boosting.
    /// param boost whether the ship is boosting.
    public void setBoost(boolean boost) {

        mBoost = boost;

    }

    /// Set whether the ship is turning left.
    /// \param left whether the ship is turning left.
    public void setLeft(boolean left) {

        mLeft = left;

    }

    /// Set whether the ship is turning right.
    /// \param left whether the ship is turning right.
    public void setRight(boolean right) {

        mRight = right;

    }

    /// Check whether the ship is currently visible.
    /// \return whether the ship is currently visible.
    public boolean isVisible() {

        return mVisible;

    }

    /// Update the ship.
    /// \param delta the time passed since the last update.
    protected void updateCurrent(float delta) {

        updateInvulnerability(delta);
        updateVelocity(delta);
        checkBounds();

        // Move.
        translate(PVector.mult(mVelocity, delta));

        // Dampen velocity.
        mVelocity.sub(PVector.mult(mVelocity, DAMPENING * delta));

        updateRotation(delta);

    }

    /// Render the ship if it is visible.
    /// \param core the processing core to use for rendering.
    public void renderCurrent(PApplet core) {

        if (mVisible) {

            super.renderCurrent(core);

        }

    }

    /// Add the ship to a grid cell.
    /// \param cell the grid cell to add the ship to,
    protected void addToCell(Cell cell) {

        cell.ship = this;

    }

    /// Remove the ship from a grid cell.
    /// \param cell the grid cell to remove the ship from.
    protected void removeFromCell(Cell cell) {

        cell.ship = null;

    }

    /// Handle collision with asteroid by bouncing off it.
    /// \param asteroid asteroid that has been collided with.
    protected void handleCollision(Asteroid asteroid) {

        bounce(asteroid);

    }

    /// Handle collision with enemy by taking damage, destroying the enemy, bouncing 
    /// off it and entering short invulnerability mode.
    /// \param enemy that has bee collided with.
    protected void handleCollision(Enemy enemy) {

        // Only collide if not invulnerable and cheat mode is off.
        if (!mInvulnerable && !mCheat) {

            mInvulnerable = true;
            mVisible = false;
            bounce(enemy);
            --mHealth;
            mEnemyDamage.add(enemy.getId());
            enemy.destroy();

        }

    }

    /// Handle collision with power-up by consuming the power-up.
    /// \param powerUp power-up that was collided with.
    protected void handleCollision(PowerUp powerUp) {

        int id = powerUp.getId();
        if (id == PowerUp.HEALTH_ID) {

            if (mHealth < MAX_HEALTH) {
                
                ++mHealth;

            }

        } else {

            mPowerUps.add(id);

        }
        powerUp.destroy();

    }

    /// Update ship invulnerability, flashing and ending invulnerability when duration reached.
    /// \param delta time passed since last update.
    private void updateInvulnerability(float delta) {

        if (mInvulnerable) {

            mInvulnerableProgress += delta;
            if (mInvulnerableProgress > INVULNERABILITY_DURATION) {

                mInvulnerable = false; 
                mInvulnerableProgress = 0f;
                mFlashProgress = 0f;
                mVisible = true;

            } else {

                mFlashProgress += delta;
                while (mFlashProgress > FLASH_DURATION) {

                    mFlashProgress -= FLASH_DURATION;
                    mVisible = !mVisible;

                }

            }

        }

    }

    /// Update ships velocity.
    /// \param delta the time passed since the last update.
    private void updateVelocity(float delta) {

        // Accelerate forwards if the ship is boosting.
        if (mBoost) {

            PVector acceleration = new PVector(0f, -ACCELERATION * delta);
            acceleration.rotate(getRadianRotation());
            mVelocity.add(acceleration);

            if (mVelocity.mag() > MAX_SPEED) {

                mVelocity.setMag(MAX_SPEED);

            }

        }

    }

    /// Check whether the ship is out of bounds and bounce back inside if it is.
    private void checkBounds() {

        PVector pos = getTranslation();
        float radius = getRadius();

        // Bounce when out of bounds.
        if (((pos.x - radius) < 0f && mVelocity.x < 0f) ||
            ((pos.x + radius) > Config.AREA_WIDTH && mVelocity.x > 0f)) {

            // reverse velocity.
            mVelocity.x *= -1;

            // Move in bounds.
            if ((pos.x - radius) < 0f) {

                setTranslation(radius, pos.y);

            } else {

                setTranslation(Config.AREA_WIDTH - radius, pos.y);

            }

        }
        if (((pos.y - radius) < 0f && mVelocity.y < 0f) ||
            ((pos.y + radius) > Config.AREA_HEIGHT && mVelocity.y > 0f)) {

            // reverse velocity.
            mVelocity.y *= -1;

            // Move in bounds.
            if ((pos.y - radius) < 0f) {

                setTranslation(pos.x, radius);

            } else {

                setTranslation(pos.x, Config.AREA_HEIGHT - radius);

            }

        }

    }

    /// Update the ships rotation based on whether left and right flags are set.
    /// \param delta time passed since last update.
    private void updateRotation(float delta) {

        if (mLeft && !mRight) {

            rotate(-ROTATE_RATE * delta);

        } else if (mRight && !mLeft) {

            rotate(ROTATE_RATE * delta);

        }

    }

    /// Bounce off a collidable object.
    /// collidable object to bounce off.
    private void bounce(CollidableObject collidable) {

        // caclulate bounce speed.
        float bounceSpeed = mVelocity.mag() * BOUNCE_MULTIPLIER;
        if (bounceSpeed < MIN_BOUNCE_SPEED) {
            
            bounceSpeed = MIN_BOUNCE_SPEED;

        }

        // Move outside collidable and set velocity moving away from collidable.
        PVector collidePos = collidable.getTranslation();
        PVector distance = PVector.sub(collidePos, getTranslation());
        distance.normalize();
        mVelocity = PVector.mult(distance, -bounceSpeed);
        distance.mult(collidable.getRadius() + getRadius());
        setTranslation(collidePos.sub(distance));

    }

}
