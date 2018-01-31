package scene;

import java.util.function.Supplier;
import processing.core.PVector;
import gameobject.Enemy;

/// Manages spawning, updating and rendering of enemies.
public class EnemyManager extends SpawnManager<Enemy> {

    /// Factory to spawn enemies with.
    private Supplier<Enemy> mFactory;

    /// Base interval for spawning enemies.
    private final float mBaseInterval;

    /// Extra time to modify spawn interval based on current enemy bound.
    private final float mExtraInterval;

    /// Maximum number of enemies.
    private final int mMax;

    /// Current upper bound on the number of enemies.
    private int mBound;

    /// Current threshold for slow start.
    private int mThreshold;

    /// Whether slow start bound build up is currently active.
    private boolean mSlowStart;

    /// Initialise enemy manager.
    public EnemyManager(Supplier<Enemy> factory, 
                        float spawnInterval, 
                        float extraInterval, 
                        int max) {

        super(spawnInterval + extraInterval);
        mFactory = factory;
        mBaseInterval = spawnInterval;
        mExtraInterval = extraInterval;
        mMax = max;
        mBound = 0;
        mThreshold = max;
        mSlowStart = true;

    }


    /// Reset bound and activate slow start. Used to limit enemy spawn rate when
    /// player is struggling.
    public void limitBound() {

        mThreshold = mBound / 2;
        mBound = 0;
        mSlowStart = true;

    }

    
    /// Adjust the enemy bound and spawn rate and spawn an enemy if there are
    /// fewer enemies than the current bound.
    protected Enemy spawn() {

        adjustBound();
        adjustSpawnRate();
        // Do not spawn enemy if number of enemies currently exceed bound.
        if (size() >= mBound) {

            return null;

        }

        // Generate spawn position and direction.
        PVector direction = new PVector(0f, 0f);
        PVector spawn = Spawn.generateSpawnPoint(direction);
        Enemy enemy = mFactory.get();
        enemy.setTranslation(spawn);

        // Calculate enemy rotation.
        float rotation = (float)Math.atan(direction.y / direction.x) * (180f / (float)Math.PI);

        // Orientate rotation to match enemy.
        if (direction.x >= 0f) {

            rotation += 90f;

        } else {

            rotation += 270f;

        }
        enemy.setRotation(rotation);
        return enemy;

    }


    /// Adjust bound on enemies.
    private void adjustBound() {

        // Half bound if maximum enemies reached.
        if (size() == mMax) {

            mBound /= 2;
            mSlowStart = false;
            if (mBound < 1) {
                mBound = 1;
            }

        // If in slow start mode double bound until threshold met.
        } else if (mSlowStart) {

            mBound *= 2;
            if (mBound > mThreshold) {

                mBound = mThreshold;
                mSlowStart = false;

            }
            if (mBound == 0) {

                mBound = 1;

            }

        // Otherwise, increment bound.
        } else {

            ++mBound;

        }

        // Ensure bound never exceeds max.
        if (mBound > mMax) {

            mBound = mMax;

        }

    }

    // Adjust spawn rate based on current bound on enemies.
    private void adjustSpawnRate() {

        mSpawnInterval = mBaseInterval + (1f - ((float)mBound/mMax)) * mExtraInterval;

    }

}
