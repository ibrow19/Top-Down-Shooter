package state;

import java.util.function.Supplier;
import processing.core.PVector;
import texture.TextureManager;
import texture.Texture;
import rect.Rect;
import camera.Camera;
import grid.Grid;
import gameobject.Ship;
import gameobject.Turret;
import gameobject.Enemy;
import gameobject.Chaser;
import gameobject.Flocker;
import gameobject.RandomEnemy;
import gameobject.Ghost;
import scene.ProjectileManager;
import scene.AsteroidManager;
import scene.PowerUpManager;
import scene.EnemyManager;
import scene.Spawn;
import config.Config;

/// Game context holding objects that make up the scene.
public class Context {

    // Constants.
    private static final float TOTAL_WIDTH = Config.AREA_WIDTH + 2 * Spawn.WIDTH;
    private static final float TOTAL_HEIGHT = Config.AREA_HEIGHT + 2 * Spawn.HEIGHT;

    private static final float GRID_CELL_SIZE = 250f;

    private static final float ASTEROID_SPAWN_INTERVAL = 6f;
    private static final float POWERUP_SPAWN_INTERVAL = 10f;

    private static final float CHASER_BASE_SPAWN_INTERVAL = 3f;
    private static final float CHASER_EXTRA_SPAWN_INTERVAL = 5f;
    private static final int CHASER_MAX = 6;

    private static final float RANDOM_BASE_SPAWN_INTERVAL = 3f;
    private static final float RANDOM_EXTRA_SPAWN_INTERVAL = 3f;
    private static final int RANDOM_MAX = 15;

    private static final float FLOCKER_BASE_SPAWN_INTERVAL = 4f;
    private static final float FLOCKER_EXTRA_SPAWN_INTERVAL = 3f;
    private static final int FLOCKER_MAX = 10;

    private static final float GHOST_BASE_SPAWN_INTERVAL = 8f;
    private static final float GHOST_EXTRA_SPAWN_INTERVAL = 2f;
    private static final int GHOST_MAX = 5;

    /// Texture manager to use for initialisation.
    private final TextureManager mTextureManager;

    // Objects that make up the game scene.
    public int score;
    public PVector mousePos;
    public Camera camera;
    public Grid grid;
    public Ship ship;
    public Turret turret;
    public ProjectileManager projectileManager;
    public AsteroidManager asteroidManager;
    public PowerUpManager powerUpManager;
    public EnemyManager randomManager;
    public EnemyManager chaserManager;
    public EnemyManager flockerManager;
    public EnemyManager ghostManager;

    /// Initialise context.
    public Context(TextureManager textureManager) {

        mTextureManager = textureManager; 
        reset();

    }

    /// Reinitialise context.
    public void reset() {

        score = 0;
        mousePos = new PVector(0f, 0f);
        grid = new Grid(new Rect(-Spawn.WIDTH, -Spawn.HEIGHT, 
                                 TOTAL_WIDTH, TOTAL_HEIGHT),
                                 GRID_CELL_SIZE);
        initProjectiles();
        initShip();
        initTurret();
        initCamera();
        initAsteroids();
        initPowerUps();
        initRandomEnemies();
        initChasers();
        initFlockers();
        initGhosts();

    }

    /// Initialise the ship.
    private void initShip() {

        Texture shipTexture = mTextureManager.getTexture(Config.SHIP_TEXTURE_ID);
        ship = new Ship(shipTexture, grid);
        ship.setTranslation(Config.AREA_WIDTH / 2f, Config.AREA_HEIGHT / 2f);

    }


    /// Initialise turret.
    private void initTurret() {

        Texture turretTexture = mTextureManager.getTexture(Config.TURRET_TEXTURE_ID);
        turret = new Turret(turretTexture, ship, projectileManager);
        turret.setTranslation(0f, 30f);

    }

    /// Initialise camera.
    private void initCamera() {

        camera = new Camera();
        camera.centre(ship.getTranslation());

    }

    /// Initialise projectiles.
    private void initProjectiles() {

        Texture laserTexture = mTextureManager.getTexture(Config.LASER_TEXTURE_ID);
        projectileManager = new ProjectileManager(laserTexture, grid);

    }

    /// Initialise asteroid manager.
    private void initAsteroids() {

        Texture asteroidTexture = mTextureManager.getTexture(Config.ASTEROID_TEXTURE_ID);
        asteroidManager = new AsteroidManager(asteroidTexture, grid, ASTEROID_SPAWN_INTERVAL);

    }

    private void initPowerUps() {

        Texture powerUpTexture = mTextureManager.getTexture(Config.POWERUP_TEXTURE_ID);
        powerUpManager = new PowerUpManager(powerUpTexture, grid, POWERUP_SPAWN_INTERVAL);

    }

    /// Initialise random enemy manager.
    private void initRandomEnemies() {

        Texture randomTexture = mTextureManager.getTexture(Config.RANDOM_TEXTURE_ID);
        Supplier<Enemy> randomFactory = 
           () -> {
               return new RandomEnemy(randomTexture, grid);
           };
        randomManager = new EnemyManager(randomFactory, 
                                         RANDOM_BASE_SPAWN_INTERVAL,
                                         RANDOM_EXTRA_SPAWN_INTERVAL,
                                         RANDOM_MAX);
    }

    /// Initialise chaser enemy manager.
    private void initChasers() {

        Texture chaserTexture = mTextureManager.getTexture(Config.CHASER_TEXTURE_ID);
        Supplier<Enemy> chaserFactory = 
           () -> {
               return new Chaser(chaserTexture, grid, ship);
           };
        chaserManager = new EnemyManager(chaserFactory, 
                                         CHASER_BASE_SPAWN_INTERVAL,
                                         CHASER_EXTRA_SPAWN_INTERVAL,
                                         CHASER_MAX);

    }

    /// Initialise flocker enemy manager.
    private void initFlockers() {

        Texture flockerTexture = mTextureManager.getTexture(Config.FLOCKER_TEXTURE_ID);
        Supplier<Enemy> flockerFactory = 
           () -> {
               return new Flocker(flockerTexture, 
                                  grid,
                                  chaserManager);
           };
        flockerManager = new EnemyManager(flockerFactory, 
                                          FLOCKER_BASE_SPAWN_INTERVAL,
                                          FLOCKER_EXTRA_SPAWN_INTERVAL,
                                          FLOCKER_MAX);

    }

    /// Initialise ghost enemy manager.
    private void initGhosts() {

        Texture ghostTexture = mTextureManager.getTexture(Config.GHOST_TEXTURE_ID);
        Supplier<Enemy> ghostFactory = 
           () -> {
               return new Ghost(ghostTexture, grid);
           };
        ghostManager = new EnemyManager(ghostFactory, 
                                        GHOST_BASE_SPAWN_INTERVAL,
                                        GHOST_EXTRA_SPAWN_INTERVAL,
                                        GHOST_MAX);

    }

}
