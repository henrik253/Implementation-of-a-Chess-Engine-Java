package BitboardValidation.Bitboards;

public class BitMaskArr {
    public long[]content;
    public BitMaskArr(){
        this.content = new long[64];
        for(int i = 0; i < 63; i++){
            this.content[63 - i] = (long)Math.pow(2, i);
        }
        this.content[0] = (long)Math.pow(2, 63) + 1;
    }
}
