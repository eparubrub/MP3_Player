public class SongList {
	private SongNode head, tail;

	public SongList() {
		head = null;
		tail = null;
	}

	// Inserts the new node with the given element at the front of the list 
	public void insertAtFront(String path, String title, String artist) {
		SongNode newSongNode = new SongNode(path, title, artist);
		if (head != null) {	
			newSongNode.setNext(head);
		}
		else {
			tail = newSongNode;
		}
		head = newSongNode;
	}
	
	// Inserts the new node with the given element at the back of the list
	public void append(String path, String title, String artist) {
		SongNode newSongNode = new SongNode(path,title,artist);
		if (tail != null) {
			tail.setNext(newSongNode);
			tail = newSongNode;
		} 
		else {
			head =  tail = newSongNode;
		}
		
	}
	
	//inserts the SongNode into place based on title
	public void insertInPlace(String path, String title, String artist){
		SongNode newSongNode = new SongNode(path,title,artist);
		SongNode curr = head;
		if (head == null && tail == null) {	
			head = tail = newSongNode;
		}
		else if (head == tail){
			
			if (newSongNode.compareString(curr) < 0){
				insertAtFront(path, title, artist);
			}
			else{
				append(path, title, artist);
			}
		}
		else if (newSongNode.compareString(curr) < 0){
			insertAtFront(path, title, artist);
		}
		else{
			while  (curr.next()!= null){
				if (newSongNode.compareString(curr.next()) > 0){
					curr = curr.next();
				}
				else if (newSongNode.compareString(curr.next()) <= 0){
					newSongNode.setNext(curr.next());
					curr.setNext(newSongNode);
					break;
				}
			}
			if (curr.next() == null) {
				append(path, title, artist);	
			}
		}
	}
	
	//Prints all nodes in a SongNode list
	public void printNodes() {
		SongNode curr = head;
		while (curr != null) {
			System.out.println(curr.path() + " " + curr.title() + " " + curr.artist());
			curr = curr.next();
		}
	}
	
	//returns the length of a songList
	public int getListLength() {
		SongNode curr = head;
		int length = 0;
		while (curr != null) {
			length = length + 1;
			curr = curr.next();
		}
		return length;
	}
	
	//returns a 2d string of the data values of the nodes in a list
	public String[][] getDataValues(){
		String dataValues[][] = new String[getListLength()][2];
		SongNode curr = head;
		int j = 0;
		while (curr != null && j<getListLength()) {
			int i = 0;
			dataValues[j][i] = curr.title();
			i++;
			dataValues[j][i] = curr.artist();
			j++;
			curr = curr.next();	
		}
		return dataValues;
	}
	
	//returns a string of the path of a songNode in a list
	//by using a given int
	public String getPathWithInt(int songInt){
		SongNode curr = head;
		int i = 0;
		while(i < songInt){
			System.out.println(curr.title());
			curr = curr.next();
			i++;
		}
		return curr.path();
	}
	
	//clears a given list
	public void clearList(){
		if (head == null){
			return;
		}
		else{
			head = tail = null;
		}
	}
	
	//adds the songNodes in the list
	//only with the string s
	public void searchedSongs(SongList list, String s){
		SongNode curr = list.head;
		while (curr != null){
			if ((s != "") && curr.title().startsWith(s)){
				this.insertInPlace(curr.path(), curr.title(), curr.artist());
			}
			curr = curr.next();
		}
	}
}


