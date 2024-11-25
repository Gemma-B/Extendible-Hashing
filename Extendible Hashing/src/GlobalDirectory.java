import java.util.ArrayList;
public class GlobalDirectory {
	public ArrayList<Bucket> globalDirectory = new ArrayList<Bucket>();
	public int globalBitDepth = 0;
	
	/**
	 * Creating a global directory
	 * @param bitDepth the size of the bit depth
	 */
	public GlobalDirectory(int bitDepth) {
		globalBitDepth = bitDepth;
		
		Bucket firstBucket = new Bucket(0, "");
		globalDirectory.add(firstBucket);
	}
	
	/**
	 * A function that inserts a key into the correct local directory
	 * @param key the key we want to insert
	 */
	public void insertKey(String key) {
		
		// make sure key does not exist in our directory
		if (searchKey(key) == -1) {
			System.out.println("UHOh");
			for(int i = 0; i < globalDirectory.size(); i++) {
				System.out.println(globalDirectory.get(i).getBitPattern()); 
				// if this bucket starts with the same pattern, then we can insert our key :)
				if (globalDirectory.get(i).getBitPattern().equals(key.substring(0, globalBitDepth))) {
					System.out.println("DID IT");
					boolean needToRehash = !globalDirectory.get(i).insertKey(key); // true if we need to rehash
					
					// if insert returns false, then we need to rehash
					if (needToRehash) {
						rehash(key);
						System.out.println("SUCCESS");
					} else {
						globalDirectory.get(i).insertKey(key);
						System.out.println("SUCCESS");
					}
				}
			}
		} else {
			System.out.println("FAILED");
		}
	}
	
	/**
	 * Searches to see if a given key is already in our directory
	 * @param key what key we're searching for
	 * @return the index of the directory a key is contained in, and -1 if the key doesn't exist 
	 */
	public int searchKey(String key) {
		for(int i = 0; i < globalDirectory.size(); i++) {
			
			// if this bucket starts with the same pattern, then we can search this bucket for the key
			if (globalDirectory.get(i).getBitPattern().equals(key.substring(0, globalBitDepth))) {
				
				// if the local search found the key, then its found!
				if (globalDirectory.get(i).searchKey(key)) {
					return i;
				}
			}
		}
		
		// if we get here, then the key was never found :(
		return -1;
	}
	
	/**
	 * A method that rehashes a given bucket into two, and increases global bit depth if needed
	 * @param key the key that tells us what bucket needs to be rehashed
	 */
	public void rehash(String key) {
		
		// finding the index of the local directory that needs to be rehashed
		int rehashedIndex = searchKey(key);
		
		if (rehashedIndex != -1) {
			// storing the keys that must be rehashed once new local directories are created
			String[] keysToBeRehashed = globalDirectory.get(rehashedIndex).getKeys();
	
			// the new directory size must be the old directory size, plus 1
			int newLocalDepth = globalDirectory.get(rehashedIndex).getBitPattern().length() + 1;
			
			// checking to see if we need to update globalDirectory size
			if (newLocalDepth > globalBitDepth) {
				globalBitDepth = newLocalDepth;
				
			}
			// creating two new buckets with the new directory size, and the old bit pattern with a 0 or a 1 attached to it
			Bucket bucket1 = new Bucket(newLocalDepth, globalDirectory.get(rehashedIndex).getBitPattern() + "0");
			Bucket bucket2 = new Bucket(newLocalDepth, globalDirectory.get(rehashedIndex).getBitPattern() + "1");
			
			// adding our new buckets to the global directory right next to our old bucket
			globalDirectory.set(rehashedIndex, bucket1);
			globalDirectory.add(rehashedIndex, bucket2);
			
			// recursively inserting all keys from the old bucket into the new buckets
			for(int i = 0; i < keysToBeRehashed.length; i++) {
				insertKey(keysToBeRehashed[i]);
			}
		} else {
			
		}
		
		//TODO: replace arrays with arraylists to make insertion easier? 
		// rehash local array into two arrays 
		// if local bit depth is greater than global bit depth then increase global bit depth
	}
	
	public String toString() {
		String str = "Global(" + globalBitDepth + ")\n";
		for(int i = 0; i < globalDirectory.size(); i++) {
			str += globalDirectory.toString() +"\n";
 		}
		return str;
	}
}
