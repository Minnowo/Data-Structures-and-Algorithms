
public class LinkedStack 
{
    public static class Node 
    {
        int data;
        Node next;

        public Node(int data)
        {
            this.data = data;
        }
    }

    private Node head;
    private int size;

    public void push(int data)
    {
        if(this.head == null)
        {
            this.head = new Node(data);
            this.size = 1;
            return;
        }

        Node n = new Node(data);
        n.next = this.head;
        this.head = n;
        this.size++;
    }

    public Integer pop()
    {
        if(this.head == null)
            return null;

        Node c = this.head;
        
        this.head = this.head.next;

        this.size--;

        return c.data;
    }

    public Integer peek()
    {
        if(this.head == null)
            return null;

        return this.head.data;
    }

    public boolean isEmpty()
    {
        return this.size == 0;
    }

    public int size()
    {
        return this.size;
    }

    public static void main(String[] args) 
    {
        LinkedStack s = new LinkedStack();    

        s.push(5);
        s.push(6);
        s.push(7);
        s.push(8);

        for(; !s.isEmpty();)
        {
            System.out.println(s.pop());
        }
    }
}
