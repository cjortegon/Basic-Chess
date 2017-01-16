package com.mountainreacher.chess.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 *
 */

public class SoundsManager {

    public static final String[] GAME_SOUNDS = {
            "sounds/pop.mp3",
            "sounds/achievement.mp3",
            "sounds/wrong2.mp3",
            "sounds/good.mp3"
    };

    private Sound sounds[];

    public SoundsManager(String[] paths) {
        sounds = new Sound[paths.length];
        for (int i = 0; i < paths.length; i++)
            sounds[i] = Gdx.audio.newSound(Gdx.files.internal(paths[i]));
    }

    public void playSound(int soundIndex) {
        sounds[soundIndex].play();
    }

}
