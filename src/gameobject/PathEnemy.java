package gameobject;

import processing.core.PVector;
import grid.Grid;
import grid.Coordinates;
import grid.Cell;
import texture.Texture;
import config.Config;

/// Enemy that uses an pathing strategy to dictate its movement.
public abstract class PathEnemy extends Enemy {

    // Constants for bouncing.
    private static final float BOUNCE_MULTIPLIER = 0.6f;
    private static final float MIN_BOUNCE_SPEED = 45f;

    /// Interval that pathing is updated at.
    private final float mPathInterval;

    /// Time since last pathing update.
    private float mPathProgress;

    /// Current velocity of enemy.
    private PVector mVelocity;
    
    /// Maximum speed the enemy can travel at.
    private final float mMaxSpeed;

    /// Rate the enemy accelerates.
    private final float mAcceleration;

    /// Rate that the enemy rotates.
    private final float mRotateRate;

    /// Current target position enemy is moving towards.
    private PVector mTarget;

    /// Initialise enemy properties.
    public PathEnemy(Texture texture, 
                     Grid grid, 
                     int id,
                     int scoreValue,
                     float pathInterval,
                     float maxSpeed,
                     float acceleration,
                     float rotateRate) {

        super(texture, grid, id, scoreValue);

        assert pathInterval > 0f;
        assert maxSpeed > 0f;
        assert acceleration > 0f;
        assert rotateRate > 0f;

        mPathInterval = pathInterval;
        mPathProgress = 0f;

        mVelocity = new PVector(0f, 0f);
        mMaxSpeed = maxSpeed;
        mAcceleration = acceleration;

        mRotateRate = rotateRate;

        mTarget = null;

    }

    /// Bounce off collided asteroid.
    /// \param asteroid asteroid collided with.
    protected void handleCollision(Asteroid asteroid) {

        float bounceSpeed = mVelocity.mag() * BOUNCE_MULTIPLIER;
        if (bounceSpeed < MIN_BOUNCE_SPEED) {
            
            bounceSpeed = MIN_BOUNCE_SPEED;

        }

        // Move outside asteroid.
        PVector asteroidPos = asteroid.getTranslation();
        PVector distance = PVector.sub(asteroidPos, getTranslation());

        distance.normalize();

        mVelocity = PVector.mult(distance, -bounceSpeed);

        distance.mult(asteroid.getRadius() + getRadius());
        setTranslation(asteroidPos.sub(distance));

    }

    /// Update the enemy.
    protected void updateCurrent(float delta) {

        // Update path at regular interval.
        mPathProgress -= delta;
        while (mPathProgress < 0f) {

            mPathProgress += mPathInterval;
            updatePath();

        }

        // Get target, move forward and rotate towards target.
        mTarget = updateTarget();
        updateVelocity(delta);
        translate(PVector.mult(mVelocity, delta));
        updateRotation(delta);

    }

    /// Get target position enemy should move towards.
    protected abstract PVector updateTarget();

    /// Updates the enemy's current pathing.
    protected abstract void updatePath();

    /// Update velocity by accelerating in direction enemy is facing.
    private void updateVelocity(float delta) {

        PVector acceleration = new PVector(0f, -mAcceleration * delta);
        acceleration.rotate(getRadianRotation());
        mVelocity.add(acceleration);
        if (mVelocity.mag() > mMaxSpeed) {

            mVelocity.setMag(mMaxSpeed);

        }

    }


    /// Update enemy's rotation by rotation towards target.
    /// \param delta the time since the last update.
    private void updateRotation(float delta) {

        // Only update rotation if there is a target to rotate towards.
        if (mTarget != null) {

            PVector distance = PVector.sub(mTarget, getTranslation());
            float targetRotation = (float)Math.atan(distance.y / distance.x) * (180f / (float)Math.PI);

            // Orientate rotation to match enemy.
            if (distance.x >= 0f) {

                targetRotation += 90f;

            } else {

                targetRotation += 270f;

            }

            float rotation = getRotation();
            float rotateStep = mRotateRate * delta;
            float difference = rotation - targetRotation;

            // Go to target rotation if possible.
            if (Math.abs(difference) <= rotateStep) {

                setRotation(targetRotation);

            // Rotate left if target is in that direction.
            } else if ((difference > 0f && difference < 180f) ||
                       (difference < -180f)) {

                setRotation(rotation - rotateStep);

            // Otherwise, rotate right.
            } else {

                setRotation(rotation + rotateStep);

            }

        }

    }

}
