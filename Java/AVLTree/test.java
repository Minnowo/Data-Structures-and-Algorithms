public class test 
{
    public static void main(String[] args) 
    {
        AVL<Integer> tree1 = new AVL<Integer>();

        tree1.insert(2);
        tree1.insert(3);
        tree1.insert(1);
        tree1.insert(7);
        tree1.insert(5);
        tree1.insert(4);
        tree1.insert(6);
        tree1.insert(10);
        tree1.insert(9);
        tree1.insert(8);

        System.out.println("AVL tree in order:");
        tree1.inOrder();

        System.out.println("\nAVL tree pre order:");
        tree1.preOrder();

        System.out.println("\nremoving 4 and 7");
        tree1.delete(4);
        tree1.delete(7);

        System.out.println("\nAVL tree in order:");
        tree1.inOrder();

        System.out.println();




        AVLP<Integer> tree2 = new AVLP<Integer>();

        tree2.insert(2);
        tree2.insert(3);
        tree2.insert(1);
        tree2.insert(7);
        tree2.insert(5);
        tree2.insert(4);
        tree2.insert(6);
        tree2.insert(10);
        tree2.insert(9);
        tree2.insert(8);

        System.out.println("\nAVLP tree in order:");
        tree2.inOrder();

        System.out.println("\nAVLP tree pre order:");
        tree2.preOrder();

        System.out.println("\nremoving 4 and 7");
        tree2.delete(4);
        tree2.delete(7);

        System.out.println("\nAVLP tree in order:");
        tree2.inOrder();
    }    
}
