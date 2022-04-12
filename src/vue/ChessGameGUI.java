package vue;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import controler.controlerLocal.ChessGameControler;
import model.Coord;
import model.Couleur;
import model.observable.ChessGame;
import tools.ChessImageProvider;
import tools.ChessPiecePos;

 
public class ChessGameGUI extends JFrame implements MouseListener, MouseMotionListener {
  JLayeredPane layeredPane;
  JPanel chessBoard;
  JLabel chessPiece;
  
  ChessGame chess_game;
  
  int xAdjustment;
  int yAdjustment;
  
  ChessGameControler controller;
 
  public ChessGameGUI()
  {
	  chess_game = new ChessGame(); // Vue observe le model
	  
	  Dimension boardSize = new Dimension(600, 600);
	 
	  //  Use a Layered Pane for this this application
	  layeredPane = new JLayeredPane();
	  getContentPane().add(layeredPane);
	  layeredPane.setPreferredSize(boardSize);
	  layeredPane.addMouseListener(this);
	  layeredPane.addMouseMotionListener(this);
	
	  //   Add a chess board to the Layered Pane 
	  chessBoard = new JPanel();
	  layeredPane.add(chessBoard, JLayeredPane.DEFAULT_LAYER);
	  chessBoard.setLayout( new GridLayout(8, 8) );
	  chessBoard.setPreferredSize( boardSize );
	  chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);
	 
	  for (int i = 0; i < 64; i++) 
	  {
		  JPanel square = new JPanel( new BorderLayout() );
		  chessBoard.add( square );
	 
		  int row = (i / 8) % 2;
		  if (row == 0)
			  square.setBackground( i % 2 == 0 ? Color.blue : Color.white );
		  else
			  square.setBackground( i % 2 == 0 ? Color.white : Color.blue );
		}
	 
	  //Add a few pieces to the board
	 
	  for (int i = 0; i < ChessPiecePos.values().length; i++) 
	  {
	
	      Couleur pieceCouleur = ChessPiecePos.values()[i].couleur;
	
	      for (int j = 0; j < (ChessPiecePos.values()[i].coords).length; j++) 
	      {
	                String className = ChessPiecePos.values()[i].nom;     // attention au chemin
	                Coord pieceCoord = ChessPiecePos.values()[i].coords[j];
	                JLabel piece = new JLabel( new ImageIcon(ChessImageProvider.getImageFile(className, pieceCouleur)) );
	                JPanel panel = (JPanel)chessBoard.getComponent(pieceCoord.x + pieceCoord.y*8);
	                panel.add(piece); 
	      }
	    }
	  
	  controller = new ChessGameControler(this.chess_game);
	  }
 
  public void mousePressed(MouseEvent e)
  {
	  System.out.println("Pressed");
	  chessPiece = null;
	  Component c =  chessBoard.findComponentAt(e.getX(), e.getY());
	 
	  if (c instanceof JPanel) {return;}
	  
	  Point parentLocation = c.getParent().getLocation();
	  
	  System.out.println(chess_game.getColorCurrentPlayer());
	  System.out.println(chess_game.getPieceColor(parentLocation.x/75, parentLocation.y/75));
	  
	  if(chess_game.getColorCurrentPlayer() != chess_game.getPieceColor(parentLocation.x/75, parentLocation.y/75)) 
	  {
		  System.out.println("Wrong piece");
		  return;
		  
	  } // Si la piece n'est pas de la bonne couleur, on annule.
	  
	  xAdjustment = parentLocation.x - e.getX();
	  yAdjustment = parentLocation.y - e.getY();
	  
	  chessPiece = (JLabel)c;
	  chessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
	  chessPiece.setSize(chessPiece.getWidth(), chessPiece.getHeight());
	  layeredPane.add(chessPiece, JLayeredPane.DRAG_LAYER);
  }
 
  //Move the chess piece around
  
  public void mouseDragged(MouseEvent me) 
  {
	  if (chessPiece == null) return;
	  chessPiece.setLocation(me.getX() + xAdjustment, me.getY() + yAdjustment);
	  
  }
  
 
  //Drop the chess piece back onto the chess board
 
  public void mouseReleased(MouseEvent e) {
	  if(chessPiece == null) return;
	 
	  chessPiece.setVisible(false);
	  Component c =  chessBoard.findComponentAt(e.getX(), e.getY());
	  
	  Point parentLocation = c.getParent().getLocation();
	 
	  if (c instanceof JLabel){
		  Container parent = c.getParent();
		  
		  parent.remove(0);
		  parent.add( chessPiece );
	  }
	  else {
		  Container parent = (Container)c;
		  parent.add( chessPiece );
	  }
	 
	  chessPiece.setVisible(true);
  }
 
  public void mouseClicked(MouseEvent e) {
  
  }
  public void mouseMoved(MouseEvent e) {
 }
  public void mouseEntered(MouseEvent e){
  
  }
  public void mouseExited(MouseEvent e) {
  
  }
 
  public static void main(String[] args) {
  JFrame frame = new ChessGameGUI();
  frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE );
  frame.pack();
  frame.setResizable(true);
  frame.setLocationRelativeTo( null );
  frame.setVisible(true);
 }
}
