package code;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EventHandler implements ActionListener{
	
	private Model _model;
	private int _i;
	private int _j;
	
	public EventHandler(int i, int j, Model m){
		_model = m;
		_i = i;
		_j = j;
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		_model.swap(_i, _j);
		_model.buttonSelected(_i, _j);
		_model.valueReset();
        _model.matchClear();

	}

}
