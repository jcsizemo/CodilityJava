package com.sizemore.codility;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

public class Chlorum implements Problem {

	int chlorum(int K, int[] C, int[] D) {

		// need a fast way to check where we are coming from
		// need a fast way to check if we're excluding anything we shouldn't

		final int[] going = C;
		final int[] attractiveness = D;
		Vector<LinkedList<Integer>> coming = new Vector<LinkedList<Integer>>();
		for (int i = 0; i < going.length; i++)
			coming.add(new LinkedList<Integer>());
		
		int[] sortedD = new int[D.length];
		for (int i = 0; i < D.length; i++)
			sortedD[i] = D[i];
		Arrays.sort(sortedD);
		
		// our solution is going to a subset of sortedD, where our attractivness
		// will be clustered at the end of the array. Therefore, the sum of the
		// values, depending on length n of the trip, should be the sum
		// of the last n numbers of sortedD. We store the sums here for
		// easy reference.
		int[] optimalSum = new int[D.length];
		optimalSum[optimalSum.length - 1] = sortedD[optimalSum.length - 1];
		for (int i = optimalSum.length - 2; i >= 0; i--) {
			optimalSum[i] = sortedD[i] + optimalSum[i+1];
		}
		
		for (int city = 0; city < going.length; city++) {
			int dest = going[city];
			if (city == dest) continue;
			coming.get(dest).add(city);
		}

		// sort the cities coming in by attractiveness. Each time we add a city,
		// we remove it from the applicable list like a stack.
		for (LinkedList<Integer> comings : coming) {
			if (comings.size() < 2) continue;
			
			Collections.sort(comings, new Comparator<Integer>() {

				@Override
				public int compare(Integer o1, Integer o2) {
					if (attractiveness[o1.intValue()] > attractiveness[o2.intValue()]) return -1;
					else return 1;
				}
				
			});
		}
		
		// even if we have multiple cities with optimal attraction, we only pick one.
		// we can't exclude any cities that aren't as attractive, so we have to
		// either include all of the most attractive city or just one. If there is an
		// optimal path connecting the n most optimal cities, the algorithm will find it
		// considering we proceeded based on a greedy approach
		int mostAttractiveIndex = - 1;
		int mostAttractive = sortedD[sortedD.length - 1];
		for (int i = 0; i < D.length && mostAttractiveIndex == -1; i++) {
			if (D[i] == mostAttractive) mostAttractiveIndex = i;
		}
		
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int tripLength = 1;
		int tripSum = mostAttractive;
		Stack<Integer> addedAttractiveness = new Stack<Integer>();
		Set<Integer> addedCities = new HashSet<Integer>();
		if (mostAttractiveIndex != going[mostAttractiveIndex]) queue.add(going[mostAttractiveIndex]);
		queue.addAll(coming.get(mostAttractiveIndex));
		addedCities.add(mostAttractiveIndex);
		
		while (tripLength != K) {
			
			Collections.sort(queue, new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					if (attractiveness[o1.intValue()] > attractiveness[o2.intValue()]) return -1;
					else return 1;
				}
			});
			
			int city = queue.removeFirst();
			if (city != going[city] && !addedCities.contains(going[city])) queue.add(going[city]);
			coming.get(city).removeAll(addedCities);
			queue.addAll(coming.get(city));
			addedCities.add(city);
			tripLength++;
			tripSum += attractiveness[city];
			addedAttractiveness.push(attractiveness[city]);
		}
		
		while (tripSum != optimalSum[optimalSum.length - tripLength]) {
			tripLength--;
			tripSum -= addedAttractiveness.pop();
		}
		
		return tripLength;
		
		/*int tripSum = mostAttractive;
		int tripLength = 1;
		
		int goingIndex = mostAttractiveIndex;
		int comingIndex = mostAttractiveIndex;
		Stack<Integer> addedAttractiveness = new Stack<Integer>();
		
		// if we hit a branch, add a stack to account for them
		while (tripLength != K) {
			tripLength++;
			if (going[goingIndex] == goingIndex) {
				comingIndex = coming.get(comingIndex).removeFirst();
				tripSum += D[comingIndex];
				addedAttractiveness.push(D[comingIndex]);
			}
			else if (coming.get(comingIndex).size() == 0) {
				goingIndex = going[goingIndex];
				tripSum += D[goingIndex];
				addedAttractiveness.push(D[goingIndex]);
			}
			else if (D[going[goingIndex]] > D[coming.get(comingIndex).peek()]){
				goingIndex = going[goingIndex];
				tripSum += D[goingIndex];
				addedAttractiveness.push(D[goingIndex]);
			}
			else {
				comingIndex = coming.get(comingIndex).removeFirst();
				tripSum += D[comingIndex];
				addedAttractiveness.push(D[comingIndex]);
			}
		}
		
		while (tripSum != optimalSum[optimalSum.length - tripLength]) {
			tripLength--;
			tripSum -= addedAttractiveness.pop();
		}
		
		// finally, check to see if the trip sum checks out with the length given our
		// trip sum array. if not, prop values until we get something that works and return
		// the adjusted trip length.

		// build coming array, then loop through the array and sort each item
		// this sorting procedure will not be longer than nlgn



		// next, we want an array that tells us what the first index is of each
		// attractiveness level in the sorted array. We do this to easily
		// determine
		// if we are missing any cities that are as attractive as we need.
		// We can compare the lowest ranked
		// city and pull its value out of this new array. Every time we add a
		// new highest
		// value attractiveness to our trip, we set the highest ranked value to
		// the
		// value in this array. Each time we add a city to our trip equaling the
		// highest
		// attractiveness, we increment this number. We can easily check if we
		// are omitting any
		// cities by taking the difference between this incremented number and
		// the first index
		// of the least attractive city. This number denotes the number of
		// cities that should
		// exist in our trip, and this should equal to number currently in the
		// trip.

		// nothing excluded should be more attractive than what is included. We
		// can use
		// the sorted array to determine if the value at the end is equal to the
		// currently
		// most attractive city in our trip

		return tripLength;*/
	}

	public void execute() {

		int[] C = { 1, 3, 0, 3, 2, 4, 4 };
		int[] D = { 6, 2, 7, 5, 6, 5, 2 };
		System.out.println(chlorum(3, C, D));
	}
}
