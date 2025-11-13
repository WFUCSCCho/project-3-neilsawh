/*
@file: Proj3.java
@description: Sorts a list of words from a file using various algorithms and records time/comparisons
@author: Neil Sawhney
@date: November 13, 2025
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Proj3 {
    // Merge Sort
    public static <T extends Comparable> void mergeSort(ArrayList<T> a, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(a, left, mid);
            mergeSort(a, mid + 1, right);
            merge(a, left, mid, right);
        }
    }

    // Merge two sorted sublists into a single sorted sublist
    public static <T extends Comparable> void merge(ArrayList<T> a, int left, int mid, int right) {
        ArrayList<T> temp = new ArrayList<>();
        int i = left;
        int j = mid + 1;
        while (i <= mid && j <= right) {
            if (a.get(i).compareTo(a.get(j)) <= 0) {
                temp.add(a.get(i++));
            } else {
                temp.add(a.get(j++));
            }
        }
        while (i <= mid) temp.add(a.get(i++));
        while (j <= right) temp.add(a.get(j++));
        for (int k = 0; k < temp.size(); k++) {
            a.set(left + k, temp.get(k));
        }
    }

    // Quick Sort
    public static <T extends Comparable> void quickSort(ArrayList<T> a, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(a, left, right);
            quickSort(a, left, pivotIndex - 1);
            quickSort(a, pivotIndex + 1, right);
        }
    }

    // Partition a sublist around a pivot for Quick Sort
    public static <T extends Comparable> int partition (ArrayList<T> a, int left, int right) {
        T pivot = a.get(right);
        int i = left - 1;
        for (int j = left; j < right; j++) {
            if (a.get(j).compareTo(pivot) <= 0) {
                i++;
                swap(a, i, j);
            }
        }
        swap(a, i + 1, right);
        return i + 1;
    }

    // Swap two elements in an ArrayList
    static <T> void swap(ArrayList<T> a, int i, int j) {
        T temp = a.get(i);
        a.set(i, a.get(j));
        a.set(j, temp);
    }

    // Heap Sort
    public static <T extends Comparable> void heapSort(ArrayList<T> a, int left, int right) {
        int size = right - left + 1;
        for (int i = size / 2 - 1; i >= 0; i--) {
            heapify(a, size, i, left);
        }
        for (int i = size - 1; i > 0; i--) {
            swap(a, left, left + i);
            heapify(a, i, 0, left);
        }
    }

    // Maintain heap property for a subtree for Heap Sort
    public static <T extends Comparable> void heapify (ArrayList<T> a, int n, int i, int offset) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;
        if (l < n && a.get(offset + l).compareTo(a.get(offset + largest)) > 0)
            largest = l;
        if (r < n && a.get(offset + r).compareTo(a.get(offset + largest)) > 0)
            largest = r;
        if (largest != i) {
            swap(a, offset + i, offset + largest);
            heapify(a, n, largest, offset);
        }
    }

    // Bubble Sort
    public static <T extends Comparable> int bubbleSort(ArrayList<T> a, int size) {
        int comparisons = 0;
        boolean swapped;
        for (int i = 0; i < size - 1; i++) {
            swapped = false;
            for (int j = 0; j < size - i - 1; j++) {
                comparisons++;
                if (a.get(j).compareTo(a.get(j + 1)) > 0) {
                    swap(a, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
        return comparisons;
    }

    // Odd-Even Transposition Sort
    public static <T extends Comparable> int transpositionSort(ArrayList<T> a, int size) {
        boolean sorted = false;
        int comparisons = 0;
        while (!sorted) {
            sorted = true;
            for (int i = 1; i <= size - 2; i += 2) {
                comparisons++;
                if (a.get(i).compareTo(a.get(i + 1)) > 0) {
                    swap(a, i, i + 1);
                    sorted = false;
                }
            }
            for (int i = 0; i <= size - 2; i += 2) {
                comparisons++;
                if (a.get(i).compareTo(a.get(i + 1)) > 0) {
                    swap(a, i, i + 1);
                    sorted = false;
                }
            }
        }
        return comparisons;
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java Proj3 {dataset-file} {sorting-algorithm-type} {number-of-lines}");
            return;
        }

        String filename = args[0];
        String algorithm = args[1].toLowerCase();
        int numLines = Integer.parseInt(args[2]);

        // Store words
        ArrayList<String> data = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            int count = 0;
            while ((line = br.readLine()) != null && count < numLines) {
                data.add(line.trim());
                count++;
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        ArrayList<String> sortedList = new ArrayList<>(data);
        Collections.sort(sortedList);

        ArrayList<String> shuffledList = new ArrayList<>(data);
        Collections.shuffle(shuffledList);

        ArrayList<String> reversedList = new ArrayList<>(data);
        Collections.sort(reversedList, Collections.reverseOrder());

        StringBuilder results = new StringBuilder();
        results.append("Lines Evaluated: ").append(numLines).append("\n");
        results.append("Algorithm: ").append(algorithm).append("\n");

        ArrayList<String> tempList = null;
        long startTime, endTime;
        int comparisons = 0;

        String[] dataTypes = {"Already-Sorted", "Shuffled", "Reversed"};
        ArrayList<ArrayList<String>> datasets = new ArrayList<>();
        datasets.add(sortedList);
        datasets.add(shuffledList);
        datasets.add(reversedList);

        for (int i = 0; i < datasets.size(); i++) {
            tempList = new ArrayList<>(datasets.get(i));

            if (algorithm.equals("bubble")) {
                startTime = System.nanoTime();
                comparisons = bubbleSort(tempList, tempList.size());
                endTime = System.nanoTime();
                results.append(dataTypes[i]).append(" Comparisons: ").append(comparisons).append("\n");
                results.append(dataTypes[i]).append(" Time: ").append(endTime - startTime).append(" ns\n");
            } else if (algorithm.equals("transposition")) {
                comparisons = transpositionSort(tempList, tempList.size());
                results.append(dataTypes[i]).append(" Comparisons: ").append(comparisons).append("\n");
                // skip printing time
            } else {
                startTime = System.nanoTime();
                if (algorithm.equals("merge")) mergeSort(tempList, 0, tempList.size() - 1);
                else if (algorithm.equals("quick")) quickSort(tempList, 0, tempList.size() - 1);
                else if (algorithm.equals("heap")) heapSort(tempList, 0, tempList.size() - 1);
                endTime = System.nanoTime();
                results.append(dataTypes[i]).append(" Time: ").append(endTime - startTime).append(" ns\n");
            }
        }


        System.out.println(results.toString());

        // Append results to analysis.txt (CSV format)
        try (FileWriter fw = new FileWriter("analysis.txt", true)) {
            String resultLine = numLines + "," + algorithm + "," + results.toString().replace("\n", ";");
            fw.write(resultLine + System.lineSeparator());
        } catch (Exception e) {
            System.out.println("Error writing analysis.txt: " + e.getMessage());
        }

        // Write the sorted list (overwrite sorted.txt)
        try (FileWriter fw = new FileWriter("sorted.txt")) {
            for (String val : tempList) {
                fw.write(val + "\n");
            }
        } catch (Exception e) {
            System.out.println("Error writing sorted.txt: " + e.getMessage());
        }
    }
}
