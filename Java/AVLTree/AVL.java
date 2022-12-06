
/**
 * AVL Tree template class, must be comparable types to use
 */
public class AVL <T extends Comparable<T>>
{
    public static class Node <K extends Comparable<K>>
    {
        Node<K> left;
        Node<K> right;
        int height;
        K data;

        public Node()
        {
            this.height = 1;
        }

        public Node(K value)
        {
            this.data = value;
            this.height = 1;
        }
    }

    private Node<T> root;

    /**
     * Inserts a node into the tree 
     */
    public void insert(T value)
    {
        root = insert(root, value);
    }

    /**
     * Inserts a value into the given tree
     */
    public Node<T> insert(Node<T> node, T value)
    {
        if (node == null)
        {
            return new Node<T>(value);
        }

        // navigate left down the tree
        if (value.compareTo(node.data) < 0)
        {
            node.left = insert(node.left, value);
        }

        // navigate right down the tree
        else if (value.compareTo(node.data) > 0)
        {
            node.right = insert(node.right, value);
        }

        // we're not supporting duplicates, we're done
        else
        {
            return node;
        }

        // rebalance the tree here, recursion handles all the moving back up for us

        // get balance factor 
        final int BALANCE = getBalanceFactor(node);

        // calculate the height of the node
        node.height = getMax(getHeight(node.left), getHeight(node.right)) + 1;

        // rotate left and right as needed 
        if (BALANCE > 1)
        {
            // RR case
            if (value.compareTo(node.left.data) < 0)
            {
                return rightRotate(node);
            }
            // RL case
            else
            {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        }

        if (BALANCE < -1)
        {
            // RR case
            if (value.compareTo(node.right.data) > 0)
            {
                return leftRotate(node);
            }
            // RL case
            else
            {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }

        return node;
    }



    /**
     * Deletes the given value from the tree
     */
    public void delete(T value)
    {
        this.root = delete(this.root, value);
    }

    /**
     * Deletes the given value from the give tree
     */
    public static <J extends Comparable<J>> Node<J> delete(Node<J> node, J value)
    {
        if(node == null)
        {
            return null;
        }

        // navigate down the left tree 
        if(value.compareTo(node.data) < 0)
        {
            node.left = delete(node.left, value);
        }

        // navigate down the right tree 
        else if(value.compareTo(node.data) > 0)
        {
            node.right = delete(node.right, value);
        }

        // found our value 
        else 
        {
            // if the node has 1 or 0 children, special case
            if(node.left == null || node.right == null)
            {
                Node<J> temp = node.left;

                // left node is null, use right
                if(temp == null)
                {
                    temp = node.right;
                }

                // if both children are null, set the node to null
                // this node will now be removed from the tree from the recursive call above 
                if(temp == null)
                {
                    node = null;
                }

                // set the node to it's only child
                // this child will now take it's place in the tree 
                else 
                {
                    node = temp;
                }
            }
            else 
            {
                // get the next node larger than the current node
                Node<J> temp = leftMost(node.right);
                
                // override the current value with the next value
                node.data = temp.data;

                // delete the child node instead of the current node in the middle of the tree 
                node.right = delete(node.right, node.data);
            }
        }

        // null check
        if(node == null)
            return null;

        // rebalance the tree here, recursion handles all the moving back up for us

        // get balance factor 
        final int BALANCE = getBalanceFactor(node);

        // calculate the height of the node
        node.height = getMax(getHeight(node.left), getHeight(node.right)) + 1;

        // rotate left and right as needed 
        if (BALANCE > 1)
        {
            // RR case
            if (value.compareTo(node.left.data) < 0)
            {
                return rightRotate(node);
            }
            // RL case
            else
            {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        }

        if (BALANCE < -1)
        {
            // RR case
            if (value.compareTo(node.left.data) > 0)
            {
                return leftRotate(node);
            }
            // RL case
            else
            {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }

        return node;
    }



    /**
     * Prints the tree in the order left, root, right
     */
    public void inOrder()
    {
        inOrder(this.root);
        System.out.println();
    }


    /**
     * Prints the given tree in the order left, root, right
     */
    public static <J extends Comparable<J>> void inOrder(Node<J> node)
    {
        if(node == null)
            return;

        inOrder(node.left);
        System.out.print(node.data + ", ");
        inOrder(node.right);
    }


    /**
     * Prints the tree in the order root, left, right...
     */
    public void preOrder()
    {
        preOrder(this.root);
        System.out.println();
    }


    /**
     * Prints the given tree in the order root, left, right...
     */
    public static <J extends Comparable<J>> void preOrder(Node<J> node)
    {
        if(node == null)
            return;

        System.out.print(node.data + ", ");
        preOrder(node.left);
        preOrder(node.right);
    }


    /**
     * Swaps the left and right nodes of the tree
     */
    public void invert()
    {
        invert(this.root);
    }


    /**
     * Swaps the left and right nodes of the given tree
     */
    public static <J extends Comparable<J>> void invert(Node<J> root)
    {
        if(root == null)
            return;

        Node<J> temp = root.left;
        root.left = root.right;
        root.right = temp;
        invert(root.left);
        invert(root.right);
    }


    /**
     * Returns the leftmost child node of the given node
     */
	public static <J extends Comparable<J>> Node<J> leftMost(Node<J> node)
	{
		if(node == null)
			return null;
		
		for(; node.left != null; node = node.left);
		
		return node;
	}
	
    /**
     * Returns the rightmost child node of the given node
     */
	public static <J extends Comparable<J>> Node<J> rightMost(Node<J> node)
	{
		if(node == null)
			return null;
		
		for(; node.right != null; node = node.right);
		
		return node;
	}
	
    /**
     * Returns the larger of a or b
     */
	public static <J extends Comparable<J>> J getMax(J a, J b)
	{
		if(a.compareTo(b) > 0)
			return a;
		return b;
	}
	
	/**
     * Returns the height of the given node
     */
	public static <J extends Comparable<J>> int getHeight(Node<J> node)
	{
		if(node == null)
			return 0;
		
		return node.height;
	}
	
    /**
     * Returns the balance factor of the given node
     */
	public static <J extends Comparable<J>> int getBalanceFactor(Node<J> node)
	{
		if(node == null)
			return 0;
		
		return getHeight(node.left) - getHeight(node.right);
	}
	
    /**
     * Rotates the tree node right  <br>
     *       [x]             [y]    <br>
     *       / \           /    \   <br>
     *     [y]      ->   [z]    [x] <br>
     *     /  \          / \    / \ <br>
     *   [z]   o               o    <br>
     */
	public static <J extends Comparable<J>> Node<J> rightRotate(Node<J> node)
    {
        // newRoot should become [y]
        Node<J> newRoot = node.left;

        // left node of [x] becomes right node of [y] -> o
        node.left = newRoot.right;

        // right node of [y] becomes [x]
        newRoot.right = node;

        // recalculate the height of [x]
        node.height = getMax(   getHeight(node.left),    getHeight(node.right)) + 1;

        // recalculate the height of [y]
        newRoot.height = getMax(getHeight(newRoot.left), getHeight(newRoot.right)) + 1;

        return newRoot;
    }
	
    
    /**
     * Rotates the tree node left <br>
     *   [x]             [y]      <br>
     *   / \           /    \     <br>
     *     [y]   ->  [x]    [z]   <br>
     *    /  \       / \    / \   <br>
     *   o    [z]       o         <br>
     */
    public static <J extends Comparable<J>> Node<J> leftRotate(Node<J> node)
    {
        // newRoot should become [y]
        Node<J> newRoot = node.right;

        // right node of [x] becomes the left node of [y] -> o
        node.right = newRoot.left;

        // left node of [y] becomes [x]
        newRoot.left = node;

        // recalculate the height of [x]
        node.height = getMax(getHeight(node.left), getHeight(node.right)) + 1;

        // recalculate the height of [y]
        newRoot.height = getMax(getHeight(newRoot.left), getHeight(newRoot.right)) + 1;

        return newRoot;
    }
}