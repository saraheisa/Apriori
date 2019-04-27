package Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Apriori {

    ArrayList<String> transactions = new ArrayList<>();
    int minSup;
    int minConf;

    public Apriori(double minSup, double minConf) {
        Scanner file;

        try {
            file = new Scanner(new File("transactions.txt"));
            while (file.hasNextLine()){
                transactions.add(file.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.minSup = (int)Math.ceil(minSup * transactions.size());
        this.minConf = (int)Math.ceil(minConf * transactions.size());
    }

    public int getFrequency(ArrayList<String> list, String[] items){
        int count = 0;

        for (int i = 0; i < list.size(); i++) {
            String[] l = list.get(i).split(" ");
            boolean flag = true;
            for (int j = 0; j < items.length; j++) {
                if(!Arrays.asList(l).contains(items[j])){
                    flag = false;
                    break;
                }
            }
            if (flag){
                count++;
            }
        }
        return count;
    }

    public void getItemSet(ArrayList<String> list, ArrayList<String> set, int index, ArrayList<String> newSet){
        int n = set.get(0).split(" ").length - 1;

        if (n == 0){
            for (int i = index+1; i < set.size(); i++) {
                String newItem = set.get(index) + " " + set.get(i);
                String[] temp = newItem.split(" ");
                if (getFrequency(list, temp) >= minSup){
                    newSet.add(newItem);
                }
            }
        }else {
            String[] setItems = set.get(index).split(" ");
            for (int i = index+1; i < set.size(); i++){
                String[] loopSetItems = set.get(i).split(" ");
                int countMatch = 0;
                for (int j = 0; j < setItems.length; j++) {
                    if (Arrays.asList(loopSetItems).contains(setItems[j])){
                        countMatch++;
                    }
                }
                if (countMatch == n){
                    String[] merged = mergeArrays2(setItems, loopSetItems);
                    boolean flag = true;
                    for (int j = 0; j < newSet.size(); j++) {
                        String[] newSetItem = newSet.get(j).split(" ");
                        if (checkSetsEquality(merged, newSetItem)){
                            flag = false;
                            break;
                        }
                    }
                    if (flag){
                        if (getFrequency(list, merged) >= minSup){
                            String newItem = arrToString(merged);
                            newSet.add(newItem);
                        }
                    }
                }
            }
        }
    }

    public String[] mergeArrays2(String[] arr1, String[] arr2){
        String[] merged = new String[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, merged, 0, arr1.length);
        System.arraycopy(arr2, 0, merged, arr1.length, arr2.length);

        Set<String> nodupes = new HashSet<>();

        for(int i=0;i<merged.length;i++){
            nodupes.add(merged[i]);
        }

        String[] nodupesarray = new String[nodupes.size()];
        int i = 0;
        Iterator<String> it = nodupes.iterator();
        while(it.hasNext()){
            nodupesarray[i] = it.next();
            i++;
        }



        return nodupesarray;
    }

    public boolean checkSetsEquality(String[] arr1, String[] arr2){

        Arrays.sort(arr1);
        Arrays.sort(arr2);

        if (Arrays.equals(arr1, arr2)){
            return true;
        }else {
            return false;
        }
    }

    public String arrToString(String[] arr){
        String s = "";
        s += arr[0];
        for (int i = 1; i < arr.length; i++) {
            s += " " + arr[i];
        }
        return s;
    }

    public ArrayList<String> getSuperSet(String[] set){
        ArrayList<String> superSet = new ArrayList<>();
        for (int i = 0; i < set.length; i++) {
            superSet.add(set[i]);
            for (int j = i+1; j < set.length; j++) {
                superSet.add(set[i] + " " + set[j]);
            }
        }
        return superSet;
    }

    public void getSuperSetItems(ArrayList<String> superSet, ArrayList<String> x, ArrayList<String> y){

        for (int i = 0; i < superSet.size(); i++) {
            String[] arr1 = superSet.get(i).split(" ");
            for (int j = 0; j < superSet.size(); j++) {
                String[] arr2 = superSet.get(j).split(" ");
                boolean flag = true;
                for (int k = 0; k < arr1.length; k++) {
                    if (Arrays.asList(arr2).contains(arr1[k])){
                        flag = false;
                        break;
                    }
                }
                if (flag){
                    x.add(arrToString(arr1));
                    y.add(arrToString(arr2));
                }
            }
        }
    }
}