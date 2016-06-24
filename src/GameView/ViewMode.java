/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameView;

import java.awt.image.BufferedImage;

/**
 * The skeleton for various game modes.
 * <p>Many of the game platforms I'm familiar with, such as the Gameboy Advance
 * and the Super Nintendo Entertainment System, utilize one of several "modes",
 * in order to provide a variety of options. Typically, these modes are
 * simply different ways of allocating precious CPU time, since these systems
 * contain only one core, with very limited processing power. Some examples
 * of these modes include:
 * <ul>
 * <li>Mode 0 (GBA): Four tile-based layers, which may translated, but not 
 * rotated or scaled.</li>
 * <li>Mode 1 (GBA): Three tile-based layers, which may be translated. Layer two
 * may be rotated or scaled.</li>
 * <li>Mode 2 (GBA): Two tile-based layers, both of which may be translated,
 * rotated, or scaled.</li>
 * <li>Mode 3 (GBA): One bitmap layer, which may be rotated or scaled, but not
 * translated.</li>
 * <li>Mode 4 (GBA): Two bitmap layers, with a limited pallete, which may be
 * rotated or scaled, but not translated.</li>
 * <li>Mode 5 (GBA): Two bitmap layers, which may be rotated or scaled, but not
 * translated. These layers are smaller than the screen itself.</li>
 * <li>Mode 7 (SNES): One tile-based layer, which has an affine transformation
 * matrix attached to it. Any combination of translations, rotations, skewing,
 * scaling, or reflections can be accomplished. An additional tiled layer
 * remains static.
 * </ul>
 * <p>These modes are all highly varied, but they accomplish one thing: creating
 * the frame. This interface provides the ability for any user-created mode to
 * provide its frame to the view.
 * @author Justis
 */
public interface ViewMode
{
    /**
     * Get the current frame.
     * This method is called every time the view needs the next frame. The
     * ViewMode can calculate this on the fly, or it can pre-calculate the frame
     * and simply push it on forward.
     * @return The final frame to be displayed.
     */
    BufferedImage getFrame();
}
