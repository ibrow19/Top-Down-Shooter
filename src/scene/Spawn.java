package scene;

import processing.core.PVector;
import random.Randomiser;
import rect.Rect;
import config.Config;

/// Game spawn area.
public class Spawn {
    
    // Constants for size of each spawn rectangle.
    public static final float WIDTH = 250f;
    public static final float HEIGHT = 250f;

    /// Rectangle areas in which enemies can be spawned.
    private static final Rect[] mSpawns;

    /// Rectangle representing play area bounds.
    private static final Rect mAreaBounds;

    static {

        // Spawn rectangle on each edge of play area.
        mSpawns = new Rect[] {
            new Rect(-WIDTH, 0f, WIDTH, Config.AREA_HEIGHT),
            new Rect(0f, -HEIGHT, Config.AREA_WIDTH, HEIGHT),
            new Rect(Config.AREA_WIDTH, 0f, WIDTH, Config.AREA_HEIGHT),
            new Rect(0f, Config.AREA_HEIGHT, Config.AREA_WIDTH, HEIGHT),
        };

        mAreaBounds = new Rect(0f, 0f, Config.AREA_WIDTH, Config.AREA_HEIGHT);

    }


    /// Create a spawn point by choosing a random position in a random spawn rectangle.
    /// \return the spawn position.
    public static PVector generateSpawnPoint() {

        int spawnIndex = Randomiser.randomInt(0, mSpawns.length - 1);
        return Randomiser.randomPoint(mSpawns[spawnIndex]);

    }


    /// Generate a spawn point and a direction pointing from that spawn point to
    /// a random point in the play area.
    /// \param direction vector to set to the generated direction.
    /// \return the spawn position.
    public static PVector generateSpawnPoint(PVector direction) {

        int spawnIndex = Randomiser.randomInt(0, mSpawns.length - 1);
        PVector spawn = Randomiser.randomPoint(mSpawns[spawnIndex]);

        PVector newDirection = Randomiser.randomPoint(mAreaBounds);
        newDirection.sub(spawn).normalize();
        direction.x = newDirection.x;
        direction.y = newDirection.y;

        return spawn;

    }

}
