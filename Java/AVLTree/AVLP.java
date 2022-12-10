

/**
 * AVLP Tree template class, must be comparable types to use <br>
 * 
 * This AVL Tree has each node keep track of the parent node
*/
public class AVLP <T extends Comparable<T>>
{
    public static class Node <K extends Comparable<K>>
    {
        Node<K> left;
        Node<K> right;
        Node<K> parent;
        int height;
        K data;
    
        public Node()
        {
            this.height = 1;
        }
    
        public Node(K data)
        {
            this.data = data;
            this.height = 1;
        }
    }

    private Node<T> root;

    /**
     * Inserts the given value into the tree
     */
    public void insert(T data)
    {
        // make a new root and we're done
        if(this.root == null)
        {
            this.root = new Node<T>(data);
            return;
        }

        Node<T> newNode;

        for(Node<T> node = this.root;;)
        {
            if(data.compareTo(node.data) < 0)
            {
                // add our new node and set the parent 
                // we then continue to the next for loop below
                if(node.left == null)
                {
                    newNode = new Node<T>(data);
                    newNode.parent = node;
                    node.left = newNode;
                    break;
                }

                // otherwise continue crawling 
                node = node.left;
            }
            else if(data.compareTo(node.data) > 0)
            {
                // add our new node and set the parent 
                // we then continue to the next for loop below
                if(node.right == null)
                {
                    newNode = new Node<T>(data);
                    newNode.parent = node;
                    node.right = newNode;
                    break;
                }

                // otherwise continue crawling 
                node = node.right;
            }
            else 
            {
                // we're not handling duplicates 
                return;
            }
        }

        
        // magic happens here,
        // loop back up the tree, and rotate where needed
        for(Node<T> node = newNode.parent, parent = node.parent;;)
        {
            // if the parent is null, `node` is this.root
        	final boolean IS_ROOT = parent == null;
            
        	final int BALANCE = getBalanceFactor(node);
        	
        	// so we know which child to set on the parent
            // if IS_ROOT is true, we don't care if it's left or right 
            final boolean IS_LEFT_NODE = IS_ROOT || parent.left == node;
            
            node.height = getMax(getHeight(node.right), getHeight(node.left)) + 1;

            Node<T> rotated;

            // handle out of balance tree
            if(BALANCE > 1)
            {
                if(data.compareTo(node.left.data) < 0)
                {
                    rotated = rightRotate(node);
                }
                else 
                {
                    node.left = leftRotate(node.left);
                    rotated = rightRotate(node);
                }
                
                // if this is our root node,
                // rotated should be our new root, and there should be no parent
                if(IS_ROOT)
                {
                	this.root = rotated;
                	this.root.parent = null;
                	return;
                }
                
                // otherwise, we need to set the parents child correctly
                if(IS_LEFT_NODE)
                    parent.left = rotated;
                else 
                    parent.right = rotated;
                
                // update our parent ref here
                rotated.parent = parent;
            } 
            
            // handle out of balance tree
            else if(BALANCE < -1)
            {
                if(data.compareTo(node.right.data) > 0)
                {
                    rotated = leftRotate(node);                    
                }
                else 
                {
                    node.right = rightRotate(node.right);
                    rotated =  leftRotate(node);
                }

                // if this is our root node,
                // rotated should be our new root, and there should be no parent
                if(IS_ROOT)
                {
                	this.root = rotated;
                	this.root.parent = null;
                	return;
                }
                
                // otherwise, we need to set the parents child correctly
                if(IS_LEFT_NODE)
                    parent.left = rotated;
                else 
                    parent.right = rotated;
                
                // update our parent ref here
                rotated.parent = parent;
            }
            
            // in case the root is not out of balance, can't loop forever
            else if(IS_ROOT)
            {
            	this.root.parent = null;
            	return;
            }
            
            // continue the crawl up
            node = parent;
            parent = node.parent;
        }
    }


    /**
     * Deletes the given value from the tree
     */
    public void delete(T data)
    {
        if(this.root == null)
        {
            return;
        }

        Node<T> node = this.root;
        for(;;)
        {
            if(data.compareTo(node.data) < 0)
            {
                // item not in tree, we're done
                if(node.left == null)
                {
                    return;
                }

                // otherwise continue crawling 
                node = node.left;
            }
            else if(data.compareTo(node.data) > 0)
            {
                // item not in tree, we're done
                if(node.right == null)
                {
                    return;
                }

                // otherwise continue crawling 
                node = node.right;
            }
            else 
            {
                // if the node has 1 or 0 children, special case
                if(node.left == null || node.right == null)
                {
                    Node<T> temp = node.left;

                    // left node is null, use right
                    if(temp == null)
                    {
                        temp = node.right;
                    }

                    // has a single child
                    // swap childs data and remove the child, easy
                    if(temp != null)
                    {
                        node.data = temp.data;
                        temp.parent = null;
                        node.left = null;
                        node.right = null;
                    }
                    // has no children, use parent ref to remove the node
                    else 
                    {
                        if(node == node.parent.left)
                        {
                            node.parent.left = null;
                        }
                        else 
                        {
                            node.parent.right = null;
                        }
                    }
                    
                    // jump to our rebalance loop at the bottom
                    break;
                }
                else 
                {
                    // get the next node larger than the current node
                    Node<T> temp = leftMost(node.right);
                    
                    // override the current value with the next value
                    node.data = temp.data;

                    // set the new remove data to the leftmost child node
                    data = node.data;

                    // jump the current node right to that point
                    node = temp;
                }
            }
        }

        // magic happens here,
        // loop back up the tree, and rotate where needed
        for(Node<T> parent = node.parent;;)
        {
            // if the parent is null, `node` is this.root
        	final boolean IS_ROOT = parent == null;
            
        	final int BALANCE = getBalanceFactor(node);
        	
        	// so we know which child to set on the parent
            // if IS_ROOT is true, we don't care if it's left or right 
            final boolean IS_LEFT_NODE = IS_ROOT || parent.left == node;
            
            node.height = getMax(getHeight(node.right), getHeight(node.left)) + 1;

            Node<T> rotated;

            // handle out of balance tree
            if(BALANCE > 1)
            {
                if(data.compareTo(node.left.data) < 0)
                {
                    rotated = rightRotate(node);
                }
                else 
                {
                    node.left = leftRotate(node.left);
                    rotated = rightRotate(node);
                }
                
                // if this is our root node,
                // rotated should be our new root, and there should be no parent
                if(IS_ROOT)
                {
                	this.root = rotated;
                	this.root.parent = null;
                	return;
                }
                
                // otherwise, we need to set the parents child correctly
                if(IS_LEFT_NODE)
                    parent.left = rotated;
                else 
                    parent.right = rotated;
                
                // update our parent ref here
                rotated.parent = parent;
            } 
            
            // handle out of balance tree
            else if(BALANCE < -1)
            {
                if(data.compareTo(node.right.data) > 0)
                {
                    rotated = leftRotate(node);                    
                }
                else 
                {
                    node.right = rightRotate(node.right);
                    rotated =  leftRotate(node);
                }

                // if this is our root node,
                // rotated should be our new root, and there should be no parent
                if(IS_ROOT)
                {
                	this.root = rotated;
                	this.root.parent = null;
                	return;
                }
                
                // otherwise, we need to set the parents child correctly
                if(IS_LEFT_NODE)
                    parent.left = rotated;
                else 
                    parent.right = rotated;
                
                // update our parent ref here
                rotated.parent = parent;
            }
            
            // in case the root is not out of balance, can't loop forever
            else if(IS_ROOT)
            {
            	this.root.parent = null;
            	return;
            }
            
            // continue the crawl up
            node = parent;
            parent = node.parent;
        }
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
		
        Node<J> node = root;

		for(;;)
		{
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

		// this is worst case for this function

		for(node = node.parent;;) 
		{
			// we're at the rightmost node in the tree 
            // we can't put this in the forloop for some reason?
			if(node.parent == null)
				return null;

			// need to keep looking up at the parent nodes until we find where we're on the left
			// if we can't find a spot we're on the left, we have the rightmost node in the tree
			if(node == node.parent.left)
			{
				return node.parent;
			}
			
            node = node.parent;
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

        Node<J> node = root;

        for(;;)
        {
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

        // this is worst case for this function

        for(node = node.parent;;)
        {
            // we're at the rightmost node in the tree 
            if(node.parent == null)
                return null;

            // need to keep looking up at the parent nodes until we find where we're on the right
            // if we can't find a spot we're on the left, we have the leftmost node in the tree
            // if(fakeLinkedList.node.data.equals(fakeLinkedList.prev.node.right.data))
            if(node == node.parent.right)
            {
                return node.parent;
            }

            node = node.parent;
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
        node.height = getMax(getHeight(node.left), getHeight(node.right)) + 1;

        // recalculate the height of [y]
        newRoot.height = getMax(getHeight(newRoot.left), getHeight(newRoot.right)) + 1;
        
        // update the parent ref here
        node.parent = newRoot;

        // there has to be a better way of doing this 
        if(newRoot.left != null && newRoot.left.right != null)
            newRoot.left.right.parent = newRoot.left;

        if(newRoot.right != null && newRoot.right.left != null)
            newRoot.right.left.parent = newRoot.right;

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

        // update the parent ref here
        node.parent = newRoot;

        // there has to be a better way of doing this 
        if(newRoot.right != null && newRoot.right.left != null)
            newRoot.right.left.parent = newRoot.right;
        
        if(newRoot.left != null && newRoot.left.right != null)
            newRoot.left.right.parent = newRoot.left;
        
        return newRoot;
    }
}