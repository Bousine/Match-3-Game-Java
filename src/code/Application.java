package code;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.ImageIcon;

public class Application implements Runnable {
	
    private Model _model;
    private JButton _button;
    private ArrayList<ArrayList<JButton>> _buttons;
    
    public Application(){
        _model = new Model();
        _buttons = new ArrayList<ArrayList<JButton>>();
    }
    
    public static void main(String [] args){
        SwingUtilities.invokeLater(new Application());
    }
    
    @Override
    public void run(){
    	JFrame window = new JFrame("Asif Hasan's Lab 10");
    	window.setLayout(new GridLayout(5, 5));
    	
		for(int i = 0; i < 5; i++){
			ArrayList<JButton> buttonRow = new ArrayList<JButton>();
			_buttons.add(buttonRow);
		}
    	
    	for(int i = 0; i < 5; i++){
    		for(int j = 0; j < 5; j++){
    			JButton _button = new JButton();
    			_buttons.get(i).add(j, _button);
    			window.add(_button);
    			_button.addActionListener(new EventHandler(i, j, _model));
    			
    		}
    	}
    	
    	_model.addObserver(this);
    	update();

        
        window.pack();
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
        
    public JButton getButton(int i, int j){
    	return _buttons.get(i).get(j);
    } 
    
    public void update(){
    	for(int i = 0; i < 5; i++){
    		for(int j = 0; j < 5; j++){
    			_buttons.get(i).get(j).setIcon(new ImageIcon("Images/" + _model.getIcon(i, j)));
    		}
    	}
    }

}
