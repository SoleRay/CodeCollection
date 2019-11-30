package common.beans;

public class Bean {

    private int attr1;

    private String attr2;

    private Bean attr3;

    public Bean() {
    }

    public Bean(int attr1, String attr2, Bean attr3) {
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.attr3 = attr3;
    }

    public int getAttr1() {
        return attr1;
    }

    public void setAttr1(int attr1) {
        this.attr1 = attr1;
    }

    public String getAttr2() {
        return attr2;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    public Bean getAttr3() {
        return attr3;
    }

    public void setAttr3(Bean attr3) {
        this.attr3 = attr3;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "attr1=" + attr1 +
                ", attr2='" + attr2 + '\'' +
                ", attr3=" + attr3 +
                '}';
    }
}
