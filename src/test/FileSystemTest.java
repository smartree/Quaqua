/*
 * @(#)FileSystemTest.java  1.0  February 27, 2006
 *
 * Copyright (c) 2006 Werner Randelshofer, Immensee, Switzerland.
 * All rights reserved.
 *
 * You may not use, copy or modify this file, except in compliance with the
 * license agreement you entered into with Werner Randelshofer.
 * For details see accompanying license terms.
 */

package test;

import ch.randelshofer.quaqua.osx.OSXFile;
import java.awt.Color;
import java.io.*;
import javax.swing.*;
import ch.randelshofer.quaqua.filechooser.*;
import javax.swing.text.*;
/**
 * FileSystemTest.
 *
 * @author  Werner Randelshofer
 * @version 1.0 February 27, 2006 Created.
 */
public class FileSystemTest extends javax.swing.JPanel {
    
    /**
     * Creates a new instance.
     */
    public FileSystemTest() {
        initComponents();
        
        test();
    }
    
    private void test() {
        // StringBuffer buf = new StringBuffer();
        DefaultStyledDocument buf = new DefaultStyledDocument();
        try {
            boolean canWorkWithAliases = OSXFile.canWorkWithAliases();
            buf.insertString(buf.getLength(), "can work with aliases="+canWorkWithAliases, null);
            SimpleAttributeSet a;
            if (canWorkWithAliases) {
                //File dir = new File("/System/Library/Frameworks/JavaVM.framework/Versions");
                File dir = new File(System.getProperty("user.home")+"/Desktop");
                File[] files = dir.listFiles();
                for (int i=0; i < files.length; i++) {
                    File f = files[i];
                    buf.insertString(buf.getLength(),"\n",null);
                    buf.insertString(buf.getLength(),"\n",null);
                    a = new SimpleAttributeSet();
                    StyleConstants.setIcon(a, new ImageIcon(OSXFile.getIconImage(f, 32)));
                    buf.insertString(buf.getLength(),"icon",a);
                    a = new SimpleAttributeSet();
                    StyleConstants.setBold(a, true);
                    buf.insertString(buf.getLength(),"\t"+f.toString(),a);
                    buf.insertString(buf.getLength(),"\n\tlabel=",null);
                    buf.insertString(buf.getLength(),Integer.toString(OSXFile.getLabel(f)),null);
                    buf.insertString(buf.getLength(),", is alias=",null);
                    int fileType = OSXFile.getFileType(f);
                    boolean isAlias = fileType == OSXFile.FILE_TYPE_ALIAS;
                    buf.insertString(buf.getLength(),""+isAlias,null);
                    if (isAlias) {
                        File resolved = OSXFile.resolveAlias(f, true);
                        if (resolved == null) {
                            buf.insertString(buf.getLength(),", can't resolve this alias without user interaction",null);
                        } else {
                            buf.insertString(buf.getLength(),", resolved=",null);
                            buf.insertString(buf.getLength(),resolved.toString(),null);
                            buf.insertString(buf.getLength(),", type=",null);
                            buf.insertString(buf.getLength(),Integer.toString(OSXFile.resolveAliasType(f, true)),null);
                        }
                    }
                    
                    buf.insertString(buf.getLength(),"\n\tkind=",null);
                    buf.insertString(buf.getLength(),OSXFile.getKindString(f),null);
                }
            }
        } catch (Throwable t) {
            CharArrayWriter caw = new CharArrayWriter();
            PrintWriter w = new PrintWriter(caw);
            t.printStackTrace(w);
            w.close();
            try {
                buf.insertString(buf.getLength(),new String(caw.toCharArray()),null);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
        
        textField.setDocument(buf);
    }
    
    public static void main(String[] args) {
        
        JFrame f = new JFrame("Native Test");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FileSystemTest nt = new FileSystemTest();
        f.getContentPane().add(nt);
        f.setSize(400,400);
        f.setVisible(true);
        nt.test();
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        textField = new javax.swing.JTextPane();

        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(textField);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane textField;
    // End of variables declaration//GEN-END:variables
    
}
