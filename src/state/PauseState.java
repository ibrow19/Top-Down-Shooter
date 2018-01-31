package state;

import processing.core.PApplet;
import processing.core.PConstants;
import gameobject.TextObject;
import config.Config;

/// Paused game state.
public class PauseState extends StatusState {

    /// Text stating the game is paused.
    private final TextObject mText;

    /// Initialise text.
    public PauseState(Context context) {

        super(context);
        
        int textSize = 25;
        boolean centred = true;
        mText = new TextObject(textSize, centred);
        mText.setText("Paused"); 
        mText.translate(Config.WINDOW_WIDTH / 2f, Config.WINDOW_HEIGHT / 2f);

    }

    /// Handle key press.
    /// \param key the key that was pressed.
    /// \return the new state.
    public SceneState handleKeyPress(char key) {

        // Return to play state when p pressed.
        if (key == 'p') {

            return new PlayState(mContext);

        }
        return this;

    }

    /// Render the game and paused text.
    /// \param core processing core to render game with.
    public void render(PApplet core) {

        super.render(core);
        mText.render(core);

    }

}
