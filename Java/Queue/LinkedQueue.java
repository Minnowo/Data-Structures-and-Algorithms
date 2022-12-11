
public class LinkedQueue 
{
    public static class Node 
    {
        int data;
        Node next;
        Node prev;

        public Node(int data)
        {
            this.data = data;
        }
    }
    private int size;
    
    private Node head;
    private Node tail;

    public void add(int data)
    {
        if(this.head == null)
        {
            this.head = new Node(data);
            this.tail = this.head;
            this.size = 1;
            return;
        }

        Node n = new Node(data);

        n.next = this.tail;
        this.tail.prev = n;

        this.tail = n;
        
        this.size++;
    }

    public Integer peek()
    {
        if(this.head == null)
            return null;

        return this.head.data;
    }

    public Integer pop()
    {
        if(this.head == null)
            return null;

        Node n = this.head;

        if(this.head == this.tail)
        {
            this.tail = null;
            this.head = null;
            this.size = 0;
            return n.data;
        }

        this.head = this.head.prev;
        this.head.next = null;

        this.size--;

        return n.data;
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
        LinkedQueue q = new LinkedQueue();
        q.add(1);
        q.add(2);
        q.add(3);
        q.add(4);
        q.add(5);

        for(; !q.isEmpty();)
        {
            System.out.println(q.pop());
        }
        
    }
}
