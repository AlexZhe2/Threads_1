import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class SortWithThreads {


    List<String> strings = new LinkedList<>();
    LinkedList<String> listStrings = new LinkedList<>();
    static LinkedList<String> listOfWords = new LinkedList<>();
    LinkedList<String> listOfWordsUnic = new LinkedList<>();
    LinkedList<String> tempListStrings = new LinkedList<>();
    TreeSet<String> setOfWords = new TreeSet<>();
    TreeMap<String, Integer> mapOfWords = new TreeMap<>();
    volatile TreeMap<String, Integer> bigMap = new TreeMap<>();
    volatile static LinkedList<LinkedList> someListOfWords = new LinkedList<>();
    volatile static boolean flag = false;
    static Object lock1 = new Object();
    static Object lock2 = new Object();


    public static void main(String[] args) throws IOException {

        SortWithThreads start = new SortWithThreads();
        start.wordCounter();
        start.wordSeparation();

        int a = listOfWords.size() / Runtime.getRuntime().availableProcessors();

        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            LinkedList<String> tempList = new LinkedList<>();

            if (i != (Runtime.getRuntime().availableProcessors())) {
                for (int j = a * i; j < (a * (i + 1)); j++) {
                    tempList.add(listOfWords.get(j));
                }
            }
            if (i == (Runtime.getRuntime().availableProcessors())) {
                for (int j = listOfWords.size() - (a * (i - 1)); j < listOfWords.size(); j++) {
                    tempList.add(listOfWords.get(j));
                }
            }
            someListOfWords.add(new LinkedList(tempList));
        }

        Thread threads = new Thread(new Runnable() {
            LinkedList<String> miniList;

            @Override
            public void run() {

                synchronized (lock1) {
                    miniList = someListOfWords.getFirst();
                    someListOfWords.remove(someListOfWords.getFirst());

                    while (true) {
                        if (flag = true) {
                            start.printTopTeen(miniList);
                            return;
                        }
                    }
                }
            }
        });

        ArrayList<Thread> arrThreads = new ArrayList<>();

        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            arrThreads.add(new Thread(threads));
        }

        for (Thread i : arrThreads) {
            i.start();
        }

        synchronized (lock2) {
            while (true) {
                int counter = 0;
                for (Thread i : arrThreads) {

                    if (!i.isAlive()) {
                        counter++;
                    }
                    if (counter == Runtime.getRuntime().availableProcessors()) {

                        start.PrintWithTreads();
                        return;
                    }
                }
            }
        }
    }

    public synchronized void wordCounter() throws IOException {

        ClassLoader loader = SortWithThreads.class.getClassLoader();
        File file = new File(loader.getResource("wp.txt").getFile());
        strings = Files.readAllLines(file.toPath());
        listStrings.addAll(strings);

        String stringTemp;
        String stringTempLowCase;

        for (int i = 0; i < listStrings.size(); i++) {

            stringTemp = listStrings.get(i);
            stringTemp = stringTemp.replaceAll("[^A-Za-z]", "=");
            stringTemp = stringTemp.replaceAll("^", "=");
            stringTemp = stringTemp.replaceAll("$", "=");
            stringTemp = stringTemp.replaceAll("={2,}", "=");
            stringTempLowCase = stringTemp.toLowerCase();

            tempListStrings.add(i, stringTempLowCase);

        }
    }

    public synchronized void wordSeparation() throws IOException {

        for (String i : tempListStrings) {
            String[] arr = i.split("=");
            listOfWords.addAll(Arrays.asList(arr));
        }

        Iterator<String> listOfWordsIterator = listOfWords.listIterator();

        while (listOfWordsIterator.hasNext()) {
            if (listOfWordsIterator.next().equals("")) {
                listOfWordsIterator.remove();
            }
        }

        flag = true;
    }


    public synchronized void printTopTeen(LinkedList localMiniList) {

        setOfWords.addAll(localMiniList);
        listOfWordsUnic.addAll(setOfWords);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(localMiniList);

        int cntUnic = 0;
        String a1;
        String a2;

        for (String i : listOfWordsUnic) {
            cntUnic = 0;
            for (int j = 0; j < arrayList.size(); j++) {
                a1 = i;
                a2 = arrayList.get(j);
                if (a1.equals(a2)) {
                    cntUnic++;
                    mapOfWords.put(i, cntUnic);
                }
            }
        }


        for (Map.Entry s : mapOfWords.entrySet())

            bigMap.merge(String.valueOf(s.getKey()), (Integer) s.getValue(), (a, b) -> a + b);


    }


    public synchronized void PrintWithTreads() {

        LinkedList<String> list7 = new LinkedList<>();
        list7.addAll(bigMap.keySet());
        LinkedList<Integer> list8 = new LinkedList<>();
        list8.addAll(bigMap.values());

        LinkedList<Coo> list9 = new LinkedList<>();

        for (int i = 0; i < list7.size(); i++) {
            list9.add(i, new Coo(list8.get(i), list7.get(i)));

        }

        Comparator<Coo> CooComparator = new Coo.CooBComparator();
        TreeSet<Coo> cooCoo = new TreeSet<>(CooComparator);

        for (int i = 0; i < list9.size(); i++) {
            cooCoo.add(list9.get(i));
        }

        LinkedList<Coo> topTen = new LinkedList<>();
        LinkedList<Coo> topTen2 = new LinkedList<>();

        topTen.addAll(cooCoo);

        for (int i = 0; i < 10; i++) {
            topTen2.add(i, topTen.get(i));
        }

        for (Coo i : topTen2) {
            System.out.println(i);
        }
    }
}



