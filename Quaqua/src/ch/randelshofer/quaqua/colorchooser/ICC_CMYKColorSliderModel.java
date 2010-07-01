/*
 * @(#)ICC_CMYKColorSliderModel.java  
 *
 * Copyright (c) 2005-2010 Werner Randelshofer
 * Hausmatt 10, Immensee, CH-6405, Switzerland.
 * All rights reserved.
 *
 * The copyright of this software is owned by Werner Randelshofer. 
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * Werner Randelshofer. For details see accompanying license terms. 
 */
package ch.randelshofer.quaqua.colorchooser;

import java.awt.*;
import java.awt.color.*;
import java.io.*;
import java.util.Arrays;
import javax.swing.*;

/**
 * A ColorSliderModel for CMYK color models (cyan, magenta, yellow, black) in
 * a color space defined by a ICC color profile (International Color Consortium).
 * <p>
 * XXX - This does not work. I think this is because of 
 * Java bug #4760025 at
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4760025
 * but maybe I am doing something in the wrong way.
 * 
 *
 * @author  Werner Randelshofer
 * @version $Id$
 */
public class ICC_CMYKColorSliderModel extends ColorSliderModel {

    private ICC_ColorSpace colorSpace;
    float[] cmyk = new float[4];
    float[] rgb = new float[3];

    /**
     * Creates a new instance.
     */
    public ICC_CMYKColorSliderModel() {
        super(new DefaultBoundedRangeModel[]{
                    new DefaultBoundedRangeModel(0, 0, 0, 100),
                    new DefaultBoundedRangeModel(0, 0, 0, 100),
                    new DefaultBoundedRangeModel(0, 0, 0, 100),
                    new DefaultBoundedRangeModel(0, 0, 0, 100)
                });
        try {
            read(getClass().getResourceAsStream("Generic CMYK Profile.icc"));
        } catch (IOException e) {
            InternalError err = new InternalError("Couldn't load \"Generic CMYK Profile.icc\".");
            err.initCause(e);
            throw err;
        }

    }

    /**
     * Creates a new instance.
     */
    public ICC_CMYKColorSliderModel(InputStream iccProfile) throws IOException {
        super(new DefaultBoundedRangeModel[]{
                    new DefaultBoundedRangeModel(0, 0, 0, 100),
                    new DefaultBoundedRangeModel(0, 0, 0, 100),
                    new DefaultBoundedRangeModel(0, 0, 0, 100),
                    new DefaultBoundedRangeModel(0, 0, 0, 100)
                });
        read(iccProfile);
    }

    public void read(InputStream iccProfile) throws IOException {
        this.colorSpace = new ICC_ColorSpace(ICC_Profile.getInstance(iccProfile));
    }

    public int getRGB() {
        cmyk[0] = components[0].getValue() / 100f;
        cmyk[1] = components[1].getValue() / 100f;
        cmyk[2] = components[2].getValue() / 100f;
        cmyk[3] = components[3].getValue() / 100f;
        rgb = colorSpace.toRGB(cmyk);
        return 0xff000000 | ((int) (rgb[0] * 255f) << 16) | ((int) (rgb[1] * 255f) << 8) | (int) (rgb[2] * 255f);
    }

    public void setRGB(int newRGB) {
        rgb[0] = ((newRGB & 0xff0000) >>> 16) / 255f;
        rgb[1] = ((newRGB & 0x00ff00) >>> 8) / 255f;
        rgb[2] = (newRGB & 0x0000ff) / 255f;
        cmyk = colorSpace.fromRGB(rgb);

        components[0].setValue((int) (cmyk[0] * 100f));
        components[1].setValue((int) (cmyk[1] * 100f));
        components[2].setValue((int) (cmyk[2] * 100f));
        components[3].setValue((int) (cmyk[3] * 100f));
        rgb = colorSpace.toRGB(cmyk);
    }

    public int toRGB(int[] values) {
        cmyk[0] = values[0] / 100f;
        cmyk[1] = values[1] / 100f;
        cmyk[2] = values[2] / 100f;
        cmyk[3] = values[3] / 100f;
        rgb = colorSpace.toRGB(cmyk);
        return 0xff000000 | ((int) (rgb[0] * 255f) << 16) | ((int) (rgb[1] * 255f) << 8) | (int) (rgb[2] * 255f);
    }

    @Override
    public Color getColor() {
        cmyk[0] = components[0].getValue() / 100f;
        cmyk[1] = components[1].getValue() / 100f;
        cmyk[2] = components[2].getValue() / 100f;
        cmyk[3] = components[3].getValue() / 100f;
        return new Color(colorSpace, cmyk, 1f);
    }

    @Override
    public void setColor(Color color) {
        if (color.getColorSpace().equals(colorSpace)) {
            cmyk = color.getColorComponents(cmyk);
        } else {
            cmyk = color.getColorComponents(colorSpace, cmyk);
        }
        components[0].setValue((int) (cmyk[0] * 100f));
        components[1].setValue((int) (cmyk[1] * 100f));
        components[2].setValue((int) (cmyk[2] * 100f));
        components[3].setValue((int) (cmyk[3] * 100f));
    }
}
