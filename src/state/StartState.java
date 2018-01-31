package state;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import gameobject.TextObject;
import texture.TextureManager;
import config.Config;

/// State to start game with.
public class StartState extends SceneState {

    /// Text displaying how to start game.
    private final TextObject mText;

    /// Initialise how to start text.
    public StartState(TextureManager textureManager) {

        super(new Context(textureManager));

        int textSize = 25;
        boolean centred = true;
        mText = new TextObject(textSize, centred);
        mText.setText("Press space to start"); 
        mText.translate(Config.WINDOW_WIDTH / 2f, Config.WINDOW_HEIGHT / 2f + 100f);

    }

    /// Render scene and text.
    /// \param core Processing core to render game with.
    public void render(PApplet core) { 

        super.render(core);
        mText.render(core);

    }

    /// Handle key press.
    /// \param key the key that was pressed.
    /// \return the new state.
    public SceneState handleKeyPress(char key) {

        // Start the game by moving into play state when space pressed.
        if (key == ' ') {

            return new PlayState(mContext);

        }
        return this;

    }

}
