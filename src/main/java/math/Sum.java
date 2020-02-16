package math;

public interface Sum {

        static int sumRange(int start,int end){
            int result = 0;
            for(int i=start;i<=end;i++){
                result = result + i;
            }
            return result;
        }

        static int factorial(int start,int end){
            int result = 1;
            for(int i=start;i<=end;i++){
                result = result * i;
            }
            return result;
        }

        static int sumPow(int start,int end){
            int result = 0;
            for(int i=start;i<=end;i++){
                result = result + i*i;
            }
            return result;
        }

        static int sumRange(){
            return sumRange(1,100);
        }

        static int factorial(){
            return factorial(1,10);
        }

        static int sumPow(){
            return factorial(1,100);
        }

    }