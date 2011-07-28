/*
 * @(#)OSXButtonStateBorder.java  
 * 
 * Copyright (c) 2011 Werner Randelshofer, Immensee, Switzerland.
 * All rights reserved.
 * 
 * You may not use, copy or modify this file, except in compliance with the
 * license agreement you entered into with Werner Randelshofer.
 * For details see accompanying license terms.
 */
package ch.randelshofer.quaqua;

import ch.randelshofer.quaqua.border.ImageBevelBorder;
import javax.swing.JComponent;
import ch.randelshofer.quaqua.border.BackgroundBorder;
import ch.randelshofer.quaqua.border.FocusedBorder;
import ch.randelshofer.quaqua.osx.OSXAquaPainter;
import ch.randelshofer.quaqua.util.CachedPainter;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;
import static ch.randelshofer.quaqua.osx.OSXAquaPainter.*;

/**
 * Native Aqua border for an {@code AbstractButton).
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class QuaquaNativeTextFieldBorder extends VisualMargin implements Border, BackgroundBorder {

    private OSXAquaPainter painter;
    private Insets imageInsets;
    // private Insets borderInsets;
    private Border backgroundBorder;
    private ImageBevelBorder imageBevelBorder;
    private final static int ARG_TEXT_FIELD = 2;
    private final static int ARG_SMALL_SIZE = 32;

    private class BGBorder extends CachedPainter implements Border {

        public BGBorder() {
            super(12);
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            JTextComponent b = (JTextComponent) c;
            //ButtonModel bm = b.getModel();

            int args = 0;
            State state;
            if (QuaquaUtilities.isOnActiveWindow(c)) {
                state = State.active;
                args |= 1;
            } else {
                state = State.inactive;
            }


            Widget widget;
            if (isSearchField(b)) {
                widget = Widget.frameTextFieldRound;
            } else {
                args |= ARG_TEXT_FIELD;
                widget = Widget.frameTextField;
            }


            painter.setWidget(widget);

            if (!b.isEnabled()) {
                state = State.disabled;
                args |= 4;
            }
            painter.setState(state);

            boolean isFocused = QuaquaUtilities.isFocused(c);
            args |= (isFocused) ? 16 : 0;
            painter.setValueByKey(OSXAquaPainter.Key.focused, isFocused ? 1 : 0);

            Size size;
            if (QuaquaUtilities.isSmallSizeVariant(b)) {
                size = Size.small;
                args |= ARG_SMALL_SIZE;
//            } else if (QuaquaUtilities.isLargeSizeVariant(b)) {
//                size = Size.large;
//                args |= 64;
            } else {
                size = Size.regular;
            }
            painter.setSize(size);

            paint(c, g, x, y, width, height, args);
        }

        @Override
        protected Image createImage(Component c, int w, int h,
                GraphicsConfiguration config) {

            return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);

        }

        @Override
        protected void paintToImage(Component c, Image img, int w, int h, Object argsObj) {
            int args = (Integer) argsObj;
            if ((args & ARG_TEXT_FIELD) == ARG_TEXT_FIELD) {
                // => Okay: this is a hard nut to crack.
                // The painter can not render text fields in arbitrary sizes.
                // We render it first into an ImageBevelBorder, and then onto the
                // image.
                BufferedImage ibbImg;
                boolean isShow = false;
                int fixedHeight, fixedYOffset;
                if ((args & ARG_SMALL_SIZE) == ARG_SMALL_SIZE) {
                    fixedHeight = 19;
                    fixedYOffset = 3;
                } else {
                    fixedHeight = 22;
                    fixedYOffset = 3;
                }

                if (imageBevelBorder == null) {

                    ibbImg = new BufferedImage(40, fixedHeight, BufferedImage.TYPE_INT_ARGB_PRE);
                    imageBevelBorder = new ImageBevelBorder(ibbImg, new Insets(4, 4, 4, 4), new Insets(0, 0, 0, 0));
                } else {
                    ibbImg = (BufferedImage) imageBevelBorder.getImage();
                    if (ibbImg.getHeight() != fixedHeight) {
                        ibbImg = new BufferedImage(40, fixedHeight, BufferedImage.TYPE_INT_ARGB_PRE);
                        imageBevelBorder.setImage(ibbImg);
                    }
                }
                Graphics2D ibbg = (Graphics2D) ibbImg.getGraphics();
                ibbg.setColor(new Color(0x0, true));
                ibbg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
                ibbg.fillRect(0, 0, ibbImg.getWidth(), ibbImg.getHeight());
                ibbg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                ibbg.setColor(c.getBackground());
                ibbg.setColor(Color.MAGENTA);
                ibbg.fillRect(0, 0, ibbImg.getWidth(), ibbImg.getHeight());
                ibbg.dispose();
                painter.paint(ibbImg,//
                        0, fixedYOffset,//
                        ibbImg.getWidth(), ibbImg.getHeight());
                // Now render the imageBevelBorder
                Graphics2D ig = (Graphics2D) img.getGraphics();
                ig.setColor(new Color(0x0, true));
                ig.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
                ig.fillRect(0, 0, img.getWidth(null), img.getHeight(null));
                ig.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                imageBevelBorder.paintBorder(c, ig,//
                        imageInsets.left, imageInsets.top,//
                        w - imageInsets.left - imageInsets.right, //
                        h - imageInsets.top - imageInsets.bottom);
                ig.dispose();

            } else {
                Graphics2D ig = (Graphics2D) img.getGraphics();
                ig.setColor(new Color(0x0, true));
                ig.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
                ig.fillRect(0, 0, img.getWidth(null), img.getHeight(null));
                ig.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                ig.dispose();


                painter.paint((BufferedImage) img,//
                        imageInsets.left, imageInsets.top,//
                        w - imageInsets.left - imageInsets.right, //
                        h - imageInsets.top - imageInsets.bottom);
            }
        }

        @Override
        protected void paintToImage(Component c, Graphics g, int w, int h, Object args) {
            // empty
        }

        public Border getBackgroundBorder() {
            if (backgroundBorder == null) {
                backgroundBorder = new BGBorder();
            }
            return backgroundBorder;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(0, 0, 0, 0);
        }

        public boolean isBorderOpaque() {
            return false;
        }
    }

    public QuaquaNativeTextFieldBorder(OSXAquaPainter.Widget widget) {
        this(widget, new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), true);
    }

    public QuaquaNativeTextFieldBorder(OSXAquaPainter.Widget widget, Insets imageInsets, Insets borderInsets, boolean fill) {
        super(new Insets(0, 0, 0, 0));
        painter = new OSXAquaPainter();
        painter.setWidget(widget);
        this.imageInsets = imageInsets;
        //this.borderInsets = borderInsets;
    }

    private boolean isSearchField(JComponent b) {
        Object variant =
                b.getClientProperty("Quaqua.TextField.style");

        if (variant == null) {
            variant = b.getClientProperty("JTextField.variant");
        }
        return variant != null && variant.equals("search");
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        // empty
    }

    public Border getBackgroundBorder() {
        if (backgroundBorder == null) {
            this.backgroundBorder = new FocusedBorder(new BGBorder());
        }
        return backgroundBorder;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        Insets vm = getVisualMargin(c);

        if (isSearchField((JComponent) c)) {
            insets = UIManager.getInsets("TextField.searchBorderInsets");
        } else if (QuaquaUtilities.isSmallSizeVariant(c)) {
            insets = UIManager.getInsets("TextField.smallBorderInsets");
        } else {
            insets = UIManager.getInsets("TextField.borderInsets");
        }
        //InsetsUtil.addTo(vm,insets);
/*
        if (c instanceof JTextComponent) {
            Insets margin = ((JTextComponent) c).getMargin();
            if (margin != null) {
                InsetsUtil.addTo(margin, insets);
            }
        }*/

        return insets;
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    public static class UIResource extends QuaquaNativeTextFieldBorder implements javax.swing.plaf.UIResource {

        public UIResource(OSXAquaPainter.Widget widget) {
            super(widget);
        }

        /**
         * Creates a new instance.
         * All borders must have the same dimensions.
         */
        public UIResource(OSXAquaPainter.Widget widget, Insets imageInsets, Insets borderInsets, boolean fill) {
            super(widget, imageInsets, borderInsets, fill);
        }
    }
}
