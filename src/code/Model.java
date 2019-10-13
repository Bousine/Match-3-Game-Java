package code;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

import javax.swing.BorderFactory;

public class Model {
	
	private Application _app;
	private Random _rand;
	private ArrayList<String> _icons;
	private ArrayList<ArrayList<String>> _gridCurrentValues;
	private HashSet<HashSet<Point>> _allMatches;
	private HashSet<Point> _all;
	private int _m;
	private int _n;
	private int _count;
	private int _top;
	
	public Model(){

		_count = 0;
		_m = 100;
		_n = 100;
		_rand = new Random();
		_icons = new ArrayList<String>();
		_gridCurrentValues = new ArrayList<ArrayList<String>>();
		_allMatches = new HashSet<HashSet<Point>>();
		_top = 0;
		_all = new HashSet<Point>();
		
		_icons.add("Tile-0.png");
		_icons.add("Tile-1.png");
		_icons.add("Tile-2.png");
		_icons.add("Tile-3.png");
		_icons.add("Tile-4.png");
		_icons.add("Tile-5.png");	
		
		for(int i = 0; i < 5; i++){
			ArrayList<String> gridRow = new ArrayList<String>();
			_gridCurrentValues.add(gridRow);
		}
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				_gridCurrentValues.get(i).add(j, null);
			}
		}
		shuffle();
		matchClear();

	}	
	
	public void swap(int i, int j){
		if(!( _m == 100 && _n == 100)){
			if(((_m == i + 1 || _m == i -1) && _n == j) || ((_n == j + 1 || _n == j -1) && _m == i)){
				String s = new String(getIcon(i, j));
				_gridCurrentValues.get(i).set(j, _gridCurrentValues.get(_m).get(_n));
				_gridCurrentValues.get(_m).set(_n, s);
				
				Point p = new Point(i, j);
				Point q = new Point(_m, _n);
				
				HashSet<Point> possibleMatches1 = new HashSet<Point>();
				possibleMatches1 = maximalMatchedRegion(p, _gridCurrentValues);
				
				HashSet<Point> possibleMatches2 = new HashSet<Point>();
				possibleMatches2 = maximalMatchedRegion(q, _gridCurrentValues);
				
				if(! (inCol(p, possibleMatches1) || inRow(p, possibleMatches1) || inCol(q, possibleMatches2) || inRow(q, possibleMatches2))){

					_gridCurrentValues.get(_m).set(_n, _gridCurrentValues.get(i).get(j));
					_gridCurrentValues.get(i).set(j, s);
					
				}
			    
				_app.update();
			}
		}
	}
	
	public void shuffle(){
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				_gridCurrentValues.get(i).set(j, _icons.get(_rand.nextInt(_icons.size())));
			}
		}
	}
	
	public void addObserver(Application a){
		_app = a;
	}
	
	public String getIcon(int i, int j){
		return _gridCurrentValues.get(i).get(j);
	}
	
	public void buttonSelected(int i, int j){
		_app.getButton(i, j).setBorder(BorderFactory.createLineBorder(Color.RED, 3));
		_m = i;
		_n = j;
	}
	
	public void valueReset(){
		_count++;
		if(_count % 2 == 0){
			_m = 100;
			_n = 100;
			for(int i = 0; i < 5; i++){
				for(int j = 0; j < 5; j++){
					_app.getButton(i, j).setBorder(null);
				}
			}
		}
	}
	
	public void matchClear(){
		_allMatches = new HashSet<HashSet<Point>>();
		_all = new HashSet<Point>();
		boolean b = true;
		if (_count % 2 == 0) {
			while (b) {
				for (int i = 0; i < _gridCurrentValues.size(); i++) {
					for (int j = 0; j < _gridCurrentValues.get(0).size(); j++) {
						Point tile = new Point(i, j);
						HashSet<Point> adjMatches = new HashSet<Point>();
						HashSet<Point> validMatches = new HashSet<Point>();
						adjMatches = maximalMatchedRegion(tile, _gridCurrentValues);
						if (adjMatches.size() >= 3) {
							validMatches = trimmedRegion(adjMatches);
							_allMatches.add(validMatches);
						}
					}
				}
				for (HashSet<Point> Set : _allMatches) {
					for (Point p : Set) {

						_all.add(p);

					}
				}

				b = hasMatch(_all);

				if (!_all.isEmpty()) {
					for (Point p : _all) {
						_gridCurrentValues.get(p.x).set(p.y, _icons.get(_rand.nextInt(_icons.size())));
					}
				}
				if (_app != null) {
					_app.update();
				}
                if(! _all.isEmpty()) {dropTiles();}
				_allMatches.clear();
				_all.clear();

						   
				if (_app != null) {
					_app.update();
				}

			} 
		}

	}
	
	private boolean hasMatch(HashSet<Point> test){
		return !test.isEmpty();
	}
	
	public void pullToTop(Point p){
		int j = p.y;
		for(int i = p.x; i > _top; i--){
			Point q = new Point(i, j);
			switchUpward(q);
		}
	}
	
	public void switchUpward(Point p){
		String tmp = _gridCurrentValues.get(p.x).get(p.y);
		_gridCurrentValues.get(p.x).set(p.y, _gridCurrentValues.get(p.x - 1).get(p.y));
		_gridCurrentValues.get(p.x - 1).set(p.y, tmp);
	}
	

	
	public void dropTiles(){
        _top = 0;
		for(int j = 0; j < _gridCurrentValues.get(0).size(); j++){
			_top = 0;
			for(int i = 0; i < _gridCurrentValues.size(); i++){
				Point test = new Point(i, j);
				if(_all.contains(test)){
					pullToTop(test);
					_top++;
				}
				
			}
		}		
	} 
	
	private HashSet<Point> trimmedRegion(HashSet<Point> in) {
		HashSet<Point> out = new HashSet<Point>();
		for (Point p : in) {
			if (inRow(p,in) || inCol(p,in)) {
				out.add(p);
			}
		}
		return out;
	}
	private boolean inCol(Point p, HashSet<Point> in) {
		return ( in.contains(new Point(p.x,p.y-2)) && in.contains(new Point(p.x,p.y-1)))
		|| ( in.contains(new Point(p.x,p.y-1)) && in.contains(new Point(p.x,p.y+1)))
		|| ( in.contains(new Point(p.x,p.y+1)) && in.contains(new Point(p.x,p.y+2)));
	}
	private boolean inRow(Point p, HashSet<Point> in) {
		return ( in.contains(new Point(p.x-2,p.y)) && in.contains(new Point(p.x-1,p.y)))
		|| ( in.contains(new Point(p.x-1,p.y)) && in.contains(new Point(p.x+1,p.y)))
		|| ( in.contains(new Point(p.x+1,p.y)) && in.contains(new Point(p.x+2,p.y)));
	}
	
	public HashSet<HashSet<Point>> partition(ArrayList<ArrayList<String>> board) {
		HashSet<HashSet<Point>> parts = new HashSet<HashSet<Point>>();
		if(board != null){
			for(int i = 0; i < board.size(); i++){
				for(int j = 0; j < board.get(0).size(); j++){
					HashSet<Point> part = new HashSet<Point>();
					Point p = new Point(i, j);
					part = maximalMatchedRegion(p, board);
					parts.add(part);
				}
			}
		}
		return parts;
	}
	
	
	public HashSet<Point> maximalMatchedRegion(Point p, ArrayList<ArrayList<String>> board) {
		HashSet<Point> result = new HashSet<Point>();
		ArrayList<Point> candidates = new ArrayList<Point>();
		if (p != null) {
			candidates.add(p);
			
			Point check;
			while(!candidates.isEmpty()){
				p = candidates.get(0);
				
				
				check = new Point(p.x-1,p.y); if (inBounds(check, board) && matches(p,check,board)) {
					candidates.add(check);
					}
				check = new Point(p.x+1,p.y); if (inBounds(check, board) && matches(p,check,board)) { 
					candidates.add(check);
					}
				check = new Point(p.x,p.y-1); if (inBounds(check, board) && matches(p,check,board)) { 
					candidates.add(check); 
					}
				check = new Point(p.x,p.y+1); if (inBounds(check, board) && matches(p,check,board)) {
					candidates.add(check);
					}
				result.add(transferFrom(candidates));
				candidates.removeAll(result);
			}
		}
		return result;
	}

	private boolean matches(Point p, Point q, ArrayList<ArrayList<String>> _board) {
		return _board.get(p.x).get(p.y).equals(_board.get(q.x).get(q.y));
	}
	
	private boolean inBounds(Point p, ArrayList<ArrayList<String>> _board) {
		return p.x >=0 && p.x < _board.size() && p.y >= 0 && p.y < _board.get(0).size();
	}
	
	private Point transferFrom(ArrayList<Point> l){
		Point a = l.get(0);
		l.remove(0);
		return a;
	}
	

	
}



