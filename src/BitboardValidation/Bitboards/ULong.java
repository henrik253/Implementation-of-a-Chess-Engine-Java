package BitboardValidation.Bitboards;

public class ULong {
    public BitMaskArr bitmasks;
    public long content;
    public ULong(BitMaskArr arr){
        this.content = 0L;
        this.bitmasks = arr;
    }
    public ULong(long i, BitMaskArr arr){
        this.bitmasks = arr;
        this.content = i;
    }
    public ULong(String bitRepresentation, BitMaskArr arr){
        this.content = Long.parseUnsignedLong(bitRepresentation);
    }
    public ULong(ULong toCopy){
        this.content = toCopy.content;
        this.bitmasks = toCopy.bitmasks;
    }

    public boolean at(int row, int col){
        return (bitmasks.content[row * 8 + col] & this.content) != 0;
    }
    public boolean atWithOutOfBoundsSafety(int row, int col){
        if(row >= 0 && row < 8 && col >= 0 && col < 8){
            return (bitmasks.content[row * 8 + col] & this.content) != 0;
        }else{
            return false;
        }

    }
    public void set(int row, int col){
        this.content |= bitmasks.content[row * 8 + col];
    }
    public void unset(int row, int col){
        this.content &= ~bitmasks.content[row * 8 + col];
    }
    public void unsetWithOutOfBoundsSafety(int row, int col){
        if(row >= 0 && row < 8 && col >= 0 && col < 8) {
            this.content |= ~bitmasks.content[row * 8 + col];
        }
    }
    public void flip(int row, int col){
        this.content ^= bitmasks.content[row * 8 + col];
    }
    public void and(ULong b){
        this.content &= b.content;
    }
    public void or(ULong b){
        this.content |= b.content;
    }
    public void xor(ULong b){
        this.content ^= b.content;
    }
    public void not(){
        this.content = ~this.content;
    }
    public void print1D(){
        StringBuilder bin = new StringBuilder(Long.toBinaryString(this.content));
        while(bin.length() < 64){
            bin.insert(0, '0');
        }
        System.out.println(bin);
    }
    public void print2D(){
        StringBuilder bin = new StringBuilder(Long.toBinaryString(this.content));
        while(bin.length() < 64){
            bin.insert(0, 0);
        }
        int index = 0;
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                if(bin.charAt(index) == '1'){
                    System.out.print("X ");
                }else{
                    System.out.print(". ");
                }
                index++;
            }
            System.out.println();
        }

    }
}
