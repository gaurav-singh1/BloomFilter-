
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.BitSet;

public class CreateBloomFilter {
	private double falseprobability;
	private long n;
	private int m;
	private int k;
	private BitSet bloomfilter;

	public CreateBloomFilter(long n, double p) {

		this.n = n;

		this.falseprobability = p;

		this.m = optimalM(n, p);

		this.k = optimalK(n, m);

		this.bloomfilter = new BitSet(m);

	}

	public int optimalM(long n, double p) {
		return (int) Math.ceil(-1 * (n * Math.log(p)) / Math.pow(Math.log(2), 2));
	}

	public int optimalK(long n, long m) {
		return (int) Math.ceil((Math.log(2) * m) / n);
	}

	public void add_all(String[] str) throws NoSuchAlgorithmException {
		for (String s : str)
			add(s);
	}

	public void add(String str) throws NoSuchAlgorithmException {

		int[] position = hashpositions(str);
		setbits(position);
	}

	private void setbits(int[] position) {
		int i;

		for (i = 0; i < k; i++) {

			bloomfilter.set(position[i], true);

		}
	}

	public void putdata(String str) {

		// putting the non zero value
		for (int i = 0; i < str.length(); i++) {

			if (str.charAt(i) == '1')
				bloomfilter.set(i);

		}

	}

	@SuppressWarnings("static-access")
	public int[] hashpositions(String str) {
		// TODO Auto-generated method stub
		int[] hashposition = new int[k];
		int hashcode;

		MurmurHash3 hash = new MurmurHash3();
		for (int i = 1; i <= k; i++) {

			hashcode = hash.murmurhash3_x86_32(str, 0, str.length(), i);
			if (hashcode < 0)
				hashcode = ~hashcode;

			hashposition[i - 1] = hashcode % m;
		}

		return hashposition;
	}

	boolean contains(String str) {

		int[] hashposition = hashpositions(str);
		boolean result = true;

		for (int i = 0; i < k; i++) {
			if (!bloomfilter.get(hashposition[i]))
				return false;
		}

		return result;

	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub

		int elements_to_add = 1000;
		CreateBloomFilter bf = new CreateBloomFilter(1000, 0.01);

		System.out.println(bf.k);
		System.out.println(bf.falseprobability);
		System.out.println(bf.m);
		System.out.println(bf.n);

		System.out.println("**************NOW ELEMENT ADDITION STARTS**************");
		String str;
		for (int i = 0; i < elements_to_add; i++) {
			str = "random string" + i;
			bf.add(str);
		}

		System.out.println("printing non zero values to 1");
		bf.settoone(bf);
		System.out.println("**************ELEMENT ADDED**************");

		System.out.println("NOW CHECKING THE ELEMENTS JUST ADDED");

		final long StartTime = System.nanoTime();

		Boolean flag;
		for (int i = 0; i < elements_to_add; i++) {
			str = "random string" + i;

			flag = bf.contains(str);
			if (!flag)
				System.out.println("error in   " + str);
		}

		final long EndTime = System.nanoTime();
		long AvgTime = (EndTime - StartTime) / elements_to_add;
		System.out.println("Avg Time in checking:" + AvgTime);

		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		// System.out.println(bf.bloomfilter.toString());
		final long startTime = System.nanoTime();

		for (int j = 0; j < elements_to_add; j++)
			if (!bf.contains("random string" + j))
				System.out.println("false");

		final long duration = System.nanoTime() - startTime;
		// long totalTime = endTime - startTime;
		System.out.println("total time for get operation=" + (duration / elements_to_add));
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		System.out.println("Size of bloom fitler");
		System.out.println(bf.toString().length());

	}

	private void settoone(CreateBloomFilter bf) {
		// TODO Auto-generated method stub
		for (int i = 0; i < bf.m; i++) {
			if (bloomfilter.get(i))
				System.out.print(1);
			else
				System.out.print(0);
		}
		System.out.println();
	}

}
