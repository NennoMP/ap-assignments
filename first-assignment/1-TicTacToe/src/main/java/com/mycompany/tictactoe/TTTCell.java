/*
* TTTCell
*
* @description: Single cell of TicTacToe table, one button for each player 
                (X and O)

* @author: m.pinna10@studenti.unipi.it
*/

package com.mycompany.tictactoe;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.util.List;


public class TTTCell extends javax.swing.JPanel 
        implements PropertyChangeListener {

    /** 
    * Represents state of a cell:
    *   - INITIAL: cell has not been clicked
    *   - X: cell clicked by player X
    *   - O: cell clicked by player O
    *   - TRISX: cell belong to a winning tris of player X
    *   - TRISO: cell belongs to a winning tris of player O
    */
    public enum CellState {
        INITIAL,
        X,
        O,
        TRISX,
        TRISO;
    }
    
    /* Bound & Constrained property */
    private CellState cellState = CellState.INITIAL;
    
    /* Utility class for supporting bound property (cellState) */
    private final PropertyChangeSupport cellStatePcs = 
            new PropertyChangeSupport(this);
    
    /* Utility class for supporting constrained property (cellState) */
    private final VetoableChangeSupport cellStateVcs = 
            new VetoableChangeSupport(this);
    
    /**
     * Constructor
     */
    public TTTCell() {
        initComponents();
    }
    
    /**
     * Getter method for the cell state.
     * 
     * @return The state of the cell
     */
    public CellState getState() {
        return cellState;
    }
    
    /**
     * Change cell state according to event-based communication received.
     * 
     * @param evt The PropertyChangeEvenet object, containing information about
     *            the game state
     *            - restart: set all cells' state to INIT
     *            - winner: set winning cells to TRIS, disable others
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals("restart")) {
            cellState = CellState.INITIAL;
            toggleButtons(true);
            setButton(buttonX, Color.LIGHT_GRAY, "X");
            setButton(buttonO, Color.LIGHT_GRAY, "O");
        } else if (evt.getPropertyName().equals("winner")) {
            List<TTTCell> winnerCells = (List<TTTCell>) evt.getNewValue();
            
            /* Check if this is a winning cell */
            if (winnerCells.indexOf(this) >= 0) {
                
                /* Check which player won */
                if (cellState.equals(CellState.X)) {
                    buttonX.setBackground(Color.GREEN);
                    this.cellState = CellState.TRISX;
                } else if (cellState.equals(CellState.O)) {
                    buttonO.setBackground(Color.GREEN);
                    this.cellState = CellState.TRISO;
                }
            } else {
                toggleButtons(false); /* Disable non-winning cells */
            }
        }
    }
    
    /**
     * Enable/Disable buttons (X and O)
     * 
     * @param b boolean value to which to set buttons' enabled property
     */
    private void toggleButtons(boolean b) {
        buttonX.setEnabled(b);
        buttonO.setEnabled(b);
    }
    
    /**
     * Update a button text and background color
     * 
     * @param button The JButton to be updated
     * @param color The color to which to set the button's background
     * @param txt The text to display in the button
     */
    private void setButton(javax.swing.JButton button, Color color, String txt) {
        button.setText(txt);
        button.setBackground(color);
    }
    
    /**
     * Method for modifying a cell state after a player move, each attempt
     * fire a VetoableChange to check that players are alternating correctly, 
     * if valid a PropertyChange is fired as well
     * 
     * @param newCellState New state to which to set the cell
     */
    public void applyPlayerAction(CellState newCellState) {
        CellState oldCellState = this.cellState;
        
        /* If cell is a winning one, it cannot be modified */
        if (cellState.equals(CellState.TRISX) 
                || cellState.equals(CellState.TRISO)) {
            return;
        }
        
        try {
            /* Fire VetoableChange to listener (Controller) */
            cellStateVcs.fireVetoableChange("cellState", oldCellState, 
                                            newCellState);
            
            /* No veto, apply changes depending on which player clicked */
            this.cellState = newCellState;
            if (this.cellState.equals(CellState.X)) {
                buttonX.setBackground(Color.YELLOW);
                buttonO.setEnabled(false);
            } else if (this.cellState.equals(CellState.O)) {
                buttonO.setBackground(Color.BLUE);
                buttonX.setEnabled(false);
            }
            
            /* Fire PropertyChange to listener (Board) */
            cellStatePcs.firePropertyChange("cellState", oldCellState, 
                                            newCellState);
        } catch (PropertyVetoException e) {
            System.err.println(e.getMessage() + "Move vetoed!");
        }
    }

    public void addVetoableChangeListener(VetoableChangeListener l) {
        cellStateVcs.addVetoableChangeListener(l);
    } 
    
    public void removeVetoableChangeListener(VetoableChangeListener l) {
        cellStateVcs.removeVetoableChangeListener(l);
    }
    
    public void addPropChangeListener(PropertyChangeListener l) {
        cellStatePcs.addPropertyChangeListener(l);
    }
    
    public void removePropChangeListener(PropertyChangeListener l) {
        cellStatePcs.removePropertyChangeListener(l);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonX = new javax.swing.JButton();
        buttonO = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.blue));
        setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        setMinimumSize(new java.awt.Dimension(0, 0));
        setPreferredSize(new java.awt.Dimension(150, 150));

        buttonX.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        buttonX.setText("X");
        buttonX.setToolTipText("");
        buttonX.setBorder(null);
        buttonX.setHideActionText(true);
        buttonX.setMaximumSize(new java.awt.Dimension(9, 19));
        buttonX.setMinimumSize(new java.awt.Dimension(9, 19));
        buttonX.setPreferredSize(new java.awt.Dimension(9, 19));
        buttonX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonXActionPerformed(evt);
            }
        });

        buttonO.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        buttonO.setText("O");
        buttonO.setToolTipText("");
        buttonO.setBorder(null);
        buttonO.setMaximumSize(new java.awt.Dimension(11, 19));
        buttonO.setMinimumSize(new java.awt.Dimension(11, 19));
        buttonO.setPreferredSize(new java.awt.Dimension(11, 19));
        buttonO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonO, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(buttonX, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(buttonO, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonXActionPerformed
        applyPlayerAction(CellState.X);
    }//GEN-LAST:event_buttonXActionPerformed

    private void buttonOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOActionPerformed
        applyPlayerAction(CellState.O);
    }//GEN-LAST:event_buttonOActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonO;
    private javax.swing.JButton buttonX;
    // End of variables declaration//GEN-END:variables
}
