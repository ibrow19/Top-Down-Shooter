package scene;

import processing.core.PVector;
import gameobject.Asteroid;
import grid.Grid;
import texture.Texture;

/// Spawns, updates and renders asteroids.
public class AsteroidManager extends SpawnManager<Asteroid> {

    /// Speed constant to use for asteroids.
    private static final float ASTEROID_SPEED = 20f;

    /// Texture to use for asteroids.
    private final Texture mAsteroidTexture;

    /// Grid to put asteroids in.
    private final Grid mGrid;

    /// Initialise manager
    public AsteroidManager(Texture asteroidTexture, Grid grid, float spawnInterval) {

        super(spawnInterval);
        mAsteroidTexture = asteroidTexture;
        mGrid = grid;

    }

    /// Spawn an asteroid at a random position in the spawn area pointing towards
    /// the play area.
    /// \return spawned asteroid.
    protected Asteroid spawn() {

        PVector velocity = new PVector(0f, 0f);
        PVector spawn = Spawn.generateSpawnPoint(velocity);
        velocity.mult(ASTEROID_SPEED);
        Asteroid asteroid = new Asteroid(mAsteroidTexture, mGrid, velocity);
        asteroid.setTranslation(spawn);
        return asteroid;

    }

}
