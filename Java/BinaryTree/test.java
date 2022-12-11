
public class test 
{
    public static void main(String[] args) 
    {
        BinarySearchTree bst = new BinarySearchTree();
        BinarySearchTree bst1 = new BinarySearchTree();
        thing(bst);
        thing1(bst1);
        
    }   
    public static void thing1(BinarySearchTree bst)
    {
        bst.insert(5);
        bst.deleteRecur(5);
        bst.insert(10);
        bst.insert(15);
        bst.insert(2);
        bst.insert(1);
        bst.deleteRecur(5);
        bst.deleteRecur(2);
        bst.deleteRecur(15);
        bst.insert(5);
        bst.insert(12);
        bst.insert(22);
        bst.insert(42);
        bst.deleteRecur(12);
        bst.inOrder();
    }
    public static void thing(BinarySearchTree bst)
    {
        bst.insert(5);
        bst.deleteIter(5);
        bst.insert(10);
        bst.insert(15);
        bst.insert(2);
        bst.insert(1);
        bst.deleteIter(5);
        bst.deleteIter(2);
        bst.deleteIter(15);
        bst.insert(5);
        bst.insert(12);
        bst.insert(22);
        bst.insert(42);
        bst.deleteIter(12);
        bst.insert(32);
        bst.deleteIter(1);
        bst.insert(25);
        bst.insert(21);
        bst.deleteIter(5);
        bst.insert(22);
        bst.inOrder();
    }
}
