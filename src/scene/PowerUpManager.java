package scene;

import processing.core.PVector;
import gameobject.PowerUp;
import grid.Grid;
import texture.Texture;
import random.Randomiser;
import config.Config;

/// Spawns, updates and renders power-ups.
public class PowerUpManager extends SpawnManager<PowerUp> {

    /// Power-up movement speed constant.
    private static final float POWERUP_SPEED = 50f;

    /// Texture to use for power-ups.
    private final Texture mPowerUpTexture;

    /// Grid to spawn power-ups in.
    private final Grid mGrid;

    /// Rate to spawn health pickups at.
    private float mHealthRate;

    /// Initialise power-up manager.
    public PowerUpManager(Texture powerUpTexture, Grid grid, float spawnInterval) {

        super(spawnInterval);
        mPowerUpTexture = powerUpTexture;
        mGrid = grid;
        mHealthRate = 0f;

    }

    /// Set the rate health pickups are spawned.
    /// \param rate the rate to spawn health pickups.
    public void setHealthRate(float rate) {

        mHealthRate = rate;

    }

    /// Spawn a power-up at a random position in the spawn area pointing towards
    /// the play area.
    /// \return spawned power-up.
    protected PowerUp spawn() {

        PVector velocity = new PVector(0f, 0f);
        PVector spawn = Spawn.generateSpawnPoint(velocity);
        velocity.mult(POWERUP_SPEED);
        PowerUp powerUp = new PowerUp(mPowerUpTexture, mGrid, generateId(), velocity);
        powerUp.setTranslation(spawn);
        return powerUp;

    }

    /// Generate random Id to use for a power-up.
    private int generateId() {

        // Choose health pickup based on current rate.
        if (Randomiser.randomFloat(0, 1) < mHealthRate) {

            return PowerUp.HEALTH_ID;

        }
        // If health pickup is not chosen, choose equally between remaining.
        return Randomiser.randomInt(2, 4);

    }

}
