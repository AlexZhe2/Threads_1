import java.util.Comparator;

public class Coo {

    Integer value;
    String word;




    public Coo(Integer value, String word) {
        this.word = word;
        this.value = value;
    }



    public String getA11() {
        return word;
    }

    public void setA11(String a11) {
        this.word = a11;
    }

    public Integer getB12() {
        return value;
    }

    public void setB12(Integer b12) {
        this.value = b12;
    }

    @Override
    public String toString() {
        return "word \"" + word+"\""+
                " value=" + value

                ;
    }

    static class CooBComparator implements Comparator<Coo> {
        @Override
        public int compare(Coo o1, Coo o2) {
            return Integer.compare(o2.getB12(), o1.getB12());  //  сравнивает числа
        }
    }
}
