package scene;

import java.util.Iterator;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PVector;
import gameobject.Asteroid;
import grid.Grid;
import gameobject.GridObject;
import camera.Camera;
import texture.Texture;
import config.Config;

/// Manages spawning, updating and rendering of one type of game object.
public abstract class SpawnManager<T extends GridObject> {

    /// Interval at which new objects are spawned.
    protected float mSpawnInterval;

    /// Time passed since last spawn.
    private float mSpawnProgress;

    /// Pool of game objects being managed.
    private final ArrayList<T> mGameObjects;

    /// Initialise manager.
    public SpawnManager(float spawnInterval) {

        mSpawnProgress = 0f;
        mSpawnInterval = spawnInterval;
        mGameObjects = new ArrayList<T>();

    }

    /// Get how many objects are currently being managed.
    public int size() {

        return mGameObjects.size();

    }

    /// Get object stored at specified index in game object pool.
    /// \param index index of object to get.
    /// \return the object at the specified index.
    public T get(int index) {

        return mGameObjects.get(index);

    }

    /// Update each game object and remove destroyed objects. Then spawn
    /// new objects if spawn interval reached.
    /// \param delta the time passed since the last update.
    public void update(float delta) {

        // Update gameObjects then remove destroyed.
        Iterator<T> it = mGameObjects.iterator();
        while (it.hasNext()) {
        
            T gameObject = it.next();
            gameObject.update(delta);
            if (gameObject.isDestroyed()) {

                gameObject.removeFromGrid();
                it.remove();

            }

        }
        updateSpawning(delta);

    }

    /// Render each game object.
    /// \param core Processing core to use for rendering.
    /// \param camera Camera to combine with transformation
    public void render(PApplet core, Camera camera) {

        Iterator<T> it = mGameObjects.iterator();
        while (it.hasNext()) {
        
            it.next().render(core, camera);

        }

    }

    /// Spawn a new game object for the manager.
    protected abstract T spawn();

    /// Spawn new objects if spawn interval reached.
    /// \param delta time passed since the last update.
    private void updateSpawning(float delta) {

        mSpawnProgress += delta;
        while (mSpawnProgress > mSpawnInterval) {

            mSpawnProgress -= mSpawnInterval;
            T spawned = spawn();
            if (spawned != null) {

                mGameObjects.add(spawned);

            }

        }

    }

}
