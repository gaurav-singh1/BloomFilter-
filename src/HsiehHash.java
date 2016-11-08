import java.util.Collection;

/**
http://www.azillionmonkeys.com/qed/hash.html
*/
public class HsiehHash {
	
	private HsiehHash() {}
	
	public static int hash(String things) {
		int hash = things.length() * 4;
		
		
			int thingHash = things.hashCode();
			/*
			// we could apply the re-hash from HashMap, but this seems pointless
			thingHash ^= (thingHash >>> 20) ^ (thingHash >>> 12);
			thingHash = thingHash ^ (thingHash >>> 7) ^ (thingHash >>> 4);
			*/
			
			hash += (thingHash >>> 16);
			int tmp = ((thingHash & 0xffff) << 11) ^ hash;
			hash = (hash << 16) ^ tmp;
			hash += (hash >> 11);
	//	}
		
		// no need for the end case handling, as we always have a multiple of 4 bytes
		
		hash ^= hash << 3;
		hash += hash >> 5;
		hash ^= hash << 4;
		hash += hash >> 17;
		hash ^= hash << 25;
		hash += hash >> 6;
		
		return hash;
	}
	
	public static void main(String[] arg){
		HsiehHash hash=new HsiehHash();
		
		System.out.println(hash.hash("google"));
		
		
	}
	
}