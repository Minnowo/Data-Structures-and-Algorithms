
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

    /**
     * A simple linked list used during successor and predecessor
     */
    public static class SimpleDoublyLinkedList<K> 
    {
        public SimpleDoublyLinkedList<K>  next;
        public SimpleDoublyLinkedList<K>  prev;
        public K node;
        
        public SimpleDoublyLinkedList()
        {
            
        }
        
        public SimpleDoublyLinkedList(K node)
        {
            this.node = node;
        }
        
        /**
         * sets this link's next pointer to a new @SimpleDoublyLinkedList 
         */
        public SimpleDoublyLinkedList<K> createNext()
        {
            this.next = new SimpleDoublyLinkedList<K>();
            this.next.prev = this;
            return this.next;
        }
        
        /**
         * sets this link's next pointer to a new @SimpleDoublyLinkedList with the given node
         */
        public SimpleDoublyLinkedList<K> createNext(K node)
        {
            this.next = new SimpleDoublyLinkedList<K>(node);
            this.next.prev = this;
            return this.next;
        }
        
        public String toString()
        {
            return this.node.toString();
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
     * Returns the value to the right (in order) of the given value
     */
    public T inOrderSuccessor(T value)
    {
        if(value == null)
            return null;
    
        Node<T> val = InOrderSuccessor(this.root, value);
        
        if(val == null)
            return null;
        
        return val.data;
    }


    /**
     * Returns the value to the right (in order) of the given value in the given tree
     */
    public static <J extends Comparable<J>> Node<J> InOrderSuccessor(Node<J> root, J value)
	{
		if(root == null)
			return null;
		
		if(root.data.compareTo(value) == 0)
			return leftMost(root.right);
		
		// this acts like a stack if a certain case appears 
		SimpleDoublyLinkedList<Node<J>> fakeLinkedList = new SimpleDoublyLinkedList<>(); 

		for(Node<J> node = root;;)
		{
			fakeLinkedList.node = node;
			fakeLinkedList.createNext();
			fakeLinkedList = fakeLinkedList.next;
			
			if(value.compareTo(node.data) < 0)
			{
				if(node.left == null)
					return null;
				
				// the current nodes left node is what we're looking for, easy 
				// Case 1, the current node is the parent of a left child
				if(node.left.data.equals(value))
				{
					// if the left child has no right nodes, return the parent
					if(node.left.right == null)
					{
						return node;
					}
					
					// otherwise we can get the leftmost right node
					return leftMost(node.left.right);
				}
				
				node = node.left;
			}
			else if(value.compareTo(node.data) > 0)
			{
				if(node.right == null)
					return null;
				
				// Case 2, the current node has a right child, with a right child
				// get it's leftmost child and we're done
				if(node.right.right != null && node.right.data.equals(value))
				{
					return leftMost(node.right.right);
				}
				
				node = node.right;
			}
			else 
			{
				// worst case here
				break;
			}
		}

		// this is worst case for this function, since it's more steps and ram usage by the linked list

		// remove empty last node, created at the start of the loop above
		fakeLinkedList = fakeLinkedList.prev;
		fakeLinkedList.next = null;
		
		// remove current node we found, this node we were looking for 
		fakeLinkedList = fakeLinkedList.prev;
		fakeLinkedList.next = null;
		
	
		for(;;)
		{
			// we're at the rightmost node in the tree 
			if(fakeLinkedList.prev == null)
				return null;

			// need to keep looking up at the parent nodes until we find where we're on the left
			// if we can't find a spot we're on the left, we have the rightmost node in the tree
			if(fakeLinkedList.node == fakeLinkedList.prev.node.left)
			{
				return fakeLinkedList.prev.node;
			}
			
			fakeLinkedList = fakeLinkedList.prev;
			fakeLinkedList.next = null;
		}
	}
	

    /**
     * Returns the value to the left (in order) of the given value
     */
    public T inOrderPredecessor(T value)
    {
        if(value == null)
            return null;
    
        Node<T> val = inOrderPredecessor(this.root, value);
        
        if(val == null)
            return null;
        
        return val.data;
    }


    /**
     * Returns the value to the left (in order) of the given value in the given tree
     */
    public static <J extends Comparable<J>> Node<J> inOrderPredecessor(Node<J> root, J value)
	{
        if(root == null)
            return null;

        if(root.data.equals(value))
            return rightMost(root.left);

        // this acts like a stack if a certain case appears 
        SimpleDoublyLinkedList<Node<J>> fakeLinkedList = new SimpleDoublyLinkedList<>(); 

        for(Node<J> node = root;;)
        {
            fakeLinkedList.node = node;
            fakeLinkedList.createNext();
            fakeLinkedList = fakeLinkedList.next;

            if(value.compareTo(node.data) < 0)
            {
                if(node.left == null)
                    return null;
                
                // Case 2, the current node has a left child, with a left child
                // get it's rightmost child and we're done
                if(node.left.left != null && node.left.data.equals(value))
                {
                    return rightMost(node.left.left);
                }
                

                node = node.left;
            }
            else if(value.compareTo(node.data) > 0)
            {
                if(node.right == null)
                    return null;
                
                // the current nodes right node is what we're looking for, easy 
                // Case 1, the current node is the parent of a right child
                if(node.right.data.equals(value))
                {
                    // if the right child has no left nodes, return the parent
                    if(node.right.left == null)
                    {
                        return node;
                    }
                    
                    // otherwise we can get the rightmost left node
                    return rightMost(node.right.left);
                }
                
                node = node.right;
            }
            else 
            {
                // worst case here
                break;
            }
        }

        // this is worst case for this function, since it's more steps and ram usage by the linked list

        // remove empty last node, created at the start of the loop above
        fakeLinkedList = fakeLinkedList.prev;
        fakeLinkedList.next = null;

        // remove current node we found, this node we were looking for 
        fakeLinkedList = fakeLinkedList.prev;
        fakeLinkedList.next = null;


        for(;;)
        {
            // we're at the rightmost node in the tree 
            if(fakeLinkedList.prev == null)
                return null;

            // need to keep looking up at the parent nodes until we find where we're on the right
            // if we can't find a spot we're on the left, we have the leftmost node in the tree
            // if(fakeLinkedList.node.data.equals(fakeLinkedList.prev.node.right.data))
            if(fakeLinkedList.node == fakeLinkedList.prev.node.right)
            {
                return fakeLinkedList.prev.node;
            }

            fakeLinkedList = fakeLinkedList.prev;
            fakeLinkedList.next = null;
        }
	}


    /**
     * Returns the leftmost value in the tree
     */
    public T leftMost()
    {
        if(this.root == null)
            return null;

        return leftMost(this.root).data;
    }


    /**
     * Returns the rightmost value in the tree
     */
    public T rightMost()
    {
        if(this.root == null)
            return null;

        return rightMost(this.root).data;
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