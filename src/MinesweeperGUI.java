/* Made by Aidan
 * 
 */


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

public class MinesweeperGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JPanel topPanel, bottomPanel;
	JLabel label, label2;
	JLabel minesLeft, gameTimer;
	JButton playButton;
	
	sweeperButton buttonArray[];
	Image imageList[];
	
	int buttonTracker[];
	
	public enum ButtonStatus{
		HIDN, REV, FLAG
	}
	
	
	int diffOptions[][] = { {81,10,9}, {256,40,16}, {480,99,30} };
	int difficulty = 2;

	// User Defined
	int numButtons = diffOptions[difficulty][0];
	int gridWidth = diffOptions[difficulty][2];
	int gridHeight = numButtons/gridWidth;
	int numMines = diffOptions[difficulty][1];
	
	int minesFlagged = 0;
	int count2 = 0;
	
	// UI Stuff
//	static Color colorHIDN = new Color(30,144,255);
	static Color colorBackground = new Color( 0.4f ,0.5f ,0.2f );
	static Color colorEmpty = new Color(30,120,255);
	static Color colorHIDN = new Color( 0.3f ,0.7f ,0.3f );
//	static Color colorBackground = new Color(135,206,235);
//	static Color colorBackground = new Color( (int) Long.parseLong("246B61", 16) );
	
	
	MinesweeperGUI( String title ){
		super( title );
		try {
			UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
	    for (UIManager.LookAndFeelInfo look : looks) {
	      System.out.println(look.getClassName());
//	      if( look.getClassName() == "com.sun.java.swing.plaf.motif.MotifLookAndFeel" )
//	    	  UIManager.setInstalledLookAndFeels();
	    }
		
		//try {
//			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//				System.out.println(info.getName());
				//if ("Metal".equals(info.getName())) {UIManager.setLookAndFeel(info.getClassName());}break;}
		//}catch(Exception e){}

		
		setSize( 120, 100);
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		setLayout( new BorderLayout() );
		
		//   thePanel.setLayout(new GridLayout(a,b,c,d));	
		loadImages();
		topPanel = initTopPanel();
		bottomPanel = initBottomPanel();
		

		topPanel.setBackground( colorBackground );
		bottomPanel.setBackground( colorBackground );
		
		getContentPane().add(topPanel, BorderLayout.NORTH);
		getContentPane().add(bottomPanel,BorderLayout.CENTER);

		
		
		pack();
		
	}
	
	void loadImages(){

		imageList = new Image[12];
		
	    try {
	    	imageList[0] = ImageIO.read( getClass().getResource("icons/smile.png") );
	    	imageList[9] = ImageIO.read( getClass().getResource("icons/bomb.png" ));	
	    	imageList[10] =ImageIO.read( getClass().getResource("icons/notBomb.png" ));
	    	imageList[11] = ImageIO.read( getClass().getResource("icons/flag.png" ));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	JPanel initTopPanel(){
		JPanel tPanel = new JPanel();
		
		tPanel.setBorder( BorderFactory.createEmptyBorder(5,5,5,5) );
		tPanel.setLayout( new BorderLayout() );
		
		minesLeft = new JLabel(String.valueOf(numMines));
		gameTimer = new JLabel("0");
				
		playButton = new JButton();
		playButton.addMouseListener(listener2);
		playButton.setPreferredSize( new Dimension(30, 30) );
		playButton.setBackground( colorHIDN );
		Image playImage;
		try {
			playImage = ImageIO.read( getClass().getResource("icons/smile.png") );
			playButton.setIcon( new ImageIcon( playImage) );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		playButton.setBorder( BorderFactory.createEmptyBorder(10,160,10,160) );	
		minesLeft.setBorder( BorderFactory.createEmptyBorder(9,3,9,194) );	
		gameTimer.setBorder( BorderFactory.createEmptyBorder(9,194,9,3) );	
		//playButton.setBorder( BorderFactory.createEmptyBorder(20, 60, 20, 20 ) );
		
		tPanel.add(minesLeft,  BorderLayout.WEST );
		tPanel.add(gameTimer, BorderLayout.EAST );
		tPanel.add(playButton, BorderLayout.CENTER );

		return tPanel;
	}
	
	JPanel initBottomPanel(){
		JPanel bPanel = new JPanel();
		
		bPanel.setBorder(BorderFactory.createEmptyBorder(0,7,7,7)); 
		
		buttonArray = new sweeperButton[numButtons];
		
		for(int i = 0; i < numButtons; i++ ){
			buttonArray[i] = new sweeperButton(i);
			buttonArray[i].setPreferredSize(new Dimension(16, 16) );
			buttonArray[i].setBorder( BorderFactory.createRaisedBevelBorder());
			buttonArray[i].bStat = ButtonStatus.HIDN;  // Constructor set to 0 anyways
			buttonArray[i].isBomb = false;
			buttonArray[i].addMouseListener(listener2);
		}
		
		bPanel.setLayout( new GridLayout(gridHeight, 0) );
		
		for(int i = 0; i < numButtons; i++ ){
			bPanel.add( buttonArray[i] );
		}
		
		return bPanel;
	}
	
	void newGame(){
		int count = 0;
//		int step = 0;
		Random randomGenerator = new Random();
		
//		Generate Bombs 
		 while( count < numMines ){
		 		

		 	int x = randomGenerator.nextInt(numButtons);
		 		
		 	
		 	buttonArray[x].isBomb = true;
		 	
		 	count++;
		}
		
		 minesLeft.setText( String.valueOf( count ) );
//		 System.out.println("Total Bombs: " + count + " /numMines " + numMines );
		 
		 
		/* Generate surrBy */
		 /*
		  * Might be more efficient to generate surrBy when generating bombs,
		  * Instead of calculating each one * 9
		  * Only calculate each bomb * 9
		  * 
		  * Where O(bomb*9 operations)
		  * Opposed to
		  * O(9 * n^2 operations)
		  */
		 
		 for( int i = 0; i < numButtons; i++ ){
			 
     		int surrByCount = 0;
     		boolean farLeft = isFarLeft(i);
     		boolean farRight = isFarRight(i);
     		
//     		if( !isTopRow(i) ){
         		if( isInBounds(i-gridWidth-1) && !farLeft && buttonArray[i - gridWidth - 1].isBomb )
         			surrByCount++;
         		if( isInBounds(i-gridWidth  ) && buttonArray[i - gridWidth + 0].isBomb )
         			surrByCount++;
         		if( isInBounds(i-gridWidth+1) && !farRight && buttonArray[i - gridWidth + 1].isBomb  )
         			surrByCount++;
//     		}
     		if( isInBounds(i-1) && !farLeft && buttonArray[i - 1].isBomb )
     			surrByCount++;
     		if( isInBounds(i+1) && !farRight && buttonArray[i + 1].isBomb )
     			surrByCount++;
//     		if( !isBottomRow(i) ){
         		if( isInBounds(i+gridWidth-1) && !farLeft && buttonArray[i + gridWidth - 1].isBomb )
         			surrByCount++;
         		if( isInBounds(i+gridWidth  ) && buttonArray[i + gridWidth + 0].isBomb )
         			surrByCount++;
         		if( isInBounds(i+gridWidth+1) && !farRight && buttonArray[i + gridWidth + 1].isBomb )
         			surrByCount++;
//     		}
	     		
     		buttonArray[i].surrBy = surrByCount;
		 }//End For	
	 
		updateButtons();
	}
	
	void newGameButtons(){
		
		for(int i = 0; i < numButtons; i++ ){
			buttonArray[i].setPreferredSize(new Dimension(16, 16) );
			buttonArray[i].setBackground( colorHIDN );
			buttonArray[i].setBorder( BorderFactory.createRaisedBevelBorder());

			buttonArray[i].bStat = ButtonStatus.HIDN;  // Constructor set to 0 anyways
			buttonArray[i].isBomb = false;
			buttonArray[i].surrBy = 0;
			
			buttonArray[i].addMouseListener(listener2);
		}
		
		newGame();
		updateButtons();
	}
	
	
	
	
	void revealButtonInit(int buttonPosition){
		buttonTracker = new int[numButtons];
		
		for(int i = 0; i < numButtons; i++){
			buttonTracker[i] = 0;
		}
		
		revealButton(buttonPosition);
//		revealAll();
	}
	
	void revealAll(){
		
		for(int i = 0; i < numButtons; i++){
			buttonArray[i].bStat = ButtonStatus.REV;
		}
		
	}
	
	void revealButton(int buttonPosition){
			 
 		boolean farLeft = isFarLeft(buttonPosition);
 		boolean farRight = isFarRight(buttonPosition);
 		
 		if( buttonTracker[buttonPosition] == 1 || buttonArray[buttonPosition].isBomb ){
 			return;
 		}else if( !isInBounds(buttonPosition) ){
 			return;
 		}else{
 			buttonTracker[buttonPosition] = 1;
 		}

 		/* Do Reveal */
 		buttonArray[buttonPosition].setText(null);
 		buttonArray[buttonPosition].bStat = ButtonStatus.REV;
 		
 		if( buttonArray[buttonPosition].surrBy == 0 ){
 		// Call on neighbors	
 			if( !isTopRow(buttonPosition) ){
	 			if( !farLeft && isInBounds(buttonPosition-gridWidth-1))
	 				revealButton( buttonPosition - gridWidth - 1 );
	     		if( isInBounds(buttonPosition-gridWidth) )
	     			revealButton( buttonPosition - gridWidth );
	     		if( !farRight && isInBounds(buttonPosition-gridWidth+1))
	     			revealButton( buttonPosition - gridWidth + 1 );
	 		}
	 		if( !farLeft && isInBounds(buttonPosition-1))
	 			revealButton( buttonPosition - 1);
	 		if( !farRight && isInBounds(buttonPosition+1))
	 			revealButton( buttonPosition + 1 );
	 		if( !isBottomRow(buttonPosition) ){
	     		if( !farLeft && isInBounds(buttonPosition+gridWidth-1))
	     			revealButton( buttonPosition + gridWidth - 1 );
	     		if( isInBounds(buttonPosition+gridWidth))
	     			revealButton( buttonPosition + gridWidth + 0 );
	     		if( !farRight && isInBounds(buttonPosition+gridWidth+1))
	     			revealButton( buttonPosition + gridWidth + 1 );
	 		}
 	
 		}
	}//end revealButton
	
	
	
	/* For Testing Largely */
	void updateButtons(){
		
		for( int i = 0; i < numButtons; i++ ){
			// Revealed Bomb or Values
			
			if( buttonArray[i].bStat == ButtonStatus.HIDN ){
				buttonArray[i].setBackground( colorHIDN );
				buttonArray[i].setText(null);
				buttonArray[i].setIcon(null);
				buttonArray[i].setBorder( BorderFactory.createRaisedBevelBorder());
			}else if( buttonArray[i].bStat == ButtonStatus.REV && buttonArray[i].isBomb ){
				// Revealed Bomb ----- Only on game over
				buttonArray[i].setIcon( new ImageIcon(imageList[9] ));
			}
			// Or Flagged
			else if( buttonArray[i].bStat == ButtonStatus.FLAG )
//				buttonArray[i].setIcon( new ImageIcon(imageList[11]));
				buttonArray[i].setText( "F" );
			// Not Bomb
			else if( buttonArray[i].bStat == ButtonStatus.REV ){
				if( buttonArray[i].surrBy == 0 )
					buttonArray[i].setBackground( colorEmpty );
				else
					buttonArray[i].setText( String.valueOf( buttonArray[i].surrBy ) );}
				
		}
		
		 gameTimer.setText( String.valueOf( count2 ) );
//		 System.out.println("Total Bombs: " + count2 );

		
		bottomPanel.repaint();
		bottomPanel.revalidate();
	}
	
	
	public static void main( String[] args){
		
		MinesweeperGUI frame = new MinesweeperGUI("Hello");
		
		frame.newGame();
		frame.setVisible(true);
		
	}
		
	MouseListener listener2 = new MouseListener (){

		@Override
		public void mouseClicked(MouseEvent e) {
			
			// if Right Mouse Button
			if( SwingUtilities.isRightMouseButton(e) ){
				if( ((sweeperButton) e.getSource() ).bStat == ButtonStatus.HIDN )
					buttonArray[((sweeperButton) e.getSource()).position].bStat = ButtonStatus.FLAG;
				else if( ((sweeperButton) e.getSource() ).bStat == ButtonStatus.FLAG )
					buttonArray[((sweeperButton) e.getSource()).position].bStat = ButtonStatus.HIDN;					
			}
			else{
					//Else first mouse button
	        	if( e.getSource() == playButton ){
	        		newGameButtons();
	        		return;
	        	}
	        	boolean isBomb = ((sweeperButton) e.getSource() ).isBomb;
	        	//ButtonStatus status = ((sweeperButton) e.getSource() ).bStat;
	        	int currPos = ((sweeperButton) e.getSource() ).position;
	        	
	        	if( !isBomb ){	
	        		revealButtonInit( currPos );
	        	}else{
	        		buttonArray[currPos].bStat = ButtonStatus.REV;
	        		revealAll();
	        		// GAME OVER!!
	        	}
			}

        	updateButtons();
        }

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}		
	};
	
	
		
	ActionListener listener = new ActionListener (){
        @Override

        public void actionPerformed(ActionEvent e) { 	
            if ( e.getSource() instanceof JButton) {
//                String text = String.valueOf( ((sweeperButton) e.getSource()).inbuttonStatus );
//                JOptionPane.showMessageDialog(null, text);
//            	if( SwingUtilities.isRightMouseButton( ) == 3 ){
//            		
//            	}
            
            	if( e.getSource() == playButton ){
            		newGameButtons();
            		return;
            	}
            	
            	boolean isBomb = ((sweeperButton) e.getSource() ).isBomb;
            	//ButtonStatus status = ((sweeperButton) e.getSource() ).bStat;
            	int currPos = ((sweeperButton) e.getSource() ).position;
            	
            	if( !isBomb ){	
            		revealButtonInit( currPos );
            	}else{
            		buttonArray[currPos].bStat = ButtonStatus.REV;
            		revealAll();
            		// GAME OVER!!
            	}
            	updateButtons();
            }
        }
    };

    
	@SuppressWarnings("serial")
	private class sweeperButton extends JButton{
		
		int position;
		ButtonStatus bStat;
		boolean isBomb;
		int surrBy;
		
		sweeperButton(int pos){
			position = pos;
			isBomb = false;
			bStat = ButtonStatus.HIDN;
		}
		
	}
    
	
	boolean isInBounds(int position){
		if( position >= 0 && position < ( numButtons ) )
			return true;
		return false;
	}
	boolean isTopRow(int position){
		return (position < gridWidth );
	}
	boolean isBottomRow(int position){
		return ( ( position + gridHeight ) > numButtons );
	}
	boolean isFarLeft(int position){
		return ( (position % gridWidth ) == 0);
	}
	boolean isFarRight(int position){
		return ( (position % gridWidth ) == ( gridWidth -1 ) );
	}
	
    
}

/*
Old Notes:
	-2) Hidden Bomb
	 * -1) Hidden Nothing
	 * 	0) Revealed 0
	 *  1) Revealed 1
	 *  2) Revealed 2
	 *  3) Revealed 3
	 *  4) Revealed 4
	 *  5) Revealed 5
	 *  6) Revealed 6
	 *  7) Flagged Bomb
	 *  8) Revealed Nothing
	 *  9) Revealed Bomb


Old Bomb-making

    public static void iterBasicTest(Calculator c){
        testRunArray = new testRun[5];

        for( int i = 0; i < 5; i++ ){
            testRunArray[i] = new testRun();
            testRunArray[i].iters = 50;
            doARun( c, testRunArray[i] );
        }

        printTestSet( testRunArray );        
    }

*/

