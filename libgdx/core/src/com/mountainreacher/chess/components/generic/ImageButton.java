package com.mountainreacher.chess.components.generic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 *
 */

public class ImageButton extends Group implements EventListener {

    private ActionListener listener;

    public ImageButton(float r, float g, float b, float a, Texture texture, int width, int height) {
        Image image = new Image(texture);
        texture.getWidth();
    }

    public ImageButton(Texture texture, int width, int height, final ActionListener listener) {
        Image image = new Image(texture);
        image.setWidth(width);
        image.setHeight(height);
        image.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                listener.onActionPerformed(ImageButton.this, null);
                return true;
            }
        });
        addActor(image);
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
