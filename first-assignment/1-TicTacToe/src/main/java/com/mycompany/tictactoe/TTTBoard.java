/*
* TTTBoard
*
* @description: Main class of the application displaying a grid of 3x3 TTTCells,
*               a RESTART button, and a TTTController label. 
*
* @author: m.pinna10@studenti.unipi.it
*/

package com.mycompany.tictactoe;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TTTBoard extends javax.swing.JFrame 
        implements PropertyChangeListener {

    
    private int nClickedCells; /* keep track of used cells */
    
    private final List<TTTCell> TTTCells;
    private final PropertyChangeSupport gameStatePcs;
    
    
    /* Constructor */
    public TTTBoard() {
        this.getContentPane().setBackground(Color.WHITE);
        
        this.nClickedCells = 0;
        this.TTTCells = new ArrayList<>();
        this.gameStatePcs = new PropertyChangeSupport(this);
        
        initComponents();
        initListeners(); /* Initialize all listeners */
   
    }
    
    /**
     * Initialize all the listeners: connect cells, controller and board.
     */
    private void initListeners() {
        this.TTTCells.add(cell1);
        this.TTTCells.add(cell2);
        this.TTTCells.add(cell3);
        this.TTTCells.add(cell4);
        this.TTTCells.add(cell5);
        this.TTTCells.add(cell6);
        this.TTTCells.add(cell7);
        this.TTTCells.add(cell8);
        this.TTTCells.add(cell9);
        
        for (TTTCell c: TTTCells) {
            
            /* Register Controller as veto change listener to each cell */
            c.addVetoableChangeListener(controller);
            
            /* Register Board as property change listener to each cell */
            c.addPropChangeListener(this);
            
            /* Register each cell as property change listener of Board */
            this.addPropChangeListener(c);
        }
        
        /* Register Controller as property change listener of Board*/
        this.addPropChangeListener(controller);
    }
    
    /** Check if game is finished each time a PropertyChangeEvent to a cell 
     * occurrs, fires a corresponding PropertyChange if WINNER or TIE situation.
     * 
     * @param evt The event from a TTTcell.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        List<TTTCell> trisCells = new ArrayList<>();
        nClickedCells++;
        
        /* Check if there is a winner */
        trisCells = checkWinner();
        
        if (trisCells.size() == 3) { /* Someone won */
            nClickedCells = 0;
            controller.setBackground(Color.GREEN);
            gameStatePcs.firePropertyChange("winner", null, trisCells);
        } else if (nClickedCells >= 9) { /* Tie */
            nClickedCells = 0;
            controller.setBackground(Color.ORANGE);
            gameStatePcs.firePropertyChange("tie", null, null);
        }    
    }
    
    /**
     * Utility function for checking is there is a winning trist (horizontal, 
     * vertical or diagonal).
     * 
     * @return List containing the three winning (tris) cells, empty array
     *         otherwise
     */
    private List<TTTCell> checkWinner() {
        
        /* Horizontal (rows) */
        if (isTris(cell1, cell2, cell3)) {
            return Arrays.asList(cell1, cell2, cell3);
        } else if (isTris(cell4, cell5, cell6)) {
            return Arrays.asList(cell4, cell5, cell6);
        } else if (isTris(cell7, cell8, cell9)) {
            return Arrays.asList(cell7, cell8, cell9);
        } 
        /* Vertical (columns) */
        else if (isTris(cell1, cell4, cell7)) {
            return Arrays.asList(cell1, cell4, cell7);
        } else if (isTris(cell2, cell5, cell8)) {
            return Arrays.asList(cell2, cell5, cell8);
        } else if (isTris(cell3, cell6, cell9)) {
            return Arrays.asList(cell3, cell6, cell9);
        } 
        /* Diagonal */
        else if (isTris(cell1, cell5, cell9)) {
            return Arrays.asList(cell1, cell5, cell9);
        } else if (isTris(cell3, cell5, cell7)) {
            return Arrays.asList(cell3, cell5, cell7);
        }
        
        return new ArrayList<>(); /* No winner, return empty array */
    }
    
    /**
     * Utility method for checking if three cells form a tris.
     * 
     * @param c1
     * @param c2
     * @param c3
     * @return True if they form a tris, False otherwise
     */
    private boolean isTris(TTTCell c1, TTTCell c2, TTTCell c3) {
       
        /* Make sure no cell is in INITIAL state */
        if (!c1.getState().equals(TTTCell.CellState.INITIAL)) {
            /* Check if all three either X or O */
            return c1.getState().equals(c2.getState()) 
                && c2.getState().equals(c3.getState());
        }
        return false;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cell1 = new com.mycompany.tictactoe.TTTCell();
        cell2 = new com.mycompany.tictactoe.TTTCell();
        cell3 = new com.mycompany.tictactoe.TTTCell();
        cell4 = new com.mycompany.tictactoe.TTTCell();
        cell5 = new com.mycompany.tictactoe.TTTCell();
        cell6 = new com.mycompany.tictactoe.TTTCell();
        cell7 = new com.mycompany.tictactoe.TTTCell();
        cell8 = new com.mycompany.tictactoe.TTTCell();
        cell9 = new com.mycompany.tictactoe.TTTCell();
        restartButton = new javax.swing.JToggleButton();
        controller = new com.mycompany.tictactoe.TTTController();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TicTacToe");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        restartButton.setBackground(new java.awt.Color(255, 51, 51));
        restartButton.setFont(new java.awt.Font("Calibri Light", 1, 18)); // NOI18N
        restartButton.setText("RESTART");
        restartButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        restartButton.setBorderPainted(false);
        restartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartButtonActionPerformed(evt);
            }
        });

        controller.setBackground(java.awt.Color.orange);
        controller.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        controller.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        controller.setText("START GAME");
        controller.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        controller.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(controller, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(restartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cell4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cell7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cell8, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cell9, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cell5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cell6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cell1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cell2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cell3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(127, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cell2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cell1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cell4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cell5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cell3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cell6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cell9, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cell8, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cell7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(controller, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(restartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addPropChangeListener(PropertyChangeListener listener) {
        gameStatePcs.addPropertyChangeListener(listener);
    }
    
    private void removePropChangeListener(PropertyChangeListener listener) {
        gameStatePcs.removePropertyChangeListener(listener);
    }
    
    private void restartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartButtonActionPerformed
        gameStatePcs.firePropertyChange("restart", null, null);
    }//GEN-LAST:event_restartButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TTTBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TTTBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TTTBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TTTBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TTTBoard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.mycompany.tictactoe.TTTCell cell1;
    private com.mycompany.tictactoe.TTTCell cell2;
    private com.mycompany.tictactoe.TTTCell cell3;
    private com.mycompany.tictactoe.TTTCell cell4;
    private com.mycompany.tictactoe.TTTCell cell5;
    private com.mycompany.tictactoe.TTTCell cell6;
    private com.mycompany.tictactoe.TTTCell cell7;
    private com.mycompany.tictactoe.TTTCell cell8;
    private com.mycompany.tictactoe.TTTCell cell9;
    private com.mycompany.tictactoe.TTTController controller;
    private javax.swing.JToggleButton restartButton;
    // End of variables declaration//GEN-END:variables
   
}
