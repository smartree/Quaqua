/*
 * @(#)BrowserTest.java  1.0  13 February 2005
 *
 * Copyright (c) 2004 Werner Randelshofer, Immensee, Switzerland.
 * All rights reserved.
 *
 * You may not use, copy or modify this file, except in compliance with the
 * license agreement you entered into with Werner Randelshofer.
 * For details see accompanying license terms.
 */

package test;

import ch.randelshofer.quaqua.DefaultBrowserCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

/**
 * BrowserTest.
 *
 * @author  Werner Randelshofer
 * @version 1.0  13 February 2005  Created.
 */
public class BrowserTest extends javax.swing.JPanel {
    
    /** Creates new form. */
    public BrowserTest() {
        initComponents();
        browser1.setModel(createDefaultTreeModel());
        browser2.setModel(createDefaultTreeModel());
        browser2.setCellRenderer(new DefaultBrowserCellRenderer());
    }

protected static TreeModel createDefaultTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("JBrowser");
        DefaultMutableTreeNode parent;
         DefaultMutableTreeNode node;

        parent = new DefaultMutableTreeNode("colors");
        root.add(parent);
        parent.add(createSequence("blue"));
        parent.add(createSequence("violet"));
        parent.add(createSequence("red"));
        parent.add(createSequence("yellow"));

        parent = new DefaultMutableTreeNode("sports");
        root.add(parent);
        parent.add(createSequence("basketball"));
        parent.add(createSequence("soccer"));
        parent.add(createSequence("football"));
        parent.add(createSequence("hockey"));

        parent = new DefaultMutableTreeNode("food");
        root.add(parent);
        parent.add(createSequence("hotdogs"));
        parent.add(createSequence("pizza"));
        parent.add(createSequence("ravioli"));
        parent.add(createSequence("bananas"));
        return new DefaultTreeModel(root);
    }

    private static DefaultMutableTreeNode createSequence(String str) {
        DefaultMutableTreeNode root=new DefaultMutableTreeNode(""+str.charAt(0));
        DefaultMutableTreeNode node=root;
        for (int i=1;i<str.length();i++) {
            node.add(node=new DefaultMutableTreeNode(""+str.charAt(i)));
        }
        return root;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        scrollPane1 = new javax.swing.JScrollPane();
        browser1 = new ch.randelshofer.quaqua.JBrowser();
        scrollPane2 = new javax.swing.JScrollPane();
        browser2 = new ch.randelshofer.quaqua.JBrowser();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 17, 17, 17));
        setLayout(new java.awt.GridBagLayout());

        scrollPane1.setViewportView(browser1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(scrollPane1, gridBagConstraints);

        scrollPane2.setViewportView(browser2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(14, 0, 0, 0);
        add(scrollPane2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ch.randelshofer.quaqua.JBrowser browser1;
    private ch.randelshofer.quaqua.JBrowser browser2;
    private javax.swing.JScrollPane scrollPane1;
    private javax.swing.JScrollPane scrollPane2;
    // End of variables declaration//GEN-END:variables
    
}
