// A class representing a single node in a linked list  with getters and setters
public class SongNode{
	private String path, title, artist;
	private SongNode next;
	
	public SongNode(String path, String title, String artist, SongNode next) {
		this.path = path;
		this.title = title;
		this.artist = artist;
		this.next = next;
	}
	
	public SongNode(String path, String title, String artist) {
		this.path = path;
		this.title = title;
		this.artist = artist;
		next = null;
	}

	public String path() {
	      return path;
	}
	
	public void setpath(String inpath) {
		path = inpath;
	}
	
	public String title() {
	      return title;
	}
	
	public void setTitle(String inTitle) {
		title = inTitle;
	}
	
	public String artist() {
	      return artist;
	}
	
	public void setArtist(String inArtist) {
		artist = inArtist;
	}
	
	public SongNode next() {
		return next;
	}
	
	public void setNext(SongNode other) {
		next = other;
	}
	
	public int compareString(SongNode other){
		return this.title().compareTo(other.title());
	}
}
