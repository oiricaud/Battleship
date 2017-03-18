package edu.utep.cs.cs4330.battleship;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;


public class Font {
    private String fontPath;

    public Font(String path){
        setFontPath(path);
    }
    public void changeFont(Context context, TextView textView){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), getFontPath());
        textView.setTypeface(typeface);
    }

    public String getFontPath() {
        return fontPath;
    }

    public void setFontPath(String fontPath) {
        this.fontPath = fontPath;
    }
}
