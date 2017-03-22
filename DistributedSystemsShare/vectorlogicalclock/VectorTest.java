/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vectorlogicalclock;

/**
 *
 * @author Ian Gortan
 */
public class VectorTest {
    
    public static void main(String[] args) {
        System.out.println("starting ....");
        MessageBuffer buffer = new MessageBuffer(10);
        (new Thread(new VectorClient(buffer, 0))).start();
        (new Thread(new VectorClient(buffer, 1))).start();
        (new Thread(new VectorClient(buffer, 2))).start();
      }
}
    

