public class Bucket {
	private int localBitDepth;
	private String bitPattern;
	private String[] keysInBucket; //array or arraylist? I think array b/c there will be a set amount of available spaces

	/**
	 * A method that creates a bucket with a given bit depth and pattern
	 * @param bitDepth the bit depth of the given bucket
	 * @param bitPattern the bit pattern associated with the given bucket
	 */
	public Bucket(int bitDepth, String bitPattern, int bucketSize) {
		this.localBitDepth = bitDepth;
		this.bitPattern = bitPattern;
		keysInBucket = new String[bucketSize];
	}
	
	/**
	 * A method that inserts a given key into keysInBucket
	 * @param key the key to insert
	 * @return true if insertion was done, false if it was not
	 */
	public boolean insertKey(String key) {
		// if the search returns false, try to insert the key
		if (!searchKey(key)) {
			for(int i = 0; i < keysInBucket.length; i++) {
			    if(keysInBucket[i] == null) {
			    	
			    	// there is an empty bucket, and we can insert the key
			    	keysInBucket[i] = key;
			        return true;
			    }
			}
		}

		// key is already in the bucket or the bucket is full and requires a rehash
		return false;
	}
	
	/**
	 * A method that searches for a given key inside keysInBucket
	 * @param key the key to search for
	 * @return true if key is found, false if it is not found
	 */
	public boolean searchKey(String key) {
		for (String i : keysInBucket) {
			if ((i != null) && i.equals(key)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Getter function to get the bit pattern
	 * @return the string of the bit pattern
	 */
	public String getBitPattern() {
		return bitPattern;
	}
	
	/**
	 * Setter function to set a new bit pattern
	 * @param newPattern the new bit pattern
	 */
	public void setBitPattern(String newPattern) {
		bitPattern = newPattern;
	}
	
	public int getBitDepth() {
		return localBitDepth;
	}
	
	public void setBitDepth(int newBitDepth) {
		localBitDepth = newBitDepth; 
	}
	/**
	 * Getter function to get all keys in the bucket
	 * @return the array of keys in the bucket
	 */
	public String[] getKeys() {
		return keysInBucket;
	}
	
	/**
	 * A method to clear keys
	 * @param bucketSize
	 */
	public void clearKeys(int bucketSize) {
		keysInBucket = new String[bucketSize];
	}
	
	/**
	 * Prints a record of the local bucket information
	 */
	public String toString() {
		String str = bitPattern + ": Local(" + localBitDepth + ")[" + bitPattern + "*] = [";
		for (int i = 0; i < keysInBucket.length - 1; i++) {
			str += keysInBucket[i];
			str += ", ";
		}
		str += keysInBucket[keysInBucket.length - 1];
		str += "]";
		return str;
	}


}