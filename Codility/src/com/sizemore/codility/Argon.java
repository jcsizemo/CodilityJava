package com.sizemore.codility;

public class Argon implements Problem {
	
	int argon(int[] A) {
		
		int firstZero = 0;
		int lastOne = A.length - 1;
		
		while (lastOne > 0 && A[lastOne] != 1) lastOne--;
		while (firstZero < A.length-1 && A[firstZero] != 0) firstZero++;
		
		// find boundaries to start evaluating, we can terminate early if there are no zeroes, no 1's, or if the first of either
		// pass each other in the search
		if (firstZero == A.length - 1 || lastOne == 0 || firstZero > lastOne) return 0;
		
		int[] zeroes = new int[A.length];
		int[] ones = new int[A.length];
		if (A[0] == 0) zeroes[0] = 1;
		if (A[A.length - 1] == 1) ones[A.length-1] = 1;
		
		// cache the zeroes and ones we have sent up to this point in the list, coming from the appropriate direction
		for (int i = 1; i < A.length; i++) {
			zeroes[i] = zeroes[i-1];
			if (A[i] == 0) zeroes[i]++;
			
			ones[A.length - i - 1] = ones[A.length - i];
			if (A[A.length - i - 1] == 1) ones[A.length - i - 1]++;
 		}
		
		int longestVacation = 0;
		
		for (int i = firstZero; i < lastOne; i++) {
			// no 0's before firstZero
			// no 1's after lastOne
			// we know we aren't going to improve our situation if we expand in either direction, can
			// check to see if each side fits the criteria, else we go to the next one
			
			int leftLength = i - firstZero + 1;
			int rightLength = lastOne - i;
			int leftZeroes = zeroes[i];
			int leftOnes = leftLength - (zeroes[i]);
			int rightZeroes = rightLength - ones[i+1];
			int rightOnes = ones[i+1];
			
			// if we hit this case, the place we've chosen as our split will never fit the criteria
			if (leftZeroes <= leftOnes || rightZeroes >= rightOnes) continue;
			
			// number of the dominant value minus the other minus 1 so we know how many we can add 
			// such that the dominant value is still in the majority
			int leftGrowth = leftZeroes - leftOnes - 1;
			int rightGrowth = rightOnes - rightZeroes - 1;
			
			// if the amount we can grow exceeds how far the array goes in the appropriate direction, truncate the growth
			if (firstZero - leftGrowth < 0) leftGrowth = firstZero;
			if (lastOne + rightGrowth >= A.length) rightGrowth = A.length - 1 - lastOne;
			
			if (longestVacation < lastOne - firstZero + leftGrowth + rightGrowth + 1) {
				longestVacation = lastOne - firstZero + leftGrowth + rightGrowth + 1;
			}	
		}
		return longestVacation;
	}

	
	public void execute() {
		
		int[] A = {1,1,1,1,1,1,0,0,0,0,0};
		System.out.println(argon(A));
	}
}
