/*
* TTTController
*
* @description: Displays the current state of the game on a label, and ensures
*               the correct flow of the game (players alternate correctly when
*               clicking a cell).
*
* @author: m.pinna10@studenti.unipi.it
*/

package com.mycompany.tictactoe;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.List;


public class TTTController extends javax.swing.JLabel 
        implements VetoableChangeListener, PropertyChangeListener {

    /** 
    * Represents state of the game:
    *   - INITIAL: no cell has been clicked yet
    *   - XTURN: it's player X turn
    *   - OTURN: it's player O turn
    *   - XWON: player X won
    *   - OWON: player O won
    *   - TIE: no more cells available, no winner
    */
    public enum GameState {
        INITIAL("START GAME", Color.ORANGE),
        XTURN("NEXT MOVE: X", Color.YELLOW),
        OTURN("NEXT MOVE: O", Color.BLUE),
        XWON("THE WINNER IS: X", Color.GREEN),
        OWON("THE WINNER IS: O", Color.GREEN),
        TIE("IT'S A TIE", Color.ORANGE);
        
        private final String text;
        private final Color color;
        
        private GameState(String s, Color color) {
            this.text = s;
            this.color = color;
        }
        
        public String getText() {
            return text;
        }
        
        public Color getColor() {
            return color;
        }
    }

    private GameState gameState = GameState.INITIAL;
    
    /** 
     * Listener for vetoing at each TTTCell state change attempt, ensures that 
     * player takes turns correctly.
     * 
     * @param evt
     * @throws PropertyVetoException If the change attempt wasn't valid
     */
    @Override
    public void vetoableChange(PropertyChangeEvent evt) 
            throws PropertyVetoException {
        
        TTTCell.CellState cellState = (TTTCell.CellState) evt.getNewValue();
        
        /* Beginning of the game, doesn't matter who moves */
        if (gameState.equals(GameState.INITIAL)) {

            if (cellState.equals(TTTCell.CellState.X)) {
                setGameState(GameState.OTURN);
            } else if (cellState.equals(TTTCell.CellState.O)) {
                setGameState(GameState.XTURN);
            }
        } else { /* Mid-game move, need to check if it is valid */
            
            if (gameState.equals(GameState.XTURN)) {
                if (cellState.equals(TTTCell.CellState.O)) {
                    throw new PropertyVetoException(
                            "Not player O turn! Next move is player X.", evt);
                }
                setGameState(GameState.OTURN);
            }
            else if (gameState.equals(GameState.OTURN)) {
                if (cellState.equals(TTTCell.CellState.X)) {
                    throw new PropertyVetoException(
                            "Not player X turn! Next move is player O.", evt);
                }
                setGameState(GameState.XTURN);
            }
        }
    }
    
    /**
     * Listener for changing the game state, in case of a restart, a tie or a 
     * winner, according to messages from the Board.
     * 
     * @param evt The event object containing the action/functionality 
     *              - restart
     *              - tie
     *              - winner
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        List<TTTCell> winnerCells;
        TTTCell.CellState winner;
        
        switch (evt.getPropertyName()) {
            case "restart":
                setGameState(GameState.INITIAL);
                break;
            case "tie":
                setGameState(GameState.TIE);
                break;
            case "winner":
                winnerCells = (List<TTTCell>) evt.getNewValue();
                winner = winnerCells.get(0).getState();
                
                /* Check which player won */
                if (winner.equals(TTTCell.CellState.TRISX)) {
                    setGameState(GameState.XWON);
                } else if (winner.equals(TTTCell.CellState.TRISO)) {
                    setGameState(GameState.OWON);
                }   break;
            default:
                break;
        }
    }
    
    /**
     * Setter method for the game state and displayed text.
     * 
     * @param newState The new state to be set.
     */
    private void setGameState(GameState newState) {
        this.gameState = newState;
        setText(this.gameState.getText());
        setBackground(this.gameState.getColor());
    }
}
