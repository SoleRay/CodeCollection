package reflect.proxy.gun;

public class BigGun implements Gun {

    private int bulletNum;

    public BigGun(int bulletNum) {
        this.bulletNum = bulletNum;
    }

    @Override
    public boolean shoot() {
        bigShoot();
        return false;
    }

    private void bigShoot(){
        System.out.println("big shoot "+bulletNum+" !!");
    }
}
