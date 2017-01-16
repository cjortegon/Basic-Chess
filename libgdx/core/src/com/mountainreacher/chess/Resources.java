package com.mountainreacher.chess;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 *
 */

public class Resources {

    public static final Resources INSTANCE = new Resources();

    private AssetManager manager;

    public Resources() {
        this.manager = new AssetManager();
    }

    public void gc() {
    }

    public Texture getTexture(String path) {
        manager.load(path, Texture.class);
        manager.finishLoading();
        return manager.get(path, Texture.class);
    }

    public Texture getFilledCircle(float r, float g, float b, float a, int radius) {
        Pixmap pixmap = new Pixmap(radius * 2, radius * 2, Pixmap.Format.RGBA4444);
        pixmap.setColor(r, g, b, a);
        pixmap.fillCircle(radius, radius, radius);
        return new Texture(pixmap);
    }

    public Texture getFilledBox(int width, int height, float r, float g, float b, float a) {
        Pixmap pixmap = new Pixmap(width, height, a == 1 ? Pixmap.Format.RGB888 : Pixmap.Format.RGBA4444);
        pixmap.setColor(r, g, b, a);
        pixmap.fill();
        return new Texture(pixmap);
    }

    public Texture getFilledBox(int width, int height, int color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA4444);
        pixmap.setColor(color);
        pixmap.fill();
        return new Texture(pixmap);
    }

    public Texture getCircleDrawing(float r, float g, float b, float a, int radius, int length) {
        Pixmap pixmap = new Pixmap(radius * 2 + length * 2, radius * 2 + length * 2, Pixmap.Format.RGBA4444);
        pixmap.setColor(r, g, b, a);
        for (int i = -length; i <= length; i++) {
            pixmap.drawCircle(radius + length, radius + length, radius + i);
        }
        return new Texture(pixmap);
    }

    public TextButton.TextButtonStyle getTextButtonStyle(Image background, BitmapFont font, int pressedOffsetX, int pressedOffsetY) {
        TextButton.TextButtonStyle button = new TextButton.TextButtonStyle();
        button.up = background.getDrawable();
        button.pressedOffsetX = 1;
        button.pressedOffsetY = -1;
        button.font = font;
        return button;
    }

}
