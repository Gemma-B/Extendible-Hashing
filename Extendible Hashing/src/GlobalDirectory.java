import java.util.HashMap;
import java.util.TreeMap;

public class GlobalDirectory {
	public HashMap<String, Bucket> globalDirectory = new HashMap<String, Bucket>();
	public int globalBitDepth = 0;
	public int bucketSize;
	
	
	public GlobalDirectory(int bucketSize) {
		// creating first bucket
		this.bucketSize = bucketSize;
		Bucket firstBucket = new Bucket(0, "" , bucketSize);
		globalDirectory.put("", firstBucket);
	}
	
	/**
	 * Trying to rehash our directory
	 * @param bitPatternToRehash the pattern of the bucket we need to rehash
	 */
	public void rehash(String bitPatternToRehash) {
		// global bit depth is increased by one
		globalBitDepth++;
		
		// creating new temporary hashmap
		HashMap<String, Bucket> tempDirectory = new HashMap<String, Bucket>();

		// looping through every key in the current global directory
		for (String key : globalDirectory.keySet()) {
		
			// creating a new bucket thats the same as the current bucket in the directory
			Bucket bucket = globalDirectory.get(key);
			
			// checking if this bucket needs to be rehashed
			if (key.startsWith(bitPatternToRehash)) {
				
				// our new bit depth is one more than our old bit depth
				int newBitDepth = globalDirectory.get(bitPatternToRehash).getBitDepth() + 1;
				Bucket bucket0 = new Bucket(newBitDepth, bitPatternToRehash + "0", this.bucketSize);
				Bucket bucket1 = new Bucket(newBitDepth, bitPatternToRehash + "1", this.bucketSize);
				
				// getting the keys we need to rehash
				String[] relocateKeys = globalDirectory.get(bitPatternToRehash).getKeys();
				
				// looping through every key and figuring out which bucket it belongs in, out of the two options
				for (String s : relocateKeys) {
					String bitPattern = s.substring(0, newBitDepth);
					
					// all of our keys need to be relocated to one of two new buckets
					if (bitPattern.endsWith("0")){
						bucket0.insertKey(s);
					} else {
						bucket1.insertKey(s);
					}
				}
				// inserting both of our buckets into the temporary directory
				tempDirectory.put(bitPatternToRehash + "0", bucket0);
				tempDirectory.put(bitPatternToRehash + "1", bucket1);
				
			} else {
				// bucket doesn't need to be rehashed
				if (bucket.getBitDepth() < globalBitDepth) {
					// keys needs to be split, but they still point to the same bucket
					tempDirectory.put(key + "0", bucket);
					tempDirectory.put(key + "1", bucket);
					
				} else {
					// bucket is good to go! just insert it directly
					tempDirectory.put(key, bucket);
				}
			}
		}
		globalDirectory = tempDirectory;
	}
	
	/**
	 * A function that inserts a key into the correct local directory
	 * @param key the key we want to insert
	 */
	public boolean insertKey(String k) {
		
		// key does not already exist
		if (searchKey(k) == -1) {
			for(String key: this.globalDirectory.keySet()) {

				// if the two bit patterns agree with each other
				if (key.equals(k.substring(0, key.length()))) {

					if (!globalDirectory.get(key).insertKey(k)) {
						String rehashBitPattern = globalDirectory.get(key).getBitPattern(); // the bit pattern of the bucket that needs to be rehashed
						rehash(rehashBitPattern);
						insertKey(k);
					} else {
						globalDirectory.get(key).insertKey(k);
					}
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Searches to see if a given key is already in our directory
	 * @param key what key we're searching for
	 * @return the index of the directory a key is contained in, and -1 if the key doesn't exist 
	 */
	public int searchKey(String k) {
		
		for(String key: this.globalDirectory.keySet()) {
			
			// if this bucket starts with the same pattern, then we can search this bucket for the key
			if (globalDirectory.get(key).getBitPattern().equals(k.substring(0, globalDirectory.get(key).getBitDepth()))) {
				// if the local search found the key, then its found!
				if (globalDirectory.get(key).searchKey(k)) {
					return 1;
				}
			}
		}
		
		// if we get here, then the key was never found :(
		return -1;
	}
	
	public String toString() {
		// sorting our keys in a tree map
		TreeMap<String, Bucket> sortedList = new TreeMap<>();
		sortedList.putAll(globalDirectory);
		
		String str = "Global(" + globalBitDepth + ")\n";
		
		// iterating over the tree map so we have a sorted print statement :)
		for (String key: sortedList.keySet()) {
			str += key + ": " + "Local(" + globalDirectory.get(key).getBitDepth() + ")[" + globalDirectory.get(key).getBitPattern()
					+ "*] = ["; 
			String[] valuesInArray = globalDirectory.get(key).getKeys();
			for (int i = 0; i < valuesInArray.length - 1; i++) {
				str += valuesInArray[i] + ", ";
			}
			
			str += valuesInArray[valuesInArray.length - 1] + "]\n";
		}
		
		return str;
	}
}
