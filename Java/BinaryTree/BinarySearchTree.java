
public class BinarySearchTree 
{
    public static class Node 
    {
        Node left;
        Node right;
        int data;

        public Node()
        {
            data = 0;
        }

        public Node(int n)
        {
            data = n;
        }
    }

    private Node root;

    public void insert(int data)
    {
        if(this.root == null)
        {
            this.root = new Node(data);
            return;
        }

        for(Node n = this.root;;)
        {
            if(data < n.data)
            {
                if(n.left == null)
                {
                    n.left = new Node(data);
                    return;
                }

                n = n.left;
            }

            else if(data > n.data)
            {
                if(n.right == null)
                {
                    n.right = new Node(data);
                    return;   
                }

                n = n.right;
            }
            else 
            {
                return;
            }
        }
    }

    /**
     * Deletes the given value from the tree using iteration
     */
    public void deleteIter(int data)
    {
        if(this.root == null)
            return;

        for(Node n = this.root, p = null;;)
        {
            if(data < n.data)
            {
                if(n.left == null)
                {
                    return;
                }

                p = n;
                n = n.left;
            }

            else if(data > n.data)
            {
                if(n.right == null)
                {
                    return;   
                }

                p = n;
                n = n.right;
            }
            else 
            {
                // has 1 or 0 children
                if(n.left == null || n.right == null)
                {
                    Node temp = n.left;

                    if(temp == null)
                    {
                        temp = n.right;
                    }

                    // has 1 child
                    if(temp != null)
                    {
                        // if there is no parent, we're removing the root node
                        if(p == null)
                        {
                            this.root = temp;
                        }
                        // else replace the current node with it's only child
                        else if(n == p.left)
                        {
                            p.left = temp;
                        }
                        else 
                        {
                            p.right = temp;
                        }
                    }
                    // has no children, use parent ref to remove the node
                    else 
                    {
                        if(p == null)
                        {
                            this.root = null;
                        }
                        else if(n == p.left)
                        {
                            p.left = null;
                        }
                        else 
                        {
                            p.right = null;
                        }
                    }
                    return;
                }
                else 
                {
                    Node tmp = leftMost(n.right);

                    n.data = tmp.data;
    
                    p = n;
                    n = n.right;

                    data = tmp.data;
                    continue;
                }
            }
        }
    }

    /**
     * Deletes the given value from the tree making use of recursion
     */
    public void deleteRecur(int data)
    {
        this.root = deleteRecur(root ,data);
    }
    
    /**
     * Deletes the given value from the given tree making use of recursion
     */
    public static Node deleteRecur(Node root, int data) 
    {
        if(root == null)
        {
            return root;
        }
        if(root.data > data)
        {            
            root.left = deleteRecur(root.left, data);
        }
        else if(root.data < data)
        {
            root.right = deleteRecur(root.right, data);
        }
        else
        {          
            if(root.left == null && root.right == null)
            {
                root = null;
            }
            else if(root.right == null)
            {
                root = root.left;
            }
            else if(root.left == null)
            {
                root = root.right;
            }
            else
            {
                Node temp  = leftMost(root.right);
                root.data = temp.data;
                root.right = deleteRecur(root.right, temp.data);
            }
        }

        return root;
    }

    /**
     * Returns the leftmost child node of the given node
     */
	public static Node leftMost(Node node)
	{
		if(node == null)
			return null;
		
		for(; node.left != null; node = node.left);
		
		return node;
	}
	
    /**
     * Returns the rightmost child node of the given node
     */
	public static Node rightMost(Node node)
	{
		if(node == null)
			return null;
		
		for(; node.right != null; node = node.right);
		
		return node;
	}

    public void inOrder()
    {
        inOrder(this.root);
        System.out.println();
    }

    public static void inOrder(Node root)
    {
        if(root == null)
            return;
        inOrder(root.left);
        System.out.print(root.data + ", ");
        inOrder(root.right);
    }

    public void printTree()
    {
        printTree(this.root, "");
    }

    public static void printTree(Node root, String sep)
    {
        if(root == null)
            return;
        
        System.out.println(sep + root.data);
        printTree(root.left, sep + " ");
        printTree(root.right, sep + " ");
    }
}
