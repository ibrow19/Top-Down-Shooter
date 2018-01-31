import processing.core.PApplet;
import processing.core.PVector;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;
import scene.Scene;
import texture.TextureManager;
import texture.Texture;
import rect.Rect;
import config.Config;

/// Class to initialise game and handle processing main loop.
public class Game extends PApplet {

    // Constants.
    private static final int FPS = 60;
    private static final int UPDATE_RATE = 120;
    private static final float STEP_SIZE = 1f / UPDATE_RATE;

    /// Accumulated time since last frame.
    private float mAccumulator;

    /// The time at the start of the frame.
    private float mStartTime;

    /// The game scene to update and draw.
    private Scene mScene;

    /// Use this class for processing main loop.
    public static void main(String[] args) {

        PApplet.main("Game");

    }

    /// Initialise screen size settings.
    public void settings() {

        size(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);

    }

    /// Setup the game by loading images and initialising the scene.
    public void setup() {

        // Set frame rate and start time.
        frameRate(FPS);
        mStartTime = millis() / 1000f;

        TextureManager tManager = new TextureManager();

        // Attempt to load and initialise textures, exit on failure.
        try {

            // Load ship texture.
            Texture ship = new Texture(this, "ship.png");
            tManager.addTexture(Config.SHIP_TEXTURE_ID, ship);

            // Load turret texture.
            Texture turret = new Texture(this, "turret.png");
            tManager.addTexture(Config.TURRET_TEXTURE_ID, turret);

            // Load laser texture.
            Texture laser = new Texture(this, "laser.png");
            addClips(laser, 0, 3, 12, 50);
            tManager.addTexture(Config.LASER_TEXTURE_ID, laser);

            // Load power up texture.
            Texture powerup = new Texture(this, "powerup.png");
            addClips(powerup, 0, 4, 45, 45);
            tManager.addTexture(Config.POWERUP_TEXTURE_ID, powerup);

            // Load asteroid texture.
            Texture asteroid = new Texture(this, "asteroid.png");
            tManager.addTexture(Config.ASTEROID_TEXTURE_ID, asteroid);

            // Load enemy textures.
            Texture random = new Texture(this, "random.png");
            tManager.addTexture(Config.RANDOM_TEXTURE_ID, random);
            Texture chaser = new Texture(this, "chaser.png");
            tManager.addTexture(Config.CHASER_TEXTURE_ID, chaser);
            Texture flocker = new Texture(this, "flocker.png");
            tManager.addTexture(Config.FLOCKER_TEXTURE_ID, flocker);
            Texture ghost = new Texture(this, "ghost.png");
            tManager.addTexture(Config.GHOST_TEXTURE_ID, ghost);

            mScene = new Scene(tManager);

        } catch (Exception e) {

            e.printStackTrace();
            exit();

        }

    }

    /// Draw the game.
    public void draw() {

        // Calculate time since last frame.
        float currentTime = millis()/1000f;
        float frameTime = currentTime - mStartTime;

        mStartTime = currentTime;
        mAccumulator += frameTime;

        mScene.updateMousePos(new PVector(mouseX, mouseY)); 

        // While there is still enough time left in accumulator update the
        // game with the fixed timestep.
        while (mAccumulator > STEP_SIZE) {

            mScene.update(STEP_SIZE); 
            mAccumulator -= STEP_SIZE;

        }

        // Clear screen then render current game state.
        background(0);
        mScene.render(this);

    }

    /// Handle mouse press event.
    public void mousePressed() {

        // Delegate handling to scene.
        mScene.handleMousePress(mouseButton, new PVector(mouseX, mouseY));

    }

    /// Handle mouse release event.
    public void mouseReleased() {

        // Delegate handling to scene.
        mScene.handleMouseRelease(mouseButton, new PVector(mouseX, mouseY));

    }

    /// Handle key pressed event.
    public void keyPressed() {

        // Delegate handling to scene.
        mScene.handleKeyPress(key);

    }

    /// Handle key released event.
    public void keyReleased() {

        // Delegate handling to scene.
        mScene.handleKeyRelease(key);

    }

    /// Add contiguous clips of a set size to a texture starting at specified index.
    /// \param texture the texture to add clips to.
    /// \param startIndex the index to start adding clips at.
    /// \param clips the number of clips to add.
    /// \param width the width of each clip to add.
    /// \param height the height of each clip to add.
    private void addClips(Texture texture, int startIndex, int clips, int width, int height) {

        for (int i = startIndex; i < clips; ++i) {

            texture.addClip(new Rect(i * width, 0, width, height));

        }

    }

}
