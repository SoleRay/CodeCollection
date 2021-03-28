package exception;

public class StackOverflowErrorCase {

    public void dosth(int i,String str){
        if(i==5000){
            return;
        }
        System.out.println(i);

        String newStr = str + str;
        dosth(i+1,str);
    }

    public static void main(String[] args) {

        String str = "hello,everyone! My name is bob, welcome to art club, the girls here are beautiful and hot.";
        StackOverflowErrorCase errorCase = new StackOverflowErrorCase();
        errorCase.dosth(0,str);
    }
}
