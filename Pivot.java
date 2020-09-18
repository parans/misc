package com.teradata.appcenter.util;

import java.util.Arrays;
import java.util.Collections;

public class Pivot {
	
	public int pivot(int[] arr) {
		
		if(arr.length < 1) {
			return -1;
		}
		
		int pivot = 0;
		int i = 1, j = arr.length - 1;
		
		// 14, 5, 1, 2, 15,
		
		while(i < j) {
			//Find an element larger than pivot, then place the pivot before that element
			while(arr[i] < arr[pivot] && i < arr.length - 1) {
				i++;
			}
			
			while(arr[j] > arr[pivot] && j > 0) {
				j--;
			}
			
			if (i < j) {
				int temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
		}
		int temp = arr[pivot];
		arr[pivot] = arr[j];
		arr[j] = temp;
		
		return j;
	}
	
	public static void main(String[] args) {
		int[] arr = new int[] {15, 20, 7, 6, 11};
		Pivot p = new Pivot();
		p.pivot(arr);
		System.out.println(Arrays.toString(arr));
	}

}
