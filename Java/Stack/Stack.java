
public class Stack 
{
    int size;

    final int[] DATA;

    public Stack(final int max)
    {
        this.size = 0;
        this.DATA = new int[max];
    }

    public boolean isEmpty()
    {
        return this.size == 0;
    }

    public int size()
    {
        return this.size;
    }

    public boolean push(int data)
    {
        if(this.size >= this.DATA.length)
        {
            return false;
        }

        this.DATA[this.size] = data;
        
        this.size++;

        return true;
    }

    public Integer peek()
    {
        if(this.size == 0)
            return null;

        return this.DATA[this.size - 1];
    }

    public Integer pop()
    {
        if(this.size == 0)
            return null;

        return this.DATA[--this.size];
    }

    public static void main(String[] args) 
    {
        Stack s = new Stack(5);
        
        s.push(1);
        s.push(2);
        s.push(3);
        s.push(4);
        s.push(5);
        
        if(s.push(6))
            System.out.println("added 6");
        else 
            System.out.println("unable to add 6");

        for(; !s.isEmpty();)
        {
            System.out.println(s.pop());
        }
    }
}
