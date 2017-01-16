package com.mountainreacher.chess.components.generic;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import com.mountainreacher.chess.Resources;

/**
 *
 */

public class ColorTextButton extends TextButton implements EventListener {

    private static final String TAG = "ColorTextButton";

    private ActionListener listener;

    public ColorTextButton(String text, BitmapFont font, float width, float height,
                           float r, float g, float b, float a) {
        super(text, Resources.INSTANCE.getTextButtonStyle(
                new Image(Resources.INSTANCE.getFilledBox((int) width, (int) height, r, g, b, a)),
                font, (int) (width / 200), (int) (-height / 100)
        ));
        getLabel().setWrap(true);
        setTouchable(Touchable.enabled);
        addListener(this);
    }

    public ColorTextButton(String text, BitmapFont font, float width, float height, int color) {
        super(text, Resources.INSTANCE.getTextButtonStyle(
                new Image(Resources.INSTANCE.getFilledBox((int) width, (int) height, color)),
                font, 1, -1
        ));
        getLabel().setWrap(true);
        setTouchable(Touchable.enabled);
        addListener(this);
    }

    public void setActionListener(ActionListener listener) {
        this.listener = listener;
        setTouchable(Touchable.enabled);
        addListener(this);
    }

    @Override
    public boolean handle(Event e) {
        if (!(e instanceof InputEvent)) return false;
        InputEvent event = (InputEvent) e;
        switch (event.getType()) {
            case touchDown:
                if (listener != null) {
                    listener.onActionPerformed(this, null);
                }
                break;
        }
        return false;
    }
}
